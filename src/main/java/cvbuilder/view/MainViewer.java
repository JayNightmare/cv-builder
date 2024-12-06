package cvbuilder.view;

import cvbuilder.controller.CoreTab;
import cvbuilder.controller.UserTab;

import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import java.awt.*;


/* Updated Version */
public class MainViewer extends Component {
    UserTab userTab = new UserTab();
    CoreTab coreTab = new CoreTab();
    ProfileStatementPanel psp = new ProfileStatementPanel();

    public void UI() {
        addComponentsToFrame();
    }

    private void addComponentsToFrame() {
        userTab.frame.setTitle("CV Builder");
        userTab.frame.setLayout(new BorderLayout());
        userTab.frame.setSize(500, 500);
        userTab.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userTab.frame.setVisible(true);

        JPanel subUserPanel = new JPanel(new BorderLayout());
        JPanel subCorePanel = new JPanel(new BorderLayout());

        // Menu - File
        JMenu menuFile = new JMenu("File");

        JMenuBar menuBar = new JMenuBar();

        JMenuItem saveOutputFileItem = new JMenuItem("Export CV (File)");
        JMenuItem saveConsoleItem = new JMenuItem("Export CV (Console)");
        JMenuItem loadButton = new JMenuItem("Load Statement");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Menu - Help
        JMenu menuHelp = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        JMenuItem contactItem = new JMenuItem("Contact");
        JMenuItem faqItem = new JMenuItem("FAQ");


        // Action Listeners
        saveOutputFileItem.addActionListener(e -> userTab.saveFileItem()); // Exports CV so user can use it else where
        saveConsoleItem.addActionListener(e -> userTab.saveConsoleItem()); // Exports CV to console
        loadButton.addActionListener(e -> psp.loadFileToProfileStatement()); // Loads File to Profile Statement
        exitItem.addActionListener(e -> System.exit(0)); // Exits Application
        aboutItem.addActionListener(e -> aboutItem()); // About Application
        contactItem.addActionListener(e -> contactItem()); // Contact Info For Application
        faqItem.addActionListener(e -> faqItem()); // FAQ About Application

        // Add Items to Menu File Option
        menuFile.addSeparator();

        menuFile.add(saveOutputFileItem);
        menuFile.add(saveConsoleItem);

        menuFile.addSeparator();

        menuFile.add(loadButton);

        menuFile.addSeparator();

        menuFile.add(exitItem);

        // Add Items to Menu Help Options
        menuHelp.addSeparator();

        menuHelp.add(aboutItem);
        menuHelp.add(contactItem);
        menuHelp.add(faqItem);

        // Add Menu to Frame
        menuBar.add(menuFile);
        menuBar.add(menuHelp);
        userTab.frame.setJMenuBar(menuBar);
        // Menu Complete! //

        JPanel mainPanel = new JPanel(new BorderLayout());
        userTab.frame.add(mainPanel);

        JTabbedPane tabTop = new JTabbedPane();
        tabTop.add("User", subUserPanel);
        tabTop.add("Core", subCorePanel);
        mainPanel.add(tabTop);

        TitledBorder coreTitle = BorderFactory.createTitledBorder("Core Competencies");
        subCorePanel.setBorder(coreTitle);

        coreTab.initializeCoreTab(subCorePanel);
        userTab.initializeUserTab(subUserPanel);
    }

    private void aboutItem() {
        String aboutText = "<html><body>" +
                "<h2><strong>CV Builder Version 0.7.1</strong></h2>" +
                "<p>Developed by: <strong>Jay</strong> (K2119852)</p>" +
                "<p>This application allows users to create and save their CVs in HTML format.</p>" +
                "<p>The application was created using Java Swing</p>" +
                "<br>" +
                "<p>For more information, visit: <a href='https://www.nexusgit.info'>nexusgit.info</a></p>" +
                "</body></html>";

        JOptionPane.showMessageDialog(null, aboutText, "About CV Builder", JOptionPane.INFORMATION_MESSAGE);
    }


    private void contactItem() {
        String contactText = "<html><body>" +
                "<h2><strong>Contact Info</strong></h2>" +
                "<p>Website: <a href='https://www.nexusgit.info'>nexusgit.info</a></p>" +
                "<p>Email: K2119852@kingston.ac.uk</p>" +
                "</body></html>";

        JOptionPane.showMessageDialog(null, contactText,"Contact Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void faqItem() {
        String faqText = "<html><body>" +

                "<h2><strong>FAQ Answers</strong></h2>" +

                "<p>Q: <strong>How do I add a new user?</strong></p>" +
                "<p>A: By clicking 'Add User' allows you to add a new info to the list.</p>" +
                "<p>Note: This will not add the user to the list until 'Save Changes' is pressed!</p>" +

                "<br>" +

                "<p>Q: <strong>How do I save changes?</strong></p>" +
                "<p>A: By clicking 'Save Changes' will save the changes to the list.</p>" +

                "<br>" +

                "<p>Q: <strong>How do I edit a user?</strong></p>" +
                "<p>A: By double clicking the info you want to edit will allow you to edit the user info.</p>" +

                "<br>" +

                "<p>Q: <strong>How do I delete a user?</strong></p>" +
                "<p>A: By clicking the 'Delete' button next to the user will delete the user.</p>" +
                "<p>Note: This will not delete the user from the list until 'Save Changes' is pressed!</p>" +

                "<br>" +

                "<p>Q: <strong>How do I add a new skill?</strong></p>" +
                "<p>A: By clicking 'Add' allows you to add a new skill to the list.</p>" +

                "<br>" +

                "<p>Q: <strong>How do I edit the skills??</strong></p>" +
                "<p>A: Click on the skill you want to edit than by clicking 'Edit' will allow you to edit the skill.</p>" +

                "<br>" +

                "<p>Q: <strong>How do I delete a skill?</strong></p>" +
                "<p>A: Click on the skill you want to delete than by clicking 'Delete' will allow you to delete the skill.</p>" +

                "<br>" +

                "<p>Q: <strong>How do I add a new statement?</strong></p>" +
                "<p>A: Click on the 'Add' button to add a new statement.</p>" +

                "<br>" +

                "<p>Q: <strong>How do I include a statement?</strong></p>" +
                "<p>A: Click on the 'Include' button to include the statement in the save file.</p>" +

                "<br>" +

                "<p>Q: <strong>How do I edit a statement?</strong></p>" +
                "<p>A: Click on the statement you want to edit than by clicking 'Edit' will allow you to edit the statement.</p>" +

                "<br>" +

                "<p>Q: <strong>How do I delete a statement?</strong></p>" +
                "<p>A: Click on the statement you want to delete than by clicking 'Delete' will allow you to delete the statement.</p>" +

                "<br>" +

                "<p>Q: <strong>How do I save the CV?</strong></p>" +
                "<p>A: Going to File and clicking 'Save' will save the CV to a file.</p>" +

                "<br>" +

                "</body></html>";

        JOptionPane.showMessageDialog(null,faqText, "Frequently Asked Questions", JOptionPane.INFORMATION_MESSAGE);
    }
}