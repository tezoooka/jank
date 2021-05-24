package jp.co.jastec.jank.cli;

import jp.co.jastec.jank.model.JankModel;

@Deprecated
public class ModelViewer extends InteractiveInput {

    public ModelViewer(JankModel model) {
        super(model);
    }

    @Override
    public String prompt() {
        return "";
    }

    @Override
    protected String defaultInput() {

        return "/v" ;
    }

    @Override
    protected void updateModel(String line) {
    }

    @Override
    protected String checkInputFormat(String line) {
        return line;
    }

    @Override
    protected Interactive getNext() {
        return this;
    }
    
}
