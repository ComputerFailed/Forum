package be.condorcet.duquesne.forum.Async;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import be.condorcet.duquesne.forum.MenuActivity;

public class SubjectListAsync extends AsyncTask<String, Void, String>
{
    private MenuActivity activity;

    public SubjectListAsync(MenuActivity activity)
    {
        this.activity=activity;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings)
    {
        String result;
        String url_base = "https://deborahprojet.000webhostapp.com/SubjectAll.php";


        try
        {
            URL url = new URL(url_base);
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
                result = "-404";
            }
            connection.disconnect();


        }
        catch (IOException e)
        {
            result = "-500";
            e.printStackTrace();
        }

        return result;
    }

    private String readStream(InputStream is) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 1000);
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String retour)
    {

        this.activity.populate(retour);
    }
}
