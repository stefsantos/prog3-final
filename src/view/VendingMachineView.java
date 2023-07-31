package view;
import javax.swing.*;

import model.Item;
import model.VendingMachineModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VendingMachineView extends JFrame {
    protected VendingMachineModel model;
    protected JPanel itemPanel;
    private JButton balanceButton; // Added balanceButton
    private JLabel balanceLabel;
    private JLabel billBalanceLabel;
    private JButton defaultButton;
    private JButton insertP1Button;
    private JButton insertP5Button;
    private JButton insertP10Button;
    private JButton insertP20Button;
    private JButton insertP50Button;
    private JButton insertP100Button;
    private JButton insertP200Button;
    private JButton insertP500Button;
    private JButton insertP1000Button;

    

    public VendingMachineView(VendingMachineModel model) {
        this.model = model;
        setTitle("Vending Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupGUI();

        setPreferredSize(new Dimension(800, 600)); // Set the preferred size of the vending machine view
        pack();
        setLocationRelativeTo(null);
    }

    protected void setupGUI() {
        itemPanel = new JPanel(new GridLayout(model.getNumRows(), 1));

        List<Item> items = model.getItems();
        for (int i = 0; i < items.size(); i++) {
            ItemPanel itemPanel = new ItemPanel(items.get(i), false);
            this.itemPanel.add(itemPanel);
        }

        JButton produceChangeButton = new JButton("Produce Change");
        produceChangeButton.addActionListener(e -> {
            model.produceChange();
            updateBalanceLabel();
            updateBillBalanceLabel();
        });

        defaultButton = new JButton("Maintenance");
        defaultButton.addActionListener(new DefaultActionListener());

        insertP1Button = createInsertButton("Insert P1", 1.0);
        insertP5Button = createInsertButton("Insert P5", 5.0);
        insertP10Button = createInsertButton("Insert P10", 10.0);
        insertP20Button = createInsertButton("Insert P20", 20.0);
        insertP50Button = createInsertButton("Insert P50", 50.0);
        insertP100Button = createInsertButton("Insert P100", 100.0);
        insertP200Button = createInsertButton("Insert P200", 200.0);
        insertP500Button = createInsertButton("Insert P500", 500.0);
        insertP1000Button = createInsertButton("Insert P1000", 1000.0);

        balanceLabel = new JLabel("Balance: P0.00");
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        billBalanceLabel = new JLabel("Bill Balance: P0.00");
        billBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(defaultButton);
        topPanel.add(balanceLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Wrap the itemPanel with a JScrollPane
        JScrollPane itemScrollPane = new JScrollPane(itemPanel);
        mainPanel.add(itemScrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(1, 2));
        controlPanel.add(produceChangeButton);
        controlPanel.add(billBalanceLabel);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        JPanel billPanel = new JPanel(new GridLayout(3, 3));
        billPanel.add(insertP1Button);
        billPanel.add(insertP5Button);
        billPanel.add(insertP10Button);
        billPanel.add(insertP20Button);
        billPanel.add(insertP50Button);
        billPanel.add(insertP100Button);
        billPanel.add(insertP200Button);
        billPanel.add(insertP500Button);
        billPanel.add(insertP1000Button);

        mainPanel.add(billPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void updateBalanceButton() {
        String balanceText = String.format("Balance: P%.2f", model.getMoneySlot().getBalance());
        balanceButton.setText(balanceText);
    }

    public void updateBalanceLabel() {
        String balanceText = String.format("Balance: P%.2f", model.getMoneySlot().getBalance());
        balanceLabel.setText(balanceText);
    }

    private void updateBillBalanceLabel() {
        String billBalanceText = String.format("Bill Balance: P%.2f", model.getMoneySlot().getBillBalance());
        billBalanceLabel.setText(billBalanceText);
    }

    private JButton createInsertButton(String buttonText, double billAmount) {
        JButton button = new JButton(buttonText);
        button.addActionListener(new InsertBillActionListener(billAmount));
        return button;
    }

    private class DefaultActionListener implements ActionListener {
        private boolean isMaintenanceMode = false; // Initialize to false

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isMaintenanceMode) {
                // Switch back to default mode
                isMaintenanceMode = false;
                defaultButton.setText("Default");

                Component[] components = itemPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof ItemPanel) {
                        ((ItemPanel) component).toggleRestockButton(true);
                    }
                }
            } else {
                // Switch to maintenance mode
                isMaintenanceMode = true;
                defaultButton.setText("Maintenance");

                Component[] components = itemPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof ItemPanel) {
                        ((ItemPanel) component).toggleRestockButton(false);
                    }
                }
            }

            // Update restock/buy button state after toggling
            updateItemButtons();
        }
    }

    private void updateItemButtons() {
        Component[] components = itemPanel.getComponents();
        for (Component component : components) {
            if (component instanceof ItemPanel) {
                ((ItemPanel) component).updateActionButton();
            }
        }
    }

    private class InsertBillActionListener implements ActionListener {
        private double billAmount;

        public InsertBillActionListener(double billAmount) {
            this.billAmount = billAmount;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            model.getMoneySlot().insertMoney(billAmount);
            updateBalanceLabel(); // Update the balance label as before
            updateBalanceButton(); // Update the balance button with the new balance
        }
    }

    public void buyCustomItem(List<Item> selectedItems) {
        if (model == null) {
            showMessageDialog("Please create a vending machine first.");
            return;
        }

        // Calculate the total price of selected items
        double totalPrice = 0.0;
        for (Item selectedItem : selectedItems) {
            totalPrice += selectedItem.getPrice();
        }

        // Check if there is sufficient balance
        double balance = model.getMoneySlot().getBalance();
        if (balance < totalPrice) {
            showMessageDialog("Insufficient balance. Please insert more money.");
            return;
        }

        // Deduct the total price from the balance
        model.getMoneySlot().deductBalance(totalPrice);

        // Update the stocks of selected items in the main vending machine
        for (Item selectedItem : selectedItems) {
            int rowIndex = model.getItems().indexOf(selectedItem);
            model.buyItem(rowIndex);
        }

        // Update the balance label and item buttons
        updateBalanceLabel();
        updateItemButtons();
        showMessageDialog("Custom items purchased successfully!");
    }

    private void showMessageDialog(String string) {
    }
    
}