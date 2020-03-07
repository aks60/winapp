package dataset;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ConnApp {

    private static ConnApp instanceClass = null;
    protected Connection connection = null;
    protected Statement statement = null;
    protected boolean autoCommit = false;

    public static ConnApp initConnect() {

        instanceClass = new ConnFb();
        return instanceClass;
    }

    public static ConnApp ins() {
        return instanceClass;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setAutoCommit(boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            System.out.println("dataset.IConnect.setAutoCommit() " + e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public abstract eExcep createConnection();

    public abstract eExcep createConnection(String server, String port, String base, String user, char[] password);

    public abstract void addUser(String user, String password, String uchId, String role, boolean readwrite);

    public abstract void grantUser(String user, String password, String role, boolean readwrite);

    public abstract void deleteUser(String user);

    public abstract void modifyPassword(String user, String pass);

    public abstract int generatorId(String table);

}
