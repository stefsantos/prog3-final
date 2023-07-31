package view;
import javax.swing.*;

import controller.VendingMachineController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    private VendingMachineController controller;

    public MainMenu(VendingMachineController controller) {
        this.controller = controller;
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        JButton regularVendingButton = new JButton("Regular Vending Machine");
        regularVendingButton.addActionListener(new RegularVendingActionListener());
        add(regularVendingButton);

        JButton specialVendingButton = new JButton("Special Vending Machine");
        specialVendingButton.addActionListener(new SpecialVendingActionListener());
        add(specialVendingButton);

        pack();
        setLocationRelativeTo(null);
    }

    private class RegularVendingActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int numRows = promptNumRows();
            if (numRows > 7) {
                controller.createRegularVendingMachine(numRows);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Number of rows must be greater than 7.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class SpecialVendingActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int numRows = promptNumRows();
            if (numRows > 7) {
                controller.createSpecialVendingMachine(numRows);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Number of rows must be greater than 7.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public int promptNumRows() {
        String input = JOptionPane.showInputDialog(null, "Enter the number of rows (greater than 7):", "Number of Rows", JOptionPane.QUESTION_MESSAGE);
        if (input != null) {
            try {
                int numRows = Integer.parseInt(input);
                return numRows;
            } catch (NumberFormatException e) {
                return promptNumRows(); // Retry if input is not a valid integer
            }
        } else {
            System.exit(0); // Close the program if the user cancels the prompt
        }
        return 0;
    }
}