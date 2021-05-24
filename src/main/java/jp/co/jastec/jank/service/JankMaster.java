package jp.co.jastec.jank.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import jp.co.jastec.jank.base.JankException;

/** 参照専用マスタ情報 */
/// Stringをキーにした任意の型をTreeMapベースで保持する。
public abstract class JankMaster<V> extends TreeMap<String, V> {

    /// Unmodifiableなオーバーライド
    @Override public V put(String key, V value) { throw new UnsupportedOperationException();}
    @Override public V remove(Object key) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends String, ? extends V> m) { throw new UnsupportedOperationException();}
    @Override public void clear() {throw new UnsupportedOperationException();}
    //// ここまで

    /** サブクラスがファイル名を指定するメソッド */
    public abstract File getFile() ;

    /** サブクラスが、CSVファイルの内容を解釈して V型を生成するメソッド */
    public abstract V parse(String... csvContent) ;


    /** CSVからマスタ情報をロードする共通の仕掛け */
    protected void loadCsv() {

        final File file = this.getFile();
            
        try ( final BufferedReader br = new BufferedReader(new FileReader(file)) ) {   

            String csv = br.readLine();
            while (csv !=null ) {
                /// CSVの簡易パーサ（クオーテーション等には非対応）
                final String[] kv = csv.split(",");
                final String key = kv[0].trim();
                final V value = this.parse(kv) ;
                super.put(key, value) ;  /// this.put() は使えない
                csv = br.readLine();
            }

        } catch (IOException ioe ) {
            throw new JankException(ioe) ;
        }

    }

}
