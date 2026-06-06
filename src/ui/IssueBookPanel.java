package ui;

import service.LibraryService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IssueBookPanel extends JPanel {

    private final LibraryService libraryService;

    // UI Form Components
    private JTextField txtBookId;
    private JTextField txtMemberId;
    private JButton btnSubmit;

    public IssueBookPanel(LibraryService libraryService) {
        this.libraryService = libraryService;

        // Step 1: Set up a clean grid layout with padding space
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 10px margins around everything
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Step 2: Create and position the Book ID inputs
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Book ID Asset Number:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        txtBookId = new JTextField(15);
        add(txtBookId, gbc);

        // Step 3: Create and position the Member ID inputs
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Library Member ID:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        txtMemberId = new JTextField(15);
        add(txtMemberId, gbc);

        // Step 4: Create and position the Submission Button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; // Stretch button across both columns
        btnSubmit = new JButton("Confirm Issue Transaction");
        btnSubmit.setBackground(new Color(46, 204, 113)); // Clean modern green tone
        btnSubmit.setForeground(Color.WHITE);
        add(btnSubmit, gbc);

        // Step 5: Attach an Action Listener to handle button clicks
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processIssueAction();
            }
        });
    }


    private void processIssueAction() {
        // Step A: Basic input verification
        if (txtBookId.getText().trim().isEmpty() || txtMemberId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please fill in both ID fields before submitting.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Step B: Convert user text inputs into structural numbers
            int bookId = Integer.parseInt(txtBookId.getText().trim());
            int memberId = Integer.parseInt(txtMemberId.getText().trim());

            // Step C: Trigger transactional operation in our backend service layer
            libraryService.issueBook(bookId, memberId);

            // Step D: Inform user of success and clear the input form
            JOptionPane.showMessageDialog(this, "🎉 Success! The transaction completed and the book has been issued.", "System Ledger Notice", JOptionPane.INFORMATION_MESSAGE);
            txtBookId.setText("");
            txtMemberId.setText("");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "❌ Formatting Error: IDs must consist of numeric values only.", "Input Rejection", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Displays the exact custom exception message pushed upwards from our LibraryService.java file
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Transaction Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
