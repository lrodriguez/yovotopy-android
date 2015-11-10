package py.com.purpleapps.yovotopy.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import py.com.purpleapps.yovotopy.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    public static final long MUNICIPALES_DATE = 1447581600000L;
    public static final long DAY_IN_MILLISECONDS = 86400000L;
    public static final long HOUR_IN_MILLISECONDS = 3600000L;
    public static final long MINUTE_IN_MILLISECONDS = 60000L;
    public static final long SECOND_IN_MILLISECONDS = 1000L;
    @Bind(R.id.main_content)
    LinearLayout mainContent;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.countdown_text)
    TextView countdownText;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static String calculateRemainingTime(Long millisUntilFinished) {
        long dias = TimeUnit.DAYS.convert(millisUntilFinished, TimeUnit.DAYS.MILLISECONDS);

        millisUntilFinished = millisUntilFinished - (dias * DAY_IN_MILLISECONDS);

        long horas = TimeUnit.HOURS.convert(millisUntilFinished, TimeUnit.DAYS.MILLISECONDS);

        millisUntilFinished = millisUntilFinished - (horas * HOUR_IN_MILLISECONDS);

        long minutos = TimeUnit.MINUTES.convert(millisUntilFinished, TimeUnit.DAYS.MILLISECONDS);

        millisUntilFinished = millisUntilFinished - (minutos * MINUTE_IN_MILLISECONDS);

        long segundos = millisUntilFinished / SECOND_IN_MILLISECONDS;

        return String.format("%d d, %d hs, %d min, %d seg", dias, horas, minutos, segundos);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mListener.showFab(true, 1);
        } else {
            if (getUserVisibleHint()) {
                mListener.showFab(false, 1);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long now = new Date().getTime();
        long timeLeft = MUNICIPALES_DATE - now;

        new CountDownTimer(timeLeft, 1000) {

            public void onTick(long millisUntilFinished) {
                String timeText = calculateRemainingTime(millisUntilFinished);

                countdownText.setText(timeText);
            }

            public void onFinish() {
                countdownText.setText("Llegó el día!");
            }
        }.start();

        refreshLayout.setEnabled(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void showFab(boolean show, int iconId);
    }

}
