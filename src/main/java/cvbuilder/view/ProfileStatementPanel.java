package cvbuilder.view;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileStatementPanel {

    public static JTextArea textArea = new JTextArea();
    private final JCheckBox includeCheckBox;
    private final JList<String> statementsList;
    private final DefaultListModel<String> listModel;
    public static Vector<Boolean> includeList = new Vector<>(); // To keep track of the included state
    private final JButton saveButton;
    private final JPanel panel = new JPanel(new BorderLayout());
    private final Path statementJSON = Paths.get("profile_statements.json");

    // Constructor
    public ProfileStatementPanel() {
        textArea = new JTextArea(10, 30);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        includeCheckBox = new JCheckBox("Include");
        listModel = new DefaultListModel<>();
        includeList = new Vector<>();
        statementsList = new JList<>(listModel);

        statementsList.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // This will trigger the list to revalidate and repaint on resize
                statementsList.revalidate();
                statementsList.repaint();
            }
        });
        statementsList.setCellRenderer(new CombinedListCellRenderer());
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        loadStatements(); // Load existing statements from the JSON file
    }

    // Getter method for the panel
    public String getProfileStatement() {
        StringBuilder profileStatement = new StringBuilder("<div>");

        if (Files.exists(statementJSON)) {
            try {
                String content = Files.readString(statementJSON);
                JSONArray statementsArray = new JSONArray(content);
                for (int i = 0; i < statementsArray.length(); i++) {
                    JSONObject statementObject = statementsArray.getJSONObject(i);
                    if (statementObject.getBoolean("include")) {
                        String[] paragraphs = statementObject.getString("statement").split("\\r?\\n");
                        for (String paragraph : paragraphs) {
                            profileStatement.append("<p>").append(paragraph.trim()).append("</p>\n");
                        }
                    }
                }
            } catch (IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to read the statement JSON.", e);
            }
        }
        profileStatement.append("</div>");
        return profileStatement.toString().trim();
    }


    // Save the statement into JSON file
    private void saveStatements() {
        JSONArray statementsArray = new JSONArray();
        for (int i = 0; i < listModel.getSize(); i++) {
            JSONObject statementObject = new JSONObject();
            statementObject.put("statement", listModel.get(i));
            statementObject.put("include", includeList.get(i));
            statementsArray.put(statementObject);
        }
        // Save this JSONArray to a file
        try (PrintWriter out = new PrintWriter(String.valueOf(statementJSON))) {
            out.println(statementsArray.toString(4)); // The number 4 is the indent factor for prettifying
        } catch (FileNotFoundException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to save the statement JSON.", e);
        }
    }

    static class CombinedListCellRenderer extends DefaultListCellRenderer {
        private static final String ELLIPSIS = "...";

        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // Applying the check mark or space logic
            if (index >= 0 && index < includeList.size() && includeList.get(index)) {
                label.setText("  ✓   | " + label.getText());
            } else {
                label.setText("        | " + label.getText());
            }

            // Applying the ellipsis logic
            FontMetrics fm = label.getFontMetrics(label.getFont());
            int listWidth = list.getWidth() - list.getInsets().left - list.getInsets().right;
            String text = label.getText();

            if (fm.stringWidth(text) >= listWidth) {
                int textWidth = fm.stringWidth(ELLIPSIS);
                int nChars = 0;
                for (int i = 0; i < text.length(); i++) {
                    textWidth += fm.charWidth(text.charAt(i));
                    if (textWidth > listWidth) break;
                    nChars++;
                }
                label.setText(text.substring(0, nChars) + ELLIPSIS);
            }

            return label;
        }
    }

    // Create a panel with a list of statements and a text area for entering new statements
    public JPanel createProfileStatementPanel() {

        JScrollPane scrollPane = new JScrollPane(textArea);
        JScrollPane listScrollPane = new JScrollPane(statementsList);

        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this::deleteAction);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(this::addAction);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(this::editAction);
        saveButton.addActionListener(this::saveAction);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        panel.add(listScrollPane, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(includeCheckBox, BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Add a new statement to the list and data model
    private void addAction(ActionEvent e) {
        String profileText = textArea.getText();
        if (!profileText.trim().isEmpty()) {
            listModel.addElement(profileText);
            includeList.add(includeCheckBox.isSelected());
            saveStatements(); // Save the new statement
            textArea.setText(""); // Clear the text area
            includeCheckBox.setSelected(false); // Reset the checkbox
        }
    }

    // Edit an existing statement in the list and data model
    private void editAction(ActionEvent e) {
        int selectedIndex = statementsList.getSelectedIndex();
        if (selectedIndex != -1) {
            textArea.setText(listModel.get(selectedIndex));
            includeCheckBox.setSelected(includeList.get(selectedIndex));
            saveButton.setEnabled(true); // Enable the save button when editing
        }
    }

    // Save the edited statement to the list and data model
    private void saveAction(ActionEvent e) {
        int selectedIndex = statementsList.getSelectedIndex();
        if (selectedIndex != -1 && !textArea.getText().trim().isEmpty()) {
            listModel.set(selectedIndex, textArea.getText().trim());
            includeList.set(selectedIndex, includeCheckBox.isSelected());
            saveStatements(); // Save the changes to the CSV
            saveButton.setEnabled(false); // Disable the save button after saving
            textArea.setText("");
            includeCheckBox.setSelected(false);
        }
        // Refresh the List of statements
        listModel.clear();
        includeList.clear();
        loadStatements();
    }

    // Delete an existing statement from the list and data model
    private void deleteAction(ActionEvent e) {
        int selectedIndex = statementsList.getSelectedIndex();
        if (selectedIndex != -1) {
            int confirm = JOptionPane.showConfirmDialog(
                    null, "Are you sure you want to delete this statement?", "Delete Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                // Remove from JList and data model
                listModel.remove(selectedIndex);
                includeList.remove(selectedIndex);

                // Save the modified list back to the CSV file
                saveStatements();
            }
        }
    }

    // Load existing statements from the JSON file
    private void loadStatements() {
        File jsonFile = statementJSON.toFile();

        if (jsonFile.exists() && jsonFile.length() > 0) {
            try {
                String content = Files.readString(statementJSON);
                JSONArray jsonArray = new JSONArray(content);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String statement = obj.getString("statement");
                    boolean include = obj.getBoolean("include");
                    listModel.addElement(statement); // Add statement to the list
                    includeList.add(include); // Track inclusion status if needed
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading the profile statements file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Handle case where the file does not exist or is empty
            JOptionPane.showMessageDialog(null, "No saved profile statements available.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Load a file into the profile statement file
    public void loadFileToProfileStatement() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Profile Statement");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt", "text"));

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                String fileContent = new String(Files.readAllBytes(selectedFile.toPath()));
                appendStatementToJson(fileContent);

                // Refresh Statement List
                loadStatements();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error processing the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // New append method
    private void appendStatementToJson(String statement) throws IOException {
        JSONArray statementsArray = loadExistingStatements();
        JSONObject newStatement = new JSONObject();
        newStatement.put("include", true);
        // Ensure newline characters are retained
        newStatement.put("statement", statement.replaceAll("\r\t\n", "\n").replaceAll("\n", "\n"));
        statementsArray.put(newStatement);
        writeJsonArrayToFile(statementsArray);
    }


    // Load existing statements from the JSON file
    private JSONArray loadExistingStatements() throws IOException {
        // Read the existing content of the JSON file into a JSONArray
        if (Files.exists(statementJSON) && Files.size(statementJSON) > 0) {
            String jsonContent = new String(Files.readAllBytes(statementJSON));
            return new JSONArray(jsonContent);
        } else {
            return new JSONArray();
        }
    }

    // Write the JSONArray to the JSON file
    private void writeJsonArrayToFile(JSONArray statementsArray) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(statementJSON.toFile()))) {
            bw.write(statementsArray.toString(4)); // Indented with 4 spaces for readability
        }
    }
}
