package report;

import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;
import java.util.List;

public class ImageRep {
    
    private List<MappingRep> mappingList;
    private final IImageProvider picture;

    public ImageRep(IImageProvider picture, List<MappingRep> list) {
        this.picture = picture;
        this.mappingList = list;
    }

    public ImageRep(IImageProvider picture) {
        this.picture = picture;
    }

    @FieldMetadata(images = {@ImageMetadata(name = "picture")})
    public IImageProvider getPicture() {
        return picture;
    }
}
