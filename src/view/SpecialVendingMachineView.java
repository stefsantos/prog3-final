package view;

import model.VendingMachineModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            // TODO: Implement the logic for handling the "Custom" item button
            // For example, you can prompt the user to enter details for the custom item.

        }
    }
}
