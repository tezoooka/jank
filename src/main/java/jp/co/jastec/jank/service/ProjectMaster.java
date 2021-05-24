package jp.co.jastec.jank.service;

import java.io.File;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.co.jastec.jank.service.ProjectMaster.ProjectAttibute;

public class ProjectMaster extends JankMaster<ProjectAttibute> {
    
    static ProjectMaster mySelf = null;

    private ProjectMaster() {
        super();
        loadCsv();
    }

    @Override public File getFile() {
        return new File(JankHome.MASTER_DIR, "project.csv");
    }

    @Override public ProjectAttibute parse(String... csvContents) {
        TaskSet taskSet = TaskSet.choice(csvContents[3]);
        ProjectAttibute value = new ProjectAttibute(csvContents[1], csvContents[2],taskSet);
        return value;
    }

    public static ProjectMaster table() {
        if ( null == mySelf ) {
            mySelf = new ProjectMaster();
        }
        return mySelf;
    }


    // public SortedMap<String, ProjectAttibute> all() {
    //     return this;
    // }

    public SortedMap<String, ProjectAttibute> selectByPractice(String practiceCode) {

        SortedMap<String, ProjectAttibute> filtered = new TreeMap<>();
        for (Entry<String, ProjectAttibute> kv : this.entrySet()) {
            String prjCode = kv.getKey();
            String praCode = kv.getValue().getPracticeCode();
            if ( praCode.equals(practiceCode) ) {
                filtered.put(prjCode, kv.getValue());
            }
        }
        return filtered;

    }



    public static class ProjectAttibute {
        final private String name;
        final private String practiceCode;
        final private TaskSet taskSet;
        
        public ProjectAttibute(String name, String practiceCode, TaskSet taskSet) {
            this.name = name;
            this.practiceCode = practiceCode;
            this.taskSet = taskSet;
        }
        public String getName() {
            return name;
        }
        public String getPracticeCode() {
            return practiceCode;
        }
        public TaskSet getTaskSet() {
            return taskSet;
        }
    }
}
