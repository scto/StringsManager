package dev.trindadedev.stringsmanager.utils;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity; 
import androidx.activity.EdgeToEdge;

import dev.trindadedev.stringsmanager.R;

public class ThemedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        enableBackgroundNav();
    }
    
    public void enableBackgroundNav(){
        getWindow().setNavigationBarColor(getColor(R.color.md_theme_background));
    }
}
