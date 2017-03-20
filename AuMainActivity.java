package com.example.david.wedog.andsql;

/**
 * Created by david on 2017/3/10.
 */


import android.app.Activity;
import android.app.Dialog;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;


public class AuMainActivity extends Activity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private ListView listView;
    private SimpleAdapter adapter;
    private DBUtil dbUtil;
    List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();

    public AuMainActivity() {  }
    final Handler myhandler1=new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.what==0x123)
            {
                ArrayList<String> drrayList=(ArrayList<String>) msg.obj;
                for(int j=0;j<drrayList.size();j+=3)
                {
                    HashMap<String,String> hashMap=new HashMap<String,String>();
                    hashMap.put("time", drrayList.get(j));
                    hashMap.put("food", drrayList.get(j+1));
                    hashMap.put("no", drrayList.get(j+2));
                    list.add(hashMap);
                }
                adapter=new SimpleAdapter(
                        AuMainActivity.this,list,
                        R.layout.activity_item,
                        new String[]{"no","time","food"},
                        new int[]{R.id.txt_Uno,R.id.txt_Utime,R.id.txt_Uweight});
                listView.setAdapter(adapter);
            }
        }
    };
    final Handler myhandler2=new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.what==0x123)
            {
                ArrayList<String> drrayList2=(ArrayList<String>) msg.obj;
                for(int j=0;j<drrayList2.size();j+=3)
                {
                    HashMap<String,String> hashMap=new HashMap<String,String>();
                    hashMap.put("time", drrayList2.get(j));
                    hashMap.put("weight", drrayList2.get(j+1));
                    hashMap.put("no", drrayList2.get(j+2));
                    list.add(hashMap);
                }
                adapter=new SimpleAdapter(
                        AuMainActivity.this,list,
                        R.layout.activity_heal,
                        new String[]{"time","weight","no"},
                        new int[]{R.id.txt_Hour,R.id.txt_Minute,R.id.txt_Rate});
                listView.setAdapter(adapter);
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aumain);
        btn1 = (Button)this.findViewById(R.id.btn_all);
        btn2 = (Button)this.findViewById(R.id.btn_add);
        btn3 = (Button)this.findViewById(R.id.btn_delete);
        btn4 = (Button)this.findViewById(R.id.btn_mode);
        btn5 = (Button)this.findViewById(R.id.btn_heal);
        listView = (ListView)this.findViewById(R.id.listView);
        dbUtil = new DBUtil();
        ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
        btn1.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideButton(true);
                setListView();
            }
        });

        btn2.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideButton(true);
                setAddDialog();
            }
        });

        btn3.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideButton(true);
                setDeleteDialog();
            }
        });
        btn4.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideButton(true);
                setModeDialog();
            }
        });
        btn5.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideButton(true);
                setHealView();
            }
        });
    }


    private void setDeleteDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.setTitle("enter the No. of time");
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        final EditText cNoEditText = (EditText)dialog.findViewById(R.id.editText1);
        Button btnConfirm = (Button)dialog.findViewById(R.id.button1);
        Button btnCancel = (Button)dialog.findViewById(R.id.button2);
        btnConfirm.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AuMainActivity.this.dbUtil.deleteUserInfo(cNoEditText.getText().toString());
                dialog.dismiss();
                AuMainActivity.this.hideButton(false);
                Toast.makeText(AuMainActivity.this, "delete success",  Toast.LENGTH_SHORT).show();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                AuMainActivity.this.hideButton(false);
            }
        });
        dialog.show();
    }

    private void setModeDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_mode);
        dialog.setTitle("choose mode 1-3");
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        final EditText cNoEditText = (EditText)dialog.findViewById(R.id.editText1);
        Button btnConfirm = (Button)dialog.findViewById(R.id.button1);
        Button btnCancel = (Button)dialog.findViewById(R.id.button2);
        btnConfirm.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AuMainActivity.this.dbUtil.updateUserMode(cNoEditText.getText().toString());
                dialog.dismiss();
                AuMainActivity.this.hideButton(false);
                Toast.makeText(AuMainActivity.this, "update success",  Toast.LENGTH_SHORT).show();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                AuMainActivity.this.hideButton(false);
            }
        });
        dialog.show();
    }

    private void setAddDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add);
        dialog.setTitle("enter the info of time ");
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        final EditText cNameEditText = (EditText)dialog.findViewById(R.id.editText1);
        final EditText cNumEditText = (EditText)dialog.findViewById(R.id.editText2);
        final EditText cFoodEditText = (EditText)dialog.findViewById(R.id.editText3);
        Button btnConfirm = (Button)dialog.findViewById(R.id.button1);
        Button btnCancel = (Button)dialog.findViewById(R.id.button2);
        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUtil.insertUserInfo(cNameEditText.getText().toString(), cNumEditText.getText().toString(),cFoodEditText.getText().toString());
                dialog.dismiss();
                hideButton(false);
                Toast.makeText(AuMainActivity.this, "add success",  Toast.LENGTH_SHORT).show();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                AuMainActivity.this.hideButton(false);
            }
        });
        dialog.show();    }

    private void setListView() {
        listView.setVisibility(View.VISIBLE);
        list=dbUtil.getAllInfo(myhandler1);
        listView.setAdapter(adapter);
    }
    private void setHealView() {
        listView.setVisibility(View.VISIBLE);
        list=dbUtil.getAllHealthInfo(myhandler2);
        listView.setAdapter(adapter);
    }
    private void hideButton(boolean result)
    {
        if (result)
        {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
            btn5.setVisibility(View.GONE);
        }
        else
        {
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
            btn4.setVisibility(View.VISIBLE);
            btn5.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed()
    {
        if (listView.getVisibility() == View.VISIBLE)
        {
            listView.setVisibility(View.GONE);
            hideButton(false);
        }
        else
        {
            AuMainActivity.this.finish();
        }
    }
}