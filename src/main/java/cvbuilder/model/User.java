package cvbuilder.model;

/**
 * @author Jay
 *
 * This model class may be handy for putting data relevant to the
 * User Profile in
 */

public class User {
    private final String name;
    private final String title;
    private final String email;
    private final boolean isSelected;

    public String toCSVString() {
        return isSelected() + "," + getName() + "," + getTitle() + "," + getEmail();
    }

    public User(String name, String  title, String email) {
        this.name = name;
        this.title = title;
        this.email = email;
        this.isSelected = false;
    }

    public User(String name, String  title, String email, boolean isSelected) {
        this.name = name;
        this.title = title;
        this.email = email;
        this.isSelected = isSelected;
    }

    public String getName() { return name; }
    public String getTitle() { return title; }
    public String getEmail() { return email; }

    public boolean isSelected() { return isSelected; }
}