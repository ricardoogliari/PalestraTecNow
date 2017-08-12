package setor.minhaempresa.com.tecnowsensor;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ricardoogliari on 8/10/17.
 */

public class Core extends Application {

    public static InterfaceRetrofit service;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.5.49.8:8052/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(InterfaceRetrofit.class);
    }
}
