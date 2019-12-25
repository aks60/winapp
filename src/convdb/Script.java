package convdb;

import common.Util;
import dataset.ConnApp;
import dataset.Field;
import dataset.Query;
import dataset.Record;
import domain.eArtdet;
import domain.eArtikls;
import domain.eCompdet;
import domain.eComplet;
import domain.eDicConst;
import domain.eDicArtgrp;
import domain.eTextgrp;
import domain.eDicRate;
import domain.eDicSyspar;
import domain.eTexture;
import domain.eFurnside1;
import domain.eFurnside2;
import domain.eFurnpar1;
import domain.eFurndet;
import domain.eGlasdet;
import domain.eGlasgrp;
import domain.eGlasprof;
import domain.eJoinpar2;
import domain.eJoinpar1;
import domain.eFurnpar2;
import domain.eJoindet;
import domain.eJoinvar;
import domain.eJoining;
import domain.eTextpar1;
import domain.eGlaspar2;
import domain.eGlaspar1;
import domain.eDicParam;
import domain.eElemdet;
import domain.eElempar1;
import domain.eElempar2;
import domain.eElement;
import domain.eElemgrp;
import domain.eFurnitura;
import domain.eRulecalc;
import domain.eSysfurn;
import domain.eSyspar;
import domain.eSysprof;
import domain.eSystree;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    public static void script() {
        Field[] fieldsUp = {
            eDicConst.up, eDicSyspar.up, eDicRate.up, eDicArtgrp.up, eDicParam.up, eRulecalc.up,  
            eTexture.up, eTextgrp.up, eTextpar1.up,
            eArtikls.up, eArtdet.up, eComplet.up, eCompdet.up,
            eJoining.up, eJoindet.up, eJoinvar.up, eJoinpar2.up, eJoinpar1.up,
            eElemgrp.up, eElement.up, eElemdet.up, eElempar1.up, eElempar2.up,
            eGlasgrp.up, eGlasprof.up, eGlasdet.up, eGlaspar1.up, eGlaspar2.up,                                     
            eFurnitura.up, eFurnside1.up, eFurndet.up, eFurnside2.up, eFurnpar1.up, eFurnpar2.up,            
            eSystree.up, eSysprof.up, eSysfurn.up, eSyspar.up           
        };
        try {
            Connection cn1 = java.sql.DriverManager.getConnection( //источник
                    "jdbc:firebirdsql:localhost/3055:D:\\Okna\\Database\\Sialbase2\\base2.GDB?encoding=win1251", "sysdba", "masterkey");
            //"jdbc:firebirdsql:localhost/3050:D:\\Okna\\Database\\Profstroy4\\ITEST.FDB?encoding=win1251", "sysdba", "masterkey");
            Connection cn2 = java.sql.DriverManager.getConnection( //приёмник
                    "jdbc:firebirdsql:localhost/3050:C:\\Okna\\winbase\\BASE.FDB?encoding=win1251", "sysdba", "masterkey");

            Util.println("Подготовка методанных");
            Statement st1 = cn1.createStatement(); //мсточник 
            Statement st2 = cn2.createStatement();//приёмник
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
            Util.println("Перенос данных");
            //Цыкл по доменам приложения
            for (Field fieldUp : fieldsUp) {

                //Поля не вошедшие в eEnum.values()
                HashSet<String[]> hsDeltaCol = deltaColumn(mdb1, fieldUp);//в последствии будут использоваться для sql update

                if (listGenerator2.contains("GEN_" + fieldUp.tname()) == true) {
                    st2.execute("DROP GENERATOR GEN_" + fieldUp.tname() + ";"); //удаление генератора приёмника
                }
                if (listExistTable2.contains(fieldUp.tname()) == true) {
                    st2.execute(print("DROP TABLE " + fieldUp.tname() + ";")); //удаление таблицы приёмника
                }
                //Создание таблицы приёмника
                for (String ddl : Script.createTable(fieldUp.fields())) {
                    st2.execute(ddl);
                }
                //Добавление столбцов не вошедших в eEnum.values()
                for (Object[] deltaCol : hsDeltaCol) {
                    st2.execute(print("ALTER TABLE " + fieldUp.tname() + " ADD " + deltaCol[0] + " " + Util.typeSql(Field.TYPE.type(deltaCol[1]), deltaCol[2]) + ";"));
                }
                //Конвертирование данных в таблицу приёмника 
                if (listExistTable1.contains(fieldUp.meta().fname) == true) {
                    convertTable(cn1, cn2, fieldUp.fields(), hsDeltaCol);
                }

                st2.execute("CREATE GENERATOR GEN_" + fieldUp.tname()); //создание генератора приёмника
                if ("id".equals(fieldUp.fields()[1].meta().fname)) {
                    st2.execute("UPDATE " + fieldUp.tname() + " SET id = gen_id(gen_" + fieldUp.tname() + ", 1)"); //заполнение ключей
                }
                st2.execute(print("ALTER TABLE " + fieldUp.tname() + " ADD CONSTRAINT PK_" + fieldUp.tname() + " PRIMARY KEY (ID);")); //DDL создание первичного ключа
            }
            Util.println("Добавление комментариев к полям");
            for (Field field : fieldsUp) {
                st2.execute("COMMENT ON TABLE " + field.tname() + " IS '" + field.meta().descr + "'"); //DDL описание таблиц
            }
            Util.println("Заключительные действия, изменение структуры БД");
            if (fieldsUp.length > 1) {
                updateDb(cn2, st2);
            }
            Util.println("Удаление столбцов не вошедших в eEnum.values()");
//            for (Field fieldUp : fieldsUp) {
//                HashSet<String[]> hsDeltaCol = deltaColumn(mdb1, fieldUp);
//                for (Object[] deltaCol : hsDeltaCol) {
//                    st2.execute("ALTER TABLE " + fieldUp.tname() + " DROP  " + deltaCol[0] + ";");
//                }
//            }
            Util.println("Обновление завершено");

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
     * @param hsDeltaCol поля не вошедшие в eEnum.values()
     */
    public static void convertTable(Connection cn1, Connection cn2, Field[] fields, HashSet<String[]> hsDeltaCol) {
        String sql = "";
        try {
            int count = 0; //колчество записей для расчёта кол. пакетов
            String tname1 = fields[0].meta().fname;
//            if (tname1.equals("EMPTY")) { //новая таблица
//                return;
//            }
//            else if (tname1.equals("CONNLST") && versionPs == 3) { //баг с названием таблицы в PS-3
//                tname1 = "CONNECT";
//            }
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
                for (Object[] str : hsDeltaCol) {//поля для sql update (в конце будут удалены)
                    nameCols2 = nameCols2 + str[0] + ",";
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
                            nameVal2 = nameVal2 + "0" + ",";
                        }
                    }
                    //Цыкл по полям не вошедших в eEnum.values()
                    for (String[] str : hsDeltaCol) {
                        Object val = rs1.getObject(str[0]);
                        nameVal2 = nameVal2 + Util.wrapperSql(val, Field.TYPE.type(str[1])) + ",";
                    }
                    nameVal2 = nameVal2.substring(0, nameVal2.length() - 1);
                    sql = "insert into " + tname2 + "(" + nameCols2 + ") values (" + nameVal2.toString() + ")";
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

    private static HashSet<String[]> deltaColumn(DatabaseMetaData mdb1, Field fieldUp) {
        try {
            HashSet<String[]> hsDeltaCol = new HashSet(); //поля не вошедшие в eEnum.values(), в последствии будут использоваться для sql update
            ResultSet rsc1 = mdb1.getColumns(null, null, fieldUp.meta().fname, null);
            while (rsc1.next()) {
                String[] name = {rsc1.getString("COLUMN_NAME"), rsc1.getString("DATA_TYPE"), rsc1.getString("COLUMN_SIZE")};
                if ("-1".equals(rsc1.getString("DATA_TYPE")) || "-4".equals(rsc1.getString("DATA_TYPE"))) {
                    name[2] = "80";
                }
                boolean find = false;
                for (Field field : fieldUp.fields()) {
                    if (field.meta().fname.equalsIgnoreCase(name[0].toString())) {
                        find = true;
                    }
                }
                if (find == false) {
                    hsDeltaCol.add(name);
                }
            }
            return hsDeltaCol;
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "DELTA-COLUMN: " + e + "\u001B[0m");
            return null;
        }
    }

    private static void updateDb(Connection cn2, Statement st2) {
        try {
            ConnApp con = ConnApp.initConnect();
            con.setConnection(cn2);

            //Секция удаления фантомов
            st2.execute(print("delete from texture where not exists (select id from textgrp a where a.gnumb = texture.cgrup)")); //textgrp_id
            st2.execute(print("delete from artdet where not exists (select id from artikls a where a.code = artdet.anumb)"));//artikl_id
            st2.execute(print("delete from artdet where not exists (select id from texture a where a.ccode = artdet.clcod and a.cnumb = artdet.clnum)"));//texture_id
            st2.execute(print("delete from element where not exists (select id from artikls a where a.code = element.anumb)"));//artikl_id
            st2.execute(print("delete from elemdet where not exists (select id from artikls a where a.code = elemdet.anumb)"));//artikl_id
            st2.execute(print("delete from elemdet where not exists (select id from element a where a.vnumb = elemdet.vnumb)"));//element_id
            st2.execute(print("delete from elempar1 where not exists (select id from element a where a.vnumb = elempar1.psss)"));//element_id           
            st2.execute(print("delete from elempar2 where not exists (select id from elemdet a where a.aunic = elempar2.psss)"));//elemdet_id
            st2.execute(print("delete from joining where not exists (select id from artikls a where a.code = joining.anum1)"));//artikl_id1
            st2.execute(print("delete from joining where not exists (select id from artikls a where a.code = joining.anum2)"));//artikl_id2
            st2.execute(print("delete from joinvar where not exists (select id from joining a where a.cconn = joinvar.cconn)"));//joining_id
            st2.execute(print("delete from joindet where not exists (select id from joinvar a where a.cunic = joindet.cunic)"));//joinvar_id
            st2.execute(print("delete from joinpar1 where not exists (select id from joinvar a where a.cunic = joinpar1.psss)"));//joinvar_id
            st2.execute(print("delete from joinpar2 where not exists (select id from joindet a where a.aunic = joinpar2.psss)"));//joindet_id                      
            st2.execute(print("delete from glasprof where not exists (select id from glasgrp a where a.gnumb = glasprof.gnumb)"));
            st2.execute(print("delete from glasprof where not exists (select id from artikls a where a.code = glasprof.anumb)"));
            st2.execute(print("delete from glasdet where not exists (select id from glasgrp a where a.gnumb = glasdet.gnumb)"));
            st2.execute(print("delete from glasdet where not exists (select id from artikls a where a.code = glasdet.anumb)"));
            st2.execute(print("delete from glaspar1 where not exists (select id from glasgrp a where a.gnumb = glaspar1.psss)"));
            st2.execute(print("delete from glaspar2 where not exists (select id from glasdet a where a.gunic = glaspar2.psss)"));
            st2.execute(print("delete from furnside1 where not exists (select id from furnitura a where a.funic = furnside1.funic)"));
            st2.execute(print("delete from furnpar1 where not exists (select id from furnside1 a where a.fincr = furnpar1.psss)"));
            st2.execute(print("delete from furndet where not exists (select id from furnitura a where a.funic = furndet.funic)"));
            st2.execute(print("delete from furndet where not exists (select id from artikls a where a.code = furndet.anumb and furndet.anumb != 'НАБОР')"));
            st2.execute(print("delete from furnpar2 where not exists (select id from furndet a where a.fincb = furnpar2.psss)"));
          
            //Секция update
            st2.execute(print("update texture set textgrp_id = (select id from textgrp a where a.gnumb = texture.cgrup)"));
            Query.connection = cn2;
            Query q1 = new Query(eTextgrp.values()).select(eTextgrp.up).query(eTextgrp.up.tname());
            Query q2 = new Query(eTexture.values()).query(eTexture.up.tname());
            for (Record record : q1) {
                Record record2 = q2.newRecord(Query.INS);
                record2.setNo(eTexture.id, -1 * record.getInt(eTextgrp.id));
                record2.setNo(eTexture.textgrp_id, record.getInt(eTextgrp.id));
                record2.setNo(eTexture.name, "Все текстуры группы");
                record2.setNo(eTexture.name2, "Все текстуры группы");
                record2.setNo(eTexture.coef1, 1);
                record2.setNo(eTexture.coef2, 1);
                record2.setNo(eTexture.coef2, 1);
                record2.setNo(eTexture.suffix1, 1);
                record2.setNo(eTexture.suffix2, 1);
                record2.setNo(eTexture.suffix3, 1);
                q2.insert(record2);
            }
            st2.execute(print("update artdet set artikl_id = (select id from artikls a where a.code = artdet.anumb)"));
            st2.execute(print("update artdet set texture_id = (select id from texture a where a.ccode = artdet.clcod and a.cnumb = artdet.clnum)"));
            st2.execute(print("update artdet set texture_id = artdet.clnum where artdet.clnum < 0"));

            Query q3 = new Query(eElemgrp.values()).query(eElemgrp.up.tname());
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
            st2.execute(print("update element set elemgrp_id = (select id from elemgrp a where a.name = element.vpref and a.level = element.atypm)"));
            st2.execute(print("update element set artikl_id = (select id from artikls a where a.code = element.anumb)"));
            st2.execute(print("update elemdet set artikl_id = (select id from artikls a where a.code = elemdet.anumb)"));
            st2.execute(print("update elemdet set element_id = (select id from element a where a.vnumb = elemdet.vnumb)"));
            st2.execute(print("update elemdet set param_id = clnum where clnum < 0"));
            st2.execute(print("update elemdet set text_st = clnum where clnum > 0"));
            st2.execute(print("update elempar1 set element_id = (select id from element a where a.vnumb = elempar1.psss)"));
            st2.execute(print("update elempar2 set elemdet_id = (select id from elemdet a where a.aunic = elempar2.psss)"));
            st2.execute(print("update joining set artikl_id1 = (select id from artikls a where a.code = joining.anum1)"));
            st2.execute(print("update joining set artikl_id2 = (select id from artikls a where a.code = joining.anum2)")); // where exists  (select id from artikls a where a.code = joining.anum2)")); 
            st2.execute(print("update joinvar set joining_id = (select id from joining a where a.cconn = joinvar.cconn)"));
            st2.execute(print("update joindet set joinvar_id = (select id from joinvar a where a.cunic = joindet.cunic)"));
            st2.execute(print("update joinpar1 set joinvar_id = (select id from joinvar a where a.cunic = joinpar1.psss)"));
            st2.execute(print("update joinpar2 set joindet_id = (select id from joindet a where a.aunic = joinpar2.psss)"));
            st2.execute(print("update glasprof set glasgrp_id = (select id from glasgrp a where a.gnumb = glasprof.gnumb)"));
            st2.execute(print("update glasprof set artikl_id = (select id from artikls a where a.code = glasprof.anumb)"));
            st2.execute(print("update glasdet set glasgrp_id = (select id from glasgrp a where a.gnumb = glasdet.gnumb)"));
            st2.execute(print("update glasdet set artikl_id = (select id from artikls a where a.code = glasdet.anumb)"));
            st2.execute(print("update glaspar1 set glasgrp_id = (select id from glasgrp a where a.gnumb = glaspar1.psss)"));
            st2.execute(print("update glaspar2 set glasdet_id = (select id from glasdet a where a.gunic = glaspar2.psss)"));
            st2.execute(print("update furnside1 set furnitura_id = (select id from furnitura a where a.funic = furnside1.funic)"));
            st2.execute(print("update furnside1 SET type_side = ( CASE  WHEN (FTYPE = 'сторона') THEN 1 WHEN (FTYPE = 'ось поворота') THEN 2 WHEN (FTYPE = 'крепление петель') THEN 3 ELSE  (1) END )"));
            st2.execute(print("update furnpar1 set furnside_id = (select id from furnside1 a where a.fincr = furnpar1.psss)"));
            st2.execute(print("update furndet set furnitura_id = (select id from furnitura a where a.funic = furndet.funic)"));
            st2.execute(print("update furndet set artikl_id = (select id from artikls a where a.code = furndet.anumb and furndet.anumb != 'НАБОР')"));
            st2.execute(print("update furnpar2 set furndet_id = (select id from furndet a where a.fincb = furnpar2.psss)"));

        } catch (Exception e) {
            System.out.println("\u001B[31m" + "UPDATE-DB:  " + e + "\u001B[0m");
        }
    }

    private static String print(String str) {
        System.out.println(str);
        return str;
    }
}
