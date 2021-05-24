package jp.co.jastec.jank.input;

import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.base.datetime.JankTime;
import jp.co.jastec.jank.cli.InputRejectException;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveInput;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.header.DayOffType;
import jp.co.jastec.jank.model.header.WorkingDateTime;

public class HalfDayOffInput extends InteractiveInput {

    private DayOffType dayOffType;

    public HalfDayOffInput(JankModel model, DayOffType dayOffTye) {
        super(model);
        this.dayOffType = dayOffTye;
    }

    @Override
    public String prompt() {
        final String halfTime = JankTime.HALF_HOLIDAY_POINT.toDisplayString();
        if ( dayOffType == DayOffType.AM_ABSENCE) {
            return String.format("始業時刻は %s ですね。終業時刻を入力してください。", halfTime);

        } else {
            return String.format("始業時刻を入力してください。終業時刻は %s ですね。", halfTime);
        }
    }

    @Override
    protected void updateModel(String line) {

        JankTime inputTime = new JankTime(line);
        JankTime startTime;
        JankTime finishTime;
        if ( dayOffType == DayOffType.AM_ABSENCE) {
            startTime = JankTime.HALF_HOLIDAY_POINT;
            finishTime = inputTime;

        } else {
            startTime = inputTime;
            finishTime = JankTime.HALF_HOLIDAY_POINT;
        }
        WorkingDateTime wtd =  this.model.getWorkingDateTime();
        wtd.setStartTime(startTime);
        wtd.setFinishTime(finishTime);
    }

    @Override
    protected String checkInputFormat(String line) throws InputRejectException {
        if ( ! this.dayOffType.isHalf() ) {
            reject(JankMessage.NOT_A_HALFHOLIDAY);
        }

        return line;
        
    }

    @Override
    protected Interactive getNext() {
        return new AbsenceTimeInput(this.model);
    }
    
}
