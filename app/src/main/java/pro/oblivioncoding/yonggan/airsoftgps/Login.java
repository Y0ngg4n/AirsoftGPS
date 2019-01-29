package pro.oblivioncoding.yonggan.airsoftgps;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLData;

import netty.client.NettyClient;
import netty.client.NetworkHandler;

public class Login extends AppCompatActivity {

    public static final String HOST = "192.168.56.1";
    public static final int PORT = 12345;

    private static TextView errorMessage;

    private static Context ctx;

    private static Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        errorMessage = findViewById(R.id.errorLogin);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            //Connect to Server
            loginButton.setEnabled(false);
                AsyncTask.execute(() -> {
                    MainActivity.nettyClient = new NettyClient(
                            ((TextView) findViewById(R.id.username)).getText().toString(),
                            ((TextView) findViewById(R.id.password)).getText().toString(), HOST, PORT);

                });

        });

        ctx.getApplicationContext();
    }


    public static void loginFailed(){
        if(errorMessage != null){
            errorMessage.setText("Login failed. Please try another username or password!");
        }
        loginButton.setEnabled(true);
    }

    public static void loginSuccess(){
        if(ctx != null){
            ctx.startActivity(new Intent(ctx, MainActivity.class));
        }
    }


}
