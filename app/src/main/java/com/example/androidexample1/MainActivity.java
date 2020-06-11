package com.example.androidexample1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs = null;
    private EditText emailEdit = null;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3);

        prefs = getSharedPreferences("lab3email", Context.MODE_PRIVATE);
        emailEdit = findViewById(R.id.emailEdit);
        emailEdit.setText(prefs.getString("emailaddr", ""));

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v ->
            {
                Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
                goToProfile.putExtra("email", emailEdit.getText().toString());
                startActivity(goToProfile);
            });
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = prefs.edit();
        editor.putString("emailaddr", emailEdit.getText().toString());
        editor.commit();
    }

}