package com.example.translatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;

public class LanguageDetection extends AppCompatActivity {

    EditText etLangString;
    Button btnCheckNow;
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_language_detection);

        etLangString = findViewById(R.id.etLangString);
        btnCheckNow = findViewById(R.id.btnCheckNow);
        tvResult = findViewById(R.id.tvResult);
        btnCheckNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String langText = etLangString.getText().toString();
                if (langText.equals("")) {
                    Toast.makeText(LanguageDetection.this, "Enter the text", Toast.LENGTH_SHORT).show();
                } else {
                    detectLang(langText);
                }
            }
        });
    }

    private void detectLang(String langText){
        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();
        languageIdentifier.identifyLanguage(langText).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String languageCode) {
                if(languageCode.equals("und")){
                    tvResult.setText("Can't identify language");
                }
                else{
                    tvResult.setText("Language is "+languageCode);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tvResult.setText("Exception "+e.getMessage());
            }
        });
    }
}