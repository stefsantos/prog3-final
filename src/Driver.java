import javax.swing.*;

import controller.VendingMachineController;

public class Driver {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { 
            VendingMachineController vendingMachineController = new VendingMachineController();
            vendingMachineController.showMainMenu();
        });
    }
}