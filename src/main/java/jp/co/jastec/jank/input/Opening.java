
package jp.co.jastec.jank.input;

import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveInput;
import jp.co.jastec.jank.model.JankModel;

public class Opening extends InteractiveInput {

    public Opening(JankModel model) {
        super(model);
    }

    @Override
    public String prompt() {
        return 
            "/*---------------------------------------------\n" +
            "  Hello - JAstec NiKkei entry - JANK\n" +
            "----------------------------------------------*/\n" +
            "Press Enter to contnue.";
    }

    @Override
    public Interactive getNext() {
        return new EmpNoInput(this.model);
    }

    @Override
    public String retryMessage() {
        return "";
    }

    @Override
    protected void updateModel(String line) {
        return;
    }

    @Override
    protected String checkInputFormat(String line) {
        return line;
    }

}
