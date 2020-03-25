package enums;

import dataset.Field;
import java.text.ParseException;
import java.util.List;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;

public interface Enam {

    public String name();

    public int ordinal();

    default int numb() {
        return -1;
    }

    default String text() {
        return null;
    }

    default Field[] fields() {
        return null;
    }

    default List<String> dict() {
        return null;
    }

    default AbstractFormatterFactory format() {
        return null;
    }
}
