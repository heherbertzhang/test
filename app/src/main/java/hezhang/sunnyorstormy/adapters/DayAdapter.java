package hezhang.sunnyorstormy.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import hezhang.sunnyorstormy.R;
import hezhang.sunnyorstormy.ui.DailyActivity;
import hezhang.sunnyorstormy.weather.Day;

/**
 * Created by herbert on 15-05-04.
 */
public class DayAdapter extends BaseAdapter {
    private Context mContext;
    private Day[] mDays;

    public DayAdapter(Context context, Day[] days) {
        mContext = context;
        mDays = days;
    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;//we aren't going to use this
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            //brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dailylist, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImage);
            holder.temperature = (TextView) convertView.findViewById(R.id.temperatureLabel);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayLabel);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }


        //Toast().makeText(DailyActivity.this, "nulllllll", Toast.LENGTH_LONG).show();

        Day day = mDays[position];
        if(day != null) {
            int id = day.getIconId();
            holder.iconImageView.setImageResource(id);
            holder.temperature.setText(day.getTempMax() + "");
            if(position == 0){
                holder.dayLabel.setText("Today");
            }else {
                holder.dayLabel.setText(day.getWeekDay());
            }
        }


        return convertView;
    }

    private static class ViewHolder {//has to be static
        ImageView iconImageView; // public by default
        TextView temperature;
        TextView dayLabel;

    }
}
