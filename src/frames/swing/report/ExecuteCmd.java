/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frames.swing.report;

import common.eProperty;
import dataset.eExcep;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.awt.Desktop;
import java.awt.Frame;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * <p>
 * Запуск отчетов в Office </p>
 */
public class ExecuteCmd {

    public static void repoType(Frame owner) {

        Object[] options = {"HTML", "WORD", "EXCEL"};
        int n = JOptionPane.showOptionDialog(owner, "Выберите формат отчёта для печати", "Формирование отчёта",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if (n == 0) {
            ExecuteCmd.startHtml("report.html");
        } else if (n == 1) {
            ExecuteCmd.startWord("report.html");
        } else if (n == 2) {
            ExecuteCmd.startExcel("report.html");
        }
    } 

    /**
     * Запуск файла из каталога
     */
    public static void startHelp(String file) {

        Desktop desktop = Desktop.getDesktop();
        try {
            URI url = new URI(eProperty.url_src.read() + "/appdoc/help/director/" + file.replace('.', '/') + ".html");
            desktop.browse(url);
            
        } catch (URISyntaxException e) {
            System.err.println("Ошибка:ExecuteCmd.startHelp() " + e);
        } catch (IOException e) {
            System.err.println("Ошибка:ExecuteCmd.startHelp() " + e);
        }
    }

    /**
     * Запуск HTML
     */
    public static void startHtml(String fileName) {
        try {
            String fileExe = eProperty.cmd_html.read();
            if (System.getProperty("os.name").equals("Linux")) {
                String path = eProperty.path_prop.value;
                Runtime.getRuntime().exec(fileExe + " " + path + "/" + fileName);
            } else {
                String path = replacePath(eProperty.path_prop.value);
                String[] cmd = arraysPath(fileExe, path, fileName);
                Runtime.getRuntime().exec(cmd);
            }
        } catch (Exception ex) {
            System.err.println("Ошибка:ExecuteCmd.startHtml() " + ex);
        }
    }

    /**
     * Запуск Word
     */
    public static void startWord(String fileName) {
        try {
            String fileExe = eProperty.cmd_word.read();
            if (System.getProperty("os.name").equals("Linux")) {
                String path = eProperty.path_prop.value;
                Runtime.getRuntime().exec(fileExe + " " + path + "/" + fileName);
            } else {
                String path = replacePath(eProperty.path_prop.value);
                String[] cmd = arraysPath(fileExe, path, fileName);
                Runtime.getRuntime().exec(cmd);
            }
        } catch (Exception ex) {
            System.err.println("Ошибка:ExecuteCmd.startWord() " + ex);
        }
    }

    /**
     * Запуск Excel
     */
    public static void startExcel(String fileName) {
        try {
            String fileExe = eProperty.cmd_excel.read();
            if (System.getProperty("os.name").equals("Linux")) {
                String path = eProperty.path_prop.value;
                Runtime.getRuntime().exec(fileExe + " " + path + "/" + fileName);
            } else {
                String path = replacePath(eProperty.path_prop.value);
                String[] cmd = arraysPath(fileExe, path, fileName);
                Runtime.getRuntime().exec(cmd);
            }
        } catch (Exception ex) {
            System.err.println("Ошибка:ExecuteCmd.startExcel() " + ex);
        }
    }

    /**
     * Извлечение файла из архива
     */
    public static void extractZip(String template, String path) {
        OutputStream out = null;
        try {
            ZipInputStream in = new ZipInputStream(new FileInputStream(path));
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                File file = new File(entry.getName());
                if (file.getName().equals(template)) {
                    out = new BufferedOutputStream(
                            new FileOutputStream(new File(eProperty.path_prop.value, "report.html")));
                    byte[] buffer = new byte[8192];
                    int readed;
                    while ((readed = in.read(buffer)) > 0) {
                        out.write(buffer, 0, readed);
                    }
                }
            }
            in.close();
            out.close();
        } catch (IOException ex) {
            System.out.println("ошибка:ExecuteCmd.extractZip() " + ex);
        }
        startHtml("report.html");
    }

    private static String[] arraysPath(String fileExe, String path, String fileName) {
        ArrayList list = new ArrayList();
        int k = fileExe.indexOf("cmd /c start");

        if (fileExe.indexOf("cmd") != -1) {
            list.add("cmd");
            fileExe = fileExe.substring(3).trim();
        }
        if (fileExe.indexOf("/c") != -1) {
            list.add("/c");
            fileExe = fileExe.substring(2).trim();
        }
        if (fileExe.indexOf("start") != -1) {
            list.add("start");
            fileExe = fileExe.substring(5).trim();
        }
        list.add(fileExe);
        list.add(path + fileName);
        String[] cmd = new String[list.size()];
        list.toArray(cmd);
        return cmd;
    }

    private static String replacePath(String src) {
        int index = 0;
        int index2 = 0;
        List<String> list = new ArrayList();
        while (index2 != -1) {
            index2 = src.indexOf("\\", index);
            if (index2 != -1) {
                String s = src.substring(index, index2);
                list.add(s);
                index = index2 + 1;
            }
        }
        list.add(src.substring(index, src.length()));

        for (int j = 0; j < list.size(); j++) {
            String str = list.get(j);
            int index3 = str.indexOf(" ");
            if (index3 != -1) {
//                for(int k = 0; k < str.length(); k++) { 
//                   //тут надо выполнить логику
//                }
                String str2 = str.substring(0, index3);
                String str3 = str.substring(index3, str.length()).trim();
                str = str2 + str3;
                list.set(j, str.substring(0, 6) + "~1");
            }
        }
        src = "";
        for (String src2 : list) {
            src = src + src2 + "\\";
        }
        return src;
    }
}
