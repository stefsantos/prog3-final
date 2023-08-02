package view;

import model.Item;
import model.VendingMachineModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialVendingMachineView extends VendingMachineView {

    private JButton customItemButton; // The "Custom" item button
    private Map<Item, ItemCustomPanel> itemPanelsMap; // Map to keep track of the custom item panels

    public SpecialVendingMachineView(VendingMachineModel model) {
        super(model);
    }

    @Override
    protected void setupGUI() {
        // Call the setupGUI method from the parent class
        super.setupGUI();

        // Add the "Custom" item button after all the regular vending machine item slots
        customItemButton = new JButton("<html>Custom</html>");

        // Set the layout manager for itemPanel to GridLayout with one column
        itemPanel.setLayout(new GridLayout(0, 1));

        // Get the preferred size of the largest button (itemButton or actionButton)
        Dimension largestButtonSize = getLargestButtonSize();

        // Set the preferred size of all buttons to match the width of the largest button
        setButtonPreferredSize(largestButtonSize);

        customItemButton.addActionListener(new CustomItemActionListener());

        // Add the customItemButton at the end of the itemPanel
        itemPanel.add(customItemButton);
    }

    // Helper method to get the preferred size of the largest button (itemButton or actionButton)
    private Dimension getLargestButtonSize() {
        Dimension itemButtonSize = getItemButtonSize();
        Dimension actionButtonSize = getActionButtonSize();
        return new Dimension(Math.max(itemButtonSize.width, actionButtonSize.width), itemButtonSize.height);
    }

    // Helper method to get the preferred size of the itemButton
    private Dimension getItemButtonSize() {
        return itemPanel.getComponent(0).getPreferredSize();
    }

    // Helper method to get the preferred size of the actionButton
    private Dimension getActionButtonSize() {
        return itemPanel.getComponent(1).getPreferredSize();
    }

    // Helper method to set the preferred size of all buttons to match the width of the largest button
    private void setButtonPreferredSize(Dimension preferredSize) {
        for (Component component : itemPanel.getComponents()) {
            if (component instanceof JButton) {
                ((JButton) component).setPreferredSize(preferredSize);
            }
        }
    }

    private class CustomItemActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a new custom item window and pass the main vending machine model reference
            CustomItemWindow customItemWindow = new CustomItemWindow(SpecialVendingMachineView.this.model);
            customItemWindow.setVisible(true);
        }
    }

    // Custom window class for the "Custom" button
    private class CustomItemWindow extends JFrame {
        private JCheckBox pearlsCheckBox;
        private JCheckBox cheesecakeCheckBox;
        private JCheckBox nataDeCocoCheckBox;
        private JCheckBox chocolateCheckBox;
        private JCheckBox aloeVeraCheckBox;
        private JCheckBox honeyCheckBox;
        private JButton createCustomMilkTeaButton;

        private VendingMachineModel mainVendingMachineModel; // Reference to the main vending machine model

        public CustomItemWindow(VendingMachineModel mainVendingMachineModel) {
            this.mainVendingMachineModel = mainVendingMachineModel;
            setTitle("Custom Item");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setPreferredSize(new Dimension(400, 400)); // Adjust the preferred size as needed
            setLayout(new BorderLayout());

            // Add the checkboxes for addons at the top
            pearlsCheckBox = new JCheckBox("Pearls");
            cheesecakeCheckBox = new JCheckBox("Cheesecake");
            nataDeCocoCheckBox = new JCheckBox("Nata de Coco");
            chocolateCheckBox = new JCheckBox("Chocolate");
            aloeVeraCheckBox = new JCheckBox("Aloe Vera");
            honeyCheckBox = new JCheckBox("Honey");

            JPanel checkBoxPanel = new JPanel(new GridLayout(2, 3));
            checkBoxPanel.add(pearlsCheckBox);
            checkBoxPanel.add(cheesecakeCheckBox);
            checkBoxPanel.add(nataDeCocoCheckBox);
            checkBoxPanel.add(chocolateCheckBox);
            checkBoxPanel.add(aloeVeraCheckBox);
            checkBoxPanel.add(honeyCheckBox);

            add(checkBoxPanel, BorderLayout.NORTH);

            // Create a scroll pane to hold the custom item panels
            JScrollPane scrollPane = new JScrollPane();
            add(scrollPane, BorderLayout.CENTER);

            JPanel mainPanel = new JPanel(new GridLayout(0, 1));

            List<Item> items = model.getItems();
            itemPanelsMap = new HashMap<>();
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (!item.getName().isEmpty()) { // Check if the item name is not empty
                    ItemCustomPanel itemCustomPanel = new ItemCustomPanel(item);
                    mainPanel.add(itemCustomPanel);
                    itemPanelsMap.put(item, itemCustomPanel);
                }
            }

            scrollPane.setViewportView(mainPanel); // Add the main panel to the scroll pane

            createCustomMilkTeaButton = new JButton("Create Custom Milk Tea");
            createCustomMilkTeaButton.addActionListener(new CreateCustomMilkTeaListener());
            add(createCustomMilkTeaButton, BorderLayout.SOUTH);

            pack();
            setLocationRelativeTo(null);
        }

        // Listener to handle the "Create Custom Milk Tea" button click
        private class CreateCustomMilkTeaListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean invalidInput = false;
                boolean insufficientStock = false;
        
                List<Item> updatedItems = new ArrayList<>();
        
                for (ItemCustomPanel itemPanel : itemPanelsMap.values()) {
                    int newStock = itemPanel.getNewStockQuantity();
                    Item item = itemPanel.getItem();
                    int currentStock = item.getStock();
        
                    // Check if the user entered a non-positive value
                    if (newStock < 0) {
                        invalidInput = true;
                        break;
                    }
        
                    // Check if there is enough stock of the item in the main vending machine
                    Item mainMachineItem = findItemByName(item.getName());
                    if (mainMachineItem != null && mainMachineItem.getStock() < newStock) {
                        insufficientStock = true;
                        break;
                    }
        
                    // Decrease the stock in the main vending machine
                    if (mainMachineItem != null) {
                        mainMachineItem.setStock(mainMachineItem.getStock() - newStock);
                        // Update the item in the main vending machine model
                        mainVendingMachineModel.updateItem(mainMachineItem);
                        updatedItems.add(mainMachineItem);
                    }
        
                    // Update the custom item panel's item stock
                    item.setStock(currentStock - newStock);
                    updatedItems.add(item);
                }
        
                if (invalidInput) {
                    JOptionPane.showMessageDialog(null, "Invalid input: Please enter a valid stock quantity.");
                } else if (insufficientStock) {
                    JOptionPane.showMessageDialog(null, "There is not enough stock for a certain item.");
                } else {
                    // Process selected addons (for demonstration purposes, just print them)
                    JOptionPane.showMessageDialog(null, "Custom Milk Tea created successfully!");
                    dispose();
        
                    // Reset the stock of custom items when the custom window is closed
                    for (Item item : updatedItems) {
                        // Update the item in the main vending machine model
                        mainVendingMachineModel.updateItem(item);
                    }
                }
            }
        }
        
        private Item findItemByName(String name) {
            List<Item> items = model.getItems();
            for (Item item : items) {
                if (item.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            }
            return null;
        }
    }

    // Custom panel class for each item in the custom window
    private class ItemCustomPanel extends JPanel {
        private Item item;
        private JTextField stockField;

        public ItemCustomPanel(Item item) {
            this.item = item;
            setupPanel();
        }

        private void setupPanel() {
            setLayout(new BorderLayout());

            JLabel itemLabel = new JLabel(item.getName()); // Show only the name of the item
            add(itemLabel, BorderLayout.CENTER);

            stockField = new JTextField();
            stockField.setPreferredSize(new Dimension(60, 30));
            add(stockField, BorderLayout.EAST);
        }

        public int getNewStockQuantity() {
            try {
                return Integer.parseInt(stockField.getText());
            } catch (NumberFormatException e) {
                return 0; // Return 0 if the input is invalid or empty
            }
        }

        public Item getItem() {
            return item;
        }
    }
}
