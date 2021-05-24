package jp.co.jastec.jank.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import jp.co.jastec.jank.base.JankException;

/**
 * アプリケーションの実行ディレクトリ内の構造定義と初期化
 */
public class JankHome {
    
    private static String MASTER_DIR_PATH =  "master" ;
    private static String ARCHIVE_DIR_PATH =  "archived" ;

    public static String BASE_PATH = "c:\\JankHome" ;
    public static File MASTER_DIR = new File(BASE_PATH, MASTER_DIR_PATH);
    public static File ARCHIVE_DIR = new File(BASE_PATH, ARCHIVE_DIR_PATH);

    public static boolean isExist() {
        File baseDir = new File(BASE_PATH) ;
        return baseDir.exists() && baseDir.isDirectory();
    }

    public static void prepare() {
        if (!isExist()) {
            MASTER_DIR.mkdirs();
            ARCHIVE_DIR.mkdir();
        }
    }

    public static void init() {
        prepare();
        writeArray("employee.csv", EMPLOYEE);  
        writeArray("practice.csv", PRACTICE);  
        writeArray("project.csv", PROJECT);  
    } 

    static void writeArray(String fileName, String[] content) {

        try {
            File writeTo = new File(MASTER_DIR, fileName);
            FileWriter fw = new FileWriter(writeTo);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            for ( String s : content) {
                pw.println(s);
            }
            pw.close();
        } catch (IOException ioe) {
            throw new JankException(ioe) ;
        }
    }

    ///
    /// これ以降は、テストデータとしてファイルに吐き出す内容。
    ///
    private static String[] EMPLOYEE  = {
            "000001,神●　●",
            "000004,市●　行●",
            "000007,太●　忠●",
            "004001,阿●　幸●",
            "006002,小●　正●",
            "006007,望●　進●",
            "007005,平●　芳●",
            "008007,西●　令●",
            "009002,柴●　健●",
            "009003,鈴●　三●",
            "010001,江●　浩●",
            "010002,島●　富●",
            "010003,鈴●　正●",
            "010008,羽●　三●",
            "011001,榎●　●",
            "011005,福●　正●",
            "012004,腰●　典●",
            "013008,山●　義●",
            "014002,功●　●",
            "014005,笹●　宏●",
            "014006,関●　●",
            "014007,早●　一●",
            "014009,三●　●",
            "014014,高●　博●",
            "015001,倉●　英●",
            "015005,仲●　尊●",
            "015014,渡●　寛●",
            "016004,大●　公●",
            "016006,川●　敏●",
            "016009,黒●　鉄●",
            "016017,宮●　伸●",
            "016021,五十●　正●",
            "016023,栗●　●",
            "016026,浜●　●",
            "017008,尾●　●",
            "017017,高●　克●",
            "017018,竹●　●",
            "017040,雨●　祥●",
            "017044,首●　栄●",
            "017050,相●　比砂●",
            "018003,磯●　大一●",
            "018005,今●　和●",
            "018006,大●　●",
            "018008,小●　●",
            "018011,北●　●",
            "018016,里●　●",
            "018020,大●　久美●",
            "018026,平●　敏●",
            "018027,平●　●",
            "018036,村●　英●",
            "018037,山●　美穂●",
            "018044,綾●　真●",
            "018059,近●　典●",
            "019004,石●　正●",
            "019005,岩●　伸●",
            "019012,鈴●　理●",
            "019013,相●　信●",
            "019017,手●　文●",
            "019030,釼●　貴●",
            "019051,坂●　久●",
            "019052,坂●　和●",
            "019054,佐●　●",
            "019055,●　宗●",
            "019060,森●　政●",
            "019062,山●　●",

    };


    private static String[] PRACTICE = {
            "A001,Aチーム開発",
            "A002,Aチーム管理",
            "A003,Aチーム評価",
            "B001,Bチーム開発",
            "B002,Bチーム管理",
            "B003,Bチーム評価",
            "E001,営業履行支援"
    };

    private static String[] PROJECT = {
            "A001P001,開発案件Aその１,A001,"+TaskSet.DEV.toString(),
            "A001P002,開発案件Aその２,A001,"+TaskSet.DEV.toString(),
            "A001P003,開発案件Aテスト,A001,"+TaskSet.DEV.toString(),
            "A001P004,開発案件A運用支援,A001,"+TaskSet.DEV.toString(),
            "A001P005,開発案件A保守,A001,"+TaskSet.DEV.toString(),
            "A0000000,管理業務,A002,"+TaskSet.MANAGE.toString(),
            "B001P001,開発案件Bその１,B001,"+TaskSet.DEV.toString(),
            "B001P002,開発案件Bその２,B001,"+TaskSet.DEV.toString(),
            "B001P003,開発案件Bテスト,B001,"+TaskSet.DEV.toString(),
            "B001P004,開発案件B運用支援,B001,"+TaskSet.DEV.toString(),
            "B001P005,開発案件B保守,B001,"+TaskSet.DEV.toString(),
            "B0000000,管理業務,B002,"+TaskSet.MANAGE.toString(),
            "E001C001,濃紺銀行,E001,"+TaskSet.SALES.toString(),
            "E001C002,青色銀行,E001,"+TaskSet.SALES.toString(),
            "E001C003,赤色銀行,E001,"+TaskSet.SALES.toString(),
            "E001C004,赤色航空,E001,"+TaskSet.SALES.toString(),
            "E001C005,青色空輸,E001,"+TaskSet.SALES.toString(),
            "E001C006,赤色証券,E001,"+TaskSet.SALES.toString(),
            "E001C007,青色証券,E001,"+TaskSet.SALES.toString(),
    };
}
