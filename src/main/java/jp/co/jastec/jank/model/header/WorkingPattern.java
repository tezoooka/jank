package jp.co.jastec.jank.model.header;

import com.google.gson.annotations.Expose;

import jp.co.jastec.jank.cli.CliView;
import jp.co.jastec.jank.cli.CliView.CliViewable;

/** 勤務形態 */
public class WorkingPattern implements CliViewable {
    
    public enum DAY { FIRST, SECOND, NONE } ;
    
    // 歴史的な慣行で、〇をつけることになっている
    @Expose
    private boolean[] circle = new boolean[2];

    public void set(DAY day) {

        if (day.ordinal() > circle.length) {
            circle[DAY.FIRST.ordinal()] = false ;
            circle[DAY.FIRST.ordinal()] = false ;
        } else {
            circle[day.ordinal()] = true;
        }

    }

    @Override
    public CliView getView() {
        String day1 = circle[0] ? "〇" : "__";
        String day2 = circle[1] ? "〇" : "__";
        String view =  String.format("勤務形態: 1日目%s   2日目%s", day1, day2);
        return new CliView(view);
    }

}
