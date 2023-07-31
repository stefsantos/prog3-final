import javax.swing.*;
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
        itemButton.setPreferredSize(new Dimension(100, 50)); // Set a fixed size for itemButton
        itemButton.addActionListener(new EditActionListener());
        add(itemButton, BorderLayout.CENTER);

        actionButton = new JButton(isRestockMode ? "Restock" : "Buy");
        actionButton.setPreferredSize(new Dimension(100, 75)); // Set a fixed size for actionButton
        actionButton.addActionListener(new ActionButtonListener());
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
                int newMaxStock = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the new max stock:", item.getMaxStock()));
                int newCalories = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the new calories:", item.getCalories()));
                double newPrice = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter the new price:", item.getPrice()));

                // Update the item's information
                item.setName(newName);
                item.setMaxStock(newMaxStock);
                item.setCalories(newCalories);
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
        actionButton.setText(isRestockMode ? "Restock" : "Buy");
    }
    

    private class ActionButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isRestockMode) {
                // Restock mode: Prompt the user for restock quantity and restock
                String restockQuantityStr = JOptionPane.showInputDialog(null, "Enter the quantity to restock:", 1);
                if (restockQuantityStr != null) {
                    int restockQuantity = Integer.parseInt(restockQuantityStr);
                    if (restockQuantity >= 0) {
                        int currentStock = item.getStock();
                        int newStock = Math.min(item.getMaxStock(), currentStock + restockQuantity);
                        item.setStock(newStock);
                        itemButton.setText(getItemButtonText());
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid restock quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                // Buy mode: Buy one stock of the item
                int currentStock = item.getStock();
                if (currentStock > 0) {
                    item.setStock(currentStock - 1);
                    itemButton.setText(getItemButtonText());
                } else {
                    JOptionPane.showMessageDialog(null, "Item out of stock.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}