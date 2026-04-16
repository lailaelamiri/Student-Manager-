package com.example.ivoryledgerii;
import android.content.Intent;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EnrollScholarActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputFamilyName;
    private EditText inputGivenName;
    private Spinner spinnerHomeCity;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private Button btnSubmitEnrollment;
    private RequestQueue ivoryRequestQueue;

    private static final String ENROLL_ENDPOINT = "http://10.0.2.2/IvoryLedgerII/ws/enrollScholar.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_scholar);

        inputFamilyName     = findViewById(R.id.inputFamilyName);
        inputGivenName      = findViewById(R.id.inputGivenName);
        spinnerHomeCity     = findViewById(R.id.spinnerHomeCity);
        radioMale           = findViewById(R.id.radioMale);
        radioFemale         = findViewById(R.id.radioFemale);
        btnSubmitEnrollment = findViewById(R.id.btnSubmitEnrollment);

        ivoryRequestQueue = Volley.newRequestQueue(this);
        btnSubmitEnrollment.setOnClickListener(this);

        runEntryAnimations();
        Button btnViewRoster = findViewById(R.id.btnViewRoster);
        btnViewRoster.setOnClickListener(v -> {
            startActivity(new Intent(EnrollScholarActivity.this, ScholarRosterActivity.class));
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmitEnrollment) {
            animateButtonPress(v);
            dispatchEnrollmentRequest();
        }
    }

    private void dispatchEnrollmentRequest() {
        StringRequest enrollRequest = new StringRequest(
                Request.Method.POST,
                ENROLL_ENDPOINT,
                vaultResponse -> {
                    Log.d("IvoryLedger_RESPONSE", vaultResponse);
                    Type collectionType = new TypeToken<Collection<ScholarRecord>>(){}.getType();
                    Collection<ScholarRecord> scholarRoster = new Gson().fromJson(vaultResponse, collectionType);
                    for (ScholarRecord record : scholarRoster) {
                        Log.d("IvoryLedger_SCHOLAR", record.toString());
                    }
                    Toast.makeText(EnrollScholarActivity.this, "Scholar enrolled successfully", Toast.LENGTH_SHORT).show();
                    clearEnrollmentFields();
                },
                networkError -> {
                    Log.e("IvoryLedger_ERROR", "Network failure: " + networkError.getMessage());
                    Toast.makeText(EnrollScholarActivity.this, "Connection failed — check XAMPP", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                String selectedGender = radioMale.isChecked() ? "male" : "female";
                Map<String, String> enrollmentParams = new HashMap<>();
                enrollmentParams.put("familyName", inputFamilyName.getText().toString().trim());
                enrollmentParams.put("givenName",  inputGivenName.getText().toString().trim());
                enrollmentParams.put("homeCity",   spinnerHomeCity.getSelectedItem().toString());
                enrollmentParams.put("genderTag",  selectedGender);
                return enrollmentParams;
            }
        };
        ivoryRequestQueue.add(enrollRequest);
    }

    void clearEnrollmentFields() {
        inputFamilyName.setText("");
        inputGivenName.setText("");
    }

    void animateButtonPress(View targetView) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(targetView, "scaleX", 1f, 0.93f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(targetView, "scaleY", 1f, 0.93f);
        ObjectAnimator scaleUpX   = ObjectAnimator.ofFloat(targetView, "scaleX", 0.93f, 1f);
        ObjectAnimator scaleUpY   = ObjectAnimator.ofFloat(targetView, "scaleY", 0.93f, 1f);
        scaleDownX.setDuration(80);
        scaleDownY.setDuration(80);
        scaleUpX.setDuration(180);
        scaleUpY.setDuration(180);
        scaleUpX.setInterpolator(new OvershootInterpolator());
        scaleUpY.setInterpolator(new OvershootInterpolator());
        AnimatorSet pressSet = new AnimatorSet();
        pressSet.play(scaleDownX).with(scaleDownY);
        pressSet.play(scaleUpX).with(scaleUpY).after(scaleDownX);
        pressSet.start();
    }

    void runEntryAnimations() {
        View[] panels = {inputFamilyName, inputGivenName, spinnerHomeCity, btnSubmitEnrollment};
        for (int i = 0; i < panels.length; i++) {
            panels[i].setAlpha(0f);
            panels[i].setTranslationY(40f);
            panels[i].animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(100L * i)
                    .setDuration(450)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }
    }
}