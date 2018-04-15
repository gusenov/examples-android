package com.example.database_list_create_read_delete_search;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etNewUserName;
    private EditText etNewUserAge;
    private Button btnAdd;

    private Button btnDelete;

    private ListView lvUsers;

    private EditText etSearchLowVal;
    private EditText etSearchHighVal;
    private Button btnSearch;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private UserCursorAdapter userAdapter;

    SharedPreferences sPref;

    final String SAVED_NEW_USER_NAME = "saved_new_user_name";
    final String SAVED_NEW_USER_AGE = "saved_new_user_age";
    final String SAVED_SEARCH_LOW_VAL = "saved_search_low_val";
    final String SAVED_SEARCH_HIGH_VAL = "saved_search_high_val";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNewUserName = (EditText)findViewById(R.id.newUserName);
        etNewUserAge = (EditText)findViewById(R.id.newUserAge);

        btnAdd = (Button)findViewById(R.id.addButton);
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        btnDelete = (Button)findViewById(R.id.deleteButton);
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUsers();
            }
        });

        lvUsers = (ListView)findViewById(R.id.userListView);

        etSearchLowVal = (EditText)findViewById(R.id.searchLowVal);
        etSearchHighVal = (EditText)findViewById(R.id.searchHighVal);
        btnSearch = (Button)findViewById(R.id.searchButton);
        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filterUsers();
            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());

        // Создаём базу данных:
        databaseHelper.create_db();

        // Открываем подключение:
        db = databaseHelper.open();

        // Получаем данные из БД в виде курсора:
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE,
                new String[] {});
        userAdapter = new UserCursorAdapter(this, userCursor);
        lvUsers.setAdapter(userAdapter);

        loadText();
    }

    @Override
    protected void onStop() {
        saveText();
        super.onStop();
    }

    private void updateContentsOfListView() {
        // Получаем данные из БД в виде курсора:
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE,
                new String[] {});
        userAdapter.swapCursor(userCursor);
    }

    private void addUser() {
        // Подключаемся к БД:
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Создаём объект для данных:
        ContentValues cv = new ContentValues();

        // Получаем данные из полей ввода:
        String name = etNewUserName.getText().toString();
        Integer age = Integer.valueOf(etNewUserAge.getText().toString());

        // Подготовим данные для вставки в виде пар:
        cv.put(DatabaseHelper.COLUMN_NAME, name);
        cv.put(DatabaseHelper.COLUMN_AGE, age);

        // Вставляем запись и получаем её ID:
        long rowID = db.insert(DatabaseHelper.TABLE, null, cv);
        Log.d("ME", "rowID = " + Long.toString(rowID));

        updateContentsOfListView();
    }

    private void deleteUsers() {
        // Подключаемся к БД:
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        for (Long userID : userAdapter.getSelectedUsers()) {
            String id = userID.toString();

            // Удаляем по ID:
            int delCount = db.delete(DatabaseHelper.TABLE,
                    DatabaseHelper.COLUMN_ID + " = " + id, null);

            Log.d("ME", "delete by ID = " + id);
        }

        updateContentsOfListView();
    }

    private void filterUsers() {
        String lowAgeVal = etSearchLowVal.getText().toString();
        String lowAge = (lowAgeVal != null && !lowAgeVal.isEmpty()) ? lowAgeVal : "0";

        String highAgeVal = etSearchHighVal.getText().toString();
        String highAge = (highAgeVal != null && !highAgeVal.isEmpty()) ? highAgeVal : "100";

        Cursor cursor = db.query(DatabaseHelper.TABLE,
                new String[] {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_AGE},
                DatabaseHelper.COLUMN_AGE + " >= ? AND " + DatabaseHelper.COLUMN_AGE + " <= ?",
                new String[] { lowAge, highAge },
                null, null, null);
        userAdapter.swapCursor(cursor);
    }

    private void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString(SAVED_NEW_USER_NAME, etNewUserName.getText().toString());
        ed.putString(SAVED_NEW_USER_AGE, etNewUserAge.getText().toString());
        ed.putString(SAVED_SEARCH_LOW_VAL, etSearchLowVal.getText().toString());
        ed.putString(SAVED_SEARCH_HIGH_VAL, etSearchHighVal.getText().toString());
        ed.commit();
    }

    private void loadText() {
        sPref = getPreferences(MODE_PRIVATE);

        String saved_new_user_name = sPref.getString(SAVED_NEW_USER_NAME, "");
        etNewUserName.setText(saved_new_user_name);

        String saved_new_user_age = sPref.getString(SAVED_NEW_USER_AGE, "");
        etNewUserAge.setText(saved_new_user_age);

        String saved_search_low_val = sPref.getString(SAVED_SEARCH_LOW_VAL, "");
        etSearchLowVal.setText(saved_search_low_val);

        String saved_search_high_val = sPref.getString(SAVED_SEARCH_HIGH_VAL, "");
        etSearchHighVal.setText(saved_search_high_val);
    }
}
