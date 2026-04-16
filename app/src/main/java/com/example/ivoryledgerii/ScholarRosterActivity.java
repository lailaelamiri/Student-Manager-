package com.example.ivoryledgerii;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.ivoryledgerii.beans.ScholarRecord;

import java.lang.reflect.Type;
import java.util.*;

public class ScholarRosterActivity extends AppCompatActivity {

    private ListView scholarListView;
    private ScholarRosterAdapter rosterAdapter;
    private List<ScholarRecord> vaultRoster = new ArrayList<>();
    private RequestQueue ivoryRequestQueue;

    private static final String LOAD_ENDPOINT   = "http://10.0.2.2/IvoryLedgerII/ws/loadScholars.php";
    private static final String ENROLL_ENDPOINT = "http://10.0.2.2/IvoryLedgerII/ws/enrollScholar.php";
    private static final String REMOVE_ENDPOINT = "http://10.0.2.2/IvoryLedgerII/ws/removeScholar.php";
    private static final String AMEND_ENDPOINT  = "http://10.0.2.2/IvoryLedgerII/ws/amendScholar.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholar_roster);

        ivoryRequestQueue = Volley.newRequestQueue(this);
        scholarListView   = findViewById(R.id.scholarListView);
        rosterAdapter     = new ScholarRosterAdapter(this, vaultRoster);
        scholarListView.setAdapter(rosterAdapter);

        fetchRosterFromVault();

        scholarListView.setOnItemClickListener((parent, view, position, id) -> {
            ScholarRecord tappedScholar = vaultRoster.get(position);
            showScholarOptionsDialog(tappedScholar);
        });
    }

    private String safe(String value) {
        return (value == null || value.isEmpty()) ? "N/A" : value;
    }

    private void fetchRosterFromVault() {
        StringRequest loadRequest = new StringRequest(
                Request.Method.GET,
                LOAD_ENDPOINT,
                vaultResponse -> {
                    Log.d("IvoryLedger_LOAD", vaultResponse);

                    Type collectionType = new TypeToken<Collection<ScholarRecord>>(){}.getType();
                    Collection<ScholarRecord> freshRoster = new Gson().fromJson(vaultResponse, collectionType);

                    vaultRoster.clear();

                    for (ScholarRecord s : freshRoster) {
                        if (s != null) {
                            vaultRoster.add(s);
                        }
                    }

                    rosterAdapter.notifyDataSetChanged();
                },
                error -> {
                    Log.e("IvoryLedger_ERROR", "Load failed: " + error.getMessage());
                    Toast.makeText(this, "Failed to load scholars", Toast.LENGTH_SHORT).show();
                }
        );
        ivoryRequestQueue.add(loadRequest);
    }

    private void showScholarOptionsDialog(ScholarRecord s) {
        new AlertDialog.Builder(this)
                .setTitle(safe(s.getFamilyName()) + " " + safe(s.getGivenName()))
                .setMessage("What would you like to do with this scholar?")
                .setPositiveButton("✏️ Edit", (d, w) -> showEditScholarDialog(s))
                .setNegativeButton("🗑️ Remove", (d, w) -> showDeleteConfirmationDialog(s))
                .setNeutralButton("Cancel", null)
                .show();
    }

    private void showDeleteConfirmationDialog(ScholarRecord s) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Removal")
                .setMessage("Remove " + safe(s.getFamilyName()) + " " + safe(s.getGivenName()) + "?")
                .setPositiveButton("Remove", (d, w) -> dispatchRemoveRequest(s))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditScholarDialog(ScholarRecord s) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_scholar, null);

        EditText family  = view.findViewById(R.id.dialogFamilyName);
        EditText given   = view.findViewById(R.id.dialogGivenName);
        Spinner city     = view.findViewById(R.id.dialogCitySpinner);
        RadioButton male = view.findViewById(R.id.dialogRadioMale);
        RadioButton female = view.findViewById(R.id.dialogRadioFemale);

        family.setText(safe(s.getFamilyName()));
        given.setText(safe(s.getGivenName()));

        List<String> cities = Arrays.asList(getResources().getStringArray(R.array.moroccan_cities));
        int index = cities.indexOf(s.getHomeCity());
        if (index >= 0) city.setSelection(index);

        if ("male".equals(s.getGenderTag())) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }

        new AlertDialog.Builder(this)
                .setTitle("Edit Scholar")
                .setView(view)
                .setPositiveButton("Save", (d, w) -> {
                    dispatchAmendRequest(
                            s.getScholarId(),
                            family.getText().toString().trim(),
                            given.getText().toString().trim(),
                            city.getSelectedItem().toString(),
                            male.isChecked() ? "male" : "female"
                    );
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void dispatchRemoveRequest(ScholarRecord s) {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                REMOVE_ENDPOINT,
                r -> {
                    Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
                    fetchRosterFromVault();
                },
                e -> Toast.makeText(this, "Remove failed", Toast.LENGTH_SHORT).show()
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("scholarId", String.valueOf(s.getScholarId()));
                return p;
            }
        };
        ivoryRequestQueue.add(req);
    }

    private void dispatchAmendRequest(int id, String f, String g, String c, String gender) {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                AMEND_ENDPOINT,
                r -> {
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                    fetchRosterFromVault();
                },
                e -> Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("scholarId", String.valueOf(id));
                p.put("familyName", f);
                p.put("givenName", g);
                p.put("homeCity", c);
                p.put("genderTag", gender);
                return p;
            }
        };
        ivoryRequestQueue.add(req);
    }
}