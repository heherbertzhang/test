package hezhang.sunnyorstormy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hezhang.sunnyorstormy.R;
import hezhang.sunnyorstormy.weather.Current;
import hezhang.sunnyorstormy.weather.Day;
import hezhang.sunnyorstormy.weather.Forecast;
import hezhang.sunnyorstormy.weather.Hour;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    private Forecast mForecast;
    private String Lat = "43.6617";
    private String Lon = "-79.3950";

    @InjectView(R.id.timeHourLabel) TextView mTimeLabel;
    @InjectView(R.id.Templabel) TextView mTemperatureLabel;
    @InjectView(R.id.HumidityValuetextView) TextView mHumidityValue;
    @InjectView(R.id.precipValueTextView) TextView mPrecipValue;
    @InjectView(R.id.summaryTextView) TextView mSummaryLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    @InjectView(R.id.RefreshImage) ImageView mRefreshImageView;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    @InjectView(R.id.latInput) EditText mLatInput;
    @InjectView(R.id.lonInput) EditText mLonInput;
    @InjectView(R.id.okButton)Button mOkButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);
        mLatInput.setVisibility(View.INVISIBLE);
        mLonInput.setVisibility(View.INVISIBLE);
        mOkButton.setVisibility(View.INVISIBLE);

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forecast(Lat, Lon);
            }
        });
        mLocationLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLoc();
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lat = mLatInput.getText().toString();
                Lon = mLonInput.getText().toString();
                if(Lat.isEmpty() || Lon.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Location Typed In!", Toast.LENGTH_LONG).show();
                    Lat = "43.6617";
                    Lon = "-79.3950";
                }
                forecast(Lat, Lon);
                displayLoc();
                mLatInput.setText("");
                mLonInput.setText("");
            }
        });


        forecast(Lat, Lon);
    }

    private void displayLoc() {
        if(mLocationLabel.getVisibility() == View.VISIBLE) {
            mLatInput.setVisibility(View.VISIBLE);
            mLonInput.setVisibility(View.VISIBLE);
            mOkButton.setVisibility(View.VISIBLE);
            mLocationLabel.setVisibility(View.INVISIBLE);
        }
        else{
            mLatInput.setVisibility(View.INVISIBLE);
            mLonInput.setVisibility(View.INVISIBLE);
            mOkButton.setVisibility(View.INVISIBLE);
            mLocationLabel.setVisibility(View.VISIBLE);
        }
    }

    private void forecast(String Lat, String Lon) {
        String apiKey = "f89d68bc7b4f8b279f4418712cb0215e";

        String foreCastUrl = "https://api.forecast.io/forecast/"
                + apiKey +"/"+ Lat + "," + Lon;
        if(isNetworkAvailable()){
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(foreCastUrl)
                    .build();
            Call call = client.newCall(request);
            Callback callback = new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if(response.isSuccessful()){
                            mForecast = parseForecastDetail(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toggleRefresh();
                                    updateDisplay();
                                }
                            });
                        }
                        else{
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception c: ", e);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Exception c: ", e);
                    }
                }
            };
            call.enqueue(callback);
        }
        else{
            alertNoInternet();
            //Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility() == View.INVISIBLE){
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrent();
        mTemperatureLabel.setText(current.getTemperature() + "");
        mTimeLabel.setText("At " + current.getFormatTime() + " it will be:");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());
        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
        mLocationLabel.setText(current.getTimeZone().replace('/',','));
    }
    private Forecast parseForecastDetail(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentWeather(jsonData));
        forecast.setHourly(getHourly(jsonData));
        forecast.setDaily(getDaily(jsonData));
        return forecast;
    }

    private Day[] getDaily(String jsonData) throws  JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily =forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];
        for(int i = 0; i < data.length(); i++){
            JSONObject jsonDay = data.getJSONObject(i);

            Day day = new Day();
            day.setTimezone(timezone);
            day.setTime(jsonDay.getLong("time"));
            day.setIcon(jsonDay.getString("icon"));
            day.setSummary(jsonDay.getString("summary"));
            day.setTempMax(jsonDay.getDouble("temperatureMax"));
            days[i] = day;
        }
        return days;
    }

    private Hour[] getHourly(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];
        for(int i = 0; i < data.length(); i++){
            JSONObject jsonHour = data.getJSONObject(i);

            Hour hour = new Hour();
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);
            hour.setSummary(jsonHour.getString("summary"));
            hours[i]= hour;
        }
        return hours;
    }

    private Current getCurrentWeather(String jsonData) throws JSONException{
        JSONObject weatherData = new JSONObject(jsonData);

        JSONObject currently = weatherData.getJSONObject("currently");
        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecip(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTimeZone(weatherData.getString("timezone"));
        return current;
    }

    private void alertNoInternet() {
        AlertInternetFragment fragment = new AlertInternetFragment();
        fragment.show(getFragmentManager(), "error_message2");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_message");
    }

    @OnClick(R.id.dailyButton)
    public void startDaily(View view){
        Intent intent = new Intent(this, DailyActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDaily());
        startActivity(intent);
    }

    @OnClick(R.id.hourlyButton)
    public void startHourly(View view){
        Intent intent = new Intent(this, HourlyActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourly());
        startActivity(intent);
    }

}
