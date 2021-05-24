package jp.co.jastec.jank.base.datetime.calendar.holidays;

import java.util.Arrays;

import jp.co.jastec.jank.base.datetime.MissingCalendarException;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic;
import jp.co.jastec.jank.base.datetime.calendar.JankHoliday;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.Factory;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.HolidayType;

public class EquinoxDay implements Factory {

    public static final EquinoxDefine[] DEFINE_SPRING = {
        // https://w.wiki/3FNT
        EquinoxDefine.between( 1900, 1923, 21, 21, 21, 22 ),
        EquinoxDefine.between( 1924, 1959, 21, 21, 21, 21 ),
        EquinoxDefine.between( 1960, 1991, 20, 21, 21, 21 ),
        EquinoxDefine.between( 1992, 2023, 20, 20, 21, 21 ),
        EquinoxDefine.between( 2024, 2055, 20, 20, 20, 21 ),
        EquinoxDefine.between( 2056, 2091, 20, 20, 20, 20 ),
        EquinoxDefine.between( 2092, 2099, 19, 20, 20, 20 ),
        EquinoxDefine.between( 2100, 2123, 20, 21, 21, 21 ),
        EquinoxDefine.between( 2124, 2155, 20, 20, 21, 21 ),
        EquinoxDefine.between( 2156, 2187, 20, 20, 20, 21 ),
        EquinoxDefine.between( 2188, 2199, 20, 20, 20, 20 ),
        EquinoxDefine.between( 2200, 2223, 21, 21, 21, 21 ),
        EquinoxDefine.between( 2224, 2255, 20, 21, 21, 21 ),
        EquinoxDefine.between( 2256, 2287, 20, 20, 21, 21 ),
        EquinoxDefine.between( 2288, 2299, 20, 20, 20, 21 ),
     };

     public static final EquinoxDefine[] DEFINE_AUTUMNINAL = {
        //https://w.wiki/3FNU
        EquinoxDefine.between( 1900, 1919, 23, 24, 24, 24 ),
        EquinoxDefine.between( 1920, 1947, 23, 23, 24, 24 ),
        EquinoxDefine.between( 1948, 1979, 23, 23, 23, 24 ),
        EquinoxDefine.between( 1980, 2011, 23, 23, 23, 23 ),
        EquinoxDefine.between( 2012, 2043, 22, 23, 23, 23 ),
        EquinoxDefine.between( 2044, 2075, 22, 22, 23, 23 ),
        EquinoxDefine.between( 2076, 2099, 22, 22, 22, 23 ),
        EquinoxDefine.between( 2100, 2107, 23, 23, 23, 24 ),
        EquinoxDefine.between( 2108, 2139, 23, 23, 23, 23 ),
        EquinoxDefine.between( 2140, 2167, 22, 23, 23, 23 ),
        EquinoxDefine.between( 2168, 2199, 22, 22, 23, 23 ),
        EquinoxDefine.between( 2200, 2227, 23, 23, 23, 24 ),
        EquinoxDefine.between( 2228, 2263, 23, 23, 23, 23 ),
        EquinoxDefine.between( 2264, 2291, 22, 23, 23, 23 ),
        EquinoxDefine.between( 2292, 2299, 22, 22, 23, 23 ),                
    };

    public static final Factory SPRING_FACTORY =  new EquinoxDay("春分の日", 3, DEFINE_SPRING );
    public static final Factory AUTUMINAL_FACTORY = new EquinoxDay("秋分の日", 9, DEFINE_AUTUMNINAL );

    private static final int LEAP_CYCLE = 4;

    private final String title;                
    private final int month;
    private final EquinoxDefine[] table;

    public EquinoxDay( String title, int month, EquinoxDefine[] table ) {
        this.title = title;
        this.month = month;
        this.table = table;
    }


    @Override
    public final JankDateCharacteristic create(int year) {
        return new JankHoliday( this.title, year, this.month, getYearOf(year), HolidayType.EQUINOX );
    }

    private int getYearOf(int year) {
        int modYear = year % LEAP_CYCLE ;
        EquinoxDefine def = Arrays.stream(this.table)
                .filter( (q) -> q.fromYear <= year &&  year <= q.toYear )
                .findFirst().get();
        if ( def == null ) {
            throw new MissingCalendarException();
        }
        return def.dayOfMonth[modYear];
    }


    /**  春秋分の日が4年毎に切り替わるパターンを定義する型 */
    private static final class EquinoxDefine {

        private int fromYear;
        private int toYear;
        private int[] dayOfMonth = new int[LEAP_CYCLE];
        private EquinoxDefine (int fromYear, int toYear, int... days) {
            assert(days.length == EquinoxDay.LEAP_CYCLE) ;
            this.fromYear = fromYear;
            this.toYear = toYear;
            this.dayOfMonth = days;
        }

        private static EquinoxDefine between  (int fromYear, int toYear, int... days) {
            return new EquinoxDefine(fromYear, toYear, days);
        }
    
    }
}
