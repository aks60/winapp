package report;

import docx.model.*;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;

public class DeveloperImage extends Developer {

    private final IImageProvider photo;

    public DeveloperImage(String name, String lastName, String mail, IImageProvider photo) {
        super(name, lastName, mail);
        this.photo = photo;
    }

    @FieldMetadata(images = {@ImageMetadata(name = "photo")})
    public IImageProvider getPhoto() {
        return photo;
    }

}
