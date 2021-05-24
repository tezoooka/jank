package jp.co.jastec.jank.model.header;

import jp.co.jastec.jank.base.JankCodeElement;
import jp.co.jastec.jank.base.validation.StringFormatChecker;
import jp.co.jastec.jank.cli.CliView;
import jp.co.jastec.jank.cli.CliView.CliViewable;
import jp.co.jastec.jank.service.EmpMaster;

/** 社員 */
public class Employee extends JankCodeElement implements CliViewable {

    /** 承認者 */
    private Employee approver; 

    public Employee(String empNo){
        super(assumeLeadingZero(empNo));
    }


    public Employee getApprover() {
        return this.approver;
    }

    private static String assumeLeadingZero(String empNo) {
        if ( empNo.length() == 5 ) {
            return "0" + empNo;
        } else {
            return empNo;
        }
    }

    @Override
    protected String getNameByCode() {
        String name = EmpMaster.table().nameOf(this.code) ;
        if ( name == null || name.isEmpty()) {
            name = "不明な社員";
        }
        return name ;
    }


    @Override
    protected boolean isValidFormat(String code) {
        return new StringFormatChecker()
                    .allowNumeric()
                    .minLength(5)
                    .maxLength(6)
                    .isValidFormat(code);
    }

    @Override
    public CliView getView() {
        String viewString = "社員番号:  " + this.code() + "   " + this.name();
        return  new CliView(viewString);
    }
}
