package com.example.pc_wanidol.secondsessionpoi.Tools;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;

import com.example.pc_wanidol.secondsessionpoi.R;
import com.example.pc_wanidol.secondsessionpoi.Models.PlaceInterest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by couchot on 05/01/17.
 */

public class NeighborAsycncTask extends AsyncTask {
    Context mContext;
    Location lo = null;

    public NeighborAsycncTask(Context mContext, Location lo) {
        super();
        this.mContext = mContext;
        this.lo = lo;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        // all the work should be put there
        int st = Integer.MAX_VALUE ;
        int idx = -1;
        for (int i=0; i< params.length; i++) {
            PlaceInterest c = (PlaceInterest) params[i];
            Location ld = new Location("");
            ld.setLatitude(c.getLatitude());
            ld.setLongitude(c.getLongitude());
            int t = getPathLength(ld);
            if (t < st){
                st = t;
                idx = i;
            }
        }

        Intent i = new Intent(mContext.getResources().getString(R.string.key_neighbor_intent));
        i.putExtra(mContext.getResources().getString(R.string.key_neighbor_path),params[idx].toString() + " : " + st +" m");
        mContext.sendBroadcast(i);
        return null;
    }

    private int getPathLength(Location ld) {
        int r=-1;
        try {
            String urls = "https://maps.googleapis.com/maps/api/directions/json?"
                    + "&mode=walking"
/*                    + "&origin=47.6421675,6.8365264"
                    + "&destination=47.643393,6.845571"
*/
                    + "&origin=" + lo.getLatitude() + "," + lo.getLongitude()
                    + "&destination=" + ld.getLatitude() + "," + ld.getLongitude()
                    + "&key= AIzaSyDr9mrOmk2ObslA4Z0ntoG9kfO_DdofJiE";
            URL url = new URL(urls);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.flush();
            String str=  convertStreamToString(conn.getInputStream());
            r = interprete_json_file(str);
        } catch (Exception e) {
        }
        return r;
    }

    private String convertStreamToString(InputStream in){
        String reponse =null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            reponse= new String(response, "UTF-8");
        } catch (Exception e){
        }
        return  reponse;
    }

    private int interprete_json_file(String jsontext){
        int dist=-1;
        try {
            JSONObject racine = new JSONObject(jsontext);
            JSONArray routes = racine.getJSONArray("routes");
            JSONObject ob1 = routes.getJSONObject(0);
            JSONArray legs = ob1.getJSONArray("legs");
            JSONObject ob2 = legs.getJSONObject(0);
            JSONObject distance = ob2.getJSONObject("distance");
            dist = distance.getInt("value");

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return dist;
    }


}

        /*
                           + "&origin=" + lo.getLatitude() + "," + lo.getLongitude()
                    + "&destination=" + ld.getLatitude() + "," + ld.getLongitude()
     */

