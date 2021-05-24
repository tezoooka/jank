package jp.co.jastec.jank.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.jastec.jank.base.JankCodeElement;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveSelect;
import jp.co.jastec.jank.cli.Option;
import jp.co.jastec.jank.cli.OptionSelecter;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.detail.Practice;
import jp.co.jastec.jank.service.PracticeMaster;

public class PracticeSelect extends InteractiveSelect<String> {

    private String practiceCode = null;

    public PracticeSelect(JankModel model) {
        super(model, false);
    }

    @Override
    protected OptionSelecter<String> createOptionSelecter() {
        
        final String myEmpNo = this.model.getEmployee().code();
        final Map<String, String> praMap = PracticeMaster.table().selectMyPractice(myEmpNo);
        final List<Option<String>> optList = new ArrayList<>();

        int index = 1;
        for (Map.Entry<String, String> kv : praMap.entrySet()) {
            final String praCode = kv.getKey();
            final String praName = kv.getValue();
            final String caption = JankCodeElement.fixing(praCode, 4, praName, 12);
            optList.add(new Option<String>(String.valueOf(index++), caption, praCode));
        }
        return new OptionSelecter<>(optList, 4);
    }


    @Override
    protected String getBasePrompt() {
        return "活動コードを選択してください";
    }

    @Override
    protected void updateModelWithOption(String selected) {
        if ( selected == null ) {
            return ;
        }
        this.practiceCode = selected;
        final Practice newPractice = new Practice(selected) ;
        this.model.getDetails().addPractice(newPractice);
    }

    @Override
    protected Interactive getNext() {
        if ( this.practiceCode == null ) {
            return this;
        } else {
            return new ProjectSelect(this.model, this.practiceCode) ;
        }
    }
    
}
