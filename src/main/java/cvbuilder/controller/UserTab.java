package cvbuilder.controller;

import cvbuilder.model.User;
import cvbuilder.model.UserGroup;
import cvbuilder.view.ButtonColumn;
import cvbuilder.view.ProfileStatementPanel;
import cvbuilder.view.SkillsPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class UserTab {
    public JFrame frame;
    public DefaultTableModel tableModel;
    private JTable userTable;

    ProfileStatementPanel  profileStatementPanel = new ProfileStatementPanel();
    SkillsPanel skillsPanel = new SkillsPanel();

    public UserTab() {
        frame = new JFrame();
    }

    public void initializeUserTab(JPanel userPanel) {
        userPanel.setLayout(new BorderLayout());
        userPanel.setBorder(BorderFactory.createTitledBorder("User Info"));

        String[] columnNames = {"Select", "Full Name", "Title", "Email", "Delete"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }
        };

        userTable = new JTable(tableModel);
        setupTableButtons();
        userPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        addControlButtons(userPanel);

        loadUsers();
    }

    private void addControlButtons(JPanel userPanel) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> saveChanges());

        JButton addButton = new JButton("Add User");
        addButton.addActionListener(e -> addUser());

        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);

        userPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addUser() {
        JTextField FirstnameField = new JTextField();
        JTextField LastNameField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField emailField = new JTextField();

        JPanel inputPanel = new JPanel(new GridLayout(0, 1));
        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(FirstnameField);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(LastNameField);

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);

        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = FirstnameField.getText() + " " + LastNameField.getText();
            String title = titleField.getText();
            String email = emailField.getText();
            tableModel.addRow(new Object[]{false, name, title, email, "Delete"});
        }
    }

    public final List<User> captureTableData() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            String name = (String) tableModel.getValueAt(i, 1);
            String title = (String) tableModel.getValueAt(i, 2);
            String email = (String) tableModel.getValueAt(i, 3);
            users.add(new User(name, title, email, isSelected));
        }

        return users;
    }

    private void saveChanges() {
        List<User> usersToSave = captureTableData();
        try {
            UserGroup.writeNameCSVFile("userinfo.csv", usersToSave); // Pass the list to the method
            JOptionPane.showMessageDialog(frame, "Changes saved successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to save changes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupTableButtons() {
        Action deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = Integer.parseInt(e.getActionCommand());
                tableModel.removeRow(row);
            }
        };

        // Set up "Delete" button column
        ButtonColumn deleteButtonColumn = new ButtonColumn(userTable, deleteAction, 4);
        userTable.getColumnModel().getColumn(4).setCellRenderer(deleteButtonColumn);
        userTable.getColumnModel().getColumn(4).setCellEditor(deleteButtonColumn);

        // Set up checkbox column behavior
        userTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 0) {
                int row = e.getFirstRow();
                Boolean checked = (Boolean) tableModel.getValueAt(row, 0);
                if (checked != null && checked) {
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        if (i != row) {
                            tableModel.setValueAt(false, i, 0);
                        }
                    }
                }
            }
        });
    }

    private void loadUsers() {
        List<User> users = UserGroup.getUsersNames();
        for (User user : users) {
            tableModel.addRow(new Object[]{user.isSelected(), user.getName(), user.getTitle(), user.getEmail(), "Delete"});
        }
    }

    public void saveFileItem() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CV as HTML");
        fileChooser.setFileFilter(new FileNameExtensionFilter("HTML Documents", "html"));
        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Ensure the file saves as .html
            if (!fileToSave.getName().toLowerCase().endsWith(".html")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".html");
            }

            String profileStatement = profileStatementPanel.getProfileStatement(); // Implement this method to read from CSV
            List<String> skillsList = skillsPanel.getSkillsList();

            // Call the method to save the HTML content
            saveCVAsHTML(fileToSave, captureTableData(), profileStatement, skillsList);
        }
    }

    // remove <br> from profileStatement
    public String removeBr(String profileStatement) {
        return profileStatement.replace("<br>", "\n").replace("<div>", "\n").replace("</div>", "").replace("<p>", "").replace("</p>", "");
    }

    public void saveConsoleItem() {
        String profileStatement = profileStatementPanel.getProfileStatement(); // Implement this method to read from CSV
        List<String> skillsList = skillsPanel.getSkillsList();

        saveCVAsConsole( captureTableData(), profileStatement, skillsList );
        System.out.println("CV saved successfully!");
        JOptionPane.showMessageDialog(null, "CV saved to console successfully!");
    }

    public void saveCVAsConsole(List<User> users, String profileStatement, List<String> skillsList) {
        // Select Selected User From List
        for (User user : users) {
            if (user.isSelected()) {
                System.out.println("Name: " + user.getName());
                System.out.println("Title: " + user.getTitle());
                System.out.println("Email: " + user.getEmail());
            }
        }

        System.out.println("Profile Statement: " + removeBr(profileStatement));
        System.out.println("Skills: " + skillsList);
    }

    public void saveCVAsHTML(File file, List<User> users, String profileStatement, List<String> skillsList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html>\n");
            writer.write("<head>\n");
            writer.write("<title>CV</title>\n");
            writer.write("<style>\n");
            writer.write("body { font-family: \"Gill Sans\", sans-serif; background: #919191; }\n");
            writer.write("div h1:nth-child(1) { display: flex; justify-content: center; }\n");
            writer.write("h1 { color: #000; font-weight: 1000; }\n");
            writer.write(".overview > hr { width: 100px; }\n");
            writer.write("p { margin: 0; }\n");
            writer.write(".profile-statement, .skills-list { width: 50%; border: 1px solid gray; border-radius: 5px; padding-left: 10px; background: #b6b6b8; }\n");
            writer.write(".profile-statement p { margin-block-end: 1em; padding-right: 10px; }\n");
            writer.write(".skills-list { list-style-type: none; padding-left: 10px; height: fit-content; }\n");
            writer.write(".skills-list li { margin-bottom: 5px; }\n");
            writer.write(".right-side { display: flex; gap: 10px; }\n");
            writer.write("#name { display: grid; grid-template-columns: 4em auto; border: 1px solid gray; border-radius: 5px; background: #b6b6b8; padding: 10px; gap: 10px; }\n");
            writer.write("#name p:nth-child(1), #name p:nth-child(3) { font-weight: 700; }\n");
            writer.write("</style>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");

            // Write user information and other details into the HTML
            for (User user : users) {
                if (user.isSelected()) {
                    writer.write("<div>\n");
                    writer.write("<h1>CV</h1>\n");
                    writer.write("</div>\n");

                    writer.write("<hr>\n");

                    writer.write("<div class='overview'>\n");

                    // User information
                    writer.write("<div id='name'>\n");
                    writer.write("<p>Name: </p>\n");
                    writer.write("<p>" + user.getTitle()+ " " + user.getName() + "</p>\n");
                    writer.write("<p>Email: </p>\n");
                    writer.write("<p>" + user.getEmail() + "</p>\n");
                    writer.write("</div>\n");

                    writer.write("<hr>\n");

                    // Profile Statement
                    writer.write("<div class='right-side'>\n");
                    writer.write("<div class=\"profile-statement\">\n");
                    writer.write("<h2>Profile Statement</h2>\n");
                    writer.write("<p>" + profileStatement + "</p>\n");
                    writer.write("</div>\n");

                    // Skills
                    writer.write("<div class=\"skills-list\">\n");
                    writer.write("<h2>Skills</h2>\n");
                    writer.write("<ul>\n");
                    for (String skill : skillsList) { writer.write("<li>" + skill + "</li>\n"); }
                    writer.write("</ul>\n");
                    writer.write("</div>\n");
                    writer.write("</div>\n");

                    writer.write("<hr>\n");

                    writer.write("</div>\n");
                }
            }

            // Close HTML tags
            writer.write("</body>\n");
            writer.write("</html>\n");

            // Notify the user that the CV was saved successfully
            JOptionPane.showMessageDialog(frame, "CV saved successfully as HTML.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving CV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
