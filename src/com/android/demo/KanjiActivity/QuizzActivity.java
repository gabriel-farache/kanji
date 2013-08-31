package com.android.demo.KanjiActivity;

import com.android.demo.KanjiActivity.R;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;


/**
 * Classe/vue correspondant au quizz : affichage du kanji et saisie de la réponse par l'utlisateur
 * @author Gabriel
 *
 */
public class QuizzActivity extends MainQuizzesActivity
{
	/**
	 * Le bouton du kanji
	 */
	private Button btKanji;

	/**
	 * Le chornométre du quizz
	 */
	private static Chronometer chrono;

	/**
	 * L'image du kanji a afficher
	 */
	private Drawable kanjiImg;

	/**
	 * EditText dans laquelle l'utlisateur met sa réponse
	 */
	private EditText util;
	
	/**
	 *Inqiue si l'utilisateur a déjà répondu une fois  
	 */
	private static boolean hasAlreadyAnswer;
	
	/**
	 * Méthode appelée é la création de la vue
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//afficher le layout comme définit dans boutons_kanjis.xml
		setContentView(R.layout.boutons_kanjis);

		//ATTENTION CHAP VAUT [1, 55] OR UN TABLEAU COMMENCE A 0 !!!
		//si on vient pour le premiére fois
		if (MainQuizzesActivity.pass == 0)
		{
			this.firstTime();
			QuizzActivity.setHaveAlreadyAnswer(false);

		}//fin if(premier passage)		
		else
		{
			//on ne fait éa que si on est passé dans verifiyActivity avant sinon, on fait juste un *2 de get base puisque pause == 0 !!!
			//met le chrono é jour du temps "perdu" dans la correction
			QuizzActivity.chrono.setBase(QuizzActivity.chrono.getBase() + SystemClock.elapsedRealtime() - VerifieActivity.getPause());
		}

		this.displayQuizz();
		
	}//fin méthode

	protected void init()
	{
		super.init();
		
		//recuperer l'image-button du kanji
		this.btKanji = (Button) findViewById(R.id.k1);
		//mettre la bonne image et le rendre visible			
		this.kanjiImg = getResources().getDrawable((Integer) MainQuizzesActivity.kanjisBouton.get(MainQuizzesActivity.random));			
		this.btKanji.setBackgroundDrawable(kanjiImg);
		this.btKanji.setVisibility(android.view.View.VISIBLE);


		this.util = (EditText) findViewById(R.id.util);
		this.util.setImeOptions (EditorInfo.IME_ACTION_DONE);
		
		//Initialisation du chrono
		QuizzActivity.chrono = new Chronometer(this);
		QuizzActivity.chrono.setBase(SystemClock.elapsedRealtime());
		//Start du chrono
		QuizzActivity.startChrono();		
	}

	/**
	 * Listener des boutons.
	 * Determine l'action é effectuer (ici appel d'une autre vue) lorsqu'un bouton est pressé.
	 */
	public void onClick(View v)
	{
		//La passerelle vers la vue/activitée
		Intent intent;

		this.checkBtAddSuppr(v);
		//si l'utilisateur ne veut pas retourner au menu
		if (v != this.btQuit && v != this.btAddSuppr)
		{			
			//Le texte rentré par l'uilisteur en guise de réponse
			String rep = this.util.getText().toString();

			intent = new Intent(QuizzActivity.this, VerifieActivity.class);
			/*On stock les informations dont aura besoin VerifieActivity pour fonctionner :
			 *  la réponse de l'ulisateur
			 *  la correction
			 *  la valeur du kanji dans R (pour pouvoir l'afficher dans VerifieActivity)
			 *  le nombre de kanji déjé passé (pour savoir quand mettre le recapitulatif)
			 *  le nombre de points
			 */
			intent.putExtra("util", rep);
			intent.putExtra("correction", ChoiceChapInterActivity.getBundle().getString(MainQuizzesActivity.random.toString()));
			intent.putExtra("idRKanji", (Integer) MainQuizzesActivity.kanjisBouton.get(MainQuizzesActivity.random));
			intent.putExtra("nbKanjiFait",MainQuizzesActivity.getNbKanjiFait());
			intent.putExtra("pts",MainQuizzesActivity.pts);
			intent.putExtra("numKanji", MainQuizzesActivity.random);
			MainQuizzesActivity.nextActivate = false;
			//On démarre l'activité
			startActivity(intent);
			finish();
		}//fin if(retour menu)
		else if(v == this.btQuit)
		{		
			QuizzActivity.hasAlreadyAnswer = false;
			this.handleQuit();
		}

	}//fin méthode

	/**
	 * Stop le chrono
	 */
	public static void stopChrono()
	{
		QuizzActivity.chrono.stop();
	}//fin méthode

	/**
	 * Démarre le chrono
	 */
	public static void startChrono()
	{
		QuizzActivity.chrono.start();
	}//fin méthode

	/**
	 * @return le chornométre du quizz
	 */
	public static Chronometer getChrono()
	{
		return QuizzActivity.chrono;
	}//fin méthode
	
	public static void setHaveAlreadyAnswer(boolean bool)
	{
		QuizzActivity.hasAlreadyAnswer = bool;
	}
	
	public static boolean getHaveAlreadyAnswer()
	{
		return (QuizzActivity.hasAlreadyAnswer);
	}


}//fin classe