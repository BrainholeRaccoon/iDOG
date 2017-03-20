package com.example.david.wedog.andsql;

/**
 * Created by david on 2017/3/10.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpConnSoap
{
    public ArrayList<String> GetWebServer(String MethodName, ArrayList<String> Parameters, ArrayList<String> ParValues)
    {
        ArrayList<String> Values = new ArrayList<String>();
        String ServerUrl = "http://172.20.10.11:1144/Service1.asmx";
        String soapAction = "http://tempuri.org/" + MethodName;
        String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body />";
        String s1, s2, s3, s4 = "";
        s4 = "<" + MethodName + " xmlns=\"http://tempuri.org/\">";
        for (int i = 0; i < Parameters.size(); i++)
        {
            s1 = Parameters.get(i).toString();
            s2 = ParValues.get(i).toString();
            s3 = "<" + s1 + ">" + s2 + "</" + s1 + ">";
            s4 = s4 + s3;
        }
        s4 = s4 + "</" + MethodName + ">";
        String s5 = "</soap:Envelope>";
        String requestData = soap + s4 + s5;
        try
        {
            URL url = new URL(ServerUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            byte[] bytes = requestData.getBytes("utf-8");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(6000);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            con.setRequestProperty("SOAPAction", soapAction);
            con.setRequestProperty("Content-Length", "" + bytes.length);
            OutputStream outStream = con.getOutputStream();
            outStream.write(bytes);
            outStream.flush();
            outStream.close();
            InputStream inStream = con.getInputStream();
            Values = StreamtoValue(inStream, MethodName);
            return Values;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public ArrayList<String> StreamtoValue(InputStream in, String MethodName) throws IOException
    {
        StringBuffer out = new StringBuffer();
        String s1 = out.toString();
        byte[] b = new byte[4096];
        ArrayList<String> Values = new ArrayList<String>();
        Values.clear();
        for (int n; (n = in.read(b)) != -1;)
        {
            s1 = new String(b, 0, n);
            out.append(s1);
        }
        System.out.println(out);
        String[] s2 = s1.split("><");
        String s5 = MethodName + "Result";
        String s3 = "",s4 = "";
        Boolean getValueBoolean = false;
        for (int i = 0; i < s2.length; i++)
        {
            s3 = s2[i];
            System.out.println(s3);
            int FirstIndexPos, LastIndexPos, LengthofS5;
            FirstIndexPos = s3.indexOf(s5);
            LastIndexPos = s3.lastIndexOf(s5);

            if (FirstIndexPos >= 0)
            {
                if (getValueBoolean == false)
                {
                    getValueBoolean = true;
                }
                if ((FirstIndexPos >= 0) && (LastIndexPos > FirstIndexPos))
                {
                    LengthofS5 = s5.length() + 1;
                    s4 = s3.substring(FirstIndexPos + LengthofS5, LastIndexPos - 2);
                    Values.add(s4);
                    getValueBoolean = false;
                    return Values;
                }

            }
            if (s3.lastIndexOf("/" + s5) >= 0)
            {
                getValueBoolean = false;
                return Values;
            }
            if ((getValueBoolean) && (s3.lastIndexOf("/" + s5) < 0) && (FirstIndexPos < 0))
            {
                LastIndexPos = s3.length();
                s4 = s3.substring(7, LastIndexPos - 8);
                Values.add(s4);
            }
        }
        return Values;
    }
}