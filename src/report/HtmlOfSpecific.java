package report;

import builder.Wincalc;
import builder.making.Specific;
import dataset.Record;
import domain.eArtikl;
import domain.ePrjprod;
import domain.eProject;
import frames.UGui;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlOfSpecific {

    private static int npp = 0;
    private static DecimalFormat df1 = new DecimalFormat("#0.0");
    private static DecimalFormat df2 = new DecimalFormat("#0.00");

    public static void specific(Record projectRec) {
        npp = 0;
        try {
            URL path = HtmlOfSpecific.class.getResource("/resource/report/Specific.html");
            File input = new File(path.toURI());
            Document doc = Jsoup.parse(input, "utf-8");

            //Заполним отчёт
            load(projectRec, doc);

            String str = doc.html();
            HtmlOfTable.write(str);
            ExecuteCmd.documentType(null);

        } catch (Exception e) {
            System.err.println("Ошибка:HtmlOfSpecific.specific()" + e);
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
        String num = projectRec.getStr(eProject.num_ord);
        String date = UGui.simpleFormat.format(projectRec.get(eProject.date4));
        String total = df1.format(spcList3.stream().mapToDouble(spc -> spc.getCost1()).sum());

        List<RSpecific> s1 = spcList3.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 1).collect(toList());
        List<RSpecific> s2 = RSpecific.groups(spcList3.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 2).collect(toList()));
        List<RSpecific> s3 = RSpecific.groups(spcList3.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 3).collect(toList()));
        List<RSpecific> s4 = RSpecific.groups(spcList3.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 5).collect(toList()));

        Elements template = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        doc.getElementsByTag("tbody").get(0).html("");

        template.get(0).getElementsByTag("td").get(0).getElementsByTag("b").get(0).text("ПРОФИЛИ");
        doc.getElementsByTag("tbody").append(template.get(0).html());
        s1.forEach(spc -> templateAdd(template, spc, doc));
        template.get(0).getElementsByTag("td").get(0).getElementsByTag("b").get(0).text("АКСЕССУАРЫ");
        doc.getElementsByTag("tbody").append(template.get(0).html());
        s2.forEach(spc -> templateAdd(template, spc, doc));
        template.get(0).getElementsByTag("td").get(0).getElementsByTag("b").get(0).text("УПЛОТНЕНИЯ");
        doc.getElementsByTag("tbody").append(template.get(0).html());
        s3.forEach(spc -> templateAdd(template, spc, doc));
        template.get(0).getElementsByTag("td").get(0).getElementsByTag("b").get(0).text("ЗАПОЛНЕНИЯ");
        doc.getElementsByTag("tbody").append(template.get(0).html());
        s4.forEach(spc -> templateAdd(template, spc, doc));

        //double total = spcList3.stream().mapToDouble(spc -> spc.getCost1()).sum(); 
    }

    private static void templateAdd(Elements template, RSpecific spc, Document doc) {
        Elements tdList = template.get(1).getElementsByTag("td");
        tdList.get(0).text(String.valueOf(++npp));
        tdList.get(1).text(spc.getArtikl());
        tdList.get(2).text(spc.getName());
        tdList.get(3).text(spc.getColorID1());
        tdList.get(4).text(spc.getCount());
        tdList.get(5).text(spc.getUnit());
        tdList.get(6).text(spc.getPrice());
        tdList.get(7).text(spc.getCost());
        doc.getElementsByTag("tbody").append(template.get(1).html());
    }
}
