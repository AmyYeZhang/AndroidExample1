package com.example.androidexample1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(v -> Toast.makeText(this, R.string.toast_message, Toast.LENGTH_LONG).show());

        CheckBox cb1 = findViewById(R.id.checkbox1);
        cb1.setOnCheckedChangeListener((cb, b) ->
            {Snackbar.make(cb1, b?R.string.on_msg:R.string.off_msg, Snackbar.LENGTH_LONG).setAction("Undo", click->cb1.setChecked(!b)).show();});

        Switch sw1 = findViewById(R.id.switch1);
        sw1.setOnCheckedChangeListener((cb, b) ->
            {Snackbar.make(sw1, b?R.string.on_msg:R.string.off_msg, Snackbar.LENGTH_LONG).setAction("Undo", click->sw1.setChecked(!b)).show();});
    }
}