package view;

import model.Item;
import model.VendingMachineModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SpecialVendingMachineView extends VendingMachineView {

    private JButton customItemButton; // The "Custom" item button

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
            // Create a new custom item window
            CustomItemWindow customItemWindow = new CustomItemWindow();
            customItemWindow.setVisible(true);
        }
    }

    // Custom window class for the "Custom" button
    private class CustomItemWindow extends JFrame {
        public CustomItemWindow() {
            setTitle("Custom Item");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setPreferredSize(new Dimension(400, 300)); // Set the preferred size to half of the regular vending machine window
            setLayout(new BorderLayout());
    
            // Create a scroll pane to hold the custom item panels
            JScrollPane scrollPane = new JScrollPane();
            add(scrollPane, BorderLayout.CENTER);
    
            JPanel mainPanel = new JPanel(new GridLayout(0, 1));
    
            List<Item> items = model.getItems();
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (!item.getName().isEmpty()) { // Check if the item name is not empty
                    ItemCustomPanel itemCustomPanel = new ItemCustomPanel(item);
                    mainPanel.add(itemCustomPanel);
                }
            }
    
            scrollPane.setViewportView(mainPanel); // Add the main panel to the scroll pane
    
            // Create the "Create Custom Milk Tea" button
            JButton createCustomMilkTeaButton = new JButton("Create Custom Milk Tea");
            createCustomMilkTeaButton.addActionListener(new CreateCustomMilkTeaListener());
            mainPanel.add(createCustomMilkTeaButton); // Add the button to the main panel
    
            pack(); // Adjust the window size based on the contents
            setLocationRelativeTo(null);
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
    
                JLabel itemLabel = new JLabel(getItemLabelText());
                add(itemLabel, BorderLayout.CENTER);
    
                stockField = new JTextField();
                stockField.setPreferredSize(new Dimension(60, 30));
                add(stockField, BorderLayout.EAST);
            }
    
            private String getItemLabelText() {
                return "<html><br><b>Name:</b> " + item.getName() +
                        "<br><b>Stock:</b> " + item.getStock() + "/" + item.getMaxStock() +
                        "<br><b>Calories:</b> " + item.getCalories() +
                        "<br><b>Price:</b> P" + String.format("%.2f", item.getPrice()) + "<br></html>";
            }
        }

        private class CreateCustomMilkTeaListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic to handle the "Create Custom Milk Tea" button
                // For example, you can display a new window or dialog for customizing milk tea options.
                // Replace the following showMessageDialog with your custom logic.
                JOptionPane.showMessageDialog(CustomItemWindow.this, "Create Custom Milk Tea Button Pressed!");
            }
        }
    }    
}
