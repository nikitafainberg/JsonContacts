package com.fineapp.jsoncontacts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Collections;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ArrayList<Form> order;
    private SwipeMenuListView list;
    private SQLite sqLite;
    ListHelper adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ArrayList<Form> listForm = getInfo();
        ArrayList<String> names = new ArrayList<>();

        list = (SwipeMenuListView) findViewById(R.id.list);
        order = new ArrayList<>();

        for (Form form : listForm){
            names.add(form.name);
        }

        Collections.sort(names);

        for (int i = 0; i < names.size(); i++){
            for (Form form : listForm){
                if (form.name.equals(names.get(i))){
                    order.add(form);
                    listForm.remove(form);
                    break;
                }
            }
        }

        adapter = new ListHelper(this, order);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(170);
                deleteItem.setIcon(R.mipmap.delete_item);
                menu.addMenuItem(deleteItem);
            }
        };
        list.setMenuCreator(creator);

        list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Form form = order.get(position);
                deleteFromSQLite(form.id);
                order.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }
    private ArrayList<Form> getInfo() {
        ArrayList<Form> forms = new ArrayList<>();
        sqLite = new SQLite(this);
        SQLiteDatabase database = sqLite.getReadableDatabase();
        Cursor cursor = database.query(SQLite.CONTACTS, null, null, null, null, null, null);

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                int nameId = cursor.getColumnIndex(SQLite.KEY_NAME);
                int idId = cursor.getColumnIndex(SQLite.KEY_ID);
                int addressId = cursor.getColumnIndex(SQLite.KEY_ADDRESS);
                int emailId = cursor.getColumnIndex(SQLite.KEY_EMAIL);
                int genderId = cursor.getColumnIndex(SQLite.KEY_GENDER);
                int phoneId = cursor.getColumnIndex(SQLite.KEY_PHONE);
                do {
                    String id = cursor.getString(idId);
                    String name = cursor.getString(nameId);
                    String address = cursor.getString(addressId);
                    String email = cursor.getString(emailId);
                    String gender = cursor.getString(genderId);
                    String phone = cursor.getString(phoneId);

                    Form form = new Form(id, name, email, address, gender, phone);
                    forms.add(form);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        sqLite.close();

        return forms;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Form form = order.get(position);
        Toast.makeText(ListActivity.this, "It's " + form.gender + ", by the way", Toast.LENGTH_SHORT).show();
        Spinner spinner = view.findViewById(R.id.spinner);
        String phone = spinner.getSelectedItem().toString();

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    private void deleteFromSQLite(String id){
        sqLite = new SQLite(this);
        SQLiteDatabase database = sqLite.getWritableDatabase();
        database.delete(SQLite.CONTACTS, SQLite.KEY_ID + "= " + id, null);
        sqLite.close();
    }
}
