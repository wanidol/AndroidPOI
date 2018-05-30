package com.example.pc_wanidol.secondsessionpoi.Validation;

import android.widget.EditText;

/**
 * Created by PC-Wanidol on 25/08/2017.
 */

public class Check_Valid {

    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String INVALID_LAT = "Invalid Latitude";
    private static final String INVALID_LNG = "Invalid Longitude";





    public static boolean isValidLatLng(double lat, double lng,EditText etxtLat,EditText etxtLng)
    {
        String txtLat = etxtLat.getText().toString().trim();
        String txtLng = etxtLng.getText().toString().trim();

        etxtLat.setError(null);
        etxtLng.setError(null);

        if(lat < -90 || lat > 90)
        {
            etxtLat.setError(INVALID_LAT);
            return false;
        }
        else if(lng < -180 || lng > 180)
        {
            etxtLng.setError(INVALID_LNG);
            return false;
        }
        return true;
    }

    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }
}
