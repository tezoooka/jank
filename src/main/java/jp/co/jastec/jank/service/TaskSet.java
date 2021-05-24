package jp.co.jastec.jank.service;

import java.util.ArrayList;
import java.util.List;

import jp.co.jastec.jank.base.JankCodeElement;

public enum TaskSet {

    DEV(
        new TaskElement("BDS", "基本設計書作成"),
        new TaskElement("BDR", "基本設計書レビュー"),
        new TaskElement("PKS", "外部設計書作成"),
        new TaskElement("PKR", "外部設計書レビュー"),
        new TaskElement("PGS", "内部設計書作成"),
        new TaskElement("PGR", "内部設計書レビュー"),
        new TaskElement("CDS", "コーディング"),
        new TaskElement("CDR", "コードレビュー"),
        new TaskElement("UTS", "単体テスト仕様書作成"),
        new TaskElement("UTR", "単体テスト実施"),
        new TaskElement("CTS", "結合テスト仕様書作成"),
        new TaskElement("CTR", "結合テスト実施"),
        new TaskElement("STS", "システムテスト仕様書作成"),
        new TaskElement("STR", "システムテスト実施"),
        new TaskElement("APP", "付帯サービス")
    ),

    MANAGE(
        new TaskElement("110", "予算管理"),
        new TaskElement("120", "収益出来高管理"),
        new TaskElement("130", "開発プロジェクト管理(定量管理、プロセス改善)"),
        new TaskElement("140", "顧客合意(報告)"),
        new TaskElement("150", "外部委託(発注)管理")
    ),

    SALES(
        new TaskElement("K11", "既）ドメイン設定"),
        new TaskElement("K12", "既）社外啓蒙活動"),
        new TaskElement("K13", "既）市場顧客動向調査"),
        new TaskElement("K14", "既）顧客情報収集"),
        new TaskElement("K15", "既）案件掘り起こし"),
        new TaskElement("K23", "既）見積提案"),
        new TaskElement("J11", "準）ドメイン設定"),
        new TaskElement("J12", "準）社外啓蒙活動"),
        new TaskElement("J13", "準）市場顧客動向調査"),
        new TaskElement("J14", "準）顧客情報収集"),
        new TaskElement("J15", "準）案件掘り起こし"),
        new TaskElement("J23", "準）見積提案"),
        new TaskElement("S11", "新）ドメイン設定"),
        new TaskElement("S12", "新）社外啓蒙活動"),
        new TaskElement("S13", "新）市場顧客動向調査"),
        new TaskElement("S14", "新）顧客情報収集"),
        new TaskElement("S15", "新）案件掘り起こし"),
        new TaskElement("S23", "新）見積提案"),
        new TaskElement("P10", "ＰＫ販売"),
        new TaskElement("P20", "代理店販売")   
    ),

    ;

    private List<TaskElement> elements = new ArrayList<>();

    private TaskSet(TaskElement... defs) {
        for (TaskElement def : defs) {
            this.elements.add(def) ;
        }
    }

    public List<TaskElement> get() {
        return this.elements ;
    }

    public String getName(String code) {
        for (TaskElement te: this.elements) {
            if ( te.code().equals(code)) {
                return te.name();
            }
        }
        return "";
    }

    public boolean isExist(String code) {
        for (TaskElement te: this.elements) {
            if ( te.code().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static TaskSet choice(String code) {
        for (TaskSet e : TaskSet.values()) {
            if ( e.toString().equals(code) ) {
                return e;
            }
        }
        return null;
    }

    public static final class TaskElement extends JankCodeElement {

        public TaskElement(String code, String name) {
            super(code);
            this.name = name;
        }

        @Override
        protected String getNameByCode() {
            return this.name;
        }

        @Override
        protected boolean isValidFormat(String code) {
            return true;
        }
        
    }

}
