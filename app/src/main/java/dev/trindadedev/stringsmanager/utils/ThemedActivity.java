package dev.trindadedev.stringsmanager.utils;

import androidx.appcompat.app.AppCompatActivity; 
import androidx.activity.EdgeToEdge;

import android.os.Bundle;
import android.view.View;

public class ThemedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        enableBackgroundNav()
    }
    
    public void enableBackgroundNav(){
        getWindow().setNavigationBarColor(getColor(R.color.md_theme_background));
    }
}
