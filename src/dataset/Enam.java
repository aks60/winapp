package dataset;

public interface Enam {

    public String name();

    public int ordinal();

    default Object key() {
        return null;
    }

    default String val() {
        return null;
    }
    
    default Field[] fields() {
        return null;
    }

    default Enam[] enams() {
        return fields();
    }
}
