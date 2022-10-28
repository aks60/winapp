package report;

import builder.Wincalc;
import common.eProp;
import dataset.Conn;
import dataset.Field;
import dataset.Query;
import static dataset.Query.SEL;
import dataset.Record;
import domain.ePrjkit;
import domain.ePrjpart;
import domain.ePrjprod;
import domain.eProject;
import domain.eSyssize;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                tdList.get(3).text(systemProfile(6));
//                tdList.get(4).text(prjprodRec.getStr(ePrjprod.name));
//                tdList.get(6).text(winc.width() + "x" + winc.height());
//                tdList.get(8).text(glassList.get(0).artiklRecAn().getStr(eArtikl.code));
//                tdList.get(10).text("");
//                tdList.get(12).text(eColor.find(winc.colorID1).getStr(eColor.name) + " / "
//                        + eColor.find(winc.colorID2).getStr(eColor.name) + " / "
//                        + eColor.find(winc.colorID3).getStr(eColor.name));
//                tdList.get(14).text(prjprodRec.getStr(ePrjprod.num));
//                tdList.get(16).text(df2.format(winc.square()));
//                tdList.get(18).text(df2.format(winc.weight()));
//                tdList.get(20).text(df1.format(prjprodRec.getInt(ePrjprod.num) * winc.price()));
//                tdList.get(22).text(df1.format(winc.price() / winc.square()));
//                tdList.get(24).text(df1.format(prjprodRec.getInt(ePrjprod.num) * winc.cost2()));
//
//                total += prjprodRec.getInt(ePrjprod.num) * winc.cost2();
//
//                if (prjkitList.size() == 0) {
//                    tab3List.get(i).html("");
//                } else {
//                    Elements captions3 = tab3List.get(i).getElementsByTag("caption");
//                    captions3.get(0).text("Комплектация к изделию № " + (i + 1));
//                    String template3 = tab3List.get(i)
//                            .getElementsByTag("tbody").get(0).getElementsByTag("tr").get(0).html();
//                    for (int k = 1; k < prjkitList.size(); k++) {
//                        tab3List.get(i).getElementsByTag("tbody").get(0).append(template3);
//                    }
//
//                    //Цикл по строкам комплектации
//                    for (int k = 0; k < prjkitList.size(); k++) {
//
//                        Record prjkitRec = prjkitList.get(k);
//                        Record artiklRec = eArtikl.find(prjkitRec.getInt(ePrjkit.artikl_id), true);
//
//                        Elements tr3List = tab3List.get(i).getElementsByTag("tr");
//                        Elements td3List = tr3List.get(k + 1).getElementsByTag("td");
//
//                        td3List.get(0).text(String.valueOf(k + 1));
//                        td3List.get(1).text(artiklRec.getStr(eArtikl.code));
//                        td3List.get(2).text(artiklRec.getStr(eArtikl.name));
//                        td3List.get(3).text(eColor.find(winc.colorID1).getStr(eColor.name));
//                        td3List.get(4).text(df1.format(prjkitRec.getFloat(ePrjkit.width))
//                                + "x" + df1.format(prjkitRec.getFloat(ePrjkit.height)));
//                        td3List.get(5).text(prjkitRec.getStr(ePrjkit.numb));
//                        td3List.get(6).text(df1.format(0));
//                        td3List.get(7).text(df1.format(0));
//                    }
//                }
            }
//            //СЕКЦИЯ №3
//            Elements trList = doc.getElementById("tab6").getElementsByTag("tr");
//            trList.get(0).getElementsByTag("td").get(1).text(df2.format(total));
//            trList.get(1).getElementsByTag("td").get(0).text(MoneyInWords.inwords(total));
//            trList.get(3).getElementsByTag("td").get(0).text("Площадь изделий в заказе : " + df1.format(square / 1000000) + " кв.м.");
//
//            Elements imgList = doc.getElementById("div2").getElementsByTag("img");
//            for (int i = 0; i < imgList.size(); i++) {
//                Element get = imgList.get(i);
//                get.attr("src", "C:\\Users\\All Users\\Avers\\Okna\\img" + (i + 1) + ".gif");
//            }
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

    private static String systemProfile(int id) {
        String ret = "";
        try {
            Statement statement = Conn.connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet recordset = statement.executeQuery("with recursive tree as (select * from systree where id = " 
                    + id + " union all select * from systree a join tree b on a.id = b.parent_id and b.id != b.parent_id) select * from tree");
            while (recordset.next()) {
                ret = recordset.getString("name") + " / " + ret;
            }
            statement.close();

        } catch (SQLException e) {
            System.err.println(e);
        }
        return ret;
    }
}
