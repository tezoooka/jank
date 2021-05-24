package jp.co.jastec.jank.input;

import java.io.File;

import jp.co.jastec.jank.cli.CliView.CliViewable;
import jp.co.jastec.jank.cli.Dialog;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.JankConsole;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.service.JankArchiver;

public class Closing implements Interactive {

    private final JankModel model;
    private final Interactive replay;
    private Interactive next = this;

    public Closing(JankModel model, Interactive replay) {
        this.model = model;
        this.replay = replay;
    }
    
    @Override
    public String prompt() {
        return "入力内容を保存して終了しますか？ Yes/No/CANcel";
    }

    @Override
    public Accepted accept(String line) {

        final String answer = line.trim();
        
        if ( isYes(answer)) {

            /// ここでmodel全体の整合チェックをする

            /// 本来ならHTTPでPOSTするなり、DBに書き込むなりするところ
            File saved = new JankArchiver().save(model);
            String msg = String.format("%s に保存しました。", saved.getAbsolutePath());
            JankConsole.writeLine(msg);
            return Accepted.EXIT;

        } 

        if ( isNo(answer) ) {
            if ( Dialog.askYesNo("入力内容を破棄して終了してよいですか？").isYes() ) {
                return Accepted.EXIT;
            } else {
                this.next = this.replay;
                return Accepted.DONE;
            }
        } 

        if ( isCancel(answer) ) {
            this.next = this.replay;
            return Accepted.DONE;
        } 

        return Accepted.REJECT;
        
    }

    private static boolean isYes(String answer) {
        return answer.equalsIgnoreCase("yes")||answer.equalsIgnoreCase("y");
    }

    private static boolean isNo(String answer) {
        return answer.equalsIgnoreCase("no")||answer.equalsIgnoreCase("n");
    }

    private static boolean isCancel(String answer) {
        return answer.equalsIgnoreCase("cancel")||answer.equalsIgnoreCase("can");
    }

 
    @Override
    public Interactive next() {
        return next;
    }

    @Override
    public String retryMessage() {
        return "";
    }

    @Override
    public CliViewable viewModel() {
        return this.model;
    }

    @Override
    public Interactive closer() {
        return this;
    }
}
