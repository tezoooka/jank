package jp.co.jastec.jank.input;

import java.util.ArrayList;
import java.util.List;

import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveSelect;
import jp.co.jastec.jank.cli.Option;
import jp.co.jastec.jank.cli.OptionSelecter;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.header.DayOffType;

public class DayOffInput extends InteractiveSelect<DayOffType> {

    static final List<Option<DayOffType>> DAYOFF_OPTIONS = new ArrayList<>() ;
    static {
        for ( DayOffType dof : DayOffType.values()) {
            final String index = String.valueOf(dof.ordinal()+1) ;
            final String caption = dof.getName() ;
            final boolean isDefault = dof == DayOffType.PAID_HOLIDAY ;
            Option<DayOffType> opt =  
                new Option<DayOffType>(index, caption, dof, isDefault); 
            DAYOFF_OPTIONS.add(opt);
        }
    }

    public DayOffInput(JankModel model) {
        super(model, false);
    }

    @Override
    protected OptionSelecter<DayOffType> createOptionSelecter() {
        return new OptionSelecter<>(DAYOFF_OPTIONS, 5);
    }

    @Override
    protected String getBasePrompt() {
        return "休暇・欠勤の種別を入力してください。";
    }

    @Override
    protected void updateModelWithOption(DayOffType selected) {
        this.model.getWorkingDateTime().setDayOffType(selected);
    }

    @Override
    protected Interactive getNext() {

        DayOffType off = this.model.getWorkingDateTime().getDayOffType();
        if ( off == DayOffType.AM_ABSENCE || off == DayOffType.PM_ABSENCE) {
            return new HalfDayOffInput(this.model, off) ;
        } else {
            return new Closing(this.model, this);
        }
    }
    
}
