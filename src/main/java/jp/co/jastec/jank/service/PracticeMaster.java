package jp.co.jastec.jank.service;

import java.io.File;
import java.util.SortedMap;

/** 活動コードマスター */
public class PracticeMaster extends JankMaster<String> {
    
    private static PracticeMaster mySelf = null;

    private PracticeMaster() {
        super();
        loadCsv();
    }


    @Override
    public File getFile() {
        return new File(JankHome.MASTER_DIR, "practice.csv");
    }

    @Override
    public String parse(String... values) {
        return values[1].trim();
    }

    public static PracticeMaster table() {
        if ( null == mySelf ) {
            mySelf = new PracticeMaster();
        }
        return mySelf;
    }


    /** empNoで示される社員が対象としている活動コードとその名称をMapで返す。 */
    public SortedMap<String, String> selectMyPractice(String empNo) {
        /// 本来なら empNoによる絞り込みを行うが、ここでは省略
        return table();
    }

    /** 活動コードに対応する名称を返す。見つからない場合はnull */
    public String nameOf(String practiceCode) {
        return this.get(practiceCode);
    }

}
