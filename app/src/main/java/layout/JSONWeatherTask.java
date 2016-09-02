package layout;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.ppatel.widgetapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ppatel on 8/16/2016.
 */
public class JSONWeatherTask extends AsyncTask<String, Void, Weather>{
    private RemoteViews views;
    public static String weather_desc = "";
    public static String weather_temp = "";

    public JSONWeatherTask(RemoteViews remoteView){
        this.views = remoteView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //dynamictext = (TextView) findViewById(R.id.dynamictext);
    }

    @Override
    protected Weather doInBackground(String... params) {
        //Weather weather = new Weather();
        String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));
        Weather weatherData = fetchWeather(data);
        //Log.d("JSONWeatherTask : ",data);
        /*try {
            weather = JSONWeatherParser.getWeather(data);

        // Let's retrieve the icon
            //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return weatherData;

    }

    @Override
    protected void onPostExecute(Weather output) {
        super.onPostExecute(output);

        Intent intent=new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(MyWidget.myContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        double kelvin_temp = Double.parseDouble(output.Temp);
        int celsius_temp = (int) Math.ceil(kelvin_temp * 1.8 - 459.67);
        views.setTextViewText(R.id.degrees_textView, Integer.toString(celsius_temp) + " \u00b0" + "F");
        views.setTextViewText(R.id.desc_textView, output.Desc.toUpperCase());
        AppWidgetManager manager = AppWidgetManager.getInstance(MyWidget.myContext.getApplicationContext());
        manager.updateAppWidget(MyWidget.thisWidget, views);
    }

    public Weather fetchWeather(String data){
        Weather weather = new Weather();
        // We start extracting the info
        try {
            // We create out JSONObject from the data
            JSONObject jObj = new JSONObject(data);
            JSONObject coordObj = getObject("coord", jObj);

            //loc.setLatitude(getFloat("lat", coordObj));
            //loc.setLongitude(getFloat("lon", coordObj));

            JSONObject sysObj = getObject("sys", jObj);
            /*
            loc.setCountry(getString("country", sysObj));
            loc.setSunrise(getInt("sunrise", sysObj));
            loc.setSunset(getInt("sunset", sysObj));
            loc.setCity(getString("name", jObj));
            weather.location = loc;*/
            // We get weather info (This is an array)
            JSONArray jArr = jObj.getJSONArray("weather");

            // We use only the first value
            JSONObject JSONWeather = jArr.getJSONObject(0);
            Log.d("Description: ", getString("description", JSONWeather));
            weather.Desc = getString("description", JSONWeather);
            weather_desc = weather.Desc;
            //views.setTextViewText(R.id.desc_textView, getString("description", JSONWeather));
            /*weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
            weather.currentCondition.setDescr(getString("description", JSONWeather));
            weather.currentCondition.setCondition(getString("main", JSONWeather));
            //weather.currentCondition.setIcon(getString("icon", JSONWeather));*/

            JSONObject mainObj = getObject("main", jObj);
            Log.d("Temp: ", getString("temp", mainObj));
            weather.Temp =  getString("temp", mainObj);
            weather_temp = weather.Temp;
            /*try {
                double kelvin_temp = Double.parseDouble("weather_temp");
                double celsius_temp = kelvin_temp * (9 / 5) - 459.67;
                weather_temp = Double.toString(celsius_temp) + " \u00b0" + "F";
            }catch(NumberFormatException e){
                Log.d("NumberformatException happened. weather_temp is : ", weather_temp);
            }*/
            //views.setTextViewText(R.id.degrees_textView, getString("temp", mainObj));
            /*
            weather.currentCondition.setHumidity(getInt("humidity", mainObj));
            weather.currentCondition.setPressure(getInt("pressure", mainObj));
            weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
            weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
            weather.temperature.setTemp(getFloat("temp", mainObj));

// Wind
            JSONObject wObj = getObject("wind", jObj);
            weather.wind.setSpeed(getFloat("speed", wObj));
            weather.wind.setDeg(getFloat("deg", wObj));

// Clouds
            JSONObject cObj = getObject("clouds", jObj);
            weather.clouds.setPerc(getInt("all", cObj));*/
        }catch(JSONException e){

        }
        return weather;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}
