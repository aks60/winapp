package report;

import dataset.Record;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlOfSpecific {
    private static DecimalFormat df1 = new DecimalFormat("#0.0");
    private static DecimalFormat df2 = new DecimalFormat("#0.00");

    public static void specific(Record projectRec) {
        try {
            URL path = HtmlOfSpecific.class.getResource("/resource/report/Specific.html");
            File input = new File(path.toURI());
            Document doc = Jsoup.parse(input, "utf-8");

            //Заполним отчёт
            load(projectRec, doc);

            String str = doc.html();
            HtmlOfTable.write(str);
            ExecuteCmd.startHtml("report.html");
            //ExecuteCmd.startWord("report.html");

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Нет доступа к файлу. Процесс не может получить доступ к файлу, так как этот файл занят другим процессом.", "ВНИМАНИЕ!", 1);
            System.err.println("Ошибка1:HtmlOfSpecific.xx()" + e);
        } catch (URISyntaxException e) {
            System.err.println("Ошибка2:HtmlOfSpecific.xx()" + e);
        } catch (Exception e) {
            System.err.println("Ошибка3:HtmlOfSpecific.xx()" + e);
        }
    }
    
    private static void load(Record projectRec, Document doc) {
        
    }    
}
