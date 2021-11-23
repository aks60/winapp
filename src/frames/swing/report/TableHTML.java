package frames.swing.report;

import common.eProperty;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JTable;
import dataset.Field;
import dataset.Table;

public class TableHTML {

    private static String charset = "UTF-8";
    private static int npp = 0;

    public static void load(String title, JTable table) {
        try ( PrintWriter out = new PrintWriter(new File(eProperty.path_prop.value, "report.html"))) {
            String text = text(title, table);
            byte[] ptext = text.getBytes();
            String text2 = new String(ptext, "windows-1251");
            out.print(text2);

        } catch (IOException e) {
            System.err.println("Ошибка:frames.swing.report.TableHTML.write() " + e);
        }
    }

    public static void load(String title, Table table, ArrayList<RColumn> listReportNode) {
        try ( PrintWriter out = new PrintWriter(new File(eProperty.path_prop.value, "report.html"))) {
            String text = text(title, table, listReportNode);
            out.print(text);

        } catch (IOException e) {
            System.err.println("Ошибка:frames.swing.report.TableHTML.write() " + e);
        }
    }

    public static void load(String title, Table table, Field... field) {
        try ( PrintWriter out = new PrintWriter(new File(eProperty.path_prop.value, "report.html"))) {
            String text = text2(title, table, field);
            byte[] ptext = text.getBytes();
            String text2 = new String(ptext, "UTF-8");
            out.print(text2);

        } catch (IOException e) {
            System.err.println("Ошибка:frames.swing.report.TableHTML.write() " + e);
        }
    }

    public static void load(String title, JTable table1, JTable table2) {
        try ( PrintWriter out = new PrintWriter(new File(eProperty.path_prop.value, "report.html"))) {
            String text = text(title, table1, table2);
            out.print(text);

        } catch (IOException e) {
            System.err.println("Ошибка:frames.swing.report.TableHTML.write() " + e);
        }
    }

    public static String text(String title, JTable table) {

        String str = "<HTML><META http-equiv=Content-Type content=\" "
                + " text/html; charset=" + charset + " \"> <HEAD> <STYLE>  <!-- "
                + "TABLE{ border: none; border-collapse:collapse; } "
                + "CAPTION{ color: #0000FF; font-size: 18px} "
                + "TH{ border: 0.5pt solid black; } "
                + "TD{ border: 0.5pt solid black; } "
                + "TD.TDFC{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDFR{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDC{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDCB { height: 20px; border-left: none;  border-right: none; } --> </STYLE> "
                + "</HEAD> <BODY> <TABLE BORDER=1 CELLSPACING=0 CELLPADDING=1> "
                + "<CAPTION><br> " + title + " <br><br></CAPTION> ";
        //записал название выбранных столбцов
        for (int index = 0; index < table.getColumnCount(); index++) {
            String colName = table.getColumnName(index);
            str += "<TH>" + colName + "</TH>";
        }
        //поехали !!!
        for (int row = 0; row < table.getRowCount(); row++) {
            str += "<TR>";//начало строки
            //первый столбец, фиксированные записи
            Object obj = table.getValueAt(row, 0);
            String str2 = (obj == null) ? "" : obj.toString();
            str += "<TD class=TDFC>" + str2 + "</TD>";
            //остальные столбцы
            for (int col = 1; col < table.getColumnCount(); col++) {
                obj = table.getValueAt(row, col);
                str2 = (obj == null) ? "" : obj.toString();
                str += "<TD class=TDC>" + str2 + "</TD>";
            }
            str += "</TR>";//конец строки
        }
        str += "</TABLE></BODY>";
        return str;
    }

    public static String text(String title, JTable table, JTable table2) {

        String str = "<HTML><META http-equiv=Content-Type content=\" "
                + " text/html; charset=" + charset + " \"> <HEAD> <STYLE>  <!-- "
                + "TABLE{ border: none; border-collapse:collapse; } "
                + "CAPTION{ color: #0000FF; font-size: 18px} "
                + "TH{ border: 0.5pt solid black; } "
                + "TD{ border: 0.5pt solid black; } "
                + "TD.TDFC{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDFR{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDC{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDCB { height: 20px; border-left: none;  border-right: none; } --> </STYLE> "
                + "</HEAD> <BODY> <TABLE BORDER=1 CELLSPACING=0 CELLPADDING=1> "
                + "<CAPTION><br> " + title + " <br><br></CAPTION> ";
        //записал название выбранных столбцов
        int indexTitle = 0;
        for (indexTitle = 0; indexTitle < table.getColumnCount(); indexTitle++) {
            String colName = table.getColumnName(indexTitle);
            str += "<TH>" + colName + "</TH>";
        }
        for (indexTitle = 0; indexTitle < table2.getColumnCount(); indexTitle++) {
            String colName = table2.getColumnName(indexTitle);
            str += "<TH>" + colName + "</TH>";
        }

        //поехали !!!
        for (int row = 0; row < table.getRowCount(); row++) {
            str += "<TR>";//начало строки
            //первый столбец, фиксированные записи
            Object obj = table.getValueAt(row, 0);
            String str2 = (obj == null) ? "" : obj.toString();
            str += "<TD class=TDFC>" + str2 + "</TD>";
            //остальные столбцы
            for (int col = 1; col < table.getColumnCount(); col++) {
                obj = table.getValueAt(row, col);
                str2 = (obj == null) ? "" : obj.toString();
                str += "<TD class=TDC>" + str2 + "</TD>";
            }
            for (int col = 0; col < table2.getColumnCount(); col++) {
                obj = table2.getValueAt(row, col);
                str2 = (obj == null) ? "" : obj.toString();
                str2 = str2.replace('.', ',');
                str += "<TD class=TDC>" + str2 + "</TD>";
            }

            str += "</TR>";//конец строки
        }
        str += "</TABLE></BODY>";
        return str;
    }

    public static String text2(String title, Table table, Field... fields) {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n"
                + "<html>\n"
                + "<head>\n"
                + "  <meta content=\"text/html; charset=UTF-8\"\n"
                + " http-equiv=\"content-type\">\n"
                + "  <title>qwerty</title>\n"
                + "</head>\n"
                + "<body style=\"margin-top: 60px; margin-left: 0px; width: 865px;\">\n"
                + "&nbsp; &nbsp;Группы печати<br>\n"
                + "&nbsp; &nbsp;Группы печати<br>\n"
                + "&nbsp; &nbsp;Группы печати<br>\n"
                + "<br>\n"
                + "&nbsp; &nbsp;<span\n"
                + " style=\"margin-top: 0px; text-decoration: underline;\">Группы\n"
                + "печати &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;\n"
                + "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<br>\n"
                + "<br>\n"
                + "</span>&nbsp; &nbsp;\n"
                + "<table\n"
                + " style=\"text-align: left; margin-left: 0px; height: 340px; width: 595px;\"\n"
                + " border=\"1\" cellpadding=\"2\" cellspacing=\"2\">\n"
                + "  <tbody>\n"
                + "    <tr>\n"
                + "      <td style=\"width: 285px;\" rowspan=\"10\"><img\n"
                + " style=\"width: 32px; height: 32px;\" alt=\"qwe\"\n"
                + " src=\"file:///C:/Documents%20and%20Settings/All%20Users/Application%20Data/Acron/Okna/117.ico\"><br>\n"
                + "      </td>\n"
                + "      <td style=\"width: 101px;\">Ыпавпапрачри</td>\n"
                + "      <td style=\"width: 147px;\"></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td style=\"width: 101px;\">Рдшгордлордлшг</td>\n"
                + "      <td style=\"width: 147px;\"></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td style=\"width: 101px;\">Тщшрощдшрдщлшгр</td>\n"
                + "      <td style=\"width: 147px;\"></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td style=\"width: 101px;\">Бщжшощжшодло</td>\n"
                + "      <td style=\"width: 147px;\"></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td style=\"width: 101px;\"></td>\n"
                + "      <td style=\"width: 147px;\"></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td style=\"width: 101px;\"></td>\n"
                + "      <td style=\"width: 147px;\"></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td style=\"width: 101px;\"></td>\n"
                + "      <td style=\"width: 147px;\"></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td style=\"width: 101px;\"></td>\n"
                + "      <td style=\"width: 147px;\"></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td style=\"width: 101px;\"></td>\n"
                + "      <td style=\"width: 147px;\"></td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td style=\"width: 101px;\"></td>\n"
                + "      <td style=\"width: 147px;\"></td>\n"
                + "    </tr>\n"
                + "  </tbody>\n"
                + "</table>\n"
                + "<br>\n"
                + "</body>\n"
                + "</html>";
    }

    public static String text(String title, Table table, Field... fields) {

        String str = "<HTML><META http-equiv=Content-Type content=\" "
                + " text/html; charset=" + charset + " \"> <HEAD> <STYLE>  <!-- "
                + "TABLE{ border: none; border-collapse:collapse; } "
                + "CAPTION{ color: #0000FF; font-size: 18px} "
                + "TH{ border: 0.5pt solid black; } "
                + "TD{ border: 0.5pt solid black; } "
                + "TD.TDFC{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDFR{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDC{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDCB { height: 20px; border-left: none;  border-right: none; } --> </STYLE> "
                + "</HEAD> <BODY> <TABLE BORDER=1 CELLSPACING=0 CELLPADDING=1> "
                + "<CAPTION><br> " + title + " <br><br></CAPTION> ";
        //записал название выбранных столбцов
        for (Field field : fields) {
            str += "<TH>" + field.meta().descr + "</TH>";
        }
        //поехали !!!
        for (int row = 0; row < table.size(); row++) {
            str += "<TR>";//начало строки
            //первый столбец, фиксированные записи
            Field firstField = fields[0];
            str += "<TD class=TDFC>" + table.getAs(row, firstField);
            str += "</TD>";
            //остальные столбцы
            for (int index = 1; index < fields.length; index++) {
                Field nextField = fields[index];
                str += "<TD class=TDC>" + table.getAs(row, nextField);
                str += "</TD>";
            }
            str += "</TR>";//конец строки
        }
        str += "</TABLE></BODY>";
        return str;
    }

    public static String text(String title, Table table, ArrayList<RColumn> listRColumn) {

        String str = "<HTML><META http-equiv=Content-Type content=\" "
                + " text/html; charset=" + charset + " \"> <HEAD> <STYLE>  <!-- "
                + "TABLE{ border: none; border-collapse:collapse; } "
                + "CAPTION{ color: #0000FF; font-size: 18px} "
                + "TH{ border: 0.5pt solid black; } "
                + "TD{ border: 0.5pt solid black; } "
                + "TD.TDFC{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDFR{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDC{ background-color: #FFFFFF; border: 0.5pt solid black; } "
                + "TD.TDCB { height: 20px; border-left: none;  border-right: none; } --> </STYLE> "
                + "</HEAD> <BODY> <TABLE BORDER=1 CELLSPACING=0 CELLPADDING=1> "
                + "<CAPTION><br> " + title + " <br><br></CAPTION> ";

        //записал название выбранных столбцов
        str += "<TH>№пп</TH>";
        for (RColumn rColumn : listRColumn) {
            str += "<TH>" + rColumn + "</TH>";
        }

        //поехали !!!
        npp = 0;
        for (int indexMaster = 0; indexMaster < table.size(); indexMaster++) {
            str += "<TR>";//начало строки     

            //номер по порядку
            String nppReport = table.get(indexMaster).get(0).equals("") ? "" : String.valueOf(++npp);
            str += "<TD class=TDFC>" + nppReport + "</TD>";

            //первый столбец, фиксированные записи           
            RColumn firsColumn = listRColumn.get(0);
            Field firstField = firsColumn.fields().get(0);
            str += "<TD class=TDFC>" + table.getAs(indexMaster, firstField);
            for (int indexField = 1; indexField < firsColumn.fields().size(); indexField++) {
                Field field = firsColumn.fields().get(indexField);
                str += "<br>" + table.getAs(indexMaster, field);
            }
            str += "</TD>";

            //остальные столбцы
            for (int indexNode = 1; indexNode < listRColumn.size(); indexNode++) {
                RColumn nextColumn = listRColumn.get(indexNode);
                Field nextField = nextColumn.fields().get(0);
                str += "<TD class=TDC>" + table.getAs(indexMaster, nextField);
                for (int indexField = 1; indexField < nextColumn.fields().size(); indexField++) {
                    Field field = nextColumn.fields().get(indexField);
                    str += "<br>" + table.getAs(indexMaster, field);
                }
                str += "</TD>";
            }
            str += "</TR>";//конец строки
        }
        str += "</TABLE></BODY>";
        return str;
    }
}
