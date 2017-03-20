package com.example.david.wedog.andsql.Conmain;

/**
 * Created by david on 2017/3/10.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.wedog.andsql.Conmain.SocketTransceiver;
import com.example.david.wedog.andsql.Conmain.TcpClient;

import com.example.david.wedog.andsql.AuMainActivity;
import com.example.david.wedog.andsql.R;

public class ConActivity extends Activity implements OnClickListener {

    private Button bnConnect;
    private TextView txReceive;
    private EditText edIP, edPort, edData;

    private Handler handler = new Handler(Looper.getMainLooper());



    private TcpClient client = new TcpClient() {

        @Override
        public void onConnect(SocketTransceiver transceiver) {
            refreshUI(true);
        }

        @Override
        public void onDisconnect(SocketTransceiver transceiver) {
            refreshUI(false);
        }

        @Override
        public void onConnectFailed() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ConActivity.this, "connect fail",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onReceive(SocketTransceiver transceiver, final String s) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    txReceive.append(s);
                }
            });
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manu);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        this.findViewById(R.id.bn_mot).setOnClickListener(this);
        this.findViewById(R.id.bn_wei).setOnClickListener(this);
        this.findViewById(R.id.btn_2).setOnClickListener(this);
        this.findViewById(R.id.btn_3).setOnClickListener(this);
        bnConnect = (Button) this.findViewById(R.id.bn_connect);
        bnConnect.setOnClickListener(this);

        edIP = (EditText) this.findViewById(R.id.ed_ip);
        edPort = (EditText) this.findViewById(R.id.ed_port);
        txReceive = (TextView) this.findViewById(R.id.tx_receive);
        txReceive.setOnClickListener(this);

        refreshUI(false);
    }

    @Override
    public void onStop() {
        client.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_connect:
                connect();
                break;
            case R.id.bn_mot:
                sendStr1();
                break;
            case R.id.bn_wei:
                sendStr2();
                break;
            case R.id.btn_2:
                send2();
                break;
            case R.id.btn_3:
                send3();
                break;

		case R.id.tx_receive:
			clear();
			break;
        }
    }


    private void refreshUI(final boolean isConnected) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                edPort.setEnabled(!isConnected);
                edIP.setEnabled(!isConnected);
                bnConnect.setText(isConnected ? "cut" : "connect");
                if (bnConnect.equals("cut")){
                    String data="cut";
                    client.getTransceiver().send(data);
                }
            }
        });
    }

    private void connect() {
        if (client.isConnected()) {

            client.disconnect();
        } else {
            try {
                String hostIP = edIP.getText().toString();
                int port = Integer.parseInt(edPort.getText().toString());
                client.connect(hostIP, port);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "wrong port", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


    private void sendStr1() {

        if(client.isConnected()){
            String data="on";
            client.getTransceiver().send(data);
        }
        else {}
    }

    private void sendStr2() {

        if(client.isConnected()){
            String data="off";
            client.getTransceiver().send(data);
        }
        else {}
    }

    private void send2() {

        if(client.isConnected()){
            String data="two";
            client.getTransceiver().send(data);
        }
        else {}
    }private void send3() {
        if(client.isConnected()){
            String data="three";
            client.getTransceiver().send(data);
        }
        else {}
    }
    private void clear() {
        new AlertDialog.Builder(this).setTitle("sure to clear?")
                .setNegativeButton("cancel", null)
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txReceive.setText("");
                    }
                }).show();
    }
}
