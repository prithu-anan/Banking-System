import bankingSystem.Bank;
import bankingSystem.Employee;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);
        Set<String> employees = new HashSet<>(Arrays.asList("MD", "O1", "O2", "C1", "C2", "C3", "C4", "C5"));

        System.out.println("Enter a command (type 'exit' to quit): ");

        while (true) {
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the bank application. Goodbye!");
                break;
            }

            processCommand(command, bank, employees, scanner);
        }

        scanner.close();
        bank.printOperationsList();
    }

    // Method to process the input command
    private static void processCommand(String command, Bank bank, Set<String> employees, Scanner scanner) {
        String[] parts = command.split(" ");

        switch (parts[0].toLowerCase()) {
            case "create":
                if (parts.length == 4 && !employees.contains(parts[1].toUpperCase())) {
                    String name = parts[1];
                    String accountType = parts[2];

                    try {
                        double initialDeposit = Double.parseDouble(parts[3].replaceAll(",", ""));
                        bank.createAccount(name, accountType, initialDeposit);
                        processAccountCommands(scanner, bank, name);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid initial deposit format.");
                    }

                } else {
                    System.out.println("Invalid 'create' command format or invalid employee name.");
                }
                break;

            case "open":
                if (parts.length == 2) {
                    String name = parts[1];

                    // Process account or employee commands based on the name
                    if (employees.contains(name.toUpperCase())) {
                        if(bank.isLoanRequestPending())
                            System.out.println(name + " active, there are loan approvals pending");
                        else System.out.println(name + " active");
                        bank.addOperation(command, name);
                        processEmployeeCommands(scanner, bank, name);
                    } else {
                        if(bank.isExist(name)) {
                            System.out.println("Welcome back, " + name);
                            bank.addOperation(command, name);
                            processAccountCommands(scanner, bank, name);
                        }
                        else System.out.println("Account Name Doesn't Exist.");
                    }
                } else {
                    System.out.println("Invalid 'open' command format.");
                }
                break;

            case "inc":
                if (parts.length == 1) {
                    bank.incrementYear();
                } else {
                    System.out.println("Invalid 'increment' command format.");
                }
                break;

            default:
                System.out.println("Unknown command: " + command);
                break;
        }
    }

    // Method to process account commands
    private static void processAccountCommands(Scanner scanner, Bank bank, String name) {
        while (true) {
            String input = scanner.nextLine();
            boolean close;

            close = processAccountCommand(input, bank, name);
            if (close) break;
        }
    }

    // Method to process employee commands
    private static void processEmployeeCommands(Scanner scanner, Bank bank, String name) {
        while (true) {
            String input = scanner.nextLine();
            boolean close;

            close = processEmployeeCommand(input, bank, name);
            if (close) break;
        }
    }

    // Method to process account command
    private static boolean processAccountCommand(String command, Bank bank, String name) {
        String[] parts = command.split(" ");

        switch (parts[0].toLowerCase()) {
            case "deposit":
                if (parts.length == 2) {

                    try {
                        double amount = Double.parseDouble(parts[1].replaceAll(",", ""));
                        bank.addOperation(command, name);
                        bank.deposit(name, amount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid deposit amount format.");
                    }

                } else {
                    System.out.println("Invalid 'deposit' command format.");
                }
                break;

            case "withdraw":
                if (parts.length == 2) {

                    try {
                        double amount = Double.parseDouble(parts[1].replaceAll(",", ""));
                        bank.addOperation(command, name);
                        bank.withdraw(name, amount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid withdraw amount format.");
                    }

                } else {
                    System.out.println("Invalid 'withdraw' command format.");
                }
                break;

            case "request":
                if (parts.length == 2) {

                    try {
                        double amount = Double.parseDouble(parts[1].replaceAll(",", ""));
                        bank.addOperation(command, name);
                        bank.requestLoan(name, amount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid loan request amount format.");
                    }

                } else {
                    System.out.println("Invalid 'request' command format.");
                }
                break;

            case "query":
                if (parts.length == 1) {
                    bank.addOperation(command, name);
                    bank.queryBalance(name);
                } else {
                    System.out.println("Invalid 'query' command format.");
                }
                break;

            case "close":
                bank.addOperation(command, name);
                System.out.println("Transaction for " + name + " closed");
                return true;

            default:
                System.out.println("Unknown command: " + command);
                break;
        }

        return false;
    }

    // Method to process employee command
    private static boolean processEmployeeCommand(String command, Bank bank, String name) {
        String[] parts = command.split(" ");
        Employee employee = bank.getEmployee(name);

        switch (parts[0].toLowerCase()) {
            case "lookup":
                if (parts.length == 2) {
                    String accountHolder = parts[1];
                    bank.addOperation(command, name);
                    employee.lookup(accountHolder, bank);
                } else {
                    System.out.println("Invalid 'lookup' command format.");
                }
                break;

            case "approve":
                if (parts.length == 2 && parts[1].equalsIgnoreCase("loan")) {
                    bank.addOperation(command, name);
                    employee.approveLoan(bank);
                } else {
                    System.out.println("Invalid 'approve loan' command format.");
                }
                break;

            case "change":
                if (parts.length == 3) {

                    try {
                        double newRate = Double.parseDouble(parts[2].replaceAll(",", ""));
                        bank.addOperation(command, name);
                        employee.changeInterestRate(parts[1], newRate, bank);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid interest rate format.");
                    }

                } else {
                    System.out.println("Invalid 'change interest rate' command format.");
                }
                break;

            case "see":
                if (parts.length == 1) {
                    bank.addOperation(command, name);
                    employee.seeInternalFund(bank);
                } else {
                    System.out.println("Invalid 'see' command format.");
                }
                break;

            case "close":
                bank.addOperation(command, name);
                System.out.println("Operations for " + name + " closed");
                return true;

            default:
                System.out.println("Unknown command: " + command);
                break;
        }

        return false;
    }
}
