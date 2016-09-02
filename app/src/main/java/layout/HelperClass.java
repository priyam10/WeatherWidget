package layout;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.ppatel.widgetapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ppatel on 8/3/2016.
 */
public class HelperClass extends AsyncTask<Void, Void, List<RedditPost>> {
    private Context mContext;
    private String mUrl;
    public RemoteViews remoteView;
    private static String LOG_ERR = "error occured: ";
    public HelperClass(Context context, String url) {
        mContext = context;
        mUrl = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //dynamictext = (TextView) findViewById(R.id.dynamictext);
    }

    @Override
    protected List<RedditPost> doInBackground(Void... params) {
        List<RedditPost> resultString = getJSON(mUrl);
        //Log.d("Result string = ", resultString);
        for (RedditPost p: resultString) {
            Log.d("Title : "+ p.getTitle(), "Link : "+ p.getLink());
        }
        return resultString;
    }

    @Override
    protected void onPostExecute(List<RedditPost> strings) {
        super.onPostExecute(strings);
        //RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.my_widget);
        //remoteView.setTextViewText(R.id.appwidget_text, "LUL");
        //dynamictext.setText(strings);
    }

    public List<RedditPost> getJSON(String url) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();

            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(stream, null);
            List<RedditPost> results = parseXML(myParser);
            stream.close();

            return results;

        } catch (Exception ex) {
            Log.e(LOG_ERR, "general err");
            return null;
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Log.e(LOG_ERR, "disconect err");
                    //disconnect error
                }
            }
        }
    }


    public List<RedditPost> parseXML(XmlPullParser myParser) {

        int event;
        String text = null;
        List<RedditPost> results = new ArrayList<RedditPost>();

        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                RedditPost post = new RedditPost();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        //get country name
                        if (name.equals("title")) { //get title
                            post.setTitle(myParser.getAttributeValue(null, "value"));

                        } else if (name.equals("link")) { //get link
                            post.setLink(myParser.getAttributeValue(null, "value"));

                        }
                        break;
                }
                results.add(post);
                event = myParser.next();
            }
            return results;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}