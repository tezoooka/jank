package jp.co.jastec.jank.service;

import java.io.File;
import java.util.SortedMap;

/** 活動コードマスター */
public class EmpMaster extends JankMaster<String> {
    
    private static EmpMaster mySelf = null;

    private EmpMaster() {
        super();
        loadCsv();
    }


    @Override
    public File getFile() {
        return new File(JankHome.MASTER_DIR, "employee.csv");
    }

    @Override
    public String parse(String... values) {
        return values[1].trim();
    }

    public static EmpMaster table() {
        if ( null == mySelf ) {
            mySelf = new EmpMaster();
        }
        return mySelf;
    }

    /** すべての活動コードとその名称をMapで返す */
    public SortedMap<String, String> all() {
        return this;
    }

    /** 活動コードに対応する名称を返す。見つからない場合はnull */
    public String nameOf(String empNo) {
        return this.get(empNo);
    }

}
