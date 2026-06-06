// No package declaration here because your file sits directly in the root 'src' folder

import service.LibraryService;
import ui.MainDashboard;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class main {

    public static void main(String[] args) {

        configureSystemLookAndFeel();


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                    LibraryService libraryService = new LibraryService();


                    MainDashboard dashboard = new MainDashboard(libraryService);


                    dashboard.setVisible(true);

                } catch (Exception e) {
                    System.err.println("Fatal Error: Could not start the graphic user interface.");
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Helper method to seamlessly blend Java Swing look and feel with your native operating system.
     */
    private static void configureSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Notice: Using standard cross-platform UI skin.");
        }
    }
}
