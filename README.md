<div align="center">

# 🏦 MiniBank

### A fault-tolerant, multi-threaded console banking system written in pure Java

*Built practical by practical, finished as one clean, tested and runnable application.*

**Java 17+** · **No external libraries** · **Layered architecture** · **JUnit 5 tests** · **Runnable JAR**

</div>

---

## 📑 Table of Contents

1. [Overview](#-overview)
2. [Key Features](#-key-features)
3. [Architecture](#-architecture)
4. [Project Structure](#-project-structure)
5. [Getting Started](#-getting-started)
6. [Using the Application](#-using-the-application)
7. [Business Rules](#-business-rules)
8. [Data Files](#-data-files)
9. [Testing](#-testing)
10. [Java Concepts Demonstrated](#-java-concepts-demonstrated)
11. [Design Decisions](#-design-decisions)
12. [Troubleshooting](#-troubleshooting)
13. [Known Limitations](#-known-limitations)
14. [Future Improvements](#-future-improvements)
15. [Author](#-author)

---

## 📖 Overview

**MiniBank** is a menu-driven banking application that runs in the terminal. Customers can open
Savings, Current and Fixed Deposit accounts, deposit, withdraw and transfer money, view statements,
process a whole batch of transactions on a thread pool, and generate an end-of-day report.

The project started as a plain array of accounts and grew, one lab at a time, into a complete
application: abstract classes and interfaces, packages, annotations and reflection, custom exceptions,
multithreading, serialization and NIO file handling, the Collections Framework, generics and finally a
service layer that ties everything together.

> **Why it is interesting:** every failure is an explicit exception, every shared balance is
> thread-safe, every service call can be audited through a reflective proxy, and the state of the bank
> survives a restart.

---

## ✨ Key Features

| Area | What you get |
|---|---|
| 🏧 **Accounts** | Savings (4 % interest, minimum balance), Current (overdraft), Fixed Deposit (locked, 7 % interest) |
| 💸 **Operations** | Open account, deposit, withdraw, transfer, statement, list accounts |
| 🛡️ **Validation** | Regex validation for mobile, e-mail, PAN and IFSC; annotation-based validation by reflection |
| ⚠️ **Error handling** | Checked exception hierarchy; `InsufficientFundsException` carries the exact **shortfall** |
| 🔒 **Thread safety** | `synchronized` deposit and withdraw; `ConcurrentHashMap` inside the transaction processor |
| ⚙️ **Batch engine** | Fixed thread pool of 4 workers processes many transactions safely |
| 🧵 **Concurrency labs** | Race condition, producer-consumer with `wait/notify`, deadlock and its fix |
| 💾 **Persistence** | Accounts are saved on exit and loaded on start-up (Java serialization) |
| 🧾 **Logging** | Append-only daily transaction log written with NIO |
| 📊 **Reporting** | End-of-day report: total deposits, withdrawals, file sizes and modified times |
| 🕵️ **Audit trail** | `@Audit` + dynamic proxy records which service method was called, with arguments and result |
| 🧪 **Tests** | JUnit 5 tests including a concurrent-deposit test |
| 📦 **Delivery** | Runnable JAR with `Main-Class` manifest |

---

## 🏗️ Architecture

The menu never touches data directly. Each layer has one job.

```
   ┌──────────────────────────────────────────────────────────┐
   │  MiniBank (menu)             console input and output    │
   └───────────────────────────┬──────────────────────────────┘
                               │ calls the BankOperations interface
   ┌───────────────────────────▼──────────────────────────────┐
   │  AuditProxy                  reads @Audit by reflection  │
   │                              and records each call       │
   └───────────────────────────┬──────────────────────────────┘
                               │
   ┌───────────────────────────▼──────────────────────────────┐
   │  BankService                 validation, business rules  │
   │                              exceptions, logging         │
   └──────┬──────────────┬──────────────┬─────────────────────┘
          │              │              │
   ┌──────▼─────┐  ┌─────▼──────┐  ┌────▼───────────────┐
   │ Repository │  │ Transaction│  │ StatePersister     │
   │ <Account>  │  │ Processor  │  │ TransactionLog     │
   │ (@Id key)  │  │ (4 threads)│  │ ReportGenerator    │
   └──────┬─────┘  └────────────┘  └────────────────────┘
          │
   ┌──────▼──────────────────────────────────────────────────┐
   │  Account  ─┬─ SavingsAccount                            │
   │            ├─ CurrentAccount                            │
   │            └─ FixedDepositAccount                       │
   └─────────────────────────────────────────────────────────┘
```

**Request flow for a deposit:**
`Menu` → `AuditProxy` (records the call) → `BankService.deposit` → `Repository.findById` →
`Account.deposit` (synchronized) → `TransactionLog.append`.

---

## 📂 Project Structure

```
Miniproject/
├── README.md
├── manifest.txt                      Main-Class: service.MiniBank
├── MiniBank.jar                      runnable JAR (after you build it)
└── src/
    ├── exception/
    │   ├── BankException.java                 checked base exception
    │   ├── InsufficientFundsException.java    carries the shortfall
    │   ├── AccountNotFoundException.java
    │   └── InvalidAmountException.java
    ├── model/
    │   ├── Account.java                       abstract, Comparable, Serializable
    │   ├── SavingsAccount.java
    │   ├── CurrentAccount.java
    │   ├── FixedDepositAccount.java           also marked Premium
    │   ├── Customer.java                      Cloneable, Serializable, nested Address
    │   ├── Command.java                       record (type, accountNumber, amount)
    │   ├── TransactionType.java               enum DEPOSIT, WITHDRAW, TRANSFER
    │   ├── Transactable.java                  interface
    │   ├── InterestBearing.java               interface with a default method
    │   ├── WithdrawRule.java                  functional interface
    │   ├── Premium.java                       marker interface
    │   └── annotation/
    │       ├── Id.java   Positive.java   MaxLength.java   Audit.java
    ├── util/
    │   ├── Validator.java                     mobile, e-mail, PAN, IFSC (regex)
    │   ├── CommandParser.java                 "DEPOSIT AC0001 500" -> Command
    │   ├── StatementFormatter.java
    │   ├── AnnotationValidator.java           reflection-based validation
    │   └── TransactionLogger.java             AutoCloseable (try-with-resources)
    ├── service/
    │   ├── MiniBank.java                      the menu and main method
    │   ├── BankOperations.java                service interface (proxied)
    │   ├── BankService.java                   openAccount, deposit, withdraw, ...
    │   ├── AuditProxy.java                    dynamic proxy + audit trail
    │   ├── Repository.java                    generic repository, finds @Id by reflection
    │   ├── AccountStore.java                  HashMap store with history and sorting
    │   ├── TransactionProcessor.java          thread pool + ConcurrentHashMap
    │   ├── TransactionTask.java  AccountWorker.java
    │   ├── TransactionBuffer.java  TransactionProducer.java  TransactionConsumer.java
    │   ├── TransferDemo.java                  deadlock and its fix
    │   ├── StatePersister.java                save / load accounts
    │   ├── TransactionLog.java                append-only log
    │   ├── ReportGenerator.java               end-of-day report
    │   └── PracticalDemo.java                 old demo of every practical
    └── test/
        └── BankServiceTest.java               JUnit 5 tests
```

---

## 🚀 Getting Started

### Requirements

- **JDK 17 or newer** (developed and tested with JDK 21 and 26)
- A terminal opened in the `Miniproject` folder, the folder that contains `src`

Check your JDK:

```
java -version
javac -version
```

### Compile and run

```
javac -d out -sourcepath src src\service\MiniBank.java
java -cp out service.MiniBank
```

### Build the runnable JAR

```
jar cfm MiniBank.jar manifest.txt -C out .
java -jar MiniBank.jar
```

`manifest.txt` contains exactly one line and **must end with an empty line**:

```
Main-Class: service.MiniBank
```

### Run the old practical demo (optional)

Shows the output of every practical one after another (about 10 seconds):

```
javac -d out -sourcepath src src\service\PracticalDemo.java
java -cp out service.PracticalDemo
```

---

## 🖥️ Using the Application

### Menu

```
-------------------- MiniBank --------------------
1. Open Account
2. Deposit
3. Withdraw
4. Transfer
5. Statement
6. List Accounts
7. Process Batch
8. End-of-Day Report
9. Audit Trail
0. Save and Exit
```

| Option | Description |
|---|---|
| **1** | Choose the type (Savings, Current, Fixed Deposit), then owner name, mobile number and opening balance |
| **2 / 3** | Account number and amount. A failed withdrawal tells you how much is missing |
| **4** | From account, to account, amount. A failed transfer is rolled back |
| **5** | Prints a statement with balance, interest rate and yearly interest |
| **6** | Lists all accounts sorted by account number |
| **7** | Type lines like `DEPOSIT AC0001 500`, finish with an empty line. Empty input runs a sample of 30 deposits |
| **8** | Reads every `.log` file in `logs/` and prints totals |
| **9** | Shows the last 10 entries of the audit trail |
| **0** | Saves all accounts to disk and exits |

### Example session

```
No saved accounts found, starting fresh.

Enter your choice : 1
1. Savings   2. Current   3. Fixed Deposit
Account type : 1
Owner name (max 10 characters) : Amit
Mobile number : 9876543210
Opening balance : 5000
Account opened : Account Number : AC0001, Owner Name : Amit, Balance : 5000

Enter your choice : 3
Account number : AC0001
Amount : 4500
Operation failed : Insufficient funds in AC0001, short by 500

Enter your choice : 7
> DEPOSIT AC0001 250
> WITHDRAW AC0001 100
>
Batch finished : 2 succeeded, 0 failed. Total balance 5000 -> 5150 (net change 150)
```

### Example statement

```
----- Account Statement -----
Account Number : AC0001
Owner Name     : Amit
Balance        : 5150
Interest Rate  : 4.0%
Yearly Interest: 206.0
Active         : true
```

### Example audit trail

```
2026-10-30 10:15:02 | openAccount | Open a new account | args=[SAVINGS, Amit, 9876543210, 5000] | OK
2026-10-30 10:15:20 | withdraw | Withdraw money | args=[AC0001, 4500] | FAILED InsufficientFundsException : Insufficient funds in AC0001, short by 500
```

---

## 📏 Business Rules

| Rule | Value |
|---|---|
| Account number format | `AC` + 4 digits, for example `AC0001` |
| Owner name | Not empty, at most 10 characters (`@MaxLength(10)`) |
| Mobile number | 10 digits starting with 6, 7, 8 or 9 |
| Opening balance | Greater than zero (`@Positive`); a Savings account needs at least 1000 |
| Savings account | 4 % interest, minimum balance 1000 must stay |
| Current account | 0 % interest, overdraft up to 2000 |
| Fixed Deposit | 7 % interest, **withdrawals are locked** |
| Deposit / withdraw / transfer amount | Must be greater than zero |
| Transfer | Source and destination must differ; if it fails midway the balance is restored |
| Shortfall | Amount missing when a withdrawal is refused |

---

## 💾 Data Files

Created automatically in the folder where you run the program:

| Path | Content |
|---|---|
| `data/bank.dat` | Saved accounts (binary), written on exit and read on start-up |
| `logs/transactions-<date>.log` | One line per successful deposit or withdrawal: `date time,TYPE,ACCOUNT,AMOUNT` |
| `reports/end-of-day.txt` | Latest end-of-day report |
| `audit/audit-trail.txt` | Audited service calls with arguments and outcome |

Delete these four folders to start with a completely clean bank.

Example log line:

```
2026-10-30 10:16:41,DEPOSIT,AC0001,250
```

---

## 🧪 Testing

The tests use **JUnit 5**. They live in `src/test/BankServiceTest.java`.

| Test | What it proves |
|---|---|
| `depositIncreasesBalance` | A deposit adds exactly the amount to the balance |
| `withdrawalOverTheLimitThrowsException` | An over-limit withdrawal throws `InsufficientFundsException` with the right shortfall and leaves the balance unchanged |
| `manyConcurrentDepositsKeepBalanceCorrect` | 10 threads x 100 deposits give exactly 1000 more; no update is lost |
| `transferMovesMoneyBetweenAccounts` | Money leaves one account and arrives in the other |
| `invalidMobileNumberIsRejected` | A wrong mobile number stops the account from being opened |

### How to run the tests

1. Create a folder `lib` inside `Miniproject`.
2. Download `junit-platform-console-standalone-1.10.2.jar` from Maven Central
   (`https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/`)
   and put it in `lib`.
3. Run these commands from `Miniproject`:

```
javac -d out -sourcepath src src\service\MiniBank.java
javac -cp "out;lib\junit-platform-console-standalone-1.10.2.jar" -d testout src\test\BankServiceTest.java
java -jar lib\junit-platform-console-standalone-1.10.2.jar execute --class-path "out;testout" --select-class test.BankServiceTest
```

Expected result: all 5 tests successful. (On Linux or macOS use `:` instead of `;` and `/` instead of `\`.)

---

## 🎓 Java Concepts Demonstrated

| Practical | Topic | Where to look |
|---|---|---|
| 1 to 5 | Classes, encapsulation, inheritance, abstract classes, polymorphism, `equals`/`hashCode`, `clone`, nested classes, records, enums, regex validation | `model/`, `util/Validator`, `util/CommandParser` |
| 6 | Interfaces (`default`, functional, marker), packages, static import, runnable JAR | `Transactable`, `InterestBearing`, `WithdrawRule`, `Premium`, `manifest.txt` |
| 7 | Custom annotations and reflection | `model/annotation/`, `util/AnnotationValidator` |
| 8 | Custom checked exceptions, `try / catch / finally`, try-with-resources | `exception/`, `Account.transfer`, `util/TransactionLogger` |
| 9 | Threads, race condition, `synchronized`, `join`, thread states | `Account.deposit`, `service/AccountWorker` |
| 10 | Thread pool, `wait / notify`, deadlock and lock ordering | `TransactionProcessor`, `TransactionBuffer`, `TransferDemo` |
| 11 | Serialization, NIO `Files`, `DirectoryStream`, `BasicFileAttributes` | `StatePersister`, `TransactionLog`, `ReportGenerator` |
| 12 | Collections: `HashMap`, `ArrayList`, `Comparable`, `Comparator`, `ConcurrentHashMap` | `AccountStore`, `Account.compareTo` |
| 13 | Generics, bounded types, `@SafeVarargs`, reflection on `@Id` | `Repository<T>` |
| 14 (capstone) | Service layer, dynamic proxy, audit, JUnit, packaging | `BankService`, `AuditProxy`, `BankServiceTest` |

---

## 🧠 Design Decisions

- **One service, one interface.** The menu only knows `BankOperations`. That is what allows the
  `AuditProxy` to sit in between without changing a single line of the service.
- **Exceptions instead of `false`.** A refused withdrawal tells the caller exactly why and by how much.
- **Reflection on `@Id`.** `Repository<T>` has no idea what an `Account` is. It finds the key field by
  annotation, so the same class stores accounts, customers or anything else with an `@Id`.
- **Deadlock avoided by lock ordering.** Whenever two accounts must be locked, the one with the smaller
  account number is locked first, so a circular wait can never form.
- **Account numbers survive a restart.** After loading, the counter continues from the highest saved
  number, so a new account can never overwrite an old one.
- **Safe batches.** `TransactionProcessor` keeps its accounts in a `ConcurrentHashMap` and counts
  successes and failures with thread-safe structures; the service logs only successful transactions.

---

## 🛠️ Troubleshooting

| Problem | Cause and fix |
|---|---|
| `package model does not exist` | You compiled a single file. Always compile from `Miniproject` with `-sourcepath src` as shown above. Do **not** use the editor's Run button |
| `no main manifest attribute` | `manifest.txt` is missing its last empty line. Press Enter after `Main-Class: service.MiniBank`, save, rebuild the JAR |
| `MiniBank.jar ... being used by another process` | The program is still running. Exit with `0`, then build the JAR again |
| `Account not found` | Account numbers have **four** digits: `AC0001`, not `AC001` |
| `bad source file ... does not contain class` | A file has the wrong name or package line. Delete the `out` folder (`rmdir /s /q out`) and recompile |
| Red underlines in `BankServiceTest.java` | The JUnit jar is not on the editor's classpath. It does not affect the application build |
| Old demo data appears in the report | Delete the `logs` folder and run again |

---

## ⚠️ Known Limitations

- The mobile number is validated but **not stored**, because `Account` has no mobile field.
- Accounts are saved only on a normal exit (option `0`). A crash loses the changes of that session.
- A transfer is written to the log as one `WITHDRAW` and one `DEPOSIT` line.
- `Repository` uses a plain `HashMap`; `BankService.openAccount` is synchronized for that reason.
- There is no login, password or user roles.
- Batch processing supports `DEPOSIT` and `WITHDRAW`; `TRANSFER` needs two accounts and is rejected.

---

## 🔭 Future Improvements

- Store customer details (name, mobile, e-mail, PAN) and link them to accounts
- Replace file serialization with a database (JDBC or SQLite)
- Add authentication and roles (customer, teller, manager)
- Interest calculation scheduled at month end
- A REST API or a JavaFX front end on top of `BankOperations`
- Maven or Gradle build with continuous testing

---

## 👤 Author

| | |
|---|---|
| **Name** | Heni Patel |
| **Enrollment No.** | 25CS061 |
| **Course** | Object Oriented Programming with Java |
| **Institute** | CHARUSAT University (CSPIT) |

---

<div align="center">

**MiniBank** · Built with ☕ Java · Milestone 3 Capstone

</div>
