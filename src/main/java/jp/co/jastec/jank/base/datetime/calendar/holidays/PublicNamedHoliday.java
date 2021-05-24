package jp.co.jastec.jank.base.datetime.calendar.holidays;

import java.util.Arrays;

import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.Factory;

public class PublicNamedHoliday extends VersionableFactory {

    // 祝日の一覧を定義
    private static Factory[] FACTORIES = {

        define("元旦")          .fixedHoliday(OPEN, OPEN, 1,  1).defined(),

        define("成人の日")      .fixedHoliday(OPEN, 1999, 1, 15)
                                .happyMonday (2000, OPEN, 1, NthDOW.SECOND)
                                .defined(),

        define("建国記念の日")  .fixedHoliday(1967, OPEN, 2, 11).defined(),

        define("春分の日")      .springEquinoxDay(OPEN, OPEN).defined(),

        define("みどりの日")    .fixedHoliday(1989, 2006 , 4, 29).defined(),

        define("昭和の日")      .fixedHoliday(2007, OPEN , 4, 29).defined(),

        define("憲法記念日")    .fixedHoliday(2007, OPEN , 5, 3).defined(),

        define("みどりの日")    .fixedHoliday(2007, OPEN , 5, 4).defined(),

        define("こどもの日")    .fixedHoliday(OPEN, OPEN , 5, 5).defined(),

        define("海の日")        .fixedHoliday(2006, 2002, 7, 20)
                                .happyMonday (2003, 2019, 7, NthDOW.THIRD)
                                .fixedHoliday(2020, 2020, 7, 23) //幻の五輪開催日
                                .fixedHoliday(2021, 2021, 7, 22)
                                .happyMonday (2003, 2019, 7, NthDOW.THIRD)
                                .defined(),

        define("山の日")        .fixedHoliday(2016, OPEN, 8, 11).defined(),

        define("敬老の日")      .fixedHoliday(1966, 2002, 9, 15)
                                .happyMonday (2003, OPEN, 9, NthDOW.THIRD)
                                .defined(),

        define("秋分の日")      .autumnEquinoxDay(OPEN, OPEN).defined(),

        define("体育の日")      .fixedHoliday(1966, 1999, 10, 10)
                                .happyMonday (2000, 2019, 10 ,NthDOW.SECOND)
                                .defined(),

        define("スポーツの日")  .happyMonday (2020, OPEN, 10, NthDOW.SECOND).defined(),  

        define("文化の日")      .fixedHoliday(OPEN, OPEN, 11,  3).defined(),
        define("勤労感謝の日")  .fixedHoliday(OPEN, OPEN, 11, 23).defined(),

        define("天皇誕生日")    .fixedHoliday(OPEN, 1988,  4, 29) //昭和天皇  
                                .fixedHoliday(1989, 2018, 12, 23) //平成天皇  
                                .fixedHoliday(2020, OPEN,  2, 23) //令和天皇  
                                .defined(),
    };

    /// 上記の static 定義を簡便に書くためのメソッド
    private static PublicNamedHoliday define(String nameOfHoliday) {
        return new PublicNamedHoliday(nameOfHoliday) ;
    }

    public static Iterable<Factory> getFactories() {
        return Arrays.asList(FACTORIES);
    }

    private PublicNamedHoliday( String name ) {
        super(name);
    }

}
