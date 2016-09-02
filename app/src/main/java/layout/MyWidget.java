package layout;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.ppatel.widgetapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of App Widget functionality.
 */
public class MyWidget extends AppWidgetProvider implements LocationListener{
    public static Context myContext;
    public static ComponentName thisWidget;
    public static double celsius_temp;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        myContext = context;
        CharSequence widgetText = "LULzor";//context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        thisWidget = new ComponentName(context, MyWidget.class);
        JSONWeatherTask task = new JSONWeatherTask(views);
        task.execute("Edison");
        //HelperClass helper = new HelperClass(context, "https://www.reddit.com/r/news/.rss");
        //helper.remoteView = views;
        //helper.execute();
        try {
            String output_temp = JSONWeatherTask.weather_temp;
            double kelvin_temp = Double.parseDouble("output_temp");
            celsius_temp = kelvin_temp * 1.8 - 459.67;
            Log.d("Kelvin_temp is " + Double.toString(kelvin_temp), " | Celsius temp is " + celsius_temp);
        }catch(Exception e){

        }
        //views.setTextViewText(R.id.desc_textView, JSONWeatherTask.weather_desc);
        //views.setTextViewText(R.id.degrees_textView, Double.toString(celsius_temp) + " \u00b0" + "F");
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        /*LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        if ( ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            //ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
            //        12 );
            Log.d("LUL: ", "pERMIssion not granted");
        }*/
    }


    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

