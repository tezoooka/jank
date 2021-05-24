package jp.co.jastec.jank.model.detail;

import com.google.gson.annotations.Expose;

import jp.co.jastec.jank.base.JankException;
import jp.co.jastec.jank.base.JankList;
import jp.co.jastec.jank.base.JankNode;
import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.base.ManHour;
import jp.co.jastec.jank.base.validation.StringFormatChecker;
import jp.co.jastec.jank.service.TaskSet.TaskElement;

/**
 * 工程作業項目
 */
public class Task extends JankNode {

    @Expose
    private float hours;
    private String taskName ;

    public Task(String taskCode, float hours) {
        super(taskCode);
        if ( ! ManHour.is05Unit(hours)) {
            throw new JankException(JankMessage.HOURS_NOT_05UNIT);
        }
        this.hours = hours;
        this.taskName = "";
    }

    public Task(TaskElement taskElement, float hours) {
        super(taskElement.code());
        this.name = taskElement.name();
        if ( ! ManHour.is05Unit(hours)) {
            throw new JankException(JankMessage.HOURS_NOT_05UNIT);
        }
        this.hours = hours;
    }


    public void setHours(float hours) {
        this.hours = hours;
    }

    public float getHours() {
        return hours;
    }

    public float hours() {
        return hours;
    }

    @Override
    protected String getNameByCode() {
        return this.taskName;
    }

    @Override
    protected boolean isValidFormat(String code) {
        return getStringFormatChecker().isValidFormat(code);
    }

    public static StringFormatChecker getStringFormatChecker(){
        return new StringFormatChecker()
            .allowNumeric()
            .allowUpperAlpha()
            .fixLength(3);
    }

    @Override
    public JankList<Task> getChildren() {
        return null;
    }
    
}