package dev.trindadedev.stringsmanager;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Process;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import dev.trindadedev.stringsmanager.activities.DebugActivity;
import dev.trindadedev.stringsmanager.adapters.StringsAdapter;
import dev.trindadedev.stringsmanager.fragments.MainFragment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

public class StringsManagerApp extends Application {

    private static FragmentManager sFragmentManager;
    public static AppLogger  logger = new AppLogger();
    
    public static final class Repo {
        public static final String FULL = "https://github.com/trindededev13/StringsManager";
        public static final String ONWER = "trindadedev13";
        public static final String NAME = "StringsManager";
    }

    public static void init(FragmentManager fragmentManager) {
        sFragmentManager = fragmentManager;
    }

    public static void updateListView(Context ctx, ArrayList<HashMap<String, Object>> listmap, ListView listStrings) {
        listStrings.setAdapter(new StringsAdapter(ctx, listmap, listStrings));
        ((BaseAdapter) listStrings.getAdapter()).notifyDataSetChanged();
        if (sFragmentManager != null) {
            sFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new MainFragment())
                    .commit();
        }
    }

    public static String getOrientation(Context ctx) {
        Configuration configuration = ctx.getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            return "portrait";
        } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return "landscape";
        } else {
            return "undefined";
        }
    }

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    @Override
    public void onCreate() {
        this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
        @Override
          public void uncaughtException(Thread thread, Throwable ex) {
             Intent intent = new Intent(getApplicationContext(), DebugActivity.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
             intent.putExtra("error", getStackTrace(ex)); PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 11111, intent, PendingIntent.FLAG_ONE_SHOT);
             AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
             am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, pendingIntent);
             Process.killProcess(Process.myPid());
             System.exit(2);
             uncaughtExceptionHandler.uncaughtException(thread, ex);
          }
        });
        super.onCreate();
    }

    private String getStackTrace(Throwable th) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        Throwable cause = th;
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        final String stacktraceAsString = result.toString();
        printWriter.close();
        return stacktraceAsString;
    }
}