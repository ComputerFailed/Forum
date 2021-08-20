package be.condorcet.duquesne.forum;

import
        androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import be.condorcet.duquesne.forum.Async.RegisterAsync;
import be.condorcet.duquesne.forum.Session.Sessions;

public class RegisterActivity extends AppCompatActivity
{

    private EditText ed_Pseudo, ed_Psw , ed_Psw2 ;
    private RadioGroup rg_gender;
    private String pseudo, mdp, mdp2, gender ,idt,town;
    private TextView tv_town;
    private Sessions session;
    private Button regis ,back, valider;
    RegisterAsync me;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        session = new Sessions(getApplicationContext());
        //* test pr id ville
        Intent intentRecup = getIntent();
        idt = intentRecup.getStringExtra("Idt");

        // titre page d inscription
        setTitle(getResources().getString(R.string.TitleRegis));
        ed_Psw = findViewById(R.id.et_mdp_r);

        ed_Pseudo = findViewById(R.id.et_pseudo_r);
        ed_Psw2 = findViewById(R.id.et_mdp_2_r);
        rg_gender = (RadioGroup) findViewById(R.id.rg_gender_r);
        tv_town=(TextView) findViewById(R.id.tv_town_r);

        regis = (Button) findViewById(R.id.btn_register_r);
        back = (Button) findViewById(R.id.btn_back_r);
        valider = (Button) findViewById(R.id.but_ville_r);

        regis.setOnClickListener(Listener_but_regis);
        back.setOnClickListener(Listener_but_back);
        valider.setOnClickListener(Listener_but_valider);
    }

    private View.OnClickListener Listener_but_regis = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            int id = rg_gender.getCheckedRadioButtonId();
            town=tv_town.getText().toString();
            if(id == -1)
            {
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.GenderNo), Toast.LENGTH_LONG).show();
            }
            else
            {
                RadioButton radioGenre = (RadioButton) findViewById(id);//**

                /*******************************************************************************************************************
                 *
                 * verification des valeurs  vides , la longueur des champs entres , verif des deux psw identiques
                 * si ces conditions ne sont pas respectees l enregistrement n aura pas lieu
                 * des verif similiares sont faites aussi cote serveur (obligatoire)
                 *
                 ******************************************************************************************************************/

                if(ed_Pseudo.getText().toString().isEmpty() || ed_Pseudo.getText().toString() == null)
                {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.PseudoNo), Toast.LENGTH_LONG).show();
                }
                else if(ed_Pseudo.getText().length() < 8)
                {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.PseudoLn), Toast.LENGTH_LONG).show();
                }

                else if(ed_Psw.getText().toString().isEmpty() || ed_Psw.getText().toString() == null)
                {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.PswNo), Toast.LENGTH_LONG).show();
                }

                else if(ed_Psw.getText().length() <10 )
                {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.PswLn), Toast.LENGTH_LONG).show();
                }

                else if(ed_Psw2.getText().toString().isEmpty() || ed_Psw2.getText().toString() == null)
                {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.CheckPsw), Toast.LENGTH_LONG).show();
                }

                else if(!ed_Psw.getText().toString().equals(ed_Psw2.getText().toString()))
                {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.PswDiff), Toast.LENGTH_LONG).show();
                }

                else if((radioGenre.getText().toString().isEmpty() || radioGenre.getText().toString() == null))
                {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.GenderNo), Toast.LENGTH_LONG).show();
                }

                else if(town == null)
                {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.Townselect), Toast.LENGTH_LONG).show();
                }

                else
                {
                    /*******************************************************************************************************
                     *
                     * initialisation des variables pour l inscription
                     * *****************************************************************************************************/
                    pseudo = ed_Pseudo.getText().toString();
                    mdp = ed_Psw.getText().toString();
                    mdp2 = ed_Psw2.getText().toString();
                    gender=radioGenre.getText().toString();
                    /*******************************************************************************************************
                     *
                     *  test afin de voir si les valeurs etaient bien recup dans les chqmps

                        Toast.makeText(RegisterActivity.this, "id to "+idt, Toast.LENGTH_LONG).show();
                        Toast.makeText(RegisterActivity.this, "oki "+mdp, Toast.LENGTH_LONG).show();
                        Toast.makeText(RegisterActivity.this, "oki "+town, Toast.LENGTH_LONG).show();
                        Toast.makeText(RegisterActivity.this, "oki "+gender, Toast.LENGTH_LONG).show();
                     * *****************************************************************************************************/
                    new RegisterAsync(RegisterActivity.this).execute(pseudo,mdp,idt,gender);
                }
            }
        }
    };

    private View.OnClickListener Listener_but_back = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener Listener_but_valider = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(RegisterActivity.this, TownListActivity.class);
            startActivityForResult(intent, 1);
        }

    };
    /****************************************************************************************************************
     *  permet d obtenir le resultat d une autre activite
     *  dans le cas present je recupere les donnees de ma liste de ville
     *  en l occurence la ville choisie par le user qui s inscrit
     *  startActivityForResult() enverra ces infos a son appel  qd on appuie sur valider le
     *  choix de la ville
     *
     * **************************************************************************************************************/
    protected void onActivityResult(int num_requete, int code_retour, Intent data)
    {

        super.onActivityResult(num_requete, code_retour, data);
        if (num_requete == 1)
        {
            if (code_retour == RESULT_OK)
            {
                town = data.getStringExtra("Libel");
                idt = data.getStringExtra("Idt");
                /********************************************************************
                 * recup de la liste de ville id et nom ville que j attribue au TxtV
                 * ******************************************************************/
                tv_town.setText(town);
                //tv_town.setText(idt);
            }
            else if (code_retour == RESULT_CANCELED)
            {
                Toast.makeText(RegisterActivity.this, R.string.Annul, Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*****************************************************************************************************************
     *
     * dans mon script php je renvoie des codes selon la cas de figure a traiter
     * si le serveur renvoie une erreur elle sera geree ici par l affichage d un msg
     *envisager les erreurs relatives a la connexion aussi
     *
     *
     * ***************************************************************************************************************/
    public void populate(int code_retour)
    {
        switch (code_retour)
        {

            case -10:
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.PseudoNo), Toast.LENGTH_LONG).show();
                break;
            case -11:
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.PswNo), Toast.LENGTH_LONG).show();
                break;
            case -12:
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.GenderNo), Toast.LENGTH_LONG).show();
                break;
            case -13:
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.TonwNo), Toast.LENGTH_LONG).show();
                break;
            case -14:
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.ErrorSql), Toast.LENGTH_LONG).show();
                break;
            case -1:
                Toast.makeText(RegisterActivity.this, R.string.accountNo, Toast.LENGTH_LONG).show();
                break;
            case -2:
                Toast.makeText(RegisterActivity.this, R.string.accountStill, Toast.LENGTH_LONG).show();
                break;
            case -404:
                Toast.makeText(RegisterActivity.this, R.string.Error_404, Toast.LENGTH_LONG).show();
                break;
            case -500:
                Toast.makeText(RegisterActivity.this, R.string.Error, Toast.LENGTH_LONG).show();
                break;
            default:
                session.LoginSession( Integer.toString(code_retour), pseudo);
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    /*    // A VERIFIER VU QUE MEME FONCTION AUTANT LA RAPPELER ET PAS LA REECRIRE

je les ai pas mis car pr tester l appli c est hyper chiant suffit juste de la mettre avec les autres verifs

        public final static boolean isPswValid(String p)
        {
            return Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-Z0-9]{4,12}$").matcher(p).matches();
        }

        public final static boolean isPseudoValid(String p)
        {
            return Pattern.compile("^(?=.*[a-zA-Z가-힣])[a-zA-Z가-힣]{1,}$").matcher(p).matches();

        }

        */
}