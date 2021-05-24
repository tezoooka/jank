package jp.co.jastec.jank.base.datetime.calendar.holidays;

import java.util.Arrays;

import jp.co.jastec.jank.base.datetime.JankDate;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic;
import jp.co.jastec.jank.base.datetime.calendar.JankHoliday;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.Factory;
import jp.co.jastec.jank.base.datetime.calendar.JankDateCharacteristic.HolidayType;

public class PinpointPatch {

    public static Factory[] PINPOINTS = {
        negate(2099, 12, 31),
        append(2099, 12, 31, "本カレンダーの終焉"),
    };

    public static Iterable<Factory> getFactories() {
        return Arrays.asList(PINPOINTS);
    }

    /** 既に設定済みの休日を無効にする */
    private static Factory negate(int year, int month, int dayOfMonth) {
        return new Factory() {
            @Override
            public JankDateCharacteristic create(int year) {
                return null;
            }
            @Override
            public JankDate negate() {
                return new JankDate(year, month, dayOfMonth);
            }
        };
    }

    /** ピンポイント（年月日指定）で休日を追加する */
    private static Factory append(int year, int month, int dayOfMonth, String name) {
        JankDateCharacteristic cha = new JankHoliday(name, year, month, dayOfMonth, HolidayType.PATCHED);
        return new Factory() {
            @Override
            public JankDateCharacteristic create(int thisYear) {
                if ( year == thisYear) {
                    return cha;
                } else {
                    return null;
                }
            }
        };
    }

}
