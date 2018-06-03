package noname.astroweather.adapters.spinneradapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import noname.astroweather.R;

public class TemperatureUnitsSpinnerAdapter extends BaseAdapter {
    Activity activity;
    String[] units;
    LayoutInflater inflater;



    public TemperatureUnitsSpinnerAdapter(Activity activity) {
        this.activity = activity;
        this.units = new String[]{"°C", "°F"};
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return units.length;
    }

    @Override
    public Object getItem(int position) {
        switch (position){
            case 0: {
                return units[0];
            }
            case 1: {
                return units[1];
            }
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_row, null);
        TextView singleTemperatureUnit = (TextView) row.findViewById(R.id.singleUnit);
        singleTemperatureUnit.setText(units[position]);
        return row;
    }
}