package jp.co.jastec.jank.base;

import java.util.ArrayList;

/**
 * 日計入力の詳細部分の階層・繰り返し構造を保持するためのリスト。
 * 各層の工数合計を取得可能な仕組みなっている。
 */
public class JankList<T extends JankNode> extends ArrayList<T> {

    public boolean remove(String code) {
        return this.removeIf((jc)->jc.code().equals(code)) ;
    }

    @Override @Deprecated // addする際に、親ノードを指定できるように
    public boolean add(T e) {
        throw new UnsupportedOperationException();
    }

    public boolean add(T node, JankNode parent) {
        node.setParent(parent);
        return super.add(node);
    }

    public boolean has(String code) {
        return find(code) != null;
    }

    public T find(String code) {
        for ( T t : this ) {
            if ( t.code().equals(code)) {
                return t ;
            }
        }
        return null;
    }

    public float sumHours() {
        float h = 0 ;
        for ( T e : this ) {
            h += e.getHours();
        }
        return h;
    }


}
