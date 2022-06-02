package report;

import docx.model.*;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;

public class SmetaRep {

    private final String name;
    private final IImageProvider photo;

    public SmetaRep(String name, IImageProvider pfoto) {
        this.name = name;
        this.photo = pfoto;
    }

    @FieldMetadata(images = {@ImageMetadata(name = "photo")})
    public IImageProvider getPhoto() {
        return photo;
    }
          
    public String getName() {
        return name;
    }
}
