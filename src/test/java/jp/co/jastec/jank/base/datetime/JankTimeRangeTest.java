package jp.co.jastec.jank.base.datetime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class JankTimeRangeTest {
    
    @Test public void 重複パターン１() {
        // XXXXXXXXXX
        // YYYY
        JankTimeRange x = JankTimeRange.of("0900-1800") ;
        JankTimeRange y = JankTimeRange.of("0900-1000") ;
        assertTrue(x.isOverlapped(y));
        assertTrue(y.isOverlapped(x));
    }

    @Test public void 重複パターン２() {
        // XXXXXXXX
        //     YYYYYYYYY
        JankTimeRange x = JankTimeRange.of("0900-1200") ;
        JankTimeRange y = JankTimeRange.of("1000-1500") ;
        assertTrue(x.isOverlapped(y));
        assertTrue(y.isOverlapped(x));
    }

    @Test public void 重複パターン３() {
        // XXXXXXXX
        //   YYYY
        JankTimeRange x = JankTimeRange.of("0900-1600") ;
        JankTimeRange y = JankTimeRange.of("1000-1500") ;
        assertTrue(x.isOverlapped(y));
        assertTrue(y.isOverlapped(x));
    }

    @Test public void 重複しないパターン１() {
        // XXXXX
        //   　 YYYY
        JankTimeRange x = JankTimeRange.of("0900-1600") ;
        JankTimeRange y = JankTimeRange.of("1600-1800") ;
        assertTrue(!x.isOverlapped(y));
        assertTrue(!y.isOverlapped(x));
    }

    @Test public void 重複しないパターン２() {
        // XXXXX
        //     　 YYYY
        JankTimeRange x = JankTimeRange.of("0900-1600") ;
        JankTimeRange y = JankTimeRange.of("1900-2100") ;
        assertTrue(!x.isOverlapped(y));
        assertTrue(!y.isOverlapped(x));
    }

    @Test public void 包含するパターン１() {
        // XXXXXXXXX
        //   YYYY
        JankTimeRange x = JankTimeRange.of("0900-1800") ;
        JankTimeRange y = JankTimeRange.of("1000-1500") ;
        assertTrue( x.isIncluding(y));
        assertTrue(!y.isIncluding(x));
    }

    @Test public void 包含するパターン２() {
        // XXXXXXXXX
        // YYYY
        JankTimeRange x = JankTimeRange.of("0900-1800") ;
        JankTimeRange y = JankTimeRange.of("0900-1500") ;
        assertTrue( x.isIncluding(y));
        assertTrue(!y.isIncluding(x));
    }

    @Test public void 包含するパターン３() {
        // XXXXXXXXX
        //      YYYY
        JankTimeRange x = JankTimeRange.of("0900-1800") ;
        JankTimeRange y = JankTimeRange.of("1000-1800") ;
        assertTrue( x.isIncluding(y));
        assertTrue(!y.isIncluding(x));
    }

    @Test public void 包含するパターン４() {
        // XXXXXXXXX
        // YYYYYYYYY
        JankTimeRange x = JankTimeRange.of("0900-1800") ;
        JankTimeRange y = JankTimeRange.of("0900-1800") ;
        assertTrue( x.isIncluding(y));
        assertTrue( y.isIncluding(x));
    }

    @Test public void 包含しないパターン１() {
        // XXXX
        // 　　YYYY
        JankTimeRange x = JankTimeRange.of("0900-1500") ;
        JankTimeRange y = JankTimeRange.of("1500-1800") ;
        assertTrue(! x.isIncluding(y));
        assertTrue(! y.isIncluding(x));
    }

    @Test public void 包含しないパターン２() {
        // XXXX
        // 　　  YYYY
        JankTimeRange x = JankTimeRange.of("0900-1500") ;
        JankTimeRange y = JankTimeRange.of("1600-1800") ;
        assertTrue(! x.isIncluding(y));
        assertTrue(! y.isIncluding(x));
    }

    @Test public void 包含しないパターン３() {
        // XXXXXXX
        // 　　  YYYY
        JankTimeRange x = JankTimeRange.of("0900-1500") ;
        JankTimeRange y = JankTimeRange.of("1400-1800") ;
        assertTrue(! x.isIncluding(y));
        assertTrue(! y.isIncluding(x));
    }


    @Test public void 文字列からの生成１(){
        JankTimeRange x = JankTimeRange.of("0900-1500") ;
        assertEquals(new JankTime(9,0),x.lower());
        assertEquals(new JankTime(15,0),x.higher());
    }

    @Test public void 文字列からの生成２(){
        JankTimeRange x = JankTimeRange.of("0900","1500") ;
        assertEquals(new JankTime(9,0),x.lower());
        assertEquals(new JankTime(15,0),x.higher());
    }

}
