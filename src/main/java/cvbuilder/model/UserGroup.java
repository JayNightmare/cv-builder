package cvbuilder.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jay
 *
 * This model class may be handy for putting data relevant groups
 * of User Profiles in {many Users}
 */

public class UserGroup {

    private static final List<User> users = new ArrayList<>();

    public static void readNameCSVFile(String filename) throws IOException {
        try (BufferedReader file = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 4) {
                    boolean isSelected = Boolean.parseBoolean(values[0].trim());
                    String name = values[1].trim();
                    String title = values[2].trim();
                    String email = values[3].trim();
                    User user = new User(name, title, email, isSelected);
                    users.add(user);
                }
            }
        }
    }

    public static void writeNameCSVFile(String filename, List<User> users) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (User user : users) {
                pw.println(user.toCSVString()); // Convert user to CSV string representation
            }
        }
    }

    public static List<User> getUsersNames() { return users; }
}
