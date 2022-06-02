package report;

import docx.model.*;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;

public class SmetaRep {

    private final String name;
    private final String color;
    private final String dimensions;
    private final IImageProvider picture;

    public SmetaRep(String name, String color, String dimensions, IImageProvider picture) {
        this.name = name;
        this.color = color;
        this.dimensions = dimensions;
        this.picture = picture;
    }

    @FieldMetadata(images = {@ImageMetadata(name = "picture")})
    public IImageProvider getPicture() {
        return picture;
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
}
