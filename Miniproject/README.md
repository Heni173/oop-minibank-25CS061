# MiniBank

A console banking application written in plain Java. It grew practical by practical
and this final version wires everything together behind one menu.

## Features

- Savings, Current and Fixed Deposit accounts (abstract `Account` with polymorphic rules)
- Open account, deposit, withdraw, transfer, statement and account list
- Input validation (regex `Validator`) and custom checked exceptions with a shortfall value
- Annotations (`@Id`, `@Positive`, `@MaxLength`, `@Audit`) read at run time by reflection
- Thread-safe operations (`synchronized`), a fixed thread pool for batch processing
- Generic `Repository<T>` that finds the key through `@Id`
- Accounts are saved to disk on exit and loaded again on start-up
- Append-only transaction log and an end-of-day report built with NIO
- An audit trail: every method marked `@Audit` is recorded by a reflective proxy
- JUnit 5 tests and a runnable JAR

## Project structure

```
Miniproject/
  manifest.txt                 Main-Class: service.MiniBank
  README.md
  src/
    exception/                 BankException and its subclasses
    model/                     Account, SavingsAccount, CurrentAccount, FixedDepositAccount,
                               Customer, Command, TransactionType, interfaces
      annotation/              Id, Positive, MaxLength, Audit
    util/                      Validator, CommandParser, StatementFormatter,
                               AnnotationValidator, TransactionLogger
    service/                   MiniBank (menu), BankService, BankOperations, AuditProxy,
                               Repository, TransactionProcessor, StatePersister,
                               TransactionLog, ReportGenerator, ...
    test/                      BankServiceTest (JUnit 5)
```

`service/PracticalDemo.java` is the old demo program that shows the output of each
practical one after another. It has its own `main`.

## Requirements

- JDK 17 or newer (developed and tested with JDK 21 and 26)

## Build and run

Open a terminal in the `Miniproject` folder (the folder that contains `src`):

```
javac -d out -sourcepath src src\service\MiniBank.java
java -cp out service.MiniBank
```

Build the runnable JAR and start it:

```
jar cfm MiniBank.jar manifest.txt -C out .
java -jar MiniBank.jar
```

`manifest.txt` must end with an empty line.

## Menu

| Option | What it does |
|---|---|
| 1 | Open an account (type, owner name, mobile number, opening balance) |
| 2 | Deposit |
| 3 | Withdraw (fails with the shortfall if the rules do not allow it) |
| 4 | Transfer between two accounts |
| 5 | Statement of one account |
| 6 | List all accounts |
| 7 | Process a batch of transactions on the thread pool |
| 8 | End-of-day report built from the logs |
| 9 | Show the last lines of the audit trail |
| 0 | Save accounts and exit |

Batch lines look like `DEPOSIT AC0001 500` or `WITHDRAW AC0002 100`.
An empty batch runs a sample of 30 deposits.

## Files created while running

| Path | Content |
|---|---|
| `data/bank.dat` | Saved accounts (binary, written on exit) |
| `logs/transactions-<date>.log` | One line per successful deposit or withdrawal |
| `reports/end-of-day.txt` | Latest end-of-day report |
| `audit/audit-trail.txt` | Which `@Audit` service method was called, with arguments and result |

Delete these folders to start with a clean bank.

## Tests

The tests use JUnit 5. Download `junit-platform-console-standalone-1.10.2.jar` from
Maven Central into a `lib` folder, then run from `Miniproject`:

```
javac -d out -sourcepath src src\service\MiniBank.java
javac -cp "out;lib\junit-platform-console-standalone-1.10.2.jar" -d testout src\test\BankServiceTest.java
java -jar lib\junit-platform-console-standalone-1.10.2.jar execute --class-path "out;testout" --select-class test.BankServiceTest
```

Tests: a deposit increases the balance, a withdrawal over the limit throws
`InsufficientFundsException`, many concurrent deposits keep the balance correct,
a transfer moves money, and an invalid mobile number is rejected.

## Design notes

- `BankService` is the only class the menu talks to. It validates input, looks accounts
  up in a `Repository<Account>`, applies the operation and writes the log.
- `AuditProxy` wraps `BankService` (through the `BankOperations` interface) with a
  `java.lang.reflect.Proxy`. It reads `@Audit` from the real method and records the call.
- Account numbers continue after the highest loaded number, so a new account never
  reuses a saved number.
- `Account.deposit` and `Account.withdraw` are `synchronized`. `TransactionProcessor` keeps
  its accounts in a `ConcurrentHashMap`.

## Known limitations

- The mobile number is validated but not stored, because `Account` has no mobile field.
- Accounts are saved only on a normal exit (option 0). A crash loses the changes of that run.
- A transfer is written to the log as one WITHDRAW and one DEPOSIT line.
- `Repository` uses a plain `HashMap`; `BankService.openAccount` is synchronized for that reason.
- There is no login or user authentication.