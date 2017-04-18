package com.example.demo3;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;


public class AgentApplication extends Application {
	
	private static Context context;
	private List<Activity> activities = new ArrayList<Activity>();  
	  
    public void addActivity(Activity activity) {  
        activities.add(activity);  
    }  
  
    @Override  
    public void onTerminate() {  
        super.onTerminate();  
          
        for (Activity activity : activities) {  
            activity.finish();  
        }  
          
        //onDestroy();  
          
        System.exit(0);  
    } 
    
    @Override
    public void onCreate() {
        super.onCreate();
        AgentApplication.context = getApplicationContext(); 
    }

}
