package be.condorcet.duquesne.forum;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.condorcet.duquesne.forum.Async.MainAsync;
import be.condorcet.duquesne.forum.Session.Sessions;


public class MainActivity extends AppCompatActivity
{
    private EditText et_pseudo_launch, et_mdp_launch;
    private Button but_connexion, but_inscription;
    private String pseudo, mdp;
    Sessions session;

    private View.OnClickListener Listener_but_connexion = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            /*recuperation du mdp et du pseudo saisis  */
            pseudo = et_pseudo_launch.getText().toString();
            mdp = et_mdp_launch.getText().toString();
            /*si le mdp et le speudo n est pas vide la tache de fond les verifie et si c est oki ca conecte*/
            if (!pseudo.equals("") && !mdp.equals(""))
            {
                new MainAsync(MainActivity.this).execute(pseudo, mdp);
            }
            /* si les champs sont vides toast  */
            else
            {
                Toast.makeText(MainActivity.this,R.string.remplir_champs, Toast.LENGTH_LONG).show();
            }

        }
    };

    private View.OnClickListener Listener_but_inscription = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getResources().getString(R.string.Title_accueil));

        session = new Sessions(getApplicationContext());

        et_pseudo_launch = (EditText)findViewById(R.id.et_pseudo_m);
        et_mdp_launch = (EditText)findViewById(R.id.et_mdp_m);


        but_connexion=(Button)findViewById(R.id.but_connexion);
        but_inscription=(Button)findViewById(R.id.but_inscription_m);


        but_connexion.setOnClickListener(Listener_but_connexion);
        but_inscription.setOnClickListener(Listener_but_inscription);

    }
    /**************************************************************************************************************************
     *
     *  ca gere le connexion donc voir si valeur non vide ,  si le coæpte exite, si url oki et si serveur operationnel
     * *************************************************************************************************************************/
    public void populate(int code_retour)
    {
        switch (code_retour)
        {
            case -1:
                Toast.makeText(MainActivity.this, R.string.accountNo, Toast.LENGTH_LONG).show();
                break;

            case -10:
                Toast.makeText(MainActivity.this,  R.string.PseudoNo, Toast.LENGTH_LONG).show();
                break;
            case -11:
                Toast.makeText(MainActivity.this,  R.string.PswNo, Toast.LENGTH_LONG).show();
                break;
            case -404:
                Toast.makeText(MainActivity.this, R.string.Error_404, Toast.LENGTH_LONG).show();
                break;
            case -500:
                Toast.makeText(MainActivity.this, R.string.Error, Toast.LENGTH_LONG).show();
                break;
            default:
                session.LoginSession( Integer.toString(code_retour),pseudo );//****sens
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
