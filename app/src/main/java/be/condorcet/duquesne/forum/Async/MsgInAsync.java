package be.condorcet.duquesne.forum.Async;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import be.condorcet.duquesne.forum.SubjectActivity;

public class MsgInAsync extends AsyncTask<String, Void, String>
{

    private SubjectActivity subjectActivity;

    public MsgInAsync(SubjectActivity subjectActivity)
    {
        this.subjectActivity=subjectActivity;
    }


    @Override
    protected void onPreExecute() {
        // Prétraitement de l'appel
    }



    @Override
    protected String doInBackground(String... strings)
    {
        String result;
        String ids = strings[0];
        String url_base = "https://deborahprojet.000webhostapp.com/MsgBySubject.php";
        String param    =  "&ids=" + ids ;
        String url_tot = url_base + "?" + param;
        try
        {
            URL url=new URL(url_tot);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200)
            {
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                result = readStream(inputStream);
            }
            else
            {
                result = "-404 ";
            }
            connection.disconnect();


        }
        catch (IOException e)
        {
            result = "-500";
            e.printStackTrace();
        }

        return result ;
    }

    private String readStream(InputStream is) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is),1000);
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String result)
    { // Callback
        this.subjectActivity.populate(result);
    }

}
