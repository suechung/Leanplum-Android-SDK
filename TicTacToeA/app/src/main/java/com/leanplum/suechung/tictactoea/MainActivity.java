package com.leanplum.suechung.tictactoea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.leanplum.Leanplum;
import java.util.HashMap;
import java.util.Map;
import java.lang.String;
import java.util.Calendar;
import android.util.Log;

import java.lang.Object;
import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;
import java.net.URISyntaxException;
import java.net.URI;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    static Map<String, Object> params = new HashMap<String, Object>();

    private Button[][] buttons = new Button[3][3];

    private boolean crossturn = true;

    private int numofplays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tracking Leanplum state
        Leanplum.advanceTo("entergame");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID =  getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.reset_button);
        buttonReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        buttons[i][j].setText("");
                    }
                }

                numofplays = 0;
                crossturn = true;
                Log.i("Leanplum", loginpage.userText.getText().toString());
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (crossturn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }

        numofplays++;

        if (checkForWin()) {
            if (crossturn) {
                crosswins();
                Leanplum.track("gamefinish");
            } else {
                Leanplum.track("gamefinish");
                noughtwins();
            }
        } else if (numofplays == 9) {
            Leanplum.track("gamefinish");
            tie();
        } else {
            crossturn = !crossturn;
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        return false;

    }

    private void crosswins() {
        Toast.makeText(this, "Cross won!", Toast.LENGTH_SHORT).show();
    }

    private void noughtwins() {
        Toast.makeText(this, "Nought won!", Toast.LENGTH_SHORT).show();
    }

    private void tie() {
        Toast.makeText(this, "It's a tie!", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("numofplays", numofplays);
        outState.putBoolean("crossturn", crossturn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        numofplays = savedInstanceState.getInt("numofplays");
        crossturn = savedInstanceState.getBoolean("crossturn");

    }


    public void inboxbutton(View view) {
        Intent inboxbutton = new Intent(this, appinbox.class);
        startActivity(inboxbutton);
    }

}
