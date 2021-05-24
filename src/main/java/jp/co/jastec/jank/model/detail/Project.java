package jp.co.jastec.jank.model.detail;

import jp.co.jastec.jank.base.JankList;
import jp.co.jastec.jank.base.JankNode;
import jp.co.jastec.jank.base.validation.StringFormatChecker;
import jp.co.jastec.jank.service.ProjectMaster;
import jp.co.jastec.jank.service.ProjectMaster.ProjectAttibute;

/** プロジェクトの雛形 */
public abstract class Project extends JankNode {

    public abstract JankList<Task> getTasks();

    public Project(String prjCode) {
        super(prjCode);
    }

    @Override
    public float getHours() {
        return this.getTasks().sumHours();
    }

    @Override
    protected String getNameByCode() {
        ProjectAttibute pa = ProjectMaster.table().get(code) ;
        if ( pa != null ) {
            return pa.getName();
        } else {
            return "何某のプロジェクト";
        }
    }

    @Override
    protected boolean isValidFormat(String code) {
        return getStringFormatChecker().isValidFormat(code);
    }
    
    public static StringFormatChecker getStringFormatChecker() {
        return new StringFormatChecker()
            .allowUpperAlpha()
            .allowNumeric()
            .fixLength(8);
    }

}
