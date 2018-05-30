package com.example.pc_wanidol.secondsessionpoi.Views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc_wanidol.secondsessionpoi.R ;
import com.example.pc_wanidol.secondsessionpoi.Models.PlaceInterest;
import com.example.pc_wanidol.secondsessionpoi.Tools.PlaceInterestDatabaseHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaceInterestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaceInterestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceInterestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARA_ID = "param_id";

    private OnFragmentInteractionListener mListener;
    View root = null;
    int id = 0;

    TextView textViewPOI = null;
    TextView textViewLatitude= null;
    TextView textViewLongitude= null;




    public PlaceInterestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaceInterestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceInterestFragment newInstance(int id) {
        PlaceInterestFragment fragment = new PlaceInterestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARA_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARA_ID);
        }
    }


    public void updateUI(){

        PlaceInterestDatabaseHandler dbh = new PlaceInterestDatabaseHandler(getContext());
        PlaceInterest c = dbh.getPlaceInterest(id);



        textViewPOI = (TextView)root.findViewById(R.id.text_view_poi_name);

        textViewPOI.setText(c.getPlacename());

        textViewLatitude = (TextView)root.findViewById(R.id.text_view_poi_latitide);
        textViewLatitude.setText(String.valueOf(c.getLatitude()));

        textViewLongitude = (TextView)root.findViewById(R.id.text_view_poi_longitude);
        textViewLongitude.setText(String.valueOf(c.getLongitude()));

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place_interest, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
