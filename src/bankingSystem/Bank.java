package bankingSystem;

import java.util.HashMap;
import java.util.Map;

// Bank class
public class Bank {
    private double internalFunds;
    private int year;
    private boolean isLoanRequestPending;
    private final Map<String, Account> accounts;
    private final Map<String, Loan> loanRequests;
    private final Map<String, Employee> employees;

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
        this.internalFunds = 1000000;
        this.year = 0;
        this.isLoanRequestPending = false;
        this.accounts = new HashMap<>();
        this.loanRequests = new HashMap<>();
        this.employees = new HashMap<>();

        employees.put("MD", new ManagingDirector("MD"));
        for(int i = 1; i <= 2; i++) { employees.put("O" + i, new Officer("O" + i)); }
        for(int i = 1; i <= 5; i++) { employees.put("C" + i, new Cashier("C" + i)); }
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
                    if (initialDeposit >= FixedDepositAccount.MIN_DEPOSIT) {
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
            System.out.println(accountType + " account for " + accountHolder + " created; initial balance " + initialDeposit + "$");
        } else {
            System.out.println("Error: Account already exists for " + accountHolder);
        }
    }

    public void deposit(String accountHolder, double amount) {
        Account account = accounts.get(accountHolder.toLowerCase());
        if (account != null) {
            account.deposit(amount);
            internalFunds += amount;
            System.out.println(amount + "$ deposited; current balance " + account.getBalance() + "$");
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
        Loan loan = new Loan(amount);
        loanRequests.put(accountHolder.toLowerCase(), loan);
        isLoanRequestPending = true;
        System.out.println("Loan request successful, sent for approval");
    }

    public void approveLoan() {
        for (String accountHolder : loanRequests.keySet()) {
            Loan loan = loanRequests.get(accountHolder.toLowerCase());
            Account account = accounts.get(accountHolder.toLowerCase());
            account.addLoanAmount(loan.amount());
            account.setBalance(account.getBalance() + loan.amount());
            isLoanRequestPending = false;
            System.out.println("Loan for " + accountHolder + " approved");
        }
    }

    public void queryBalance(String accountHolder) {
        Account account = accounts.get(accountHolder.toLowerCase());
        if (account != null) {
            if(account.getLoanAmount() == 0)
                System.out.println("Current Balance " + account.getBalance() + "$");
            else System.out.println("Current Balance " + account.getBalance() + "$, loan " + account.getLoanAmount() + "$");
        } else {
            System.out.println("Error: Account not found for " + accountHolder);
        }
    }

    public void incrementYear() {
        year++;

        for (Account account : accounts.values()) {
            double interest = account.getBalance() * getInterestRate(account);
            account.deposit(interest);
            double SERVICE_CHARGE = 500;
            if(!(account instanceof StudentAccount)) account.deposit(-SERVICE_CHARGE);
            if(account instanceof FixedDepositAccount) ((FixedDepositAccount) account).setHasReachedMaturityPeriod(true);
        }

        for (String accountHolder : loanRequests.keySet()) {
            Account account = accounts.get(accountHolder.toLowerCase());
            Loan loan = loanRequests.get(accountHolder.toLowerCase());
            account.setBalance(account.getBalance() - loan.getInterest());
        }
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
        } else if (accountType.equalsIgnoreCase("student")) {
            AccountType.STUDENT_ACCOUNT.interestRate = newRate;
        } else if (accountType.equalsIgnoreCase("fixed deposit")) {
            AccountType.FIXED_DEPOSIT_ACCOUNT.interestRate = newRate;
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
        // Convert both the name and keys in the map to lowercase for case-insensitive comparison
        String lowercaseName = name.toLowerCase();

        // Use containsKey with the lowercase name
        return accounts.containsKey(lowercaseName);
    }

    public Employee getEmployee(String name) {
        return employees.get(name.toUpperCase());
    }
}