package jp.co.jastec.jank.base.datetime.calendar.holidays;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jp.co.jastec.jank.base.datetime.JankDate;
import jp.co.jastec.jank.base.datetime.calendar.JankCalendar;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic;
import jp.co.jastec.jank.base.datetime.calendar.JankHoliday;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.Factory;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.HolidayType;

/** 世代別に定義内容の版数を保持できるFactoryクラス */
public abstract class VersionableFactory implements JankDateCharacteristic.Factory {
    
    protected static final int OPEN = -1; /// 西暦年としてあり得ない整数なら何でもよい

    protected final String name;
    protected List<Versioning> versions = new ArrayList<>();

    protected VersionableFactory( String name ) {
        this.name = name;
    }

    // public abstract Factory[] getFactories(); 

    /// 無くても良いメソッドだけど、一応defineの指定内容に矛盾がないかチェックしている。
    protected VersionableFactory defined() {
        for (int i = 0 ;  i < this.versions.size() - 1; i++) {
            Versioning here = this.versions.get(i);
            Versioning next = this.versions.get(i+1);
            assert(here.endingYear <= next.endingYear);
        }
        return this;
    }

    // 月・日が固定の祝日に対する Facory を生成してリストに追加する。
    protected VersionableFactory fixedHoliday(int startingYear, int endingYear, int month, int dayOfMonth) {

        assert(startingYear <= endingYear) : "endingYear is not after startingYear" ;
        Factory factory = new Factory() {
            @Override
            public JankDateCharacteristic create(int year) {
                return new JankHoliday(name, year, month, dayOfMonth, HolidayType.FIXED_HOLIDAY);
            }
        };
        addFactoryVerion( startingYear, endingYear , factory);
        return this;
    }

    /** 第ｎ〇曜日の、ｎを示すための列挙子 */
    public enum NthDOW { FIRST, SECOND, THIRD, FOURTH } ;
        
    // ハッピーマンデー制度の祝日に対する Factory を生成して List に追加する
    protected VersionableFactory happyMonday(int startingYear, int endingYear, int month, NthDOW nThDow) {
        assert(startingYear <= endingYear) : "endingYear is not after startingYear" ;
        FloatingHolidayFactory happyFactory 
            = new FloatingHolidayFactory(name, month, nThDow, DayOfWeek.MONDAY, HolidayType.HAPPY_MONDAY);
        addFactoryVerion(startingYear, endingYear ,happyFactory) ;
        return this;
    }

    /// HappyMonday用Factoryの一般形（月曜じゃなくても使える）
    public static class FloatingHolidayFactory implements Factory {
        
        final private String name;
        final private int month;
        final private NthDOW nThDow;
        final private DayOfWeek dow;
        final private HolidayType type;

        public FloatingHolidayFactory(String name, int month, NthDOW nThDow, DayOfWeek dow, HolidayType holidayType) {
            this.name = name;
            this.month = month;
            this.nThDow = nThDow;
            this.dow = dow;
            this.type = holidayType;
        }
        
        @Override
        public JankDateCharacteristic create(int year) {
            final LocalDate firstDow = JankCalendar.findFirstDayOfWeek(year, month, dow).localDate() ;
            final LocalDate nTh = firstDow.plusDays((nThDow.ordinal()) * JankDate.DAYS_IN_WEEK);
            final int dayOfMonth = nTh.getDayOfMonth();
            return new JankHoliday(this.name, year, this.month, dayOfMonth, this.type) ;
        }
    }


    public VersionableFactory springEquinoxDay(int startinfYear, int endingYear) {
        addFactoryVerion( startinfYear, endingYear ,EquinoxDay.SPRING_FACTORY);
        return this;
    }    
    
    public VersionableFactory autumnEquinoxDay(int startinfYear, int endingYear) {
        addFactoryVerion( startinfYear, endingYear ,EquinoxDay.AUTUMINAL_FACTORY);
        return this;
    }    

    protected void addFactoryVerion ( int startingYear , int endingYear , Factory factory ) {
        Versioning fv = new Versioning(startingYear, endingYear, factory);
        this.versions.add(fv);
    }

    // 同じ祝日でも時代によって定義内容が変わるので、世代別に複数のFactoryを用意する仕組み
    protected static class Versioning {
        protected final int startingYear;
        protected final int endingYear;
        protected final Factory factory;
        protected Versioning ( int startingYear , int endingYear , Factory factory ) {
            this.startingYear = ( OPEN == startingYear ? Integer.MIN_VALUE : startingYear ) ;
            this.endingYear = endingYear = ( OPEN == endingYear ? Integer.MAX_VALUE : endingYear ) ;
            this.factory = factory;
        }
    }


    @Override
    public JankDateCharacteristic create(int year) {
        Versioning adoption = this.versions.stream()
            .filter((h)-> h.startingYear <= year && year <= h.endingYear)
            .findFirst()
            .orElse(null);
        
        if ( adoption != null ) {
            return adoption.factory.create(year) ;
        } else {
            return null;
        }

    };
}
