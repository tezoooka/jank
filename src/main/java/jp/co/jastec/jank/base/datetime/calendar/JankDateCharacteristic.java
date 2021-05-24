package jp.co.jastec.jank.base.datetime.calendar;

import java.time.DayOfWeek;

import jp.co.jastec.jank.base.datetime.JankDate;

/// 日付（特に休日）の特徴を示すインターフェース
/// 休日の属性しかないので、JankHolidayというクラスに直接書いても良かったけど、
/// 休日以外の日にも転用可能なように、interface を独立させている。
public interface JankDateCharacteristic  {

    /** 祝日の種類を示す列挙子 */
    public enum HolidayType {
        FIXED_HOLIDAY,
        HAPPY_MONDAY,
        EQUINOX,
        SUBSTITUTE_HOLIDAY,
        NATIONS_DAYOFF,
        ENTERPRISE_HOLIDAY,
        PATCHED,
    }

    /** 年別カレンダー生成時に用いる interface */
    public interface Factory {
        public JankDateCharacteristic create(int year);
        public default JankDate negate() {
            return null;
        }
    }

    public String getName() ;

    public JankDate getDate();

    public HolidayType getHolidayType() ;

    /** 公的な休日か？（土曜日や会社特有の休日は含まない） */
    public boolean isPublicHoliday() ;

    public boolean isNagated();

    /** 単純に「休日か？」という判定（土日も含む） */
    public default boolean isHoliday() {
        DayOfWeek dow = this.getDate().getDayOfWeek() ;
        if ( dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            return true ;
        } else {
            return getHolidayType() != null;
        }
    }

    /** 祝日かどうかの判定（土日は含まず、振替休日や国民の休日も含まない） */
    public default boolean isCelebrationDay() {
        HolidayType ht = this.getHolidayType();
        return
            ht == HolidayType.FIXED_HOLIDAY ||
            ht == HolidayType.HAPPY_MONDAY ||
            ht == HolidayType.EQUINOX ;
    }

    /** isCelebrationDay() と等価 */
    public default boolean isNamedHoliday() {
        return isCelebrationDay();
    }

    public default String toDisplayString() {
        HolidayType ht  = this.getHolidayType();
        return String.format("%s (%s) %s", 
                    this.getDate().toStringWithDow(),
                    this.getName(),
                    ht != null ? ht.toString() : ""
                );
    } 

    /** 何の変哲もない平日を示す */
    public static JankDateCharacteristic ordinaryDay(JankDate date) {

        return new JankDateCharacteristic() {

            @Override
            public String getName() {
                return "";
            }

            @Override
            public JankDate getDate() {
                return date;
            }

            @Override
            public HolidayType getHolidayType() {
                return null;
            }

            @Override
            public boolean isPublicHoliday() {
                return (date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }

            @Override
            public boolean isNagated() {
                return false;
            }

        };
    }
}