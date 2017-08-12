package setor.minhaempresa.com.tecnowsensor;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.FaceDetector;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private ConstraintLayout back;
    private SensorManager sensorManager;

    private static final String TAG = "TECNOW";
    private static boolean incomingFlag = false;
    private static String incoming_number = null;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){

                incomingFlag = false;
                String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Log.i(TAG, "call OUT:"+phoneNumber);

            }else{

                TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);

                switch (tm.getCallState()) {
                    //recebendo ligacao
                    case TelephonyManager.CALL_STATE_RINGING:
                        incomingFlag = true;
                        incoming_number = intent.getStringExtra("incoming_number");
                        Log.i(TAG, "RINGING :"+ incoming_number);
                        ligar();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: //recusa
                        if(incomingFlag){
                            Log.i(TAG, "ACCEPT :"+ incoming_number);
                        }

                        break;
                    case TelephonyManager.CALL_STATE_IDLE: //ligaçao encerra
                        if(incomingFlag){
                            Log.i(TAG, "IDLE");
                            desligar();
                        }

                        break;
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        back = (ConstraintLayout) findViewById(R.id.background);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        registra();

        IntentFilter filtro = new IntentFilter();
        filtro.addAction("android.intent.action.PHONE_STATE");

        registerReceiver(receiver, filtro);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void registra(){
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;

        if (values[0] > Math.abs(15) || values[0] > Math.abs(15) || values[0] > Math.abs(15)){
            back.setBackgroundColor(Color.GREEN);
            sensorManager.unregisterListener(this);
            ligar();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    back.post(new Runnable() {
                        @Override
                        public void run() {
                            back.setBackgroundColor(Color.RED);
                            desligar();
                        }
                    });

                    registra();
                }
            }).start();
        } else {
            back.setBackgroundColor(Color.RED);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void ligar(){
        Call<Object> call = Core.service.liga();
        makeCall(call);
    }

    public void desligar(){
        Call<Object> call = Core.service.desliga();
        makeCall(call);
    }

    public void makeCall(Call<Object> call){
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("TAG", "sucesso");
                Log.e("TAG", response.body().toString());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("TAG", "falha: " + t.getMessage());
            }

        });
    }
}
