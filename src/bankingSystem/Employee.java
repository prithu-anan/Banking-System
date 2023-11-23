package bankingSystem;

// Employee class
abstract public class Employee {
    protected String name;

    Employee(String name) {
        this.name = name;
    }

    abstract void lookup(String accountHolder, Bank bank);

    abstract void approveLoan(Bank bank);

    abstract void changeInterestRate(String accountType, double newRate, Bank bank);

    abstract void seeInternalFund(Bank bank);
}

// ManagingDirector class
class ManagingDirector extends Employee {
    ManagingDirector(String name) {
        super(name);
    }

    @Override
    void lookup(String accountHolder, Bank bank) {
        bank.lookup(accountHolder);
    }

    @Override
    void approveLoan(Bank bank) {
        bank.approveLoan();
    }

    @Override
    void changeInterestRate(String accountType, double newRate, Bank bank) {
        bank.changeInterestRate(accountType, newRate);
    }

    @Override
    void seeInternalFund(Bank bank) {
        bank.seeInternalFund();
    }
}

// Officer class
class Officer extends Employee {
    Officer(String name) {
        super(name);
    }

    @Override
    void lookup(String accountHolder, Bank bank) {
        bank.lookup(accountHolder);
    }

    @Override
    void approveLoan(Bank bank) {
        bank.approveLoan();
    }

    @Override
    void changeInterestRate(String accountType, double newRate, Bank bank) {
        System.out.println("You don’t have permission for this operation");
    }

    @Override
    void seeInternalFund(Bank bank) {
        System.out.println("You don’t have permission for this operation");
    }
}

// Cashier class
class Cashier extends Employee {
    Cashier(String name) {
        super(name);
    }

    @Override
    void lookup(String accountHolder, Bank bank) {
        bank.lookup(accountHolder);
    }

    @Override
    void approveLoan(Bank bank) {
        System.out.println("You don’t have permission for this operation");
    }

    @Override
    void changeInterestRate(String accountType, double newRate, Bank bank) {
        System.out.println("You don’t have permission for this operation");
    }

    @Override
    void seeInternalFund(Bank bank) {
        System.out.println("You don’t have permission for this operation");
    }
}
