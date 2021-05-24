package jp.co.jastec.jank.model.detail;

import jp.co.jastec.jank.base.JankList;
import jp.co.jastec.jank.base.JankNode;

/** 生産管理情報詳細 ::: 全然TBD*/
public class Productive extends JankNode {

    private String lcCode ;
    private String firstActivity ;
    private String secondActivity;
    private float hours;

    public Productive(String lcCode, String firstActivity, String secondActivity) {
        super(joinCodes(lcCode, firstActivity, secondActivity));
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    @Override
    public float getHours() {
        return this.hours;
    }

    @Override
    public String code() {
        return joinCodes(this.lcCode, this.firstActivity, this.secondActivity);
    }

    @Override
    protected String getNameByCode() {
        return "";
    }


    private static String joinCodes(String lcCode, String firstActivity, String secondActivity) {
        return String.join("-", lcCode, firstActivity, secondActivity);
    }

    @Override
    public JankList<?> getChildren() {
        return null;
    }

    @Override
    protected boolean isValidFormat(String code) {
        return false;
    }

    
}
