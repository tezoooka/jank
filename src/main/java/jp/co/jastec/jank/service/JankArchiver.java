package jp.co.jastec.jank.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jp.co.jastec.jank.base.JankException;
import jp.co.jastec.jank.base.datetime.JankDate;
import jp.co.jastec.jank.base.datetime.JankTime;

/// JankModelの内容をファイルに保存する
/// 本来であれば、DBに格納するなり、HTTPでPOSTするなりする処理であるが、
/// とりあえずJSON形式でファイルに保存しておく。
public class JankArchiver {
    
    /** JankArchiverで保存可能なModelを示すインターフェース */
    public interface JankArchiveModel { 
        String getUniqIdentier();
    }

    public File save(JankArchiveModel model)  {

        try {
            File archiveTo = resoleveFilePath(model) ;
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(JankTime.class, JankTime.JSON_SERIALIZER)
                .registerTypeAdapter(JankDate.class, JankDate.JSON_SERIALIZER)
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();

            FileWriter writer = new FileWriter(archiveTo);
            gson.toJson(model, writer);
            writer.close();

            return archiveTo;

        } catch ( IOException ioe ) {
            throw new JankException(ioe);
        }
    }    
    
    private File resoleveFilePath(JankArchiveModel model) {
        String uniqName =  model.getUniqIdentier() + ".json";
        File file = new File(JankHome.ARCHIVE_DIR, uniqName);
        return file ;
    }



}
