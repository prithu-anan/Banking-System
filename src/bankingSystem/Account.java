package bankingSystem;


// Account class
abstract public class Account {
    protected String accountHolder;
    protected double balance;
    protected double loanAmount;
    protected double MAX_LOAN = Double.MAX_VALUE;

    Account(String accountHolder, double initialDeposit) {
        this.accountHolder = accountHolder;
        this.balance = initialDeposit;
        this.loanAmount = 0;
    }

    public abstract void deposit(double amount, Bank bank);

    public abstract void withdraw(double amount, Bank bank);

    public void requestLoan(double amount, Bank bank) { bank.requestLoan(accountHolder, amount); }

    public double queryBalance(Bank bank) { return bank.queryBalance(accountHolder); }

    double getBalance() { return balance; }

    void setBalance(double balance) { this.balance = balance; }

    double getLoanAmount() { return loanAmount; }

    void setLoanAmount(double loanAmount) { this.loanAmount += loanAmount; }

    double getMaxLoan() { return MAX_LOAN; }
}


// SavingsAccount class
class SavingsAccount extends Account {
    SavingsAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
        super.MAX_LOAN = 10000;
    }

    @Override
    public void deposit(double amount, Bank bank) {
        bank.deposit(accountHolder, amount);
        System.out.println(amount + "$ deposited; current balance " + balance + "$");
    }

    @Override
    public void withdraw(double amount, Bank bank) {
        if (balance - amount >= 1000) {
            bank.withdraw(accountHolder, amount);
            System.out.println(amount + "$ withdrawn; current balance " + balance + "$");
        } else System.out.println("Invalid transaction; current balance " + balance + "$");
    }
}


// StudentAccount class
class StudentAccount extends Account {
    private final double MAX_WITHDRAWAL;

    StudentAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
        super.MAX_LOAN = 1000;
        MAX_WITHDRAWAL = 1000;
    }

    @Override
    public void deposit(double amount, Bank bank) {
        bank.deposit(accountHolder, amount);
        System.out.println(amount + "$ deposited; current balance " + balance + "$");
    }

    @Override
    public void withdraw(double amount, Bank bank) {
        if (amount <= MAX_WITHDRAWAL && balance - amount >= 0) {
            bank.withdraw(accountHolder, amount);
            System.out.println(amount + "$ withdrawn; current balance " + balance + "$");
        } else System.out.println("Invalid transaction; current balance " + balance + "$");
    }
}


// FixedDepositAccount class
class FixedDepositAccount extends Account {
    private static final double MIN_INITIAL_DEPOSIT = 100000;
    private final double MIN_DEPOSIT = 50000;
    private boolean hasReachedMaturityPeriod;

    FixedDepositAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
        super.MAX_LOAN = 100000;
        hasReachedMaturityPeriod = false;
    }

    @Override
    public void deposit(double amount, Bank bank) {
        if (amount >= MIN_DEPOSIT) {
            bank.deposit(accountHolder, amount);
            System.out.println(amount + "$ deposited; current balance " + balance + "$");
        } else System.out.println("Invalid transaction; current balance " + balance + "$");
    }

    @Override
    public void withdraw(double amount, Bank bank) {
        if (hasReachedMaturityPeriod && balance - amount >= 0) {
            bank.withdraw(accountHolder, amount);
            System.out.println(amount + "$ withdrawn; current balance " + balance + "$");
        } else System.out.println("Invalid transaction; current balance " + balance + "$");
    }

    static double getMinInitialDeposit() { return MIN_INITIAL_DEPOSIT; }

    public void setHasReachedMaturityPeriod(boolean hasReachedMaturityPeriod) {
        this.hasReachedMaturityPeriod = hasReachedMaturityPeriod;
    }
}