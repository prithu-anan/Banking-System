package bankingSystem;

import java.util.*;

// Bank class
public class Bank {
    private record Operation(String details, String accountHolder, int year) {}

    private record Loan(String accountHolder, double amount) {}

    private final double INITIAL_FUNDS = 1000000;
    private final double SERVICE_CHARGE = 500;
    private final double LOAN_INTEREST_RATE = 0.10;
    private double internalFunds;
    private int year;
    private boolean loanRequestPending;
    private final Map<String, Account> accounts;
    private final List<Loan> loanRequests;
    private final Map<String, Employee> employees;
    private final List<Operation> operations;

    private enum AccountType {
        SAVINGS_ACCOUNT(0.10),
        STUDENT_ACCOUNT(0.05),
        FIXED_DEPOSIT_ACCOUNT(0.15);

        private double interestRate;

        AccountType(double interestRate) {
            this.interestRate = interestRate;
        }
    }

    public Bank() {
        this.internalFunds = INITIAL_FUNDS;
        this.year = 0;
        this.loanRequestPending = false;
        this.accounts = new HashMap<>();
        this.loanRequests = new ArrayList<>();
        this.employees = new HashMap<>();
        this.operations = new ArrayList<>();

        employees.put("MD", new ManagingDirector("MD"));
        for(int i = 1; i <= 2; i++) { employees.put("O" + i, new Officer("O" + i)); }
        for(int i = 1; i <= 5; i++) { employees.put("C" + i, new Cashier("C" + i)); }

        System.out.println("Bank Created; MD, O1, O2, C1, C2, C3, C4, C5 created");
    }

    public void createAccount(String accountHolder, String accountType, double initialDeposit) {
        if (!accounts.containsKey(accountHolder)) {
            Account account;
            switch (accountType.toLowerCase()) {
                case "savings":
                    account = new SavingsAccount(accountHolder, initialDeposit);
                    break;
                case "student":
                    account = new StudentAccount(accountHolder, initialDeposit);
                    break;
                case "fixed deposit":
                    if (initialDeposit >= FixedDepositAccount.getInitialMinDeposit()) {
                        account = new FixedDepositAccount(accountHolder, initialDeposit);
                    } else {
                        System.out.println("Error: Initial deposit for Fixed Deposit Account must be at least 100,000$");
                        return;
                    }
                    break;
                default:
                    System.out.println("Error: Invalid account type");
                    return;
            }
            accounts.put(accountHolder.toLowerCase(), account);
            System.out.println(accountType + " account for " +
                    accountHolder + " created; initial balance " + initialDeposit + "$");
        } else {
            System.out.println("Error: Account already exists for " + accountHolder);
        }
    }

    public void deposit(String accountHolder, double amount) {
        Account account = accounts.get(accountHolder.toLowerCase());
        if (account != null) {
            boolean success = account.deposit(amount);
            if(success) {
                internalFunds += amount;
                System.out.println(amount + "$ deposited; current balance " + account.getBalance() + "$");
            } else {
                System.out.println("Invalid transaction; current balance " + account.getBalance() + "$");
            }
        } else {
            System.out.println("Error: Account not found for " + accountHolder);
        }
    }

    public void withdraw(String accountHolder, double amount) {
        Account account = accounts.get(accountHolder.toLowerCase());
        if (account != null) {
            boolean success = account.withdraw(amount);
            if (success) {
                internalFunds -= amount;
                System.out.println(amount + "$ withdrawn; current balance " + account.getBalance() + "$");
            } else {
                System.out.println("Invalid transaction; current balance " + account.getBalance() + "$");
            }
        } else {
            System.out.println("Error: Account not found for " + accountHolder);
        }
    }

    public void requestLoan(String accountHolder, double amount) {
        Account account = accounts.get(accountHolder.toLowerCase());
        if (account != null) {
            if (amount + account.getLoanAmount() <= account.getMaxLoan()) {
                if (amount <= internalFunds) {
                    Loan loan = new Loan(accountHolder, amount);
                    loanRequests.add(loan);
                    loanRequestPending = true;
                    System.out.println("Loan request successful, sent for approval");
                } else {
                    System.out.println("Error: Insufficient internal funds");
                }
            } else {
                System.out.println("Error: Loan amount exceeds maximum loan amount");
            }
        } else {
            System.out.println("Error: Account not found for " + accountHolder);
        }
    }

    public void approveLoan() {
        Iterator<Loan> iterator = loanRequests.iterator();

        while (iterator.hasNext()) {
            Loan loan = iterator.next();
            Account account = accounts.get(loan.accountHolder().toLowerCase());

            account.addLoanAmount(loan.amount());
            account.setBalance(account.getBalance() + loan.amount());

            iterator.remove();
            System.out.println("Loan for " + loan.accountHolder() + " approved");
        }

        loanRequestPending = false;
    }

    public void queryBalance(String accountHolder) {
        Account account = accounts.get(accountHolder.toLowerCase());
        if (account != null) {
            if(account.getLoanAmount() == 0)
                System.out.println("Current Balance " + account.getBalance() + "$");
            else
                System.out.println("Current Balance " + account.getBalance()
                        + "$, loan " + account.getLoanAmount() + "$");
        } else {
            System.out.println("Error: Account not found for " + accountHolder);
        }
    }

    public void incrementYear() {
        year++;

        for (Account account : accounts.values()) {
            double interest = account.getBalance() * getInterestRate(account);
            account.deposit(interest);
            account.deposit(- LOAN_INTEREST_RATE * account.getLoanAmount());
            if(!(account instanceof StudentAccount)) account.deposit(-SERVICE_CHARGE);
            if(account instanceof FixedDepositAccount) ((FixedDepositAccount) account).setHasReachedMaturityPeriod(true);
        }

        System.out.println("1 year passed");
    }

    private double getInterestRate(Account account) {
        if (account instanceof SavingsAccount) {
            return AccountType.SAVINGS_ACCOUNT.interestRate;
        } else if (account instanceof StudentAccount) {
            return AccountType.STUDENT_ACCOUNT.interestRate;
        } else if (account instanceof FixedDepositAccount) {
            return AccountType.FIXED_DEPOSIT_ACCOUNT.interestRate;
        }
        return 0;
    }

    public void changeInterestRate(String accountType, double newRate) {
        if (accountType.equalsIgnoreCase("savings")) {
            AccountType.SAVINGS_ACCOUNT.interestRate = newRate;
            System.out.println("Interest rate for Savings Account changed to " + newRate);
        } else if (accountType.equalsIgnoreCase("student")) {
            AccountType.STUDENT_ACCOUNT.interestRate = newRate;
            System.out.println("Interest rate for Student Account changed to " + newRate);
        } else if (accountType.equalsIgnoreCase("fixed deposit")) {
            AccountType.FIXED_DEPOSIT_ACCOUNT.interestRate = newRate;
            System.out.println("Interest rate for Fixed Deposit Account changed to " + newRate);
        } else {
            System.out.println("Error: Invalid account type");
        }
    }

    public void lookup(String accountHolder) {
        Account account = accounts.get(accountHolder.toLowerCase());
        if (account != null) {
            System.out.println(accountHolder + "'s current balance " + account.getBalance() + "$");
        } else {
            System.out.println("Error: Account not found for " + accountHolder);
        }
    }

    public void seeInternalFund() {
        System.out.println("Internal Funds: " + internalFunds + "$");
    }

    public boolean isExist(String name) {
        String lowercaseName = name.toLowerCase();
        return accounts.containsKey(lowercaseName);
    }

    public Employee getEmployee(String name) {
        return employees.get(name.toUpperCase());
    }

    public boolean isLoanRequestPending() { return loanRequestPending; }

    public void addOperation(String details, String accountHolder) {
        operations.add(new Operation(details, accountHolder, year));
    }

    public void printOperationsList() {
        for (Operation operation : operations) {
            System.out.println("Operation Performed: " + operation.details() +
                    ", By: " + operation.accountHolder() +
                    ", In Year: " + operation.year());
        }
    }
}