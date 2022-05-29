package report;

import builder.Wincalc;
import builder.making.Specific;
import common.eProp;
import dataset.Record;
import domain.eArtikl;
import domain.eProject;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import frames.Specifics;
import frames.UGui;
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
        Wincalc winc = wincs.get(0);
        int length = 777;
        BufferedImage bi = new BufferedImage(80, 80, BufferedImage.TYPE_INT_RGB);
        winc.gc2d = bi.createGraphics();
        float height = (winc.height1() > winc.height2()) ? winc.height1() : winc.height2();
        float width = (winc.width1() > winc.width2()) ? winc.width1() : winc.width2();
        winc.scale = (length / width > length / height) ? length / (height + 200) : length / (width + 200);
        winc.rootArea.draw(); //рисую конструкцию
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayImageProvider byteArrayImageProvider = new ByteArrayImageProvider(baos.toByteArray());

    }

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
}

//        Blob blob = rs.getBlob("BPICT");
//        int blobLength = (int) blob.length();
//        byte[] bytes = blob.getBytes(1, blobLength);
//        blob.free();
//        BufferedImage img = ImageIO.read(new java.io.ByteArrayInputStream(bytes));
//        ImageIcon icon = new ImageIcon(img);
//            BufferedImage bi = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
//            winc.gc2d = bi.createGraphics();
//            winc.gc2d.fillRect(0, 0, length, length);
//            float height = (winc.height1() > winc.height2()) ? winc.height1() : winc.height2();
//            float width = (winc.width1() > winc.width2()) ? winc.width1() : winc.width2();
//            winc.scale = (length / width > length / height)
//                    ? length / (height + 200) : length / (width + 200);            
//            winc.gc2d.scale(winc.scale, winc.scale);
//            winc.rootArea.draw(); //рисую конструкцию
//            return new ImageIcon(bi);
/**
 * private IImageProvider getImage(String key, Object object) { try { String
 * imageAsString = ""; if (object instanceof String) { imageAsString = (String)
 * object; } else { imageAsString = (String) ((JSONObject)
 * object).get(IMAGE_TYPE); } return new
 * ByteArrayImageProvider(Base64Utility.decode(imageAsString)); } catch
 * (Exception e) { throw new JSONException("JSONObject[" + quote(key) + "] is
 * not an Image."); } }
 *
 *
 * FieldsMetadata metadata = new FieldsMetadata();
 * metadata.addFieldAsImage("chart1"); metadata.addFieldAsImage("chart2");
 * report.setFieldsMetadata(metadata);
 *
 * IImageProvider logo = new FileImageProvider(new File("path/to/image1"),
 * true); context.put("chart1", logo);
 *
 * IImageProvider logo2 = new FileImageProvider(new File("path/to/image2"),
 * true); context.put("chart2", logo2);
 */
