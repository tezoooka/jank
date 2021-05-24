package jp.co.jastec.jank.input;

import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.cli.InputRejectException;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveInput;
import jp.co.jastec.jank.input.InputEditors.Editor;
import jp.co.jastec.jank.model.JankModel;

public class Editing extends InteractiveInput {

    public Editing(JankModel model) {
        super(model);
    }

    @Override
    public String prompt() {
        return switchPromot() ;
    }

    @Override
    protected void updateModel(String line) {
        editor.edit(this.model, line) ;
    }

    private InputEditors.Editor editor;

    @Override
    protected String checkInputFormat(String line) throws InputRejectException {

        Editor editor = InputEditors.getEditor(line) ;
        
        if ( null == editor ) {
            reject(JankMessage.ILLEAGAL_INPUT_FORMAT, line);
        }

        if ( !editor.isValidCommand(model, line) ) {
            reject(JankMessage.ILLEAGAL_LINENO, line);
        }

        this.editor = editor;

        return line;
    }

    @Override
    public String helpText() {
        return InputEditors.getCommandGuideList();
    }

    @Override
    protected Interactive getNext() {
        Interactive forwarding = this.editor.forward(this.model) ;
        return forwarding == null ? this : forwarding ;
    }
    
    private String switchPromot() {
        final float wh = this.model.getWorkingHours();
        final float mh = this.model.getManHours();
        if ( mh != wh ) { 
            return  "実労働時間の工数の合計が合っていません。\n以下の編集作業が可能です。\n" + InputEditors.getCommandGuideList();
        } else {
            return  "入力内容を保存/終了するには、/q を入力してください。\n" +
                    "入力内容を修正するには、以下の編集作業が可能です。\n" + InputEditors.getCommandGuideList();
        }

    }
    
}
