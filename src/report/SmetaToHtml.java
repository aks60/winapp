package report;

import builder.Wincalc;
import dataset.Query;
import dataset.Record;
import domain.eColor;
import domain.ePrjpart;
import domain.ePrjprod;
import domain.eProject;
import domain.eSysuser;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import frames.UGui;
import frames.swing.draw.Canvas;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SmetaToHtml {

    private static DecimalFormat df1 = new DecimalFormat("0.0");
    private static DecimalFormat df2 = new DecimalFormat("#0.00");

    public static void exec(Record projectRec) {
        try {
            URL path = ReportDocx.class.getResource("/resource/report/Smeta2.html");
            File input = new File(path.toURI());
            Document doc = Jsoup.parse(input, "utf-8");

            //Заполним отчёт
            load(projectRec, doc);

            String str = doc.html();
            TableToHtml.write(str);
            ExecuteCmd.startHtml("report.html");

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
        int npp = 0, length = 400;
        float sum1 = 0f, sum2 = 0f, total = 0f;

        Record prjpartRec = ePrjpart.find(projectRec.getInt(eProject.prjpart_id));
        Record sysuserRec = eSysuser.find2(prjpartRec.getStr(ePrjpart.login));
        List<Record> prjprodList = ePrjprod.find2(projectRec.getInt(eProject.id));

        Elements titl = doc.getElementById("p1").getElementsByTag("b");
        titl.get(0).text("Смета №" + projectRec.getStr(eProject.num_ord) + " от '" + UGui.DateToStr(projectRec.get(eProject.date4)) + "'");
        Elements attr = doc.getElementById("tab1").getElementsByTag("td");
        if (prjpartRec.getInt(ePrjpart.flag2) == 0) {
            //Част.лицо
            attr.get(1).text(prjpartRec.getStr(ePrjpart.partner));
            attr.get(5).text(prjpartRec.getStr(ePrjpart.addr_phone));
            attr.get(9).text(prjpartRec.getStr(ePrjpart.addr_email));

        } else {//Организация
            attr.get(1).text(prjpartRec.getStr(ePrjpart.partner));
            attr.get(5).text(prjpartRec.getStr(ePrjpart.org_phone));
            attr.get(9).text(prjpartRec.getStr(ePrjpart.org_email));
            attr.get(13).text(prjpartRec.getStr(ePrjpart.org_contact));
        }

        attr.get(3).text(sysuserRec.getStr(eSysuser.fio));
        attr.get(7).text(sysuserRec.getStr(eSysuser.phone));
        attr.get(11).text(sysuserRec.getStr(eSysuser.email));

        Element div2 = doc.getElementById("div2");
        String template2 = div2.html();
        List<Wincalc> wincList = wincList(prjprodList, length);

        for (int i = 1; i < prjprodList.size(); i++) {
           div2.append(template2); 
        }
        Elements tabs2 = doc.getElementById("div2").getElementsByClass("tab2");
        Elements tabs3 = doc.getElementById("div2").getElementsByClass("tab3");
        for (int i = 0; i < prjprodList.size(); i++) {

            Elements tdList = tabs2.get(i).getElementsByTag("td");
            Wincalc winc = wincList.get(i);
            Record prjprodRec = prjprodList.get(i);
            Elements captions2 = tabs2.get(i).getElementsByTag("caption");
            captions2.get(0).text("Изделие № " + (i + 1));            
            tdList.get(2).text(prjprodRec.getStr(ePrjprod.name));
            tdList.get(4).text(prjprodRec.getStr(ePrjprod.name));
            tdList.get(6).text(winc.width() + "x" + winc.height());
            tdList.get(8).text("ЗАП-ХХ");
            tdList.get(10).text("НЕТ");
            tdList.get(12).text(eColor.find(winc.colorID1).getStr(eColor.name) + " / " 
                    + eColor.find(winc.colorID2).getStr(eColor.name) + " / " 
                    + eColor.find(winc.colorID3).getStr(eColor.name));
            tdList.get(14).text(prjprodRec.getStr(ePrjprod.num));
            tdList.get(16).text("0,77 кв.м.");
            tdList.get(18).text("77 кг.");
            tdList.get(20).text(df1.format(winc.cost1()));
            tdList.get(22).text("772");
            tdList.get(24).text(df1.format(winc.cost2()));

            sum1 += winc.cost2();
            
            Elements captions3 = tabs3.get(i).getElementsByTag("caption");
            captions3.get(0).text("Комплектация к изделию № " + (i + 1)); 
        }
    }

    private static List<Wincalc> wincList(List<Record> prjprodList, int length) {
        List<Wincalc> list = new ArrayList();
        for (Record prjprod : prjprodList) {
            String script = prjprod.getStr(ePrjprod.script);
            Wincalc winc = new Wincalc(script);
            winc.constructiv(true);
            winc.imageIcon = Canvas.createIcon(winc, length);
            winc.bufferImg = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
            winc.gc2d = winc.bufferImg.createGraphics();
            winc.gc2d.fillRect(0, 0, length, length);
            float height = (winc.height1() > winc.height2()) ? winc.height1() : winc.height2();
            float width = (winc.width1() > winc.width2()) ? winc.width1() : winc.width2();
            winc.scale = (length / width > length / height) ? length / (height + 80) : length / (width + 80);
            winc.gc2d.scale(winc.scale, winc.scale);
            winc.rootArea.draw(); //рисую конструкцию
            list.add(winc);
        }
        return list;
    }

    private static byte[] toByteArray(BufferedImage bi) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }
}
