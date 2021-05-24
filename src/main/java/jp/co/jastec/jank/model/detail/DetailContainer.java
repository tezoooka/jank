package jp.co.jastec.jank.model.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

import jp.co.jastec.jank.base.FixedWidthString;
import jp.co.jastec.jank.base.JankCodeElement;
import jp.co.jastec.jank.base.JankList;
import jp.co.jastec.jank.base.JankNode;
import jp.co.jastec.jank.base.ManHour;
import jp.co.jastec.jank.cli.CliView;
import jp.co.jastec.jank.cli.CliView.CliViewable;

/** 日計入力の詳細部分（活動コード・プロジェクト・工程作業項目・工数）を格納するクラス */
/// 実体は、活動コードのリストを保持しているだけで、その他の情報は活動コードをルートにする
/// 階層構造になっている
public class DetailContainer implements CliViewable, ManHour {

    // 行番号とインスタンスの実体との対応表
    private Map<Integer, JankNode> lineNoMap ;

    @Expose
    JankList<Practice> practices = new JankList<>();

    public DetailContainer() {
    }

    public JankList<Practice> getPractices() {
        return practices;
    }
    
    public void setPractices(JankList<Practice> practices) {
        this.practices = practices;
    }

    public void addPractice(Practice newPractice) {
        if ( ! this.practices.has(newPractice.code())) {
            practices.add(newPractice, null);
        }
    }

    public float getHours() {
        if ( practices != null ) {
            return practices.sumHours();
        } else {
            return 0;
        }
    }

    private JankNode ancesterOf(int generation, JankNode leaf) {
        int gens = generation;
        JankNode node = leaf;
        while ( gens-- > 0 ) {
            if ( null == node ) {
                return null;
            }
            node = node.getParent();
        }
        return node;
    }


    public JankNode getByLineNo(int lineNo) {
        return this.lineNoMap.get(lineNo) ;
    }

    public Task getTaskByLineNo(int lineNo) {
        JankNode leaf = getByLineNo(lineNo) ;
        return (Task) ancesterOf(0, leaf); 
    }

    public Project getProjectByLineNo(int lineNo) {
        JankNode leaf = getByLineNo(lineNo) ;
        return (Project) ancesterOf(1, leaf); 
    }

    public Practice getPracticeByLineNo(int lineNo) {
        JankNode leaf = getByLineNo(lineNo) ;
        return (Practice) ancesterOf(2, leaf); 
    }


    // public static class PathMap extends LinkedHashMap<String, JankNode> {
    //     static final String SEP = "/";
    //     PathMap(JankList<? extends JankNode> root) {
    //         traverse(root, SEP);
    //     }

    //     void traverse(JankList<? extends JankNode> node, String parentPath) {
    //         for (JankNode e :node) {
    //             String path = parentPath + e.code();
    //             this.put(path,e) ;
    //             JankList<? extends JankNode> children = e.getChildren();
    //             if ( children != null) {
    //                 traverse(children, path + SEP);
    //             }
    //         }
    //     }

    //     @Override
    //     public String toString() {
    //         final StringBuilder sb = new StringBuilder();
    //         for (String path :this.keySet()) {
    //             JankNode e = this.get(path);
    //             sb  .append(path)
    //                 .append(":")
    //                 .append(e.name())
    //                 .append("\n");
    //         }
    //         return sb.toString();
    //     }
    // }


    // 以下はCliViewableの実装

    static final String header2 = "   活動コード          プロジェクト                      工程作業項目    工数";

    @Override
    public CliView getView() {

        if ( null == practices || practices.size()==0 ) {
            return null;
        }

        CliView cliView = new CliView();
        
        cliView.add(CliView.HR);
        cliView.add(header2);

        // Tree構造を単純な表にする
        List<FlatRecord> flatList = flatten() ;

        // 一行毎に表示形式にしたListを生成してViewに追加
        List<String> viewStrings = flatList.stream().map((f)-> f.toString()).toList();
        cliView.addAll(viewStrings);

        /// この部分は副作用あり
        // 行番号と対応するNode（Taskノードの対尾表を作成しておく）
        this.lineNoMap = new HashMap<>();
        for ( FlatRecord fr : flatList) {
            lineNoMap.put(fr.lineNo, fr.leafNode) ;
        }

        final int footerWidth = FixedWidthString.calcWidth(header2);
        final String footer = FixedWidthString.width(footerWidth).right("合計 %.1f", getHours()) ;
        cliView.add(footer);
        
        return cliView ;
    
    }


    private Project unfilledProject() {
        return new Project("") {
            @Override public String code() { return "________"; }
            @Override public String name() { return ""; }
            @Override public JankList<Task> getTasks() { return new JankList<>();}
            @Override public JankList<?> getChildren() {return getTasks(); }
        };
    }

    private Task unfilledTask() {
        return new Task("___", 0);
    }

    private List<FlatRecord> flatten() {

        List<FlatRecord> flatList = new ArrayList<>();
        FlatRecord previous = new FlatRecord();
        int lineNo = 1;
        for( Practice pra : this.practices ) {
            JankList<Project> proList =  pra.getProjects();
            if  ( proList.isEmpty() ) {
                proList = new JankList<>();
                proList.add(unfilledProject(), pra);
            }
            for ( Project prj : proList) {
                JankList<Task> taskList =  prj.getTasks();
                if  ( taskList.isEmpty() ) {
                    taskList = new JankList<>();
                    taskList.add(unfilledTask(),prj);
                }
                for ( Task t : taskList) {
                    FlatRecord fr = new FlatRecord();
                    fr.practiceCode = pra.code();
                    fr.practiceName = pra.name();
                    fr.projectCode = prj.code();
                    fr.projectName = prj.name();
                    fr.taskCode = t.code();
                    fr.taskName = t.name();
                    fr.leafNode = t;
                    if ( t.code().equals("___")) {
                        fr.hours = "___";
                    } else {
                        fr.hours = String.format("%.1f",t.getHours());
                    }
                    flatList.add(fr.suppressDuplicate(previous).withLineNo(lineNo++));
                    previous = fr ;
                }
            }
        }
        return flatList;
    }


    private static class FlatRecord {
        int lineNo;
        JankNode leafNode ;
        String practiceCode;
        String practiceName;
        String projectCode;
        String projectName;
        String taskCode;
        String taskName;
        String hours;

        @Override
        public String toString() {

            String aPart = fixing(this.practiceCode, 4, this.practiceName, 14);
            String jPart = fixing(this.projectCode, 8, this.projectName, 22);
            String tPart = fixing(this.taskCode, 3, this.taskName, 7);
            String hPart = FixedWidthString.width(4).right(this.hours);

            final String format = "%2d %s %s %s    %s" ;
            return String.format(format, lineNo, aPart, jPart, tPart, hPart);
        }

        @Override public FlatRecord clone() {
            final FlatRecord fr = new FlatRecord() ;
            fr.practiceCode = this.practiceCode;
            fr.practiceName = this.practiceName;
            fr.projectCode = this.projectCode;
            fr.projectName = this.projectName;
            fr.taskCode = this.taskCode;
            fr.taskName = this.taskName;
            fr.hours = this.hours;
            fr.leafNode = this.leafNode;
            return fr;
        }

        FlatRecord suppressDuplicate(FlatRecord previous) {
            FlatRecord sup = this.clone();

            if (this.practiceCode.equals(previous.practiceCode)){
                sup.practiceCode = "";
                sup.practiceName = "";
                if (this.projectCode.equals(previous.projectCode)) {
                    sup.projectCode = "";
                    sup.projectName = "";
                }

            } 
            return sup;
        }

        FlatRecord withLineNo(int i) {
            this.lineNo = i;
            return this;
        }

        public String fixing(String code, int codeLen, String name, int nameLen) {
            return JankCodeElement.fixing(code, codeLen, name, nameLen);
        }

    }

}
