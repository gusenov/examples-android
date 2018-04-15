package com.example.database_list_create_read_delete_search;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    /** Полный путь к базе данных. */
    private static String DB_PATH;

    private static String DB_NAME = "userinfo.db";

    /** Версия базы данных. */
    private static final int SCHEMA = 1;

    /** Название таблицы в БД. */
    static final String TABLE = "users";

    // Названия столбцов:
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_AGE = "age";

    private Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;

        DB_PATH = context.getDatabasePath(DB_NAME).getPath();

        // Log.d("ME", DB_PATH);
        // /data/user/0/com.example.database_list_create_read_delete_search/databases/userinfo.db

        // Log.d("ME", context.getFilesDir().getPath());
        // /data/user/0/com.example.database_list_create_read_delete_search/files
    }

    public SQLiteDatabase open() throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    void create_db() {
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                this.getReadableDatabase();

                // Получаем локальную БД как поток:
                myInput = myContext.getAssets().open(DB_NAME);

                // Путь к новой БД:
                String outFileName = DB_PATH;

                // Открываем пустую БД:
                myOutput = new FileOutputStream(outFileName);

                // Побайтово копируем данные:
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        } catch (IOException ex) {
            Log.d("DatabaseHelper", ex.getMessage());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Do nothing.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing.
    }
}
