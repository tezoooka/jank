package jp.co.jastec.jank.base;

import com.google.gson.annotations.Expose;

/// 〇〇コードと〇〇名称をセットを保持するための基底クラス
public abstract class JankCodeElement {

    @Expose
    protected String code;
    
    @Expose
    protected String name;

    public JankCodeElement(String code) {
        this.isValidFormat(code);
        this.code = code;
    }

    public String code() {
        return this.code;
    }

    public String name() {
        if (this.name==null) {
            this.name = this.getNameByCode() ;
        }
        return this.name;
    }

    protected abstract String getNameByCode() ;

    protected abstract boolean isValidFormat(String code);


    public boolean eq(JankCodeElement that) {
        return this.code().equals(that.code());
    }


    /** コードと名称を固定長に整形するPrettyMethod */
    public static String fixing(String code, int codeLen, String name, int nameLen) {
        String fixWidthCode = FixedWidthString.width(codeLen).left(code);
        String fixWidthName = FixedWidthString.width(nameLen).left(name) ;
        if ( name == null || name.isEmpty() ) {
            return fixWidthCode + " " + fixWidthName + " ";
        } else {
            return fixWidthCode + "(" + fixWidthName + ")";
            }
    }

}
