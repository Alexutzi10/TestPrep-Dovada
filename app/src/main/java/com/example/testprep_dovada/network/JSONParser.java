package com.example.testprep_dovada.network;

import android.util.Log;

import com.example.testprep_dovada.data.DateConverter;
import com.example.testprep_dovada.data.Dovada;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JSONParser {

    public static List<Dovada> getFromJson(String json) {
        List<Dovada> dovezi = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            JSONObject details = root.getJSONObject("details");
            JSONArray datasets = details.getJSONArray("datasets");

            for (int i = 0; i < datasets.length(); i++) {
                JSONObject object = datasets.getJSONObject(i);
                JSONObject dovada = object.getJSONObject("dovada");

                String stringDate = dovada.getString("startDate");
                String clientName = dovada.getString("clientName");
                int loadPeriod = dovada.getInt("loanPeriod");

                Date date;
                try {
                    date = DateConverter.toDate(stringDate);
                } catch (Exception ex) {
                    Log.e("JSONParser", "Error when parsing the date");
                    continue;
                }

                Dovada proof = new Dovada(date, clientName, loadPeriod);
                dovezi.add(proof);
            }
            return dovezi;
        } catch (JSONException ex) {
            Log.e("JSONParser", "Error when parsing the json");
        }
        return new ArrayList<>();
    }
}
