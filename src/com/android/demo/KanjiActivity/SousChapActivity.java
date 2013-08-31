package com.android.demo.KanjiActivity;

import java.util.ArrayList;

import com.android.demo.KanjiActivity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Classe/vue permettant de choisir son chapitre en fonction du groupe de chapitre choisit précedement
 * @author Gabriel
 *
 */
public class SousChapActivity extends Activity implements View.OnClickListener
{
	/**
	 * Les boutons (correspond aux différents chapitres du goupe en cours)
	 */
	private ArrayList<Button> buttons;

	/**
	 * Le numéro du premier kanji du chapitre du groupe en cours
	 */
	private int numChap;

	/**
	 * Le groups choisit (1-9, 10-18, ...)
	 */
	private static String valChaps; 

	private Button btQuit;

	/**
	 * Méthode appelée é la création de la vue
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		//Affichage des élément de sous_chapitre_display.xml
		setContentView(R.layout.sous_chapitre_display);

		//L'intent pour récupérer les infos
		Intent thisIntent = getIntent();
		

		//On récupére le groupe de chapitres choisit
		SousChapActivity.valChaps = thisIntent.getExtras().getString("chap");

		//initialisation ArrayList
		this.buttons = new ArrayList<Button>(10);

		//on initialise les boutons en fonction des chapitres
		this.initButtons(this.initChaps());
		
		

	}//fin méthode

	
	private String[] initChaps()
	{
		//les  numéros de chapitre
		String [] chaps = new String[10];
		
		//on fonction du groupe choisit, on donne leur valeur aux boutons et on donne la valeur du premier kanji du premier chapitre du groupe
		if (SousChapActivity.valChaps.compareToIgnoreCase("1-9") == 0)
		{
			for (int i = 1; i <= 9; i++)
			{
				chaps[i-1] = "0"+i;
			}
			this.numChap = 1;

		}
		else if (SousChapActivity.valChaps.compareToIgnoreCase("10-18") == 0)
		{
			for (int i = 0; i < 9; i++)
			{
				chaps[i] = ""+(10+i);
			}
			this.numChap = 10;
		}
		else if (SousChapActivity.valChaps.compareToIgnoreCase("19-27") == 0)
		{
			for (int i = 0; i < 9; i++)
			{
				chaps[i] = ""+(19+i);
			}
			this.numChap = 19;
		}
		else if (SousChapActivity.valChaps.compareToIgnoreCase("28-36") == 0)
		{
			for (int i = 0; i < 9; i++)
			{
				chaps[i] = ""+(28+i);
			}
			this.numChap = 28;
		}
		else if (SousChapActivity.valChaps.compareToIgnoreCase("37-45") == 0)
		{
			for (int i = 0; i < 9; i++)
			{
				chaps[i] = ""+(37+i);
			}
			this.numChap = 37;
		}
		else //if (valChaps.compareToIgnoreCase("46-55") == 0)
		{
			for (int i = 0; i < 10; i++)
			{
				chaps[i] = ""+(46+i);
			}
			this.numChap = 46;

		}//fin if-else if -...-else
		
		return chaps;
	}
	
	
	private void initButtons(String [] chaps)
	{
		/*Récupération des 9 boutons de chois de chapitre + ajout d'un listener pour chaque bouton*/
		this.buttons.add(0, (Button) findViewById(R.id.bt1));
		this.buttons.get(0).setText(chaps[0]);
		this.buttons.get(0).setOnClickListener(this);

		this.buttons.add(1, (Button) findViewById(R.id.bt2));
		this.buttons.get(1).setText(chaps[1]);
		this.buttons.get(1).setOnClickListener(this);

		this.buttons.add(2, (Button) findViewById(R.id.bt3));
		this.buttons.get(2).setText(chaps[2]);
		this.buttons.get(2).setOnClickListener(this);

		this.buttons.add(3, (Button) findViewById(R.id.bt4));
		this.buttons.get(3).setText(chaps[3]);
		this.buttons.get(3).setOnClickListener(this);

		this.buttons.add(4, (Button) findViewById(R.id.bt5));
		this.buttons.get(4).setText(chaps[4]);
		this.buttons.get(4).setOnClickListener(this);

		this.buttons.add(5, (Button) findViewById(R.id.bt6));
		this.buttons.get(5).setText(chaps[5]);
		this.buttons.get(5).setOnClickListener(this);

		this.buttons.add(6, (Button) findViewById(R.id.bt7));
		this.buttons.get(6).setText(chaps[6]);
		this.buttons.get(6).setOnClickListener(this);

		this.buttons.add(7, (Button) findViewById(R.id.bt8));
		this.buttons.get(7).setText(chaps[7]);
		this.buttons.get(7).setOnClickListener(this);

		this.buttons.add(8, (Button) findViewById(R.id.bt9));
		this.buttons.get(8).setText(chaps[8]);
		this.buttons.get(8).setOnClickListener(this);

		if (this.numChap == 46)
		{
			/*Dans le dernier groupe, il y a 10 boutons donc on fait les actions é faire pour ce 10iéme bouton (on le recupére, lui donne sa valeur, le rendons visible et on lui met un listener*/
			this.buttons.add(9,(Button) findViewById(R.id.bt10));
			this.buttons.get(9).setText("55");
			this.buttons.get(9).setVisibility(android.view.View.VISIBLE);
			this.buttons.get(9).setOnClickListener(this);
		}
		
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
		
		if (v != this.btQuit)
		{

			this.handleChapChoice(v);
		}//fin if(retour menu)
		else
		{
			this.handleBackMenu();
		}	
		
	}//fin méthode

	private void handleChapChoice(View v)
	{
		//La passerelle vers la vue/activitée
		Intent intent;
		int i;
				
		if (AccueilActivity.getChoixAcc().compareToIgnoreCase("kanjiTxtIn") == 0)
		{
			intent = new Intent(SousChapActivity.this, QuizzActivity.class);
		}
		else if (AccueilActivity.getChoixAcc().compareToIgnoreCase("kanjiBtn") == 0)
		{
			intent = new Intent(SousChapActivity.this, TrainingQuizzActivity.class);
		}
		else if (AccueilActivity.getChoixAcc().compareToIgnoreCase("SigniBtn") == 0)
		{
			intent = new Intent(SousChapActivity.this, SigniTrainingQuizzActivity.class);
		}
		else if (AccueilActivity.getChoixAcc().compareToIgnoreCase("SigniQCM") == 0)
		{
			intent = new Intent(SousChapActivity.this, SigniQCMQuizzActivity.class);
		}
		else 
		{
			intent = new Intent(SousChapActivity.this, QCMQuizzActivity.class);
		}

		/*On stock les informations dont aura besoin VerifieActivity pour fonctionner :
		 *  le type du choix de l'utilisateur (intervalle ou chapitre)
		 *  en fonction du bouton préssé, le numéro du premier kanji du chapitre choisit : on prend la valeur du premier kanji du premier chapitre du groupe et on lui ajoute la valeur du bouton - 1 (pour le 1er bouton)
		 */
		intent.putExtra("type", "chap");
		
		for(i = 0; ((i < 10 && this.numChap == 46) || (i < 9 && this.numChap != 46)) && this.buttons.get(i) != v; i++) {}
		intent.putExtra("chap", numChap+i);
		
		//On démarre l'activité
		startActivity(intent);
	}
	
	private void handleBackMenu()
	{
		Intent intent = new Intent(SousChapActivity.this, AccueilActivity.class);
		//On démarre l'activité
		startActivity(intent);
	}
	/**
	 * 
	 * @return le groupe de chapitre 1-9, 10-18, ...
	 */
	public static String getValChaps()
	{
		return SousChapActivity.valChaps;
	}


}//fin classe