package com.example.androidflutter;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startFlutterActivity(View view) {
        ComponentName componentName = new ComponentName(this, "com.example.flutterpart.MainActivity");
        Intent intent = new Intent().setComponent(componentName);
        startActivity(intent);
    }
}
