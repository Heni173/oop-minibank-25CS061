<div align="center">

# 🏦 MiniBank

### A fault-tolerant, multi-threaded console banking system in pure Java

*Started as an array of accounts. Finished as a layered, tested and runnable application.*

`Java 17+` &nbsp;·&nbsp; `Layered architecture` &nbsp;·&nbsp; `Thread-safe` &nbsp;·&nbsp; `Reflection and annotations` &nbsp;·&nbsp; `JUnit 5` &nbsp;·&nbsp; `Runnable JAR`

</div>

---

## At a Glance

| | |
|---|---|
| **Language** | Java 17 or newer |
| **Application dependencies** | None (JUnit is needed only to run the tests) |
| **Size** | 40+ Java files, 3,500+ lines, 6 packages |
| **Account types** | Savings, Current, Fixed Deposit |
| **Menu operations** | 9 (open, deposit, withdraw, transfer, statement, list, batch, report, audit) |
| **Unit tests** | 5 JUnit 5 tests, including a concurrent-deposit test |
| **Persistence** | Accounts saved on exit and loaded on start-up |
| **Delivery** | Runnable JAR (`Main-Class: service.MiniBank`) |

## Table of Contents

- 📖 [Overview](#overview)
- ✨ [Highlights](#highlights)
- ⚡ [Quick Start](#quick-start)
- 🏗️ [Architecture](#architecture)
- 📂 [Project Structure](#project-structure)
- 🧩 [Class Reference](#class-reference)
- 🖥️ [Using the Application](#using-the-application)
- 📏 [Business Rules](#business-rules)
- 🔒 [Concurrency Model](#concurrency-model)
- 💾 [Data Files](#data-files)
- 🧪 [Testing](#testing)
- 🎓 [Practical Roadmap](#practical-roadmap)
- 🧠 [Design Decisions](#design-decisions)
- 🛠️ [Troubleshooting](#troubleshooting)
- ⚠️ [Known Limitations](#known-limitations)
- 🔭 [Future Improvements](#future-improvements)
- 👤 [Author](#author)

---

## Overview

**MiniBank** is a menu-driven banking application that runs in the terminal. A teller can open
Savings, Current and Fixed Deposit accounts, deposit, withdraw and transfer money, print statements,
process a whole batch of transactions on a thread pool, and produce an end-of-day report.

The project grew one lab at a time: abstract classes and interfaces, packages, annotations and
reflection, custom exceptions, multithreading, serialization and NIO file handling, the Collections
Framework and generics. The final capstone adds a **service layer** that ties everything together
behind one menu.

> **What makes it solid:** every failure is an explicit exception that explains itself, every shared
> balance is thread-safe, every service call can be recorded in an audit trail, and the state of the
> bank survives a restart.

## Highlights

| Area | What you get |
|---|---|
| 🏧 **Accounts** | Savings (4 % interest, minimum balance), Current (overdraft), Fixed Deposit (7 % interest, locked) |
| 💸 **Operations** | Open account, deposit, withdraw, transfer with rollback, statement, list accounts |
| 🛡️ **Validation** | Regex `Validator` for mobile, e-mail, PAN and IFSC; annotation rules (`@Positive`, `@MaxLength`) checked by reflection |
| ⚠️ **Error handling** | Checked exception hierarchy; `InsufficientFundsException` carries the exact **shortfall** |
| 🔒 **Thread safety** | `synchronized` deposit and withdraw, `ConcurrentHashMap`, atomic counters, lock ordering |
| ⚙️ **Batch engine** | Fixed thread pool of 4 workers processes many transactions safely |
| 🧵 **Concurrency labs** | Race condition, producer-consumer with `wait / notify`, deadlock and its fix |
| 💾 **Persistence** | Java serialization; account numbers continue after the highest saved number |
| 🧾 **Logging** | Append-only daily transaction log written with NIO |
| 📊 **Reporting** | End-of-day report: deposits, withdrawals, file sizes and last-modified times |
| 🕵️ **Audit trail** | `@Audit` plus a dynamic proxy records which service method was called, with arguments and result |
| 🧪 **Quality** | JUnit 5 tests and a runnable JAR |

---

## Quick Start

You need **JDK 17 or newer**. Open a terminal in the `Miniproject` folder (the folder that contains `src`).

```
javac -d out -sourcepath src src\service\MiniBank.java
java -cp out service.MiniBank
```

Build and run the JAR:

```
jar cfm MiniBank.jar manifest.txt -C out .
java -jar MiniBank.jar
```

`manifest.txt` has exactly one line and **must end with an empty line**:

```
Main-Class: service.MiniBank
```

> Use the commands above instead of an editor's *Run* button. The project uses packages, so every
> class must be compiled together from the `Miniproject` folder.

On Linux or macOS use `/` instead of `\`.

---

## Architecture

The menu never touches data directly. Each layer has one job.

```
   ┌──────────────────────────────────────────────────────────┐
   │  MiniBank                    menu, console input/output  │
   └───────────────────────────┬──────────────────────────────┘
                               │  talks only to BankOperations
   ┌───────────────────────────▼──────────────────────────────┐
   │  AuditProxy                  reads @Audit by reflection  │
   │                              and records the call        │
   └───────────────────────────┬──────────────────────────────┘
                               │
   ┌───────────────────────────▼──────────────────────────────┐
   │  BankService                 validation, business rules, │
   │                              exceptions, transaction log │
   └──────┬──────────────┬──────────────────┬─────────────────┘
          │              │                  │
   ┌──────▼─────┐  ┌─────▼──────────┐  ┌────▼─────────────────┐
   │ Repository │  │ Transaction    │  │ StatePersister       │
   │ <Account>  │  │ Processor      │  │ TransactionLog       │
   │ key = @Id  │  │ 4 threads +    │  │ ReportGenerator      │
   │            │  │ ConcurrentMap  │  │ StatementFormatter   │
   └──────┬─────┘  └────────────────┘  └──────────────────────┘
          │
   ┌──────▼───────────────────────────────────────────────────┐
   │  Account  ──┬── SavingsAccount                           │
   │  (abstract) ├── CurrentAccount                           │
   │             └── FixedDepositAccount                      │
   └──────────────────────────────────────────────────────────┘
```

**A deposit travels like this:**
`Menu` → `AuditProxy` (records the call) → `BankService.deposit` → `Repository.findById` →
`Account.deposit` (synchronized) → `TransactionLog.append`.

---

## Project Structure

```
Miniproject/
├── README.md
├── manifest.txt                          Main-Class: service.MiniBank
├── MiniBank.jar                          created by the jar command
└── src/
    ├── exception/
    │   ├── BankException.java
    │   ├── InsufficientFundsException.java
    │   ├── AccountNotFoundException.java
    │   └── InvalidAmountException.java
    ├── model/
    │   ├── Account.java
    │   ├── SavingsAccount.java
    │   ├── CurrentAccount.java
    │   ├── FixedDepositAccount.java
    │   ├── Customer.java
    │   ├── Command.java
    │   ├── TransactionType.java
    │   ├── Transactable.java
    │   ├── InterestBearing.java
    │   ├── WithdrawRule.java
    │   ├── Premium.java
    │   └── annotation/
    │       ├── Id.java
    │       ├── Positive.java
    │       ├── MaxLength.java
    │       └── Audit.java
    ├── util/
    │   ├── Validator.java
    │   ├── CommandParser.java
    │   ├── StatementFormatter.java
    │   ├── AnnotationValidator.java
    │   └── TransactionLogger.java
    ├── service/
    │   ├── MiniBank.java
    │   ├── BankOperations.java
    │   ├── BankService.java
    │   ├── AuditProxy.java
    │   ├── Repository.java
    │   ├── AccountStore.java
    │   ├── TransactionProcessor.java
    │   ├── TransactionTask.java
    │   ├── AccountWorker.java
    │   ├── TransactionBuffer.java
    │   ├── TransactionProducer.java
    │   ├── TransactionConsumer.java
    │   ├── TransferDemo.java
    │   ├── StatePersister.java
    │   ├── TransactionLog.java
    │   ├── ReportGenerator.java
    │   └── PracticalDemo.java
    └── test/
        └── BankServiceTest.java
```

---

## Class Reference

### `exception`

| Class | Purpose |
|---|---|
| `BankException` | Checked base class of every banking error |
| `InsufficientFundsException` | A withdrawal was refused; `getShortfall()` returns the missing amount |
| `AccountNotFoundException` | No account has the given number |
| `InvalidAmountException` | The amount is zero or negative (also used for a bad opening balance) |

### `model`

| Class | Purpose |
|---|---|
| `Account` | Abstract base: balance, synchronized `deposit` and `withdraw`, `transfer` with rollback. `Comparable` by account number, `Serializable`, fields marked `@Id`, `@MaxLength`, `@Positive` |
| `SavingsAccount` | 4 % interest; a minimum balance must stay in the account |
| `CurrentAccount` | 0 % interest; the balance may fall to minus the overdraft limit |
| `FixedDepositAccount` | 7 % interest; withdrawals are locked; marked `Premium` |
| `Customer` | Name, e-mail, mobile and an `@Id` customer id, with a nested `Address`; `Cloneable` and `Serializable` |
| `Command` | Record `(type, accountNumber, amount)`, one line of a batch |
| `TransactionType` | Enum `DEPOSIT`, `WITHDRAW`, `TRANSFER` |
| `Transactable` | Contract for `deposit` and `withdraw` |
| `InterestBearing` | Interface with a `default` method `yearlyInterest()` |
| `WithdrawRule` | Functional interface, used with an anonymous class and a lambda |
| `Premium` | Marker interface (no methods) |

### `model.annotation`

| Annotation | Purpose |
|---|---|
| `@Id` | Marker for the key field; `Repository` finds it by reflection |
| `@Positive` | Value must be greater than zero (message defaults to `must be > 0`) |
| `@MaxLength(n)` | A string may not be longer than `n` |
| `@Audit("text")` | Marks a service method that must be recorded in the audit trail |

### `util`

| Class | Purpose |
|---|---|
| `Validator` | Regex checks for mobile, e-mail, PAN and IFSC |
| `CommandParser` | Turns `DEPOSIT AC0001 500` into a `Command` |
| `StatementFormatter` | Builds the text of an account statement |
| `AnnotationValidator` | Reads `@Positive` and `@MaxLength` by reflection and returns the error messages |
| `TransactionLogger` | Small `AutoCloseable` used to show try-with-resources |

### `service`

| Class | Purpose |
|---|---|
| `MiniBank` | The menu and the `main` method |
| `BankOperations` | Interface of the service; the proxy wraps this type |
| `BankService` | `openAccount`, `deposit`, `withdraw`, `transfer`, `statement`, `listAccounts`, `processBatch`, `report`, load and save |
| `AuditProxy` | Dynamic proxy that records every `@Audit` method call |
| `Repository<T>` | Generic repository; finds the key through `@Id`; `saveAll` is `@SafeVarargs`; bounded generic `average` |
| `AccountStore` | `HashMap` store with an `ArrayList` history and `sortedByBalance()` (used by the lab demo) |
| `TransactionProcessor` | Fixed pool of 4 threads, `ConcurrentHashMap` of accounts, counts successes and failures |
| `TransactionTask` | Runnable that performs one deposit or withdrawal (lab demo) |
| `AccountWorker` | Runnable that deposits many times (race-condition demo) |
| `TransactionBuffer`, `TransactionProducer`, `TransactionConsumer` | Bounded buffer with `wait` and `notify` |
| `TransferDemo` | Deadlock with opposite lock order, and the fix |
| `StatePersister` | Saves and loads the account array with object streams |
| `TransactionLog` | Appends one line to a log file |
| `ReportGenerator` | Walks the logs folder and writes the end-of-day report |
| `PracticalDemo` | Runs the demonstration of every lab, one after another |

### `test`

| Class | Purpose |
|---|---|
| `BankServiceTest` | Five JUnit 5 tests of `BankService` |

---

## Using the Application

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

| Option | What happens |
|---|---|
| **1** | Choose the type, then owner name, mobile number and opening balance |
| **2** | Account number and amount; the deposit is logged |
| **3** | Account number and amount; a refusal tells you how much is missing |
| **4** | From account, to account, amount; a failed transfer is rolled back |
| **5** | Statement with balance, interest rate and yearly interest |
| **6** | All accounts, sorted by account number |
| **7** | Type lines such as `DEPOSIT AC0001 500`, finish with an empty line. An empty batch runs 30 sample deposits |
| **8** | Reads every `.log` file in `logs/` and prints the totals |
| **9** | The last 10 entries of the audit trail |
| **0** | Saves every account to disk and exits |

Account numbers have four digits: `AC0001`, not `AC001`. Input is not case sensitive.

### A real session

Typed input is shown after each prompt. The menu is not repeated, to keep it short.

<details>
<summary><b>▶ Open the full session (first run)</b></summary>

```text
No saved accounts found, starting fresh.

Enter your choice : 1
1. Savings   2. Current   3. Fixed Deposit
Account type : 1
Owner name (max 10 characters) : Amit
Mobile number : 9876543210
Opening balance : 5000
Account opened : Account Number : AC0001, Owner Name : Amit, Balance : 5000

Enter your choice : 1
1. Savings   2. Current   3. Fixed Deposit
Account type : 3
Owner name (max 10 characters) : Dev
Mobile number : 9988776655
Opening balance : 20000
Account opened : Account Number : AC0002, Owner Name : Dev, Balance : 20000

Enter your choice : 2
Account number : AC0001
Amount : 700
Deposit successful

Enter your choice : 3
Account number : AC0001
Amount : 6000
Operation failed : Insufficient funds in AC0001, short by 1300

Enter your choice : 4
From account number : AC0001
To account number : AC0002
Amount : 1000
Transfer of 1000 from AC0001 to AC0002 successful
Transfer attempt finished for AC0001

Enter your choice : 5
Account number : AC0001
----- Account Statement -----
Account Number : AC0001
Owner Name     : Amit
Balance        : 4700
Interest Rate  : 4.0%
Yearly Interest: 188.0
Active         : true

Enter your choice : 7
Enter transactions like DEPOSIT AC0001 500 (one per line).
Press Enter on an empty line to finish.
If you add nothing, a sample batch of 30 deposits is used.
> DEPOSIT AC0001 250
> WITHDRAW AC0002 100
> 
Transaction failed for AC0002 : Insufficient funds in AC0002, short by 100
Batch finished : 1 succeeded, 1 failed. Total balance 25700 -> 25950 (net change 250)

Enter your choice : 8
END OF DAY REPORT
Generated   : 2026-11-01 14:56:34
Logs folder : logs

FILE                             SIZE  LAST MODIFIED        LINES
----                             ----  -------------        -----
transactions-2026-11-01.log     159 B  2026-11-01 14:56:34  4

SUMMARY
Files read          : 1
Total lines         : 4
Deposits            : 3 transactions, total 1950
Withdrawals         : 1 transactions, total 1000
Net change          : 950
Bad lines skipped   : 0

Enter your choice : 9
2026-11-01 14:56:21 | openAccount | Open a new account | args=[SAVINGS, Amit, 9876543210, 5000] | OK
2026-11-01 14:56:24 | openAccount | Open a new account | args=[FD, Dev, 9988776655, 20000] | OK
2026-11-01 14:56:26 | deposit | Deposit money | args=[AC0001, 700] | OK
2026-11-01 14:56:28 | withdraw | Withdraw money | args=[AC0001, 6000] | FAILED InsufficientFundsException : Insufficient funds in AC0001, short by 1300
2026-11-01 14:56:30 | transfer | Transfer money | args=[AC0001, AC0002, 1000] | OK
2026-11-01 14:56:31 | statement | Show account statement | args=[AC0001] | OK
2026-11-01 14:56:34 | processBatch | Process a batch of transactions | args=[[Command[type=DEPOSIT, accountNumber=AC0001, amount=250], Command[type=WITHDRAW, accountNumber=AC00...] | OK

Enter your choice : 0
Accounts saved. Goodbye!
```

</details>

### Accounts survive a restart

Starting the program again loads `data/bank.dat`:

```text
2 saved account(s) loaded.

Enter your choice : 6
1. Account Number : AC0001, Owner Name : Amit, Balance : 4950
2. Account Number : AC0002, Owner Name : Dev, Balance : 21000

Enter your choice : 0
Accounts saved. Goodbye!
```

The balances are the ones saved at the end of the first session.

<!--
Optional: add screenshots of your terminal here.
1. Create a folder named docs inside Miniproject and put the images in it.
2. Add a line such as:  ![Main menu](docs/menu.png)
-->

---

## Business Rules

| Rule | Value |
|---|---|
| Account number | `AC` followed by four digits |
| Owner name | Not empty, at most 10 characters |
| Mobile number | 10 digits, the first digit is 6, 7, 8 or 9 |
| Opening balance | Greater than zero; a Savings account needs at least 1000 |
| Savings account | 4 % interest; the balance may not drop below 1000 |
| Current account | 0 % interest; overdraft up to 2000 |
| Fixed Deposit | 7 % interest; every withdrawal is refused |
| Deposit, withdrawal, transfer | The amount must be greater than zero |
| Transfer | Source and destination must differ; if a step fails the balance is restored |
| Shortfall | `amount - available balance`, reported when a withdrawal is refused |

The `Validator` class also provides e-mail, PAN and IFSC checks; the menu itself validates the mobile number.

---

## Concurrency Model

| Concern | How it is handled |
|---|---|
| **Two threads change one balance** | `Account.deposit` and `Account.withdraw` are `synchronized`. Without it the lab demo loses money (a race condition) |
| **Many transactions at once** | `TransactionProcessor` runs a fixed pool of 4 threads. Its accounts live in a `ConcurrentHashMap`; successes and failures are counted with thread-safe types |
| **Deadlock** | `TransferDemo` shows two threads locking two accounts in opposite order and stuck forever. The fix is to always lock the account with the **smaller account number first** |
| **Transfers in the service** | `Account.transfer` withdraws and then deposits, never holding two locks at once, so it cannot deadlock |
| **Log file** | `BankService` writes log lines inside a lock, so lines never interleave |
| **Account numbers** | The number generator is `synchronized`, so two threads never get the same number |

---

## Data Files

Created automatically in the folder where the program runs:

| Path | Content |
|---|---|
| `data/bank.dat` | Saved accounts (binary); written on exit, read on start-up |
| `logs/transactions-<date>.log` | One line per successful deposit or withdrawal |
| `reports/end-of-day.txt` | The latest end-of-day report |
| `audit/audit-trail.txt` | Audited service calls with arguments and outcome |

A log line has four comma-separated fields:

```
2026-11-01 14:55:14,WITHDRAW,AC0001,1000
```

Delete these four folders to start with a clean bank.

---

## Testing

The tests live in `src/test/BankServiceTest.java` and use **JUnit 5**.

| Test | What it proves |
|---|---|
| `depositIncreasesBalance` | A deposit adds exactly the amount |
| `withdrawalOverTheLimitThrowsException` | An over-limit withdrawal throws `InsufficientFundsException` with the right shortfall and leaves the balance unchanged |
| `manyConcurrentDepositsKeepBalanceCorrect` | 10 threads x 100 deposits add exactly 1000; no update is lost |
| `transferMovesMoneyBetweenAccounts` | Money leaves one account and arrives in the other |
| `invalidMobileNumberIsRejected` | A wrong mobile number stops the account from being opened |

### Running the tests

1. Create a folder `lib` inside `Miniproject`.
2. Download `junit-platform-console-standalone-1.10.2.jar` from
   `https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/`
   and put it in `lib`.
3. From `Miniproject` run:

```
javac -d out -sourcepath src src\service\MiniBank.java
javac -cp "out;lib\junit-platform-console-standalone-1.10.2.jar" -d testout src\test\BankServiceTest.java
java -jar lib\junit-platform-console-standalone-1.10.2.jar execute --class-path "out;testout" --select-class test.BankServiceTest
```

The summary at the end should report 5 tests successful and 0 failed. On Linux or macOS use `:`
instead of `;` and `/` instead of `\`.

> An editor may underline `org.junit` in the test file until the JUnit jar is added to its
> classpath. This does not affect building or running the application.

### The old demonstration

`PracticalDemo` prints the output of every lab in order and takes about ten seconds:

```
javac -d out -sourcepath src src\service\PracticalDemo.java
java -cp out service.PracticalDemo
```

---

## Practical Roadmap

| Practical | Topic | Where to look |
|---|---|---|
| 1 | Environment and project setup; the menu shell: `record` (`BankInfo`), `enum`, `Scanner` input, `switch`, a loop that runs until the user exits, goodbye message | `service/MiniBank` (menu loop), `service/PracticalDemo` (`BankInfo` header), `model/TransactionType` (enum) |
| 2 | `Customer` and `Account`: encapsulation (private fields, the balance changes only through `deposit` and `withdraw`, no public setter), `private static` counters that generate `CUST101` and `AC0001`, constructors, getters, an array of objects | `model/Customer`, `model/Account` |
| 3 | Object behaviour: `toString`, `equals` and `hashCode` based on the account number, the nested static class `Address`, `Cloneable` with `clone()`, `instanceof` | `Account.toString`, `Account.equals`, `Account.hashCode`, `Customer.Address`, `Customer.clone`, `service/PracticalDemo` |
| 4 | Validation and parsing: regular expressions with `Pattern` (mobile, e-mail, PAN, IFSC), `enum TransactionType`, `record Command`, `String.split`, `StringBuilder` | `util/Validator`, `model/TransactionType`, `model/Command`, `util/CommandParser`, `util/StatementFormatter` |
| 5 | Inheritance and polymorphism: abstract `Account`, three subclasses with their own interest and withdrawal rules, `super(...)`, pattern `instanceof` | `model/Account`, `model/SavingsAccount`, `model/CurrentAccount`, `model/FixedDepositAccount`, `service/PracticalDemo` |
| 6 | Interfaces (default, functional, marker), packages, static import, runnable JAR | `Transactable`, `InterestBearing`, `WithdrawRule`, `Premium`, `manifest.txt` |
| 7 | Custom annotations and reflection | `model/annotation/`, `AnnotationValidator` |
| 8 | Custom checked exceptions, `try / catch / finally`, try-with-resources | `exception/`, `Account.transfer`, `TransactionLogger` |
| 9 | Threads, race condition, `synchronized`, `join`, thread states | `Account.deposit`, `AccountWorker` |
| 10 | Thread pool, `wait / notify`, deadlock and lock ordering | `TransactionProcessor`, `TransactionBuffer`, `TransferDemo` |
| 11 | Serialization, NIO `Files`, `DirectoryStream`, file attributes | `StatePersister`, `TransactionLog`, `ReportGenerator` |
| 12 | Collections: `HashMap`, `ArrayList`, `Comparable`, `Comparator`, `ConcurrentHashMap` | `AccountStore`, `Account.compareTo` |
| 13 | Generics, bounded types, `@SafeVarargs`, reflection on `@Id` | `Repository<T>` |
| 14 | Capstone: service layer, dynamic proxy, audit, JUnit, packaging | `BankService`, `AuditProxy`, `BankServiceTest` |

---

## Design Decisions

- **One service, one interface.** The menu only knows `BankOperations`. That is what lets `AuditProxy`
  sit in between without changing a single line of `BankService`. A Java dynamic proxy can only wrap an interface.
- **Exceptions instead of `false`.** A refused withdrawal says why and by how much, so the caller can react.
- **Reflection on `@Id`.** `Repository<T>` has no idea what an `Account` is. It finds the key field by
  annotation, so the same class stores accounts, customers or anything else that has an `@Id`.
- **Deadlock avoided by ordering.** When two accounts must be locked, the smaller account number is
  locked first, so a circular wait can never form.
- **Account numbers survive a restart.** After loading, the counter continues from the highest saved
  number, so a new account can never overwrite an old one.
- **Only successful batch items are logged.** `TransactionProcessor` remembers which commands succeeded,
  and `BankService` writes exactly those to the log.
- **Plain Java.** No framework and no build tool, so every line can be read and explained.

---

## Troubleshooting

| Problem | Cause and fix |
|---|---|
| `package model does not exist` | A single file was compiled. Compile from `Miniproject` with `-sourcepath src`, as in Quick Start |
| `no main manifest attribute` | `manifest.txt` lacks its final empty line. Press Enter after `Main-Class: service.MiniBank`, save, rebuild the JAR |
| `MiniBank.jar ... being used by another process` | The program is still running. Exit with `0`, then build the JAR again |
| `Account not found` | Account numbers have four digits: `AC0001` |
| `bad source file ... does not contain class` | A file has the wrong name or package line. Delete `out` (`rmdir /s /q out`) and compile again |
| Old lab data appears in the report | Delete the `logs` folder and run again |
| Red underline on `org.junit` | The JUnit jar is not on the editor's classpath; see Testing |

---

## Known Limitations

- The mobile number is validated but **not stored**, because `Account` has no mobile field.
- Accounts are saved only on a normal exit (option `0`). A crash loses the changes of that session.
- A transfer appears in the log as one `WITHDRAW` and one `DEPOSIT` line.
- The rollback inside `Account.transfer` restores the saved balance; it is not atomic against other
  threads changing the same account at the same moment. This is fine for a single-user console.
- `Repository` uses a plain `HashMap`, so `BankService.openAccount` is synchronized.
- A batch supports `DEPOSIT` and `WITHDRAW`; `TRANSFER` needs two accounts and is rejected.
- There is no login, password or user roles.

## Future Improvements

- Store customer details (name, mobile, e-mail, PAN) and link them to accounts
- Replace file serialization with a database (JDBC or SQLite)
- Authentication and roles (customer, teller, manager)
- Month-end interest posting
- A REST API or a JavaFX front end on top of `BankOperations`
- A Maven or Gradle build with automatic testing

---

## Author

| | |
|---|---|
| **Name** | Heni Patel |
| **Enrollment number** | 25CS061 |
| **Course** | Object Oriented Programming (CEUE203) |
| **Institute** | CHARUSAT University (CSPIT) |

This project was created for academic purposes.

---

<div align="center">

**MiniBank** &nbsp;·&nbsp; Built with ☕ Java &nbsp;·&nbsp; Capstone project

</div>