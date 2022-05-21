package report;

import builder.Wincalc;
import builder.making.Specific;
import common.eProp;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import frames.Specifics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ReportDocx {

    public static void outGoMaterial(List<Wincalc> wincList, String orderNum) {
        try {
            List<Specific> spcList = new ArrayList();
            for (Wincalc winc : wincList) {
                winc.constructiv(true);
                spcList.addAll(winc.listSpec);
            }
            List<Specific> spcList2 = Specifics.groups(spcList, 3);
            
//          InputStream in = Main.class.getResourceAsStream("/resource/template/OutGoMaterial.docx");
            InputStream in = new FileInputStream(eProp.path_prop.read() + "/OutGoMaterial.docx");
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);

            FieldsMetadata metadata = report.createFieldsMetadata();
            metadata.load("spc", Specific.class, true);         

            IContext context = report.createContext();
            context.put("orderNum", orderNum);
            context.put("spc", spcList2);

            OutputStream out = new FileOutputStream(new File(eProp.path_prop.read() + "/report.docx"));
            report.process(context, out);
            ExecuteCmd.startWord("report.docx");

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Нет доступа к файлу. Файл отчёта открыт другим приложением.", "ВНИМАНИЕ!", 1);
            System.err.println("Ошибка:Tex.btnTest()" + e);
        } catch (Exception e) {
            System.err.println("Ошибка:Tex.btnTest()" + e);
        }
    }
}
