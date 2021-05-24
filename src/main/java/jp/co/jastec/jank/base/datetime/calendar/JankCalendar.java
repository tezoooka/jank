package jp.co.jastec.jank.base.datetime.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import jp.co.jastec.jank.base.datetime.JankDate;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.Factory;
import jp.co.jastec.jank.base.datetime.calendar.holidays.EnterpriseHoliday;
import jp.co.jastec.jank.base.datetime.calendar.holidays.NationsDayoff;
import jp.co.jastec.jank.base.datetime.calendar.holidays.PinpointPatch;
import jp.co.jastec.jank.base.datetime.calendar.holidays.PublicNamedHoliday;
import jp.co.jastec.jank.base.datetime.calendar.holidays.SubstitudeHoliday;

/** 一年分の休日カレンダー */
public class JankCalendar extends TreeMap<JankDate, JankDateCharacteristic>  {

    /** 生成済みの年のカレンダーをキャッシュするマップ */
    private static final Map<Integer, JankCalendar> cache = new HashMap<>() ;

    // このインスタンス自身を意味するカレンダーの年（キャッシュのキーにもなる）
    int year;

    private JankCalendar (int year) {
        this.year = year;
        this.createHolidayMap(year);
    }

    /** year年の祝日カレンダーを返す */
    public static JankCalendar yearOf(int year) {
        JankCalendar jc = cache.get(year);
        if ( null == jc ) {
            jc = new JankCalendar(year);
            cache.put(year, jc);
        }
        return jc;
    } 
    
    public int getYear() {
        return this.year;
    }

    /** 一年分の休日Mapを生成する */
    private void createHolidayMap(int year) {

        // 一般的な祝日
        for ( Factory f : PublicNamedHoliday.getFactories() ) {
            this.putHoliday( f ) ;
        }

        // 振替休日を追加（祝日と日曜日が重なったとき）
        for (Factory f : SubstitudeHoliday.of(this).getFactories()) {
            this.putHoliday( f ) ;
        }

        // 国民の休日の追加（祝日に挟まれた平日）
        for (Factory f : new NationsDayoff(this).getFactories()) {
            this.putHoliday( f ) ;
        }

        // 企業独自の休日
        for (Factory f : EnterpriseHoliday.getFactories()) {
            this.putHoliday( f ) ;
        }

        // ロジックで判定できない日のピンポイント補正
        for (Factory f : PinpointPatch.getFactories()) {
            this.putHoliday( f ) ;
        }

    }

    private void putHoliday(Factory factory) {

        JankDateCharacteristic holiday = factory.create(year);
        if (holiday != null ) {
            super.put(holiday.getDate(), holiday);
        }
        
        JankDate negating = factory.negate() ;
        if ( negating != null) {
            JankDateCharacteristic cha = this.get(negating) ;
            if ( cha != null && cha instanceof JankHoliday) {
                ((JankHoliday) cha).negate();
            }
        }

    }

    /** Y年M月の最初のX曜日を求める */
    public static JankDate findFirstDayOfWeek(int year, int month, DayOfWeek dow) {
        final LocalDate first = LocalDate.of(year, month, 1) ;
        int offset = dow.getValue() - first.getDayOfWeek().getValue();
        offset = ( offset < 0 ) ? offset += JankDate.DAYS_IN_WEEK : offset ;
        return new JankDate(first.plusDays(offset)) ;
    }

    public static final void main(String args[]) {
        int yyyy1;
        int yyyy2;
        if ( args.length == 0 ) {
            yyyy1 = LocalDate.now().getYear();
            yyyy2 = yyyy1;

        } else if ( args.length == 1 ) {
            yyyy1 = Integer.parseInt(args[0]);
            yyyy2 = yyyy1;

        } else  {
            yyyy1 = Integer.parseInt(args[0]);
            yyyy2 = Integer.parseInt(args[1]);
        }

        for ( int y = yyyy1 ; y <= yyyy2 ; y++) {
            JankCalendar jc = JankCalendar.yearOf(y);
            for ( JankDateCharacteristic cha : jc.values()) {
                System.out.println(cha.toDisplayString());
            }
        }
    }


}