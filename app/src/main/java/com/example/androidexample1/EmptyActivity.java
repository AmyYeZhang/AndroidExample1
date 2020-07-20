package com.example.androidexample1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Bundle dataToPass = getIntent().getExtras();

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(dataToPass);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment).commit();
    }
}