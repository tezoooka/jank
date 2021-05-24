package jp.co.jastec.jank.model.detail;

import jp.co.jastec.jank.base.JankList;

/** 生産管理情報詳細が必要なプロジェクト（作ったけど未使用） */
public class ProjectWithProductive extends Project {
    
    private JankList<Productive> productiveList;

    public ProjectWithProductive(String prjCode) {
        super(prjCode);
    }

    @Override
    public JankList<Task> getTasks() {
        JankList<Task> taskList = new JankList<Task>();
        Task t = new Task(this.code, productiveList.sumHours());
        taskList.add(t,this);
        return taskList;
    }

    @Override
    public JankList<?> getChildren() {
        return this.productiveList;
    }
    
}
