package jp.co.jastec.jank.model.header;

import java.util.Arrays;

import com.google.gson.annotations.Expose;

/** 休暇種別 */
public enum DayOffType {
 
    PAID_HOLIDAY    ("有給休暇", "0"),
    AM_ABSENCE      ("午前休","2"),
    PM_ABSENCE      ("午後休","3"),
    SPECIAL5        ("特休５号","5"),
    SUMMER_VAC      ("夏期休暇","8"),
    SPECIAL5_NEXT   ("特休５号２日以降","12"),
    ALTERNATIVE     ("代休","17"),
    AFTER_OVRTNIGHT ("明休","18"),
    OTHER_FOR_BOARD ("その他特休（役員）","19"),
    ALLDAY_ABSENCE  ("欠勤","20"),
    
    ;

    @Expose
    private String name ;
    @Expose
    private String code ;

    private DayOffType(String name, String code) {
        this.name = name;
        this.code = code;
    }
    public String getName() { return this.name; }
    public String getCode() { return this.code; }

    public boolean isHalf() { 
        return (this == AM_ABSENCE) || (this == PM_ABSENCE ) ;
    }

    public static DayOffType choice(String code) {
        return Arrays.stream(DayOffType.values())
            .filter((h)->h.code.equals(code))
            .findFirst()
            .orElse(null)
            ;
    }
}
