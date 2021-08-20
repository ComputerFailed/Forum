package be.condorcet.duquesne.forum.Async;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import be.condorcet.duquesne.forum.MainActivity;

public class MainAsync  extends AsyncTask<String, Void, Integer>
{

    private MainActivity main_activity;

    public MainAsync(MainActivity main_activity)
    {
        this.main_activity=main_activity;
    }

    @Override
    protected void onPreExecute() {
        // Prétraitement de l'appel
    }



    @Override
    protected Integer doInBackground(String... strings) {
        Integer retour;
        String pseudo = strings[0];
        String psw = strings[1];
        String url_base = "https://deborahprojet.000webhostapp.com/c.php";

        String param    = "pseudo=" + pseudo + "&psw=" + psw;
        String url_tot = url_base + "?" + param;
        try {
            URL url=new URL(url_tot);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if(responseCode == 200){
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                Scanner scanner = new Scanner(inputStreamReader);
                String retour_scanner = scanner.next();
                retour = Integer.parseInt(retour_scanner);
            }
            else
            {
                retour = -404 ;
            }
            connection.disconnect();


        }
        catch (IOException e)
        {
            retour = -500 ;
            e.printStackTrace();
        }

        return retour ;
    }

    @Override
    protected void onPostExecute(Integer retour)
    { // Callback
        this.main_activity.populate(retour);
    }
}
