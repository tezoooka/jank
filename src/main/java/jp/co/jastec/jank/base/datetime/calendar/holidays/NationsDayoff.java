package jp.co.jastec.jank.base.datetime.calendar.holidays;

import java.util.ArrayList;
import java.util.List;

import jp.co.jastec.jank.base.datetime.JankDate;
import jp.co.jastec.jank.base.datetime.calendar.JankCalendar;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic;
import jp.co.jastec.jank.base.datetime.calendar.JankHoliday;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.Factory;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.HolidayType;

/** 国民の休日（祝日に挟まれた平日を休みにする） */
public class NationsDayoff {

    private final JankCalendar jankCalendar;
    
    /** コンストラクタに渡す JankClendar には、祝日が設定済みであること */
    public NationsDayoff(JankCalendar jankCalendar) {
        this.jankCalendar = jankCalendar;
    }

    public Iterable<Factory> getFactories() {
        
        List<Factory> factories = new ArrayList<>();

        for ( JankDate holiday : this.jankCalendar.keySet() ) {
            final JankDate today = holiday;
            final JankDate tomorrow = today.plusDays(1);
            final JankDate dayAfterTomorrow = tomorrow.plusDays(1);
            if ( isCelebrationDay(today) &&
                 isNotHoliday(tomorrow) && 
                 isCelebrationDay(dayAfterTomorrow) ) {
                final Factory factory = new Factory() {
                    @Override
                    public JankDateCharacteristic create(int year) {
                        return new JankHoliday(
                            "国民の休日", tomorrow,  HolidayType.NATIONS_DAYOFF
                        );
                    }
                };
                factories.add(factory);
            }
        }
        return factories;
    }

    // 祝日（振替休日や国民の休日は含まない）か？ 
    private boolean isCelebrationDay(JankDate jankDate) {
        JankDateCharacteristic cha = this.jankCalendar.get(jankDate);
        if (null == cha) {
            return false;
        }
        return cha.isCelebrationDay();
    }
    
    private boolean isNotHoliday(JankDate jankDate) {
        JankDateCharacteristic cha = this.jankCalendar.get(jankDate);
        /// 否定疑問は分かりにくいので、一旦肯定形で判定してから否定する
        boolean isHoleday = (cha != null && cha.isHoliday());
        return ! isHoleday;
    }

}