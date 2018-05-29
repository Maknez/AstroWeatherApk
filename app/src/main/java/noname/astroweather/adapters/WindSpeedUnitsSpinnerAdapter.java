package noname.astroweather.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import noname.astroweather.R;

public class WindSpeedUnitsSpinnerAdapter extends BaseAdapter {
    Activity activity;
    String[] units;
    LayoutInflater inflater;

    public WindSpeedUnitsSpinnerAdapter(Activity activity) {
        this.activity = activity;
        this.units = new String[]{"mph", "kmph"};
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return units.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_row, null);
        TextView singleWindSpeedUnit = (TextView) row.findViewById(R.id.singleUnit);
        singleWindSpeedUnit.setText(units[position]);
        return row;
    }
}