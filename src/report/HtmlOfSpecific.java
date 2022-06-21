package report;

import builder.Wincalc;
import builder.making.Specific;
import dataset.Record;
import domain.ePrjprod;
import domain.eProject;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
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
            ExecuteCmd.documentType(null);

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
        List<Specific> spcList2 = new ArrayList();
        List<Record> prjprodList = ePrjprod.find2(projectRec.getInt(eProject.id));
        for (Record prjprodRec : prjprodList) {
            String script = prjprodRec.getStr(ePrjprod.script);
            Wincalc winc = new Wincalc(script);
            winc.constructiv(true);
            spcList2.addAll(winc.listSpec);
        }
        List<RSpecific> spcList3 = new ArrayList();
        spcList2.forEach(el -> spcList3.add(new RSpecific(el)));
        double total = spcList3.stream().mapToDouble(spc -> spc.getCost1()).sum(); 
        
        String template = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr").get(0).html();
        for (int i = 1; i < spcList3.size(); i++) {
            doc.getElementsByTag("tbody").append(template);
        }        
    }    
}
