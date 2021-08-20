package be.condorcet.duquesne.forum;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import be.condorcet.duquesne.forum.Async.TownAsync;

public class TownListActivity extends AppCompatActivity
{
    private RadioGroup rg_list_town;
    private Button but_valider ;
    private Button but_retour;


    private View.OnClickListener Listener_but_valider = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == but_valider)
            {
                int id = rg_list_town.getCheckedRadioButtonId();
                if (id == -1)
                {
                    Toast.makeText(TownListActivity.this, R.string.Tv_choose_town, Toast.LENGTH_SHORT).show();
                }
                else
                 {
                    String town = ((RadioButton) findViewById(id)).getText().toString();
                    Intent intent_retour = new Intent();
                    /**********************************************************************************************
                     *
                     *   a recup avec les getExtra sur l autre intent
                     *
                     *
                     * *****************************************************************************************/
                    intent_retour.putExtra("Idt", Integer.toString(id));
                    intent_retour.putExtra("Libel", town);
                    setResult(RESULT_OK, intent_retour);
                    finish();
                }
            }
            else if (v == but_retour)
            {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    };
    /****************************************************************************************************************
     *
     *    Retour a la page de connexion
     *
     * **************************************************************************************************************/
    private View.OnClickListener Listener_but_retour = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            Intent intent = new Intent(TownListActivity.this, MainActivity.class);
            startActivity(intent);
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town_list);

        rg_list_town = (RadioGroup) findViewById(R.id.rg_list_town_l);

        new TownAsync(TownListActivity.this).execute();

        //variable Button
        but_valider = (Button) findViewById(R.id.but_valider_l);
        but_retour = (Button) findViewById(R.id.but_ret_l);

        but_valider.setOnClickListener(Listener_but_valider);
        but_retour.setOnClickListener(Listener_but_retour);
    }
    /****************************************************************************************************************
     *
     *     va gerer les erreurs relatives au listing des villes
     *      serveur en panne, url incorrecte, elment pas trouve
     *
     * **************************************************************************************************************/
    public void populate(String retour_code)
    {
        if (retour_code.equals("-404"))
        {
            Toast.makeText(TownListActivity.this, R.string.Error_404, Toast.LENGTH_LONG).show();
        }
        else if (retour_code.equals("-500"))
        {
            Toast.makeText(TownListActivity.this, R.string.Error, Toast.LENGTH_LONG).show();
        }
        else
          {
            try
            {
                JSONArray itemArray = new JSONArray(retour_code);
                ArrayList<HashMap<String, String>> townList = new ArrayList<>();

                for (int i = 0; i < itemArray.length(); i++)
                {
                    HashMap<String, String> town= new HashMap<>();
                    JSONObject obj = itemArray.getJSONObject(i);
                    town.put("id", obj.getString("Idt"));//id de la ville

                    town.put("nom", obj.getString("Libel"));// nom de la ville
                    townList.add(town);
                }

                int nbr_elem = townList.size();
                RadioButton[] tab_rb = new RadioButton[nbr_elem];
                int i;
                for (i = 0; i < nbr_elem; i++)
                {
                    tab_rb[i] = new RadioButton(this);
                    int id = Integer.parseInt(townList.get(i)
                            .get("id"));
                    tab_rb[i].setId(id);
                    tab_rb[i].setText(townList.get(i)
                            .get("nom"));
                    rg_list_town.addView(tab_rb[i]);
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(TownListActivity.this, R.string.ElementNo, Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }
    }
}