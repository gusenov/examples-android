package com.example.database_list_create_read_delete_search;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class UserCursorAdapter extends CursorAdapter {
    HashMap<Long, Boolean> itemChecked = new HashMap<>();

    public UserCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
        TextView tvName = (TextView)view.findViewById(R.id.tvName);
        tvName.setText(name);

        int age = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE));
        TextView tvAge = (TextView) view.findViewById(R.id.tvAge);
        tvAge.setText(String.valueOf(age));

        // Должно быть final, чтобы зафиксировалось в onClick каждого пункта.
        final Long id = getItemId(cursor.getPosition());

        CheckBox userCheckbox = (CheckBox)view.findViewById(R.id.userCheckbox);
        userCheckbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v.findViewById(R.id.userCheckbox);
                if (checkBox.isChecked()) {
                    itemChecked.put(id, true);
                } else {
                    itemChecked.put(id, false);
                }
            }
        });

        if (itemChecked.containsKey(id)) {
            userCheckbox.setChecked(itemChecked.get(id));
        } else {
            userCheckbox.setChecked(false);
        }
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        itemChecked.clear();
        return super.swapCursor(newCursor);
    }

    @Override
    public long getItemId(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
    }

    public ArrayList<Long> getSelectedUsers() {
        ArrayList<Long> result = new ArrayList<>();
        for (int i = 0; i < this.getCount(); i++) {
            Long id = this.getItemId(i);
            if (itemChecked.containsKey(id) && itemChecked.get(id)) {
                result.add(id);
            }
        }
        return result;
    }

}
