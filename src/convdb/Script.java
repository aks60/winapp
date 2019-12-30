package convdb;

import common.Util;
import dataset.ConnApp;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtdet;
import domain.eArtgrp;
import domain.eArtikls;
import domain.eColgrp;
import domain.eColor;
import domain.eColpar1;
import domain.eCompdet;
import domain.eComplet;
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
import domain.eKits;
import domain.eOrders;
import domain.eParams;
import domain.ePartner;
import domain.eRulecalc;
import domain.eSysconst;
import domain.eSysdata;
import domain.eSysfurn;
import domain.eSyspar1;
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

/**
 * В пс3 и пс4 разное количество полей в таблицах, но список столбцов в
 * прилшжении eEnum.values() для них один. Отсутствующие поля пс3 в
 * eEnum.values() будут заполняться пустышками. 3. Поля не вошедшие в список
 * столбцов eEnum.values() тоже будут переноситься для sql update и потом
 * удаляться. Обновление данных выполняется пакетом, если была ошибка в пакете,
 * откат и пакет обслуживается отдельными insert.
 */
public class Script {

    private static char versionPs = 4;
    private static Connection cn1;
    private static Connection cn2;
    private static Statement st1; //источник 
    private static Statement st2;//приёмник

    public static void script() {
        Field[] fieldsUp = {
            eSysconst.up, eSysdata.up, eCurrenc.up, eArtgrp.up, eParams.up, eRulecalc.up,
            eColor.up, eColgrp.up, eColpar1.up,
            eArtikls.up, eArtdet.up, eComplet.up, eCompdet.up,
            eJoining.up, eJoindet.up, eJoinvar.up, eJoinpar2.up, eJoinpar1.up,
            eElemgrp.up, eElement.up, eElemdet.up, eElempar1.up, eElempar2.up,
            eGlasgrp.up, eGlasprof.up, eGlasdet.up, eGlaspar1.up, eGlaspar2.up,
            eFurniture.up, eFurnside1.up, eFurndet.up, eFurnside2.up, eFurnpar1.up, eFurnpar2.up,
            eSysprof.up, eSystree.up, eSysfurn.up, eSyspar1.up,
            eKits.up, eKitdet.up,
            ePartner.up, eOrders.up
        };
        try {
            cn1 = java.sql.DriverManager.getConnection( //источник
                    "jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251", "sysdba", "masterkey");
            //"jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\ITEST.FDB?encoding=win1251", "sysdba", "masterkey");
            cn2 = java.sql.DriverManager.getConnection( //приёмник
                    "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");

            Util.println("\u001B[32m" + "Подготовка методанных" + "\u001B[0m");
            st1 = cn1.createStatement(); //источник 
            st2 = cn2.createStatement();//приёмник
            DatabaseMetaData mdb1 = cn1.getMetaData();
            DatabaseMetaData mdb2 = cn2.getMetaData();
            ResultSet resultSet1 = mdb1.getTables(null, null, null, new String[]{"TABLE"});
            ResultSet resultSet2 = mdb2.getTables(null, null, null, new String[]{"TABLE"});

            List<String> listExistTable1 = new ArrayList<String>();//таблицы приёмника
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
            Util.println("\u001B[32m" + "Перенос данных" + "\u001B[0m");
            //Цыкл по доменам приложения
            for (Field fieldUp : fieldsUp) {

                //Поля не вошедшие в eEnum.values()
                HashMap<String, String[]> hmDeltaCol = deltaColumn(mdb1, fieldUp);//в последствии будут использоваться для sql update

                if (listGenerator2.contains("GEN_" + fieldUp.tname()) == true) {
                    sql("DROP GENERATOR GEN_" + fieldUp.tname() + ";"); //удаление генератора приёмника
                }
                if (listExistTable2.contains(fieldUp.tname()) == true) {
                    sql("DROP TABLE " + fieldUp.tname() + ";"); //удаление таблицы приёмника
                }
                //Создание таблицы приёмника
                for (String ddl : Script.createTable(fieldUp.fields())) {
                    st2.execute(ddl);
                }
                //Добавление столбцов не вошедших в eEnum.values()
                for (Map.Entry<String, String[]> entry : hmDeltaCol.entrySet()) {
                    String deltaCol[] = entry.getValue();
                    sql("ALTER TABLE " + fieldUp.tname() + " ADD " + entry.getKey() + " " + Util.typeSql(Field.TYPE.type(deltaCol[0]), deltaCol[1]) + ";");
                }
                //Конвертирование данных в таблицу
                if (listExistTable1.contains(fieldUp.meta().fname) == true) {
                    convertTable(cn1, cn2, fieldUp.fields(), hmDeltaCol);
                }
                //Создание генератора таблицы
                sql("CREATE GENERATOR GEN_" + fieldUp.tname());
                if ("id".equals(fieldUp.fields()[1].meta().fname)) {
                    sql("UPDATE " + fieldUp.tname() + " SET id = gen_id(gen_" + fieldUp.tname() + ", 1)"); //заполнение ключей
                }
                sql("ALTER TABLE " + fieldUp.tname() + " ADD CONSTRAINT PK_" + fieldUp.tname() + " PRIMARY KEY (ID);"); //DDL создание первичного ключа
            }
            Util.println("\u001B[32m" + "Добавление комментариев к полям" + "\u001B[0m");
            for (Field field : fieldsUp) {
                sql("COMMENT ON TABLE " + field.tname() + " IS '" + field.meta().descr + "'"); //DDL описание таблиц
            }
            updateDb(cn2, st2);

            Util.println("\u001B[32m" + "Удаление столбцов не вошедших в eEnum.values()" + "\u001B[0m");
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
            batch.add("COMMENT ON COLUMN \"" + f[i].tname() + "\"." + f[i].name() + " IS '" + f[i].meta().descr + "';");
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
    public static void convertTable(Connection cn1, Connection cn2, Field[] fields, HashMap<String, String[]> hmDeltaCol) {
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

                Util.println("Таблица:" + tname2 + " пакет:" + index_page);
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
                            if (field.meta().type() == Field.TYPE.BOOL || field.meta().type() == Field.TYPE.DBL 
                                    || field.meta().type() == Field.TYPE.FLT || field.meta().type() == Field.TYPE.INT 
                                    || field.meta().type() == Field.TYPE.LONG) {
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
                    //System.out.println(sql);
                    if (bash == true) {
                        st2.addBatch(sql);
                    } else {
                        try {
                            //Если была ошибка в пакете выполняю отдельные sql insert
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
                String key = rsc1.getString("COLUMN_NAME");
                String[] val = {rsc1.getString("DATA_TYPE"), rsc1.getString("COLUMN_SIZE")};
                if ("-1".equals(rsc1.getString("DATA_TYPE")) || "-4".equals(rsc1.getString("DATA_TYPE"))) {
                    val[1] = "80";
                }
                boolean find = false;
                for (Field field : fieldUp.fields()) {
                    if (field.meta().fname.equalsIgnoreCase(key)) {
                        find = true;
                    }
                }
                if (find == false) {
                    hmDeltaCol.put(key, val);
                }
            }
            return hmDeltaCol;
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "DELTA-COLUMN: " + e + "\u001B[0m");
            return null;
        }
    }

    private static void updateDb(Connection cn2, Statement st2) {
        try {
            ConnApp con = ConnApp.initConnect();
            con.setConnection(cn2);
            Util.println("\u001B[32m" + "Секция удаления потеренных ссылок (фантомов)" + "\u001B[0m");
            sql("delete from color where not exists (select id from colgrp a where a.gnumb = color.cgrup)");
            sql("delete from artdet where not exists (select id from artikls a where a.code = artdet.anumb)");
            sql("delete from artdet where not exists (select id from color a where a.ccode = artdet.clcod and a.cnumb = artdet.clnum)");
            sql("delete from element where not exists (select id from artikls a where a.code = element.anumb)");
            sql("delete from elemdet where not exists (select id from artikls a where a.code = elemdet.anumb)");
            sql("delete from elemdet where not exists (select id from element a where a.vnumb = elemdet.vnumb)");
            sql("delete from elempar1 where not exists (select id from element a where a.vnumb = elempar1.psss)");
            sql("delete from elempar2 where not exists (select id from elemdet a where a.aunic = elempar2.psss)");
            sql("delete from joining where not exists (select id from artikls a where a.code = joining.anum1)");
            sql("delete from joining where not exists (select id from artikls a where a.code = joining.anum2)");
            sql("delete from joinvar where not exists (select id from joining a where a.cconn = joinvar.cconn)");
            sql("delete from joindet where not exists (select id from joinvar a where a.cunic = joindet.cunic)");
            sql("delete from joinpar1 where not exists (select id from joinvar a where a.cunic = joinpar1.psss)");
            sql("delete from joinpar2 where not exists (select id from joindet a where a.aunic = joinpar2.psss)");
            sql("delete from glasprof where not exists (select id from glasgrp a where a.gnumb = glasprof.gnumb)");
            sql("delete from glasprof where not exists (select id from artikls a where a.code = glasprof.anumb)");
            sql("delete from glasdet where not exists (select id from glasgrp a where a.gnumb = glasdet.gnumb)");
            sql("delete from glasdet where not exists (select id from artikls a where a.code = glasdet.anumb)");
            sql("delete from glaspar1 where not exists (select id from glasgrp a where a.gnumb = glaspar1.psss)");
            sql("delete from glaspar2 where not exists (select id from glasdet a where a.gunic = glaspar2.psss)");
            sql("delete from furnside1 where not exists (select id from furniture a where a.funic = furnside1.funic)");
            sql("delete from furnpar1 where not exists (select id from furnside1 a where a.fincr = furnpar1.psss)");
            sql("delete from furndet where not exists (select id from furniture a where a.funic = furndet.funic)");
            sql("delete from furndet where not exists (select id from artikls a where a.code = furndet.anumb and furndet.anumb != 'НАБОР')");
            sql("delete from furnpar2 where not exists (select id from furndet a where a.fincb = furnpar2.psss)");

            sql("delete from sysprof where not exists (select id from artikls a where a.code = sysprof.anumb)");
            sql("delete from sysprof where not exists (select id from systree a where a.nuni = sysprof.nuni)");
            sql("delete from sysfurn where not exists (select id from furniture a where a.funic = sysfurn.funic)");
            sql("delete from sysfurn where not exists (select id from systree a where a.nuni = sysfurn.nuni)");
            sql("delete from syspar1 where not exists (select id from systree a where a.nuni = syspar1.psss)");
            sql("delete from kits where not exists (select id from artikls a where a.code = kits.anumb)");
            //sql("delete from kits set where not exists (select id from color a where a.ccode = kits.clnum)");
            sql("delete from kitdet where not exists (select id from kits a where a.kunic = kitdet.kunic)");
            sql("delete from kitdet where not exists (select id from artikls a where a.code = kitdet.anumb)");

            Util.println("\u001B[32m" + "Секция коррекции внешних ключей" + "\u001B[0m");
            sql("update color set colgrp_id = (select id from colgrp a where a.gnumb = color.cgrup)");
            Query.connection = cn2;
            Query q1 = new Query(eColgrp.values()).select(eColgrp.up).table(eColgrp.up.tname());
            Query q2 = new Query(eColor.values()).table(eColor.up.tname());
            for (Record record : q1) {
                Record record2 = q2.newRecord(Query.INS);
                record2.setNo(eColor.id, -1 * record.getInt(eColgrp.id));
                record2.setNo(eColor.colgrp_id, record.getInt(eColgrp.id));
                record2.setNo(eColor.name, "Все текстуры группы");
                record2.setNo(eColor.name2, "Все текстуры группы");
                record2.setNo(eColor.coef1, 1);
                record2.setNo(eColor.coef2, 1);
                record2.setNo(eColor.coef2, 1);
                record2.setNo(eColor.suffix1, 1);
                record2.setNo(eColor.suffix2, 1);
                record2.setNo(eColor.suffix3, 1);
                q2.insert(record2);
            }
            sql("update artdet set artikl_id = (select id from artikls a where a.code = artdet.anumb)");
            sql("update artdet set color_id = (select id from color a where a.ccode = artdet.clcod and a.cnumb = artdet.clnum)");
            sql("update artdet set color_id = artdet.clnum where artdet.clnum < 0");

            Query q3 = new Query(eElemgrp.values()).table(eElemgrp.up.tname());
            ResultSet rs3 = st2.executeQuery("select distinct VPREF, ATYPM from element order by  ATYPM, VPREF");
            ArrayList<Object[]> fieldList = new ArrayList();
            while (rs3.next()) {
                fieldList.add(new Object[]{rs3.getString("VPREF"), rs3.getInt("ATYPM")});
            }
            for (Object[] obj : fieldList) {
                Record record = q3.newRecord(Query.INS);
                record.setNo(eElemgrp.id, ConnApp.get().generstorId(eElemgrp.up.tname()));
                record.setNo(eElemgrp.name, obj[0]);
                record.setNo(eElemgrp.level, obj[1]);
                q3.insert(record);
            }
            sql("update element set elemgrp_id = (select id from elemgrp a where a.name = element.vpref and a.level = element.atypm)");
            sql("update element set artikl_id = (select id from artikls a where a.code = element.anumb)");
            sql("update elemdet set artikl_id = (select id from artikls a where a.code = elemdet.anumb)");
            sql("update elemdet set element_id = (select id from element a where a.vnumb = elemdet.vnumb)");
            sql("update elemdet set param_id = clnum where clnum < 0");
            sql("update elemdet set color_st = clnum where clnum > 0");
            sql("update elempar1 set element_id = (select id from element a where a.vnumb = elempar1.psss)");
            sql("update elempar2 set elemdet_id = (select id from elemdet a where a.aunic = elempar2.psss)");
            sql("update joining set artikl_id1 = (select id from artikls a where a.code = joining.anum1)");
            sql("update joining set artikl_id2 = (select id from artikls a where a.code = joining.anum2)"); // where exists  (select id from artikls a where a.code = joining.anum2)")); 
            sql("update joinvar set joining_id = (select id from joining a where a.cconn = joinvar.cconn)");
            sql("update joindet set joinvar_id = (select id from joinvar a where a.cunic = joindet.cunic)");
            sql("update joinpar1 set joinvar_id = (select id from joinvar a where a.cunic = joinpar1.psss)");
            sql("update joinpar2 set joindet_id = (select id from joindet a where a.aunic = joinpar2.psss)");
            sql("update glasprof set glasgrp_id = (select id from glasgrp a where a.gnumb = glasprof.gnumb)");
            sql("update glasprof set artikl_id = (select id from artikls a where a.code = glasprof.anumb)");
            sql("update glasdet set glasgrp_id = (select id from glasgrp a where a.gnumb = glasdet.gnumb)");
            sql("update glasdet set artikl_id = (select id from artikls a where a.code = glasdet.anumb)");
            sql("update glaspar1 set glasgrp_id = (select id from glasgrp a where a.gnumb = glaspar1.psss)");
            sql("update glaspar2 set glasdet_id = (select id from glasdet a where a.gunic = glaspar2.psss)");
            sql("update furnside1 set furniture_id = (select id from furniture a where a.funic = furnside1.funic)");
            sql("update furnside1 SET type_side = ( CASE  WHEN (FTYPE = 'сторона') THEN 1 WHEN (FTYPE = 'ось поворота') THEN 2 WHEN (FTYPE = 'крепление петель') THEN 3 ELSE  (1) END )");
            sql("update furnpar1 set furnside_id = (select id from furnside1 a where a.fincr = furnpar1.psss)");
            sql("update furndet set furniture_id = (select id from furniture a where a.funic = furndet.funic)");
            sql("update furndet set artikl_id = (select id from artikls a where a.code = furndet.anumb and furndet.anumb != 'НАБОР')");
            sql("update furnpar2 set furndet_id = (select id from furndet a where a.fincb = furnpar2.psss)");
            sql("update systree set parent_id = (select id from systree a where a.nuni = systree.npar and systree.npar != 0)");
            sql("update systree set parent_id = id where npar = 0");

            sql("update sysprof set artikl_id = (select id from artikls a where a.code = sysprof.anumb)");
            sql("update sysprof set systree_id = (select id from systree a where a.nuni = sysprof.nuni)");
            sql("update sysfurn set furniture_id = (select id from furniture a where a.funic = sysfurn.funic)");
            sql("update sysfurn set systree_id = (select id from systree a where a.nuni = sysfurn.nuni)");
            sql("update syspar1 set systree_id = (select id from systree a where a.nuni = syspar1.psss)");
            sql("update kits set artikl_id = (select id from artikls a where a.code = kits.anumb)");
            //sql("update kits set color_id = (select id from color a where a.ccode = kits.clnum)");
            sql("update kitdet set kits_id = (select id from kits a where a.kunic = kitdet.kunic)");
            sql("update kitdet set artikl_id = (select id from artikls a where a.code = kitdet.anumb)");

        } catch (Exception e) {
            System.out.println("\u001B[31m" + "UPDATE-DB:  " + e + "\u001B[0m");
        }
    }

    private static void sql(String str) {
        try {
            Util.println(str);
            st2.execute(str);
        } catch (Exception e) {
            System.out.println("\u001B[31m" + "SQL-DB:  " + e + "\u001B[0m");
        }
    }
}
