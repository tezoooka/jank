package jp.co.jastec.jank.model.header;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    /** 全部入りのリスト */
    private static List<RestCheckItem> ALL = Arrays.asList(LANCH, DINNER, NIGHT, DEEP) ;

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

    public float getDeducationHours(JankTimeRange workStartFinish) {
        float sum = 0 ;
        for ( RestCheckItem r : deductions) {
            sum += r.getDeductionHuors(workStartFinish.lower() , workStartFinish.higher());
        }
        return sum;
    }

    public static List<RestCheckItem> itemList() {
        return ALL ;
    }

    @Deprecated
    public static RestCheckItem choice_(char c) {
        for ( RestCheckItem rc : ALL ) {
            char properChar = rc.getDispString().charAt(0);
            if ( properChar == c) {
                return rc ;
            }
        }
        return null ;
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
        RestCheck.itemList().forEach((item)-> viewString.append(getViewString(item)));
        return new CliView(viewString);
    }

    private String getViewString(RestCheckItem item) {
        if ( !this.deductions.contains(item)) {
            return item.getDispString().substring(0,1) + " ";
        } else {
            return "__ " ;
        }
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

        // public String getDispChar() {
        //     return dispString.substring(0, 1);
        // }

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

