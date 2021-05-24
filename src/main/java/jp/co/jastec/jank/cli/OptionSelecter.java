package jp.co.jastec.jank.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OptionSelecter<T> extends ArrayList<Option<T>> {

    private Option<T> defaultOption;
    private final int numOfViewLines ;

    private int viewPoint = 0 ;

    public OptionSelecter( int numOfViewLines ) {
        super();
        this.numOfViewLines = numOfViewLines;
    }

    public OptionSelecter(List<Option<T>> options) {
        this(options, 5);
    }

    public OptionSelecter(List<Option<T>> options, int numOfViewLine ) {
        super(options);
        this.numOfViewLines = numOfViewLine;
        for (Option<T> opt : this) {
            if (opt.isDefault) {
                assert ( this.defaultOption == null ) : "Default option must be one " ;
                this.defaultOption = opt;
            }
        }
    }

    public Option<T> get(String index) {
        return 
        this.stream().filter(o-> o.index.equalsIgnoreCase(index)).findAny().orElse(null);
    }

    public Option<T> getDefault() {
        return defaultOption;
    }

    public boolean hasDefault() {
        return defaultOption != null;
    }

    public String getOptionLines() {

        // 今回表示する範囲
        final int startLine = viewPoint;
        final int endLine = Math.min(startLine + numOfViewLines, this.size()) ;
        final String optionLines = this.subList(startLine, endLine)
                            .stream()
                            .map( o -> o.getDisplayString())
                            .collect(Collectors.joining("\n"));
        
        // 継続行があるか？
        final String more =  ( endLine < this.size() ) ? "...more" : "" ;
        
        // 次回表示する部分
        this.viewPoint = ( more.isEmpty() ) ? 0 : endLine;

        return optionLines + more ;

    }

    public interface OptionSelectable {
        OptionSelecter<?> getOptionSelecter();
    } 
    
}