package dataset;

import common.eProfile;
import common.eProperty;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JOptionPane;
import startup.App;

/**
 * Соединение через PostgresSQL
 */
public class ConnFb extends dataset.ConnApp {

    public final static String driver = "org.firebirdsql.jdbc.FBDriver";
    public final static String fbserver = "jdbc:firebirdsql:";
    public String url = "";

    /**
     * Соединение с БД
     */
    public eExcep createConnection(String server, String port, String base, String user, char[] password, String role) {
        try {
            if (Class.forName(driver) == null) {
                JOptionPane.showMessageDialog(App.Top.frame, "Ошибка загрузки файла драйвера",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
            String url = fbserver + "//" + server + ":" + port + "/" + base;
            Properties props = new Properties();
            props.setProperty("user", user.toLowerCase());
            props.setProperty("password", String.valueOf(password));
            if (role != null) {
                props.setProperty("roleName", role);
            }
            props.setProperty("encoding", "win1251");
            connection = DriverManager.getConnection(url, props);
            connection.setAutoCommit(true);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        } catch (ClassNotFoundException e) {
            return eExcep.findDrive;
        } catch (SQLException e) {
            return eExcep.getError(e.getErrorCode());
        }
        return eExcep.yesConn;
    }

    //Добавление нового пользователя   
    //CREATE USER <user_name> PASSWORD '<user_password>' [FIRSTNAME 'FirstName'] [MIDDLENAME 'MiddleName'] [LASTNAME 'LastName'];
    public void addUser(String user, String password, String uchId, String role, boolean readwrite) {
        try {
            //создание пользователя
            user = user.toLowerCase();
            String sql = "select a.user2 from school.uchusers a where a.user2 = '" + user + "'";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            if (rs.next()) {
                JOptionPane.showMessageDialog(App.Top.frame, "Есть уже такой пользователь в базе данных !", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String sql1 = "create user " + user + " with password '" + password + "'";
                connection.createStatement().executeUpdate(sql1);
                sql1 = "GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA school, logger to " + user;
                connection.createStatement().executeUpdate(sql1);
                sql1 = "GRANT UPDATE ON ALL SEQUENCES IN SCHEMA school, logger to " + user;
                connection.createStatement().executeUpdate(sql1);
                sql1 = "GRANT USAGE ON SCHEMA school, logger TO " + user;
                connection.createStatement().executeUpdate(sql1);
                if (readwrite == true) {
                    role = role + "_RW";
                } else {
                    role = role + "_RO";
                }
                //такой записи вообще нет
                sql = "insert into school.uchusers(user2, uch, role) values('" + user + "'," + uchId + ",'" + role + "')";
                connection.createStatement().executeUpdate(sql);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public String getUser() {
        return "null";
    }

    //Изменение привилегий пользователя
    public void grantUser(String user, String password, String role, boolean rw) {
        String sql;
        if (rw == true) {
            role = role + "_RW";
        } else {
            role = role + "_RO";
        }
        user = user.toLowerCase();
        sql = "update school.uchusers set role = '" + role + "' where user2 = '" + user + "'";
        try {
            connection.createStatement().executeUpdate(sql);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    //Удаление пользователя
    public void deleteUser(String user) {
        try {
            connection.createStatement().executeUpdate("delete from school.uchusers where user2 = '" + user + "'");
            connection.createStatement().executeUpdate("REVOKE SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA school, logger from " + user);
            connection.createStatement().executeUpdate("REVOKE GRANT OPTION FOR ALL PRIVILEGES ON DATABASE " + eProperty.base1.read() + " FROM " + user);
            connection.createStatement().executeUpdate("REVOKE ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA school, logger FROM " + user);
            connection.createStatement().executeUpdate("REVOKE USAGE ON SCHEMA school, logger FROM " + user);
            connection.createStatement().executeUpdate("DROP USER " + user);

        } catch (SQLException e) {
            System.err.println("Ошибка удаления пользователя" + e);
        }
    }

    //Изменение параметров пользователя
    public void modifyPassword(String user, String pass) {
        try {
            user = user.toLowerCase();
            String sql = "ALTER USER " + user + " WITH PASSWORD '" + pass + "'";
            connection.createStatement().executeUpdate(sql);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    //Генератор ключа ID
    @Override
    public int genId(Field field) {
        try {
            int next_id = 0;
            Statement statement = connection.createStatement();
            String sql = "SELECT GEN_ID(gen_" + field.tname() + ", 1) FROM RDB$DATABASE";
            ResultSet rs = statement.executeQuery(sql);
            /*String mySeqv = table_name + "_id_seq";
            ResultSet rs = statement.executeQuery("SELECT nextval('" + mySeqv + "')");*/
            if (rs.next()) {
                next_id = rs.getInt("GEN_ID");
            }
            rs.close();
            return next_id;
        } catch (SQLException e) {
            System.err.println("Ошибка генерации ключа " + e);
            return 0;
        }
    }

    @Override
    public String version() {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT rdb$get_context('SYSTEM', 'ENGINE_VERSION') as version from rdb$database";
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            String v = rs.getString("VERSION");
            rs.close();
            return v;

        } catch (SQLException e) {
            System.err.println("Ошибка получения версии " + e);
            return "";
        }
    }
}
