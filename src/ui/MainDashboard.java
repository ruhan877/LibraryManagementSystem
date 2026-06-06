package ui;

import service.LibraryService;
import javax.swing.*;
import java.awt.*;


public class MainDashboard extends JFrame {

    private final LibraryService libraryService;


    public MainDashboard(LibraryService libraryService) {
        this.libraryService = libraryService;

        // Step 1: Configure basic frame properties
        setTitle("Library Management System");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centres the window automatically on your monitor screen

        // Step 2: Use BorderLayout to neatly expand component layouts
        setLayout(new BorderLayout());

        // Step 3: Create a clean Header Banner Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Step 4: Create a JTabbedPane container for step-by-step navigation tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Step 5: Instantiating specific functional subpanels (Pass down the service context)
        IssueBookPanel issuePanel = new IssueBookPanel(this.libraryService);
        ReturnBookPanel returnPanel = new ReturnBookPanel(this.libraryService);

        // Step 6: Map panels as distinct navigation tab indexes
        tabbedPane.addTab("📖 Issue Assets", issuePanel);
        tabbedPane.addTab("🔄 Process Returns", returnPanel);

        // Step 7: Push the main tab group to the center workspace area
        add(tabbedPane, BorderLayout.CENTER);
    }


    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(41, 128, 185)); // Deep modern primary blue tone
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        JLabel titleLabel = new JLabel("LMS Enterprise Ledger Engine");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        panel.add(titleLabel);
        return panel;
    }
}
