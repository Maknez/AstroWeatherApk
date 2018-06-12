package noname.astroweather.weather.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import noname.astroweather.R;

public class LocalizationAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public LocalizationAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Object object) {
        list.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        LocalizationRow localizationRow = new LocalizationRow();

        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.display_localization_row, parent, false);
            localizationRow.cityNameRow = row.findViewById(R.id.cityNameRow);
            localizationRow.countryNameRow = row.findViewById(R.id.countryNameRow);
            row.setTag(localizationRow);
        } else {
            localizationRow = (LocalizationRow) row.getTag();
        }
        Localization localization = (Localization) getItem(position);
        localizationRow.cityNameRow.setText(localization.getCity());
        localizationRow.countryNameRow.setText(localization.getCountry());

        return row;
    }
}
