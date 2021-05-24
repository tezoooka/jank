package jp.co.jastec.jank.base.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** 日付と時間の両方を保持するクラス */
/// 一応作ったけど、たぶん何処でも使っていない。
public class JankDateTime {

    JankDate jDate;
    JankTime jTime;
    
    LocalDateTime localDateTime;

    public JankDateTime() {
        LocalDateTime now = LocalDateTime.now();
        construct(new JankDate(now.toLocalDate()), new JankTime(now.toLocalTime()));
    }

    public JankDateTime(String dateString, String timeString) {
        construct(new JankDate(dateString), new JankTime(timeString));
    }

    private void construct(JankDate jd, JankTime jt) {
        this.jDate = jd;
        this.jTime = jt;
        LocalDate dateAdjusted = jd.localDate() ;
        if ( jt.isOverNight()) {
            dateAdjusted = dateAdjusted.plusDays(1);
        }
        this.localDateTime = LocalDateTime.of(dateAdjusted, jt.localTime());
    }

}
