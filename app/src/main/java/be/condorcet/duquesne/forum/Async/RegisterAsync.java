package be.condorcet.duquesne.forum.Async;



import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import be.condorcet.duquesne.forum.RegisterActivity;


public class RegisterAsync extends AsyncTask<String, Void, Integer>
{
    private RegisterActivity activity;

    public RegisterAsync(RegisterActivity activity)

    {
        this.activity=activity;
    }


    @Override
    protected void onPreExecute() {
        // Prétraitement de l'appel
    }



    @Override
    protected Integer doInBackground(String... strings)
    {
        //   $stm->execute(array($pseudo, $psw,$town, $gender));
        Integer retour;
        String pseudo = strings[0];
        String psw = strings[1];
        String town = strings[2];
        String gender = strings[3];
        String url_base ="https://deborahprojet.000webhostapp.com/Inscription.php" ;
                //"//I.php";
        String param="Pseudo=" + pseudo + "&Psw=" + psw  + "&Town=" + town + "&Gender=" + gender;//oki
        //https://deborahprojet.000webhostapp.com/I.php?Pseudo=luca&Psw=123456&Gender=h&Town=2
        //https://deborahprojet.000webhostapp.com/I.php?Pseudo=deborah&Psw=azerty&Gender=Womam&Town=4
        String url_tot= url_base + "?" + param;
        try
        {
            URL url=new URL(url_tot);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if(responseCode == 200)
            {
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                Scanner scanner = new Scanner(inputStreamReader);

               String retour_scanner = scanner.next();
               retour = Integer.parseInt(retour_scanner);//retour <br"
                //scanner.close();
            }
            else
            {
                // prob url qui va vers le serveur ne trouve pas l adresse url des rcp ou autre
                retour = -404 ;
            }
            connection.disconnect();
        }
        catch (IOException e)
        {
            //erreur du serveur multiple cause
            retour = -500 ;
            e.printStackTrace();
        }

        return retour ;
    }

    @Override
    protected void onPostExecute(Integer retour)
    { 

        this.activity.populate(retour);

    }

}
