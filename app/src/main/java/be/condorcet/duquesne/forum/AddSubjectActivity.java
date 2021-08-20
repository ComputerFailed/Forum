package be.condorcet.duquesne.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import be.condorcet.duquesne.forum.Async.AddSubjectAsync;
import be.condorcet.duquesne.forum.Session.Sessions;


public class AddSubjectActivity extends AppCompatActivity {

    EditText et_msg;
    Button back,add;
    Sessions session;
    private String idu, titre;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        setTitle(getResources().getString(R.string.TitleSubject));
        session = new Sessions(getApplicationContext());
        session.isLoggedIn();

        HashMap<String, String> user = session.getUserDetails();
        idu = user.get(Sessions.keyPseudo);
        et_msg= (EditText) findViewById(R.id.et_newSubject);
        add=(Button) findViewById(R.id.btn_addSubject);
        back=(Button) findViewById(R.id.btn_subjectBack);
        add.setOnClickListener(Listener_add);
        back.setOnClickListener(Listener_back);
    }


    View.OnClickListener Listener_add = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            titre = et_msg.getText().toString();
            if (titre == null || titre.length() <= 10)
            {
                Toast.makeText(AddSubjectActivity.this, getResources().getString(R.string.SubjLn), Toast.LENGTH_LONG).show();
            }
            else
            {
                // ajouter d autres elements
                new AddSubjectAsync(AddSubjectActivity.this).execute(idu,titre);
            }
        }
    };


    View.OnClickListener Listener_back = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            // retour menu
            Intent intent = new Intent(AddSubjectActivity.this, MenuActivity.class);
            startActivity(intent);
        }

    };

    public void populate(int code_retour)
    {
        switch (code_retour)
        {
            case -1:
                Toast.makeText(AddSubjectActivity.this, R.string.SujectExis, Toast.LENGTH_LONG).show();
                break;
            case -10:
                Toast.makeText(AddSubjectActivity.this, "prob titre", Toast.LENGTH_LONG).show();
                break;
            case -11:
                Toast.makeText(AddSubjectActivity.this, "prob user", Toast.LENGTH_LONG).show();
                break;
            case -404:
                Toast.makeText(AddSubjectActivity.this, R.string.Error_404, Toast.LENGTH_LONG).show();
                break;
            case -500:
                Toast.makeText(AddSubjectActivity.this, R.string.Error, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(AddSubjectActivity.this, R.string.Btn_oki, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddSubjectActivity.this, MenuActivity.class);
                startActivity(intent);
                break;
        }
    }
}