package jp.co.jastec.jank.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.jastec.jank.base.JankCodeElement;
import jp.co.jastec.jank.cli.Interactive;
import jp.co.jastec.jank.cli.InteractiveSelect;
import jp.co.jastec.jank.cli.Option;
import jp.co.jastec.jank.cli.OptionSelecter;
import jp.co.jastec.jank.model.JankModel;
import jp.co.jastec.jank.model.detail.GenericProject;
import jp.co.jastec.jank.model.detail.Project;
import jp.co.jastec.jank.service.ProjectMaster;
import jp.co.jastec.jank.service.ProjectMaster.ProjectAttibute;

public class ProjectSelect extends InteractiveSelect<String> {

    private String practiceCode ;
    private String projectCode ;
    private boolean goingPractice = false ;
    
    public ProjectSelect(JankModel model, String practiceCode) {
        super(model, false);
        this.practiceCode = practiceCode;
    }

    @Override
    protected String getBasePrompt() {
        return "プロジェクト（または顧客名）を選択してください。\n 空Enterで活動コード選択に戻ります。";
    }

    @Override
    protected OptionSelecter<String> createOptionSelecter() {
        final List<Option<String>> optList = new ArrayList<>();
        final Map<String, ProjectAttibute> prjMap = ProjectMaster.table().selectByPractice(this.practiceCode);
        int i = 1 ;
        for (Entry<String, ProjectAttibute> kv : prjMap.entrySet() ) {
            final String code = kv.getKey();
            final String name = kv.getValue().getName();
            final String caption = JankCodeElement.fixing(code, 8, name, 24) ;
            optList.add(new Option<String>(String.valueOf(i++), caption, code)) ;
        }
        optList.add(new Option<String>(" 空Enter", "活動コード選択へもどる", null, true)) ;

        return new OptionSelecter<String>(optList, 6);
    }

    @Override
    protected void updateModelWithOption(String selected) {
        if ( selected == null ) {
            this.goingPractice = true;
            return;
        }
        this.projectCode = selected;
        Project newProject = new GenericProject(selected);
        this.model.getDetails().getPractices().find(this.practiceCode).addProject(newProject);
    }

    @Override
    protected Interactive getNext() {
        
        if ( this.goingPractice ) {
            return new PracticeSelect(this.model) ;
        
        } else if ( this.projectCode != null) {
            return new TaskHourInput(this.model,this.practiceCode, this.projectCode, this);
        
        } else {
            return this;
        }
    }
    
}
