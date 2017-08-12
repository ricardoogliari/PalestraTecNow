package setor.minhaempresa.com.tecnowsensor;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ricardoogliari on 8/10/17.
 */

public interface InterfaceRetrofit {

    @GET("ligar")
    Call<Object> liga();

    @GET("desligar")
    Call<Object> desliga();

}
