package jp.co.jastec.jank.cli;

import java.util.ArrayList;
import java.util.List;

import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.cli.OptionSelecter.OptionSelectable;
import jp.co.jastec.jank.model.JankModel;

public abstract class InteractiveSelect<T> extends InteractiveInput implements OptionSelectable {

    // static final int MAX_LINELENGTH = 72;

    private OptionSelecter<T> optionSelecter = null;

    private boolean isMultiSelectable; 

    public InteractiveSelect(JankModel model, boolean multiSelectable) {
        super(model);
        this.isMultiSelectable = multiSelectable;
    }

    @Override
    public String prompt() {
        return getBasePrompt();
    }

    /** オプション選択器の生成 */
    protected abstract OptionSelecter<T> createOptionSelecter();

    /** 選択可能な項目の名称を返す */
    protected abstract String getBasePrompt() ;

    /** 選択済みの項目をモデルに反映する */
    protected abstract void updateModelWithOption(T selected);

    @Override
    protected String checkInputFormat(String line) throws InputRejectException {

        List<Option<T>> selected = getSelectedOptions(line);
        if ( null == selected) {
            reject(JankMessage.UNSELECTABLE_OPTION, line);
        }

        if ( !this.isMultiSelectable && selected.size() > 1) {
            reject(JankMessage.UNSELECTABLE_MULTI, line);
        }

        

        return line;
    }

    @Override
    protected void updateModel(String line) {
        
        for ( Option<T> opt : getSelectedOptions(line) ) {
            this.updateModelWithOption(opt.getValue());        
        }

        return ;

    }

    protected List<Option<T>> getSelectedOptions(String entered) {

        List<Option<T>> selectedOptions = new ArrayList<>();
        OptionSelecter<T> selecter = this.optionSelecter;

        if ( entered == null || entered.isBlank() ) {
            Option<T> defOption = selecter.getDefault();
            if ( defOption != null) {
                selectedOptions.add(defOption);
            } else {
                return null;
            }
            
        } else {

            for ( String index : entered.split(" ")) {
                Option<T> opt = selecter.get(index) ;
                if ( null == opt ) {
                    return null;
                }
                selectedOptions.add(opt) ;
            }
        }

        return selectedOptions;
    
    }

    @Override
    public OptionSelecter<T> getOptionSelecter() {
        if (this.optionSelecter == null) {
            this.optionSelecter = createOptionSelecter();
        }
        return this.optionSelecter;
    }
}
