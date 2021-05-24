package jp.co.jastec.jank.cli;

import jp.co.jastec.jank.cli.CliView.CliViewable;

public interface Interactive {

    public abstract String prompt() ;

    public abstract String retryMessage() ;

    public default String helpText() { return ""; }

    public CliViewable viewModel();

    public abstract Accepted accept(String line) ;
    
    public abstract Interactive next() ;

    public abstract Interactive closer() ;

    public enum Accepted {

        EXIT,
        RESTART,
        HELP,
        CHOICE,
        DONE,
        REJECT,
        VIEW,
        END,

    }

}
