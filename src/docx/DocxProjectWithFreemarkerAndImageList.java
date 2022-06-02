package docx;

import builder.Wincalc;
import builder.making.Joining;
import dataset.Record;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import docx.model.DeveloperWithImage;
import docx.model.Project;
import domain.ePrjprod;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import frames.swing.draw.Canvas;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

public class DocxProjectWithFreemarkerAndImageList {

    public static void smeta2(Record record) {
        int length = 200;
        String script = record.getStr(ePrjprod.script);
        Wincalc winc = new Wincalc(script);
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
        ByteArrayImageProvider byteArrayImageProvider1 = new ByteArrayImageProvider(toByteArray(bi));
        ByteArrayImageProvider byteArrayImageProvider2 = new ByteArrayImageProvider(toByteArray(bi));
        

        try {

            InputStream in = DocxProjectWithFreemarkerAndImageList.class.getResourceAsStream("DocxProjectWithFreemarkerAndImageList.docx");
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);

            FieldsMetadata metadata = report.createFieldsMetadata();
            metadata.load("developers", DeveloperWithImage.class, true);

            metadata.addFieldAsImage("logo");

            IContext context = report.createContext();
            Project project = new Project("XDocReport");
            project.setURL("http://code.google.com/p/xdocreport/");
            context.put("project", project);
            IImageProvider logo = new ClassPathImageProvider(DocxProjectWithFreemarkerAndImageList.class, "logo.png");
            context.put("logo", logo);

            List<DeveloperWithImage> developers = new ArrayList<DeveloperWithImage>();
            developers.add(new DeveloperWithImage("ZERR", "Angelo", "angelo.zerr@gmail.com", byteArrayImageProvider1));
            developers.add(new DeveloperWithImage("Leclercq", "Pascal", "pascal.leclercq@gmail.com", byteArrayImageProvider2));
            context.put("developers", developers);

            OutputStream out = new FileOutputStream(new File("DocxProjectWithFreemarkerAndImageList_Out.docx"));
            report.process(context, out);

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
     
    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }    
}
