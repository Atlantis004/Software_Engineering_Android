package com.example.listycity;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView cityList;
    CityArrayAdapter cityAdapter;
    ArrayList<city> dataList;

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);


        dataList = new ArrayList<>();
        dataList.add(new city("Edmonton", "Alberta"));
        dataList.add(new city("Vancouver", "British Columbia"));
        dataList.add(new city("Moscow", "Moscow"));
        dataList.add(new city("Sydney", "New South Wales"));
        dataList.add(new city("Berlin", "Berlin"));
        dataList.add(new city("Jaisalmer", "Rajasthan"));
        dataList.add(new city("Tokyo", "Tokyo"));
        dataList.add(new city("Islamabad", "ICT"));
        dataList.add(new city("Mumbai", "Maharashtra"));
        dataList.add(new city("Sao Paulo", "Sao Paulo"));

        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button addBtn = findViewById(R.id.add_city_button);
        Button editBtn = findViewById(R.id.edit_city_button);
        Button deleteBtn = findViewById(R.id.delete_city_button);


        cityList.setOnItemClickListener((parent, view, position, id) -> selectedPosition = position);

        addBtn.setOnClickListener(v -> showAddDialog());
        editBtn.setOnClickListener(v -> showEditDialog());
        deleteBtn.setOnClickListener(v -> {
            if (selectedPosition != -1 && selectedPosition < dataList.size()) {
                dataList.remove(selectedPosition);
                cityAdapter.notifyDataSetChanged();
                selectedPosition = -1;
            }
        });
    }

    private void showAddDialog() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        layout.setPadding(pad, pad, pad, pad);

        final EditText cityInput = new EditText(this);
        cityInput.setHint("City name");
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);

        final EditText provinceInput = new EditText(this);
        provinceInput.setHint("Province");
        provinceInput.setInputType(InputType.TYPE_CLASS_TEXT);

        layout.addView(cityInput);
        layout.addView(provinceInput);

        new AlertDialog.Builder(this)
                .setTitle("Add a New City")
                .setView(layout)
                .setPositiveButton("ADD", (dialog, which) -> {
                    String cityName = cityInput.getText().toString().trim();
                    String provinceName = provinceInput.getText().toString().trim();

                    if (!cityName.isEmpty() && !provinceName.isEmpty()) {
                        dataList.add(new city(cityName, provinceName));
                        cityAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void showEditDialog() {
        if (selectedPosition == -1 || selectedPosition >= dataList.size()) {
            new AlertDialog.Builder(this)
                    .setTitle("No City Selected")
                    .setMessage("Tap a city first, then press EDIT CITY.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        city selectedCity = dataList.get(selectedPosition);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        layout.setPadding(pad, pad, pad, pad);

        final EditText cityInput = new EditText(this);
        cityInput.setHint("City name");
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setText(selectedCity.getName());

        final EditText provinceInput = new EditText(this);
        provinceInput.setHint("Province");
        provinceInput.setInputType(InputType.TYPE_CLASS_TEXT);
        provinceInput.setText(selectedCity.getProvince());

        layout.addView(cityInput);
        layout.addView(provinceInput);

        new AlertDialog.Builder(this)
                .setTitle("Edit City")
                .setView(layout)
                .setPositiveButton("SAVE", (dialog, which) -> {
                    String updatedCity = cityInput.getText().toString().trim();
                    String updatedProvince = provinceInput.getText().toString().trim();

                    if (!updatedCity.isEmpty() && !updatedProvince.isEmpty()) {
                        selectedCity.setName(updatedCity);
                        selectedCity.setProvince(updatedProvince);
                        cityAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel())
                .show();
    }
}
