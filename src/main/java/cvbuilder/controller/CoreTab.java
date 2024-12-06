package cvbuilder.controller;

import cvbuilder.view.ProfileStatementPanel;
import cvbuilder.view.SkillsPanel;

import javax.swing.*;
import java.awt.*;

public class CoreTab {
    private final JTabbedPane subTabbedPane;

    public CoreTab() {
        subTabbedPane = new JTabbedPane();
    }

    public void initializeCoreTab(JPanel corePanel) {
        corePanel.setLayout(new BorderLayout());

        // Instantiate the ProfileStatementPanel and use it to create the subpage
        ProfileStatementPanel profileStatementPanelInstance = new ProfileStatementPanel();
        JPanel profileStatementPanel = profileStatementPanelInstance.createProfileStatementPanel();

        // Ensure this is not static if SkillsPanel state differs between instances
        JPanel skillsPanel = SkillsPanel.createSkillsPanel();

        subTabbedPane.addTab("Profile Statement", profileStatementPanel);
        subTabbedPane.addTab("Skills", skillsPanel);

        corePanel.add(subTabbedPane, BorderLayout.CENTER);
    }
}
