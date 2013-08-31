package com.android.demo.KanjiActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public abstract class MainTrainingActivity extends MainQuizzesActivity
{
	

	/**
	 * Le bouton du kanji
	 */
	protected Button btKanji;

	/**
	 * Le bouton de validation
	 */
	protected ImageButton btNext;


	/**
	 * Le bouton de reponse
	 */
	protected Button btRep;

	/**
	 * indique si l'utilisateur a déjà cliqué une fois pour avoir la réponse
	 */
	protected static boolean repOk;

	protected static boolean isLastKanji;	


	protected abstract void continuer();

	/**
	 * Méthode appelée é la création de la vue
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//Selon la classe fille, charger le bon layouts
		if(this instanceof SigniTrainingQuizzActivity)
		{
			//afficher le layout comme définit dans boutons_kanjis.xml
			setContentView(R.layout.signi_training_boutons_kanjis);
		}
		else if(this instanceof RevisionQuizzActivity)
		{
			//afficher le layout comme définit dans boutons_kanjis.xml
			setContentView(R.layout.revisions_kanjis);
		}
		else
		{
			//afficher le layout comme définit dans boutons_kanjis.xml
			setContentView(R.layout.training_boutons_kanjis);
		}
		//ATTENTION CHAP VAUT [1, 55] OR UN TABLEAU COMMENCE A 0 !!!
		//si on vient pour le premiére fois
		if (MainQuizzesActivity.pass == 0)
		{
			this.firstTime();
		}//fin if(premier passage)

		//Si il y a vraiment de kanjis à afficher, faire le quizz
		if (MainQuizzesActivity.nbKanjiTotChap > 0)
		{
			//afficher le quizz
			this.displayQuizz();
			//si c'est la denrière question, mettre à jour les variable de fin
			if (MainQuizzesActivity.kanjiFait.size() >= MainQuizzesActivity.nbKanjiTotChap)
			{
				MainTrainingActivity.isLastKanji = true;
				this.btNext.setVisibility(android.view.View.GONE);
				this.btOk.setVisibility(android.view.View.VISIBLE);
			}
		}
		//sinon, afficher qu'il n'y a rien à faire
		else
		{
			this.btRep = (Button) findViewById(R.id.rep);
			this.btRep.setText("Aucun kanji à afficher.");

			this.btKanji = (Button) findViewById(R.id.k1);
			this.btKanji.setVisibility(android.view.View.GONE);

			this.initButtons();
		}


	}//fin méthode onCreate

	protected void displayQuizz()
	{
		super.displayQuizz();

		if(MainTrainingActivity.repOk)
		{
			this.afficheRep();
		}
	}

	protected void afficheRep()
	{
		this.btKanji.setBackgroundResource(MainQuizzesActivity.kanjisBouton.get(MainQuizzesActivity.random));
	}

	/**
	 * Affiche un pop-up contenant les information d'un kanji
	 * @param i : le numéro du kanji
	 */
	protected void affichePopUp(int i)
	{
		//Afficher le message dans un pop-up
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Kanji n°"+(((Integer)i).toString()));
		builder.setMessage("Kanji n°"+(((Integer)i).toString()))
		.setCancelable(false)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		//Création puis affichage du pop-up 
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Listener des boutons.
	 * Determine l'action é effectuer (ici appel d'une autre vue) lorsqu'un bouton est pressé.
	 */
	public void onClick(View v)
	{
		this.checkBtAddSuppr(v);
		
		//si l'utilisateur ne veut pas retourner au menu
		if (v != this.btAddSuppr && v != this.btQuit && v != this.btOk)
		{			
			this.displayOrContinue(v);
		}//fin if(retour menu)
		else if (v == this.btQuit || v == this.btOk)
		{
			this.quit(v);
		}
	}//fin méthode

	/**
	 * Gère les évenment en fonction de si l'utilisateur veut afficher les infos du kanji et passser à la question suivante
	 * @param v
	 */
	protected void displayOrContinue(View v)
	{
		if (v == this.btNext)
		{
			this.continuer();
		}
		else
		{
			//si l'utilisateur clique sur le kanji, lui afficher le numéro de ce dernier à condition qu'il ait déjà répondu
			if (v == this.btKanji && MainTrainingActivity.repOk)
			{
				affichePopUp(MainQuizzesActivity.random);
			}
			else
			{
				if (v == this.btKanji || v == this.btRep)
				{
					this.display();
				}
			}
		}
	}

	/**
	 * Affiche selon que ce soit la dernière quesion, bouton next ou de fin
	 */
	protected void display()
	{

		MainTrainingActivity.repOk = true;
		if (!MainTrainingActivity.isLastKanji)
		{
			this.btNext.setVisibility(android.view.View.VISIBLE);
		}
		else
		{
			this.btNext.setVisibility(android.view.View.GONE);
			this.btOk.setVisibility(android.view.View.VISIBLE);
		}
	}

	/**
	 * Gère le retour du quizz à la fin de ce dernier ou si demande de retour à l'acceuil
	 * @param v : le bouton pressé
	 */
	protected void quit(View v)
	{
		//La passerelle vers la vue/activitée
		Intent intent;

		if (v == this.btOk)
		{
			MainQuizzesActivity.setEnd();
			if (MainQuizzesActivity.type.compareToIgnoreCase("chap") == 0)
				intent = new Intent(MainTrainingActivity.this, ChapitreActivity.class);
			else
				intent = new Intent(MainTrainingActivity.this, IntervalleActivity.class);

			//On démarre l'activité
			startActivity(intent);
			finish();
		}
		else
		{				
			this.handleQuit();
		}
	}


}
