package jp.co.jastec.jank.base;

/// 各種のエラーメッセージ等。
/// 普通はテキストファイルやstatic final const にするだろうが、enumでも実装したらどうあんるかの実験
public enum JankMessage {

    EMPNO_ILLEGALFORMAT("E0001", "社員番号の形式が正しくないよ"),
    ACTCODE_NOT_4DG("E0002","活動コードが4桁でないよ。"),
    HOURS_NOT_05UNIT("E0003","工数が0.5刻みではないよ。"),
    DATETIME_ILLEGALFORMAT("E0004","日付や時間表記の形式が間違っています。"),
    // LESS_WORKING_WRONG("E0005","不就業の内容にエラーがあります。"),
    UPSET_START_FNISH_TIME("E0006","始業時刻と就業時刻の関係がおかしい。"),
    HOLIDAY_BUT_NOCHECK("E0007","休日ですが勤務形態がありません"), 
    INVALID_RESTCHECK("E0008", "休憩チェックが何かヘン"), 
    INVALID_ABSENT_REASON("E0009","不就業の報告内容がへん"), 
    UNSELECTABLE_OPTION("E00010", "この値は選択できません。"),
    MISSING_HOLYIDAYTYPE("E00011","休暇種別がおかしい"),
    NOT_A_HALFHOLIDAY("E00012","半休ではないので工数入力は不要"), 
    DUPE_ABSENCETIME("E00013", "重複した不就業情報があります。"), 
    UNSELECTABLE_MULTI("E00014","複数選択はできません"),
    OUT_OF_DATE_RANGE("E00015", "サポート範囲外の日付"),
    ILLEAGAL_INPUT_FORMAT("E00016", "入力した形式に誤りがあります"), 
    YOU_ARE_NOT_JACKBAUER("E00017", "You are not Jack Bauer 24時間以上働けません"),
    WRONG_DATE_TIME("E00018", "日付または時刻の表現になんらかの誤りがあります。"), 
    FAIL_TO_UPDATE("E00019", "更新に失敗しました"), 
    ILLEAGAL_LINENO("E00020", "行番号がただしくありません")
    ;

    
    String code;
    String text;
    private JankMessage(String code,String text) {
        this.code = code;
        this.text = text;
    }
    public String getMessageText() {
        return this.text;
    }

    public String getMessageCode() {
        return this.code;
    }

    public String getMessage() {
        return getMessageCode() + ": " + getMessageText() ;
    }

    public boolean isError() {
        return this.getMessageCode().startsWith("E");
    }

    public boolean isWarning() {
        return this.getMessageCode().startsWith("W");
    }

    public boolean isInformation() {
        return this.getMessageCode().startsWith("I");
    }

}