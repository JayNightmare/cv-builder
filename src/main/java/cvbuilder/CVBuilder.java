package cvbuilder;

import javax.swing.*;
import cvbuilder.model.*;
import cvbuilder.view.MainViewer;

import java.io.IOException;

public class CVBuilder {
    public static void main(String[] args) throws IOException {
        // Read the CSV file and populate the user list
        UserGroup.readNameCSVFile("userinfo.csv");

        // needed to run the GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainViewer ui = new MainViewer();
            ui.UI();
        });
    }
}
