package convert;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.Util;
import common.eProperty;
import dataset.ConnApp;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtdet;
import domain.eArtgrp;
import domain.eArtikl;
import domain.eColgrp;
import domain.eColor;
import domain.eColpar1;
import domain.eCurrenc;
import domain.eElemdet;
import domain.eElement;
import domain.eJoining;
import domain.eElemgrp;
import domain.eElempar1;
import domain.eElempar2;
import domain.eFurndet;
import domain.eFurniture;
import domain.eFurnpar1;
import domain.eFurnpar2;
import domain.eFurnside1;
import domain.eFurnside2;
import domain.eGlasdet;
import domain.eGlasgrp;
import domain.eGlaspar1;
import domain.eGlaspar2;
import domain.eGlasprof;
import domain.eJoindet;
import domain.eJoinpar1;
import domain.eJoinpar2;
import domain.eJoinvar;
import domain.eKitdet;
import domain.eKitpar1;
import domain.eKits;
import domain.eOrders;
import domain.eParams;
import domain.ePartner;
import domain.eRulecalc;
import domain.eSyssize;
import domain.eSysdata;
import domain.eSysfurn;
import domain.eSyspar1;
import domain.eSysprod;
import domain.eSysprof;
import domain.eSystree;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import wincalc.script.Winscript;

/**
 * В пс3 и пс4 разное количество полей в таблицах, но список столбцов в
 * прилшжении eEnum.values() для них один. Отсутствующие поля пс3 в
 * eEnum.values() будут заполняться пустышками. 3. Поля не вошедшие в список
 * столбцов eEnum.values() тоже будут переноситься для sql update и потом
 * удаляться. Обновление данных выполняется пакетом, если была ошибка в пакете,
 * откат и пакет обслуживается отдельными insert.
 */
public class Profstroy {

    private static char versionPs = 4;
    private static Connection cn1;
    private static Connection cn2;
    private static Statement st1; //источник 
    private static Statement st2;//приёмник

    public static void script() {
        Field[] fieldsUp = { //порядок записи определён в ссответсвии с зависимостями
            eSyssize.up, eSysdata.up, eParams.up, eRulecalc.up, ePartner.up, eOrders.up,
            eKitpar1.up, eKitdet.up, eKits.up,
            eJoinpar2.up, eJoinpar1.up, eJoindet.up, eJoinvar.up, eJoining.up,
            eElempar1.up, eElempar2.up, eElemdet.up, eElement.up, eElemgrp.up,
            eGlaspar1.up, eGlaspar2.up, eGlasdet.up, eGlasprof.up, eGlasgrp.up,
            eSyspar1.up, eSysprof.up, eSysfurn.up, eSysprod.up, eSystree.up,
            eFurnpar1.up, eFurnpar2.up, eFurnside1.up, eFurnside2.up, eFurndet.up, eFurniture.up,
            eArtdet.up, eArtikl.up, eArtgrp.up,
            eColpar1.up, eColor.up, eColgrp.up,
            eCurrenc.up
        };
        try {
            String src = "jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.fdb?encoding=win1251";
            //String src = "jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\ITEST.FDB?encoding=win1251";
            String out = "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251";

            cn1 = java.sql.DriverManager.getConnection(src, "sysdba", "masterkey"); //источник
            cn2 = java.sql.DriverManager.getConnection(out, "sysdba", "masterkey"); //приёмник

            System.out.println("\u001B[32m" + "Подготовка методанных" + "\u001B[0m");
            st1 = cn1.createStatement(); //источник 
            st2 = cn2.createStatement();//приёмник
            DatabaseMetaData mdb1 = cn1.getMetaData();
            DatabaseMetaData mdb2 = cn2.getMetaData();
            ResultSet resultSet1 = mdb1.getTables(null, null, null, new String[]{"TABLE"});
            ResultSet resultSet2 = mdb2.getTables(null, null, null, new String[]{"TABLE"});

            List<String> listExistTable1 = new ArrayList<String>();//таблицы источника
            List<String> listExistTable2 = new ArrayList<String>();//таблицы приёмника
            List<String> listGenerator2 = new ArrayList<String>();//генераторы приёмника 

            while (resultSet1.next()) {
                listExistTable1.add(resultSet1.getString("TABLE_NAME"));
                if ("CONNECT".equals(resultSet1.getString("TABLE_NAME"))) {
                    versionPs = 3;
                    eProperty.versionDb.write("3");
                    eJoining.up.meta().fname = "CONNECT";
                } else {
                    eProperty.versionDb.write("4");
                }
                eProperty.storeProperty();
            }
            while (resultSet2.next()) {
                listExistTable2.add(resultSet2.getString("TABLE_NAME"));
            }
            //Генераторы приёмника
            resultSet2 = st2.executeQuery("select rdb$generator_name from rdb$generators");
            while (resultSet2.next()) {
                listGenerator2.add(resultSet2.getString("RDB$GENERATOR_NAME").trim());
            }
            System.out.println("\u001B[32m" + "Перенос данных" + "\u001B[0m");
            //Цыкл по доменам приложения
            for (Field fieldUp : fieldsUp) {
                //Поля не вошедшие в eEnum.values()
                HashMap<String, String[]> hmDeltaCol = deltaColumn(mdb1, fieldUp);//в последствии будут использоваться для sql update

                //Удаление таблиц приёмника
                if (listExistTable2.contains(fieldUp.tname()) == true) {
                    sql("DROP TABLE " + fieldUp.tname() + ";");
                }
                //Удаление генератора приёмника
                if (listGenerator2.contains("GEN_" + fieldUp.tname()) == true) {
                    sql("DROP GENERATOR GEN_" + fieldUp.tname() + ";");
                }
                //Создание таблицы приёмника
                for (String ddl : Profstroy.createTable(fieldUp.fields())) {
                    st2.execute(ddl);
                }
                //Добавление столбцов не вошедших в eEnum.values()
                for (Map.Entry<String, String[]> entry : hmDeltaCol.entrySet()) {
                    String deltaCol[] = entry.getValue();
                    sql("ALTER TABLE " + fieldUp.tname() + " ADD " + entry.getKey() + " " + Util.typeSql(Field.TYPE.type(deltaCol[0]), deltaCol[1]) + ";");
                }
                //Конвертирование данных в таблицу
                if (listExistTable1.contains(fieldUp.meta().fname) == true) {
                    convertTable(cn1, cn2, hmDeltaCol, fieldUp.fields());
                }
                //Создание генератора таблицы
                sql("CREATE GENERATOR GEN_" + fieldUp.tname());
                if ("id".equals(fieldUp.fields()[1].meta().fname)) {
                    sql("UPDATE " + fieldUp.tname() + " SET id = gen_id(gen_" + fieldUp.tname() + ", 1)"); //заполнение ключей
                    sql("CREATE OR ALTER TRIGGER " + fieldUp.tname() + "_bi FOR " + fieldUp.tname() + " ACTIVE BEFORE INSERT POSITION 0 as begin"
                            + " if (new.id is null) then new.id = gen_id(gen_" + fieldUp.tname() + ", 1); end");
                }
                sql("ALTER TABLE " + fieldUp.tname() + " ADD CONSTRAINT PK_" + fieldUp.tname() + " PRIMARY KEY (ID);"); //DDL создание первичного ключа
            }
            //cn1.close();  //Закроем соединение источника

            System.out.println("\u001B[32m" + "Добавление комментариев к полям" + "\u001B[0m");
            for (Field field : fieldsUp) {
                sql("COMMENT ON TABLE " + field.tname() + " IS '" + field.meta().descr() + "'"); //DDL описание таблиц
            }
            deletePart(cn2, st2);
            updatePart(cn2, st2);
            foreignkeyPart(cn2, st2);

            System.out.println("\u001B[32m" + "Удаление столбцов не вошедших в eEnum.values()" + "\u001B[0m");
            for (Field fieldUp : fieldsUp) {
                HashMap<String, String[]> hmDeltaCol = deltaColumn(mdb1, fieldUp);
                for (Map.Entry<String, String[]> entry : hmDeltaCol.entrySet()) {
                    sql("ALTER TABLE " + fieldUp.tname() + " DROP  " + entry.getKey() + ";");
                }
            }
            System.out.println("\u001B[34m" + "ОБНОВЛЕНИЕ ЗАВЕРШЕНО" + "\u001B[0m");

        } catch (Exception e) {
            System.out.println("\u001B[31m" + "SQL-SCRIPT: " + e + "\u001B[0m");
        }
    }

    /**
     * Создание таблицы
     *
     * @param f все поля соэдаваемой таблицы
     * @return список ddl операторов создания таблицы
     */
    public static ArrayList<String> createTable(Field... f) {

        ArrayList<String> batch = new ArrayList();
        String ddl = "CREATE TABLE " + f[0].tname() + " (";
        for (int i = 1; i < f.length; ++i) {

            Field f2 = f[i];
            ddl = ddl + "\n" + f2.name() + "  " + Util.typeSql(f2.meta().type(), f2.meta().size());
            if (f2.meta().isnull() == false) {
                ddl = ddl + " NOT NULL";
            }
            ddl += ",";
        }
        ddl = ddl.substring(0, ddl.length() - 1) + ");";
        batch.add(ddl);
        for (int i = 1; i < f.length; ++i) {
            batch.add("COMMENT ON COLUMN \"" + f[i].tname() + "\"." + f[i].name() + " IS '" + f[i].meta().descr() + "';");
        }
        return batch;
    }

    /**
     * Конвертор данных таблиц
     *
     * @param cn1 соединение источника
     * @param cn2 соединение приёмника
     * @param fields все поля таблицы
     * @param hmDeltaCol поля не вошедшие в eEnum.values()
     */
    public static void convertTable(Connection cn1, Connection cn2, HashMap<String, String[]> hmDeltaCol, Field[] fields) {
        String sql = "";
        try {
            int count = 0; //колчество записей для расчёта кол. пакетов
            String tname1 = fields[0].meta().fname;
            String tname2 = fields[0].tname();
            HashSet hsExistField = new HashSet(); //список полей которые есть в источнике и в eEnum.values()
            boolean bash = true;
            Statement st1 = cn1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            Statement st2 = cn2.createStatement();
            ResultSet rs1 = st1.executeQuery("select count(*) from " + tname1);
            if (rs1.next()) {
                count = rs1.getInt(1);
            }
            //Цыкл по пакетам
            for (int index_page = 0; index_page <= count / 500; ++index_page) {

                System.out.println("Таблица:" + tname2 + " пакет:" + index_page);
                String nameCols2 = "";
                rs1 = st1.executeQuery("select first 500 skip " + index_page * 500 + " * from " + tname1);
                ResultSetMetaData md1 = rs1.getMetaData();
                for (int index = 1; index <= md1.getColumnCount(); index++) {

                    //Список полей которые есть в источнике и в eEnum.values()
                    String fn = md1.getColumnLabel(index);
                    for (Field f : fields) {
                        if (f.meta().fname.equalsIgnoreCase(fn)) {
                            hsExistField.add(f);
                        }
                    }
                }
                //Строка insert into(...)
                for (int index = 1; index < fields.length; index++) {
                    Field field = fields[index];
                    nameCols2 = nameCols2 + field.name() + ",";
                }
                for (Map.Entry<String, String[]> entry : hmDeltaCol.entrySet()) { //поля для sql update (в конце будут удалены)
                    nameCols2 = nameCols2 + entry.getKey() + ",";
                }
                nameCols2 = nameCols2.substring(0, nameCols2.length() - 1);
                //Строка values(...)
                while (rs1.next()) {
                    String nameVal2 = "";
                    //Цыкл по полям eEnum.values()
                    for (int index = 1; index < fields.length; index++) {
                        Field field = fields[index];
                        if (hsExistField.contains(field)) { //т.к. ps3 и ps4 разное количество полей
                            Object val = rs1.getObject(field.meta().fname);
                            nameVal2 = nameVal2 + Util.wrapperSql(val, field.meta().type()) + ",";
                        } else {
                            if (field.meta().isnull() == false) { //если not null то тупо пишу 0
                                nameVal2 = nameVal2 + "0" + ",";
                            } else {
                                nameVal2 = nameVal2 + "null" + ",";
                            }
                        }
                    }
                    //Цыкл по полям не вошедших в eEnum.values()
                    for (Map.Entry<String, String[]> entry : hmDeltaCol.entrySet()) {
                        Object val = rs1.getObject(entry.getKey());
                        nameVal2 = nameVal2 + Util.wrapperSql(val, Field.TYPE.type(entry.getValue()[0])) + ",";
                    }
                    nameVal2 = nameVal2.substring(0, nameVal2.length() - 1);
                    sql = "insert into " + tname2 + "(" + nameCols2 + ") values (" + nameVal2.toString() + ")";
                    if (bash == true) {
                        st2.addBatch(sql);
                    } else {
                        try {  //Если была ошибка в пакете выполняю отдельные sql insert
                            st2.executeUpdate(sql);
                        } catch (SQLException e) {
                            System.out.println("\u001B[31m" + "SCRIPT-INSERT:  " + e + "\u001B[0m");
                        }
                    }
                }
                bash = true;
                cn2.setAutoCommit(false);
                try {
                    //Пакетный insert
                    st2.executeBatch();
                    cn2.commit();
                    st2.clearBatch();
                    cn2.setAutoCommit(true);

                } catch (SQLException e) {
                    cn2.rollback();
                    bash = false;
                    --index_page;
                    cn2.setAutoCommit(true);
                    System.out.println("\u001B[31m" + "SCRIPT-BATCH:  " + e + "\u001B[0m");
                }
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "CONVERT-TABLE:  " + e + "\u001B[0m");
        }
    }

    private static HashMap<String, String[]> deltaColumn(DatabaseMetaData mdb1, Field fieldUp) {
        try {
            HashMap<String, String[]> hmDeltaCol = new HashMap(); //поля не вошедшие в eEnum.values(), в последствии будут использоваться для sql update
            ResultSet rsc1 = mdb1.getColumns(null, null, fieldUp.meta().fname, null);
            while (rsc1.next()) {
                String table_name = rsc1.getString("TABLE_NAME");
                if (fieldUp.meta().fname.equals(table_name)) {

                    String column_name = rsc1.getString("COLUMN_NAME");
                    String[] val = {rsc1.getString("DATA_TYPE"), rsc1.getString("COLUMN_SIZE")};
                    if ("-1".equals(rsc1.getString("DATA_TYPE")) || "-4".equals(rsc1.getString("DATA_TYPE"))) {
                        val[1] = "80";
                    }
                    boolean find = false;
                    for (Field field : fieldUp.fields()) {
                        if (field.meta().fname.equalsIgnoreCase(column_name)) {
                            find = true;
                        }
                    }
                    if (find == false) {
                        hmDeltaCol.put(column_name, val);
                    }
                }
            }
            return hmDeltaCol;
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "DELTA-COLUMN: " + e + "\u001B[0m");
            return null;
        }
    }

    private static void deletePart(Connection cn2, Statement st2) {
        try {
            ConnApp con = ConnApp.initConnect();
            con.setConnection(cn2);
            System.out.println("\u001B[32m" + "Секция удаления потеренных ссылок (фантомов)" + "\u001B[0m");
            sql("delete from params where grup > 0");  //group > 0
            sql("delete from color where not exists (select id from colgrp a where a.id = color.cgrup)");  //colgrp_id
            sql("delete from colpar1 where not exists (select id from color a where a.cnumb = colpar1.psss)"); //color_id
            sql("delete from artdet where not exists (select id from artikl a where a.code = artdet.anumb)");  //artikl_id
            sql("delete from artdet where not exists (select id from color a where a.ccode = artdet.clcod and a.cnumb = artdet.clnum)");  //color_fk
            sql("delete from element where not exists (select id from artikl a where a.code = element.anumb)");  //artikl_id
            sql("delete from elemdet where not exists (select id from artikl a where a.code = elemdet.anumb)");  //artikl_id            
            sql("delete from elemdet where not exists (select id from color a where a.cnumb = elemdet.color_fk) and elemdet.color_fk > 0 and elemdet.color_fk != 100000"); //color_fk
            sql("delete from elemdet where not exists (select id from element a where a.vnumb = elemdet.vnumb)");  //element_id
            sql("delete from elempar1 where not exists (select id from element a where a.vnumb = elempar1.psss)");  //element_id 
            sql("delete from elempar2 where not exists (select id from elemdet a where a.aunic = elempar2.psss)");  //elemdet_id
            sql("delete from joining where not exists (select id from artikl a where a.code = joining.anum1)");  //artikl_id1
            sql("delete from joining where not exists (select id from artikl a where a.code = joining.anum2)");  //artikl_id2
            sql("delete from joinvar where not exists (select id from joining a where a.cconn = joinvar.cconn)");  //joining_id
            sql("delete from joindet where not exists (select id from joinvar a where a.cunic = joindet.cunic)");  //joinvar_id
            sql("delete from joindet where not exists (select id from color a where a.cnumb = joindet.color_fk) and joindet.color_fk > 0 and joindet.color_fk != 100000"); //color_fk
            sql("delete from joinpar1 where not exists (select id from joinvar a where a.cunic = joinpar1.psss)");  //joinvar_id
            sql("delete from joinpar2 where not exists (select id from joindet a where a.aunic = joinpar2.psss)");  //joindet_id 
            sql("delete from glasprof where not exists (select id from glasgrp a where a.gnumb = glasprof.gnumb)");  //glasgrp_id
            sql("delete from glasprof where not exists (select id from artikl a where a.code = glasprof.anumb)");  //artikl_id
            sql("delete from glasdet where not exists (select id from glasgrp a where a.gnumb = glasdet.gnumb)");  //glasgrp_id
            sql("delete from glasdet where not exists (select id from color a where a.cnumb = glasdet.color_fk) and glasdet.color_fk > 0 and glasdet.color_fk != 100000"); //color_fk
            sql("delete from glasdet where not exists (select id from artikl a where a.code = glasdet.anumb)");  //artikl_id
            sql("delete from glaspar1 where not exists (select id from glasgrp a where a.gnumb = glaspar1.psss)");  //glasgrp_id
            sql("delete from glaspar2 where not exists (select id from glasdet a where a.gunic = glaspar2.psss)");  //glasdet_id
            sql("delete from furnside1 where not exists (select id from furniture a where a.funic = furnside1.funic)"); //furniture_id
            sql("delete from furnpar1 where not exists (select id from furnside1 a where a.fincr = furnpar1.psss)");  //furnside_id           
            sql("delete from furndet where not exists (select id from furniture a where a.funic = furndet.funic)");  //furniture_id
            sql("delete from furndet where not exists (select id from artikl a where a.code = furndet.anumb and furndet.anumb != 'НАБОР')");  //artikl_id
            sql("delete from furndet where not exists (select id from color a where a.cnumb = furndet.color_fk) and furndet.color_fk > 0 and furndet.color_fk != 100000"); //color_fk           
            sql("delete from furnpar2 where not exists (select id from furndet a where a.fincb = furnpar2.psss)"); //furndet_id
            sql("delete from sysprof where not exists (select id from artikl a where a.code = sysprof.anumb)");  //artikl_id
            sql("delete from sysprof where not exists (select id from systree a where a.nuni = sysprof.nuni)");  //systree_id
            sql("delete from sysfurn where not exists (select id from furniture a where a.funic = sysfurn.funic)");  //furniture_id
            sql("delete from sysfurn where not exists (select id from systree a where a.nuni = sysfurn.nuni)");  //systree_id
            sql("delete from syspar1 where not exists (select id from systree a where a.nuni = syspar1.psss)");  //systree_id
            sql("delete from kits where not exists (select id from artikl a where a.code = kits.anumb)");  //artikl_id
            sql("delete from kitdet where not exists (select id from kits a where a.kunic = kitdet.kunic)");  //kits_id
            sql("delete from kitdet where not exists (select id from artikl a where a.code = kitdet.anumb)");  //artikl_id 
            sql("delete from kitpar1 where not exists (select id from kitdet a where a.kincr = kitpar1.psss)");  //kitdet_id
        } catch (Exception e) {
            System.out.println("\u001B[31m" + "DELETE-PART:  " + e + "\u001B[0m");
        }
    }

    private static void updatePart(Connection cn2, Statement st2) {
        try {
            ConnApp con = ConnApp.initConnect();
            con.setConnection(cn2);
            System.out.println("\u001B[32m" + "Секция коррекции внешних ключей" + "\u001B[0m");
            sql("update color set colgrp_id = (select id from colgrp a where a.id = color.cgrup)");
            sql("update colpar1 set color_id = (select id from color a where a.cnumb = colpar1.psss)");
            sql("update artdet set artikl_id = (select id from artikl a where a.code = artdet.anumb)");
            sql("update artdet set color_fk = (select id from color a where a.ccode = artdet.clcod and a.cnumb = artdet.clnum)");
            sql("update artdet set color_fk = artdet.clnum where artdet.clnum < 0");
            updateElemgrp();
            sql("update element set elemgrp_id = (select id from elemgrp a where a.name = element.vpref and a.level = element.atypm)");
            sql("update element set artikl_id = (select id from artikl a where a.code = element.anumb)");
            sql("update elemdet set artikl_id = (select id from artikl a where a.code = elemdet.anumb)");
            if (versionPs == 4) {
                sql("update artikl set analog_id = (select id from artikl a where a.code = artikl.amain)");
                sql("update artikl set syssize_id = (select id from syssize a where a.sunic = artikl.sunic)");
            }
            sql("update elemdet set element_id = (select id from element a where a.vnumb = elemdet.vnumb)");
            sql("update elemdet set color_fk = (select id from color a where a.cnumb = elemdet.color_fk) where elemdet.color_fk > 0 and elemdet.color_fk != 100000");
            sql("update elempar1 set element_id = (select id from element a where a.vnumb = elempar1.psss)");
            sql("update elempar2 set elemdet_id = (select id from elemdet a where a.aunic = elempar2.psss)");
            sql("update joining set artikl_id1 = (select id from artikl a where a.code = joining.anum1)");
            sql("update joining set artikl_id2 = (select id from artikl a where a.code = joining.anum2)");
            sql("update joinvar set joining_id = (select id from joining a where a.cconn = joinvar.cconn)");
            sql("update joindet set joinvar_id = (select id from joinvar a where a.cunic = joindet.cunic)");
            sql("update joindet set artikl_id = (select id from artikl a where a.code = joindet.anumb)");
            sql("update joindet set color_fk = (select id from color a where a.cnumb = joindet.color_fk) where joindet.color_fk > 0 and joindet.color_fk != 100000");
            sql("update joinpar1 set joinvar_id = (select id from joinvar a where a.cunic = joinpar1.psss)");
            sql("update joinpar2 set joindet_id = (select id from joindet a where a.aunic = joinpar2.psss)");
            sql("update glasprof set glasgrp_id = (select id from glasgrp a where a.gnumb = glasprof.gnumb)");
            sql("update glasprof set artikl_id = (select id from artikl a where a.code = glasprof.anumb)");
            sql("update glasdet set glasgrp_id = (select id from glasgrp a where a.gnumb = glasdet.gnumb)");
            sql("update glasdet set artikl_id = (select id from artikl a where a.code = glasdet.anumb)");
            sql("update glasdet set color_fk = (select id from color a where a.cnumb = glasdet.color_fk) where glasdet.color_fk > 0 and glasdet.color_fk != 100000");
            sql("update glaspar1 set glasgrp_id = (select id from glasgrp a where a.gnumb = glaspar1.psss)");
            sql("update glaspar2 set glasdet_id = (select id from glasdet a where a.gunic = glaspar2.psss)");
            sql("update furnside1 set furniture_id = (select id from furniture a where a.funic = furnside1.funic)");
            sql("update furnside1 set type_side = ( CASE  WHEN (FTYPE = 'сторона') THEN 1 WHEN (FTYPE = 'ось поворота') THEN 2 WHEN (FTYPE = 'крепление петель') THEN 3 ELSE  (1) END )");
            sql("update furnpar1 set furnside_id = (select id from furnside1 a where a.fincr = furnpar1.psss)");
            sql("update furndet set furniture_id = (select id from furniture a where a.funic = furndet.funic)");
            sql("update furndet set color_fk = (select id from color a where a.cnumb = furndet.color_fk) where furndet.color_fk > 0 and furndet.color_fk != 100000");
            sql("update furndet set artikl_id = (select id from artikl a where a.code = furndet.anumb and furndet.anumb != 'НАБОР')");
            sql("update furnpar2 set furndet_id = (select id from furndet a where a.fincb = furnpar2.psss)");
            sql("update systree set parent_id = (select id from systree a where a.nuni = systree.npar and systree.npar != 0)");
            sql("update systree set parent_id = id where npar = 0");
            sql("update sysprof set artikl_id = (select id from artikl a where a.code = sysprof.anumb)");
            sql("update sysprof set systree_id = (select id from systree a where a.nuni = sysprof.nuni)");
            sql("update sysfurn set furniture_id = (select id from furniture a where a.funic = sysfurn.funic)");
            sql("update sysfurn set systree_id = (select id from systree a where a.nuni = sysfurn.nuni)");
            sql("update syspar1 set systree_id = (select id from systree a where a.nuni = syspar1.psss)");
            updateSysprod();
            sql("update kits set artikl_id = (select id from artikl a where a.code = kits.anumb)");
            sql("update kits set color_id = (select id from color a where a.cnumb = kits.clnum)");
            sql("update kitdet set kits_id = (select id from kits a where a.kunic = kitdet.kunic)");
            sql("update kitdet set artikl_id = (select id from artikl a where a.code = kitdet.anumb)");
            sql("update kitdet set color1_id = (select id from color a where a.cnumb = kitdet.clnum)");
            sql("update kitdet set color2_id = (select id from color a where a.cnumb = kitdet.clnu1)");
            sql("update kitdet set color3_id = (select id from color a where a.cnumb = kitdet.clnu2)");
            sql("update kitpar1 set kitdet_id = (select id from kitdet a where a.kincr = kitpar1.psss)");
        } catch (Exception e) {
            System.out.println("\u001B[31m" + "UPDATE-PART:  " + e + "\u001B[0m");
        }
    }

    private static void foreignkeyPart(Connection cn2, Statement st2) {
        try {
            System.out.println("\u001B[32m" + "Секция создания внешних ключей" + "\u001B[0m");
            sql("alter table artikl add constraint fk_artikl1 foreign key (currenc_id) references currenc (id)");
            sql("alter table color add constraint fk_color1 foreign key (colgrp_id) references colgrp (id)");
            sql("alter table artdet add constraint fk_artdet1 foreign key (artikl_id) references artikl (id)");
            //sql("alter table artdet add constraint fk_artdet2 foreign key (color_fk) references color (id)");
            sql("alter table element add constraint fk_element1 foreign key (elemgrp_id) references elemgrp (id)");
            sql("alter table element add constraint fk_element2 foreign key (artikl_id) references artikl (id)");
            sql("alter table elemdet add constraint fk_elemdet1 foreign key (artikl_id) references artikl (id)");
            sql("alter table elemdet add constraint fk_elemdet2 foreign key (element_id) references element (id)");
            sql("alter table elempar1 add constraint fk_elempar1 foreign key (element_id) references element (id)");
            sql("alter table elempar2 add constraint fk_elempar2 foreign key (elemdet_id) references elemdet (id)");
            sql("alter table joining add constraint fk_joining1 foreign key (artikl_id1) references artikl (id)");
            sql("alter table joining add constraint fk_joining2 foreign key (artikl_id2) references artikl (id)");
            sql("alter table joinvar add constraint fk_joinvar1 foreign key (joining_id) references joining (id)");
            sql("alter table joindet add constraint fk_joindet1 foreign key (joinvar_id) references joinvar (id)");
            sql("alter table joinpar1 add constraint fk_joinpar1 foreign key (joinvar_id) references joinvar (id)");
            sql("alter table joinpar2 add constraint fk_joinpar2 foreign key (joindet_id) references joindet (id)");
            sql("alter table glasprof add constraint fk_glasprof1 foreign key (glasgrp_id) references glasgrp (id)");
            sql("alter table glasprof add constraint fk_glasprof2 foreign key (artikl_id) references artikl (id)");
            sql("alter table glasdet add constraint fk_glasdet1 foreign key (glasgrp_id) references glasgrp (id)");
            sql("alter table glasdet add constraint fk_glasdet2 foreign key (artikl_id) references artikl (id)");
            sql("alter table glaspar1 add constraint fk_glaspar1 foreign key (glasgrp_id) references glasgrp (id)");
            sql("alter table glaspar2 add constraint fk_glaspar2 foreign key (glasdet_id) references glasdet (id)");
            sql("alter table furnside1 add constraint fk_furnside1 foreign key (furniture_id) references furniture (id)");
            sql("alter table furnpar1 add constraint fk_furnpar1 foreign key (furnside_id) references furnside1 (id)");
            sql("alter table furndet add constraint fk_furndet1 foreign key (furniture_id) references furniture (id)");
            sql("alter table furndet add constraint fk_furndet2 foreign key (artikl_id) references artikl (id)");
            sql("alter table furnpar2 add constraint fk_furnpar2 foreign key (furndet_id) references furndet (id)");
            sql("alter table sysprof add constraint fk_sysprof1 foreign key (artikl_id) references artikl (id)");
            sql("alter table sysprof add constraint fk_sysprof2 foreign key (systree_id) references systree (id)");
            sql("alter table sysfurn add constraint fk_sysfurn1 foreign key (systree_id) references systree (id)");
            sql("alter table sysfurn add constraint fk_sysfurn2 foreign key (furniture_id) references furniture (id)");
            sql("alter table syspar1 add constraint fk_syspar1 foreign key (systree_id) references systree (id)");
            sql("alter table kits add constraint fk_kits1 foreign key (artikl_id) references artikl (id)");
            sql("alter table kits add constraint fk_kits2 foreign key (color_id) references color (id)");
            sql("alter table kitdet add constraint fk_kitdet1 foreign key (kits_id) references kits (id)");
            sql("alter table kitdet add constraint fk_kitdet2 foreign key (artikl_id) references artikl (id)");
            sql("alter table kitdet add constraint fk_kitdet3 foreign key (color1_id) references color (id)");
            sql("alter table kitdet add constraint fk_kitdet4 foreign key (color2_id) references color (id)");
            sql("alter table kitdet add constraint fk_kitdet5 foreign key (color3_id) references color (id)");
            sql("alter table kitpar1 add constraint fk_kitpar1 foreign key (kitdet_id) references kitdet (id)");
            //sql("alter table mmm add constraint fk_mmm foreign key (yyy_id) references yyy (id)"); 
        } catch (Exception e) {
            System.out.println("\u001B[31m" + "ALTERDB-PERT:  " + e + "\u001B[0m");
        }
    }

    private static void updateElemgrp() throws SQLException {
        System.out.println("updateElemgrp()");
        Query.connection = cn2;
        Query q = new Query(eElemgrp.values()).table(eElemgrp.up);
        ResultSet rs = st2.executeQuery("select distinct VPREF, ATYPM from element order by  ATYPM, VPREF");
        ArrayList<Object[]> fieldList = new ArrayList();
        while (rs.next()) {
            fieldList.add(new Object[]{rs.getString("VPREF"), rs.getInt("ATYPM")});
        }
        for (Object[] obj : fieldList) {
            Record record = q.newRecord(Query.INS);
            record.setNo(eElemgrp.id, ConnApp.instanc().generatorId(eElemgrp.up));
            record.setNo(eElemgrp.name, obj[0]);
            record.setNo(eElemgrp.level, obj[1]);
            q.insert(record);
        }
    }

    private static void updateSysprod() throws SQLException {
        System.out.println("updateSysprod()");
        Integer prj[] = {601001, 601002, 601003, 601004, 601005, 601006, 601007,
            601008, 601009, 601010, 604004, 604005, 604006, 604007, 604008, 604009, 604010};
        for (int index = 0; index < prj.length; ++index) {

            String script = Winscript.test(prj[index], -1, -1, -1, -1);
            JsonElement jsonElem = new Gson().fromJson(script, JsonElement.class);
            JsonObject jsonObj = jsonElem.getAsJsonObject();
            String name = jsonObj.get("prj").getAsString()
                    + "-  " + jsonObj.get("name").getAsString();

            Query q = new Query(eSysprod.values()).table(eSysprod.up);
            Record record = q.newRecord(Query.INS);
            record.setNo(eSysprod.npp, index + 1);
            record.setNo(eSysprod.id, ConnApp.instanc().generatorId(eSysprod.up));
            record.setNo(eSysprod.name, name);
            record.setNo(eSysprod.script, script);
            q.insert(record);
        }
    }

    private static void sql(String str) {
        try {
            System.out.println(str);
            st2.execute(str);
        } catch (Exception e) {
            System.out.println("\u001B[31m" + "SQL-DB:  " + e + "\u001B[0m");
        }
    }
}
