package controller;
import java.util.List;

import javax.swing.JOptionPane;

import model.Item;
import model.VendingMachineModel;
import view.MainMenu;
import view.SpecialVendingMachineView;
import view.VendingMachineView;

public class VendingMachineController {
    private VendingMachineModel model;
    private MainMenu mainMenu;

    public VendingMachineController() {
        this.mainMenu = new MainMenu(this);
        this.mainMenu.setVisible(true);
    }

    public void showMainMenu() {
        mainMenu.setVisible(true);
    }

   public void createRegularVendingMachine(int numRows) {
        this.model = new VendingMachineModel(numRows);
        int maxStock = promptMaxStockForAllItems(); // Prompt the user to enter maximum stock for all items
        setMaxStockForAllItems(maxStock); // Set the maximum stock for all items
        VendingMachineView vendingMachineView = new VendingMachineView(model);
        vendingMachineView.setVisible(true);
        vendingMachineView.updateBalanceLabel();
    }

    public void createSpecialVendingMachine(int numRows) {
        this.model = new VendingMachineModel(numRows);
        int maxStock = promptMaxStockForAllItems(); // Prompt the user to enter maximum stock for all items
        setMaxStockForAllItems(maxStock); // Set the maximum stock for all items
        SpecialVendingMachineView specialVendingMachineView = new SpecialVendingMachineView(model);
        specialVendingMachineView.setVisible(true);
        specialVendingMachineView.updateBalanceLabel();
    }

    private int promptMaxStockForAllItems() {
        String input = JOptionPane.showInputDialog(
                null,
                "Enter the maximum stock for all items:",
                "Maximum Stock",
                JOptionPane.QUESTION_MESSAGE
        );
        if (input != null) {
            try {
                int maxStock = Integer.parseInt(input);
                return maxStock;
            } catch (NumberFormatException e) {
                // Handle invalid input (not a valid integer)
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid input. Please enter a valid integer.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );
                // Retry the input
                return promptMaxStockForAllItems();
            }
        } else {
            // User canceled the prompt, exit the program
            System.exit(0);
            return 0; // Not necessary, but added for clarity
        }
    }

    private void setMaxStockForAllItems(int maxStock) {
        List<Item> items = model.getItems();
        for (Item item : items) {
            item.setMaxStock(maxStock);
        }
    }
}
