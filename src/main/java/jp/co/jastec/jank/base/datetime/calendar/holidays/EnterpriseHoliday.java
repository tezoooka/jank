package jp.co.jastec.jank.base.datetime.calendar.holidays;

import java.util.Arrays;

import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic;
import jp.co.jastec.jank.base.datetime.calendar.JankHoliday;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.Factory;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.HolidayType;

public class EnterpriseHoliday extends VersionableFactory {

    private static Factory[] FACTORIES = {
        
        define("年末年始")  .fixedHoliday( OPEN, OPEN, 12, 29).defined(),
        define("年末年始")  .fixedHoliday( OPEN, OPEN, 12, 30).defined(),
        define("年末年始")  .fixedHoliday( OPEN, OPEN, 12, 31).defined(),
        define("年末年始")  .fixedHoliday( OPEN, OPEN,  1,  2).defined(),
        define("年末年始")  .fixedHoliday( OPEN, OPEN,  1,  3).defined(),
        
        // define("創立記念日")  .fixedHoliday( OPEN, OPEN,  6,  1).defined(),

    };

    /// 上記の static 定義を簡便に書くためのメソッド
    private static VersionableFactory define(String nameOfHoliday) {
        return new EnterpriseHoliday(nameOfHoliday) ;
    }

    public static Iterable<Factory> getFactories() {
        return Arrays.asList(FACTORIES);
    }

    // 月・日が固定の祝日に対する Facory を生成してリストに追加する。
    protected VersionableFactory fixedHoliday(int startingYear, int endingYear, int month, int dayOfMonth) {

        assert(startingYear <= endingYear) : "endingYear is not after startingYear" ;
        Factory factory = new Factory() {
            @Override
            public JankDateCharacteristic create(int year) {
                return new JankHoliday(name, year, month, dayOfMonth, HolidayType.ENTERPRISE_HOLIDAY);
            }
        };
        addFactoryVerion( startingYear, endingYear , factory);
        return this;
    }

    private EnterpriseHoliday( String name ) {
        super(name);
    }
}
