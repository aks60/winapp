package report;

import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;

public class SmetaRep {

    private final String npp;
    private final String name;
    private final String color;
    private final String dimensions;
    private final String num;
    private final String cost2;
    private final IImageProvider picture;

    public SmetaRep(String npp, String name, String color, String dimensions, String num, String cost2, IImageProvider picture) {
        this.npp = npp;
        this.name = name;
        this.color = color;
        this.dimensions = dimensions;
        this.picture = picture;
        this.num = num;
        this.cost2 = cost2;
    }

    @FieldMetadata(images = {@ImageMetadata(name = "picture")})
    public IImageProvider getPicture() {
        return picture;
    }

    public String getNpp() {
        return npp;
    }
          
    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getNum() {
        return num;
    }

    public String getCost2() {
        return cost2;
    }
    
}
