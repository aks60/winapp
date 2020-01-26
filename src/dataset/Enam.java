package dataset;

public interface Enam {

    public String name();

    public int ordinal();

    default Field[] fields() {
        return null;
    }
    
    default Enam[] enams() {
        return fields();
    }
}
