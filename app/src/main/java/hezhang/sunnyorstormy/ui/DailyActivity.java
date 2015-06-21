package hezhang.sunnyorstormy.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hezhang.sunnyorstormy.R;
import hezhang.sunnyorstormy.adapters.DayAdapter;
import hezhang.sunnyorstormy.weather.Day;

public class DailyActivity extends Activity {
    private Day[] mDays;
    @InjectView(android.R.id.list) ListView mListView;
    @InjectView(android.R.id.empty) TextView mEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);//unparcel
        DayAdapter adapter = new DayAdapter(this, mDays);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmpty);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dayOfWeek = mDays[position].getWeekDay();
                String summary = mDays[position].getSummary();
                String hiTemp = mDays[position].getTempMax() + "";
                String msg = String.format("On %s the high will be %s and it will be %s",
                        dayOfWeek,
                        hiTemp,
                        summary);
                Toast.makeText(DailyActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
