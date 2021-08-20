package be.condorcet.duquesne.forum.Async;

import android.os.AsyncTask;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import be.condorcet.duquesne.forum.AddSubjectActivity;

public class AddSubjectAsync extends AsyncTask<String, Void, Integer>{
    private AddSubjectActivity AddSubjectActivity;

    public AddSubjectAsync(AddSubjectActivity AddSubjectActivity)
    {
        this.AddSubjectActivity=AddSubjectActivity;
    }
    @Override
    protected void onPreExecute() {
        // Prétraitement de l'appel
    }



    @Override
    protected Integer doInBackground(String... strings) {
        Integer retour;
        String idu  = strings[0];
        String titre = strings[1];
        String url_base = "https://deborahprojet.000webhostapp.com/AddSubject.php";
        String param    = "idu=" + idu + "&titre=" + titre;
        String url_tot = url_base + "?" + param;
        try
        {
            URL url=new URL(url_tot);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            /********************************************************************************'
             *
             * test en get avant pr tester ds l url
             * apres bon fctionnement on passe en post pr la securite sachant qu on accede et
             * qu on modifie l etat de l base
             *
             * ******************************************************************************/
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if(responseCode == 200)
            {
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
    {

        this.AddSubjectActivity.populate(retour);
    }

}
