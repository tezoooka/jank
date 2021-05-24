package jp.co.jastec.jank.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.jastec.jank.base.JankCodeElement;
import jp.co.jastec.jank.service.TaskSet.TaskElement;

public class TaskSet  extends ArrayList<TaskElement> {

    /// 便宜上、すべてstaticにクラス内部も持っているが、
    /// 本来はプロジェクト登録の内容によって、より動的に変化する

    /** 開発用の工程作業目の一覧 */
    public static final String DEV = "DEV" ;
    /** 営業用 */
    public static final String SALES = "SALES" ;
    /** プロジェクト管理用 */
    public static final String MANAGE = "MANAGE" ;

    private static Map<String, TaskSet> map ;

    static {  
        
        map = new HashMap<>();
        map.put( TaskSet.DEV, new TaskSet(
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
        ));

        map.put(TaskSet.MANAGE, new TaskSet(
            new TaskElement("110", "予算管理"),
            new TaskElement("120", "収益出来高管理"),
            new TaskElement("130", "開発プロジェクト管理(定量管理、プロセス改善)"),
            new TaskElement("140", "顧客合意(報告)"),
            new TaskElement("150", "外部委託(発注)管理")
        ));

        map.put(TaskSet.SALES, new TaskSet(
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
        ));
    
    }

    private TaskSet(TaskElement... defs) {
        for (TaskElement def : defs) {
            this.add(def) ;
        }
    }

    public TaskElement get(String code) {
        for (TaskElement te: this) {
            if ( te.code().equals(code)) {
                return te;
            }
        }
        return null;
    }

    public String getName(String code) {
        TaskElement te = get(code) ;
        return te == null ? "" : te.name();
    }

    public boolean isExist(String code) {
        return get(code) != null ;
    }

    public static TaskSet choice(String code) {
        for (Entry<String, TaskSet> kv : TaskSet.map.entrySet()) {
            if ( kv.getKey().equals(code) ) {
                return kv.getValue();
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
