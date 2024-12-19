package com.example.testprep_dovada;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testprep_dovada.data.Dovada;
import com.example.testprep_dovada.database.DovadaService;
import com.example.testprep_dovada.network.AsyncTaskRunner;
import com.example.testprep_dovada.network.Callback;
import com.example.testprep_dovada.network.HttpManager;
import com.example.testprep_dovada.network.JSONParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFERENCES = "shared_preferences";
    public static final String SPINNER_KEY = "spinner_key";
    public static final String ET_KEY = "et_key";
    private final String url = "https://api.npoint.io/efcea1d46c36fa555d04";
    private FloatingActionButton fab;
    private TextView tv;
    private Spinner spinner;
    private EditText et;
    private Button bttn;
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private List<Dovada> dovezi = new ArrayList<>();
    private DovadaService dovadaService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();

        fab.setOnClickListener(click -> {
            asyncTaskRunner.executeAsync(new HttpManager(url), onMainThreadOperation());
        });
        
        bttn.setOnClickListener(click -> {
            if (et.getText() == null || et.getText().toString().isBlank()) {
                Toast.makeText(getApplicationContext(), R.string.empty_period_field, Toast.LENGTH_SHORT).show();
            }

            String comp = spinner.getSelectedItem().toString();
            int value = Integer.parseInt(et.getText().toString());

            List<Dovada> list;
            if (comp.equals("are valoarea mai mare decat")) {
                list = dovezi.stream().filter(d -> d.getPeriod() > value).collect(Collectors.toList());
            } else if (comp.equals("are valoarea mai mica decat")) {
                list = dovezi.stream().filter(d -> d.getPeriod() < value).collect(Collectors.toList());
            } else {
                list = dovezi.stream().filter(d -> d.getPeriod() == value).collect(Collectors.toList());
            }

            dovadaService.delete(list, callbackDeleted(comp, value));
        });
    }

    private Callback<List<Dovada>> callbackDeleted(String comp, int value) {
        return result -> {
            if (result != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SPINNER_KEY, comp);
                editor.putInt(ET_KEY, value);
                editor.apply();
                dovezi.removeAll(result);
            }
        };
    }

    private Callback<String> onMainThreadOperation() {
        return result -> {
            if (result != null) {
                List<Dovada> list = JSONParser.getFromJson(result);
                List<Dovada> listToInsert = list.stream().filter(d -> !dovezi.contains(d)).collect(Collectors.toList());

                if (!listToInsert.isEmpty()) {
                    dovadaService.insertAll(listToInsert, callbackInsertAll());
                }
            }
        };
    }

    private Callback<List<Dovada>> callbackInsertAll() {
        return result -> {
            if (result != null) {
                Toast.makeText(getApplicationContext(), R.string.inserted, Toast.LENGTH_SHORT).show();
                dovezi.addAll(result);
            }
        };
    }

    private void initComponents() {
        fab = findViewById(R.id.surugiu_george_alexandru_fab);
        tv = findViewById(R.id.surugiu_george_alexandru_tv);
        spinner = findViewById(R.id.surugiu_george_alexandru_spinner);
        et = findViewById(R.id.surugiu_george_alexandru_et);
        bttn = findViewById(R.id.surugiu_george_alexandru_bttn);
        
        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        dovadaService = new DovadaService(getApplicationContext());
        
        String comp = sharedPreferences.getString(SPINNER_KEY, "");
        int value = sharedPreferences.getInt(ET_KEY, 0);
        
        if (comp.equals("are valoarea mai mare decat")) {
            spinner.setSelection(0);
        } else if (comp.equals("are valoarea mai mica decat")) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(2);
        }

        if (value != 0) {
            et.setText(String.valueOf(value));
        }
        
        dovadaService.getAll(callbackGetAll());
    }

    private Callback<List<Dovada>> callbackGetAll() {
        return result -> {
            if (result != null) {
                dovezi.clear();
                dovezi.addAll(result);
                Toast.makeText(getApplicationContext(), R.string.loaded, Toast.LENGTH_SHORT).show();
            }
        };
    }
}