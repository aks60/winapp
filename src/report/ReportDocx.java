package report;

import builder.Wincalc;
import builder.making.Specific;
import common.eProp;
import dataset.Record;
import domain.eArtikl;
import domain.eProject;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import frames.Specifics;
import frames.UGui;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.swing.JOptionPane;

public class ReportDocx {

    private static DecimalFormat df1 = new DecimalFormat("0.0");
    private static DecimalFormat df2 = new DecimalFormat("#0.00");

    public static void outGoMaterial(List<Wincalc> wincList, String orderNum) {
        try {
            List<Specific> spcList = new ArrayList();
            for (Wincalc winc : wincList) {
                winc.constructiv(true);
                spcList.addAll(winc.listSpec);
            }
            List<Specific> spcList2 = Specifics.groups(spcList, 3);
            List<SpecificRep> spcList3 = new ArrayList();
            spcList2.forEach(el -> spcList3.add(new SpecificRep(el)));
            double total = spcList3.stream().mapToDouble(spc -> spc.getCost1()).sum();

            InputStream in = ReportDocx.class.getResourceAsStream("/resource/report/OutGoMaterial.docx");
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);

            FieldsMetadata metadata = report.createFieldsMetadata();
            metadata.load("spc", SpecificRep.class, true);

            IContext context = report.createContext();
            context.put("num", orderNum);
            context.put("spc", spcList3);
            context.put("resultTotal", String.valueOf(df1.format(total)));

            OutputStream out = new FileOutputStream(new File(eProp.path_prop.read() + "/report.docx"));
            report.process(context, out);
            ExecuteCmd.startWord("report.docx");

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Нет доступа к файлу. Файл отчёта открыт другим приложением.", "ВНИМАНИЕ!", 1);
            System.err.println("Ошибка1:ReportDocx.outGoMaterial()" + e);
        } catch (XDocReportException e) {
            System.err.println("Ошибка2:ReportDocx.outGoMaterial()" + e);
        } catch (Exception e) {
            System.err.println("Ошибка3:ReportDocx.outGoMaterial()" + e);
        }
    }

    public static void Specific2(List<Wincalc> wincList, Record record) {
        try {
            List<Specific> m = new ArrayList();
            for (Wincalc winc : wincList) {
                winc.constructiv(true);
                m.addAll(winc.listSpec);
            }
            List<Specific> m1 = Specifics.groups(m, 3);
            List<SpecificRep> m2 = new ArrayList();
            m1.forEach(el -> m2.add(new SpecificRep(el)));

            String num = record.getStr(eProject.num_ord);
            String date = UGui.simpleFormat.format(record.get(eProject.date4));
            String total = df1.format(m2.stream().mapToDouble(spc -> spc.getCost1()).sum());

            List<SpecificRep> s1 = m2.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 1).collect(toList());
            List<SpecificRep> s2 = m2.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 2).collect(toList());
            List<SpecificRep> s3 = m2.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 3).collect(toList());
            List<SpecificRep> s4 = m2.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 5).collect(toList());

            InputStream in = ReportDocx.class.getResourceAsStream("/resource/report/Specific2.docx");
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);

            FieldsMetadata metadata = report.createFieldsMetadata();
            metadata.load("s1", SpecificRep.class, true);
            metadata.load("s2", SpecificRep.class, true);
            metadata.load("s3", SpecificRep.class, true);
            metadata.load("s4", SpecificRep.class, true);

            IContext context = report.createContext();
            context.put("num", num);
            context.put("date", date);
            context.put("total", total);
            context.put("s1", s1);
            context.put("s2", s2);
            context.put("s3", s3);
            context.put("s4", s4);

            OutputStream out = new FileOutputStream(new File(eProp.path_prop.read() + "/report.docx"));
            report.process(context, out);
            ExecuteCmd.startWord("report.docx");

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Нет доступа к файлу. Файл отчёта открыт другим приложением.", "ВНИМАНИЕ!", 1);
            System.err.println("Ошибка1:ReportDocx.Specific2()" + e);
        } catch (XDocReportException e) {
            System.err.println("Ошибка2:ReportDocx.Specific2()" + e);
        } catch (Exception e) {
            System.err.println("Ошибка3:ReportDocx.Specific2()" + e);
        }
    }
}
