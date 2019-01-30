package pro.oblivioncoding.yonggan.airsoftgps;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import netty.client.NettyClient;

public class LoginActivity extends AppCompatActivity {

    private static final String HOST = "oblivioncoding.pro";
    private static final int PORT = 12345;

    public static Consumer<Boolean> loginConsumer;
    final private LoginActivity instance = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(v -> {
            //Connect to Server
            loginConsumer = aBoolean -> {
                if (aBoolean)
                    instance.runOnUiThread(() -> startActivity(new Intent(instance, MainActivity.class)));
                else {
                    Log.i("Consumer", "Consumer accepted");
                    instance.runOnUiThread(() -> ((TextView) findViewById(R.id.errorLogin)).setText("Username or Password is wrong!"));
                }
            };

            AsyncTask.execute(() -> MainActivity.nettyClient = new NettyClient(((TextView) findViewById(R.id.username)).getText().toString(), ((TextView) findViewById(R.id.password)).getText().toString(), HOST, PORT));
            instance.runOnUiThread(() -> ((TextView) findViewById(R.id.errorLogin)).setText("Trying to connect..."));
        });
    }
}
