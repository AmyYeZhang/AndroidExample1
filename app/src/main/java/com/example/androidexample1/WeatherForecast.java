package com.example.androidexample1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        pb = findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);

        ForecastQuery query = new ForecastQuery();
        query.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric",
                      "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");

    }

    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>{

        private String current, min, max, unit, iconName;
        private double UV;
        private Bitmap image = null;

        @Override
        protected String doInBackground(String... args) {
            try {

                //get the temperature information
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT
                while(eventType != XmlPullParser.END_DOCUMENT){

                    if(eventType == XmlPullParser.START_TAG){
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature")) {
                            //If you get here, then you are pointing to a <temperature> start tag
                            current = xpp.getAttributeValue(null,    "value");
                            publishProgress(25);
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                            unit = xpp.getAttributeValue(null, "unit");
                        } else if(xpp.getName().equals("weather")) {
                            //If you get here, then you are pointing to a <weather> start tag
                            iconName = xpp.getAttributeValue(null, "icon") + ".png"; // this will run for <AMessage message="parameter" >
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                //check the image exists or not
                if(fileExistance(iconName)){
                    //exist
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(iconName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    image = BitmapFactory.decodeStream(fis);
                    Log.i("ForecastQuery", "image " + iconName + " is found locally. ");

                } else {
                    //download the image
                    url = new URL("http://openweathermap.org/img/w/" + iconName);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(urlConnection.getInputStream());
                    }

                    //save image to local
                    FileOutputStream outputStream = openFileOutput( iconName, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    Log.i("ForecastQuery", "image " + iconName + " is downloaded. ");
                }
                publishProgress(100);

                //get the UV rate
                url = new URL(args[1]);
                urlConnection = (HttpURLConnection) url.openConnection();
                response = urlConnection.getInputStream();
                //JSON reading
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string
                // convert string to JSON
                JSONObject uvReport = new JSONObject(result);
                //get the double associated with "value"
                UV = uvReport.getDouble("value");
            }
            catch (Exception e)
            {

            }
            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //pb.setVisibility(View.VISIBLE);
            pb.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String fromDoInBackground) {
            //set the TextView
            TextView tvCurrent = findViewById(R.id.tvCurrent);
            tvCurrent.setText(tvCurrent.getText() + current + unit);
            TextView tvMin = findViewById(R.id.tvMin);
            tvMin.setText(tvMin.getText() + min + unit);
            TextView tvMax = findViewById(R.id.tvMax);
            tvMax.setText(tvMax.getText() + max + unit);
            TextView tvUV = findViewById(R.id.tvUV);
            tvUV.setText(tvUV.getText() + Double.toString(UV));
            ImageView iv = findViewById(R.id.ivWeather);
            iv.setImageBitmap(image);

            //set the ProgressBar
            pb.setVisibility(View.INVISIBLE);
        }
    }
}