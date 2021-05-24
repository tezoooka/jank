package jp.co.jastec.jank.base.datetime;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.base.datetime.calendar.JankCalendar;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic;

/** 日付（年・月・日）を示すクラス */
/// 実体は LocalDate だが、LocalDate は拡張できないので、委譲して機能を付加/制限している。
/// 　無駄に間口の広いparse機能
/// 　休日判定
public class JankDate implements Comparable<JankDate> {

    public static int DAYS_IN_WEEK = DayOfWeek.values().length; // 7のこと

    private static final LocalDate MOST_PAST_LOCALDATE = LocalDate.of(1900,1,1) ;
    private static final LocalDate MOST_FUTURE_LOCALDATE = LocalDate.of(2100,12,31) ;

    public static final JankDate MOST_PAST = new JankDate(MOST_PAST_LOCALDATE) ;
    public static final JankDate MOST_FUTURE = new JankDate(MOST_FUTURE_LOCALDATE) ;

    private final LocalDate localdate ;

    public JankDate() {
        this(LocalDate.now());
    }

    public JankDate(String dateString) {
        this(DateString.parse(dateString));
    }

    public JankDate(int year, int month, int dayOfMonth) {
        this(LocalDate.of(year, month, dayOfMonth));
    }

    public JankDate(LocalDate localDate) {
        if ( localDate.isBefore(MOST_PAST_LOCALDATE) ||
             localDate.isAfter(MOST_FUTURE_LOCALDATE)) {
            throw new JankDateTimeException(JankMessage.OUT_OF_DATE_RANGE);
        }
        this.localdate = localDate;

    }

    public JankDate(JankDate jankDate) {
        this(jankDate.localDate());
    }

    // LocalDateのよく使うメソッドをオーバーライド的に実装
    public JankDate plusDays(long days) {
        LocalDate ld = this.localDate().plusDays(days);
        return new JankDate(ld);
    }

    public int getYear() {
        return localdate.getYear() ;
    }

    public int getMonthValue() {
        return localdate.getMonthValue();
    }

    public int getDayOfMonth() {
        return localdate.getDayOfMonth();
    }

    public DayOfWeek getDayOfWeek() {
        return this.localdate.getDayOfWeek();
    }

    // 以降はJankDate特有のメソッド

    public LocalDate localDate() {
        return this.localdate;
    }

    public boolean isHoliday() {
        return getCharacteristic().isHoliday();
    }

    public JankDateCharacteristic getCharacteristic() {
        if ( this instanceof JankDateCharacteristic) {
            return (JankDateCharacteristic) this;
        } else {
            return getCharacteristic(this);
        }
    }

    /** 該当日の日付属性を取り出す。 */
    public static JankDateCharacteristic getCharacteristic(JankDate date) {
        final int year = date.localDate().getYear();
        final JankCalendar jc = JankCalendar.yearOf(year) ;
        return  jc.getOrDefault(date, JankDateCharacteristic.ordinaryDay(date));
    }

    public String toStringWithDow() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd(E)");
        return this.localdate.format(f);
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return this.localdate.format(f);
    }

    // 以下、基礎的なinterfaceの実装(HashMapの要素になるので)

    @Override
    public int hashCode() {
        return this.localdate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ( (obj != null ) && ( obj instanceof JankDate )) {
            JankDate that = (JankDate) obj;
            return this.localdate.equals(that.localdate);
        }
        return false;
    }

    @Override
    public int compareTo(JankDate that) {
        return this.localDate().compareTo(that.localDate());
    }
    
    // 以下は、各種ヘルパー内部クラス

    /** 日付文字列形式のチェックと解釈 */
    public static class DateString {
        private DateString() {}

        //許容する形式
        public enum AvaliableFormat {

            YYYYMMDD("yyyyMMdd",    "^(20|19)[0-9][0-9](0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$"),
            YYYY_M_D("yyyy/M/d",    "^(20|19)[0-9][0-9]/[0-9]{1,2}/[0-9]{1,2}$" ),
            MMDD    ("MMdd",        "^(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$"),
            MM_DD   ("M/d",         "^[0-9]{1,2}/[0-9]{1,2}$"),
            ;

            private Pattern regx;
            private DateTimeFormatter format; 

            private AvaliableFormat(String format, String regex) {
                this.regx = Pattern.compile(regex);
                this.format = DateTimeFormatter.ofPattern(format);
            }
            
            Pattern getRegx() {
                return regx;
            }

            DateTimeFormatter getFormat() {
                return format;
            }

        }

        /**
         * 文字列が日付形式を満たしているか確認する。<br>
         * OKの場合は、parse可能なDateTimeFormatterを返す。<br>
         * NGの場合は、nullを返す。
         **/
        public static AvaliableFormat check(String dateString) {

            for (AvaliableFormat format : AvaliableFormat.values()) {
                Pattern p = format.getRegx();
                if ( p.matcher(dateString).find() ) {
                    return format;
                }
            }
            return null;
        }

        public static LocalDate parse(String dateString) {

            AvaliableFormat checkedFormat = check(dateString) ;
            if ( null == checkedFormat ) {
                throw new JankDateTimeException(JankMessage.DATETIME_ILLEGALFORMAT, dateString);
            }

            // MMDDにYYYYを補完
            if ( checkedFormat == AvaliableFormat.MMDD ) {
                String yyyy = String.valueOf(LocalDate.now().getYear());
                dateString = yyyy + dateString ;
                checkedFormat = AvaliableFormat.YYYYMMDD;
            }

            // MM/DDにYYYYを補完
            if ( checkedFormat == AvaliableFormat.MM_DD ) {
                String yyyy = String.valueOf(LocalDate.now().getYear());
                dateString = yyyy + "/" + dateString ;
                checkedFormat = AvaliableFormat.YYYY_M_D;
            }

            try {
                return LocalDate.parse(dateString, checkedFormat.getFormat()) ; 
            } catch ( DateTimeParseException e) {
                throw new JankDateTimeException(JankMessage.DATETIME_ILLEGALFORMAT,dateString) ;
            }

        }

    }

    public static JsonSerializer<JankDate> JSON_SERIALIZER = new JsonSerializer<JankDate>(){
        @Override
        public JsonElement serialize(JankDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    };


    public static JsonDeserializer<JankDate> JSON_DESERIALIZER = new JsonDeserializer<JankDate>(){
        @Override
        public JankDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new JankDate(json.getAsString());
        }
    };

}
