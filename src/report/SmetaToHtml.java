package report;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SmetaToHtml {

    public static void exec() {
        try {
            URL path = ReportDocx.class.getResource("/resource/report/Smeta2.html");
            File input = new File(path.toURI());
            Document doc = Jsoup.parse(input, "utf-8");
            String str = doc.html();
            TableToHtml.write(str);
            ExecuteCmd.startWord("report.html");
            
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Нет доступа к файлу. Файл отчёта открыт другим приложением.", "ВНИМАНИЕ!", 1);
            System.err.println("Ошибка1:SmetaToHtml.xx()" + e);
        } catch (URISyntaxException e) {
            System.err.println("Ошибка2:SmetaToHtml.xx()" + e);
        } catch (Exception e) {
            System.err.println("Ошибка3:SmetaToHtml.xx()" + e);
        }
    }

}
