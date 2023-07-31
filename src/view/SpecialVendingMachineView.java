package view;
import javax.swing.*;

import model.VendingMachineModel;

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
        customItemButton.setPreferredSize(new Dimension(200, 200));
        customItemButton.addActionListener(new CustomItemActionListener());
        itemPanel.add(customItemButton);
    }

    private class CustomItemActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: Implement the logic for handling the "Custom" item button
            // For example, you can prompt the user to enter details for the custom item.

        }
    }
}
