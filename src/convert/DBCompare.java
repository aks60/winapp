package convert;

import builder.Wincalc;
import builder.specif.Specification;
import common.eProperty;
import domain.eSetting;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static jdk.nashorn.internal.objects.Global.println;
import jxl.Sheet;
import jxl.Workbook;
import startup.Test;

public class DBCompare {

    public static int numDb = Integer.valueOf(eProperty.base_num.read());

    enum Fld { //артикул, color1, color2, color3, ширина, уг1, уг2, длина, кол, погона, норм.отх, себест, без.скидк, со.скидк
        ANUMB, CLNUM, CLNU1, CLNU2, ARADI, AUG01, AUG02, ALENG, AQTYP, AQTYA, APERC, ASEB1, APRC1, APRCD
    }

    //сравнение спецификации с профстроем
    public static void iwinXls(ArrayList<Specification> spcList, int prj, boolean detail) {

        //TODO нужна синхронизация функции
        System.out.println();
        System.out.println("Prj=" + prj);
        Float iwinTotal = 0f, jarTotal = 0f;
        String path = "src\\resource\\xls\\ps4\\p" + prj + ".xls";
        if ("ps3".equals(eSetting.find(2).getStr(eSetting.val)) == true) {
            path = "src\\resource\\xls\\ps3\\p" + prj + ".xls";
        }
        //Specification.sort(spcList);
        Map<String, Float> hmXls = new LinkedHashMap();
        Map<String, Float> hmJar = new LinkedHashMap();
        Map<String, String> hmArt = new LinkedHashMap();
        for (Specification spc : spcList) {

            String key = spc.name.trim().replaceAll("[\\s]{1,}", " ");
            Float val = (hmJar.get(key) == null) ? 0.f : hmJar.get(key);
            hmJar.put(key, val + spc.inCost);
            hmArt.put(key, spc.artikl);
        }
        try {
            Sheet sheet = Workbook.getWorkbook(new File(path)).getSheet(0);
            if ("ps3".equals(eSetting.find(2).getStr(eSetting.val)) == true) {
                for (int i = 2; i < sheet.getRows(); i++) {

                    String art = sheet.getCell(0, i).getContents().trim();
                    String key = sheet.getCell(1, i).getContents().trim().replaceAll("[\\s]{1,}", " ");
                    String val = sheet.getCell(6, i).getContents();
                    if (key.isEmpty() || art.isEmpty() || val.isEmpty()) {
                        continue;
                    }
                    //System.out.println(art + " - " + key + " - " + val);
                    val = val.replaceAll("[\\s|\\u00A0]+", "").replace(",", ".");
                    Float val2 = (hmXls.get(key) == null) ? 0.f : hmXls.get(key);
                    try {
                        Float val3 = Float.valueOf(val) + val2;
                        hmXls.put(key, val3);
                        hmArt.put(key, art);
                    } catch (Exception e) {
                        System.err.println("Ошибка:Main.compareIWin " + e);
                        continue;
                    }
                }
            } else {
                for (int i = 5; i < sheet.getRows(); i++) {

                    String art = sheet.getCell(1, i).getContents().trim();
                    String key = sheet.getCell(2, i).getContents().trim().replaceAll("[\\s]{1,}", " ");
                    String val = sheet.getCell(10, i).getContents();
                    if (key.isEmpty() || art.isEmpty() || val.isEmpty()) {
                        continue;
                    }
                    //System.out.println(art + " - " + key + " - " + val);
                    val = val.replaceAll("[\\s|\\u00A0]+", "").replace(",", ".");
                    Float val2 = (hmXls.get(key) == null) ? 0.f : hmXls.get(key);
                    try {
                        Float val3 = Float.valueOf(val) + val2;
                        hmXls.put(key, val3);
                        hmArt.put(key, art);
                    } catch (Exception e) {
                        System.err.println("Ошибка:Main.compareIWin " + e);
                        continue;
                    }
                }
            }
            if (detail == true) {
                System.out.printf("%-64s%-24s%-16s%-16s%-16s", new Object[]{"Name", "Artikl", "Xls", "Jar", "Delta"});
                System.out.println();
                for (Map.Entry<String, Float> entry : hmXls.entrySet()) {
                    String key = entry.getKey();
                    Float val1 = entry.getValue();
                    Float val2 = (hmJar.get(key) == null) ? 0.f : hmJar.get(key);
                    hmJar.remove(key);
                    System.out.printf("%-64s%-24s%-16.2f%-16.2f%-16.2f", new Object[]{key, hmArt.get(key), val1, val2, Math.abs(val1 - val2)});
                    System.out.println();
                    jarTotal = jarTotal + val2;
                    iwinTotal = iwinTotal + val1;
                }
                //System.out.println();
                if (hmJar.isEmpty() == false) {
                    System.out.printf("%-72s%-24s%-20s", new Object[]{"Name", "Artikl", "Value"});
                }
                System.out.println();
                for (Map.Entry<String, Float> entry : hmJar.entrySet()) {
                    String key = entry.getKey();
                    Float value3 = entry.getValue();
                    System.out.printf("%-72s%-24s%-16.2f", "Лишние: " + key, hmArt.get(key), value3);
                    System.out.println();
                    jarTotal = jarTotal + value3;
                }
            } else {
                for (Map.Entry<String, Float> entry : hmXls.entrySet()) {
                    String key = entry.getKey();
                    Float val1 = entry.getValue();
                    Float val2 = (hmJar.get(key) == null) ? 0.f : hmJar.get(key);
                    hmJar.remove(key);
                    jarTotal = jarTotal + val2;
                    iwinTotal = iwinTotal + val1;
                }
                for (Map.Entry<String, Float> entry : hmJar.entrySet()) {
                    String key = entry.getKey();
                    Float value3 = entry.getValue();
                    jarTotal = jarTotal + value3;
                }
            }
            System.out.printf("%-18s%-18s%-18s%-12s", "Prj=" + prj, "iwin=" + String.format("%.2f", iwinTotal), "jar="
                    + String.format("%.2f", jarTotal), "dx=" + String.format("%.2f", Math.abs(iwinTotal - jarTotal)));
            System.out.println();

        } catch (Exception e2) {
            System.err.println("Ошибка:Main.compareIWin " + e2);
        }
    }

    //System.out.println("\u001B[31m XXX \u001B[0m");
    public static void iwinRec(Wincalc iwin, int pnumb) {
        try {
            Map<String, List> map = new HashMap();
            Connection cn = Test.connect(numDb)[0];
            Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("select a.* from SPECPAU a left join LISTPRJ b on a.PUNIC = b.PUNIC where b.PNUMB = " + pnumb);
            while (rs.next()) {
                List rec = new ArrayList();
                for (int i = 0; i < Fld.values().length; i++) {
                    rec.add(rs.getObject(Fld.values()[i].name()));
                }
                map.put(rs.getString("ANUMB"), rec);
                System.out.println(rec);            
            }

        } catch (SQLException e) {
            println("Ошибка: DBCompare.iwinRec().  " + e);
        }
    }
}
