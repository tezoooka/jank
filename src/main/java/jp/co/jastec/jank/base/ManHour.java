package jp.co.jastec.jank.base;

public interface ManHour {
    public float getHours() ;

    public static boolean is05Unit(float hours) {
        int x10int = (int) (hours * 10 );
        return ( x10int % 5 == 0 );
    }
}
