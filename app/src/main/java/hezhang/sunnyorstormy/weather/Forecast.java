package hezhang.sunnyorstormy.weather;

import hezhang.sunnyorstormy.R;

/**
 * Created by herbert on 15-05-01.
 */

public class Forecast {
    private Current mCurrent;
    private Hour[] mHourly;
    private Day[] mDaily;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHourly() {
        return mHourly;
    }

    public void setHourly(Hour[] hourly) {
        mHourly = hourly;
    }

    public Day[] getDaily() {
        return mDaily;
    }

    public void setDaily(Day[] daily) {
        mDaily = daily;
    }

    public static int getIconId(String iconString){
        int iconId = 0;
        if (iconString.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (iconString.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (iconString.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (iconString.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (iconString.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }
        return iconId;
    }
}
