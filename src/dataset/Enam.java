package dataset;

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

    default Enam[] enams() {
        return fields();
    }

    default Record newRecord(String up) {
        Record record = new Record();
        for (Field field : fields()) {
            record.add(null);
        }
        record.set(0, up);
        return record;
    }
}
