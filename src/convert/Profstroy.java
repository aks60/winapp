package convert;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.Util;
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
import domain.eSetting;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import wincalc.script.Winscript;

/**
 * В пс3 и пс4 разное количество полей в таблицах, но список столбцов в
 * программе eEnum.values() для них один. Отсутствующие поля пс3 в
 * eEnum.values() будут заполняться пустышками. 3. Поля не вошедшие в список
 * столбцов eEnum.values() тоже будут переноситься для sql update и потом
 * удаляться. Обновление данных выполняется пакетом, если была ошибка в пакете,
 * откат и пакет обслуживается отдельными insert.
 */
public class Profstroy {

    private static int versionPs = 0;
    private static Connection cn1;
    private static Connection cn2;
    private static Statement st1; //источник 
    private static Statement st2;//приёмник

    public static void script() {
        Field[] fieldsUp = { //в порядке удаления
            eSetting.up, eSyssize.up, eSysdata.up, eParams.up, eRulecalc.up, ePartner.up, eOrders.up,
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
            //String src = "jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Alutex3\\aluteh.fdb?encoding=win1251";
            //String src = "jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\ITEST.FDB?encoding=win1251";            
            String out = "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251";

            cn1 = java.sql.DriverManager.getConnection(src, "sysdba", "masterkey"); //источник
            cn2 = java.sql.DriverManager.getConnection(out, "sysdba", "masterkey"); //приёмник

            System.out.println("\u001B[32m" + "Подготовка методанных" + "\u001B[0m");
            cn2.setAutoCommit(false);
            Query.connection = cn2;
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
                    eJoining.up.meta().fname = "CONNECT";
                }
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
                    executeSql("DROP TABLE " + fieldUp.tname() + ";");
                }
                //Удаление генератора приёмника
                if (listGenerator2.contains("GEN_" + fieldUp.tname()) == true) {
                    executeSql("DROP GENERATOR GEN_" + fieldUp.tname() + ";");
                }
                //Создание таблицы приёмника
                for (String ddl : Profstroy.createTable(fieldUp.fields())) {
                    executeSql(ddl);
                }
                //Добавление столбцов не вошедших в eEnum.values()
                for (Map.Entry<String, String[]> entry : hmDeltaCol.entrySet()) {
                    String deltaCol[] = entry.getValue();
                    executeSql("ALTER TABLE " + fieldUp.tname() + " ADD " + entry.getKey() + " " + Util.typeSql(Field.TYPE.type(deltaCol[0]), deltaCol[1]) + ";");
                }
                //Конвертирование данных в таблицу
                if (listExistTable1.contains(fieldUp.meta().fname) == true) {
                    convertTable(cn1, cn2, hmDeltaCol, fieldUp.fields());
                }
                //Создание генератора таблицы
                executeSql("CREATE GENERATOR GEN_" + fieldUp.tname());
                if ("id".equals(fieldUp.fields()[1].meta().fname)) { //если имена ключей совпадают
                    executeSql("UPDATE " + fieldUp.tname() + " SET id = gen_id(gen_" + fieldUp.tname() + ", 1)"); //заполнение ключей
                }
                executeSql("CREATE OR ALTER TRIGGER " + fieldUp.tname() + "_bi FOR " + fieldUp.tname() + " ACTIVE BEFORE INSERT POSITION 0 as begin"
                        + " if (new.id is null) then new.id = gen_id(gen_" + fieldUp.tname() + ", 1); end");
                executeSql("ALTER TABLE " + fieldUp.tname() + " ADD CONSTRAINT PK_" + fieldUp.tname() + " PRIMARY KEY (ID);"); //DDL создание первичного ключа
            }            

            System.out.println("\u001B[32m" + "Добавление комментариев к полям" + "\u001B[0m");
            for (Field field : fieldsUp) {
                executeSql("COMMENT ON TABLE " + field.tname() + " IS '" + field.meta().descr() + "'"); //DDL описание таблиц
            }
            ConnApp.initConnect().setConnection(cn2);
            deletePart(cn2, st2);
            updatePart(cn2, st2);
            metaPart(cn2, st2);

            System.out.println("\u001B[32m" + "Удаление лищних столбцов" + "\u001B[0m");
            for (Field fieldUp : fieldsUp) {
                HashMap<String, String[]> hmDeltaCol = deltaColumn(mdb1, fieldUp);
                for (Map.Entry<String, String[]> entry : hmDeltaCol.entrySet()) {
                    executeSql("ALTER TABLE " + fieldUp.tname() + " DROP  " + entry.getKey() + ";");
                }
            }
            System.out.println("\u001B[34m" + "ОБНОВЛЕНИЕ ЗАВЕРШЕНО" + "\u001B[0m");
        } catch (Exception e) {
            System.err.println("\u001B[31m" + "SQL-SCRIPT: " + e + "\u001B[0m");
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
            int count = 0; //количество записей для расчёта кол. пакетов
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
                            System.err.println("\u001B[31m" + "SCRIPT-INSERT:  " + e + "\u001B[0m");
                        }
                    }
                }
                bash = true;
                try {
                    //Пакетный insert
                    st2.executeBatch();
                    cn2.commit();
                    st2.clearBatch();

                } catch (SQLException e) {
                    cn2.rollback();
                    bash = false;
                    --index_page;
                    System.out.println("\u001B[31m" + "SCRIPT-BATCH:  " + e + "\u001B[0m");
                }
            }
        } catch (SQLException e) {
            System.err.println("\u001B[31m" + "CONVERT-TABLE:  " + e + "\u001B[0m");
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
            System.err.println("\u001B[31m" + "DELTA-COLUMN: " + e + "\u001B[0m");
            return null;
        }
    }

    private static void deletePart(Connection cn2, Statement st2) {
        try {
            System.out.println("\u001B[32m" + "Секция удаления потеренных ссылок (фантомов)" + "\u001B[0m");
            executeSql("delete from params where grup > 0");  //group > 0  
            deleteSql(eColor.up, "cgrup", eColgrp.up, "id");//colgrp_id
            deleteSql(eColpar1.up, "psss", eColor.up, "cnumb"); //color_id 
            deleteSql(eArtdet.up, "anumb", eArtikl.up, "code");//artikl_id
            //цвет не должен влиять глобально, теряются ссылки... ("delete from artdet where not exists (select id from color a where a.ccode = artdet.clcod and a.cnumb = artdet.clnum)");  //color_fk            
            deleteSql(eElement.up, "anumb", eArtikl.up, "code");//artikl_id  
            deleteSql(eElemdet.up, "anumb", eArtikl.up, "code");//artikl_id
            //цвет не должен влиять глобально на калькуляцию!!! executeSql("delete from elemdet where not exists (select id from color a where a.cnumb = elemdet.color_fk) and elemdet.color_fk > 0 and elemdet.color_fk != 100000"); //color_fk
            deleteSql(eElemdet.up, "vnumb", eElement.up, "vnumb");//element_id
            deleteSql(eElempar1.up, "psss", eElement.up, "vnumb");//element_id   
            deleteSql(eElempar2.up, "psss", eElemdet.up, "aunic");//elemdet_id
            deleteSql(eJoining.up, "anum1", eArtikl.up, "code");//artikl_id1
            deleteSql(eJoining.up, "anum2", eArtikl.up, "code");//artikl_id2
            deleteSql(eJoinvar.up, "cconn", eJoining.up, "cconn");//joining_id
            deleteSql(eJoindet.up, "cunic", eJoinvar.up, "cunic");//joinvar_id
            executeSql("delete from joindet where not exists (select id from color a where a.cnumb = joindet.color_fk) and joindet.color_fk > 0 and joindet.color_fk != 100000"); //color_fk  
            deleteSql(eJoinpar1.up, "psss", eJoinvar.up, "cunic");//joinvar_id
            deleteSql(eJoinpar2.up, "psss", eJoindet.up, "aunic");//joindet_id 
            deleteSql(eGlasprof.up, "gnumb", eGlasgrp.up, "gnumb");//glasgrp_id
            deleteSql(eGlasprof.up, "anumb", eArtikl.up, "code");//artikl_id
            deleteSql(eGlasdet.up, "gnumb", eGlasgrp.up, "gnumb");//glasgrp_id
            executeSql("delete from glasdet where not exists (select id from color a where a.cnumb = glasdet.color_fk) and glasdet.color_fk > 0 and glasdet.color_fk != 100000"); //color_fk
            deleteSql(eGlasdet.up, "anumb", eArtikl.up, "code");//artikl_id 
            deleteSql(eGlaspar1.up, "psss", eGlasgrp.up, "gnumb");//glasgrp_id
            deleteSql(eGlaspar2.up, "psss", eGlasdet.up, "gunic");//glasdet_id
            deleteSql(eFurnside1.up, "funic", eFurniture.up, "funic");//furniture_id
            deleteSql(eFurnside2.up, "fincs", eFurndet.up, "id");
            deleteSql(eFurnpar1.up, "psss", eFurnside1.up, "fincr");//furnside_id  
            deleteSql(eFurndet.up, "funic", eFurniture.up, "funic");//furniture_id          
            //теряется ссылка в furnside2 executeSql("delete from furndet where not exists (select id from artikl a where a.code = furndet.anumb and furndet.anumb != 'НАБОР')");  //artikl_id
            //теряется ссылка в furnside2 executeSql("delete from furndet where not exists (select id from color a where a.cnumb = furndet.color_fk) and furndet.color_fk > 0 and furndet.color_fk != 100000"); //color_fk           
            deleteSql(eFurnpar2.up, "psss", eFurndet.up, "id");//furndet_id
            deleteSql(eSysprof.up, "anumb", eArtikl.up, "code");//artikl_id 
            deleteSql(eSysprof.up, "nuni", eSystree.up, "nuni");//systree_id 
            deleteSql(eSysfurn.up, "funic", eFurniture.up, "funic");//furniture_id 
            deleteSql(eSysfurn.up, "nuni", eSystree.up, "nuni");//systree_id
            deleteSql(eSyspar1.up, "psss", eSystree.up, "nuni");//systree_id 
            deleteSql(eKits.up, "anumb", eArtikl.up, "code");//artikl_id
            deleteSql(eKitdet.up, "kunic", eKits.up, "kunic");//kits_id  
            deleteSql(eKitdet.up, "anumb", eArtikl.up, "code");//artikl_id
            deleteSql(eKitpar1.up, "psss", eKitdet.up, "kincr");//kitdet_id
        } catch (Exception e) {
            System.err.println("\u001B[31m" + "DELETE-PART:  " + e + "\u001B[0m");
        }
    }

    private static void updatePart(Connection cn2, Statement st2) {
        try {
            System.out.println("\u001B[32m" + "Секция коррекции внешних ключей" + "\u001B[0m");
            updateSetting();
            String max = new Query(eColgrp.id).select("select max(id) as id from " + eColgrp.up.tname()).get(0).getStr(eColgrp.id);
            executeSql("set generator gen_colgrp to " + max);
            updateSql(eColor.up, eColor.colgrp_id, "cgrup", eColgrp.up, "id");
            updateSql(eColpar1.up, eColpar1.color_id, "psss", eColor.up, "cnumb");
            updateSql(eArtdet.up, eArtdet.artikl_id, "anumb", eArtikl.up, "code");
            executeSql("update artdet set color_fk = (select id from color a where a.ccode = artdet.clcod and a.cnumb = artdet.clnum)");
            executeSql("update artdet set color_fk = artdet.clnum where artdet.clnum < 0");
            updateElemgrp();
            executeSql("update element set elemgrp_id = (select id from elemgrp a where a.name = element.vpref and a.level = element.atypm)");
            updateSql(eElement.up, eElement.artikl_id, "anumb", eArtikl.up, "code");
            executeSql(4, "update element set typset = vtype");
            executeSql(3, "update element set typset = case vtype when 'внутренний' then 1  when 'армирование' then 2 when 'ламинирование' then 3 when 'покраска' then 4 when 'состав_С/П' then 5 when 'кронштейн_стойки' then 6 when 'дополнительно' then 7 else null  end;");
            executeSql("update element set todef = 1  where vsets in (1,2)");
            executeSql("update element set toset = 1  where vsets = 1");
            updateSql(eElemdet.up, eElemdet.artikl_id, "anumb", eArtikl.up, "code");
            executeSql(4, "update artikl set analog_id = (select id from artikl a where a.code = artikl.amain)");
            executeSql(4, "update artikl set syssize_id = (select id from syssize a where a.sunic = artikl.sunic)");
            updateSql(eElemdet.up, eElemdet.element_id, "vnumb", eElement.up, "vnumb");
            executeSql("update elemdet set color_fk = (select id from color a where a.cnumb = elemdet.color_fk) where elemdet.color_fk > 0 and elemdet.color_fk != 100000");
            updateSql(eElempar1.up, eElempar1.element_id, "psss", eElement.up, "vnumb");
            updateSql(eElempar2.up, eElempar2.elemdet_id, "psss", eElemdet.up, "aunic");
            updateSql(eJoining.up, eJoining.artikl_id1, "anum1", eArtikl.up, "code");
            updateSql(eJoining.up, eJoining.artikl_id2, "anum2", eArtikl.up, "code");
            updateSql(eJoinvar.up, eJoinvar.joining_id, "cconn", eJoining.up, "cconn");
            updateSql(eJoindet.up, eJoindet.joinvar_id, "cunic", eJoinvar.up, "cunic");
            updateSql(eJoindet.up, eJoindet.artikl_id, "anumb", eArtikl.up, "code");
            executeSql("update joindet set color_fk = (select id from color a where a.cnumb = joindet.color_fk) where joindet.color_fk > 0 and joindet.color_fk != 100000");
            updateSql(eJoinpar1.up, eJoinpar1.joinvar_id, "psss", eJoinvar.up, "cunic");
            updateSql(eJoinpar2.up, eJoinpar2.joindet_id, "psss", eJoindet.up, "aunic");
            updateSql(eGlasprof.up, eGlasprof.glasgrp_id, "gnumb", eGlasgrp.up, "gnumb");
            updateSql(eGlasprof.up, eGlasprof.artikl_id, "anumb", eArtikl.up, "code");
            executeSql("update glasprof set toin = 1  where gtype in (1,3)");
            executeSql("update glasprof set toout = 1  where gtype in (2,3)");
            updateSql(eGlasdet.up, eGlasdet.glasgrp_id, "gnumb", eGlasgrp.up, "gnumb");
            updateSql(eGlasdet.up, eGlasdet.artikl_id, "anumb", eArtikl.up, "code");
            executeSql("update glasdet set color_fk = (select id from color a where a.cnumb = glasdet.color_fk) where glasdet.color_fk > 0 and glasdet.color_fk != 100000");
            updateSql(eGlaspar1.up, eGlaspar1.glasgrp_id, "psss", eGlasgrp.up, "gnumb");
            updateSql(eGlaspar2.up, eGlaspar2.glasdet_id, "psss", eGlasdet.up, "gunic");
            executeSql("update furniture set view_open = case fview when 'поворотная' then 1  when 'раздвижная' then 2 when 'раздвижная <=>' then 3 when 'раздвижная |^|' then 4  else null  end;");
            updateSql(eFurnside1.up, eFurnside1.furniture_id, "funic", eFurniture.up, "funic");
            executeSql("update furnside1 set side_use = ( CASE  WHEN (FTYPE = 'сторона') THEN 1 WHEN (FTYPE = 'ось поворота') THEN 2 WHEN (FTYPE = 'крепление петель') THEN 3 ELSE  (1) END )");
            updateSql(eFurnside2.up, eFurnside2.furndet_id, "fincs", eFurndet.up, "id");
            updateSql(eFurnpar1.up, eFurnpar1.furnside_id, "psss", eFurnside1.up, "fincr");
            updateSql(eFurndet.up, eFurndet.furniture_id, "funic", eFurniture.up, "funic");
            executeSql("update furndet set color_fk = (select id from color a where a.cnumb = furndet.color_fk) where furndet.color_fk > 0 and furndet.color_fk != 100000");
            executeSql("update furndet set artikl_id = (select id from artikl a where a.code = furndet.anumb and furndet.anumb != 'НАБОР')"); //TODO 'НАБОР'- конвертирование фурнитуры
            executeSql("update furndet set furndet_id = id where fleve = 1");
            updateSql(eFurnpar2.up, eFurnpar2.furndet_id, "psss", eFurndet.up, "id");
            executeSql("update systree set parent_id = (select id from systree a where a.nuni = systree.npar and systree.npar != 0)");
            executeSql("update systree set parent_id = id where npar = 0");
            updateSql(eSysprof.up, eSysprof.artikl_id, "anumb", eArtikl.up, "code");
            updateSql(eSysprof.up, eSysprof.systree_id, "nuni", eSystree.up, "nuni");           
            updateSql(eSysfurn.up, eSysfurn.furniture_id, "funic", eFurniture.up, "funic");
            updateSql(eSysfurn.up, eSysfurn.systree_id, "nuni", eSystree.up, "nuni");            
            executeSql("update sysfurn set side_open = ( CASE  WHEN (NOTKR = 'запрос') THEN 1 WHEN (NOTKR = 'левое') THEN 2 WHEN (NOTKR = 'правое') THEN 3 ELSE  (1) END )");
            executeSql("update sysfurn set hand_pos = ( CASE  WHEN (NRUCH = 'по середине') THEN 1 WHEN (NRUCH = 'константная') THEN 2 ELSE  (1) END )");            
            updateSql(eSyspar1.up, eSyspar1.systree_id, "psss", eSystree.up, "nuni");
            updateSysprod();
            updateSql(eKits.up, eKits.artikl_id, "anumb", eArtikl.up, "code");
            updateSql(eKits.up, eKits.color_id, "clnum", eColor.up, "cnumb");
            updateSql(eKitdet.up, eKitdet.kits_id, "kunic", eKits.up, "kunic");
            updateSql(eKitdet.up, eKitdet.artikl_id, "anumb", eArtikl.up, "code");
            updateSql(eKitdet.up, eKitdet.color1_id, "clnum", eColor.up, "cnumb");
            updateSql(eKitdet.up, eKitdet.color2_id, "clnu1", eColor.up, "cnumb");
            updateSql(eKitdet.up, eKitdet.color3_id, "clnu2", eColor.up, "cnumb");
            updateSql(eKitpar1.up, eKitpar1.kitdet_id, "psss", eKitdet.up, "kincr");
        } catch (Exception e) {
            System.err.println("\u001B[31m" + "UPDATE-PART:  " + e + "\u001B[0m");
        }
    }

    private static void metaPart(Connection cn2, Statement st2) {
       
        try {
            System.out.println("\u001B[32m" + "Секция создания внешних ключей" + "\u001B[0m");
            metaSql("alter table artikl add constraint fk_currenc foreign key (currenc_id) references currenc (id)");
            metaSql("alter table color add constraint fk_color1 foreign key (colgrp_id) references colgrp (id)");
            metaSql("alter table artdet add constraint fk_artdet1 foreign key (artikl_id) references artikl (id)");
            metaSql("alter table systree add constraint fk_systree1 foreign key (parent_id) references systree (id)");
            metaSql("alter table element add constraint fk_element1 foreign key (elemgrp_id) references elemgrp (id)");
            metaSql("alter table element add constraint fk_element2 foreign key (artikl_id) references artikl (id)");
            metaSql("alter table elemdet add constraint fk_elemdet1 foreign key (artikl_id) references artikl (id)");
            metaSql("alter table elemdet add constraint fk_elemdet2 foreign key (element_id) references element (id)");
            metaSql("alter table elempar1 add constraint fk_elempar1 foreign key (element_id) references element (id)");
            metaSql("alter table elempar2 add constraint fk_elempar2 foreign key (elemdet_id) references elemdet (id)");
            metaSql("alter table joining add constraint fk_joining1 foreign key (artikl_id1) references artikl (id)");
            metaSql("alter table joining add constraint fk_joining2 foreign key (artikl_id2) references artikl (id)");
            metaSql("alter table joinvar add constraint fk_joinvar1 foreign key (joining_id) references joining (id)");
            metaSql("alter table joindet add constraint fk_joindet1 foreign key (joinvar_id) references joinvar (id)");
            metaSql("alter table joinpar1 add constraint fk_joinpar1 foreign key (joinvar_id) references joinvar (id)");
            metaSql("alter table joinpar2 add constraint fk_joinpar2 foreign key (joindet_id) references joindet (id)");
            metaSql("alter table glasprof add constraint fk_glasprof1 foreign key (glasgrp_id) references glasgrp (id)");
            metaSql("alter table glasprof add constraint fk_glasprof2 foreign key (artikl_id) references artikl (id)");
            metaSql("alter table glasdet add constraint fk_glasdet1 foreign key (glasgrp_id) references glasgrp (id)");
            metaSql("alter table glasdet add constraint fk_glasdet2 foreign key (artikl_id) references artikl (id)");
            metaSql("alter table glaspar1 add constraint fk_glaspar1 foreign key (glasgrp_id) references glasgrp (id)");
            metaSql("alter table glaspar2 add constraint fk_glaspar2 foreign key (glasdet_id) references glasdet (id)");
            metaSql("alter table furnside1 add constraint fk_furnside1 foreign key (furniture_id) references furniture (id)");
            metaSql("alter table furnside2 add constraint fk_furnside2 foreign key (furndet_id) references furndet (id)");
            metaSql("alter table furnpar1 add constraint fk_furnpar1 foreign key (furnside_id) references furnside1 (id)");
            metaSql("alter table furndet add constraint fk_furndet1 foreign key (furniture_id) references furniture (id)");
            metaSql("alter table furndet add constraint fk_furndet2 foreign key (artikl_id) references artikl (id)");
            metaSql("alter table furnpar2 add constraint fk_furnpar2 foreign key (furndet_id) references furndet (id)");
            metaSql("alter table sysprof add constraint fk_sysprof1 foreign key (artikl_id) references artikl (id)");
            metaSql("alter table sysprof add constraint fk_sysprof2 foreign key (systree_id) references systree (id)");
            metaSql("alter table sysfurn add constraint fk_sysfurn1 foreign key (systree_id) references systree (id)");
            metaSql("alter table sysfurn add constraint fk_sysfurn2 foreign key (furniture_id) references furniture (id)");
            metaSql("alter table syspar1 add constraint fk_syspar1 foreign key (systree_id) references systree (id)");
            metaSql("alter table kits add constraint fk_kits1 foreign key (artikl_id) references artikl (id)");
            metaSql("alter table kits add constraint fk_kits2 foreign key (color_id) references color (id)");
            metaSql("alter table kitdet add constraint fk_kitdet1 foreign key (kits_id) references kits (id)");
            metaSql("alter table kitdet add constraint fk_kitdet2 foreign key (artikl_id) references artikl (id)");
            metaSql("alter table kitdet add constraint fk_kitdet3 foreign key (color1_id) references color (id)");
            metaSql("alter table kitdet add constraint fk_kitdet4 foreign key (color2_id) references color (id)");
            metaSql("alter table kitdet add constraint fk_kitdet5 foreign key (color3_id) references color (id)");
            metaSql("alter table kitpar1 add constraint fk_kitpar1 foreign key (kitdet_id) references kitdet (id)");
        } catch (Exception e) {
            System.err.println("\u001B[31m" + "ALTERDB-PART:  " + e + "\u001B[0m");
        }
    }

    private static void updateElemgrp() throws SQLException {
        try {
            System.out.println("updateElemgrp()");
            Query q = new Query(eElemgrp.values());
            ResultSet rs = st2.executeQuery("select distinct VPREF, ATYPM from element order by  ATYPM, VPREF");
            ArrayList<Object[]> fieldList = new ArrayList();
            while (rs.next()) {
                fieldList.add(new Object[]{rs.getString("VPREF"), rs.getInt("ATYPM")});
            }
            for (Object[] obj : fieldList) {
                Record record = q.newRecord(Query.INS);
                record.setNo(eElemgrp.id, ConnApp.instanc().genId(eElemgrp.up));
                record.setNo(eElemgrp.name, obj[0]);
                record.setNo(eElemgrp.level, obj[1]);
                q.insert(record);
                cn2.commit();
            }
        } catch (Exception e) {
            System.err.println("\u001B[31m" + "UPDATE-ELEMGRP:  " + e + "\u001B[0m");
        }
    }

    private static void updateSysprod() {
        try {
            System.out.println("updateSysprod()");
            Integer prj[] = {601001, 601002, 601003, 601004, 601005, 601006, 601007,
                601008, 601009, 601010, 604004, 604005, 604006, 604007, 604008, 604009, 604010};
            cn2.commit();
            for (int index = 0; index < prj.length; ++index) {

                String script = Winscript.test(prj[index], -1, -1, -1, -1);
                JsonElement jsonElem = new Gson().fromJson(script, JsonElement.class);
                JsonObject jsonObj = jsonElem.getAsJsonObject();
                String name = jsonObj.get("prj").getAsString()
                        + "-  " + jsonObj.get("name").getAsString();

                Query q = new Query(eSysprod.values());
                Record record = q.newRecord(Query.INS);
                record.setNo(eSysprod.npp, index + 1);
                record.setNo(eSysprod.id, ConnApp.instanc().genId(eSysprod.up));
                record.setNo(eSysprod.name, name);
                record.setNo(eSysprod.script, script);
                q.insert(record);
            }
            cn2.commit();

        } catch (Exception e) {
            System.err.println("\u001B[31m" + "UPDATE-SYSPROD:  " + e + "\u001B[0m");
        }
    }

    private static void updateSetting() {
        try {
            System.out.println("updateSetting()");
            Query q = new Query(eSetting.values());
            Record record = q.newRecord(Query.INS);
            record.setNo(eSetting.id, 1);
            record.setNo(eSetting.name, "Версия программы");
            record.setNo(eSetting.val, "1.0");
            q.insert(record);
            record = q.newRecord(Query.INS);
            record.setNo(eSetting.id, 2);
            record.setNo(eSetting.name, "Версия базы данных");
            record.setNo(eSetting.val, "ps" + versionPs);
            q.insert(record);
            cn2.commit();
        } catch (Exception e) {
            System.err.println("\u001B[31m" + "UPDATE-SETTING:  " + e + "\u001B[0m");
        }
    }

    private static void deleteSql(Field table1, String id1, Field table2, String id2) {
        try {
            int recordDelete = 0, recordCount = 0;
            Set set = new HashSet();
            ResultSet rs = st2.executeQuery("select " + id2 + " from " + table2.tname());
            while (rs.next()) {
                set.add(rs.getObject(id2));
            }
            rs = st2.executeQuery("select * from " + table1.tname());
            while (rs.next()) {
                ++recordCount;
                if (set.contains(rs.getObject(id1)) == false) {
                    ++recordDelete;
                    st2.addBatch("delete from " + table1.tname() + " where id = " + rs.getObject("id"));
                }
            }
            rs.close();
            String postpref = (recordDelete == 0) ? "" : "\u001B[34m Всего/удалено = " + recordCount + "/" + recordDelete + "\u001B[0m";
            System.out.println("delete from " + table1.tname() + " where not exists (select id from " + table2.tname()
                    + " a where a." + id2 + " = " + table1.tname() + "." + id1 + ")" + postpref);

            st2.executeBatch();
            cn2.commit();
            st2.clearBatch();

        } catch (Exception e) {
            System.err.println("\u001B[31m" + "DELETE-SQL:  " + e + "\u001B[0m");
        }
    }

    private static void updateSql(Field table1, Field fk1, String id1, Field table2, String id2) {
        try {
            int recordUpdate = 0, recordCount = 0;
            Set<Object[]> set = new HashSet();
            ResultSet rs = st2.executeQuery("select id, " + id2 + " from " + table2.tname());
            while (rs.next()) {
                Object[] arr = {rs.getObject("id"), rs.getObject(id2)};
                set.add(arr);
            }
            rs = st2.executeQuery("select * from " + table1.tname());
            while (rs.next()) {
                ++recordCount;
                Object val = rs.getObject(id1);
                Object[] obj = set.stream().filter(el -> el[1].equals(val)).findFirst().orElse(null);
                if (obj != null) {
                    ++recordUpdate;
                    st2.addBatch("update " + table1.tname() + " set " + fk1.name() + " = " + obj[0] + " where id = " + rs.getObject("id"));
                }
            }
            String postpref = (recordCount == recordUpdate) ? "" : "\u001B[34m Всего/неудач = " + recordCount + "/" + (recordCount - recordUpdate) + "\u001B[0m";
            System.out.println("update " + table1.tname() + " set " + fk1.name() + " = (select id from " + table2.tname()
                    + " a where a." + id2 + " = " + table1.tname() + "." + id1 + ")" + postpref);
            st2.executeBatch();
            cn2.commit();
            st2.clearBatch();

        } catch (Exception e) {
            System.err.println("\u001B[31m" + "UPDATE-SQL:  " + e + "\u001B[0m");
        }
    }

    private static void metaSql(String str) {
        try {
            System.out.println(str);
            st2.execute(str);
            cn2.commit();
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "НЕУДАЧА-SQL: Связь не установлена\u001B[0m");
        }
    }

    private static void executeSql(String str) {
        try {
            System.out.println(str);
            st2.execute(str);
            cn2.commit();
        } catch (Exception e) {
            System.err.println("\u001B[31m" + "SQL-DB:  " + e + "\u001B[0m");
        }
    }

    private static void executeSql(int versionPs, String str) {
        if (Profstroy.versionPs == versionPs) {
            executeSql(str);
        }
    }
}
