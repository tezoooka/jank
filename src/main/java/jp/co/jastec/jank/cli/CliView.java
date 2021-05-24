package jp.co.jastec.jank.cli;

import java.util.ArrayList;
import java.util.Collection;

public class CliView extends ArrayList<String> {
    
    public static final String HR = "------------------------------------------------------------------------------";

    public CliView() {
        super();
    }

    public CliView(Collection<String> strings) {
        super(strings);
    }

    public CliView(CharSequence viewString) {
        super();
        this.add(viewString.toString());
    }

    public void addView(CliViewable viewable) {
        if ( viewable != null) {
            this.add(viewable.getView());
        }
    }

    public String[] toLines() {
        return this.toArray(new String[0]) ;
    }


    @Override
    public boolean add(String e) {
        if (e != null) {
            super.add(e);
        }
        return true;
    }

    public boolean add(String[] lines) {
        for (String line : lines) add(line) ;
        return true;
    }

    private boolean add(CliView view) {
        if ( view != null) {
            this.addAll(view);
        }
        return true;
    }


    public static interface CliViewable {
        public CliView getView() ;
    }

    public static interface CliViewString {
        public String getViewString() ;
    }

}
