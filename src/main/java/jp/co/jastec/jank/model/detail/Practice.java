package jp.co.jastec.jank.model.detail;

import com.google.gson.annotations.Expose;

import jp.co.jastec.jank.base.JankList;
import jp.co.jastec.jank.base.JankNode;
import jp.co.jastec.jank.base.validation.StringFormatChecker;
import jp.co.jastec.jank.service.PracticeMaster;

public class Practice extends JankNode {

    @Expose
    private JankList<Project> projects = new JankList<>();

    /** 活動コード */
    public Practice(String practiceCode) {
        super(practiceCode);
    }

    public JankList<Project> getProjects() {
        return this.projects;
    }

    public void addProject(Project newProject) {
        if ( ! this.projects.has(newProject.code()) ) {
            this.projects.add(newProject, this);
        }
    }

    public void removeProject(String prjectCode) {
        this.projects.remove(prjectCode);
    }


    @Override
    public float getHours() {
        return this.projects.sumHours();
    }

    @Override
    protected String getNameByCode() {
        String name = PracticeMaster.table().getOrDefault(code, code + "活動");
        return name;
    }

    @Override
    protected boolean isValidFormat(String code) {

        return  getStringFormatChecker().isValidFormat(code);
    }

    @Override
    public JankList<?> getChildren() {
        return this.getProjects();
    }

    public static StringFormatChecker getStringFormatChecker() {
        return  new StringFormatChecker()
                .allowUpperAlpha()
                .allowNumeric()
                .fixLength(4);
    }

}
