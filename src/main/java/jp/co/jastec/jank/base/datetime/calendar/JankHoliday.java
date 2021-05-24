package jp.co.jastec.jank.base.datetime.calendar;

import java.time.DayOfWeek;

import jp.co.jastec.jank.base.datetime.JankDate;

public class JankHoliday extends JankDate implements JankDateCharacteristic {

    private String name ;

    private HolidayType holidayType ;

    private HolidayType negatedType ;

    public JankHoliday(String name, int year, int month, int dayOfMonth, HolidayType holidayType) {
        this(name, new JankDate(year, month, dayOfMonth), holidayType);
    }

    public JankHoliday(String name, JankDate date, HolidayType holidayType) {
        super(date);
        this.name = name;
        this.holidayType = holidayType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JankDate getDate() {
        return this;
    }

    public void negate() {
        this.negatedType = this.holidayType;
        this.holidayType = null;
    }

    @Override
    public boolean isNagated() {
        return negatedType != null;
    }

    public JankDateCharacteristic.HolidayType getHolidayType() {
        return holidayType;
    }

    @Override
    public boolean isHoliday() {
        DayOfWeek dow = this.localDate().getDayOfWeek() ;
        if ( dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            return true ;
        } else {
            return this.holidayType != null ;
        }
    }

    /** 公的な休日（日曜日or祝日）か？ */
    @Override
    public boolean isPublicHoliday() {
        HolidayType ht = this.getHolidayType();
        DayOfWeek dow = this.localDate().getDayOfWeek() ;
        return  ht != null && 
                (ht != HolidayType.ENTERPRISE_HOLIDAY) ||
                (dow == DayOfWeek.SUNDAY);
    }


}
