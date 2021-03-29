package convert;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.eProperty;
import frames.Util;
import dataset.Conn;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eColor;
import domain.eColmap;
import domain.eElemdet;
import domain.eElement;
import domain.eJoining;
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
import domain.eGroups;
import domain.eJoindet;
import domain.eJoinpar1;
import domain.eJoinpar2;
import domain.eJoinvar;
import domain.eKitdet;
import domain.eKitpar1;
import domain.eKits;
import domain.ePrjpart;
import domain.eRulecalc;
import domain.eSetting;
import domain.eSysfurn;
import domain.eSyspar1;
import domain.eSysprof;
import domain.eSystree;
import enums.TypeElem;
import enums.TypeGroups;
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
import builder.script.Winscript;
import domain.eProject;
import domain.eSysmodel;
import java.awt.Color;
import java.util.Arrays;
import java.util.Queue;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import startup.App;
import startup.Main;

/**
 * В пс3 и пс4 разное количество полей в таблицах, но список столбцов в
 * программе eEnum.values() для них один. Отсутствующие поля пс3 в
 * eEnum.values() будут заполняться пустышками. 3. Поля не вошедшие в список
 * столбцов eEnum.values() тоже будут переноситься для sql update и потом
 * удаляться. Обновление данных выполняется пакетом, если была ошибка в пакете,
 * откат и пакет обслуживается отдельными insert.
 */
public class Profstroy {

    private static Queue<Object[]> que = null;
    private static int count = 0;
    private static String versionPs = "4";
    private static int numDb = Integer.valueOf(eProperty.base_num.read());
    private static Connection cn1;
    private static Connection cn2;
    private static Statement st1; //источник 
    private static Statement st2;//приёмник
    private static String src, out;
    private static JTextPane tp = null;

    public static void exec() {
        cn1 = startup.Test.connect(numDb)[0]; //источник
        cn2 = startup.Test.connect(numDb)[1]; //приёмник
        if (JOptionPane.showConfirmDialog(null, "КОНВЕРТАЦИЯ БАЗЫ ДАННЫХ", "КОНВЕРТАЦИЯ",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
            script();
        }
    }

    public static void exec(Queue<Object[]> _que, Connection _cn1, Connection _cn2) {
        que = _que;
        cn1 = _cn1;
        cn2 = _cn2;
        script();
    }

    public static void script() {
        try {
            println(Color.GREEN, "Подготовка методанных");
            cn2.setAutoCommit(false);
            Query.connection = cn2;
            st1 = cn1.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY); //источник 
            st2 = cn2.createStatement();//приёмник
            DatabaseMetaData mdb1 = cn1.getMetaData();
            DatabaseMetaData mdb2 = cn2.getMetaData();
            ResultSet resultSet1 = mdb1.getTables(null, null, null, new String[]{"TABLE"});
            ResultSet resultSet2 = mdb2.getTables(null, null, null, new String[]{"TABLE"});

            List<String> listExistTable1 = new ArrayList<String>(); //таблицы источника
            List<String> listExistTable2 = new ArrayList<String>(); //таблицы приёмника
            List<String> listGenerator2 = new ArrayList<String>();  //генераторы приёмника 
            while (resultSet1.next()) {
                listExistTable1.add(resultSet1.getString("TABLE_NAME"));
                if ("CONNECT".equals(resultSet1.getString("TABLE_NAME"))) {
                    versionPs = "3";
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
            println(Color.BLACK, "Методанные готовы");
            println(Color.GREEN, "Перенос данных");
            //Цикл по доменам приложения
            for (Field fieldUp : App.db) {
                println(Color.GREEN, " *** Секция " + fieldUp.tname() + " ***");

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
                for (String ddl : createTable(fieldUp.fields())) {
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
                //Создание генератора
                executeSql("CREATE GENERATOR GEN_" + fieldUp.tname());

                //Особенности таблицы PARAMS
                if ("PARAMS".equals(fieldUp.tname()) == true) {
                    executeSql("SET GENERATOR  GEN_" + fieldUp.tname() + " TO " + -10000);
                }
                //Заполнение таблицы ключами
                if ("id".equals(fieldUp.fields()[1].meta().fname)) { //если имена ключей совпадают
                    executeSql("UPDATE " + fieldUp.tname() + " SET id = gen_id(gen_" + fieldUp.tname() + ", 1)"); //заполнение ключей
                }
                //Особенности таблицы COLOR
                if ("COLOR".equals(fieldUp.tname()) == true) {
                    int max1 = new Query(fieldUp.fields()[1]).select("select max(id) as id from " + fieldUp.tname()).get(0).getInt(1);
                    int max2 = checkKeyColor(max1); //проверим ключ на уникальность
                    executeSql("set generator GEN_" + fieldUp.tname() + " to " + max2);
                }
                //Создание триггеров генераторов
                executeSql("CREATE OR ALTER TRIGGER " + fieldUp.tname() + "_bi FOR " + fieldUp.tname() + " ACTIVE BEFORE INSERT POSITION 0 as begin"
                        + " if (new.id is null) then new.id = gen_id(gen_" + fieldUp.tname() + ", 1); end");
                //DDL создание первичного ключа
                executeSql("ALTER TABLE " + fieldUp.tname() + " ADD CONSTRAINT PK_" + fieldUp.tname() + " PRIMARY KEY (ID);");
            }

            println(Color.GREEN, "Добавление комментариев к полям");
            for (Field field : App.db) {
                executeSql("COMMENT ON TABLE " + field.tname() + " IS '" + field.meta().descr() + "'"); //DDL описание таблиц
            }

            println(Color.GREEN, "Секция создания ролей");
            Set<String> set = new HashSet();
            ResultSet rs = st2.executeQuery("select a.rdb$role_name from rdb$roles a where a.rdb$role_name != 'RDB$ADMIN'");
            while (rs.next()) {
                set.add(rs.getString("rdb$role_name"));
            }
            set.forEach(role -> executeSql("DROP ROLE " + role));
            Arrays.asList("DEFROLE", "MANAGER_RO", "MANAGER_RW", "TEXNOLOG_RO", "TEXNOLOG_RW").forEach(role -> executeSql("CREATE ROLE " + role));
            for (Field field : App.db) {
                executeSql("GRANT SELECT ON " + field.tname() + " TO MANAGER_RO");
                executeSql("GRANT ALL ON " + field.tname() + " TO MANAGER_RW");
                executeSql("GRANT SELECT ON " + field.tname() + " TO TEXNOLOG_RO");
                executeSql("GRANT ALL ON " + field.tname() + " TO TEXNOLOG_RW");
            }
            if (Main.dev == true) {
                executeSql("GRANT TEXNOLOG_RW, DEFROLE TO TEXNOLOG");
                executeSql("GRANT MANAGER_RW, DEFROLE TO MANAGER");
            }
            Conn.initConnect().setConnection(cn2);
            deletePart(cn2, st2);
            updatePart(cn2, st2);
            metaPart(cn2, st2);

            println(Color.GREEN, "Удаление лищних столбцов");
            executeSql("ALTER TABLE GROUPS DROP  FK;");
            for (Field fieldUp : App.db) {
                HashMap<String, String[]> hmDeltaCol = deltaColumn(mdb1, fieldUp);
                for (Map.Entry<String, String[]> entry : hmDeltaCol.entrySet()) {
                    executeSql("ALTER TABLE " + fieldUp.tname() + " DROP  " + entry.getKey() + ";");
                }
            }
            cn2.commit();
            cn2.setAutoCommit(true);

            println(Color.BLUE, "ОБНОВЛЕНИЕ ЗАВЕРШЕНО");
        } catch (Exception e) {
            println(Color.RED, "Ошибка: script() " + e);
        }
    }

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
            //Цикл по пакетам
            for (int index_page = 0; index_page <= count / 500; ++index_page) {

                println(Color.BLACK, "Таблица:" + tname2 + " пакет:" + index_page);
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
                    //Цикл по полям eEnum.values()
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
                    //Цикл по полям не вошедших в eEnum.values()
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
                            println(Color.RED, "SCRIPT-INSERT:  " + e);
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
                    println(Color.BLACK, "SCRIPT-BATCH:  " + e);
                }
            }
        } catch (SQLException e) {
            println(Color.RED, "Ошибка: convertTable().  " + e);
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
            println(Color.RED, "Ошибка: deltaColumn(). " + e);
            return null;
        }
    }

    //Секция удаления потеренных ссылок (фантомов)
    private static void deletePart(Connection cn2, Statement st2) {
        try {
            println(Color.GREEN, "Секция удаления потеренных ссылок (фантомов)");
            executeSql("delete from params where pnumb > 0");  //group > 0  
            deleteSql(eColmap.up, "psss", eColor.up, "cnumb"); //color_id1 
            deleteSql(eArtdet.up, "anumb", eArtikl.up, "code");//artikl_id
            //цвет не должен влиять глобально, теряются ссылки... ("delete from artdet where not exists (select id from color a where a.ccode = artdet.clcod and a.cnumb = artdet.clnum)");  //color_fk            
            deleteSql(eElement.up, "anumb", eArtikl.up, "code");//artikl_id  
            deleteSql(eElemdet.up, "anumb", eArtikl.up, "code");//artikl_id
            //цвет не должен влиять глобально на калькуляцию!!! executeSql("delete from elemdet where not exists (select id from color a where a.cnumb = elemdet.color_fk) and elemdet.color_fk > 0 and elemdet.color_fk != 100000"); //color_fk
            deleteSql(eElemdet.up, "vnumb", eElement.up, "vnumb");//element_id
            deleteSql(eElempar1.up, "psss", eElement.up, "vnumb");//element_id   
            deleteSql(eElempar2.up, "psss", eElemdet.up, "aunic");//elemdet_id
            //Включить в продакшине!!!  deleteSql(eJoining.up, "anum1", eArtikl.up, "code");//artikl_id1
            //Включить в продакшине!!!  deleteSql(eJoining.up, "anum2", eArtikl.up, "code");//artikl_id2
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
            //исключение незаконченной транзакции deleteSql(eFurndet.up, "anumb", eArtikl.up, "code");//artikl_id
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
            println(Color.RED, "Ошибка: deletePart().  " + e);
        }
    }

    //Секция коррекции внешних ключей
    private static void updatePart(Connection cn2, Statement st2) {
        try {
            println(Color.GREEN, "Секция коррекции внешних ключей");
            loadSetting("Функция loadSetting()");
            loadGroups("Функция loadGroups()");
            executeSql("insert into groups (grup, name) select distinct " + TypeGroups.SERI_PROF.id + ", aseri from artikl");
            updateSql(eRulecalc.up, eRulecalc.artikl_id, "anumb", eArtikl.up, "code");
            executeSql("update rulecalc set type = rulecalc.type * -1 where rulecalc.type < 0");
            executeSql("update color set rgb = bin_or(bin_shl(bin_and(rgb, 0xff), 16), bin_and(rgb, 0xff00), bin_shr(bin_and(rgb, 0xff0000), 16))");
            executeSql("update colmap a set a.color_id2 = (select first 1 id from color b where a.ptext = b.name), joint = '1', elem = '1', glas = '1', furn = '1', otkos = '1', komp = '1'");
            updateSql(eColmap.up, eColmap.color_id1, "psss", eColor.up, "cnumb");
            updateSql(eArtikl.up, eArtikl.series_id, "aseri", eGroups.up, "name");
            updateSql(eArtdet.up, eArtdet.artikl_id, "anumb", eArtikl.up, "code");
            executeSql("update params a set a.params_id = (select b.id from params b where a.pnumb = b.pnumb and b.znumb = 0)");
            executeSql("update artikl set artgrp1_id = (select a.id from groups a where munic = a.fk and a.grup = " + TypeGroups.PRICE_INC.numb() + ")");
            executeSql("update artikl set artgrp2_id = (select a.id from groups a where udesc = a.fk and a.grup = " + TypeGroups.PRICE_DEC.numb() + ")");
            executeSql("update artikl set artgrp3_id = (select a.id from groups a where apref = a.name and a.grup = " + TypeGroups.CATEG_PRF.numb() + ")");
            executeSql("update color set colgrp_id = (select a.id from groups a where cgrup = a.fk and a.grup = " + TypeGroups.COLOR.numb() + ")");
            executeSql("update colmap set colgrp_id = (select a.id from groups a where colgrp_id = a.fk and a.grup = " + TypeGroups.COLMAP.numb() + ")");
            executeSql("update artdet set color_fk = (select first 1 id from color a where a.id = artdet.clcod or a.cnumb = artdet.clnum) where artdet.clnum >= 0");
            executeSql("update artdet set color_fk = (select -1 * id from groups a where a.fk = (-1 * artdet.clnum) and a.grup = 2) where artdet.clnum < 0");
            executeSql("3", "update artdet set mark_c1 = 1, mark_c2 = 1, mark_c3 = 1"); // where clnum >= 0");
            executeSql("4", "update artdet set mark_c1 = 1 where cways in (4,5,6,7)");
            executeSql("4", "update artdet set mark_c2 = 1 where cways in (1,3,5,7)");
            executeSql("4", "update artdet set mark_c3 = 1 where cways in (2,3,6,7)");
            updateSql(eElement.up, eElement.artikl_id, "anumb", eArtikl.up, "code");
            updateSql(eElement.up, eElement.series_id, "vlets", eGroups.up, "name");
            executeSql("update element set elemgrp_id = (select id from groups a where grup = 8 and a.name = element.vpref and a.npp = element.atypm)");
            executeSql("4", "update element set typset = vtype");
            executeSql("3", "update element set typset = case vtype when 'внутренний' then 1  when 'армирование' then 2 when 'ламинирование' then 3 when 'покраска' then 4 when 'состав_С/П' then 5 when 'кронштейн_стойки' then 6 when 'дополнительно' then 7 else null  end;");
            executeSql("update element set todef = 1  where vsets in (1,2)");
            executeSql("update element set toset = 1  where vsets = 1");
            updateSql(eElemdet.up, eElemdet.artikl_id, "anumb", eArtikl.up, "code");
            executeSql("4", "update artikl set analog_id = (select id from artikl a where a.code = artikl.amain)");
            executeSql("4", "update artikl set syssize_id = (select id from syssize a where a.sunic = artikl.sunic)");
            executeSql("4", "update artikl set size_falz = (select a.falz from syssize a where a.id = artikl.syssize_id) where size_falz is null or size_falz = 0");
            updateSql(eElemdet.up, eElemdet.element_id, "vnumb", eElement.up, "vnumb");
            executeSql("update elemdet set color_fk = (select id from color a where a.cnumb = elemdet.color_fk) where elemdet.color_fk > 0 and elemdet.color_fk != 100000");
            executeSql("update elemdet set color_fk = (select -1*id from groups a where a.fk = elemdet.color_fk) where elemdet.color_fk < 0");
            executeSql("3", "update elemdet set types = (CASE  WHEN (types = 11) THEN 3003 WHEN (types = 21) THEN 4095 "
                    + "WHEN (types = 31) THEN 273 WHEN (types = 32) THEN 546 WHEN (types = 33) THEN 819 WHEN (types = 41) THEN 1638 WHEN (types = 42) THEN 1911 WHEN (types = 43) THEN 2184 ELSE  (0) END )");
            updateSql(eElempar1.up, eElempar1.element_id, "psss", eElement.up, "vnumb");
            updateSql(eElempar2.up, eElempar2.elemdet_id, "psss", eElemdet.up, "aunic");
            executeSql("update elempar1 b set b.params_id = (select id from params a where b.params_id = a.pnumb and a.znumb = 0) where b.params_id < 0");
            executeSql("update elempar2 b set b.params_id = (select id from params a where b.params_id = a.pnumb and a.znumb = 0) where b.params_id < 0");
            updateSql(eJoining.up, eJoining.artikl_id1, "anum1", eArtikl.up, "code");
            updateSql(eJoining.up, eJoining.artikl_id2, "anum2", eArtikl.up, "code");
            updateSql(eJoinvar.up, eJoinvar.joining_id, "cconn", eJoining.up, "cconn");
            updateSql(eJoindet.up, eJoindet.joinvar_id, "cunic", eJoinvar.up, "cunic");
            updateSql(eJoindet.up, eJoindet.artikl_id, "anumb", eArtikl.up, "code");
            executeSql("update joinvar set types = types * 10 + cnext");
            executeSql("update joindet set color_fk = (select id from color a where a.cnumb = joindet.color_fk) where joindet.color_fk > 0 and joindet.color_fk != 100000");
            executeSql("update joindet set color_fk = (select -1*id from groups a where a.fk = joindet.color_fk) where joindet.color_fk < 0");
            executeSql("3", "update joindet set types = (CASE  WHEN (types = 11) THEN 3003 WHEN (types = 21) THEN 4095 "
                    + "WHEN (types = 31) THEN 273 WHEN (types = 32) THEN 546 WHEN (types = 33) THEN 819 WHEN (types = 41) THEN 1638 WHEN (types = 42) THEN 1911 WHEN (types = 43) THEN 2184  ELSE  (0) END )");
            updateSql(eJoinpar1.up, eJoinpar1.joinvar_id, "psss", eJoinvar.up, "cunic");
            updateSql(eJoinpar2.up, eJoinpar2.joindet_id, "psss", eJoindet.up, "aunic");
            executeSql("update joinpar1 b set b.params_id = (select id from params a where b.params_id = a.pnumb and a.znumb = 0) where b.params_id < 0");
            executeSql("update joinpar2 b set b.params_id = (select id from params a where b.params_id = a.pnumb and a.znumb = 0) where b.params_id < 0");
            updateSql(eGlasprof.up, eGlasprof.glasgrp_id, "gnumb", eGlasgrp.up, "gnumb");
            updateSql(eGlasprof.up, eGlasprof.artikl_id, "anumb", eArtikl.up, "code");
            executeSql("update glasprof set inside = 1  where gtype in (1,3,7)");
            executeSql("update glasprof set outside = 1  where gtype in (2,3,7)");
            updateSql(eGlasdet.up, eGlasdet.glasgrp_id, "gnumb", eGlasgrp.up, "gnumb");
            updateSql(eGlasdet.up, eGlasdet.artikl_id, "anumb", eArtikl.up, "code");
            executeSql("update glasdet set color_fk = (select id from color a where a.cnumb = glasdet.color_fk) where glasdet.color_fk > 0 and glasdet.color_fk != 100000");
            executeSql("update glasdet set color_fk = (select -1*id from groups a where a.fk = glasdet.color_fk) where glasdet.color_fk < 0");
            executeSql("3", "update glasdet set types = (CASE  WHEN (types = 11) THEN 3003 WHEN (types = 21) THEN 4095 "
                    + "WHEN (types = 31) THEN 273 WHEN (types = 32) THEN 546 WHEN (types = 33) THEN 819 WHEN (types = 41) THEN 1638 WHEN (types = 42) THEN 1911 WHEN (types = 43) THEN 2184  ELSE  (0) END )");
            updateSql(eGlaspar1.up, eGlaspar1.glasgrp_id, "psss", eGlasgrp.up, "gnumb");
            updateSql(eGlaspar2.up, eGlaspar2.glasdet_id, "psss", eGlasdet.up, "gunic");
            executeSql("update glaspar1 b set b.params_id = (select id from params a where b.params_id = a.pnumb and a.znumb = 0) where b.params_id < 0");
            executeSql("update glaspar2 b set b.params_id = (select id from params a where b.params_id = a.pnumb and a.znumb = 0) where b.params_id < 0");
            executeSql("4", "update furniture set hand_set1 = cast(bin_and(thand, 1) as varchar(5)), hand_set2 = cast(bin_shr(bin_and(thand, 2), 1) as varchar(5)), hand_set3 = cast(bin_shr(bin_and(thand, 4), 2) as varchar(5))");
            executeSql("update furniture set view_open = case fview when 'поворотная' then 1  when 'раздвижная' then 2 when 'раздвижная <=>' then 3 when 'раздвижная |^|' then 4  else null  end;");
            updateSql(eFurnside1.up, eFurnside1.furniture_id, "funic", eFurniture.up, "funic");
            executeSql("update furnside1 set side_use = ( CASE  WHEN (FTYPE = 'сторона') THEN 1 WHEN (FTYPE = 'ось поворота') THEN 2 WHEN (FTYPE = 'крепление петель') THEN 3 ELSE  (1) END )");
            updateSql(eFurnside2.up, eFurnside2.furndet_id, "fincs", eFurndet.up, "id");
            updateSql(eFurnpar1.up, eFurnpar1.furnside_id, "psss", eFurnside1.up, "fincr");
            updateSql(eFurnpar2.up, eFurnpar2.furndet_id, "psss", eFurndet.up, "id");
            executeSql("update furnpar1 b set b.params_id = (select id from params a where b.params_id = a.pnumb and a.znumb = 0) where b.params_id < 0");
            executeSql("update furnpar2 b set b.params_id = (select id from params a where b.params_id = a.pnumb and a.znumb = 0) where b.params_id < 0");
            updateSql(eFurndet.up, eFurndet.furniture_id1, "funic", eFurniture.up, "funic");
            executeSql("3", "update furndet set color_fk = (select id from color a where a.cnumb = furndet.color_fk) where furndet.color_fk > 0 and furndet.color_fk != 100000 and furndet.anumb != 'КОМПЛЕКТ'");
            executeSql("4", "update furndet set color_fk = (select id from color a where a.cnumb = furndet.color_fk) where furndet.color_fk > 0 and furndet.color_fk != 100000 and furndet.anumb != 'НАБОР'");
            executeSql("update furndet set color_fk = (select (-1 * id) from groups a where a.fk = furndet.color_fk) where furndet.color_fk < 0");
            executeSql("3", "update furndet set types = (CASE  WHEN (types = 11) THEN 3003 WHEN (types = 21) THEN 4095 "
                    + "WHEN (types = 31) THEN 273 WHEN (types = 32) THEN 546 WHEN (types = 33) THEN 819 WHEN (types = 41) THEN 1638 WHEN (types = 42) THEN 1911 WHEN (types = 43) THEN 2184  ELSE  (0) END )");
            executeSql("3", "update furndet set artikl_id = (select id from artikl a where a.code = furndet.anumb and furndet.anumb != 'КОМПЛЕКТ')");
            executeSql("4", "update furndet set artikl_id = (select id from artikl a where a.code = furndet.anumb and furndet.anumb != 'НАБОР')");
            executeSql("3", "update furndet set furniture_id2 = (CASE  WHEN (furndet.anumb = 'КОМПЛЕКТ') THEN color_fk ELSE  (null) END)");
            executeSql("4", "update furndet set furniture_id2 = (CASE  WHEN (furndet.anumb = 'НАБОР') THEN color_fk ELSE  (null) END)");
            updateSql(eFurndet.up, eFurndet.furniture_id2, "furniture_id2", eFurniture.up, "funic");
            executeSql("update furndet set furndet_id = id where furndet_id = 0");
            executeSql("update furndet set color_fk = null where furniture_id2 > 0"); //ссылка на набор
            executeSql("set generator GEN_" + eFurndet.up.tname() + " to " + new Query(eFurndet.id).select("select max(id) as id from " + eFurndet.up.tname()).get(0, eFurndet.id));
            executeSql("update systree set parent_id = (select id from systree a where a.nuni = systree.npar and systree.npar != 0)");
            executeSql("update systree set parent_id = id where npar = 0");
            updateSql(eSysprof.up, eSysprof.artikl_id, "anumb", eArtikl.up, "code");
            updateSql(eSysprof.up, eSysprof.systree_id, "nuni", eSystree.up, "nuni");
            updateSql(eSysfurn.up, eSysfurn.furniture_id, "funic", eFurniture.up, "funic");
            updateSql(eSysfurn.up, eSysfurn.systree_id, "nuni", eSystree.up, "nuni");
            updateSql(eSysfurn.up, eSysfurn.artikl_id1, "aruch", eArtikl.up, "code");
            updateSql(eSysfurn.up, eSysfurn.artikl_id2, "apetl", eArtikl.up, "code");
            executeSql("update sysfurn set side_open = (CASE  WHEN (NOTKR = 'запрос') THEN 1 WHEN (NOTKR = 'левое') THEN 2 WHEN (NOTKR = 'правое') THEN 3 ELSE  (1) END )");
            executeSql("update sysfurn set hand_pos = (CASE  WHEN (NRUCH = 'по середине') THEN 1 WHEN (NRUCH = 'константная') THEN 2 ELSE  (1) END )");
            updateSql(eSyspar1.up, eSyspar1.systree_id, "psss", eSystree.up, "nuni");
            executeSql("update syspar1 b set b.params_id = (select id from params a where b.params_id = a.pnumb and a.znumb = 0) where b.params_id < 0");
            loadModels();
            updateSql(eKits.up, eKits.artikl_id, "anumb", eArtikl.up, "code");
            updateSql(eKits.up, eKits.color_id, "clnum", eColor.up, "cnumb");
            updateSql(eKitdet.up, eKitdet.kits_id, "kunic", eKits.up, "kunic");
            updateSql(eKitdet.up, eKitdet.artikl_id, "anumb", eArtikl.up, "code");
            updateSql(eKitdet.up, eKitdet.color1_id, "clnum", eColor.up, "cnumb");
            updateSql(eKitdet.up, eKitdet.color2_id, "clnu1", eColor.up, "cnumb");
            updateSql(eKitdet.up, eKitdet.color3_id, "clnu2", eColor.up, "cnumb");
            updateSql(eKitpar1.up, eKitpar1.kitdet_id, "psss", eKitdet.up, "kincr");
            updateSql(eProject.up, eProject.prjpart_id, "kname", ePrjpart.up, "partner");

            executeSql("update prjpart set org_leve2 = trim(org_leve2)");
        } catch (Exception e) {
            println(Color.RED, "Ошибка: updatePart().  " + e);
        }
    }

    //Секция ссылочной целостности
    private static void metaPart(Connection cn2, Statement st2) {
        try {
            println(Color.GREEN, "Секция создания внешних ключей");
            alterTable("artikl", "fk_currenc1", "currenc1_id", "currenc");
            alterTable("artikl", "fk_currenc2", "currenc2_id", "currenc");
            alterTable("color", "fk_color1", "colgrp_id", "groups");
            alterTable("alter table color add constraint ung1_color unique (name)");
            alterTable("colmap", "fk_colmap_1", "colgrp_id", "groups");
            alterTable("colmap", "fk_colmap_2", "color_id1", "color");
            alterTable("colmap", "fk_colmap_3", "color_id2", "color");
            alterTable("artikl", "fk_artikl1", "artgrp1_id", "groups");
            alterTable("artikl", "fk_artikl2", "artgrp2_id", " groups");
            alterTable("artikl", "fk_artikl3", "artgrp3_id", "groups");
            alterTable("artikl", "fk_artikl4", "series_id", "groups");
            alterTable("artikl", "fk_artikl5", "syssize_id", "syssize");
            alterTable("artdet", "fk_artdet1", "artikl_id", "artikl");
            alterTable("systree", "fk_systree1", "parent_id", "systree");
            alterTable("element", "fk_element1", "elemgrp_id", "groups");
            alterTable("element", "fk_element2", "artikl_id", "artikl");
            alterTable("elemdet", "fk_elemdet1", "artikl_id", "artikl");
            alterTable("elemdet", "fk_elemdet2", "element_id", "element");
            alterTable("elempar1", "fk_elempar1", "element_id", "element");
            alterTable("elempar2", "fk_elempar2", "elemdet_id", "elemdet");
            alterTable("joining", "fk_joining1", "artikl_id1", "artikl");
            alterTable("joining", "fk_joining2", "artikl_id2", "artikl");
            alterTable("joinvar", "fk_joinvar1", "joining_id", "joining");
            alterTable("joindet", "fk_joindet1", "joinvar_id", "joinvar");
            alterTable("joinpar1", "fk_joinpar1", "joinvar_id", "joinvar");
            alterTable("joinpar2", "fk_joinpar2", "joindet_id", "joindet");
            alterTable("glasprof", "fk_glasprof1", "glasgrp_id", "glasgrp");
            alterTable("glasprof", "fk_glasprof2", "artikl_id", "artikl");
            alterTable("glasdet", "fk_glasdet1", "glasgrp_id", "glasgrp");
            alterTable("glasdet", "fk_glasdet2", "artikl_id", "artikl");
            alterTable("glaspar1", "fk_glaspar1", "glasgrp_id", "glasgrp");
            alterTable("glaspar2", "fk_glaspar2", "glasdet_id", "glasdet");
            alterTable("furnside1", "fk_furnside1", "furniture_id", "furniture");
            alterTable("furnside2", "fk_furnside2", "furndet_id", "furndet");
            alterTable("furnpar1", "fk_furnpar1", "furnside_id", "furnside1");
            alterTable("furndet", "fk_furndet1", "furniture_id1", "furniture");
            alterTable("furndet", "fk_furndet2", "artikl_id", "artikl");
            alterTable("furnpar2", "fk_furnpar2", "furndet_id", "furndet");
            alterTable("sysprof", "fk_sysprof1", "artikl_id", "artikl");
            alterTable("sysprof", "fk_sysprof2", "systree_id", "systree");
            alterTable("sysfurn", "fk_sysfurn1", "systree_id", "systree");
            alterTable("sysfurn", "fk_sysfurn2", "furniture_id", "furniture");
            alterTable("syspar1", "fk_syspar1", "systree_id", "systree");
            alterTable("sysprod", "fk_sysprod_2", "systree_id", "systree");
            alterTable("project", "fk_project_1", "prjpart_id", "prjpart");
            alterTable("kits", "fk_kits1", "artikl_id", "artikl");
            alterTable("kits", "fk_kits2", "color_id", "color");
            alterTable("kitdet", "fk_kitdet1", "kits_id", "kits");
            alterTable("kitdet", "fk_kitdet2", "artikl_id", "artikl");
            alterTable("kitdet", "fk_kitdet3", "color1_id", "color");
            alterTable("kitdet", "fk_kitdet4", "color2_id", "color");
            alterTable("kitdet", "fk_kitdet5", "color3_id", "color");
            alterTable("kitpar1", "fk_kitpar1", "kitdet_id", "kitdet");

        } catch (Exception e) {
            println(Color.RED, "Ошибка: metaPart().  " + e);
        }
    }

    private static void loadModels() {
        try {
            println(Color.BLACK, "loadModels()");

            List<Integer> prjList = (numDb == 1) ? Arrays.asList(601001, 601002, 601003, 601004, 601005, 601006, 601007, 601008, 601009, 601010, 604004, 604005, 604006, 604007, 604008, 604009, 604010)
                    : Arrays.asList(601001, 601002, 601003);

            String script;
            cn2.commit();
            int index = 0;
            for (int prj : prjList) {

                if ("ps3".equals(eSetting.find(2).getStr(eSetting.val))) {
                    script = Winscript.testPs3(prj, true);
                } else {
                    script = Winscript.testPs4(prj, true);
                }
                if (script != null) {
                    JsonElement jsonElem = new Gson().fromJson(script, JsonElement.class);
                    JsonObject jsonObj = jsonElem.getAsJsonObject();
                    String name = "<html>" + jsonObj.get("prj").getAsString() + " " + jsonObj.get("name").getAsString();
                    int form = (jsonObj.get("prj").getAsInt() < 601999) ? TypeElem.RECTANGL.id : TypeElem.ARCH.id;
                    Query q = new Query(eSysmodel.values());
                    Record record = eSysmodel.up.newRecord(Query.INS);
                    record.setNo(eSysmodel.npp, ++index);
                    record.setNo(eSysmodel.id, Conn.instanc().genId(eSysmodel.up));
                    record.setNo(eSysmodel.name, name);
                    record.setNo(eSysmodel.script, script);
                    record.setNo(eSysmodel.form, form);
                    q.insert(record);
                }
            }
            cn2.commit();

        } catch (Exception e) {
            println(Color.RED, "Ошибка: modifyModels.  " + e);
        }
    }

    private static void loadSetting(String mes) {
        println(Color.BLACK, mes);
        try {
            println(Color.BLACK, "updateSetting()");
            Query q = new Query(eSetting.values());
            Record record = eSetting.up.newRecord(Query.INS);
            record.setNo(eSetting.id, 1);
            record.setNo(eSetting.name, "Версия программы");
            record.setNo(eSetting.val, "1.0");
            q.insert(record);
            record = eSetting.up.newRecord(Query.INS);
            record.setNo(eSetting.id, 2);
            record.setNo(eSetting.name, "Версия базы данных");
            record.setNo(eSetting.val, "ps" + versionPs);
            q.insert(record);
            cn2.commit();
        } catch (Exception e) {
            println(Color.RED, "Ошибка: modifySetting().  " + e);
        }
    }

    private static void loadGroups(String mes) {
        println(Color.BLACK, mes);
        try {
            ResultSet rs = st1.executeQuery("select * from SYSDATA where SUNIC in (2002, 2003, 2004, 2005, 2007, 2009, 2010, 2013, 2055, 2056, 2057, 2058, 2062, 2101, 2104)");
            while (rs.next()) {
                String sql = "insert into " + eGroups.up.tname() + "(ID, GRUP, NAME, VAL) values ("
                        + rs.getInt("SUNIC") + "," + TypeGroups.SYS_DATA.id + ",'" + rs.getString("SNAME") + "'," + rs.getString("SFLOT") + ")";
                st2.executeUpdate(sql);
            }
            executeSql("ALTER TABLE GROUPS ADD FK INTEGER;");
            executeSql("SET GENERATOR  GEN_GROUPS TO " + 10000);

            rs = st1.executeQuery("select * from GRUPCOL");
            while (rs.next()) {
                String sql = "insert into " + eGroups.up.tname() + "(ID, GRUP, NAME, VAL, FK) values ("
                        + Conn.instanc().genId(eGroups.up) + "," + TypeGroups.COLOR.id + ",'" + rs.getString("GNAME") + "',"
                        + rs.getString("GKOEF") + "," + rs.getInt("GUNIC") + ")";
                st2.executeUpdate(sql);
            }
            rs = st1.executeQuery("select * from GRUPART");
            while (rs.next()) {
                String sql = "insert into " + eGroups.up.tname() + "(ID, GRUP, NAME, VAL, FK) values ("
                        + Conn.instanc().genId(eGroups.up) + "," + TypeGroups.PRICE_INC.id + ",'" + rs.getString("MNAME") + "',"
                        + rs.getString("MKOEF") + "," + rs.getInt("MUNIC") + ")";
                st2.executeUpdate(sql);
            }
            rs = st1.executeQuery("select * from DESCLST");
            while (rs.next()) {
                String sql = "insert into " + eGroups.up.tname() + "(ID, GRUP, NAME, VAL, FK) values ("
                        + Conn.instanc().genId(eGroups.up) + "," + TypeGroups.PRICE_DEC.id + ",'" + rs.getString("NDESC") + "',"
                        + rs.getString("VDESC") + "," + rs.getInt("UDESC") + ")";
                st2.executeUpdate(sql);
            }
            rs = st1.executeQuery("select distinct APREF from ARTIKLS where APREF is not null");
            while (rs.next()) {
                String sql = "insert into " + eGroups.up.tname() + "(ID, GRUP, NAME) values ("
                        + Conn.instanc().genId(eGroups.up) + "," + TypeGroups.CATEG_PRF.id + ",'" + rs.getString("APREF") + "')";
                st2.executeUpdate(sql);
            }
            rs = st1.executeQuery("select * from PARLIST where PCOLL = 1 and ZNUMB = 0");
            while (rs.next()) {
                String sql = "insert into " + eGroups.up.tname() + "(ID, GRUP, NAME, FK) values ("
                        + Conn.instanc().genId(eGroups.up) + "," + TypeGroups.COLMAP.id + ",'" + rs.getString("PNAME") + "'," + rs.getInt("PNUMB") + ")";
                st2.executeUpdate(sql);
            }
            rs = st1.executeQuery("select distinct VPREF, ATYPM from VSTALST order by  ATYPM, VPREF");
            while (rs.next()) {
                String sql = "insert into " + eGroups.up.tname() + "(ID, GRUP, NAME, NPP) values ("
                        + Conn.instanc().genId(eGroups.up) + "," + TypeGroups.CATEG_VST.id + ",'" + rs.getString("VPREF") + "'," + rs.getInt("ATYPM") + ")";
                st2.executeUpdate(sql);
            }
            cn2.commit();

        } catch (SQLException e) {
            println(Color.RED, "Ошибка: modifyGroups().  " + e);
        }
    }

    private static int checkKeyColor(int max) {
        List<Integer[]> recordList = new ArrayList();
        Set<Integer> set = new HashSet();
        try {
            ResultSet rs = st2.executeQuery("select * from COLOR");
            while (rs.next()) {
                recordList.add(new Integer[]{rs.getInt("ID"), rs.getInt("CNUMB")});
            }
        } catch (SQLException e) {
            println(Color.RED, "Ошибка: UPDATE-checkUniqueKeyColor().  " + e);
        }
        for (Integer[] recordArr : recordList) {
            if (set.add(recordArr[0]) == false) {
                executeSql("update COLOR set id = " + (++max) + " where cnumb = " + recordArr[1]);
            }
        }
        return max;
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
            String postpref = (recordDelete == 0) ? "" : " Всего/удалено = " + recordCount + "/" + recordDelete;
            println(Color.BLACK, "delete from " + table1.tname() + " where not exists (select id from " + table2.tname()
                    + " a where a." + id2 + " = " + table1.tname() + "." + id1 + ")", Color.BLUE, postpref);
            st2.executeBatch();
            cn2.commit();
            st2.clearBatch();

        } catch (Exception e) {
            println(Color.RED, "Ошибка: deleteSql().  " + e);
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
            String postpref = (recordCount == recordUpdate) ? "" : " Всего/неудач = " + recordCount + "/" + (recordCount - recordUpdate);
            println(Color.BLACK, "update " + table1.tname() + " set " + fk1.name() + " = (select id from "
                    + table2.tname() + " a where a." + id2 + " = " + table1.tname() + "." + id1 + ")", Color.BLUE, postpref);
            st2.executeBatch();
            cn2.commit();
            st2.clearBatch();

        } catch (Exception e) {
            println(Color.RED, "Ошибка: updateSql().  " + e);
        }
    }

    private static void alterTable(String tname1, String cn, String fk, String tname2) {
        String str = "alter table " + tname1 + " add constraint " + cn + " foreign key (" + fk + ") references " + tname2 + " (id)";
        println(Color.BLACK, str);
        try {
            st2.execute(str);
            cn2.commit();
        } catch (SQLException e) {
            println(Color.BLUE, "НЕУДАЧА-SQL: executeSql(). " + str);
        }
    }

    private static void alterTable(String str) {
        try {
            println(Color.BLACK, str);
            st2.execute(str);
            cn2.commit();
        } catch (SQLException e) {
            println(Color.BLUE, "НЕУДАЧА-SQL: checkUniqueKeyColor(). Связь не установлена");
        }
    }

    private static void executeSql(String... s) {
        if (s.length == 2 && versionPs.equals(s[0]) == false) {
            return;
        }
        String str = (s.length == 2) ? s[1] : s[0];
        try {
            println(Color.BLACK, str);
            st2.execute(str);
            cn2.commit();
        } catch (Exception e) {
            println(Color.RED, "Ошибка:  executeSql(). " + e);
        }
    }

    private static void println(Object... obj) {
        if (obj.length == 0 && obj.length == 1) {
            return;
        }
        if (obj.length == 2) {
            if (que != null) {
                que.add(new Object[]{obj[0], obj[1]});
            } else {
                System.out.println(Util.consoleColor(obj[0]) + obj[1].toString() + "\u001B[0m");
            }
        } else {
            if (que != null) {
                que.add(new Object[]{obj[0], obj[1], obj[2], obj[3]});
            } else {
                System.out.println(Util.consoleColor(obj[0]) + obj[1].toString() + Util.consoleColor(obj[2]) + obj[3].toString() + "\u001B[0m");
            }
        }
    }
}
