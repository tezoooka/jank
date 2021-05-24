package jp.co.jastec.jank.base.datetime;

import jp.co.jastec.jank.base.JankMessage;

/** 二つの時刻（From-To）を抽象的に表現するためのインターフェース */
/// このインターフェースを具備することで、二つの時間幅の重なりや包含をチェックできる。
public interface JankTimeRange {
    
    public JankTime lower() ;
    public JankTime higher();

    public default boolean isOverlapped(JankTimeRange that) {
        //http://koseki.hatenablog.com/entry/20111021/range
        int x = that.lower().value() ;
        int y = that.higher().value();
        int a = this.lower().value();
        int b = this.higher().value();
        return x < b && a < y ;
    }

    public default boolean isIncluding(JankTimeRange innerRange) {
        return  this.lower().value()  <= innerRange.lower().value() &&
                this.higher().value() >= innerRange.higher().value();

    }

    public static JankTimeRange of(String hhmm_hhmm) {

        String[] hm = hhmm_hhmm.split("-") ;
        if (hm.length != 2) {
            throw new JankDateTimeException(JankMessage.DATETIME_ILLEGALFORMAT, hhmm_hhmm);
        }

        return of(hm[0], hm[1]);

    }

    public static JankTimeRange of(String xx, String yy) {

        JankTime x = new JankTime(xx);
        JankTime y = new JankTime(yy);

        return of(x, y);

    }

    public static JankTimeRange of(JankTime x, JankTime y) {
        JankTime lo ;
        JankTime hi ;

        if ( x.value() < y.value() ) {
            lo = x ;
            hi = y ;
        } else {
            lo = y ;
            hi = x ;
        }
        return new JankTimeRange() {
            public JankTime lower() {
                return lo;
            }
            public JankTime higher() {
                return hi;
            };

            @Override
            public String toString() {
                return lo.toDisplayString() + "-" + hi.toDisplayString();
            }
        };
    }


    public static boolean isValidFormat(String hhmm_hhmm) {
        
        String[] hm = hhmm_hhmm.split("-") ;
        if (hm.length != 2) {
            return false;
        }

        if ( null == JankTime.TimeString.check(hm[0]))  {
            return false;
        }

        if ( null == JankTime.TimeString.check(hm[1]))  {
            return false;
        }

        return true;
    } 
}
