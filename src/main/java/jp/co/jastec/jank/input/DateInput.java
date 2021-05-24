package jp.co.jastec.jank.input;

import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.base.datetime.JankDate;
import jp.co.jastec.jank.cli.InputRejectException;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveInput;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.header.WorkingDateTime;

public class DateInput extends InteractiveInput {

    public DateInput(JankModel model) {
        super(model);
    }

    @Override
    public String prompt() {
        return "日付を入力してください。今日日付の場合は 空Enter を押してください";
    }

    @Override
    protected String defaultInput() {
        return new JankDate().toString();
    }

    @Override
    protected void updateModel(String line) {
        WorkingDateTime wdt = this.model.getWorkingDateTime();
        if ( wdt == null) {
            wdt = new WorkingDateTime();
        }
        wdt.setBaseDate(new JankDate(line));
        this.model.setWorkingTime(wdt);
        
    }

    @Override
    protected String checkInputFormat(String line) throws InputRejectException{
        if ( null == JankDate.DateString.check(line) ){
            reject(JankMessage.DATETIME_ILLEGALFORMAT, line);
        } 
        return line;
    }

    @Override
    protected Interactive getNext() {
        if (this.model.getWorkingDateTime().getBaseDate().isHoliday()) {
            return new WorkingPatternInput(this.model);
        } else {
            return new TimeInput(this.model);
        }
    }    
}
