package jp.co.jastec.jank.input;

import jp.co.jastec.jank.base.JankList;
import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.base.ManHour;
import jp.co.jastec.jank.cli.InputRejectException;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveInput;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.detail.GenericProject;
import jp.co.jastec.jank.model.detail.Practice;
import jp.co.jastec.jank.model.detail.Project;
import jp.co.jastec.jank.model.detail.Task;

/// 本クラスは、活動コード～工数をを一行で入力する仕組みだが、選択的に入力するようにしたので、
/// 未使用である。
@Deprecated 
public class DetailOneLineInput extends InteractiveInput {

    private final static String SAME_AS_ABOVE = "=";

    private final static float JACK_BAUER = 24.0f;

    private InputElement currentInput;
    private InputElement previousInput = new InputElement("", "", "");
    private float hours ;
    private boolean ended = false ;

    public DetailOneLineInput(JankModel model) {
        super(model);
    }

    @Override
    public String prompt() {
        return "活動コード/プロジェクトコード/工程作業項目/工数 を空白で区切って入力してください。\n" +
                "前行と同じコードは " + SAME_AS_ABOVE + " を入力できます。\n" +
                "一日分の入力を終えたら、 e を押してください。";
    }

    @Override
    protected void updateModel(String line) {
        
        if ( this.ended ) {
            return ;
        }

        // 活動コードのリスト
        // 最初はインスタンスがないので作成する
        JankList<Practice> practices = this.model.getPractices();
        if ( practices == null ) {
            practices = new JankList<>();
            this.model.setPractices(practices);
        }

        // 活動コードのリスト内に、入力した活動コードがあればそれを取り出す。
        // なければ新しくつくる。
        Practice pra = findOrNewPractice(this.currentInput.practiceCode, practices);
        
        // 活動コード内のプロジェクトリスト内に、入力した・・・・以下同様
        Project prj = findOrNewProject(this.currentInput.projectCode, pra.getProjects());
        
        // 工程作業項目と工数をセット
        JankList<Task> tasks = prj.getTasks();
        tasks.add( new Task(this.currentInput.taskCode, hours)) ;

        this.previousInput = this.currentInput;
    }

    private Practice findOrNewPractice(String code, JankList<Practice> list){
        Practice pra = list.find(code);
        if ( pra ==  null ) {
            pra =  new Practice(code) ;
            list.add(pra);
        }
        return pra;      
    }

    private Project findOrNewProject(String code, JankList<Project> list){
        Project prj = list.find(code);
        if ( prj == null ) {
            prj = new GenericProject(code);
            list.add(prj);
        }
        return prj;
    }

    @Override
    protected String checkInputFormat(String line) throws InputRejectException {
        
        if (line.trim().equals("E")) {
            this.ended = true ;
            return line;
        }

        final String params[] = line.toUpperCase().split(" ") ;
        if ( params.length != 4 ) {
            reject(JankMessage.ILLEAGAL_INPUT_FORMAT, line);
        }

        final InputElement pi = this.previousInput;

        final String praCode = assumeImput(params[0], pi.practiceCode) ;
        final String prjCode = assumeImput(params[1], pi.projectCode) ;
        final String tskCode = assumeImput(params[2], pi.taskCode) ;

        final InputElement ci = new InputElement(praCode, prjCode,tskCode);
        this.currentInput = ci;

        //活動コードの形式チェック
        if (! Practice.getStringFormatChecker().isValidFormat(ci.practiceCode)){
            reject(JankMessage.ILLEAGAL_INPUT_FORMAT, "活動コード " + ci.practiceCode);
        }

        //プロジェクトコードの形式チェック
        if (! Project.getStringFormatChecker().isValidFormat(ci.projectCode)){
            reject(JankMessage.ILLEAGAL_INPUT_FORMAT, "プロジェクトコード " + ci.projectCode);
        }

        //プロジェクトコードの形式チェック
        if (! Task.getStringFormatChecker().isValidFormat(ci.taskCode)){
            reject(JankMessage.ILLEAGAL_INPUT_FORMAT, "工程作業項目 " + ci.taskCode);
        }
        
        this.hours = this.parseHours(params[3]) ;

        return line;

    }

    private String assumeImput(String current, String previous) {
        if ( current.trim().equals(SAME_AS_ABOVE) && !previous.isEmpty()) {
            return previous;
        } else {
            return current.trim();
        }
    }

    private float parseHours(String stringHour) throws InputRejectException{

        float floatHour = 0 ;

        try {
            floatHour = Float.parseFloat(stringHour) ;
        
            if ( ! ManHour.is05Unit(floatHour )) {
                throw new NumberFormatException();
            }

            if ( floatHour > JACK_BAUER ) {
                reject(JankMessage.YOU_ARE_NOT_JACKBAUER, stringHour);
            }

        } catch (NumberFormatException nfe) {
            reject(JankMessage.ILLEAGAL_INPUT_FORMAT, "工数 " + stringHour);
        }

        return floatHour;
    }



    @Override
    protected Interactive getNext() {
        if ( this.ended ) {
            return new Closing(this.model, this);
        } else {
            return this;
        }
    }

    private static class InputElement {
        private String practiceCode;
        private String projectCode;
        private String taskCode;
        private InputElement(String practiceCoe, String projectCode, String taskCode) {
            this.practiceCode = practiceCoe;
            this.projectCode = projectCode;
            this.taskCode = taskCode;
        }
    }


}
