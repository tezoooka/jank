package jp.co.jastec.jank.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FixedWidthStringTest {
    

    @Test public void 連続左寄せ() {

        String body = "AあB愛C";
        String fixWidth;

        fixWidth = FixedWidthString.width(2).left(body) ;
        assertEquals("A ", fixWidth);
        
        fixWidth = FixedWidthString.width(3).left(body) ;
        assertEquals("Aあ", fixWidth);
        
        fixWidth = FixedWidthString.width(4).left(body) ;
        assertEquals("AあB", fixWidth);

        fixWidth = FixedWidthString.width(5).left(body) ;
        assertEquals("AあB ", fixWidth);

        fixWidth = FixedWidthString.width(6).left(body) ;
        assertEquals("AあB愛", fixWidth);

        fixWidth = FixedWidthString.width(7).left(body) ;
        assertEquals("AあB愛C", fixWidth);

        fixWidth = FixedWidthString.width(8).left(body) ;
        assertEquals("AあB愛C ", fixWidth);

        fixWidth = FixedWidthString.width(9).left(body) ;
        assertEquals("AあB愛C  ", fixWidth);

    }


    @Test public void 連続右寄せ() {

        String body = "AあB愛C";
        String fixWidth;

        fixWidth = FixedWidthString.width(2).right(body) ;
        assertEquals(" C", fixWidth);
        
        fixWidth = FixedWidthString.width(3).right(body) ;
        assertEquals("愛C", fixWidth);
        
        fixWidth = FixedWidthString.width(4).right(body) ;
        assertEquals("B愛C", fixWidth);

        fixWidth = FixedWidthString.width(5).right(body) ;
        assertEquals(" B愛C", fixWidth);

        fixWidth = FixedWidthString.width(6).right(body) ;
        assertEquals("あB愛C", fixWidth);

        fixWidth = FixedWidthString.width(7).right(body) ;
        assertEquals("AあB愛C", fixWidth);

        fixWidth = FixedWidthString.width(8).right(body) ;
        assertEquals(" AあB愛C", fixWidth);

        fixWidth = FixedWidthString.width(9).right(body) ;
        assertEquals("  AあB愛C", fixWidth);

    }

    @Test public void 連続中央寄せ() {

        String body = "AあB愛C";
        String fixWidth;

        fixWidth = FixedWidthString.width(2).center(body) ;
        assertEquals("A ", fixWidth);
        
        fixWidth = FixedWidthString.width(3).center(body) ;
        assertEquals("Aあ", fixWidth);
        
        fixWidth = FixedWidthString.width(4).center(body) ;
        assertEquals("AあB", fixWidth);

        fixWidth = FixedWidthString.width(5).center(body) ;
        assertEquals("AあB ", fixWidth);

        fixWidth = FixedWidthString.width(6).center(body) ;
        assertEquals("AあB愛", fixWidth);

        fixWidth = FixedWidthString.width(7).center(body) ;
        assertEquals("AあB愛C", fixWidth);

        fixWidth = FixedWidthString.width(8).center(body) ;
        assertEquals("AあB愛C ", fixWidth);

        fixWidth = FixedWidthString.width(9).center(body) ;
        assertEquals(" AあB愛C ", fixWidth);

        fixWidth = FixedWidthString.width(10).center(body) ;
        assertEquals(" AあB愛C  ", fixWidth);

        fixWidth = FixedWidthString.width(11).center("@") ;
        assertEquals("     @     ", fixWidth);

        fixWidth = FixedWidthString.width(12).center("@") ;
        assertEquals("     @      ", fixWidth);

        fixWidth = FixedWidthString.width(10).center("寿") ;
        assertEquals("    寿    ", fixWidth);

        fixWidth = FixedWidthString.width(11).center("寿") ;
        assertEquals("    寿     ", fixWidth);

        fixWidth = FixedWidthString.width(12).center("寿") ;
        assertEquals("     寿     ", fixWidth);

    }
}
