package jp.co.jastec.jank.input;

import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveInput;
import jp.co.jastec.jank.model.JankModel;

public class StartDetailInput extends InteractiveInput {

    public StartDetailInput(JankModel model) {
        super(model);
    }

    @Override
    public String prompt() {
        float h = this.model.getWorkingDateTime().getWorkingHours();
        return String.format("実労働時間は %.1f でした。\n"+
                            " 続けて、詳細な活動～プロジェクト情報を入力します。" +
                            "\n Enterキーを推してください。", h);
    }

    @Override
    protected void updateModel(String line) {
        
        // JankList<Practice> practiceList = new JankList<>();
        // this.model.setPractices(practiceList);
        
    }

    @Override
    protected String checkInputFormat(String line) {
        return line;
    }

    @Override
    protected Interactive getNext() {
        // return new DetailOneLineInput(this.model);
        return new PracticeSelect(this.model);
    }

}
