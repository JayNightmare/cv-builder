package cvbuilder.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SkillsPanel {

    private static final String SKILLS_FILE = "user_skills.csv";
    private static final List<String> skills;
    private static final DefaultListModel<String> listModel;

    // Load the skills from the file
    static {
        skills = new ArrayList<>();
        listModel = new DefaultListModel<>();
        loadSkills();
    }

    // Create the Skills Panel
    public static JPanel createSkillsPanel() {
        JPanel skillsPanel = new JPanel(new BorderLayout());
        TitledBorder coreTitle = BorderFactory.createTitledBorder("Skills");
        skillsPanel.setBorder(coreTitle);

        // Create the JList and add it to a JScrollPane
        JList<String> skillsList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(skillsList);
        skillsPanel.add(listScrollPane, BorderLayout.CENTER);

        // Create Button and add Action Listener
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        addButton.addActionListener((ActionEvent e) -> {
            String newSkill = JOptionPane.showInputDialog(skillsPanel, "Enter a new skill:");
            if (newSkill != null && !newSkill.trim().isEmpty() && !skills.contains(newSkill.trim())) {
                listModel.addElement("  ✓  | " + newSkill.trim());
                skills.add(newSkill.trim());
                saveSkills();
            }
        });

        JButton editButton = getButton(skillsList, skillsPanel);
        JButton deleteButton = getDeleteButton(skillsList);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        skillsPanel.add(buttonPanel, BorderLayout.SOUTH);

        return skillsPanel;
    }

    // Load skills from the file
    private static void loadSkills() {
        File file = new File(SKILLS_FILE);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String skill;
                while ((skill = br.readLine()) != null) {
                    skills.add(skill);
                    listModel.addElement("  ✓  | " + skill);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error loading skills: " + e.getMessage());
            }
        }
    }

    // Save skills to the file
    private static void saveSkills() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SKILLS_FILE))) {
            for (String skill : skills) {
                bw.write(skill);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving skills: " + e.getMessage());
        }
    }


    // Get the Edit Button
    private static JButton getButton(JList<String> skillsList, JPanel skillsPanel) {
        JButton editButton = new JButton("Edit");
        editButton.addActionListener((ActionEvent e) -> {
            int selectedIndex = skillsList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedSkill = skillsList.getSelectedValue();
                //selectedSkill = selectedSkill.replace("  ✓  | ", "").trim();

                String newSkill = JOptionPane.showInputDialog(skillsPanel, "Edit skill:", selectedSkill);
                if (newSkill != null && !newSkill.trim().isEmpty() && !newSkill.equals(selectedSkill)) {
                    listModel.set(selectedIndex, "  ✓  | " + newSkill.trim());
                    skills.set(selectedIndex, newSkill.trim());
                    saveSkills();
                }
            }
        });
        return editButton;
    }


    // Get the Delete Button
    private static JButton getDeleteButton(JList<String> skillsList) {
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener((ActionEvent e) -> {
            int selectedIndex = skillsList.getSelectedIndex();
            if (selectedIndex != -1) {
                skills.remove(selectedIndex);
                listModel.remove(selectedIndex);
                saveSkills();
            }
        });
        return deleteButton;
    }

    // Get the list of skills
    public List<String> getSkillsList() {
        List<String> skillsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(SKILLS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                skillsList.add(line.trim()); // Add skill to list
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading skills file: " + e.getMessage());
        }
        return skillsList;
    }
}
