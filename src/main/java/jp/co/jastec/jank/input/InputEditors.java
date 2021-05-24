package jp.co.jastec.jank.input;

import java.util.regex.Pattern;

import jp.co.jastec.jank.cli.Dialog;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.detail.Practice;
import jp.co.jastec.jank.model.detail.Project;
import jp.co.jastec.jank.model.detail.Task;

public class InputEditors {

    private static String commandGuide = null;

    private static Editor[] EDITORS = {
        new ChangeHourEdior(),
        new DeleteTaskEditor(),
        new DeleteProjectEditor(),
        new DeletePracticeEditor(),
        new ReEntryWorkTimeEditor(),
        new AddDetailLineEditor(),
    };

    // インスタンス化しない
    private InputEditors() {}

    /** サブコマンドに応じた個別のEditorを返す */
    public static Editor getEditor(String inputString) {
        for (Editor e : EDITORS) {
            if (inputString.startsWith(e.getCommandPrefix().toUpperCase())) {
                return e;
            }
        }
        return null;
    }

    // サブコマンドの一覧とガイド文言を返す
    public static String getCommandGuideList() {
        if (commandGuide == null) {
            StringBuilder sb = new StringBuilder();
            for (Editor e : EDITORS) {
                sb  .append(e.getCommandGuidance())
                    .append("\n") 
                ;
            }
            sb.append(" n は行番号を示します");
            commandGuide = sb.toString();
        }
        return commandGuide;
    }

    // 個々の編集機能を示すインターフェース
    public interface Editor {
        /** サブコマンドのプリフィックス文字列 */
        String getCommandPrefix() ;
        /** ガイド文言 */
        String getCommandGuidance();
        /** 入力内容を検査する */
        boolean isValidCommand(JankModel model, String inpuString);
        /** 実際にモデルを編集する */
        public void edit(JankModel model, String inputString);
        public Interactive forward(JankModel model) ;
    }

    // 行番号と見なせる正規表現
    private static final Pattern P_LINENO = Pattern.compile("^[1-9][0-9]?$");

    private static abstract class AbstractLineNoEditor implements Editor {

        @Override
        public String getCommandGuidance() {
            return getSimpleGuidance() + getCommandPrefix() + " n";
        }

        public abstract String getSimpleGuidance();


        @Override
        public boolean isValidCommand(JankModel model, String inputString) {

            final String[] params = inputString.split(" ") ;
            
            // コマンド文字列と行番号に分かれてるか
            if ( params.length != 2 ) {
                return false;
            }
    
            // コマンド文字列が合致するか
            if (! params[0].equalsIgnoreCase(getCommandPrefix())){
                return false;
            }
    
            // 行番号らしき数字か
            if ( ! P_LINENO.matcher(params[1]).matches())  {
                return false;
            }
    
            // 実在する行番号か
            if( null == model.getDetails().getByLineNo(getLineNo(inputString)) ){
                return false;
            }

            return true ;
        }

        @Override
        public abstract void edit(JankModel model, String inputString) ;

        @Override
        public Interactive forward(JankModel model) {
            return null;
        }

        protected int getLineNo(String inputString) {
            return Integer.parseInt(inputString.split(" ")[1]);
        }
    }


    /** Taskに設定されている工数部分を書き換える */
    public static class ChangeHourEdior extends  AbstractLineNoEditor {

        @Override 
        public String getCommandPrefix() { return "/ch";} ;

        @Override
        public String getSimpleGuidance() { return " 入力済みの工数を修正する   Change Hour ..." ;}

        @Override
        public void edit(JankModel model, String inputString) {

            final int lineNo = getLineNo(inputString);
            final float hours = Dialog.askNumeric("修正する工数を入力してください").getNumericValue() ;
            // 0.5刻みのチェックは省略
            final Task task = model.getDetails().getTaskByLineNo(lineNo) ;
            assert(task!=null);
            task.setHours(hours);

        }
    }

    public static class  DeleteTaskEditor extends AbstractLineNoEditor {

        @Override
        public String getCommandPrefix() { return "/dt";}

        @Override
        public String getSimpleGuidance() { return " 工程作業項目を削除　　　   Delete Task ..." ;}

        @Override public void edit(JankModel model, String inputString) {
            final int lineNo = getLineNo(inputString);
            Task task = model.getDetails().getTaskByLineNo(lineNo) ;
            if ( task!=null) {
                task.removeMySelf();
            }
        }

    }


    public static class DeleteProjectEditor extends AbstractLineNoEditor {

        @Override
        public String getCommandPrefix() {return "/dp"; }

        @Override
        public String getSimpleGuidance() {return " プロジェクト全体を削除　Delete Project ..."; }

        @Override
        public void edit(JankModel model, String inputString) {
            final int lineNo = getLineNo(inputString);
            Project project = model.getDetails().getProjectByLineNo(lineNo) ;
            if ( project!=null) {
                project.removeMySelf();
            }
        }
        
    }

    public static class DeletePracticeEditor extends AbstractLineNoEditor {

        @Override
        public String getCommandPrefix() { return "/dk"; }

        @Override
        public String getSimpleGuidance() {return " 活動コード全体を削除  　Delete Katsudo ...";}

        @Override
        public void edit(JankModel model, String inputString) {
            final int lineNo = getLineNo(inputString);
            Practice practice = model.getDetails().getPracticeByLineNo(lineNo) ;
            if ( practice != null) {
                practice.removeMySelf();
            }
        }
        
    }


    public static class ReEntryWorkTimeEditor implements Editor {

        @Override
        public String getCommandPrefix() {
            return "/re";
        }

        @Override
        public String getCommandGuidance() {
            return " 就業管理情報を再入力  Reentry WorkTime ..." + getCommandPrefix();
        }

        @Override
        public boolean isValidCommand(JankModel model, String inpuString) {
            return true;
        }

        @Override
        public void edit(JankModel model, String inputString) {
            // nothing            
        }

        @Override
        public Interactive forward(JankModel model) {
            return new DateInput(model);
        }

    }

    public static class AddDetailLineEditor implements Editor {

        @Override
        public String getCommandPrefix() {
            return "/ad";
        }

        @Override
        public String getCommandGuidance() {
            return " 明細行と工数を追加     ADd Detail Line ..." + getCommandPrefix();
        }

        @Override
        public boolean isValidCommand(JankModel model, String inpuString) {
            return true;
        }

        @Override
        public void edit(JankModel model, String inputString) {
            // nothing            
        }

        @Override
        public Interactive forward(JankModel model) {
            return new PracticeSelect(model);
        }

    }

}
