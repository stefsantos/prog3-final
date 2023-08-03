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

    private JButton customItemButton;
    private Map<Item, ItemCustomPanel> itemPanelsMap;

    public SpecialVendingMachineView(VendingMachineModel model) {
        super(model);
    }

    @Override
    protected void setupGUI() {
        super.setupGUI();

        customItemButton = new JButton("<html>Custom</html>");

        itemPanel.setLayout(new GridLayout(0, 1));
        Dimension largestButtonSize = getLargestButtonSize();
        setButtonPreferredSize(largestButtonSize);

        customItemButton.addActionListener(new CustomItemActionListener());

        itemPanel.add(customItemButton);
    }

    private Dimension getLargestButtonSize() {
        Dimension itemButtonSize = getItemButtonSize();
        Dimension actionButtonSize = getActionButtonSize();
        return new Dimension(Math.max(itemButtonSize.width, actionButtonSize.width), itemButtonSize.height);
    }

    private Dimension getItemButtonSize() {
        return itemPanel.getComponent(0).getPreferredSize();
    }

    private Dimension getActionButtonSize() {
        return itemPanel.getComponent(1).getPreferredSize();
    }

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
            CustomItemWindow customItemWindow = new CustomItemWindow(SpecialVendingMachineView.this.model);
            customItemWindow.setVisible(true);
        }
    }

    private class CustomItemWindow extends JFrame {
        private JCheckBox pearlsCheckBox;
        private JCheckBox cheesecakeCheckBox;
        private JCheckBox nataDeCocoCheckBox;
        private JCheckBox chocolateCheckBox;
        private JCheckBox aloeVeraCheckBox;
        private JCheckBox honeyCheckBox;
        private JButton createCustomMilkTeaButton;

        private VendingMachineModel mainVendingMachineModel;

        public CustomItemWindow(VendingMachineModel mainVendingMachineModel) {
            this.mainVendingMachineModel = mainVendingMachineModel;
            setTitle("Custom Item");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setPreferredSize(new Dimension(400, 400));
            setLayout(new BorderLayout());

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

            JScrollPane scrollPane = new JScrollPane();
            add(scrollPane, BorderLayout.CENTER);

            JPanel mainPanel = new JPanel(new GridLayout(0, 1));

            List<Item> items = model.getItems();
            itemPanelsMap = new HashMap<>();
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (!item.getName().isEmpty()) {
                    ItemCustomPanel itemCustomPanel = new ItemCustomPanel(item);
                    mainPanel.add(itemCustomPanel);
                    itemPanelsMap.put(item, itemCustomPanel);
                }
            }

            scrollPane.setViewportView(mainPanel);

            createCustomMilkTeaButton = new JButton("Create Custom Milk Tea");
            createCustomMilkTeaButton.addActionListener(new CreateCustomMilkTeaListener());
            add(createCustomMilkTeaButton, BorderLayout.SOUTH);

            pack();
            setLocationRelativeTo(null);
        }

        private class CreateCustomMilkTeaListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean invalidInput = false;
                boolean insufficientStock = false;
        
                List<Item> updatedItems = new ArrayList<>();
                StringBuilder addonsMessage = new StringBuilder();
        
                int totalStock = 0;
                for (ItemCustomPanel itemPanel : itemPanelsMap.values()) {
                    int newStock = itemPanel.getNewStockQuantity();
                    Item item = itemPanel.getItem();
                    int currentStock = item.getStock();
        
                    if (newStock < 0) {
                        invalidInput = true;
                        break;
                    }
        
                    if (newStock > 0) {
                        addonsMessage.append(String.format("(%dx) %s with ", newStock, item.getName()));
                    }
        
                    totalStock += newStock;
        
                    Item mainMachineItem = findItemByName(item.getName());
                    if (mainMachineItem != null && mainMachineItem.getStock() < newStock) {
                        insufficientStock = true;
                        break;
                    }
        
                    if (mainMachineItem != null) {
                        mainMachineItem.setStock(mainMachineItem.getStock() - newStock);
                        mainVendingMachineModel.updateItem(mainMachineItem);
                        updatedItems.add(mainMachineItem);
                    }
        
                    item.setStock(currentStock - newStock);
                    updatedItems.add(item);
                }
        
                // Get selected addons
                String selectedAddons = getSelectedAddons();
        
                // Check if any item has a quantity greater than 0
                if (totalStock == 0) {
                    invalidInput = true;
                }
        
                // Modify the prompt message to include selected addons and quantities
                String promptMessage;
                if (addonsMessage.length() > 0) {
                    addonsMessage.delete(addonsMessage.length() - 6, addonsMessage.length());
                    if (!selectedAddons.isEmpty()) {
                        promptMessage = String.format("You have created a %s Milk Tea with %s", addonsMessage.toString(), getSelectedAddons());
                    } else {
                        promptMessage = String.format("You have created a %s Milk Tea", addonsMessage.toString());
                    }
                } else {
                    if (!selectedAddons.isEmpty()) {
                        promptMessage = String.format("You have created a Milk Tea with %s", selectedAddons);
                    } else {
                        promptMessage = "You have created a Milk Tea";
                    }
                }
        
                if (invalidInput) {
                    JOptionPane.showMessageDialog(null, "Invalid input: Please enter a valid stock quantity.");
                } else if (insufficientStock) {
                    JOptionPane.showMessageDialog(null, "There is not enough stock for a certain item.");
                } else {
                    double totalPrice = 200.0 + (totalStock * 50.0);
                    double currentBalance = mainVendingMachineModel.getMoneySlot().getBalance();
        
                    if (totalPrice > currentBalance) {
                        JOptionPane.showMessageDialog(null, "Not enough money to buy. Please insert more money.");
                        return;
                    }
        
                    // Show the prompt message
                    JOptionPane.showMessageDialog(null, promptMessage);
                    dispose();
        
                    // Update balance in main vending machine model
                    mainVendingMachineModel.getMoneySlot().setBalance(currentBalance - totalPrice);
        
                    // Show additional prompts
                    showMixingMilkTeasPrompt();
                    showAddingAddonsPrompt(totalStock > 0);
        
                    // Final prompt
                    JOptionPane.showMessageDialog(null, "Custom Milk Tea Dispensed");
        
                    for (Item item : updatedItems) {
                        mainVendingMachineModel.updateItem(item);
                    }
                }
            }
        
            private String getSelectedAddons() {
                StringBuilder selectedAddons = new StringBuilder();
                if (pearlsCheckBox.isSelected()) {
                    selectedAddons.append("Pearls, ");
                }
                if (cheesecakeCheckBox.isSelected()) {
                    selectedAddons.append("Cheesecake, ");
                }
                if (nataDeCocoCheckBox.isSelected()) {
                    selectedAddons.append("Nata de Coco, ");
                }
                if (chocolateCheckBox.isSelected()) {
                    selectedAddons.append("Chocolate, ");
                }
                if (aloeVeraCheckBox.isSelected()) {
                    selectedAddons.append("Aloe Vera, ");
                }
                if (honeyCheckBox.isSelected()) {
                    selectedAddons.append("Honey, ");
                }
        
                // Remove the trailing comma and space
                if (selectedAddons.length() > 2) {
                    selectedAddons.delete(selectedAddons.length() - 2, selectedAddons.length());
                }
        
                return selectedAddons.toString();
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

        private void showMixingMilkTeasPrompt() {
            JOptionPane.showMessageDialog(null, "Mixing Milk Teas");
        }

        private void showAddingAddonsPrompt(boolean hasAddons) {
            if (hasAddons) {
                JOptionPane.showMessageDialog(null, "Adding Addons");
            }
        }

        private String getSelectedAddons() {
            StringBuilder selectedAddons = new StringBuilder();
            if (pearlsCheckBox.isSelected()) {
                selectedAddons.append("Pearls, ");
            }
            if (cheesecakeCheckBox.isSelected()) {
                selectedAddons.append("Cheesecake, ");
            }
            // Add other checkbox items similarly

            // Remove the trailing comma and space
            if (selectedAddons.length() > 2) {
                selectedAddons.delete(selectedAddons.length() - 2, selectedAddons.length());
            }

            return selectedAddons.toString();
        }
    }

    private class ItemCustomPanel extends JPanel {
        private Item item;
        private JTextField stockField;

        public ItemCustomPanel(Item item) {
            this.item = item;
            setupPanel();
        }

        private void setupPanel() {
            setLayout(new BorderLayout());

            JLabel itemLabel = new JLabel(item.getName());
            add(itemLabel, BorderLayout.CENTER);

            stockField = new JTextField();
            stockField.setPreferredSize(new Dimension(60, 30));
            add(stockField, BorderLayout.EAST);
        }

        public int getNewStockQuantity() {
            try {
                return Integer.parseInt(stockField.getText());
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        public Item getItem() {
            return item;
        }
    }
}
