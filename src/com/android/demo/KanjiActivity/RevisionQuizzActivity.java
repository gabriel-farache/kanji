package com.android.demo.KanjiActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import com.android.demo.KanjiActivity.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * Classe/vue correspondant à la révision de kanji : affichage des kanjis marqué
 * @author Gabriel
 *
 */
public class RevisionQuizzActivity extends MainTrainingActivity
{	
	/**
	 * L'indice courant du kanji afficher en révision
	 */
	private static int indiceListRev;

	/**
	 * Initialisation du quizz.
	 * N'est réalisé qu'une fois par partie (au début).
	 */
	protected void firstTime()
	{
		//indiquer qu'on a déjà fait l'initialisation
		MainQuizzesActivity.pass = 1;
		
		/*
		 * Ré-initiliser les préférences pour montrer/cacher le pop-up de confirmation
		 * pour ajouter/supprimer kanji dans la liste de révsion 
		*/
		SharedPreferences settings = getSharedPreferences(PREFS_NAME_SUPPR, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("skipMessage", "NOT checked");
		editor.commit();

		settings = getSharedPreferences(PREFS_NAME_ADD, 0);
		editor = settings.edit();
		editor.putString("skipMessage", "NOT checked");
		editor.commit();

		//Initiliser la liste contenant les kanji a réviser. N'est fait qu'une seule fois.
		if (MainTrainingActivity.listRev == null)
		{
			MainTrainingActivity.listRev = new ArrayList<Integer>();
			this.createListRev();
		}

		
		//Création de l'assocation pour les kanjis déjé passé
		MainQuizzesActivity.kanjiFait = new HashMap<Integer, Integer>();
		MainQuizzesActivity.nextActivate = false;
		//On créer l'association num kanji - valeur en mémoire dans R
		MainQuizzesActivity.kanjisBouton = AccueilActivity.getKanjisBouton();
		RevisionQuizzActivity.indiceListRev = 0;
		MainQuizzesActivity.nbKanjiTotChap = MainTrainingActivity.listRev.size();

	}

	/**
	 * Affiche le quizz
	 */
	protected void displayQuizz()
	{
		this.init();
		this.displayInfos();
	}
	
	/**
	 * Initialise la mise en forme et les boutons du quizz
	 */
	protected void init()
	{
		TextView title = (TextView) findViewById(R.id.title);
		MainQuizzesActivity.nbKanjiTotChap = MainTrainingActivity.listRev.size();
		MainQuizzesActivity.nextActivate = true;

		title.setText("Revisions");	
		MainQuizzesActivity.setRandom(RevisionQuizzActivity.listRev.get(RevisionQuizzActivity.indiceListRev));

		//recuperer l'image-button du kanji
		this.btKanji = (Button) findViewById(R.id.k1);
		//mettre la bonne image et le rendre visible	
		this.btKanji.setBackgroundResource(MainQuizzesActivity.kanjisBouton.get(MainQuizzesActivity.random));
		this.btKanji.setVisibility(android.view.View.VISIBLE);
		this.btKanji.setOnClickListener(this);

		//idem avec le bouton de validation
		this.btNext = (ImageButton) findViewById(R.id.next);
		//on met le listener
		this.btNext.setOnClickListener(this);

		//idem avec le bouton de reponse
		this.btRep = (Button) findViewById(R.id.rep);
		this.btRep.setText(ChoiceChapInterActivity.getBundle().getString(MainQuizzesActivity.random.toString()));

		RevisionQuizzActivity.repOk = false;			//
		RevisionQuizzActivity.isLastKanji = false;		//

		this.initButtons();
		
		MainQuizzesActivity.kanjiFait.put(MainQuizzesActivity.random, 1);

	}
	
	/**
	 * Affiche les informations de progression (points et kanji vus)
	 */
	protected void displayInfos()
	{
		//TextView pour afficher le nombre de points et le nombre de kanji déjé fait
		TextView nbKTxt = (TextView) findViewById(R.id.kanji);

		ProgressBar pBarKanji = (ProgressBar) findViewById(R.id.ProgressBarKanji);

		//affichage des points et du nombre de kanjis fait/le nombre total
		int nbKanjiFait = MainQuizzesActivity.kanjiFait.size();
		double progressKanji = pBarKanji.getMax()*(((double)nbKanjiFait/(double)MainQuizzesActivity.nbKanjiTotChap));

		nbKTxt.setText("Kanji :\t"+nbKanjiFait+" / "+MainQuizzesActivity.nbKanjiTotChap);		
		pBarKanji.setProgress((int)progressKanji);
	}

	/**
	 * Permet de passer à la question suivante
	 */
	protected void continuer()
	{
		//La passerelle vers la vue/activitée
		Intent intent;

		intent = new Intent(RevisionQuizzActivity.this, RevisionQuizzActivity.class);
		MainQuizzesActivity.nextActivate = false;
		//on indique que l'on à pas encore répondu 
		RevisionQuizzActivity.repOk = false;
		//on passe donc au kanji suivant dans la liste
		RevisionQuizzActivity.indiceListRev++;
		//On démarre l'activité
		startActivity(intent);
		finish();
	}

	/**
	 * Gère la fin du quizz ou le retour à l'acceuil
	 */
	protected void quit(View v)
	{
		//La passerelle vers la vue/activitée
		Intent intent;

		if (v == this.btOk)
		{
			MainQuizzesActivity.setEnd();
			intent = new Intent(RevisionQuizzActivity.this, AccueilActivity.class);
			//On démarre l'activité
			startActivity(intent);
			finish();
		}
		else
		{				
			this.handleQuit();
		}
	}
	
	/**
	 * Supprime le kanji courant de la liste de revision
	 * @param file : le fichier
	 * @return true si succès, false sinon 
	 * 
	 */
	public boolean supprKanjiList(File fileName) 
	{
		boolean result = super.supprKanjiList(fileName);
		
		//si la suppression s'est bien passé
		if(result)
		{
			//mettre à jour les infos
			//nombre de kanji fait - 1 car on vient d'en supprimer un de la liste donc la nbKaniTot aussi
			MainQuizzesActivity.kanjiFait.remove(MainQuizzesActivity.random);
			//l'indice ne doit pas bouger lors de la question suivante donc on fait -1 ici
			RevisionQuizzActivity.indiceListRev--;
		}
		return result;
	}
}//fin classe