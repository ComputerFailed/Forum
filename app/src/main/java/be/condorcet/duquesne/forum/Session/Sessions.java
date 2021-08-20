package be.condorcet.duquesne.forum.Session;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import be.condorcet.duquesne.forum.MainActivity;
public class Sessions
{
    // permet de garder trace des variables suite à une fermeture par exemple
    SharedPreferences preferences;

    //Pour éditer les préférences
    SharedPreferences.Editor editor;


    Context _context;


    int mode = 0;


    private static final String preferenceName = "AndroidPreference";


    private static final String isLog = "IsLogged";


    public static final String keyPseudo = "pseudo";


    public static final String keyPsw = "psw";

    // Constructeur avc principe d injection de dépendance on recoit le context en param il est injecté utile pr les tests unitaires
    public Sessions(Context context)
    {
        this._context = context;
        preferences = _context.getSharedPreferences(preferenceName, mode); // si on possède plusieurs ensemble de préférence.on peut le faire avc un seul nom , fct surchargée
        // on edite les preferences
        editor = preferences.edit();

    }

    /**
     * création d'une session de connexion
     * */
    public void LoginSession(String pseudo, String psw)
    {
        // on va stocker la valeur de connexion a true
        editor.putBoolean(isLog, true);

        // on stock le nom dans la preference

        editor.putString(keyPseudo, pseudo);

        // on stock l email ds la preference
        editor.putString(keyPsw, psw);

        //  commit() retourne true si la valeur est sauvegardée avec succès autrement false
        editor.commit();
    }

    /**
     * on va verifier  l'état de la connexion , si c'est pas oki on redirige vers page de connexion

     * */
    public void loginCkeck()
    {
        // Check login status
        if(!this.isLoggedIn())
        {
            //utilisateur non connecté on le redirige donc vers la page de connexion
            Intent i = new Intent(_context, MainActivity.class);
            // fermeture de ttes les activités
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // nveau drapeau pour demarrer une nvelle activité
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //ouverture de l activité de connexion
            _context.startActivity(i);
        }

    }



    /**
     * on va obtenir les données de session qui sont stockées
     * */
    public HashMap<String, String> getUserDetails()
    {
        // Une HashMap est une collection en Java qui associe une clé à une valeur, ça associe une clé à un objet, utile pour
        // recup des objets ds une bsd
        HashMap<String, String> user = new HashMap<String, String>();
        // nom de l utilisateur
        user.put(keyPseudo, preferences.getString(keyPseudo, null));

        // email id de l utilisateur
        user.put(keyPsw, preferences.getString(keyPsw, null));

        // on retourne l utilisateur
        return user;
    }

    /**
     * on vide la session
     * */
    public void logout()
    {
        // Con vide les données des preferences
        editor.clear();
        editor.commit();

        //apres deconnexion on redirige vers la page de connexion
        Intent i = new Intent(_context, MainActivity.class);
        // fermeture de ttes les activités
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //   // nveau drapeau pour demarrer une nvelle activité
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //ouverture de l activité de connexion
        _context.startActivity(i);
    }

    /**
     *verif de la connexion , ca donne l etat de la connexion
     * **/
    public boolean isLoggedIn()
    {
        return preferences.getBoolean(isLog, false);
    }
}
