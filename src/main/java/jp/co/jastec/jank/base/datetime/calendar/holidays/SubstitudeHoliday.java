package jp.co.jastec.jank.base.datetime.calendar.holidays;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import jp.co.jastec.jank.base.datetime.JankDate;
import jp.co.jastec.jank.base.datetime.calendar.JankCalendar;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic;
import jp.co.jastec.jank.base.datetime.calendar.JankHoliday;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.Factory;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.HolidayType;

/** 振替休日（祝日が日曜日の場合に次の平日を休みにする） */
public class SubstitudeHoliday  {

    private final JankCalendar jankCalendar;
    private final int year;

    private SubstitudeHoliday(JankCalendar jankCalendar) {
        this.year = jankCalendar.getYear();
        this.jankCalendar = jankCalendar;
    }

    /// コンストラクタを隠すのに、ファクトリメソッドを用意
    /** この引数の JankCalendar には、祝日が既に入っている必要がある */
    public static SubstitudeHoliday of (JankCalendar jankCalendar) {
        return new SubstitudeHoliday(jankCalendar);
    }

    // 一年間の日曜日をチェックして、祝日と重なっていたら、次の平日を振替日にする
    public Iterable<Factory> getFactories() {

        List<Factory> factories = new ArrayList<>() ;

        /// for ( 今年最初の日曜日 ; 今年いっぱい ; 次の日曜日 ) { 振替休日をつくる }　
        /// while文で書くのが普通かもしれないが、意外に古典的forループが使える
        for ( JankDate eachSunday = JankCalendar.findFirstDayOfWeek(this.year, 1, DayOfWeek.SUNDAY); // 今年最初の日曜日
                eachSunday.getYear() == this.year ;  //今年の間
                eachSunday = eachSunday.plusDays(JankDate.DAYS_IN_WEEK) ) {  // 翌週の日曜日
                
            JankDateCharacteristic cha = jankCalendar.get(eachSunday) ;

            if ( cha != null && cha.isPublicHoliday() ) {
                final JankDate subst = findSubstitude(eachSunday) ;
                final Factory factory = new Factory() {
                    @Override
                    public JankDateCharacteristic create(int year) {
                        return new JankHoliday(
                            "振替休日(" + cha.getName() + ")",  subst,  HolidayType.SUBSTITUTE_HOLIDAY
                        );
                    }
                };

                factories.add(factory) ;
            }
        }
        return factories;
    }

    private JankDate findSubstitude(JankDate holiday) {
        JankDate nextDay = holiday.plusDays(1);
        while ( null != checkPublicHoliday(nextDay) || nextDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            nextDay = nextDay.plusDays(1);
        }
        return nextDay;
    }

    /** 公的な休日であればCharacteristecインスタンスを返す */
    private JankDateCharacteristic checkPublicHoliday(JankDate jankDate) {
        JankDateCharacteristic cha = this.jankCalendar.get(jankDate) ;
        if ( cha != null && cha.isPublicHoliday() ) {
            return cha;
        } else {
            return null;
        }
    }
}