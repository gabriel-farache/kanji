package com.android.demo.KanjiActivity;

import java.util.Locale;
import java.util.ResourceBundle;
import com.android.demo.KanjiActivity.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * La vue principale (écran d'acceuil)
 * @author Gabriel
 */
public class ChoiceChapInterActivity extends ListActivity
{

	/**
	 * La locale, ici FRENCH
	 */
	private static Locale l = Locale.FRENCH ;
	
	/**
	 * Le bundle qui contient les solutions (mots en franéais), le numéro du premier kanji de chaque chapitre, la petite histoire du kanji (Not Yet Implemented)
	 */
	private static ResourceBundle messages = ResourceBundle.getBundle("com.android.demo.KanjiActivity.test", l);
    
	/**
	 * Méthode appelée é la création de la vue
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	    //setContentView(R.layout.main);
	    
	    String[] choice = getResources().getStringArray(R.array.choices_array);
	    setListAdapter(new ArrayAdapter<String>(this, R.layout.choice_display, choice));

	    ListView lv = getListView();
	    lv.setTextFilterEnabled(true);

	    lv.setOnItemClickListener(new OnItemClickListener() 
	    {
	      public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	      {
	    	//La passerelle vers la vue/activitée
	      	Intent intent;
	      	
	      	//si c'est le bouton "Intervalle" qui a été préssé
	      	if (id == 0)
	  	   	{
	      		intent = new Intent(ChoiceChapInterActivity.this, IntervalleActivity.class);
	      		//Démarrer l'activité suivante
		      	startActivity(intent);
	  	   	}
	  	   	else if (id == 1)
	  	   	{
	  	   		
	  	   		intent = new Intent(ChoiceChapInterActivity.this, ChapitreActivity.class);
	  	   		//Démarrer l'activité suivante
		      	startActivity(intent);
	  	   	}
	  	   	else
	  	   	{
	  	   		Toast.makeText(getApplicationContext(),"Choix inconnu.", Toast.LENGTH_SHORT).show();
	  	   	}
	     	
	      }
	    });

	}//fin méthode
    
    /**
     * Fonction retournant le bundle
     * @return le bundle (test_fr.properties)
     */
	public static ResourceBundle getBundle()
	{
		return (ChoiceChapInterActivity.messages);
	}//fin méthode
	
  
}//fin classe

