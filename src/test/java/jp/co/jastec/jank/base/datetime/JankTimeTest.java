package jp.co.jastec.jank.base.datetime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class JankTimeTest {


    /// インスタンス生成

    @Test public void コロンあり4桁() {
        JankTime jankTime = new JankTime("9:00");
        LocalTime localTime = LocalTime.of(9, 0, 0);
        assertEquals(localTime, jankTime.localTime());
    }

    @Test public void コロンあり3桁() {
        JankTime jankTime = new JankTime("23:30");
        LocalTime localTime = LocalTime.of(23, 30, 0);
        assertEquals(localTime, jankTime.localTime());
    }

    @Test public void コロン無し4桁() {
        JankTime jankTime = new JankTime("0930");
        String strTime = "09:30";
        assertEquals(strTime, jankTime.toDisplayString());
    }

    @Test public void コロン無し3桁() {
        JankTime jankTime = new JankTime("930");
        String strTime = "09:30";
        assertEquals(strTime, jankTime.toDisplayString());
    }

    @Test public void 真夜中24時越え() {
        JankTime jankTime = new JankTime("25:00");
        LocalTime localTime = LocalTime.of(1, 00, 0);
        assertEquals(localTime, jankTime.localTime());
    }

    @Test public void 朝33時() {
        JankTime jankTime = new JankTime("33:30");
        LocalTime localTime = LocalTime.of(9, 30, 0);
        assertEquals(localTime, jankTime.localTime());
    }

    @Test public void 分の刻みが正しい() {
        JankTime jankTime = new JankTime("9:30");
        String strTime = "09:30";
        assertEquals(strTime, jankTime.toDisplayString());
    }

    @Test public void LocalTimeから() {
        JankTime jankTime = new JankTime(LocalTime.of(13, 30, 00));
        String strTime = "13:30";
        assertEquals(strTime, jankTime.toDisplayString());
    }

    @Test public void LocalTimeから切り捨て() {
        JankTime jankTime = new JankTime(LocalTime.of(13, 15, 20));
        String strTime = "13:00";
        assertEquals(strTime, jankTime.toDisplayString());
    }


    @Test public void 分の刻みが間違い() {
        try {
            new JankTime("9:45");
            fail();
        } catch (Exception e) {
            assertEquals(JankDateTimeException.class, e.getClass());
        }
    }

    @Test public void 流石に遅すぎる() {
        try {
            new JankTime("34:00");
            fail();
        } catch (Exception e) {
            assertTrue(true);;
        }
    }


    // メソッド類

    @Test public void 時刻の取り出し() {
        JankTime jt = new JankTime(22, 30) ;
        assertEquals(22, jt.hour());
    }

    @Test public void 分の取り出し() {
        JankTime jt = new JankTime(22, 30) ;
        assertEquals(30, jt.minute());
    }

    @Test public void 時間差の算出() {
        JankTime x = new JankTime(9, 00) ;
        JankTime y = new JankTime("32:30") ;
        assertEquals(23.5f, JankTime.hoursBetween(x, y));
    }

    @Test public void 時間差の算出_逆順() {
        JankTime x = new JankTime(9, 00) ;
        JankTime y = new JankTime("18:30") ;
        assertEquals(9.5f, JankTime.hoursBetween(y, x));
    }

    @Test public void 等価性() {

        JankTime x = new JankTime(14,30) ;
        JankTime y = new JankTime(14,30) ;
        JankTime z = new JankTime(15,00) ;
        assertEquals(x, x);
        assertEquals(x, y);
        assertNotEquals(y, z);

    }

    @Test public void 比較() {

        JankTime x = new JankTime(14,30) ;
        JankTime y = new JankTime(14,30) ;
        JankTime z = new JankTime(15,00) ;
        assertTrue(x.compareTo(y) == 0 );
        assertTrue(x.compareTo(z) < 0 );
        assertTrue(z.compareTo(x) > 0 );

    }

}
