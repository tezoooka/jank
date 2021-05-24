
package jp.co.jastec.jank.input;

import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.cli.InputRejectException;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveInput;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.header.Employee;

public class EmpNoInput extends InteractiveInput {

    public EmpNoInput(JankModel model) {
        super(model);
    }

    @Override
    public String prompt() {
        return "社員番号を入力してください。";
    }

    @Override
    protected String checkInputFormat(String line) throws InputRejectException {

        if (line.length() == 5 ) {
            return "0" + line;

        } else if ( line.length() == 6) {
            return line;
        
        } else {
            reject(JankMessage.EMPNO_ILLEGALFORMAT, line);
        }
        return line;

    }

    @Override
    public Interactive getNext() {
        return new DateInput(this.model);
    }


    @Override
    protected void updateModel(String line) {
        Employee emp = new Employee(line);
        this.model.setEmployee(emp);
    }
    
}
