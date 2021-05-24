package jp.co.jastec.jank.input;

import java.util.Arrays;

import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveSelect;
import jp.co.jastec.jank.cli.Option;
import jp.co.jastec.jank.cli.OptionSelecter;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.header.WorkingDateTime;
import jp.co.jastec.jank.model.header.WorkingPattern;
import jp.co.jastec.jank.model.header.WorkingPattern.DAY;

public class WorkingPatternInput extends InteractiveSelect<WorkingPattern.DAY> {

    private static OptionSelecter<DAY> chachedOptionSelecter = null;

    public WorkingPatternInput(JankModel model) {
        super(model, true);
    }


    @Override
    protected OptionSelecter<DAY> createOptionSelecter() {
        if ( chachedOptionSelecter == null ) {
            Option<WorkingPattern.DAY> day1 = new Option<WorkingPattern.DAY>("1", "1日目", WorkingPattern.DAY.FIRST, true);
            Option<WorkingPattern.DAY> day2 = new Option<WorkingPattern.DAY>("2", "2日目", WorkingPattern.DAY.SECOND, false);
            // Option<WorkingPattern.DAY> none = new Option<WorkingPattern.DAY>("空Enter", "なし", null);
            chachedOptionSelecter =  new OptionSelecter<>(Arrays.asList(day1, day2),2);
        }
        return chachedOptionSelecter;
    }

    @Override
    protected String getBasePrompt() {
        return "休日出勤ですね。お疲れ様です。勤務形態を入力してください。";
    }

    @Override
    protected void updateModelWithOption(WorkingPattern.DAY selected) {

        if ( selected != null) {
            WorkingDateTime wdt = this.model.getWorkingDateTime();
            WorkingPattern wp = wdt.getWorkingPattern();
            wp.set(selected);
            wdt.setWorkingPattern(wp);
        } 

    }

    @Override
    protected Interactive getNext() {
        return new TimeInput(this.model);
    }
    
}
