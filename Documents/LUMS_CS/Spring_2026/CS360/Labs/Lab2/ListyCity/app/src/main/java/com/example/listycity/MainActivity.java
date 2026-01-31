package com.example.listycity;

import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ListView cityList;
    ArrayAdapter<String> cityAdapter;
    ArrayList<String> dataList;

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        cityList=findViewById(R.id.city_list);
        String []cities={"Edmonton","Vancouver","Moscow","Sydney","Berlin","Vietnam","Tokyo","Islamabad","Mumbai","Sau Paulo"};
        dataList=new ArrayList<>();
        dataList.addAll(Arrays.asList(cities));
        cityAdapter=new ArrayAdapter<>(this, R.layout.content, dataList);
        cityList.setAdapter(cityAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button addBtn = findViewById(R.id.add_city_button);
        Button deleteBtn = findViewById(R.id.delete_city_button);

        cityList.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
        });

        addBtn.setOnClickListener(v -> {
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);

            new AlertDialog.Builder(this)
                    .setTitle("Add a New City")
                    .setView(input)
                    .setPositiveButton("ADD", (dialog, which) -> {
                        String cityName = input.getText().toString();
                        if (!cityName.isEmpty()) {
                            dataList.add(cityName);
                            cityAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel())
                    .show();
        });

        deleteBtn.setOnClickListener(v -> {
            if (selectedPosition != -1 && selectedPosition < dataList.size()) {
                dataList.remove(selectedPosition);
                cityAdapter.notifyDataSetChanged();
                selectedPosition = -1;
            }
        });
    }
}