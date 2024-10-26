package dev.trindadedev.stringsmanager;

import java.util.ArrayList;
import java.util.List;

public class AppLogger {
    
    List<String> logs;
    
    public AppLogger() {
        logs = new ArrayList<>();
    }
    
    public String getLogs(){
        return logs.toString();
    }
    
    public void add(String logVal){
        logs.add(logVal);
    }
}
