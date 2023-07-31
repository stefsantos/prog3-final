public class MoneySlot {
    private double balance;
    private double billBalance;

    public MoneySlot() {
        this.balance = 0.0;
    }

    public double getBalance() {
        return balance;
    }

    public void insertMoney(double amount) {
        balance += amount;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBillBalance() {
        return billBalance;
    }

    public void deductBalance(double amount) {
        balance -= amount;
    }

}
