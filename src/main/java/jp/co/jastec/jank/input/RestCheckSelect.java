package jp.co.jastec.jank.input;

import java.util.ArrayList;
import java.util.List;

import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveSelect;
import jp.co.jastec.jank.cli.Option;
import jp.co.jastec.jank.cli.OptionSelecter;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.header.RestCheck;
import jp.co.jastec.jank.model.header.RestCheck.RestCheckItem;
import jp.co.jastec.jank.model.header.WorkingDateTime;

public class RestCheckSelect extends InteractiveSelect<RestCheckItem> {

    public static Option<RestCheckItem> RESTCHECK_NONE = new Option<RestCheckItem>("空Enter", "なし", null, true) ;

    public RestCheckSelect(JankModel model) {
        super(model, true);
    }

    @Override
    protected String getBasePrompt() {
        return "休憩控除チェックがあれば、入力してください。複数の場合は空白で区切って入れられます。";
    }

    @Override
    protected void updateModelWithOption(RestCheckItem selected) {

            WorkingDateTime wdt = this.model.getWorkingDateTime();
            RestCheck restCheck = wdt.getRestCheck() ;
            restCheck.check(selected);

    }


    @Override
    protected Interactive getNext() {
        return new StartDetailInput(this.model);
    }


    @Override
    protected OptionSelecter<RestCheckItem> createOptionSelecter() {

        final List<Option<RestCheckItem>> options = new ArrayList<>();
        options.add(RESTCHECK_NONE) ;
        int i = 1;
        for ( RestCheckItem rci : RestCheck.itemList()) {
            final String caption = String.format("%s (%s-%s)", 
                        rci.getDispString(),
                        rci.lower().toDisplayString(),
                        rci.higher().toDisplayString()
            );
            final String index = Integer.toString(i++);
            final Option<RestCheckItem> opt = new Option<RestCheckItem>(index, caption, rci);
            options.add(opt) ;
        }
        return new OptionSelecter<RestCheckItem>(options, 5);
        
    }
    
}

