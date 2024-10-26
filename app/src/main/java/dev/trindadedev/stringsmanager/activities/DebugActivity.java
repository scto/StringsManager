package dev.trindadedev.stringsmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import dev.trindadedev.stringsmanager.AppLogger;
import dev.trindadedev.stringsmanager.StringsManagerApp;
import dev.trindadedev.stringsmanager.utils.ThemedActivity;
import dev.trindadedev.stringsmanager.classes.TextUtils;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.InputStream;

public class DebugActivity extends ThemedActivity {
    
    AppLogger logger = StringsManagerApp.logger;
    
    String[] exceptionType = {
        "StringIndexOutOfBoundsException",
        "IndexOutOfBoundsException",
        "ArithmeticException",
        "NumberFormatException",
        "ActivityNotFoundException"
    };
    String[] errMessage = {
        "Invalid string operation\n",
        "Invalid list operation\n",
        "Invalid arithmetical operation\n",
        "Invalid toNumber block operation\n",
        "Invalid intent operation"
    };
    
    String saco = "saco";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String errMsg = "";
        String madeErrMsg = "";
        if (intent != null) {
            errMsg = intent.getStringExtra("error");
            String[] spilt = errMsg.split("\n");
            // errMsg = spilt[0];
            try {
                for (int j = 0; j < exceptionType.length; j++) {
                    if (spilt[0].contains(exceptionType[j])) {
                        madeErrMsg = errMessage[j];
                        int addIndex =
                                spilt[0].indexOf(exceptionType[j]) + exceptionType[j].length();
                        madeErrMsg += spilt[0].substring(addIndex, spilt[0].length());
                        break;
                    }
                }
                if (madeErrMsg.isEmpty()) madeErrMsg = errMsg;
            } catch (Exception e) {
            }
        }
        logger.add(madeErrMsg);
        saco = madeErrMsg;
        MaterialAlertDialogBuilder bld = new MaterialAlertDialogBuilder(this);
        bld.setTitle("An error occured");
        bld.setMessage(madeErrMsg);
        bld.setPositiveButton("Copy", (d, w) -> {
             TextUtils.copy(this, saco);
        });
        bld.setNeutralButton("End Application", (d, w) -> {
             finish();
        });
        bld.show();
    }
}
