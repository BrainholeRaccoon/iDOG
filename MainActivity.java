package com.example.david.wedog.andsql;

/**
 * Created by david on 2017/3/10.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.david.wedog.andsql.AuMainActivity;
import com.example.david.wedog.andsql.Conmain.ConActivity;
import com.example.david.wedog.andsql.Conmain.SocketTransceiver;
import com.example.david.wedog.andsql.Conmain.TcpClient;


public class MainActivity  extends AppCompatActivity  {

    public final static String EXTRA_MESSAGE = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void logIn(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        EditText editText1 = (EditText) findViewById(R.id.edit_username);
        EditText editText2 = (EditText) findViewById(R.id.edit_password);
        String user = "zsf";
        String pass = "123";
        String username = editText1.getText().toString();
        String password = editText2.getText().toString();
        String message ;
        if (username.equals(user)&& password.equals(pass) ) {
            setContentView(R.layout.activity_manu);
        }
        else {
            message = "wrong username or password";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    public void clearMessage(View view){
        EditText editText1 = (EditText) findViewById(R.id.edit_username);
        EditText editText2 = (EditText) findViewById(R.id.edit_password);
        editText1.setText("" );
        editText2.setText("" );
    }

    public void control(View v){
        Intent intent = new Intent(this, ConActivity.class);
        startActivity(intent);
    }
    public void userset(View v){
        Intent intent1 = new Intent(this, AuMainActivity.class);
        startActivity(intent1);
    }

}

