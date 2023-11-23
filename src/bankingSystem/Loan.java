package bankingSystem;

// Loan class
record Loan(double amount) {
    public double getInterest() {
        double INTEREST_RATE = 0.10;
        return amount * INTEREST_RATE;
    }
}