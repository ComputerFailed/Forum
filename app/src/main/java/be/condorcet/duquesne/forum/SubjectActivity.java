package be.condorcet.duquesne.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import be.condorcet.duquesne.forum.Async.AddMsgAsync;
import be.condorcet.duquesne.forum.Async.MsgInAsync;
import be.condorcet.duquesne.forum.Session.Sessions;

public class SubjectActivity extends AppCompatActivity
{
    String ids, idu ,message_text;
    private Button add, back;
    Sessions session;
    private EditText text_message;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        setTitle(getResources().getString(R.string.TitleAddMsg));

        /* on recupere les infos du user connecte car on a besoin de son id pr l insert de msg*/
        session = new Sessions(getApplicationContext());
        session.loginCkeck();
        HashMap<String, String> user = session.getUserDetails();
        idu = user.get(Sessions.keyPseudo);
        /*recuperation des infs de l activite sujet on recupere le nom du sujet
        * grace au putextra de l activite sujet  */
        Intent intentRecup = getIntent();
        ids = intentRecup.getStringExtra("sujet");
        text_message = (EditText) findViewById(R.id.et_MsgSubject);

        add = (Button) findViewById(R.id.btn_add_msg);
        back = (Button) findViewById(R.id.btn_bck);


        add.setOnClickListener(Listener_btn_add_msg);
        back.setOnClickListener(Listener_btn_bck);

        new MsgInAsync(SubjectActivity.this).execute(ids);

    }
    /****************************************************************************************************************
     *
     *     permet d ajouter un message, pr ce faire on a besoin de la date systeme grace a Calendar,
     *     id user, id sujet et du msg a placer
     *
     * **************************************************************************************************************/
    private View.OnClickListener Listener_btn_add_msg = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            message_text = text_message.getText().toString();

            if (!message_text.equals(""))
            {
                if (message_text.length() > 1)
                {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    new AddMsgAsync(SubjectActivity.this).execute(ids, idu, message_text,formattedDate);
                }
                else

                {
                    Toast.makeText(SubjectActivity.this, R.string.MsgNo, Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(SubjectActivity.this, R.string.Btn_add_mss, Toast.LENGTH_LONG).show();
            }

        }
    };
    /****************************************************************************************************************
     *
     *      retour a la page ou il y a le listing de sujet
     *
     * **************************************************************************************************************/
    private View.OnClickListener Listener_btn_bck = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(SubjectActivity.this, MenuActivity.class);
            startActivity(intent);
        }
    };

    /******************************************************************************************************************
     *
     *  appelee ds Msglnasync
     *  liste des messges, gere les diffferentes erreurs elatives a ce listing
     *
     * *************************************************************************************************************'*/
    public void populate(String retour_code)
    {

        if (retour_code.equals("-404"))
        {
            Toast.makeText(SubjectActivity.this, R.string.Error_404, Toast.LENGTH_LONG).show();
        }
        else if (retour_code.equals("-500"))
        {
            Toast.makeText(SubjectActivity.this, R.string.Error, Toast.LENGTH_LONG).show();
        }
        else
            {
            String Content, Date, Pseudo;
            try
            {
                JSONArray itemArray = new JSONArray(retour_code);
                ArrayList<HashMap<String, String>> listmessage = new ArrayList<>();
                for (int i = 0; i < itemArray.length(); i++)
                {
                    HashMap<String, String> mes = new HashMap<>();
                    JSONObject obj = itemArray.getJSONObject(i);
                    mes.put("idm", obj.getString("idm"));
                    mes.put("Content", obj.getString("Content"));
                    mes.put("Pseudo", obj.getString("Pseudo"));
                    mes.put("Date", obj.getString("Date"));
                    listmessage.add(mes);
                }
                int nbr_elem = listmessage.size();
                ListView listView = (ListView) findViewById(R.id.li_message);
                ArrayList<String> arrayList = new ArrayList<>();
                int i;
                for (i = 0; i < nbr_elem; i++)
                {
                    Content = listmessage.get(i).get("Content");
                    Pseudo = listmessage.get(i).get("Pseudo");
                    Date = listmessage.get(i).get("Date");
                    arrayList.add("Content :" + Content + " Pseudo :" + Pseudo + "Date :"+ Date);
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(arrayAdapter);
            }
            catch (JSONException e)
            {
                Toast.makeText(SubjectActivity.this, R.string.ElementNo, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    /******************************************************************************************************************
     *
     *  appelee ds AddMsgAsync
     *  gere les erreur  relative a l ajout de sujet
     *
     * *************************************************************************************************************'*/

    public void populate2(int code_retour)
    {
        switch (code_retour)
        {
            case -404:
                Toast.makeText(SubjectActivity.this, R.string.Error_404, Toast.LENGTH_LONG).show();
                break;
            case -500:
                Toast.makeText(SubjectActivity.this, R.string.Error, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(SubjectActivity.this, R.string.SubjOki, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SubjectActivity.this, MenuActivity.class);
                startActivity(intent);
                break;
        }
    }
}
