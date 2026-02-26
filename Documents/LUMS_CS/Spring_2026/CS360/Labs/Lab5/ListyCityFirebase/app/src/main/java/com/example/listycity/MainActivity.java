package com.example.listycity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView cityList;
    CityArrayAdapter cityAdapter;
    ArrayList<city> dataList;

    private FirebaseFirestore db;
    private CollectionReference citiesRef;
    private static final String TAG = "Firestore";

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);

        dataList = new ArrayList<>();
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        db = FirebaseFirestore.getInstance();
        citiesRef = db.collection("cities");

        citiesRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e(TAG, "Listen failed.", error);
                return;
            }
            if (value == null) return;

            dataList.clear();
            for (QueryDocumentSnapshot doc : value) {
                city c = doc.toObject(city.class);
                if (c != null) dataList.add(c);
            }
            cityAdapter.notifyDataSetChanged();
            selectedPosition = -1;
        });

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
        deleteBtn.setOnClickListener(v -> deleteSelectedCity());
    }

    private void deleteSelectedCity() {
        if (selectedPosition == -1 || selectedPosition >= dataList.size()) {
            new AlertDialog.Builder(this)
                    .setTitle("No City Selected")
                    .setMessage("Tap a city first, then press DELETE CITY.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        city toDelete = dataList.get(selectedPosition);
        String docId = toDelete.getName();

        new AlertDialog.Builder(this)
                .setTitle("Delete City")
                .setMessage("Delete " + docId + "?")
                .setPositiveButton("DELETE", (d, w) -> {
                    citiesRef.document(docId)
                            .delete()
                            .addOnSuccessListener(v -> Log.d(TAG, "City deleted"))
                            .addOnFailureListener(e -> Log.e(TAG, "Delete failed", e));
                    selectedPosition = -1;
                })
                .setNegativeButton("CANCEL", null)
                .show();
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
                        city newCity = new city(cityName, provinceName);
                        citiesRef.document(cityName)
                                .set(newCity)
                                .addOnSuccessListener(v -> Log.d(TAG, "City added"))
                                .addOnFailureListener(e -> Log.e(TAG, "Add failed", e));
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
        String oldName = selectedCity.getName();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        layout.setPadding(pad, pad, pad, pad);

        final EditText cityInput = new EditText(this);
        cityInput.setHint("City name");
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setText(oldName);

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
                        city updated = new city(updatedCity, updatedProvince);

                        if (oldName.equals(updatedCity)) {
                            citiesRef.document(oldName)
                                    .set(updated)
                                    .addOnSuccessListener(v -> Log.d(TAG, "City updated"))
                                    .addOnFailureListener(e -> Log.e(TAG, "Update failed", e));
                        } else {
                            citiesRef.document(oldName)
                                    .delete()
                                    .addOnSuccessListener(v -> citiesRef.document(updatedCity)
                                            .set(updated)
                                            .addOnSuccessListener(v2 -> Log.d(TAG, "City updated"))
                                            .addOnFailureListener(e -> Log.e(TAG, "Update failed", e)))
                                    .addOnFailureListener(e -> Log.e(TAG, "Update failed", e));
                        }

                        selectedPosition = -1;
                    }
                })
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel())
                .show();
    }
}