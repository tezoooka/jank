package jp.co.jastec.jank.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.annotations.Expose;

import jp.co.jastec.jank.base.JankList;
import jp.co.jastec.jank.cli.CliView;
import jp.co.jastec.jank.cli.CliView.CliViewable;
import jp.co.jastec.jank.model.detail.DetailContainer;
import jp.co.jastec.jank.model.detail.Practice;
import jp.co.jastec.jank.model.header.Employee;
import jp.co.jastec.jank.model.header.WorkingDateTime;
import jp.co.jastec.jank.service.JankArchiver.JankArchiveModel;

public class JankModel implements CliViewable , JankArchiveModel {

    private LocalDateTime created ;

    @Expose
    private Employee employee; 

    @Expose
    private WorkingDateTime workingDateTime; 

    @Expose
    private DetailContainer details = new DetailContainer();

    public void setEmployee(Employee emp) {
        this.employee = emp ;
        this.created = LocalDateTime.now();
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setWorkingTime(WorkingDateTime wdt) {
        this.workingDateTime = wdt;
    }

    public WorkingDateTime getWorkingDateTime() {
        return workingDateTime;
    }

    public DetailContainer getDetails() {
        return details;
    }

    public JankList<Practice> getPractices() {
        return this.details.getPractices();
    }

    public void setPractices(JankList<Practice> practices) {
        this.details.setPractices(practices);
    }

    /** 就業管理上の実労働時間を返す */
    public float getWorkingHours() {
        return (this.workingDateTime == null) ? 0 : this.workingDateTime.getWorkingHours() ;
    }

    /** 生産管理上の工数の合計を返す */
    public float getManHours() {
        return  (this.details == null) ? 0 : this.details.getHours();
    }

    public boolean isManHoursFilled() {

        final float workingHours = getWorkingHours() ;
        final float manHours = getManHours();
        return (workingHours > 0 ) && (manHours >= workingHours);
        
    }



    public CliView getView() {

        CliView view = new CliView();
        if ( this.employee != null ) {
            view.add(CliView.HR);
            view.addView(this.employee);
        }
        view.addView(this.workingDateTime);
        view.addView(this.details);

        return view;
    }

    @Override
    public String getUniqIdentier() {
 
        String empNo = this.employee.code();

        LocalDate date = this.workingDateTime.getBaseDate().localDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuuMdd_HHmmss_SSS");
        LocalTime time = this.created.toLocalTime();
        LocalDateTime dateTime = LocalDateTime.of(date,time);
        String ymds = dateTime.format(dateTimeFormatter);

        return empNo + "_" + ymds ;

    }
}