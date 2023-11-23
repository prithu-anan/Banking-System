package bankingSystem;


// Employee class
abstract public class Employee {
    protected String name;

    Employee(String name) {
        this.name = name;
    }

    abstract public void lookup(String accountHolder, Bank bank);

    abstract public void approveLoan(Bank bank);

    abstract public void changeInterestRate(String accountType, double newRate, Bank bank);

    abstract public void seeInternalFund(Bank bank);
}


// ManagingDirector class
class ManagingDirector extends Employee {
    ManagingDirector(String name) {
        super(name);
    }

    @Override
    public void lookup(String accountHolder, Bank bank) { bank.lookup(accountHolder); }

    @Override
    public void approveLoan(Bank bank) { bank.approveLoan(); }

    @Override
    public void changeInterestRate(String accountType, double newRate, Bank bank) {
        bank.changeInterestRate(accountType, newRate);
    }

    @Override
    public void seeInternalFund(Bank bank) { bank.seeInternalFund(); }
}


// Officer class
class Officer extends Employee {
    Officer(String name) {
        super(name);
    }

    @Override
    public void lookup(String accountHolder, Bank bank) {
        bank.lookup(accountHolder);
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
    public void seeInternalFund(Bank bank) {
        System.out.println("You don’t have permission for this operation");
    }
}


// Cashier class
class Cashier extends Employee {
    Cashier(String name) {
        super(name);
    }

    @Override
    public void lookup(String accountHolder, Bank bank) {
        bank.lookup(accountHolder);
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
    public void seeInternalFund(Bank bank) {
        System.out.println("You don’t have permission for this operation");
    }
}
