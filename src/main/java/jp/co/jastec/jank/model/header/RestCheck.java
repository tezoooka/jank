package jp.co.jastec.jank.model.header;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;

import jp.co.jastec.jank.base.JankMessage;
import jp.co.jastec.jank.base.datetime.JankTime;
import jp.co.jastec.jank.base.datetime.JankTimeRange;
import jp.co.jastec.jank.cli.CliView;
import jp.co.jastec.jank.cli.CliView.CliViewable;

/**
 * 休憩チェックなる謎多き仕様の実装
 */
public class RestCheck implements CliViewable {

    /** 昼食チェック */
    public static final RestCheckItem LANCH  = RestCheckItem.create( "12:00", "13:00", "昼食" ) ;
    /** 夕食チェック */
    public static final RestCheckItem DINNER = RestCheckItem.create( "18:00", "18:30", "夕食" ) ;
    /** 夜間チェック */
    public static final RestCheckItem NIGHT  = RestCheckItem.create( "22:00", "22:30", "夜間" ) ;
    /** 深夜チェック */
    public static final RestCheckItem DEEP   = RestCheckItem.create( "26:00", "27:00", "深夜" ) ;


    /** 全部入り */
    private static Collection<RestCheckItem> ALL = Arrays.asList(LANCH, DINNER, NIGHT, DEEP) ;

    @Expose
    private Set<RestCheckItem> deductions;

    public RestCheck() {
        // 最初は全部控除対象になる
        deductions = new HashSet<>(ALL) ;
    }

    // チェックされたら控除対象から外す
    public void check(RestCheckItem item) {
        this.deductions.removeIf((r)->r == item) ;
    }

    // チェックされたら控除対象に含める
    public void uncheck(RestCheckItem item) {
        this.deductions.add(item);
    }

    public float getDeducationHours(JankTime startTime, JankTime finishTime) {
        float sum = 0 ;
        for ( RestCheckItem r : deductions) {
            sum += r.getDeductionHuors(startTime , finishTime);
        }
        return sum;
    }

    public static Collection<RestCheckItem> items() {
        return ALL ;
    }

    public JankMessage validateWithWorkingTime( JankTimeRange fromTo ) {
        for ( RestCheckItem d : this.deductions) {
            if ( !fromTo.isIncluding(d)) {
                return JankMessage.INVALID_RESTCHECK;
            }
        }
        return null;
    }

    @Override
    public CliView getView() {

        StringBuilder viewString = new StringBuilder(); 
        viewString.append("休憩控除: ");
        RestCheck.items().forEach((item)-> viewString.append(getViewString(item)));
        return new CliView(viewString);

    }

    private String getViewString(RestCheckItem item) {
        if ( !this.deductions.contains(item)) {
            return item.getDispString().substring(0,1) + " ";
        } else {
            return "__ " ;
        }
    }

    public static JsonSerializer<RestCheck> JSON_SERIALIZER = new JsonSerializer<RestCheck>(){
        @Override
        public JsonElement serialize(RestCheck src, Type typeOfSrc, JsonSerializationContext context) {

            Set<RestCheckItem> unchecked = new HashSet<>();
            for (RestCheckItem rci : ALL ) {
                if ( !src.deductions.contains(rci)) {
                    unchecked.add(rci);
                } 
            }
            String csv =  unchecked.stream().map((i) -> i.dispString).collect(Collectors.joining(","));
            return new JsonPrimitive(csv);
        }        
    };


    public static JsonDeserializer<RestCheck> JSON_DESERIALIZER = new JsonDeserializer<RestCheck>(){
        @Override
        public RestCheck deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String[] rests = json.getAsString().split(",");
            RestCheck restCheck = new RestCheck();
            for (String restString : rests) {
                RestCheckItem serielized = get(restString);
                if ( null == serielized ) {
                    throw new JsonParseException(json.getAsString());
                }
                restCheck.check(serielized);
            }
            return null;
        }
    };

    public static RestCheckItem get(String dispString) {
        for ( RestCheckItem item : ALL ) {
            if ( item.dispString.equals(dispString)) {
                return item;
            }
        }
        return null;
    }

    public static class RestCheckItem implements JankTimeRange {

        private JankTime startAt;
        private JankTime endAt;
        
        @Expose
        private String dispString;

        private RestCheckItem(String startAt, String endAt, String dispChar ) {
            this.startAt = new JankTime(startAt);
            this.endAt = new JankTime(endAt);
            this.dispString = dispChar;
        }

        @Deprecated
        public float getDeductionHuors(JankTime start, JankTime end) {
            if ( this.isOverlapped(JankTimeRange.of(start, end))  ) {
                return getDurationHours();
            } else {
                return 0 ;
            }
        }

        public float getDeductionHuors(JankTimeRange range) {
            if ( this.isOverlapped(range)) {
                return getDurationHours();
            } else {
                return 0 ;
            }
        }

        private float getDurationHours() {
            return JankTime.hoursBetween(startAt, endAt);
        }

        public String getDispString() {
            return dispString;
        }

        private static RestCheckItem create(String startAt, String endAt, String dispChar) {
            return new RestCheckItem(startAt, endAt, dispChar) ;
        }

        @Override public JankTime lower() { return this.startAt; }

        @Override public JankTime higher() {return this.endAt; }
        
        @Override public String toString() { return this.dispString; }
    }


}

