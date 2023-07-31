package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendingMachineModel {
    private int numRows;
    private List<Item> items;
    private MoneySlot moneySlot;
    private Map<Double, Integer> billStocks;

    public VendingMachineModel(int numRows) {
        this.numRows = numRows;
        this.items = new ArrayList<>();
        this.moneySlot = new MoneySlot();
        this.billStocks = new HashMap<>();
        presetItems();
        presetBills();
        createEmptySlots();
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        // If numRows is changed, reset the items list and preset items
        items.clear();
        presetItems();
    }

    public List<Item> getItems() {
        return items;
    }

    public MoneySlot getMoneySlot() {
        return moneySlot;
    }

    private void presetItems() {
        // Define the preset items
        Item[] presetItems = new Item[10];
        presetItems[0] = new Item("Roasted Milk Tea", 10, 150, 100.00);
        presetItems[1] = new Item("Tiramisu Milk Tea", 10, 200, 100.00);
        presetItems[2] = new Item("Lychee Milk Tea", 10, 180, 100.00);
        presetItems[3] = new Item("Strawberry Milk Tea", 10, 170, 100.00);
        presetItems[4] = new Item("Honeydew Milk Tea", 10, 160, 100.00);
        presetItems[5] = new Item("Oolong Milk Tea", 10, 110, 100.00);
        presetItems[6] = new Item("Hazelnut Milk Tea", 10, 190, 100.00);
        presetItems[7] = new Item("Coffee Milk Tea", 10, 160, 100.00);
        presetItems[8] = new Item("Choco-berry Milk Tea", 10, 210, 100.00);
        presetItems[9] = new Item("Honeydew Milk Tea", 10, 150, 100.00);

        // Populate the items list with the preset items
        for (int i = 0; i < Math.min(numRows, presetItems.length); i++) {
            items.add(presetItems[i]);
        }
    }

    private void presetBills() {
        // Define the preset bills and their initial stock
        double[] billDenominations = { 1.0, 5.0, 10.0, 20.0, 50.0, 100.0, 200.0, 500.0, 1000.0 };
        int initialStock = 10;

        for (double denomination : billDenominations) {
            billStocks.put(denomination, initialStock);
        }
    }

    public void insertBill(double denomination) {
        Integer currentStock = billStocks.get(denomination);
        if (currentStock != null) {
            billStocks.put(denomination, currentStock + 1);
            double balance = moneySlot.getBalance();
            balance += denomination;
            moneySlot.setBalance(balance);
        }
    }

    public Map<Double, Integer> getBillStocks() {
        return billStocks;
    }

    private void createEmptySlots() {
        int numEmptySlots = Math.max(0, numRows - items.size());
        for (int i = 0; i < numEmptySlots; i++) {
            items.add(new Item("", 0, 0, 0.0)); // Empty item slots
        }
    }

    public void restockItem(int rowIndex, int quantity) {
        if (rowIndex >= 0 && rowIndex < numRows) {
            Item item = items.get(rowIndex);
            if (item != null) {
                int maxStock = item.getMaxStock();
                int currentStock = item.getStock();

                if (quantity >= 0) {
                    int newStock = Math.min(maxStock, currentStock + quantity);
                    item.setStock(newStock);
                }
            }
        }
    }

    public boolean buyItem(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < numRows) {
            Item item = items.get(rowIndex);
            if (item != null) {
                int currentStock = item.getStock();
                double itemPrice = item.getPrice();
                double balance = moneySlot.getBalance();

                if (currentStock > 0 && balance >= itemPrice) {
                    item.setStock(currentStock - 1);
                    moneySlot.setBalance(balance - itemPrice);
                    //updateLabel();
                    return true;
                }
            }
        }
        return false;
    }

    public void produceChange() {
        double balance = moneySlot.getBalance();

        // Define the denominations in descending order (larger denominations first)
        double[] denominations = { 1000.0, 500.0, 200.0, 100.0, 50.0, 20.0, 10.0, 5.0, 1.0 };

        StringBuilder changeInfo = new StringBuilder("Dispensing change:\n");

        for (double denomination : denominations) {
            int numBills = (int) (balance / denomination);
            if (numBills > 0) {
                balance -= numBills * denomination;
                changeInfo.append("P").append((int) denomination).append(" bill(s): ").append(numBills).append("\n");
            }
        }

        if (balance > 0.0) {
            // If there's still a remaining balance that couldn't be dispensed in denominations
            changeInfo.append("Remaining balance: P").append(String.format("%.2f", balance)).append("\n");
        }

        System.out.println(changeInfo.toString()); // Replace with UI display
        moneySlot.setBalance(0.0);
    }
}