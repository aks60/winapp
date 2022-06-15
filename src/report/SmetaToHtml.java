package report;

import dataset.Record;
import domain.ePrjpart;
import domain.ePrjprod;
import domain.eProject;
import domain.eSysuser;
import frames.UGui;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SmetaToHtml {

    public static void exec(Record projectRec) {
        try {
            URL path = ReportDocx.class.getResource("/resource/report/Smeta2.html");
            File input = new File(path.toURI());
            Document doc = Jsoup.parse(input, "utf-8");

            update(projectRec, doc);

            String str = doc.html();
            TableToHtml.write(str);
            ExecuteCmd.startWord("report.html");

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Нет доступа к файлу. Процесс не может получить доступ к файлу, так как этот файл занят другим процессом.", "ВНИМАНИЕ!", 1);
            System.err.println("Ошибка1:SmetaToHtml.xx()" + e);
        } catch (URISyntaxException e) {
            System.err.println("Ошибка2:SmetaToHtml.xx()" + e);
        } catch (Exception e) {
            System.err.println("Ошибка3:SmetaToHtml.xx()" + e);
        }
    }

    private static void update(Record projectRec, Document doc) {
        Record prjpartRec = ePrjpart.find(projectRec.getInt(eProject.prjpart_id));
        Record sysuserRec = eSysuser.find2(prjpartRec.getStr(ePrjpart.login));  
        List<Record> prjprodList = ePrjprod.find2(projectRec.getInt(eProject.id));

        Elements titl = doc.getElementById("p1").getElementsByTag("b");
        titl.get(0).text("Смета №" + projectRec.getStr(eProject.num_ord) + " от '" + UGui.DateToStr(projectRec.get(eProject.date4)) + "'");
        Elements attr = doc.getElementById("tab1").getElementsByTag("td");        
         //Част.лицо
        if(prjpartRec.getInt(ePrjpart.flag2) == 0) {
           attr.get(1).text(prjpartRec.getStr(ePrjpart.partner)); 
           attr.get(5).text(prjpartRec.getStr(ePrjpart.addr_phone));
           attr.get(9).text(prjpartRec.getStr(ePrjpart.addr_email));
           //Организация
        } else { 
            attr.get(1).text(prjpartRec.getStr(ePrjpart.partner));
            attr.get(5).text(prjpartRec.getStr(ePrjpart.org_phone));
            attr.get(9).text(prjpartRec.getStr(ePrjpart.org_email));
            attr.get(13).text(prjpartRec.getStr(ePrjpart.org_contact));
        }
        
        attr.get(3).text(sysuserRec.getStr(eSysuser.fio));
        attr.get(7).text(sysuserRec.getStr(eSysuser.phone));
        attr.get(11).text(sysuserRec.getStr(eSysuser.email));
                
        Element tab2 = doc.getElementById("tab2");
        String fragment = tab2.html() + "<br>";
        
        prjprodList.forEach(act -> tab2.append(fragment));

        
        
        
        Elements elems = doc.getElementById("tab2").getElementsByTag("caption");
        System.out.println(fragment);
    }
}
