package jp.co.jastec.jank.model.header;

import com.google.gson.annotations.Expose;

import jp.co.jastec.jank.base.datetime.JankDate;
import jp.co.jastec.jank.base.datetime.JankTime;
import jp.co.jastec.jank.base.datetime.JankTimeRange;
import jp.co.jastec.jank.base.validation.JankValidatable;
import jp.co.jastec.jank.base.validation.ResultList;
import jp.co.jastec.jank.cli.CliView;
import jp.co.jastec.jank.cli.CliView.CliViewable;

/** 対象日・勤務時間・休暇・不就業 等々 */
public class WorkingDateTime implements JankTimeRange, CliViewable, JankValidatable {

    @Expose
    private JankDate baseDate = null ;
    
    @Expose
    private WorkingPattern workingPattern = new WorkingPattern();

    @Expose
    private JankTime startTime = null;
    @Expose
    private JankTime finishTime = null;

    @Expose
    private DayOffType dayOffType = null;

    @Expose
    private RestCheck restCheck = new RestCheck();

    @Expose
    private AbsenceTime absenceTime = new AbsenceTime();

    public WorkingDateTime() {
    }

    public void setBaseDate(JankDate baseDate) {
        this.baseDate = baseDate;
        validate();
    }

    public JankDate getBaseDate() {
        return baseDate;
    }

    public void setWorkingPattern(WorkingPattern workingPattern) {
        this.workingPattern = workingPattern;
    }

    public WorkingPattern getWorkingPattern() {
        return workingPattern;
    }

    public void setStartTime(JankTime startTime) {
        this.startTime = startTime;
        validate();
    }

    public JankTime getStartTime() {
        return startTime;
    }

    public void setFinishTime(JankTime finishTime) {
        this.finishTime = finishTime;
        validate();
    }

    public JankTime getFinishTime() {
        return finishTime;
    }

    public void setRestCheck(RestCheck restCheck) {
        this.restCheck = restCheck;
        validate();
    }

    public RestCheck getRestCheck() {
        return restCheck;
    }

    public void setAbsenceTime(AbsenceTime absenceTime) {
        this.absenceTime = absenceTime;
        validate();
    }

    public AbsenceTime getAbsenceTime() {
        return absenceTime;
    }

    public DayOffType getDayOffType() {
        return dayOffType;
    }

    public void setDayOffType(DayOffType dayOff) {
        this.dayOffType = dayOff;
    }

    public float getWorkingHours() {

        // 始業時刻・終了時刻が未入力の場合は、計算しない
        if ( startTime == null || finishTime == null) {
            return 0;
        }

        final float clockHours = JankTime.hoursBetween(startTime, finishTime);
        final float restHours = restCheck.getDeducationHours(startTime, finishTime);
        final float absenceHours = absenceTime.getDeducationHours();
        // 物理的な実時間 - 休憩時間 - 不就業時間
        return clockHours - restHours - absenceHours ;

    }


    /// JankTimeRangeの実装
    @Override public JankTime lower() { return this.startTime; }
    @Override public JankTime higher() { return this.finishTime; }

    
    @Override
    public CliView getView() {
        CliView view = new CliView();

        String dts = getDateTimeString();

        String hol = (dayOffType != null) ? dayOffType.getName() : "" ;

        view.add(dts + " " + hol);

        if (workingPattern != null) {
            view.addView(workingPattern);
        }
        if (absenceTime != null) {
            view.addView(this.absenceTime);
        }
        if (restCheck != null) {
            view.addView(this.restCheck);
        }
        view.add(String.format("実労働時間:   %.1f", this.getWorkingHours())) ;
        return view;
    }

    private String getDateTimeString() {
        String d = ( this.baseDate != null) ? this.baseDate.toStringWithDow() : "____/__/__(__）";
        String h = "";
        if ( this.baseDate.isHoliday() ) {
            h = this.baseDate.getCharacteristic().getName(); 
        }


        String s = ( this.startTime != null) ? this.startTime.toDisplayString() : "__:__";
        String f = ( this.finishTime != null) ? this.finishTime.toDisplayString() : "__:__";
        return "就業日・時間: " + d + h + " " + s + "-" + f ; 
    }

    @Override
    public ResultList validate() {
        // TODO 
        return null;
    }

    

}
