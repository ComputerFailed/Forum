package be.condorcet.duquesne.forum;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import be.condorcet.duquesne.forum.Async.SubjectListAsync;
import be.condorcet.duquesne.forum.Session.Sessions;


public class MenuActivity extends AppCompatActivity
{
    private Button retour, btn_list ,add;
    private RadioGroup rg_list_sujet;
    Sessions session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.MenuPage));
        setContentView(R.layout.activity_menu);
        /*pr arriver au menu il faut etre connecte d ou le systeme de session
        * la session est accordee sur le speudo a la connexion             */
        session = new Sessions(getApplicationContext());
        session.loginCkeck();

        rg_list_sujet = (RadioGroup) findViewById(R.id.rg_list_sujet);

        retour = (Button) findViewById(R.id.btn_menu_logout);
        btn_list = (Button) findViewById(R.id.b_page_menu_oki);
        add = (Button) findViewById(R.id.btn_add_subject);


        retour.setOnClickListener(Listener_btn_menu_logout);
        btn_list.setOnClickListener(Listener_b_page_menu_oki);
        add.setOnClickListener(Listener_btn_add_subject);

        new SubjectListAsync(MenuActivity.this).execute();

    }
    /****************************************************************************************************************
     *
     *      evenement qui pour ajouter un sujet
     *
     * **************************************************************************************************************/
    private View.OnClickListener Listener_btn_add_subject = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(MenuActivity.this, AddSubjectActivity.class);
            startActivity(intent);
        }

    };
    /****************************************************************************************************************
     *
     *      evenement qui pour choisir un sujet , un sujet doit etre selectionne sinon un msg d erreur s affiche
     *
     * **************************************************************************************************************/
    private View.OnClickListener Listener_b_page_menu_oki = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int id = rg_list_sujet.getCheckedRadioButtonId();
            if(id<0)
            {
                Toast.makeText(MenuActivity.this, R.string.Txt_choose_subject, Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent intent = new Intent(MenuActivity.this, SubjectActivity.class);
                String ids = String.valueOf(id);
                /*je place ds Subject le nom du sujet choisis ds le menu , on peut choisir autre chose selon la table et les
                * elements qu elle fournit */
                intent.putExtra("sujet", ids);
                startActivity(intent);
            }
        }


    };
    /****************************************************************************************************************
     *
     *      Deconnexion grace a la session
     *
     * **************************************************************************************************************/
    private View.OnClickListener Listener_btn_menu_logout = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            session.logout();
        }

    };

    /**************************************************************************************************************
     *
     *  methode qui va gerer les erreurs relatives au listing des sujets
     *  prob de serveur , url pas oki, element pas trouve ..
     *
     *
     * **********************************************************************************************************/
    public void populate(String retour_code)
    {

        if (retour_code.equals("-404"))
        {
            Toast.makeText(MenuActivity.this, R.string.Error_404, Toast.LENGTH_LONG).show();
        }
        else if (retour_code.equals("-500"))
        {
            Toast.makeText(MenuActivity.this, R.string.Error, Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {
                JSONArray itemArray = new JSONArray(retour_code);
                ArrayList<HashMap<String, String>> listsujet = new ArrayList<>();
                for (int i = 0; i < itemArray.length(); i++)
                {
                    HashMap<String, String> sujet = new HashMap<>();
                    JSONObject obj = itemArray.getJSONObject(i);
                    sujet.put("id", obj.getString("ids"));
                    sujet.put("Title", obj.getString("Title"));
                    listsujet.add(sujet);
                }

                int nbr_elem = listsujet.size();
                RadioButton[] tab_rb = new RadioButton[nbr_elem];
                int i;
                for (i = 0; i < nbr_elem; i++)
                {
                    tab_rb[i] = new RadioButton(this);
                    int id = Integer.parseInt(listsujet.get(i).get("id"));
                    tab_rb[i].setId(id);
                    tab_rb[i].setText(listsujet.get(i).get("Title"));
                    rg_list_sujet.addView(tab_rb[i]);
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(MenuActivity.this, R.string.ElementNo, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}