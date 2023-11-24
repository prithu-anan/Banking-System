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

    private enum EmployeeType {
        OFFICER(2),
        CASHIER(5);

        private int count;

        EmployeeType(int count) {
            this.count = count;
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
        for(int i = 1; i <= EmployeeType.OFFICER.count; i++) { employees.put("O" + i, new Officer("O" + i)); }
        for(int i = 1; i <= EmployeeType.CASHIER.count; i++) { employees.put("C" + i, new Cashier("C" + i)); }

        System.out.println("Bank Created; MD, O1, O2, C1, C2, C3, C4, C5 created");
    }

    public void createAccount(String accountHolder, String accountType, double initialDeposit) {
        if (!accounts.containsKey(accountHolder)) {
            Account account;
            switch (accountType.toLowerCase()) {
                case "savings":
                    account = new SavingsAccount(accountHolder, initialDeposit);
                    internalFunds += initialDeposit;
                    break;
                case "student":
                    account = new StudentAccount(accountHolder, initialDeposit);
                    internalFunds += initialDeposit;
                    break;
                case "fixed deposit":
                    if (initialDeposit >= FixedDepositAccount.getMinInitialDeposit()) {
                        account = new FixedDepositAccount(accountHolder, initialDeposit);
                        internalFunds += initialDeposit;
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

    public Employee createEmployee(String type) {
        Employee employee;
        switch (type.toUpperCase()) {
            case "O" -> {
                employee = new Officer("O" + ++EmployeeType.OFFICER.count);
                employees.put("O" + EmployeeType.OFFICER.count, employee);
                System.out.println("O" + EmployeeType.OFFICER.count + " created");
                return employee;
            }
            case "C" -> {
                employee = new Cashier("C" + ++EmployeeType.CASHIER.count);
                employees.put("C" + EmployeeType.CASHIER.count, employee);
                System.out.println("C" + EmployeeType.CASHIER.count + " created");
                return employee;
            }
            default -> {
                System.out.println("Error: Invalid employee type");
                return null;
            }
        }
    }

    void deposit(String accountHolder, double amount) {
        Account account = accounts.get(accountHolder.toLowerCase());
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            internalFunds += amount;
        } else {
            System.out.println("Error: Account not found for " + accountHolder);
        }
    }

    void withdraw(String accountHolder, double amount) {
        Account account = accounts.get(accountHolder.toLowerCase());
        if (account != null) {
            account.setBalance(account.getBalance() - amount);
            internalFunds -= amount;
        } else {
            System.out.println("Error: Account not found for " + accountHolder);
        }
    }

    void requestLoan(String accountHolder, double amount) {
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

    double queryBalance(String accountHolder) {
        Account account = accounts.get(accountHolder.toLowerCase());
        if (account != null) {
            if(account.getLoanAmount() == 0)
                System.out.println("Current Balance " + account.getBalance() + "$");
            else
                System.out.println("Current Balance " + account.getBalance()
                        + "$, loan " + account.getLoanAmount() + "$");
            return account.getBalance();
        } else {
            System.out.println("Error: Account not found for " + accountHolder);
            return 0;
        }
    }

    double lookup(String accountHolder) {
        Account account = accounts.get(accountHolder.toLowerCase());
        if (account != null) {
            System.out.println(accountHolder + "'s current balance " + account.getBalance() + "$");
            return account.getBalance();
        } else {
            System.out.println("Error: Account not found for " + accountHolder);
            return 0;
        }
    }

    void approveLoan() {
        Iterator<Loan> iterator = loanRequests.iterator();

        while (iterator.hasNext()) {
            Loan loan = iterator.next();
            Account account = accounts.get(loan.accountHolder().toLowerCase());

            account.setLoanAmount(account.getLoanAmount() + loan.amount());
            account.setBalance(account.getBalance() + loan.amount());

            iterator.remove();
            System.out.println("Loan for " + loan.accountHolder() + " approved");
        }

        loanRequestPending = false;
    }

    void changeInterestRate(String accountType, double newRate) {
        final double roi = newRate / 100.0;
        if (accountType.equalsIgnoreCase("savings")) {
            AccountType.SAVINGS_ACCOUNT.interestRate = roi;
            System.out.println("Interest rate for Savings Account changed to " + newRate);
        } else if (accountType.equalsIgnoreCase("student")) {
            AccountType.STUDENT_ACCOUNT.interestRate = roi;
            System.out.println("Interest rate for Student Account changed to " + newRate);
        } else if (accountType.equalsIgnoreCase("fixed deposit")) {
            AccountType.FIXED_DEPOSIT_ACCOUNT.interestRate = roi;
            System.out.println("Interest rate for Fixed Deposit Account changed to " + newRate);
        } else {
            System.out.println("Error: Invalid account type");
        }
    }

    double seeInternalFund() {
        System.out.println("Internal Funds: " + internalFunds + "$");
        return internalFunds;
    }

    public int incrementYear() {
        year++;

        for (Account account : accounts.values()) {
            double interest = account.getBalance() * getInterestRate(account);
            double loanInterest = account.getLoanAmount() * LOAN_INTEREST_RATE;

            account.setBalance(account.getBalance() + interest);

            if(account.getBalance() < loanInterest) {
                account.setLoanAmount(account.getLoanAmount() + loanInterest - account.getBalance());
                account.setBalance(0);
            } else account.setBalance(account.getBalance() - loanInterest);

            if(!(account instanceof StudentAccount))
                if(account.getBalance() < SERVICE_CHARGE) {
                    account.setLoanAmount(account.getLoanAmount() + SERVICE_CHARGE - account.getBalance());
                    account.setBalance(0);
                } else account.setBalance(account.getBalance() - SERVICE_CHARGE);

            if(account instanceof FixedDepositAccount) ((FixedDepositAccount) account).setHasReachedMaturityPeriod(true);
        }

        System.out.println(year + " year(s) passed");
        return year;
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

    public boolean isAccountExist(String name) {
        String lowercaseName = name.toLowerCase();
        return accounts.containsKey(lowercaseName);
    }

    public Account getAccount(String name) {
        return accounts.get(name.toLowerCase());
    }

    public Employee getEmployee(String name) {
        return employees.get(name.toUpperCase());
    }

    public boolean isLoanRequestPending() { return loanRequestPending; }

    public void addOperation(String details, String accountHolder) {
        operations.add(new Operation(details, accountHolder, year));
    }

    public void printOperationList() {
        for (Operation operation : operations) {
            System.out.println("Operation Performed: " + operation.details() +
                    ", By: " + operation.accountHolder() +
                    ", In Year: " + operation.year());
        }
    }
}