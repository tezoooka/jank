package jp.co.jastec.jank.model.detail;

import jp.co.jastec.jank.base.JankException;
import jp.co.jastec.jank.base.JankList;
import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.base.ManHour;

/** 工程作業項目が不要なプロジェクト（作ったけど未使用） */
public class ProjectWithoutTasks extends Project {

    private float hours ;
    
    public ProjectWithoutTasks(String prjCode, float hours) {
        super(prjCode);
        if ( ! ManHour.is05Unit(hours) ) {
            throw new JankException(JankMessage.HOURS_NOT_05UNIT);
        }
        this.hours = hours;
    }

    @Override
    public float getHours() {
        return this.hours;
    }

    @Override
    public JankList<Task> getTasks() {
        return TASK_EMPTY;
    }

    @Override
    public JankList<Task> getChildren() {
        return getTasks();
    }
    
    private static JankList<Task> TASK_EMPTY = new JankList<>();
}
