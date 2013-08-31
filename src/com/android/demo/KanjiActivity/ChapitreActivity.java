package com.android.demo.KanjiActivity;

import java.util.ArrayList;

import com.android.demo.KanjiActivity.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
/**
 * Classe/vue affichant les choix entre les groupes de chapitres
 * @author Gabriel
 */
public class ChapitreActivity extends Activity implements View.OnClickListener
{
	/**
	 * Boutons pour les chapitres
	 */
	private ArrayList<Button> buttonsChap;
	
	private Button btQuit;
	
	/**
	 * Méthode appelée é la création de la vue
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
		 super.onCreate(savedInstanceState);
		 
		 setContentView(R.layout.chapitre_display);
		 
		 //Initialisation des différents boutons des groupes de chapitres + ajout des listeners
		 this.buttonsChap = new ArrayList<Button>(6);
		 /*i = 0 => chap 1-9
		  * i = 1 => chap 10-18
		  * i = 2 => chap 19-27
		  * i = 3 => chap 28-36
		  * i = 4 => chap 37-45
		  * i = 5 => chap 46-55
		  */
		 this.buttonsChap.add(0,(Button) findViewById(R.id.ch1));
		 this.buttonsChap.get(0).setOnClickListener(this);
		 this.buttonsChap.add(1,(Button) findViewById(R.id.ch10));
		 this.buttonsChap.get(1).setOnClickListener(this);
		 this.buttonsChap.add(2,(Button) findViewById(R.id.ch19));
		 this.buttonsChap.get(2).setOnClickListener(this);
		 this.buttonsChap.add(3,(Button) findViewById(R.id.ch28));
		 this.buttonsChap.get(3).setOnClickListener(this);
		 this.buttonsChap.add(4,(Button) findViewById(R.id.ch37));
		 this.buttonsChap.get(4).setOnClickListener(this);
		 this.buttonsChap.add(5,(Button) findViewById(R.id.ch46));
		 this.buttonsChap.get(5).setOnClickListener(this);
	     
		//idem avec le bouton de retour au menu
		this.btQuit = (Button) findViewById(R.id.menu);
		//on met le listener
		this.btQuit.setOnClickListener(this);
	}
	
	
	 /**
     * Listener des boutons.
     * Determine l'action é effectuer (ici appel d'une autre vue) lorsqu'un bouton est pressé.
     */
	public void onClick(View v)
    {
		//La passerelle vers la vue/activitée
		Intent intent;
		
		if (v != this.btQuit)
		{
			intent = new Intent(ChapitreActivity.this, SousChapActivity.class);
			//Selon le groupe de chapitre choisit, on en informe la classe SousChapActivity
			if (v == this.buttonsChap.get(0))
			{
				intent.putExtra("chap", "1-9");
			}
			else if (v == this.buttonsChap.get(1))
			{
				intent.putExtra("chap", "10-18");
			}
			else if (v == this.buttonsChap.get(2))
			{
				intent.putExtra("chap", "19-27");
			}
			else if (v == this.buttonsChap.get(3))
			{
				intent.putExtra("chap", "28-36");
			}
			else if (v == this.buttonsChap.get(4))
			{
				intent.putExtra("chap", "37-45");
			}
			else //if (v == btChap45)
			{
				intent.putExtra("chap", "46-55");
			}	
	    }//fin if(retour menu)
		else
		{
			intent = new Intent(ChapitreActivity.this, AccueilActivity.class);
			//On démarre l'activité
			startActivity(intent);
		}
		//On démarre l'activité
		startActivity(intent);		
    }//fin méthode
	
}//fin classe