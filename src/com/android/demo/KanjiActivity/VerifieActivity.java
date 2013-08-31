package com.android.demo.KanjiActivity;

import com.android.demo.KanjiActivity.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Classe/vue vérifiant la validité de la réponse de l'utilisteur. 
 * C'est aussi elle qui vérifie si le quizz est finis
 * @author Gabriel
 *
 */
public class VerifieActivity extends Activity implements View.OnClickListener
{

	/**
	 * Bouton de fin (si fin du quizz sinon ne s'affiche pas)
	 */
	private Button end;

	/**
	 * Bouton next (si la quizz continue)
	 */
	private ImageButton next;

	/**
	 * Le nombre de kanji déjé fait
	 */
	private int nbKanjiFait;

	/**
	 * Nombre de points de l'utilisateur
	 */
	private int pts;

	/**
	 * Le kanji en cours
	 */
	private Button kanji;

	/*
	 * La pause que l'utilisateur fait au moment de la correction (pour le chorno)
	 */
	private static long pause;

	private Button correction;
	private Button repUtil;
	private Button repCorr;
	private String rep;
	private String corr;
	private int numKanji;
	/**
	 * Méthode appelée é la création de la vue
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		this.init();

		this.correction();

		//S'il reste des kanjis é faire
		if (this.nbKanjiFait < MainQuizzesActivity.getNbKanjiTotChap())
		{
			//Mettre un listener sur le bouton next
			this.next.setOnClickListener(this);			
		}
		//sinon, le diriger vers la page de fin
		else
		{
			this.prepareEnd();

		}//fin if-else 02


	}//fin méthode

	private void init()
	{
		//On s'arréte é ce moment lé, donc on stock ce temps
		VerifieActivity.pause = SystemClock.elapsedRealtime();
		//On arréte le chrono du quizz
		QuizzActivity.stopChrono();

		//On récupére l'intent (pour nous permettre de récupérer les infos transmise par l'activité précedente)
		Intent thisIntent = getIntent();
		//On affiche la vue comme défini dans verif_display.xml
		setContentView(R.layout.verif_display);

		/*Récupération des différents boutons*/
		this.correction = (Button) findViewById(R.id.okKo);
		this.repUtil = (Button) findViewById(R.id.repUtil);
		this.repCorr = (Button) findViewById(R.id.repCorr);

		/*Récupéartion des information transmise par l'actitvité précente (ici QuizzActivity)*/
		//La réponse de l'utilisateur
		this.rep = thisIntent.getExtras().getString("util").replaceAll("[\r\n]+", "").trim();
		//La correction
		this.corr = thisIntent.getExtras().getString("correction").replaceAll("[\r\n]+", "").trim();
		//La valeur du kanji en mémoire (dans R)
		int idRKanji = thisIntent.getExtras().getInt("idRKanji");
		this.numKanji = thisIntent.getExtras().getInt("numKanji");;
		//Le nombre de kanji fait
		this.nbKanjiFait = thisIntent.getExtras().getInt("nbKanjiFait");
		//Le nombre de point de l'utilisateur
		this.pts = thisIntent.getExtras().getInt("pts");

		//Récupération du bouton du kanji et de next
		this.kanji = (Button) findViewById(R.id.kanjiCorr);
		this.next = (ImageButton) findViewById(R.id.next);

		//Mettre la kanji en background ud bouton du kanji (afficher le kanji)
		this.kanji.setBackgroundDrawable(getResources().getDrawable(idRKanji));
		//Lui montrer sa réponse et la bonne réponse
		this.repUtil.setText("Votre reponse : "+System.getProperty("line.separator")+this.rep);
		this.repCorr.setText("Bonne reponse : "+System.getProperty("line.separator")+this.corr);
		//Mettre un listener sur la kanji (pour afficher la petite histoire)		
		this.kanji.setOnClickListener(this);
	}


	private void correction()
	{
		String noCount = "";
		
		//Vérifier sa réponse
		if(this.rep.compareToIgnoreCase(this.corr) == 0)
		{
			//Si c'est bon, on lui donne un point...
			//si l'utilisateur n'a jamais validé sa réponse ava,t cette fois, on compte les points
			if(!QuizzActivity.getHaveAlreadyAnswer())
			{
				MainQuizzesActivity.setPts((pts+1));
				QuizzActivity.setHaveAlreadyAnswer(true);
			}
			else
			{
				noCount = " (Non comptabilisé car déjà répondu)";
			}
			MainQuizzesActivity.setScore(((float)MainQuizzesActivity.pts/((float)MainQuizzesActivity.getNbKanjiFait()))*100);
			//...et on luis dit qu'il a la bonne réponse.
			this.correction.setText("Bonne Reponse !"+noCount);
			this.correction.setTextColor(android.graphics.Color.GREEN);			
		}
		//Sinon...
		else
		{
			//... lui dire qu'il a faux
			this.correction.setText("Mauvaise Reponse !");
			this.correction.setTextColor(android.graphics.Color.RED);
		}//fin if-else 01

	}

	private void prepareEnd()
	{
		//Récupérer le bouton de fin
		this.end = (Button) findViewById(R.id.end);
		//Prevenir QuizzActvity que c'est la fin
		QuizzActivity.setEnd();

		//Faire partir le bouton next de la vue et le remplacer par le bouton de fin
		this.next.setVisibility(android.view.View.GONE);
		this.end.setVisibility(android.view.View.VISIBLE);
		//Mettre un listener sur le bouton de fin
		this.end.setOnClickListener(this);
	}

	/**
	 * Listener des boutons.
	 * Determine l'action é effectuer (ici appel d'une autre vue) lorsqu'un bouton est pressé.
	 */
	public void onClick(View v)
	{
		//Si l'utilisateur a cliqué sur le kanji, on lui donne la petite histoire (Not Yet Implemented)
		if (v == this.kanji)
		{
			this.displayKanjiInfos();
		}
		//Sinon, on fait selon le bouton préssé
		else
		{
			//Si on a cliqué sur next
			if (v == this.next)
			{
				this.handleNext();
				QuizzActivity.setHaveAlreadyAnswer(false);
			}
			//Si c'est la fin du quizz, c'est le bouton end qui apparaét 
			else
			{
				this.handleEnd();
				QuizzActivity.setHaveAlreadyAnswer(false);
			}

		}
	}//fin méthode

	private void displayKanjiInfos()
	{
		//Récupérer le message (il faut le récupérer en fonction du numéro du kanji ==> il faudrait que QuizzActivity le transmette)
		String mess = ChoiceChapInterActivity.getBundle().getString("mess");
		//Afficher le message dans un pop-up
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("La petite histoire :\n"+mess+".")
		.setTitle("Kanji n°"+this.numKanji)
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

	private void handleNext()
	{
		Intent intent = new Intent(VerifieActivity.this, QuizzActivity.class);
		startActivity(intent);
		finish();

	}

	private void handleEnd()
	{
		Intent intent = new Intent(VerifieActivity.this, RecapActivity.class);
		//On Stock les infos : ici le nombre de points de l'utilisateur
		intent.putExtra("pts", QuizzActivity.getPts());
		startActivity(intent);
		finish();
	}
	/**
	 * 
	 * @return le temps de pause (temps lorsqu'on est entré dans la classe)
	 */
	public static long getPause()
	{
		return VerifieActivity.pause;
	}//fin méthode

}//fin classe