package jp.co.jastec.jank.base.datetime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class JankDateTest {
    
    @Test public void YYYY_MM_DD形式() {
        String src = "2021/05/31";
        String result = new JankDate(src).toString(); 
        assertEquals(src, result);
    }

    @Test public void YYYY_MM_DD形式_20世紀() {
        String src = "1966/05/31";
        String result = new JankDate(src).toString(); 
        assertEquals(src, result);
    }

    @Test public void YYYY_M_D形式() {
        String src = "2021/05/01";
        String result = new JankDate("2021/5/1").toString(); 
        assertEquals(src, result);
    }

    @Test public void YYYYMMDD形式() {
        String src = "2021/05/31";
        String result = new JankDate("20210531").toString(); 
        assertEquals(src, result);
    }

    @Test public void MMDD形式() {
        String src = "2021/05/31";
        String result = new JankDate("0531").toString(); 
        assertEquals(src, result);
    }

    @Test public void M_D形式() {
        String src = "2021/05/01";
        String result = new JankDate("5/1").toString(); 
        assertEquals(src, result);
    }

    @Test public void M_DD形式() {
        String src = "2021/05/11";
        String result = new JankDate("5/11").toString(); 
        assertEquals(src, result);
    }
    @Test public void MM_D形式() {
        String src = "2021/05/01";
        String result = new JankDate("05/1").toString(); 
        assertEquals(src, result);
    }

    @Test public void 月があり得ない() {
        try {
            new JankDate("13/1");
        } catch ( Exception e ) {
            assertEquals(JankDateTimeException.class, e.getClass());
        }
    }

    @Test public void 日があり得ない() {
        try {
            new JankDate("12/32");
        } catch ( Exception e ) {
            assertEquals(JankDateTimeException.class, e.getClass());
        }
    }

    @Test public void セパレータが違う() {
        try {
            new JankDate("2012-12-01");
        } catch ( Exception e ) {
            assertEquals(JankDateTimeException.class, e.getClass());
        }
    }

    ///

    @Test public void 年の取り出し() {
        assertEquals(2050, new JankDate("2050/1/1").getYear());
    }

    @Test public void 月の取り出し() {
        assertEquals(1, new JankDate("2050/1/1").getDayOfMonth());
    }

    @Test public void 日の取り出し() {
        assertEquals(1, new JankDate("2050/1/1").getDayOfMonth());
    }

    @Test public void 日の加算() {
        JankDate x = new JankDate("2020/2/29").plusDays(1);
        assertEquals(new JankDate("2020/3/1"), x);
        JankDate y = new JankDate("2019/3/1").plusDays(-1);
        assertEquals(new JankDate("2019/2/28"), y);
    }

    @Test public void 等価性() {
        JankDate x = new JankDate(2022, 12, 1) ;
        JankDate y = new JankDate("2022/12/1") ;
        JankDate z = y.plusDays(1);
        
        assertEquals(x, y);
        assertEquals(y, x);
        assertNotEquals(y, z);
        assertNotEquals(y, "2022/12/1");
        assertNotEquals(y, null);
    }


}
