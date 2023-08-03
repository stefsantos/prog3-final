package view;

import model.Item;
import model.VendingMachineModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VendingMachineView extends JFrame {
    protected VendingMachineModel model;
    protected JPanel itemPanel;
    private JButton balanceButton;
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

        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
    }

    protected void setupGUI() {
        itemPanel = new JPanel(new GridLayout(model.getNumRows(), 1));
    
        List<Item> items = model.getItems();
        for (int i = 0; i < items.size(); i++) {
            ItemPanel itemPanel = new ItemPanel(items.get(i), false, this);
            this.itemPanel.add(itemPanel);
        }
    
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
    
        JScrollPane itemScrollPane = new JScrollPane(itemPanel);
        mainPanel.add(itemScrollPane, BorderLayout.CENTER);
    
        JPanel billPanel = new JPanel(new GridLayout(7, 3)); // 5 rows (4 rows for bills + 1 row for restock buttons)
        billPanel.add(insertP1Button);
        billPanel.add(insertP5Button);
        billPanel.add(insertP10Button);
        billPanel.add(insertP20Button);
        billPanel.add(insertP50Button);
        billPanel.add(insertP100Button);
        billPanel.add(insertP200Button);
        billPanel.add(insertP500Button);
        billPanel.add(insertP1000Button);
    
        JButton produceChangeButton = new JButton("Produce Change");
        produceChangeButton.addActionListener(new ProduceChangeActionListener());
        billPanel.add(produceChangeButton);
    
        JButton summaryButton = new JButton("Summary of Transactions");
        summaryButton.addActionListener(new SummaryButtonActionListener());
        billPanel.add(summaryButton);
    
        JButton collectPaymentButton = new JButton("Collect Payment");
        collectPaymentButton.addActionListener(new CollectPaymentActionListener());
        billPanel.add(collectPaymentButton);
    
        // Add the restock buttons below the last row of billPanel
        JButton restock1Button = createRestockButton("Restock 1 Peso", 1);
        JButton restock5Button = createRestockButton("Restock 5 Pesos", 5);
        JButton restock10Button = createRestockButton("Restock 10 Pesos", 10);
        JButton restock20Button = createRestockButton("Restock 20 Pesos", 20);
        JButton restock50Button = createRestockButton("Restock 50 Pesos", 50);
        JButton restock100Button = createRestockButton("Restock 100 Pesos", 100);
        JButton restock200Button = createRestockButton("Restock 200 Pesos", 200);
        JButton restock500Button = createRestockButton("Restock 500 Pesos", 500);
        JButton restock1000Button = createRestockButton("Restock 1000 Pesos", 1000);
    
        billPanel.add(restock1Button);
        billPanel.add(restock5Button);
        billPanel.add(restock10Button);
        billPanel.add(restock20Button);
        billPanel.add(restock50Button);
        billPanel.add(restock100Button);
        billPanel.add(restock200Button);
        billPanel.add(restock500Button);
        billPanel.add(restock1000Button);
    
        mainPanel.add(billPanel, BorderLayout.SOUTH);
    
        add(mainPanel);
    }
    
    private JButton createInsertButton(String buttonText, double billAmount) {
        JButton button = new JButton(buttonText);
        button.addActionListener(new InsertBillActionListener(billAmount));
        return button;
    }
    
    private JButton createRestockButton(String buttonText, int denomination) {
        JButton button = new JButton(buttonText);
        button.addActionListener(new RestockButtonActionListener(denomination));
        return button;
    }

    void updateBalanceButton() {
        String balanceText = String.format("Balance: P%.2f", model.getMoneySlot().getBalance());
        balanceButton.setText(balanceText);
    }

    public void updateBalanceLabel() {
        double balance = model.getMoneySlot().getBalance();
        String balanceText = String.format("Balance: P%.2f", balance);
        balanceLabel.setText(balanceText);
    }

    private void updateBillBalanceLabel() {
        String billBalanceText = String.format("Bill Balance: P%.2f", model.getMoneySlot().getBillBalance());
        billBalanceLabel.setText(billBalanceText);
    }

    private class DefaultActionListener implements ActionListener {
        private boolean isMaintenanceMode = true;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isMaintenanceMode) {
                isMaintenanceMode = false;
                defaultButton.setText("Default");

                Component[] components = itemPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof ItemPanel) {
                        ((ItemPanel) component).toggleRestockButton(true);
                    }
                }
            } else {
                isMaintenanceMode = true;
                defaultButton.setText("Maintenance");

                Component[] components = itemPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof ItemPanel) {
                        ((ItemPanel) component).toggleRestockButton(false);
                    }
                }
            }

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
            updateBalanceLabel();
            updateBalanceButton();
        }
    }

    private class CollectPaymentActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double totalEarnings = model.getTotalEarnings();
            if (totalEarnings <= 0.0) {
                showMessageDialog("No earnings to collect.");
                return;
            }

            List<Integer> billsUsed = calculateBillsUsedForChange(totalEarnings);
            showChangeMessageDialog(billsUsed, "Earnings collected: P" + String.format("%.2f", totalEarnings));

            // Reset the total earnings after collecting
            model.resetTotalEarnings();
            updateBillBalanceLabel();
        }
    }
    
    
    

    // Helper method to calculate bills used for change
    private List<Integer> calculateBillsUsedForChange(double balance) {
        int[] denominations = { 1000, 500, 200, 100, 50, 20, 10, 5, 1 };
        List<Integer> billsUsed = new ArrayList<>();

        for (int denomination : denominations) {
            int count = (int) (balance / denomination);
            balance -= count * denomination;
            billsUsed.add(count);
        }

        return billsUsed;
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "", JOptionPane.INFORMATION_MESSAGE);
    }
    // Inside VendingMachineView class
    private class ProduceChangeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double balance = model.getMoneySlot().getBalance();
            if (balance <= 0.0) {
                showMessageDialog("No change to produce.");
                return;
            }

            List<Integer> billsUsed = calculateBillsUsedForChange(balance);
            showChangeMessageDialog(billsUsed, "Change: P" + String.format("%.2f", balance));

            // Reset the balance after producing change
            model.getMoneySlot().resetBalance();
            updateBalanceLabel();
            updateBalanceButton();
        }
    }

    private class SummaryButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> transactionRecords = model.getTransactionRecords();
            if (transactionRecords.isEmpty()) {
                showMessageDialog("No transactions recorded since last restocking.");
            } else {
                StringBuilder summaryMessage = new StringBuilder("Summary of Transactions:\n\n");
                double totalSales = 0.0;
    
                for (String transaction : transactionRecords) {
                    summaryMessage.append(transaction).append("\n");
                    totalSales += extractPriceFromTransaction(transaction);
                }
    
                summaryMessage.append("\nTotal Sales: P").append(String.format("%.2f", totalSales));
    
                showMessageDialog(summaryMessage.toString());
            }
        }
    
        private double extractPriceFromTransaction(String transaction) {
            String priceSubstring = transaction.substring(transaction.lastIndexOf("P") + 1);
            return Double.parseDouble(priceSubstring);
        }
    }
    
    

    private void showChangeMessageDialog(List<Integer> billsUsed, String message) {
        StringBuilder messageBuilder = new StringBuilder(message);
        messageBuilder.append("\nDispensing:\n\n");

        int[] denominations = { 1000, 500, 200, 100, 50, 20, 10, 5, 1 };
        for (int i = 0; i < denominations.length; i++) {
            int denomination = denominations[i];
            int count = billsUsed.get(i);

            if (count > 0) {
                messageBuilder.append("P").append(denomination).append(" bills: ").append(count).append("\n");
            }
        }

        showMessageDialog(messageBuilder.toString());
    }

    private class RestockButtonActionListener implements ActionListener {
        private int denomination;
    
        public RestockButtonActionListener(int denomination) {
            this.denomination = denomination;
        }
    
        @Override
        public void actionPerformed(ActionEvent e) {
            String restockQuantityStr = JOptionPane.showInputDialog(
                    null,
                    "Enter the quantity to restock for " + denomination + " Pesos:",
                    1
            );
    
            if (restockQuantityStr != null) {
                try {
                    int restockQuantity = Integer.parseInt(restockQuantityStr);
                    if (restockQuantity >= 0) {
                        // Call the restock method in the VendingMachineModel to update the stock for this denomination
                        model.restock(denomination, restockQuantity);
                        updateBalanceLabel(); // Update the balance label to reflect changes
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Invalid restock quantity. Please enter a non-negative integer.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Invalid input. Please enter a valid quantity.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
    

}
