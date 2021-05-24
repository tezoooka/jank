package jp.co.jastec.jank.input;

import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.base.datetime.JankTimeRange;
import jp.co.jastec.jank.cli.InputRejectException;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveInput;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.header.WorkingDateTime;

public class TimeInput extends InteractiveInput {

    private boolean isAbsenceDay = false ;

    public TimeInput(JankModel model) {
        super(model);
    }

    @Override
    public String prompt() {
        return "始業時刻-終業時刻を入力してください。\n" + 
            "9:00-18:00の場合は 空Enter を押してください\n" +
            "休暇・欠勤の場合は、k を入力してください。";
    }

    @Override
    protected String defaultInput() {
        return "9:00-18:00" ;
    }

    @Override
    protected void updateModel(String line) {

        if ( this.isAbsenceDay ) {
            return;
        }
        
        WorkingDateTime wdt = this.model.getWorkingDateTime();
        JankTimeRange jtr = JankTimeRange.of(line) ;
        wdt.setStartTime(jtr.lower());
        wdt.setFinishTime(jtr.higher());
        
    }

    @Override
    protected String checkInputFormat(String line) throws InputRejectException {
        if ("K".equals(line)) {
            this.isAbsenceDay = true ;
            return line;
        }

        if ( !JankTimeRange.isValidFormat(line)) {
            reject(JankMessage.DATETIME_ILLEGALFORMAT);
        }

        return line;
    }

    @Override
    protected Interactive getNext() {
        if ( isAbsenceDay ) {
            return new DayOffInput(this.model) ;
        } else {
            return new AbsenceTimeInput(this.model);
        }
    }
    
}
