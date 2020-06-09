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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3);
        onPause();

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v ->
            {   saveSharedPrefs(emailEdit.getText().toString());
                Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
                goToProfile.putExtra("lab3email", emailEdit.getText().toString());
                startActivity(goToProfile);
            });
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs = getSharedPreferences("lab3email", Context.MODE_PRIVATE);
        String savedString = prefs.getString("emailaddr", "");
        emailEdit = findViewById(R.id.emailEdit);
        emailEdit.setText(savedString);
    }

    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("emailaddr", stringToSave);
        editor.commit();
    }
}