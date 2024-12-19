package com.example.testprep_dovada.database;

import android.content.Context;
import android.telecom.Call;

import com.example.testprep_dovada.data.Dovada;
import com.example.testprep_dovada.network.AsyncTaskRunner;
import com.example.testprep_dovada.network.Callback;

import java.util.List;
import java.util.concurrent.Callable;

public class DovadaService {
    private DovadaDao dovadaDao;
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();

    public DovadaService(Context context) {
        dovadaDao = DatabaseManager.getInstance(context).getDovadaDao();
    }

    public void getAll(Callback<List<Dovada>> callback) {
        Callable<List<Dovada>> callable = () -> dovadaDao.getAll();
        asyncTaskRunner.executeAsync(callable, callback);
    }

    public void insertAll(List<Dovada> dovezi, Callback<List<Dovada>> callback) {
        Callable<List<Dovada>> callable = () -> {
            List<Long> ids = dovadaDao.insertAll(dovezi);
            for (int i = 0; i < dovezi.size(); i++) {
                dovezi.get(i).setId(ids.get(i));
            }
            return dovezi;
        };
        asyncTaskRunner.executeAsync(callable, callback);
    }

    public void delete(List<Dovada> dovezi, Callback<List<Dovada>> callback) {
        Callable<List<Dovada>> callable = () -> {
          int count = dovadaDao.delete(dovezi);
          if (count <= 0) {
              return null;
          }
          return dovezi;
        };
        asyncTaskRunner.executeAsync(callable, callback);
    }
}
