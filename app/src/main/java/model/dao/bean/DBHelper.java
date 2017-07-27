package model.dao.bean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by lala on 2017/7/20.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASENAME = "itheima.db";
    private static final int DATABASEVERSION = 1;
    private static DBHelper instance;
    public DBHelper(Context context){
        super(context,DATABASENAME,null,DATABASEVERSION);
    }

    public static DBHelper getInstance(Context context){
        if (instance==null){
            synchronized (DBHelper.class){
                if (instance==null){
                    instance=new DBHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
