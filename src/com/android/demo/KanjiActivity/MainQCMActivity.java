package com.android.demo.KanjiActivity;

import java.util.ArrayList;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public abstract class MainQCMActivity extends MainQuizzesActivity
{


	/**
	 * Le bouton de reponse
	 */
	protected Button btRep;


	/**
	 * Le bouton de validation
	 */
	protected ImageButton btNext;


	/**
	 * L'image du kanji a afficher
	 */
	protected Drawable kanjiImg;

	/**
	 * Tous les boutons utilisés
	 */
	protected ArrayList<Button> tabButtons;

	protected static ArrayList<Integer> errAnswers;

	protected static boolean dejaRep;			//indique si l'utilisateur a deja repondu
	protected String rep;
	protected Integer placement;
	protected boolean trouve;



	protected static boolean isLastKanji = false;

	protected abstract void handleNext();

	protected abstract void initQCM();

	/**
	 * Méthode appelée é la création de la vue
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if(this instanceof QCMQuizzActivity)
		{
			//afficher le layout comme définit dans boutons_kanjis.xml
			setContentView(R.layout.qcm_boutons_kanjis);
		}
		else
		{
			//afficher le layout comme définit dans boutons_kanjis.xml
			setContentView(R.layout.signi_qcm_boutons_kanjis);
		}

		//ATTENTION CHAP VAUT [1, 55] OR UN TABLEAU COMMENCE A 0 !!!
		//si on vient pour le premiére fois
		if (MainQuizzesActivity.pass == 0)
		{
			this.firstTime();
		}//fin if(premier passage)		

		this.displayQuizz();

		if (MainQuizzesActivity.kanjiFait.size() >= MainQuizzesActivity.nbKanjiTotChap)
		{
			MainQCMActivity.isLastKanji = true;
			this.btNext.setVisibility(android.view.View.GONE);
			//this.btOk.setVisibility(android.view.View.VISIBLE);
		}
	}//fin méthode

	protected void firstTime()
	{
		super.firstTime();

		//on initialise le tableau d'erreurs
		MainQCMActivity.errAnswers = new ArrayList<Integer>();	//

		MainQCMActivity.dejaRep = false;		//
		MainQCMActivity.isLastKanji = false;	//
	}

	protected void init()
	{
		super.init();	
		this.initButtons();
		this.initQCM();
	}

	protected void displayRep()
	{
		this.tabButtons.get(placement).setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.circle),null);
		this.tabButtons.get((placement+1)%4).setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.cross),null);
		this.tabButtons.get((placement+2)%4).setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.cross),null);
		this.tabButtons.get((placement+3)%4).setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.cross),null);

		if (!MainQCMActivity.isLastKanji)
		{
			this.btNext.setVisibility(android.view.View.VISIBLE);
		}
		else
		{
			this.btOk.setVisibility(android.view.View.VISIBLE);
		}
	}

	/**
	 * Listener des boutons.
	 * Determine l'action é effectuer (ici appel d'une autre vue) lorsqu'un bouton est pressé.
	 */
	public void onClick(View v)
	{		
		this.checkBtAddSuppr(v);
		//si l'utilisateur ne veut pas retourner au menu
		if (v != this.btQuit && v != this.btOk && v != this.btAddSuppr)
		{			
			if (v == this.btNext)
			{
				this.handleNext();
			}
			else
			{
				this.handleRep(v);
				MainQuizzesActivity.score = ((float)MainQuizzesActivity.pts/((float)MainQuizzesActivity.kanjiFait.size()))*100;
				this.displayInfos();
			}

		}//fin if(bt != retour menu || ok)
		else if(v == this.btOk)
		{

			this.handleOk();
		}
		else if (v == this.btQuit)
		{				
			this.handleQuit();
		}



	}//fin méthode
	private void correction(View v)
	{
		int i;

		//récupérer le bouton sur lequel on a cliqué
		for (i = 0; this.tabButtons.get(i) != v && i < 4; i++){}

		if (this.rep.compareToIgnoreCase((String)this.tabButtons.get(i).getText()) == 0)
		{
			this.tabButtons.get(i).setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.circle),null);
			if (MainQCMActivity.dejaRep == false)
			{
				MainQuizzesActivity.pts++;
			}

			this.trouve = true;
		}
		else
		{
			this.tabButtons.get(i).setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.cross),null);
		}
	}

	private void handleOk()
	{
		MainQuizzesActivity.setEnd();
		Intent intent = new Intent(MainQCMActivity.this, RecapErrorsActivity.class);
		intent.putExtra("typeQCM", 1);
		intent.putExtra("pageCour", 1);
		intent.putIntegerArrayListExtra("errAnswers", MainQCMActivity.errAnswers);
		//On démarre l'activité
		startActivity(intent);
	}
	private void handleRep(View v)
	{
		this.correction(v);

		//si on n'a pas la bonne solution et qu'on a jamais tenter, on met celui qu'il fallait trouver dans les erreurs
		if(!this.trouve && !MainQCMActivity.dejaRep)
		{
			MainQCMActivity.errAnswers.add(MainQuizzesActivity.random);	
		}

		//on indique que l'utilisateur a déjà donnée une réponse
		MainQCMActivity.dejaRep = true;

		//si c'est la dernière question, on affiche le bouton "ok" et pas le "next"
		if (!MainQCMActivity.isLastKanji)
		{
			this.btNext.setVisibility(android.view.View.VISIBLE);
		}
		else
		{
			this.btNext.setVisibility(android.view.View.GONE);
			this.btOk.setVisibility(android.view.View.VISIBLE);
		}
	}


}
