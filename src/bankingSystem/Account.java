package bankingSystem;

// Account class
abstract class Account {
    protected String accountHolder;
    protected double balance;
    protected double loanAmount;

    Account(String accountHolder, double initialDeposit) {
        this.accountHolder = accountHolder;
        this.balance = initialDeposit;
        this.loanAmount = 0;
    }

    abstract void deposit(double amount);

    abstract boolean withdraw(double amount);

    double getBalance() { return balance; }

    void addLoanAmount(double loanAmount) { this.loanAmount += loanAmount; }

    double getLoanAmount() { return loanAmount; }

    public void setBalance(double balance) { this.balance = balance; }
}

// SavingsAccount class
class SavingsAccount extends Account {
    SavingsAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
    }

    @Override
    void deposit(double amount) {
        balance += amount;
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
    final double MAX_WITHDRAWAL;

    StudentAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
        MAX_WITHDRAWAL = 10000;
    }

    @Override
    void deposit(double amount) {
        balance += amount;
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
    static final double MIN_DEPOSIT = 100000;
    private boolean hasReachedMaturityPeriod;

    FixedDepositAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
        hasReachedMaturityPeriod = true;
    }

    @Override
    void deposit(double amount) {
        balance += amount;
    }

    @Override
    boolean withdraw(double amount) {
        if (hasReachedMaturityPeriod && balance - amount >= 0) {
            balance -= amount;
            hasReachedMaturityPeriod = false;
            return true;
        }
        return false;
    }

    public void setHasReachedMaturityPeriod(boolean hasReachedMaturityPeriod) {
        this.hasReachedMaturityPeriod = hasReachedMaturityPeriod;
    }
}