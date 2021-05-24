package jp.co.jastec.jank.base.validation;

import java.util.ArrayList;

import jp.co.jastec.jank.base.JankMessage;

/// Validatorで検出したエラーを蓄積するための構造
/// 実体はただのListだけど、型チェックを確実にするためと、
// いずれ機能追加できるように、Listを継承した専用クラスにしている
public class ResultList  extends ArrayList<JankMessage>  {
    public static final ResultList EMPTY = new ResultList();
}
