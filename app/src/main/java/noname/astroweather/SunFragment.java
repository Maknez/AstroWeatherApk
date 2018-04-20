package noname.astroweather;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SunFragment extends Fragment {

    TextView mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_sun, container, false);

        mView = (TextView) rootView.findViewById(R.id.sloneczko);

        mView.setText("Nowa randomowa wartość");


        return rootView;
    }
}
