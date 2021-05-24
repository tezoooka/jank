package jp.co.jastec.jank.base;

/**
 * 固定幅文字列 
 * 単純な文字数ではなく、文字幅（等幅フォントの場合の全角・半角）を基準に、固定長の文字列を生成する
 * 
 * String fixed = FixedWidthString.width(8).right("3.14") ;
 * 
 */
/// 普通に文字数ベース右寄せ・左寄せするのは簡単だけど、CLIで桁揃えする際に、
/// 全角半角が混ざると意外に面倒なので作成したクラス。
public class FixedWidthString {

    private int width;
    private char fillingChar =  ' ';

    private boolean nullSafe = true;

    private FixedWidthString(){}

    public static FixedWidthString width(int width) {
        FixedWidthString fws = new FixedWidthString();
        fws.width = width;
        return fws;
    }

    public FixedWidthString paddngChar(char paddingChar) {
        assert(isAscii(paddingChar)) : "paddingChar must be ASCII." ;
        this.fillingChar = paddingChar;
        return this;
    }

    public String left(String format, Object... args) {
        final String body = String.format(format, args);
        return left(body);
    }

    public String right(String format, Object... args) {
        final String body = String.format(format, args);
        return right(body);
    }

    public String center(String format, Object... args) {
        final String body = String.format(format, args);
        return center(body);
    }

    public String left(CharSequence body) {
        
        if (this.nullSafe && body == null) {
            body = "";
        }

        final int fillWidth = this.width - calcWidth(body);

        if (fillWidth >= 0) {
            return body.toString() + padding(fillWidth);

        } else {
            return shrink(body);
        }
    }


    public String right(CharSequence body) {
        /// left()メソッドを左右対称にした実装もしてみたが、
        /// 似たようなコードになって冗長だったので、left()を流用する
        return reverse(left(reverse(body))).toString();
    }

    public String center(CharSequence body) {
        if (this.nullSafe && body == null) {
            body = "";
        }

        final int fillWidth = this.width - calcWidth(body);

        if (fillWidth >= 0) {
            final int leftFillWidth = fillWidth / 2;   // 切り捨てなので奇数の場合はちょっと左による
            final int rightFillWidth = fillWidth - leftFillWidth;

            return new StringBuilder()
                        .append(padding(leftFillWidth))
                        .append(body.toString())
                        .append(padding(rightFillWidth))
                        .toString();

        } else {
            return shrink(body);
        }

    }

    // 指定幅に縮める
    /// 余分な空白を埋めるよりも、こっちのほうが難しい
    private String shrink(CharSequence body) {

        final StringBuilder sb = new StringBuilder();
        int currentWidth = 0 ;
        for (int i = 0 ; i < body.length(); i++) {
            final char c = body.charAt(i);                
            currentWidth += isAscii(c) ? 1 : 2;

            final int remainChars = this.width - currentWidth ;
            if ( remainChars >= 0 ) {  // ピッタリか、まだ余裕があるとき
                sb.append(body.charAt(i));

            } else if ( remainChars == -1 && (!isAscii(c))) { // 全角を埋めるとオーバーするけど半角1文字分余っているとき
                sb.append(this.fillingChar);
                break ;

            } else { // 全幅埋まってしまった
                break;
            }
        }
        return sb.toString();
    }
    
    // 文字列の前後を逆転
    private static CharSequence reverse(CharSequence str) {
        return new StringBuilder(str).reverse();
    }

    // 文字を繰り返す
    private CharSequence padding(int n) {
        final StringBuilder sb = new StringBuilder();
        while ( n-- > 0 ) sb.append(this.fillingChar);
        return sb; 
    }

    // 文字列の幅（非ASCIIを2と数える）を求める
    public static int calcWidth(CharSequence body) {
        int width = 0 ;
        for (int i=0 ; i < body.length() ; i++ ) {
            width += isAscii(body.charAt(i)) ? 1 : 2 ;
        }
        return width ;
    }

    // 半角文字判定　（簡易形なので半角カナは含まない）
    private static boolean isAscii(char c) {
        return c < 0x7f ;
    }
}
