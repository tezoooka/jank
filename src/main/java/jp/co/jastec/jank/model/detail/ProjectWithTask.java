package jp.co.jastec.jank.model.detail;

import jp.co.jastec.jank.base.JankList;

/** 工程作業項目が必要なプロジェクト（未使用） */
public class ProjectWithTask extends Project {


    private JankList<Task> tasks;
    
    public ProjectWithTask(String prjCode) {
        super(prjCode);
    }
    public void addTask(Task task) {
        this.tasks.add(task,this);
    }

    @Override
    public JankList<Task> getTasks() {
        return this.tasks;
    }
    
    @Override
    public JankList<?> getChildren() {
        return this.tasks;
    }


}
