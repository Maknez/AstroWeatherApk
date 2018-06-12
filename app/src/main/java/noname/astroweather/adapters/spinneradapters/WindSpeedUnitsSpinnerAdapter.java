package noname.astroweather.adapters.spinneradapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import noname.astroweather.R;

public class WindSpeedUnitsSpinnerAdapter extends BaseAdapter {
    String[] windSpeedUnits;
    LayoutInflater inflater;

    public WindSpeedUnitsSpinnerAdapter(Activity activity) {
        this.windSpeedUnits = new String[]{"mph", "kmph"};
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return windSpeedUnits.length;
    }

    @Override
    public Object getItem(int position) {
        return windSpeedUnits[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View singleUnitViewInSpinner = inflater.inflate(R.layout.spinner_row, null);
        TextView singleWindSpeedUnit = singleUnitViewInSpinner.findViewById(R.id.singleUnit);
        singleWindSpeedUnit.setText(windSpeedUnits[position]);
        return singleUnitViewInSpinner;
    }
}