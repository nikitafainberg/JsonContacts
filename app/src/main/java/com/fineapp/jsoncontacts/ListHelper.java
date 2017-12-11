package com.fineapp.jsoncontacts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListHelper extends BaseAdapter {

    private List<Form> appViews;
    private Context context;

    public ListHelper(Context context, List<Form> appViews){
        this.context = context;
        this.appViews = appViews;
    }

    @Override
    public int getCount() {
        return appViews.size();
    }

    @Override
    public Object getItem(int position) {
        return appViews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item, parent, false);
        }
        Form form = getForm(position);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtEmail = convertView.findViewById(R.id.txtEmail);
        TextView txtAddress = convertView.findViewById(R.id.txtAddress);
        Spinner spinner = convertView.findViewById(R.id.spinner);

        txtName.setText(form.name);
        txtEmail.setText(form.email);
        txtAddress.setText(form.address);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, jsonParser(form.phone));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                Log.d("!!!!!!!!!!!!!!!", spinner.getSelectedItem().toString());
//                itemSelected = spinner.getItemAtPosition(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        return convertView;
    }

    private ArrayList<String> jsonParser(String text){
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(text);

            list.add(object.getString("mobile"));
            list.add(object.getString("home"));
            list.add(object.getString("office"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Form getForm(int position){
        return (Form)getItem(position);
    }
}
