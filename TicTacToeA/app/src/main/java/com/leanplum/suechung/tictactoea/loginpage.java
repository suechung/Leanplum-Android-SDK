package com.leanplum.suechung.tictactoea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leanplum.Leanplum;
import com.leanplum.LeanplumActivityHelper;
import com.leanplum.LeanplumPushService;
import com.leanplum.Var;
import com.leanplum.annotations.Parser;
import com.leanplum.annotations.Variable;
import com.leanplum.callbacks.VariablesChangedCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class loginpage extends AppCompatActivity {

    public static Var<String> kitty = Var.defineAsset("kitty", "kitty.jpg");
    // Example of a Variable defined inside the onCreate method


    @Variable public static String gametitle = "TicTacToe";

    private Button loginbutton;


    public static EditText userText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);


        loginbutton = (Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbutton(v);
            }
        });


        setContentView(R.layout.activity_loginpage);


        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout);

        // LP Variable Image/File
        ImageView im = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200,100);
        im.setLayoutParams(layoutParams);

        //im.setImageResource(R.drawable.kitty);
        im.setImageBitmap(BitmapFactory.decodeStream(kitty.stream()));

        // LP Variable String
        final TextView gameTitle = (TextView) findViewById(R.id.gameTitle);

        gameTitle.setText(gametitle);

        layout.addView(im);

        Leanplum.addVariablesChangedAndNoDownloadsPendingHandler(new VariablesChangedCallback() {
            @Override
            public void variablesChanged() {
                Log.i("#### ", gametitle);

            }
        });

    }

    public void loginbutton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        userText = (EditText) findViewById(R.id.userText);
        Leanplum.setUserId(userText.getText().toString());
        Leanplum.track("signup");
        Log.i("####", "Logging in with Username: " + userText.getText().toString());
    }

}
