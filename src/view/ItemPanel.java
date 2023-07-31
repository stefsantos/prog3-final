package view;
import javax.swing.*;

import model.Item;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemPanel extends JPanel {
    private Item item;
    private JButton itemButton;
    private JButton actionButton;
    private boolean isRestockMode;

    public ItemPanel(Item item, boolean isRestockMode) {
        this.item = item;
        this.isRestockMode = isRestockMode;
        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());

        itemButton = new JButton(getItemButtonText());
        itemButton.setPreferredSize(new Dimension(100, 100)); // Set a fixed size for itemButton
        itemButton.addActionListener(new EditActionListener());
        add(itemButton, BorderLayout.CENTER);

        actionButton = new JButton();
        actionButton.setPreferredSize(new Dimension(100, 75)); // Set a fixed size for actionButton
        actionButton.addActionListener(new ActionButtonListener());
        updateActionButton();
        add(actionButton, BorderLayout.EAST);
    }

    private String getItemButtonText() {
        return "<html><b>Name:</b> " + item.getName() +
                "<br><b>Stock:</b> " + item.getStock() + "/" + item.getMaxStock() +
                "<br><b>Calories:</b> " + item.getCalories() +
                "<br><b>Price:</b> P" + String.format("%.2f", item.getPrice()) + "</html>";
    }

    private class EditActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String newName = JOptionPane.showInputDialog(null, "Enter the new name:", item.getName());
            if (newName != null) {
                int newMaxStock;
                do {
                    String maxStockStr = JOptionPane.showInputDialog(null, "Enter the new max stock (should not be greater than " + item.getMaxStock() + "):", item.getMaxStock());
                    if (maxStockStr == null) {
                        return; // User canceled the input, exit without saving changes
                    }
                    newMaxStock = Integer.parseInt(maxStockStr);
                } while (newMaxStock > item.getMaxStock());

                double newPrice;
                do {
                    String priceStr = JOptionPane.showInputDialog(null, "Enter the new price:", item.getPrice());
                    if (priceStr == null) {
                        return; // User canceled the input, exit without saving changes
                    }
                    newPrice = Double.parseDouble(priceStr);
                } while (newPrice < 0); // Ensure price is non-negative

                // Update the item's information
                item.setName(newName);
                item.setMaxStock(newMaxStock);
                item.setPrice(newPrice);

                // Update the item button text
                itemButton.setText(getItemButtonText());
            }
        }
    }

    public void toggleRestockButton(boolean isRestockMode) {
        this.isRestockMode = isRestockMode;
        updateActionButton();
    }

    public void updateActionButton() {
        String buttonText = isRestockMode ? "Restock" : (item.getStock() > 0 ? "Buy" : "Out of Stock");
        actionButton.setText(buttonText);
    }

    private class ActionButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isRestockMode) {
                // Restock mode: Prompt the user for restock quantity and restock
                String restockQuantityStr = JOptionPane.showInputDialog(null, "Enter the quantity to restock:", 1);
                if (restockQuantityStr != null) {
                    try {
                        int restockQuantity = Integer.parseInt(restockQuantityStr);
                        if (restockQuantity >= 0) {
                            int currentStock = item.getStock();
                            int maxStock = item.getMaxStock();
                            int newStock = currentStock + restockQuantity;

                            if (newStock > maxStock) {
                                // Show a warning message when restocking exceeds the max stock
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Restocking quantity exceeds the maximum stock.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE
                                );
                                return; // Exit the restocking process if the quantity exceeds the max stock
                            }

                            item.setStock(newStock);
                            itemButton.setText(getItemButtonText());
                            updateActionButton();
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid restock quantity. Please enter a non-negative integer.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                // Buy mode: Buy one stock of the item
                if (item.getStock() > 0) {
                    item.setStock(item.getStock() - 1);
                    itemButton.setText(getItemButtonText());
                    updateActionButton();
                    // Show a prompt that the item has been dispensed
                    JOptionPane.showMessageDialog(
                            null,
                            "Item dispensed: " + item.getName(),
                            "Item Dispensed",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    // Show a message if the item is out of stock
                    JOptionPane.showMessageDialog(
                            null,
                            "Item out of stock.",
                            "Out of Stock",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }    
}