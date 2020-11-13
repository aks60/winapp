package dataset;

import frames.Util;
import common.eProfile;
import common.eProperty;
import static dataset.Query.connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 * Соединение через PostgresSQL
 */
public class ConnFb extends dataset.ConnApp {

    public final static String driver = "org.firebirdsql.jdbc.FBDriver";
    public final static String fbserver = "jdbc:firebirdsql:";
    public String url = "";

    public void configApp() {
        Util.setSimpleDateFormat(new SimpleDateFormat("dd.MM.yyyy"));
    }

    /**
     * Соединение с БД
     */
    public eExcep createConnection(int num_base) {
        try {
            if (Class.forName(driver) == null) {
                return eExcep.loadDrive; //Ошибка загрузки файла драйвера;
            }
            if (num_base == 1) {
                url = fbserver + "//" + eProperty.server1.read() + ":" + eProperty.port.read() + "/" + eProperty.base1.read();
            } else if (num_base == 2) {
                url = fbserver + "//" + eProperty.server2.read() + ":" + eProperty.port.read() + "/" + eProperty.base2.read();
            } else if (num_base == 3) {
                url = fbserver + "//" + eProperty.server3.read() + ":" + eProperty.port.read() + "/" + eProperty.base3.read();
            }
            String user2 = eProperty.user.read();
            String passw = eProperty.password;
            connection = DriverManager.getConnection(url, user2, passw);
            connection.setAutoCommit(true);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        } catch (ClassNotFoundException e) {
            return eExcep.findDrive;
        } catch (SQLException e) {
            System.out.println(e);
            return eExcep.getError(e.getErrorCode());
        }
        configApp();
        return eExcep.yesConn;
    }

    public eExcep createConnection(String server, String port, String base, String user, char[] password) {
        try {
            if (Class.forName(driver) == null) {
                JOptionPane.showMessageDialog(eProfile.appframe, "Ошибка загрузки файла драйвера",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
            String url = fbserver + "//" + server + ":" + port + "/" + base + "?characterEncoding=cp1251";

            user = user.toLowerCase();
            connection = DriverManager.getConnection(url, user, String.valueOf(password));
            connection.setAutoCommit(true);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        } catch (ClassNotFoundException e) {
            return eExcep.findDrive;
        } catch (SQLException e) {
            return eExcep.getError(e.getErrorCode());
        }
        configApp();
        return eExcep.yesConn;
    }

    //Добавление нового пользователя
    public void addUser(String user, String password, String uchId, String role, boolean readwrite) {
        try {
            //создание пользователя
            user = user.toLowerCase();
            String sql = "select a.user2 from school.uchusers a where a.user2 = '" + user + "'";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            if (rs.next()) {
                JOptionPane.showMessageDialog(eProfile.appframe, "Есть уже такой пользователь в базе данных !", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
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
            System.out.println(e);
        }
    }

    //Изменение привилегий пользователя
    public void grantUser(String user, String password, String role, boolean readwrite) {
        String sql;
        if (readwrite == true) {
            role = role + "_RW";
        } else {
            role = role + "_RO";
        }
        user = user.toLowerCase();
        sql = "update school.uchusers set role = '" + role + "' where user2 = '" + user + "'";
        try {
            connection.createStatement().executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
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
            System.out.println(e);
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
}
