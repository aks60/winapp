package report;

import builder.Wincalc;
import builder.making.Specific;
import dataset.Record;
import domain.ePrjprod;
import domain.eProject;
import frames.Specifics;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlOfMaterial {

    public static void material(Record projectRec) {
        try {
            URL path = ReportDocx.class.getResource("/resource/report/Material.html");
            File input = new File(path.toURI());
            Document doc = Jsoup.parse(input, "utf-8");

            //Заполним отчёт
            //load(projectRec, doc);

            String str = doc.html();
            HtmlOfTable.write(str);
            ExecuteCmd.startHtml("report.html");
            //ExecuteCmd.startWord("report.html");

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Нет доступа к файлу. Процесс не может получить доступ к файлу, так как этот файл занят другим процессом.", "ВНИМАНИЕ!", 1);
            System.err.println("Ошибка1:SmetaToHtml.xx()" + e);
        } catch (URISyntaxException e) {
            System.err.println("Ошибка2:SmetaToHtml.xx()" + e);
        } catch (Exception e) {
            System.err.println("Ошибка3:SmetaToHtml.xx()" + e);
        }
    }

    private static void load(Record projectRec, Document doc) {

        List<Specific> spcList = new ArrayList();
        List<Record> prjprodList = ePrjprod.find2(projectRec.getInt(eProject.id));
        for (Record prjprodRec : prjprodList) {
            String script = prjprodRec.getStr(ePrjprod.script);
            Wincalc winc = new Wincalc(script);
            winc.constructiv(true);
            spcList.addAll(winc.listSpec);
        }
        List<Specific> spcList2 = Specifics.groups(spcList, 3);
        //double total = spcList3.stream().mapToDouble(spc -> spc.getCost1()).sum();            
    }
}
