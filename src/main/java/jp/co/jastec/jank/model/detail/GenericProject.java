package jp.co.jastec.jank.model.detail;

import com.google.gson.annotations.Expose;

import jp.co.jastec.jank.base.JankList;

/** 単純なプロジェクト入力情報（実際には使えない） */
/// 実際には、プロジェクトの種類によって、工程作業項目が不要だったり、
/// 生産管理情報が必要だったりするので、このクラスのように単純にはいかない
public class GenericProject extends Project {

    @Expose
    JankList<Task> tasks = new JankList<>();

    public GenericProject(String prjCode) {
        super(prjCode);
    }

    @Override
    public JankList<Task> getTasks() {
        return this.tasks;
    }

    @Override
    public JankList<Task> getChildren() {
        return this.tasks;
    }
    
}
