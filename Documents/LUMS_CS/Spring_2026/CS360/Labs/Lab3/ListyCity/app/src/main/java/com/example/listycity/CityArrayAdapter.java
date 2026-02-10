package com.example.listycity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CityArrayAdapter extends ArrayAdapter<city> {

    public CityArrayAdapter(@NonNull Context context, @NonNull ArrayList<city> cities) {
        super(context, 0, cities);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.city_row, parent, false);
        }

        city city = getItem(position);

        TextView nameText = view.findViewById(R.id.city_name_text);
        TextView provinceText = view.findViewById(R.id.city_province_text);

        if (city != null) {
            nameText.setText(city.getName());
            provinceText.setText(city.getProvince());
        }

        return view;
    }
}
