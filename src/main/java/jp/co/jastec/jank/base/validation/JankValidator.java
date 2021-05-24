package jp.co.jastec.jank.base.validation;

import java.util.List;

import jp.co.jastec.jank.base.JankMessage;

/**
 *  modelクラスの検証を行うための基底クラスで、各modelクラスは、自身を検証するために、
 * このクラスを継承したValidatorを具備する必要がある。
 */
public abstract class JankValidator<T> implements JankValidatable {

    /** 検証対象のモデルオブジェクト */
    private T model;

    /** 検証結果の格納リスト */
    private ResultList resultList = new ResultList();

    /** コンストラクタに検証対象のモデルを渡す */
    public JankValidator(T model) {
        this.model = model;
    }

    /** 必要な検証機能のリストを返す */ 
    protected abstract List<JankValidation> getValidations() ;

    public ResultList validate() {
        
        this.resultList.clear();
        for ( JankValidation v: this.getValidations()) {
            // TODO 本当はアノテーションをつけて自動で列挙したい
            JankMessage jm = v.exec();
            if ( jm != null) {
                this.resultList.add(v.exec());
            }
        }
        return this.resultList;
    }

    protected T model() {
        return this.model;
    }

    protected static boolean isAnyNull(Object... objs) {
        for ( Object o : objs) {
            if ( o == null ) return true ;
        }
        return false ;
    }

    /**
     * Validaotrが実行する個々のバリデーションロジック<br/>
     * Validatorは、モデル全体の検証を担うのに対し、ValidationはValidator内の個々のチェックに対応する。
     * 言い換えると、Validatorは複数のエラーを返すが、Validationは単一のエラーメッセージしか返さない。
     */
    public interface JankValidation {
        public JankMessage exec() ;
        
    }


}
