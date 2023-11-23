package bankingSystem;


// Account class
abstract class Account {
    protected String accountHolder;
    protected double balance;
    protected double loanAmount;
    protected double MAX_LOAN;

    Account(String accountHolder, double initialDeposit) {
        this.accountHolder = accountHolder;
        this.balance = initialDeposit;
        this.loanAmount = 0;
    }

    abstract boolean deposit(double amount);

    abstract boolean withdraw(double amount);

    double getBalance() { return balance; }

    void setBalance(double balance) { this.balance = balance; }

    double getLoanAmount() { return loanAmount; }

    void addLoanAmount(double loanAmount) { this.loanAmount += loanAmount; }

    double getMaxLoan() { return MAX_LOAN; }
}


// SavingsAccount class
class SavingsAccount extends Account {
    SavingsAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
        super.MAX_LOAN = 10000;
    }

    @Override
    boolean deposit(double amount) {
        balance += amount;
        return true;
    }

    @Override
    boolean withdraw(double amount) {
        if (balance - amount >= 1000) {
            balance -= amount;
            return true;
        }
        return false;
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
    boolean deposit(double amount) {
        balance += amount;
        return true;
    }

    @Override
    boolean withdraw(double amount) {
        if (amount <= MAX_WITHDRAWAL && balance - amount >= 0) {
            balance -= amount;
            return true;
        }
        return false;
    }
}


// FixedDepositAccount class
class FixedDepositAccount extends Account {
    private static final double MIN_INITIAL_DEPOSIT = 100000;
    private static final double MIN_DEPOSIT = 50000;
    private boolean hasReachedMaturityPeriod;

    FixedDepositAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
        super.MAX_LOAN = 100000;
        hasReachedMaturityPeriod = false;
    }

    @Override
    boolean deposit(double amount) {
        if (amount >= MIN_DEPOSIT) {
            balance += amount;
            return true;
        }
        return false;
    }

    @Override
    boolean withdraw(double amount) {
        if (hasReachedMaturityPeriod && balance - amount >= 0) {
            balance -= amount;
            return true;
        }
        return false;
    }

    static double getInitialMinDeposit() { return MIN_INITIAL_DEPOSIT; }

    public void setHasReachedMaturityPeriod(boolean hasReachedMaturityPeriod) {
        this.hasReachedMaturityPeriod = hasReachedMaturityPeriod;
    }
}