package bankingSystem;


// Employee class
abstract public class Employee {
    protected String name;

    Employee(String name) {
        this.name = name;
    }

    abstract public double lookup(String accountHolder, Bank bank);

    abstract public void approveLoan(Bank bank);

    abstract public void changeInterestRate(String accountType, double newRate, Bank bank);

    abstract public double seeInternalFund(Bank bank);
}


// ManagingDirector class
class ManagingDirector extends Employee {
    ManagingDirector(String name) {
        super(name);
    }

    @Override
    public double lookup(String accountHolder, Bank bank) { return bank.lookup(accountHolder); }

    @Override
    public void approveLoan(Bank bank) { bank.approveLoan(); }

    @Override
    public void changeInterestRate(String accountType, double newRate, Bank bank) {
        bank.changeInterestRate(accountType, newRate);
    }

    @Override
    public double seeInternalFund(Bank bank) { return bank.seeInternalFund(); }
}


// Officer class
class Officer extends Employee {
    Officer(String name) {
        super(name);
    }

    @Override
    public double lookup(String accountHolder, Bank bank) {
        return bank.lookup(accountHolder);
    }

    @Override
    public void approveLoan(Bank bank) {
        bank.approveLoan();
    }

    @Override
    public void changeInterestRate(String accountType, double newRate, Bank bank) {
        System.out.println("You don’t have permission for this operation");
    }

    @Override
    public double seeInternalFund(Bank bank) {
        System.out.println("You don’t have permission for this operation");
        return 0;
    }
}


// Cashier class
class Cashier extends Employee {
    Cashier(String name) {
        super(name);
    }

    @Override
    public double lookup(String accountHolder, Bank bank) {
        return bank.lookup(accountHolder);
    }

    @Override
    public void approveLoan(Bank bank) {
        System.out.println("You don’t have permission for this operation");
    }

    @Override
    public void changeInterestRate(String accountType, double newRate, Bank bank) {
        System.out.println("You don’t have permission for this operation");
    }

    @Override
    public double seeInternalFund(Bank bank) {
        System.out.println("You don’t have permission for this operation");
        return 0;
    }
}
