package pro.oblivioncoding.yonggan.airsoftgps;

import android.content.Intent;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import netty.client.NettyClient;
import netty.client.NetworkHandler;

public class LoginActivity extends AppCompatActivity {

    public static final String HOST = "bierbrauer-beerzone.de";
    public static final int PORT = 12345;

    public static String username;
    public static String password;


    public static Consumer<Boolean> loginConsumer;
    final private LoginActivity instance = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(NetworkHandler.loggedIN){
            instance.runOnUiThread(() -> startActivity(new Intent(instance, MainActivity.class)));
        }else {
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

                username = ((TextView) findViewById(R.id.username)).getText().toString();
                password = ((TextView) findViewById(R.id.password)).getText().toString();
                MainActivity.connectToServer(username, password, HOST, PORT);
                instance.runOnUiThread(() -> ((TextView) findViewById(R.id.errorLogin)).setText("Trying to connect..."));
            });
        }
    }
}
