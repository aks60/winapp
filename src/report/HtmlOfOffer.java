package report;

import builder.Wincalc;
import common.MoneyInWords;
import common.eProp;
import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import domain.eFurniture;
import domain.ePrjkit;
import domain.ePrjpart;
import domain.ePrjprod;
import domain.eProject;
import domain.eSysfurn;
import domain.eSystree;
import domain.eSysuser;
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

public class HtmlOfOffer {

    private static DecimalFormat df0 = new DecimalFormat("0");
    private static DecimalFormat df1 = new DecimalFormat("0.0");
    private static DecimalFormat df2 = new DecimalFormat("#0.00");

    public static void offer(Record projectRec) {
        try {
            URL path = HtmlOfOffer.class.getResource("/resource/report/Offer.html");
            File input = new File(path.toURI());
            Document doc = Jsoup.parse(input, "utf-8");

            //Заполним отчёт
            load(projectRec, doc);

            String str = doc.html();
            HtmlOfTable.write(str);
            ExecuteCmd.documentType(null);

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Нет доступа к файлу. Процесс не может получить доступ к файлу, так как этот файл занят другим процессом.", "ВНИМАНИЕ!", 1);
            System.err.println("Ошибка1:HtmlOfSmeta.smeta2()" + e);
        } catch (URISyntaxException e) {
            System.err.println("Ошибка2:HtmlOfSmeta.smeta2()" + e);
        } catch (Exception e) {
            System.err.println("Ошибка3:HtmlOfSmeta.smeta2()" + e);
        }
    }

    private static void load(Record projectRec, Document doc) {
        int length = 400;
        float total = 0f;
        float square = 0f; //площадь
        try {
            Record prjpartRec = ePrjpart.find(projectRec.getInt(eProject.prjpart_id));
            Record sysuserRec = eSysuser.find2(prjpartRec.getStr(ePrjpart.login));
            List<Record> prjprodList = ePrjprod.find2(projectRec.getInt(eProject.id));
            List<Record> prjkitAll = new ArrayList();

            doc.getElementById("h01").text("Коммерческое предложение от " + UGui.DateToStr(projectRec.get(eProject.date4)));

            //СЕКЦИЯ №1
            //СЕКЦИЯ №2
            Element div2 = doc.getElementById("div2");
            String template2 = div2.html();
            List<Wincalc> wincList = wincList(prjprodList, length);

            for (int i = 1; i < prjprodList.size(); i++) {
                div2.append(template2);
            }
            Elements tab4List = doc.getElementById("div2").getElementsByClass("tab4");
            //Elements tab3List = doc.getElementById("div2").getElementsByClass("tab3");

            //Цикл по изделиям
            for (int i = 0; i < prjprodList.size(); i++) {

                Elements tdList = tab4List.get(i).getElementsByTag("td");
                Wincalc winc = wincList.get(i);
                square = square + winc.width() * winc.height();
                Record prjprodRec = prjprodList.get(i);
                List<Record> prjkitList = ePrjkit.find2(projectRec.getInt(eProject.id), prjprodRec.getInt(ePrjprod.id));
                prjkitAll.addAll(prjkitList);

                tdList.get(0).text("Изделие № " + (i + 1));
                tdList.get(3).text(eSystree.systemProfile(6));
                int furniture_id = winc.sysfurnRec.getInt(eSysfurn.furniture_id);
                String fname = (furniture_id != -1) ? eFurniture.find(furniture_id).getStr(eFurniture.name) : "";               
                tdList.get(5).text(fname);
                String gname = (winc.glassRec != null) ? winc.glassRec.getStr(eArtikl.code) + " - " + winc.glassRec.getStr(eArtikl.name) : "";
                tdList.get(7).text(gname);
                tdList.get(9).text(eColor.find(winc.colorID1).getStr(eColor.name));
                tdList.get(11).text(eColor.find(winc.colorID2).getStr(eColor.name));
                tdList.get(13).text(eColor.find(winc.colorID3).getStr(eColor.name));
                tdList.get(15).text(winc.width() + "x" + winc.height());
                tdList.get(17).text(prjprodRec.getStr(ePrjprod.num));
                tdList.get(19).text(df2.format(winc.square()));
                tdList.get(21).text(df2.format(winc.weight()));
                tdList.get(23).text(df1.format(prjprodRec.getInt(ePrjprod.num) * winc.price()));
                tdList.get(25).text(df1.format(prjprodRec.getInt(ePrjprod.num) * winc.cost2()));
            }
            //СЕКЦИЯ №
            Elements trList = doc.getElementById("tab5").getElementsByTag("tr");
            trList.get(0).getElementsByTag("td").get(2).text(df1.format(square / 1000000) + " кв.м.");
            //trList.get(3).getElementsByTag("td").get(1).text(df2.format(total));
            //trList.get(5).getElementsByTag("td").get(0).text(MoneyInWords.inwords(total));
            

            Elements imgList = doc.getElementById("div2").getElementsByTag("img");
            for (int i = 0; i < imgList.size(); i++) {
                Element get = imgList.get(i);
                get.attr("src", "C:\\Users\\All Users\\Avers\\Okna\\img" + (i + 1) + ".gif");
            }
        } catch (Exception e) {
            System.err.println("Ошибка:HtmlOfOffer.load()" + e);
        }
    }

    private static List<Wincalc> wincList(List<Record> prjprodList, int length) {
        List<Wincalc> list = new ArrayList();
        try {
            for (int index = 0; index < prjprodList.size(); ++index) {
                Record prjprodRec = prjprodList.get(index);
                String script = prjprodRec.getStr(ePrjprod.script);
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
                File outputfile = new File(eProp.path_prop.read(), "img" + (index + 1) + ".gif");
                ImageIO.write(winc.bufferImg, "gif", outputfile);
                list.add(winc);
            }
        } catch (Exception e) {
            System.err.println("Ошибка:HtmlOfSmeta.wincList()" + e);
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
