package jp.co.jastec.jank.base;

/**
 * 活動コード・プロジェクト・工程作業項目のような木構造のノード示す抽象型
 */
public abstract class JankNode extends JankCodeElement implements ManHour {

    public static final String PATH_SEP = "/";

    /** 親ノードへの逆参照 */
    protected JankNode parent;

    public JankNode(String code) {
        super(code);
    }

    public void setParent(JankNode parent) {
        this.parent = parent ;
    }

    public JankNode getParent() {
        return parent;
    }

    public abstract JankList<? extends JankNode> getChildren();

    /** 自分自身を親ノード内のリストから削除する */
    public boolean removeMySelf() {
        if ( this.parent != null && this.parent.getChildren() != null) {
            return this.parent.getChildren().remove(this.code) ;
        }
        return false;
    }

    // // Rootからの階層を含めて固有の表記にする
    // public String getPath() {

    //     final List<String> codeList = new ArrayList<>();

    //     codeList.add(this.code) ;
    //     JankNode parent = this.parent;
    //     while (parent != null) {
    //         codeList.add(parent.code()) ;
    //     }

    //     Collections.reverse(codeList);
    //     return PATH_SEP + String.join(PATH_SEP, codeList);
    // }


    // // Path表現されたノードを再帰的に探索する
    // public JankNode find(String path) {
    //     return findRecurseive(this, path);
    // }

    // private JankNode findRecurseive(JankNode node, String path) {

    //     if ( node.getPath().equals(path)) {
    //         return node;
    //     }

    //     JankList<? extends JankNode> children = this.getChildren();
    //     if ( children == null ) {
    //         return null;
    //     }

    //     for (JankNode child : children) {
    //         JankNode found = child.find(path);
    //         if ( null != found ) {
    //             return found ;
    //         }
    //     }
    //     return null;

    // }

}
