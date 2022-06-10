package report;

import builder.Wincalc;
import builder.making.Specific;
import common.MoneyInWords;
import common.UCom;
import common.eProp;
import dataset.Query;
import dataset.Record;
import domain.eArtikl;
import domain.eColor;
import domain.ePrjpart;
import domain.ePrjprod;
import domain.eProject;
import domain.eSysuser;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import frames.Specifics;
import frames.UGui;
import frames.swing.draw.Canvas;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.System.in;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ReportDocx {
    
    private static DecimalFormat df1 = new DecimalFormat("0.0");
    private static DecimalFormat df2 = new DecimalFormat("#0.00");
    
    public static void material2(List<Wincalc> wincList, String orderNum) {
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
            
            InputStream in = ReportDocx.class.getResourceAsStream("/resource/report/Material2.docx");
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
        } catch (XDocReportException e) {
            System.err.println("Ошибка2:ReportDocx.outGoMaterial()" + e);
        } catch (Exception e) {
            System.err.println("Ошибка3:ReportDocx.outGoMaterial()" + e);
        }
    }
    
    public static void specific2(List<Wincalc> wincList, Record record) {
        try {
            List<Specific> m = new ArrayList();
            for (Wincalc winc : wincList) {
                winc.constructiv(true);
                m.addAll(winc.listSpec);
            }
            List<SpecificRep> m2 = new ArrayList();
            m.forEach(el -> m2.add(new SpecificRep(el)));
            
            String num = record.getStr(eProject.num_ord);
            String date = UGui.simpleFormat.format(record.get(eProject.date4));
            String total = df1.format(m2.stream().mapToDouble(spc -> spc.getCost1()).sum());
            
            List<SpecificRep> s1 = m2.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 1).collect(toList());
            List<SpecificRep> s2 = SpecificRep.groups(m2.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 2).collect(toList()));
            List<SpecificRep> s3 = SpecificRep.groups(m2.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 3).collect(toList()));
            List<SpecificRep> s4 = SpecificRep.groups(m2.stream().filter(s -> s.spc().artiklRec.getInt(eArtikl.level1) == 5).collect(toList()));
            
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
        } catch (XDocReportException e) {
            System.err.println("Ошибка2:ReportDocx.Specific2()" + e);
        } catch (Exception e) {
            System.err.println("Ошибка3:ReportDocx.Specific2()" + e);
        }
    }
    
    public static void smeta2(Record order, Query prjprodList) {
        try {
            int npp = 0, length = 400;
            float sum1 = 0f, sum2 = 0f, total = 0f;
            InputStream in = ReportDocx.class.getResourceAsStream("/resource/report/Smeta2.docx");
            OutputStream out = new FileOutputStream(new File(eProp.path_prop.read() + "/report.docx"));
            List<SmetaRep> sketchList = new ArrayList<SmetaRep>();
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);
            FieldsMetadata metadata = report.createFieldsMetadata();
            metadata.load("sket", SmetaRep.class, true);
            
            List<Wincalc> wincList = wincList(prjprodList, length);
            for (int i = 0; i < wincList.size(); ++i) {
                Wincalc winc = wincList.get(i);
                Record prjprod = prjprodList.get(i);
                ByteArrayImageProvider imageProvider = new ByteArrayImageProvider(toByteArray(winc.bufferImg));
                String name = prjprod.getStr(ePrjprod.name);
                String color = eColor.find(winc.colorID1).getStr(eColor.name);
                String dimensions = winc.width() + "x" + winc.height();
                String num = prjprod.getStr(ePrjprod.num);
                String cost2 = df1.format(winc.cost2());
                sum1 += winc.cost2();
                sketchList.add(new SmetaRep(String.valueOf(++npp), name, color, dimensions, num, cost2, imageProvider));
            }
            IContext context = report.createContext();
            context.put("sket", sketchList);
            context.put("num", order.getStr(eProject.num_ord));
            context.put("sum1", df1.format(sum1));
            context.put("sum2", df1.format(sum2));
            context.put("total", df1.format(sum1 + sum2));
            report.process(context, out);
            ExecuteCmd.startWord("report.docx");
            
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Нет доступа к файлу. Файл отчёта открыт другим приложением.", "ВНИМАНИЕ!", 1);
            System.err.println("Ошибка1:ReportDocx.smeta2()" + e);
        } catch (XDocReportException e) {
            System.err.println("Ошибка2:ReportDocx.smeta2()" + e);
        } catch (IOException e) {
            System.err.println("Ошибка3:ReportDocx.smeta2()" + e);
        }
    }
    
    public static void smeta3(Record orderRec, Query prjprodList) {
        try {
            
            int length = 400, npp = 0;
            float sum1 = 0f, sum2 = 0f, sum3 = 0f, total = 0f;
            Record prjpartRec = ePrjpart.find(orderRec.getInt(eProject.prjpart_id));
            Record sysuserRec = eSysuser.find2(prjpartRec.getStr(ePrjpart.manager));
            InputStream in = ReportDocx.class.getResourceAsStream("/resource/report/Smeta4.docx");
            OutputStream out = new FileOutputStream(new File(eProp.path_prop.read() + "/report.docx"));
            List<ImageRep> pictureList = new ArrayList<ImageRep>();
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);
            FieldsMetadata metadata = report.createFieldsMetadata();
            metadata.load("pict", ImageRep.class, true);
            
            IContext context = report.createContext();
            context.put("num", orderRec.getStr(eProject.num_ord));
            context.put("date", UGui.simpleFormat.format(orderRec.get(eProject.date4)));
            context.put("name1", prjpartRec.getStr(ePrjpart.partner));
            context.put("phone1", prjpartRec.getStr(ePrjpart.addr_phone));
            context.put("email1", (prjpartRec.getInt(ePrjpart.flag2) == 1)
                    ? prjpartRec.getStr(ePrjpart.org_email) : prjpartRec.getStr(ePrjpart.addr_email));
            context.put("cont1", (prjpartRec.getInt(ePrjpart.flag2) == 1)
                    ? prjpartRec.getStr(ePrjpart.contact) : "");
            context.put("name2", sysuserRec.getStr(eSysuser.fio));
            context.put("phone2", sysuserRec.getStr(eSysuser.phone));
            context.put("email2", sysuserRec.getStr(eSysuser.email));
            
            List<Wincalc> wincList = wincList(prjprodList, length);
            for (int i = 0; i < wincList.size(); ++i) {
                
                Wincalc winc = wincList.get(i);
                Record prjprod = prjprodList.get(i);
                
                ByteArrayImageProvider imageProvider = new ByteArrayImageProvider(toByteArray(winc.bufferImg));
                String name = prjprod.getStr(ePrjprod.name);
                String color = eColor.find(winc.colorID1).getStr(eColor.name);
                String dimensions = winc.width() + "x" + winc.height();
                String num = prjprod.getStr(ePrjprod.num);
                String cost2 = df1.format(winc.cost2());
                sum1 += winc.cost2();               
                pictureList.add(new ImageRep(imageProvider));
            }                             
            context.put("pict", pictureList);
            
            
            context.put("total", "12567.89");
            context.put("tota2", UCom.firstUpperCase(MoneyInWords.inwords(12567.89)));
            report.process(context, out);
            ExecuteCmd.startWord("report.docx");
            
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Нет доступа к файлу. Файл отчёта открыт другим приложением.", "ВНИМАНИЕ!", 1);
        } catch (XDocReportException e) {
            System.err.println("Ошибка2:ReportDocx.smeta3()" + e);
        } catch (IOException e) {
            System.err.println("Ошибка3:ReportDocx.smeta3()" + e);
        }
    }
    
    private static List<Wincalc> wincList(Query prjprodList, int length) {
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
