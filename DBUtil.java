package com.example.david.wedog.andsql;

/**
 * Created by david on 2017/3/10.
 */

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DBUtil {
    private ArrayList<String> arrayList1 = new ArrayList<String>();
    private ArrayList<String> arrayList2 = new ArrayList<String>();
    private ArrayList<String> arrayList3 = new ArrayList<String>();
    private HttpConnSoap Soap = new HttpConnSoap();

    public DBUtil() {
    }


    public List<HashMap<String, String>> getAllInfo(final Handler myhandler) {
        HashMap<String,String> tempHash = new HashMap<String,String>();
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        tempHash.put("time","time");
        tempHash.put("food","food");
        tempHash.put("no","no");
        list.clear();
        arrayList1.clear();
        arrayList2.clear();
        arrayList3.clear();
        list.add(tempHash);

        new Thread() {
            public void run() {
                arrayList1 = Soap.GetWebServer("selectAllUserInfor", arrayList1, arrayList2);
                Message msg = new Message();
                msg.what = 0x123;
                msg.obj = arrayList1;
                myhandler.sendMessage(msg);
            }
        } .start();


        return list;
    }

    public List<HashMap<String, String>> getAllHealthInfo(final Handler myhandler) {
        HashMap<String, String> tempHash = new HashMap<String, String>();
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        tempHash.put("time", "time");
        tempHash.put("weight", "weight");
        tempHash.put("no", "no");
        list.clear();
        arrayList1.clear();
        arrayList2.clear();
        arrayList3.clear();
        list.add(tempHash);

        new Thread() {
            public void run() {
                arrayList1 = Soap.GetWebServer("selectAllHealthInfor", arrayList1, arrayList2);

                Message msg = new Message();
                msg.what = 0x123;
                msg.obj = arrayList1;
                myhandler.sendMessage(msg);
            }
        }.start();
        return list;
    }

    public void insertUserInfo(String no, String time,String food) {
        arrayList1.clear();
        arrayList2.clear();

        arrayList1.add("no");
        arrayList1.add("time");
        arrayList1.add("food");
        arrayList2.add(no);
        arrayList2.add(time);
        arrayList2.add(food);
        new Thread()
        {
            public void run()
            {
                try
                {
                    Soap.GetWebServer("insertUserInfo", arrayList1, arrayList2);
                }
                catch(Exception e)
                {

                }
            }
        }.start();

    }

    public void deleteUserInfo(String no) {
        arrayList1.clear();
        arrayList2.clear();
        arrayList1.add("no");
        arrayList2.add(no);
        new Thread()
        {
            public void run()
            {
                try
                {
                    Soap.GetWebServer("deleteUserInfo", arrayList1,arrayList2);
                }
                catch(Exception e)
                {

                }
            }
        }.start();
    }

    public void updateUserMode(String no) {
        arrayList1.clear();
        arrayList2.clear();
        arrayList1.add("no");
        arrayList2.add(no);
        new Thread()
        {
            public void run()
            {
                try
                {
                    Soap.GetWebServer("updateUserMode", arrayList1,arrayList2);
                }
                catch(Exception e)
                {

                }
            }
        }.start();
    }
}
