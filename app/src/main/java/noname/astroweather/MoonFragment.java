package noname.astroweather;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MoonFragment extends Fragment {

    TextView mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_moon, container, false);

        mView = (TextView) rootView.findViewById(R.id.ksiezyczek);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == 2) {
            mView.setBackgroundResource(R.drawable.moon_landscape);
        } else if (config.orientation == 1) {
            //pionowe
            mView.setBackgroundResource(R.drawable.moon_portrait);
        }

        mView.setText("Nowa randomowa wartość MOON");


        return rootView;
    }
}
