package com.example.pc_wanidol.secondsessionpoi.Views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pc_wanidol.secondsessionpoi.R;
import com.example.pc_wanidol.secondsessionpoi.Validation.Check_Valid;
import com.example.pc_wanidol.secondsessionpoi.Tools.PlaceInterestDatabaseHandler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPoiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyPoiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPoiFragment extends Fragment   {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText etxtPOI,etxtLATITUDE,etxtLONGITUDE;
    Button btn;
    View view;



    private PlaceInterestDatabaseHandler db = new PlaceInterestDatabaseHandler(getActivity());

    boolean result;

    public MyPoiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPoiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPoiFragment newInstance(String param1, String param2) {
        MyPoiFragment fragment = new MyPoiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        poiHandler db = new PlaceInterestDatabaseHandler(getActivity());

        view =  inflater.inflate(R.layout.fragment_my_poi, container, false);
        etxtPOI = (EditText)view.findViewById(R.id.edittxt_placename);
        etxtLATITUDE = (EditText)view.findViewById(R.id.edittext_Latitude);
        etxtLONGITUDE = (EditText)view.findViewById(R.id.edittext_Longitude);
        btn = (Button)view.findViewById(R.id.btn_Add);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkValidation()){

                    hideKeyboard();
                    AddPlaceInterest(view);



                    MyMapFragment fragment = new MyMapFragment();
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.content_main,fragment).commit();

                }


            }
        });


        return view;

    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus()
                        .getWindowToken(), 0);
    } // hideKeyboard


    private boolean checkValidation() {
        boolean ret = true;


        if (!Check_Valid.hasText(etxtPOI)) {
            ret = false;
        }else{
            if ((Check_Valid.hasText(etxtLATITUDE))&&(Check_Valid.hasText(etxtLONGITUDE))) {

                double lat = Float.parseFloat(etxtLATITUDE.getText().toString());
                double lng = Float.parseFloat(etxtLONGITUDE.getText().toString());

                if (!Check_Valid.isValidLatLng(lat,lng,etxtLATITUDE,etxtLONGITUDE)){
                    ret = false;
                }

            }else{
                ret = false;
            }

        }

        return ret;
    }

    public void AddPlaceInterest(View view){
        String PlaceName = etxtPOI.getText().toString();
        Float latitude = Float.parseFloat(etxtLATITUDE.getText().toString());
        Float longitude = Float.parseFloat(etxtLONGITUDE.getText().toString());


        PlaceInterestDatabaseHandler db = new PlaceInterestDatabaseHandler(getActivity());
        boolean result = db.addData(PlaceName,latitude,longitude);

        if (result){
            Toast.makeText(getActivity(),"Success!",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getActivity(),"Failed!",Toast.LENGTH_SHORT).show();
        }

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
