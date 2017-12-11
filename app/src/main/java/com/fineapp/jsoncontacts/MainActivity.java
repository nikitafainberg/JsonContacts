package com.fineapp.jsoncontacts;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private SQLite sqLite;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqLite = new SQLite(this);
        intent = new Intent(MainActivity.this, ListActivity.class);

        SQLiteDatabase database = sqLite.getReadableDatabase();
        Cursor cursor = database.query(SQLite.CONTACTS, null, null, null, null, null, null);

        if(cursor.getCount() > 0) {
            startActivity(intent);
        }else {
            new AsyncLoad().execute();
        }
        cursor.close();
        sqLite.close();
    }

    public class AsyncLoad extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = null;
            try {
                URL url = new URL("https://api.androidhive.info/contacts/");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                buffer = new StringBuffer();

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null){
                    connection.disconnect();
                }

                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String resoult) {
            super.onPostExecute(resoult);
            try {
                JSONObject jsonObject = new JSONObject(resoult);

                JSONArray array = jsonObject.getJSONArray("contacts");

                for (int i = 0; i < array.length(); i++){
                    JSONObject object = (JSONObject) array.get(i);

                    String name = object.getString("name");
                    String email = object.getString("email");
                    String address = object.getString("address");
                    String gender = object.getString("gender");
                    String phone = object.getString("phone");

                    saveInSQLite(name, email, address, gender, phone);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void saveInSQLite(String name, String email, String address, String gender, String phone){
            SQLiteDatabase database = sqLite.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(SQLite.KEY_NAME, name);
            contentValues.put(SQLite.KEY_EMAIL, email);
            contentValues.put(SQLite.KEY_ADDRESS, address);
            contentValues.put(SQLite.KEY_GENDER, gender);
            contentValues.put(SQLite.KEY_PHONE, phone);

            database.insert(SQLite.CONTACTS, null, contentValues);
            sqLite.close();

            startActivity(intent);
        }
    }
}
