package com.example.translatorapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TextTranslation extends AppCompatActivity {


    private EditText sourceLanguageEt;
    private TextView destinationLanguageTv;
    private MaterialButton sourceLanguageChooseBtn;
    private MaterialButton destinationLanguageChooseBtn;
    private MaterialButton translateBtn;

    private TranslatorOptions translatorOptions;
    private Translator translator;
    private ProgressDialog progressDialog;
    private ArrayList<ModelLanguage> languageArrayList;
    private static final String TAG = "MAIN_TAG";
    private String sourceLanguageCode=  "en";
    private String sourceLanguageTitle = "English";
    private String destinationLanguageCode = "hi";
    private String destinationLanguageTitle = "Hindi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_translation);

        sourceLanguageEt = findViewById(R.id.sourceLanguageEt);
        destinationLanguageTv = findViewById(R.id.destinationLanguageTv);
        sourceLanguageChooseBtn = findViewById(R.id.sourceLanguageChooseBtn);
        destinationLanguageChooseBtn = findViewById(R.id.destinationLanguageChooseBtn);
        translateBtn = findViewById(R.id.translateBtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        loadAvailableLanguages();

        //select source language
        sourceLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sourceLanguageChoose();
            }
        });

        //select target language
        destinationLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinationLanguageChoose();
            }
        });

        //actually translate the text

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private String sourceLanguageText;
    private void validateData() {
        sourceLanguageText = sourceLanguageEt.getText().toString().trim();
        Log.d(TAG,"validateData: sourceLanguageText: "+sourceLanguageText);
        if(sourceLanguageText.isEmpty()){
            Toast.makeText(this, "Enter text to translate", Toast.LENGTH_SHORT).show();
        }
        else{
            startTranslations();
        }

    }

    private void startTranslations() {
        // progressDialog.setMessage("Processing language Model");
        progressDialog.show();
        translatorOptions = new TranslatorOptions.Builder().setSourceLanguage(sourceLanguageCode).setTargetLanguage(destinationLanguageCode).build();
        translator = Translation.getClient(translatorOptions);


        DownloadConditions downloadConditions = new DownloadConditions.Builder().requireWifi().build();
        translator.downloadModelIfNeeded(downloadConditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //translation model ready to translated
                Log.d(TAG,"onSuccess: model ready , starting translate...");
                // progressDialog.setMessage("Translating...");

                //start translation process
                translator.translate(sourceLanguageText).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d(TAG,"onSucces: Translated Text: "+s);
                        destinationLanguageTv.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.e(TAG,"onFailure: ", e);
                        Toast.makeText(TextTranslation.this, "Failed to translate due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.e(TAG,"onFailure: ", e);
                Toast.makeText(TextTranslation.this, "Failes to ready model due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sourceLanguageChoose(){
        PopupMenu popupMenu = new PopupMenu(this,sourceLanguageChooseBtn);
        for (int i = 0;i<languageArrayList.size();i++){
            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).languageTitle);
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position  = item.getItemId();

                sourceLanguageCode = languageArrayList.get(position).languageCode;
                sourceLanguageTitle = languageArrayList.get(position).languageTitle;
                sourceLanguageChooseBtn.setText(sourceLanguageTitle);
                sourceLanguageEt.setHint("Enter"+sourceLanguageTitle);

                Log.d(TAG,"onMenuItemClick: sourceLanguageCode: "+sourceLanguageCode);
                Log.d(TAG,"onMenuItemClick: sourceLanguageTitle: "+sourceLanguageTitle);

                return false;
            }
        });
    }

    private void destinationLanguageChoose(){
        PopupMenu popupMenu = new PopupMenu(this,destinationLanguageChooseBtn);
        for(int i = 0;i<languageArrayList.size();i++){
            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).getLanguageTitle());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int position = item.getItemId();
                destinationLanguageCode = languageArrayList.get(position).languageCode;
                destinationLanguageTitle = languageArrayList.get(position).languageTitle;
                destinationLanguageChooseBtn.setText(destinationLanguageTitle);

                Log.d(TAG, "onMenuItemClick: destinationLanguageCode: "+destinationLanguageCode);
                Log.d(TAG, "onMenuItemClick: destinationLanguageTitle: "+destinationLanguageTitle);

                return false;
            }
        });
    }


    private void loadAvailableLanguages() {
        languageArrayList = new ArrayList<>();

        List<String> languageCodeList = TranslateLanguage.getAllLanguages();
        for(String languageCode : languageCodeList){
            String languageTitle = new Locale(languageCode).getDisplayLanguage();
            Log.d(TAG,"loadAvailableLanguages: languageCode"+languageCode);
            Log.d(TAG,"loadAvailableLanguages: languageTitle"+languageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(languageCode,languageTitle);
            languageArrayList.add(modelLanguage);
        }

    }
}