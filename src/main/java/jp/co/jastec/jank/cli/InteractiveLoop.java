package jp.co.jastec.jank.cli;

import jp.co.jastec.jank.cli.Interactive.Accepted;
import jp.co.jastec.jank.cli.OptionSelecter.OptionSelectable;

/// 対話型アプリケーションのやりとりを実現するためのフレームワーク
/// このLoopから Interactiveクラスを呼び出すことで、様々なやりとりを実現する。
public class InteractiveLoop {

    static final String PROMPTING = "Jank> " ;
    static final String MORE_SELECT = "/" ;

    private Interactive interactive;
    private Interactive restartPoint;

    public InteractiveLoop(Interactive opening) {
        this.interactive = opening;
        this.restartPoint = opening;
    }


    public void start() {
        
        JankConsole.setup();

        while ( this.interactive != null ) {

            showPrompt();
            Accepted accepted = this.interactive.accept(readLine());

            if ( accepted == Accepted.DONE ) {
                Interactive nextInteractive = this.interactive.next() ;
                this.interactive = nextInteractive;
                showViewModel();

            } else if ( accepted == Accepted.END) {
                this.interactive = this.interactive.closer();

            } else if ( accepted == Accepted.RESTART) {
                this.interactive = this.restartPoint;

            } else if ( accepted == Accepted.HELP) {
                showHelp();

            } else if ( accepted == Accepted.VIEW) {
                showViewModel();

            } else if ( accepted == Accepted.EXIT) {
                break;


            } else {
                showMessage();
            }
        }

        JankConsole.close();
        
    }

    private String readLine() {

        String readLine = "" ; 

        if ( this.interactive instanceof OptionSelectable) {
            OptionSelectable optSelecter = (OptionSelectable) this.interactive;
            readLine = selectOptions(optSelecter);
        } else {
            readLine  = JankConsole.readLine() ;
        }
        return readLine;
    }

    private String selectOptions( OptionSelectable selectable ) {
        OptionSelecter<?> selecter = selectable.getOptionSelecter();
        String readLine = MORE_SELECT;
        while ( readLine.trim().equals(MORE_SELECT) ) {
            show(PROMPTING, JankConsole.Color.YELLOW, selecter.getOptionLines());
            readLine  = JankConsole.readLine() ;
            if ( !selecter.hasDefault() && readLine.isBlank()) {
                readLine = MORE_SELECT;
            }
        }
        return readLine;
    }



    private void show(String prefix, JankConsole.Color color, String text) {
        String[] lines = text.split("\n");
        JankConsole.writeLine(prefix, color, lines);
    }

    
    private void showPrompt() {
        show(PROMPTING, JankConsole.Color.NONE, this.interactive.prompt());
    }

    private void showMessage() {
        String text = this.interactive.retryMessage();
        show(PROMPTING, JankConsole.Color.YELLOW, text) ;
    }

    private void showHelp() {
        String text = this.interactive.helpText();
        show(PROMPTING, JankConsole.Color.GREEN, text) ;
    }

    private void showViewModel() {
        CliView view = this.interactive.viewModel().getView();
        String[] lines = view.toLines();
        JankConsole.writeLine("");
        JankConsole.writeLine("| ", JankConsole.Color.CYAN, lines);
        JankConsole.writeLine("");
    }
}
