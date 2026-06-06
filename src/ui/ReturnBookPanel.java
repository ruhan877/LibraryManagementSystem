package ui;

import service.LibraryService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ReturnBookPanel extends JPanel {

    private final LibraryService libraryService;

    // UI Form Components
    private JTextField txtBookId;
    private JButton btnSubmit;

    public ReturnBookPanel(LibraryService libraryService) {
        this.libraryService = libraryService;

        // Step 1: Initialize GridBagLayout configurations
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Step 2: Build and align form label
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Return Book ID:"), gbc);

        // Step 3: Build and align text input field
        gbc.gridx = 1; gbc.gridy = 0;
        txtBookId = new JTextField(15);
        add(txtBookId, gbc);

        // Step 4: Build and align return confirmation button
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2; // Span across two grid cells
        btnSubmit = new JButton("Confirm Return Receipt");
        btnSubmit.setBackground(new Color(230, 126, 34)); // Modern distinct orange alert tone
        btnSubmit.setForeground(Color.WHITE);
        add(btnSubmit, gbc);

        // Step 5: Attach click handler event listeners
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processReturnAction();
            }
        });
    }


    private void processReturnAction() {
        // Step A: Ensure field isn't blank
        if (txtBookId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter a Book ID asset code before checking in.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Step B: Sanitize and parse user input string
            int bookId = Integer.parseInt(txtBookId.getText().trim());

            // Step C: Trigger transactional service logic and retrieve fine balance output data
            double calculatedFine = libraryService.returnBook(bookId);

            // Step D: Evaluate fine conditions and display customized notifications to operators
            if (calculatedFine > 0) {
                String message = String.format("🔄 Return Completed!\n⚠️ This book was overdue. Calculated Fine: ₹%.2f (or $%.2f)", calculatedFine, calculatedFine);
                JOptionPane.showMessageDialog(this, message, "Overdue Late Return Balance Alert", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "🎉 Success! Book returned cleanly within valid period. Fine: Zero balance.", "Return Log Cleared", JOptionPane.INFORMATION_MESSAGE);
            }

            // Step E: Wipe out data forms clean for next transaction entry loops
            txtBookId.setText("");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "❌ Formatting Error: Target asset IDs must be purely numeric characters.", "Input Rejection", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Safely intercepts custom transactional backend failures, displaying it dynamically inside dialogue boxes
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Return Operation Terminated", JOptionPane.ERROR_MESSAGE);
        }
    }
}
