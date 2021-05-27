package jp.co.jastec.jank.input;

import jp.co.jastec.jank.base.JankCodeElement;
import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.cli.InputRejectException;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveInput;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.detail.Practice;
import jp.co.jastec.jank.model.detail.Project;
import jp.co.jastec.jank.model.detail.Task;
import jp.co.jastec.jank.service.ProjectMaster;
import jp.co.jastec.jank.service.ProjectMaster.ProjectAttibute;
import jp.co.jastec.jank.service.TaskSet;
import jp.co.jastec.jank.service.TaskSet.TaskElement;

public class TaskHourInput extends InteractiveInput {

    private enum InteractiveState { Doing, Returning, Ending }

    private String practiceCode;
    private String projectCode;
    private TaskSet taskSet;
    private float inputHours = 0 ;
    private String inputTaskCode = "";
    private Interactive previousInput;

    private InteractiveState state = InteractiveState.Doing; 


    public TaskHourInput(JankModel model, String practiceCode, String projectCode, Interactive previousInput) {
        super(model);
        this.practiceCode = practiceCode;
        this.projectCode = projectCode;
        ProjectAttibute pa = ProjectMaster.table().get(projectCode);
        this.taskSet = pa.getTaskSet();
        this.previousInput = previousInput;
    }


    @Override
    public String prompt() {
        String msg =    "工程作業項目3桁と工数を、空白で区切って入力してください。\n" +
                        "入力可能な工程作業項目をみるには、/h と入力してください。\n" + 
                        "プロジェクト入力に戻る場合は、空Enter";
        return msg;
    }

    @Override
    protected String checkInputFormat(String line) throws InputRejectException {

        if ( line.isEmpty() ) {
            this.state = InteractiveState.Returning;
            return line ;
        }

        if ( line.equals("/E")) {
            this.state = InteractiveState.Ending;
            return line;
        }

        String[] params = line.split(" ") ;
        if ( params.length != 2 ) {
            reject(JankMessage.ILLEAGAL_INPUT_FORMAT, line);
        }
        String taskCode = params[0];
        if ( ! this.taskSet.isExist(taskCode) ){
            reject(JankMessage.UNSELECTABLE_OPTION, "工程作業項目" + taskCode);
        } else {
            this.inputTaskCode = taskCode;
        }

        try {
            this.inputHours = Float.parseFloat(params[1]) ;
        } catch (NumberFormatException nfe) {
            reject(JankMessage.ILLEAGAL_INPUT_FORMAT, "工数 " + params[1]);
        }
        
        return line;
    }

    @Override
    protected void updateModel(String line) {

        if ( this.state != InteractiveState.Doing ) {
            return ;
        }
        final Practice pra = this.model.getPractices().find(practiceCode);
        final Project  prj = pra.getProjects().find(projectCode);
        final Task newTask = new Task(taskSet.get(this.inputTaskCode), inputHours);
        prj.getTasks().add(newTask, prj);

    }

    @Override
    protected Interactive getNext() {

        switch (this.state) {
            case Doing: 
                if (this.model.isManHoursFilled()) {
                    return new Editing(this.model) ;
                } else {
                    return this;
                }
            case Returning : return previousInput;
            case Ending : return new Closing(this.model, this);
            default : return this; /// 書かないと怒られる
        }

    }

    @Override
    public String helpText() {

        StringBuilder sb = new StringBuilder();
        for (TaskElement te : this.taskSet) {
            final String caption = "  " + JankCodeElement.fixing(te.code(), 4, te.name(), 10) + "\n";
            sb.append(caption);
        }
        return sb.toString();
        
    }
}
