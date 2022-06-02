package report;

import builder.Wincalc;
import builder.making.Joining;
import builder.making.Specific;
import common.eProp;
import dataset.Record;
import docx.DocxProjectWithFreemarkerAndImageList;
import static docx.DocxProjectWithFreemarkerAndImageList.toByteArray;
import docx.model.DeveloperWithImage;
import docx.model.Project;
import domain.eArtikl;
import domain.ePrjprod;
import domain.eProject;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
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
            System.err.println("Ошибка1:ReportDocx.Specific2()" + e);
        } catch (XDocReportException e) {
            System.err.println("Ошибка2:ReportDocx.Specific2()" + e);
        } catch (Exception e) {
            System.err.println("Ошибка3:ReportDocx.Specific2()" + e);
        }
    }

    public static void smeta2(List<Wincalc> wincs, Record record) {
        try {
            int length = 400;
            InputStream in = DocxProjectWithFreemarkerAndImageList.class.getResourceAsStream("/resource/report/Smeta2a.docx");
            OutputStream out = new FileOutputStream(new File(eProp.path_prop.read() + "/report.docx"));
            List<SmetaRep> imageList = new ArrayList<SmetaRep>();
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);            
            FieldsMetadata metadata = report.createFieldsMetadata();
            metadata.load("imageList", SmetaRep.class, true);
            IContext context = report.createContext();
            context.put("imageList", imageList);

            //for (Wincalc win : wincs) {
            Wincalc winc = new Wincalc(wincs.get(0).script());
            Joining joining = new Joining(winc, true);//заполним соединения из конструктива
            joining.calc();
            winc.imageIcon = Canvas.createIcon(winc, length);
            BufferedImage bi = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
            winc.gc2d = bi.createGraphics();
            winc.gc2d.fillRect(0, 0, length, length);
            float height = (winc.height1() > winc.height2()) ? winc.height1() : winc.height2();
            float width = (winc.width1() > winc.width2()) ? winc.width1() : winc.width2();
            winc.scale = (length / width > length / height) ? length / (height) : length / (width);
            winc.gc2d.scale(winc.scale, winc.scale);
            winc.rootArea.draw(); //рисую конструкцию
            ByteArrayImageProvider imageProvider = new ByteArrayImageProvider(toByteArray(bi));
            imageList.add(new SmetaRep("ZERR", imageProvider));

            report.process(context, out);
            ExecuteCmd.startWord("report.docx");
            //}
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
    }

    public static byte[] toByteArray(BufferedImage bi) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }
}

/*     
    //https://mkyong.com/java/how-to-convert-bufferedimage-to-byte-in-java/
    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }

    //http://www.java2s.com/example/java-utility-method/bufferedimage-to-byte-array-index-0.html
    public static byte[] toByteArray2(BufferedImage bi) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }
    
    //http://www.java2s.com/example/java-utility-method/bufferedimage-to-byte-array-index-0.html
    public static byte[] toByteArray2(BufferedImage bi, String format) {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, format, buff);
            byte[] bytes = buff.toByteArray();
            buff.close();
            return bytes;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
 */
