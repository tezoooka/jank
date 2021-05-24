package jp.co.jastec.jank.cli;

public class Option<T> {

    String index ;
    String caption ;
    boolean isDefault = false ;

    T value ;
    // public static Option<?> NOTHONG_SELECTED = new Option<>("", "（選択しない）", null);

    public Option(String index, String caption, T value) {
        this(index, caption, value, false) ;
    }

    public Option(String index, String caption, T value, boolean isDefault ) {
        this.index = index ;
        this.caption = caption ;
        this.value = value;
        this.isDefault = isDefault;
    }

    public String getDisplayString() {
        final String defaultMarking = isDefault ? ">" : "";
        return String.format(" >%S%s %s ", defaultMarking, index, caption) ;
    }

    public T getValue() {
        return value;
    }

    public boolean isDefault() {
        return isDefault;
    }

    // @SuppressWarnings("unchecked")
    // public Option<T> nothing() {
    //     return (Option<T>) Option.NOTHONG_SELECTED;
    // }
    

}