package jp.co.jastec.jank.base.datetime;

import java.lang.reflect.Type;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.regex.Pattern;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/** 時刻を示すクラス */
/// 日計入力というアプリケーションにおいて、時刻や時間のチェックや計算がかなりの部分を占める
/// ので、このアプリケーション用に専用クラスを設けることにした。
public class JankTime implements Comparable<JankTime> {

    /** 通常の始業時刻 */
    public static final JankTime STD_WORKSTART = new JankTime(9,0) ;
    
    /** 通常の終業時刻 */
    public static final JankTime STD_WORKFINISH = new JankTime(18,0) ;
    
    /** 半休のときの始業・終業時刻 */
    public static final JankTime HALF_HOLIDAY_POINT = new JankTime(14,0) ;

    /** 就業管理上の分の刻み */
    public static final int ROUNDING_UNIT = 30 ;
    
    /** 25時以降を何時まで認めるか */
    private  static final int MAX_OVERNIGHT_HOUR = 33 ;
    
    public static final int MINS_PER_HOURS = 60 ;

    /** 0:00からの経過分 */
    /// インスタンスの実体は、分で持つ
    /// LocalTimeを使う方法や、時と分を別々に持つ方法など、いくつかの実装案があり得るが、
    /// 差分計算（時刻から時間を求める）や、比較の容易性を鑑み、分のみで保持する方法を選択した。
    private int minutesValue;

    /** 時・分から生成 */
    public JankTime(int hour, int minute) {
        construct(hour, minute);
    }

    /** 現在時刻 */
    public JankTime() {
        this(LocalTime.now()) ;
    }

    public JankTime(String timeString) {
        final int[] hhmm = TimeString.parse(timeString) ;
        construct(hhmm[0], hhmm[1]);
    }

    public JankTime(LocalTime localTime) {
        final int hh = localTime.getHour();
        final int mm = localTime.getMinute();
        //30分未満を切り捨て
        final int rounded = ( mm / ROUNDING_UNIT ) * ROUNDING_UNIT;
        construct(hh, rounded);
    }

    public boolean isOverNight() {
        return this.minutesValue >= 24 * MINS_PER_HOURS ;
    }

    private void construct(int hour, int minute) {
        if ( minute % ROUNDING_UNIT != 0 ) {
            throw new JankDateTimeException() ;
        }
        if ( hour > MAX_OVERNIGHT_HOUR ) {
            throw new JankDateTimeException() ;
        }
        this. minutesValue = hour * MINS_PER_HOURS + minute;
    }

    public LocalTime localTime() {
        int hh = this.hour() ;
        if ( isOverNight() ) {
            hh -= 24 ;
        }
        int mm = this.minute() ;
        int ss = 0;
        return LocalTime.of(hh, mm, ss) ;
    }

    public int value() {
        return this.minutesValue;
    }

    public int hour() {
        return this.minutesValue / MINS_PER_HOURS ;
    }

    public int minute() {
        return this.minutesValue % MINS_PER_HOURS ;
    }

    // public boolean isSameTime(JankTime that) {
    //     return this.value() == that.value();
    // }

    // public boolean isSameTime(String that) {
    //     return this.value() == new JankTime(that).value();
    // }

    public String toDisplayString() {
        return String.format("%02d:%02d", hour(), minute() ) ;
    }

    @Override
    public String toString() {
        return toDisplayString() + " " + super.toString();
    }

    @Override
    public int hashCode() {
        return this.minutesValue % ROUNDING_UNIT ;
    }

    @Override
    public boolean equals(Object obj) {
        if ( (obj != null ) && ( obj instanceof JankTime )) {
            JankTime that = (JankTime) obj;
            return this.minutesValue == that.minutesValue;
        }
        return false;
    }

    @Override
    public int compareTo(JankTime that) {
        return Integer.compare(this.minutesValue, that.minutesValue);
    }


    public static float hoursBetween( JankTime x, JankTime y) {
        // Duration duration = Duration.between(x.localTime(), y.localTime());
        // return ( duration.getSeconds() / 60 ) / 60.0f ;
        final int minutes = x.minutesValue - y.minutesValue;
        return Math.abs( minutes / 60f) ;
    }

    // DateStringは正規表現を使ったが、こっちは敢えて自力で（ただし宣言的に）書く 
    public static class TimeString {
        private TimeString() {}

        enum Format {

            HH_MM   ("[0-3][0-9]:[0-6][0-9]" , 0, 2, 3, 5),
            H_MM    (     "[0-9]:[0-6][0-9]" , 0, 1, 2, 4),
            HHMM    ("[0-3][0-9][0-6][0-9]"  , 0, 2, 2, 4),
            HMM     (     "[0-9][0-6][0-9]"  , 0, 1, 1, 3),

            ;
            final Pattern pattern;
            final int hhBegin;
            final int hhEnd;
            final int mmBegin;
            final int mmEnd;

            Format(String regExp, int hhBegin, int hhEnd, int mmBegin, int mmEnd) {
                pattern = Pattern.compile(regExp);
                this.hhBegin = hhBegin;
                this.hhEnd = hhEnd;
                this.mmBegin = mmBegin;
                this.mmEnd = mmEnd;
            }

        }

        public static Format check(String toBeChecked) {
            for (Format f : Format.values()) {
                if ( f.pattern.matcher(toBeChecked).matches()) {
                    return f;
                }
            }
            return null;
        }

        public static int[] parse(String toBeChecked) {
            Format f = check(toBeChecked);
            if ( f == null ) {
                throw new DateTimeException(toBeChecked);
            }

            int[] hhmm = new int[2];
            hhmm[0] = parseIntSubstr(toBeChecked, f.hhBegin, f.hhEnd) ;
            hhmm[1] = parseIntSubstr(toBeChecked, f.mmBegin, f.mmEnd) ;
            return hhmm;
        }

        private static int parseIntSubstr(String s, int beginIndex , int endIndex ) {
            return Integer.parseInt(s.substring(beginIndex, endIndex));
        }


    }


    public static JsonSerializer<JankTime> JSON_SERIALIZER = new JsonSerializer<JankTime>(){
        @Override
        public JsonElement serialize(JankTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toDisplayString());
        }        
    };


    public static JsonDeserializer<JankTime> JSON_DESERIALIZER = new JsonDeserializer<JankTime>(){
        @Override
        public JankTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new JankTime(json.getAsString());
        }
    };


}