package com.android.demo.KanjiActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class principal pour les quizz.
 * Toutes les autres class principal (QCM et Training) en hérite
 * @author Gabriel
 *
 */
public abstract class MainQuizzesActivity extends Activity  implements View.OnClickListener
{

	/**
	 * La liste des kanjis a reviser 
	 */
	protected static ArrayList<Integer> listRev = null;
	/**
	 * Pour verifier si checkbox cocher pour ajout liste revision
	 */
	public CheckBox dontShowAgain;

	/**
	 * Nom du fichier de preférence pour le bouton ADD
	 */
	public static final String PREFS_NAME_ADD = "PrefsFileADD";

	/**
	 * Nom du fichier de preférence pour le bouton SUPPR
	 */
	public static final String PREFS_NAME_SUPPR = "PrefsFileSUPPR";

	/**
	 * Le bouton de ajoute liste revision
	 */
	protected Button btAddSuppr;

	/**
	 * Le n° du 1er kanji du quizz en cours
	 */
	private static int first;

	/**
	 * Le n°du dernier kanji du quizz en cours
	 */
	private static int last;

	/**
	 * Indicateur de fin du quizz
	 */
	protected static int pass;

	/**
	 * Le seuil de fin des kanji du quizz en cours
	 */
	private static int seuilFin;

	/**
	 * Les kanji faits (key : numéro du kanji, Values : Don't Care) durant le quizz
	 */
	protected static HashMap<Integer, Integer> kanjiFait;

	/**
	 * Le seuil de début des kanji du quizz en cours
	 */
	private static int seuilDeb;

	/**
	 * Le numéro aléatoire du kanji
	 */
	protected static Integer random;

	/**permet de garder les infos bonnes si jamais l'écran est tourné
	 * 
	 */
	protected static boolean nextActivate;	

	/**
	 * Le numéro du chapitre du quizz ((si "Chapitre choisit)
	 */
	protected static Integer chap;

	/**
	 * Le type choisit ("Intervalle" ou "Chapitre")
	 */
	protected static String type;

	/**
	 * Correspondance numéro kanji - valeur en mémoire du kanji (dans R)
	 */
	protected static HashMap<Integer, Integer> kanjisBouton;

	/**
	 * Le nombre totale de kanji dans le chapitre du quizz en cours
	 */
	protected static int nbKanjiTotChap;

	/**
	 * Le nombre de point pour un quizz
	 */
	protected static int pts;

	/**
	 * Le numéro du 1er kanji du chapitre suivant (si "Chapitre choisit)
	 */
	protected int nextChap;

	/**
	 * Le numéro du 1er kanji du chapitre en cours (si "Chapitre choisit)
	 */
	protected int currChap;

	/**
	 * Le bouton de retour au menu
	 */
	protected Button btQuit;

	/**
	 * Le bouton de validation
	 */
	protected ImageButton btOk;

	protected static float score;

	/**
	 * Initialisation du quizz.
	 * N'est réalisé qu'une fois par partie (au début).
	 */
	protected void firstTime()
	{
		//Récupérer passerelle avec autres activities
		Intent thisIntent = getIntent();
		//On siginifie que le quizz a commencé
		MainQuizzesActivity.pass = 1;
		MainQuizzesActivity.nextActivate = false;
		//On récupére le type choisit
		MainQuizzesActivity.type = thisIntent.getExtras().getString("type");
		//initialisation des points
		MainQuizzesActivity.pts = 0;
		MainQuizzesActivity.score = 0.0F;
		//On créer l'association num kanji - valeur en mémoire dans R
		MainQuizzesActivity.kanjisBouton = AccueilActivity.getKanjisBouton();

		//Création de l'assocation pour les kanjis déjé passé
		MainQuizzesActivity.kanjiFait = new HashMap<Integer, Integer>();		

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

		//Si l'utilisateur a choisit "Chapitre"
		if (MainQuizzesActivity.type.compareToIgnoreCase("chap") == 0)
		{			
			this.initChap(thisIntent);
		}
		//Sinon
		else
		{
			this.initInter(thisIntent);
		}//fin if- else

		//Calculer le nombre total de kanji du chapitre
		MainQuizzesActivity.nbKanjiTotChap = MainQuizzesActivity.seuilFin - MainQuizzesActivity.seuilDeb;
		MainQuizzesActivity.first = MainQuizzesActivity.seuilDeb;
		MainQuizzesActivity.last = MainQuizzesActivity.seuilFin;
	}	

	/**
	 * Initialise les variable seuil debut et fin et autres variables de chapitre si appellé depuis ChapitreActivity
	 */
	protected void initChap(Intent thisIntent)
	{
		//Récupérer le numéro du chapitre
		MainQuizzesActivity.chap = thisIntent.getExtras().getInt("chap");

		//Récupérer dans le bundle les numéros des 1er kanji du chapitre en cours et du chapitre suivant
		this.nextChap= Integer.parseInt(ChoiceChapInterActivity.getBundle().getString("C"+((Integer)(MainQuizzesActivity.chap+1)).toString()));
		this.currChap = Integer.parseInt(ChoiceChapInterActivity.getBundle().getString("C"+MainQuizzesActivity.chap.toString()));

		//Récupérer les seuils de début et de fin. Correspondent aux valeurs des 1ers kanji du chapitre courant et du chapitre suivant
		MainQuizzesActivity.seuilDeb = this.currChap;
		MainQuizzesActivity.seuilFin = this.nextChap;


	}

	/**
	 * Initialise les variable seuil debut et fin si appellé depuis IntervalleActivity
	 */
	protected void initInter(Intent thisIntent)
	{
		//Récupérer les intervalle de début et de fin choisit par l'utilisateur
		MainQuizzesActivity.seuilDeb = thisIntent.getExtras().getInt("intervalleDeb");
		MainQuizzesActivity.seuilFin = thisIntent.getExtras().getInt("intervalleFin");

	}

	/**
	 * Affiche le quizz
	 */
	protected void displayQuizz()
	{
		this.createRandomKanji();
		this.init();
		this.displayInfos();
	}

	/**
	 * Initialise la mise en forme et les boutons du quizz
	 */
	protected void init()
	{
		ProgressBar pBarKanji = (ProgressBar) findViewById(R.id.ProgressBarKanji);
		ProgressBar pBarPts = (ProgressBar) findViewById(R.id.ProgressBarScore);
		TextView title = (TextView) findViewById(R.id.title);

		if (MainQuizzesActivity.type.compareToIgnoreCase("chap") == 0)
		{			
			title.setText("Chapitre "+MainQuizzesActivity.chap+" : "+MainQuizzesActivity.first+" à "+(MainQuizzesActivity.last-1));

		}
		//Sinon
		else
		{
			title.setText("Kanji " +MainQuizzesActivity.first+ " à " + (MainQuizzesActivity.last-1));

		}//fin if- else

		//on met la taille de la bar en 100 pour pouvoir afficher les progressions plus simplement
		pBarKanji.setMax(100);
		if(pBarPts != null)
			pBarPts.setMax(100);

		//On signifie que la question a été créé pour garder les infos si l'écran est tourné
		MainQuizzesActivity.nextActivate = true;
		//initiliser les boutons principaux
		this.initButtons();
	}

	/**
	 * Initialise les boutons communs à tous les quizz
	 */
	protected void initButtons()
	{
		//Recupération du bouton de retour au menu
		this.btQuit = (Button) findViewById(R.id.menu);
		//on met le listener
		this.btQuit.setOnClickListener(this);
		//on met en sur-brillance rouge le fond lorsque l'utilisateur clique sur le bouton
		this.btQuit.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getActionMasked();
				//quand l'utlisateur appuie, on met en rouge
				if(action == MotionEvent.ACTION_DOWN)
				{     	
					btQuit.setBackgroundColor(android.graphics.Color.parseColor("#990000"));	                
				}
				//sinon en gris (comme le background)
				else
				{
					btQuit.setBackgroundColor(android.graphics.Color.parseColor("#8F8F8D"));
				}

				return false;
			}			
		});

		this.btOk = (ImageButton) findViewById(R.id.ok);
		this.btOk.setOnClickListener(this);

		this.btAddSuppr = (Button) findViewById(R.id.addSuppr);

		if(MainQuizzesActivity.random != null && this.containsKanji(MainQuizzesActivity.random))
		{
			this.btAddSuppr.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
		}		

		this.btAddSuppr.setOnClickListener(this);
	}

	/**
	 * Affiche les informations de progression (points et kanji vus)
	 */
	protected void displayInfos()
	{
		//TextView pour afficher le nombre de points et le nombre de kanji déjé fait
		TextView ptsTxt = (TextView) findViewById(R.id.pts);
		TextView nbKTxt = (TextView) findViewById(R.id.kanji);
		//Basr de progressions
		ProgressBar pBarKanji = (ProgressBar) findViewById(R.id.ProgressBarKanji);
		ProgressBar pBarPts = (ProgressBar) findViewById(R.id.ProgressBarScore);

		//affichage des points et du nombre de kanjis fait/le nombre total
		int nbKanjiFait = MainQuizzesActivity.kanjiFait.size();
		double progressKanji = pBarKanji.getMax()*(((double)nbKanjiFait/(double)MainQuizzesActivity.nbKanjiTotChap));

		//Dans certain quizz, il n'y a pas de scors (training) donc ne pas essayer d'afficher le score
		if(ptsTxt != null)
		{
			ptsTxt.setText("Score :\t"+MainQuizzesActivity.score+"%");
			pBarPts.setProgress((int)score);
		}
		//mise à jour de la progression des kanjis vus		
		nbKTxt.setText("Kanji :\t"+nbKanjiFait+" / "+MainQuizzesActivity.nbKanjiTotChap);		
		pBarKanji.setProgress((int)progressKanji);
	}

	/**
	 * Créer un nombre aléatoire compris entre le seuil min et le seuil max du quizz en cours
	 */
	protected void createRandomKanji()
	{
		if(!MainQuizzesActivity.nextActivate)
		{
			//en prendre un au hasard qui n'est pas déjé passé
			do
			{
				//le nombre random (borne min inclus, borne max exclut)
				MainQuizzesActivity.random = MainQuizzesActivity.seuilDeb + (new Random()).nextInt(MainQuizzesActivity.seuilFin - MainQuizzesActivity.seuilDeb);
			}while(MainQuizzesActivity.kanjiFait.containsKey(MainQuizzesActivity.random));

			//mettre le kanji qu'on a fait dans la liste a exclure
			MainQuizzesActivity.kanjiFait.put(MainQuizzesActivity.random, 1);

			//on réduit le champs du random si les extrémités sont déjé passé
			if (MainQuizzesActivity.random == MainQuizzesActivity.seuilDeb && MainQuizzesActivity.seuilDeb+1 < MainQuizzesActivity.seuilFin)
			{
				do
				{
					MainQuizzesActivity.seuilDeb++;
				}while(MainQuizzesActivity.kanjiFait.containsKey(MainQuizzesActivity.seuilDeb) && MainQuizzesActivity.seuilDeb+1 < MainQuizzesActivity.seuilFin);
			}
			else if (MainQuizzesActivity.random == MainQuizzesActivity.seuilFin-1 && MainQuizzesActivity.seuilDeb < MainQuizzesActivity.seuilFin-2)
			{
				do
				{
					MainQuizzesActivity.seuilFin--;
				}while(MainQuizzesActivity.kanjiFait.containsKey(MainQuizzesActivity.seuilFin-1) && MainQuizzesActivity.seuilDeb < MainQuizzesActivity.seuilFin-2);
			}
		}
	}

	/**
	 * Pour compléter onClick. Permet de faire le traitement pour l'ajout/suppression de kanji dans la liste de révisions
	 * @param v
	 */
	protected void checkBtAddSuppr(View v)
	{
		if(v == this.btAddSuppr)
		{
			//ecrit/supprime le kanji dans le fichier si l'utilisateur le demande
			//Supprime si le kanji est déjà dans la liste, l'écrit sinon
			if(this.containsKanji(MainQuizzesActivity.random))
			{
				//demander confirmation et supprimer
				this.checkSupprList();
			}
			else
			{
				//demander confirmation et ajouter
				this.checkAddList();
			}
		}
	}

	/**
	 * Gère le retour à l'acceuil
	 */
	protected void handleQuit()
	{
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage("Voulez-vous retournez à l'accueil ? (vous perdrez votre avancement)");
		builder.setCancelable(false);
		builder.setTitle("Confirmation");

		builder.setPositiveButton("OUI",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(MainQuizzesActivity.this, AccueilActivity.class);
				MainQuizzesActivity.setEnd();
				//On démarre l'activité
				startActivity(intent);
				finish();

			}
		});

		builder.setNegativeButton("NON",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		dialog = builder.create();
		dialog.show();
	}

	/**
	 * Listener du bouton back est pressé.
	 * Ici, il est désactivé et on en informe l'utilisateur.
	 */
	public void onBackPressed()
	{
		Toast.makeText(getBaseContext(),
				"Le bouton back est desactivé sur l'application.",
				Toast.LENGTH_SHORT).show();
		return;
	}//fin méthode


	/**
	 * Met à jours les champs signifiant la fin du quizz
	 */
	public static void setEnd()
	{
		MainQuizzesActivity.pass = 0;
	}//fin méthode

	/**
	 * @return 0 = FIN 1 = PAS FIN
	 */
	public static int getEnd()
	{
		return MainQuizzesActivity.pass;
	}//fin méthode

	/** 
	 * @return le nombre de kanji total du chapitre
	 */
	public static int getNbKanjiTotChap()
	{
		return (MainQuizzesActivity.nbKanjiTotChap);
	}//fin méthode

	/** 
	 * @param pts : le nombre de points
	 */
	public static void setPts(int pts)
	{
		MainQuizzesActivity.pts = pts;
	}//fin méthode

	public static void setScore(float score)
	{
		MainQuizzesActivity.score = score;
	}//fin méthode

	/** 
	 * @return le nombre de points de l'utilisateur
	 */
	public static int getPts()
	{
		return (MainQuizzesActivity.pts);
	}//fin méthode

	/** 
	 * @return le type choisit par l'utilisateur (Chapitre ou Intervalle)
	 */
	public static String getType()
	{
		return(MainQuizzesActivity.type);
	}//fin méthode

	/**
	 * Retourne le nombre de kanij déjà vu dans le quizz en cours
	 * @return nombre de kanji vue dans le quizz en cours
	 */
	protected static int getNbKanjiFait()
	{
		return (MainQuizzesActivity.kanjiFait.size());
	}

	/**
	 * Met à jour le nombre 'random' 
	 * @param rand : le nouveau nomvre
	 */
	public static void setRandom(Integer rand) 
	{
		MainQuizzesActivity.random = rand;		
	}


	/**
	 * Vérifie si la liste contient un kanji
	 * @param numKanji : le numéro du kanji à vérifier
	 * @return true si contient, false sinon
	 */
	protected boolean containsKanji(int numKanji) 
	{
		boolean trouve = false;

		//parcours séquentiel de la liste
		for(int kanji = 0; kanji < MainQuizzesActivity.listRev.size() && !trouve; kanji++)
		{
			if(MainQuizzesActivity.listRev.get(kanji) == numKanji)
			{
				trouve = true;
			}
		}

		return trouve;

	}

	/**
	 * Crée la liste de revision à partir du fichier
	 */
	protected void createListRev()
	{
		try
		{
			//Récupération du fichier et ouverture en lecture
			File fic = new File(Environment.getExternalStorageDirectory(), "ListRev.txt");
			InputStream ips=new FileInputStream(fic); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne = br.readLine();
			//lecture du fichier jusqu'à la fin
			while (ligne != null)
			{
				//ajout du n° du kanji à la liste
				MainQuizzesActivity.listRev.add(Integer.parseInt(ligne.replaceAll("[\r\n]+", "")));
				ligne = br.readLine();
			}

			br.close(); 
		}		
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Affiche un pop-up de confirmation si besoin et ajoute le kanji courant à la liste de révision
	 */
	protected void checkAddList()
	{
		AlertDialog.Builder adb=new AlertDialog.Builder(MainQuizzesActivity.this);
		LayoutInflater adbInflater = LayoutInflater.from(MainQuizzesActivity.this);
		View eulaLayout = adbInflater.inflate(R.layout.checkbox, null);

		//Création de la check box
		dontShowAgain = (CheckBox)eulaLayout.findViewById(R.id.skip);
		dontShowAgain.setChecked(false);
		adb.setView(eulaLayout);
		adb.setTitle("Ajouter kanji");
		adb.setMessage("Êtes-vous sûr de vouloir ajouter ce kanji a la liste de revision ?");
		adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{  
			public void onClick(DialogInterface dialog, int which) {
				String checkBoxResult = "NOT checked";
				if (dontShowAgain.isChecked())  checkBoxResult = "checked";
				SharedPreferences settings = getSharedPreferences(PREFS_NAME_ADD, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("skipMessage", checkBoxResult);	
				// Commit the edits!
				editor.commit();
				//add the kanji to the revision list
				addToList(MainQuizzesActivity.random);
				//update the icon
				btAddSuppr.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
				Toast.makeText(getBaseContext(),
						"Ajout du kanji terminé avec succès.",
						Toast.LENGTH_SHORT).show();
				return;  
			} });

		adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		{  
			public void onClick(DialogInterface dialog, int which) {
				String checkBoxResult = "NOT checked";
				if (dontShowAgain.isChecked())  checkBoxResult = "checked";
				SharedPreferences settings = getSharedPreferences(PREFS_NAME_ADD, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("skipMessage", checkBoxResult);	
				// Commit the edits!
				editor.commit();
				return;  
			} });

		//Verifier si l'utilisateur demande à ce que le pop-up ne s'affiche plus
		SharedPreferences settings = getSharedPreferences(PREFS_NAME_ADD, 0);
		String skipMessage = settings.getString("skipMessage", "NOT checked");
		//si il n'a rien demander, afficher le pop-up
		if (!skipMessage.equalsIgnoreCase("checked") ) 
		{
			adb.show();
		}
		//sinon, ajouter le kanji et changer l'icône ajout/suppression
		else
		{
			this.addToList(MainQuizzesActivity.random);
			this.btAddSuppr.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
		}
	}

	/**
	 * Ajoute un kanji à la liste de revision
	 * @param numKanji : le n° du kanji à ajouter
	 */
	protected void addToList(int numKanji)
	{
		BufferedWriter ficRev;

		try
		{
			//On recupère le fichier dans lequel la liste est stocké
			File fic = new File(Environment.getExternalStorageDirectory(), "ListRev.txt");
			//on crée le bufferWriter pour écrire dans le fichier
			ficRev = new BufferedWriter(new FileWriter(fic, true));
			//on écrit dans le fichier et on ferme
			ficRev.write(numKanji+"\n");
			ficRev.close();
			//on met à jour la liste de revision pour ne pas avoir à ré-ouvrir le fichier
			MainQuizzesActivity.listRev.add(numKanji);

		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}
	
	/**
	 * Affiche un pop-up de confirmation si besoin et supprime le kanji courant de la liste de révision
	 */
	protected void checkSupprList()
	{
		AlertDialog.Builder adb=new AlertDialog.Builder(MainQuizzesActivity.this);
		LayoutInflater adbInflater = LayoutInflater.from(MainQuizzesActivity.this);
		View eulaLayout = adbInflater.inflate(R.layout.checkbox, null);
		
		//Création de la check box
		dontShowAgain = (CheckBox)eulaLayout.findViewById(R.id.skip);
		dontShowAgain.setChecked(false);
		adb.setView(eulaLayout);
		adb.setTitle("Ajouter kanji");
		adb.setMessage("Êtes-vous sûr de vouloir supprimer ce kanji a la liste de revision ?");
		adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{  
			public void onClick(DialogInterface dialog, int which) {
				String checkBoxResult = "NOT checked";
				if (dontShowAgain.isChecked())  checkBoxResult = "checked";
				SharedPreferences settings = getSharedPreferences(PREFS_NAME_SUPPR, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("skipMessage", checkBoxResult);	
				// Commit the edits!
				editor.commit();
				//add the kanji to the revision list
				supprKanjiList(new File(Environment.getExternalStorageDirectory(), "ListRev.txt"));
				//update the icon
				btAddSuppr.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
				Toast.makeText(getBaseContext(),
						"Supression du kanji terminé avec succès.",
						Toast.LENGTH_SHORT).show();
				return;  
			} });

		adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		{  
			public void onClick(DialogInterface dialog, int which) {
				String checkBoxResult = "NOT checked";
				if (dontShowAgain.isChecked())  checkBoxResult = "checked";
				SharedPreferences settings = getSharedPreferences(PREFS_NAME_SUPPR, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("skipMessage", checkBoxResult);	
				// Commit the edits!
				editor.commit();
				return;  
			} });

		//Verifier si l'utilisateur demande à ce que le pop-up ne s'affiche plus
		SharedPreferences settings = getSharedPreferences(PREFS_NAME_SUPPR, 0);
		String skipMessage = settings.getString("skipMessage", "NOT checked");
		//si il n'a rien demander, afficher le pop-up
		if (!skipMessage.equalsIgnoreCase("checked") ) 
		{
			adb.show();
		}
		//sinon, ajouter le kanji et changer l'icône ajout/suppression
		else
		{
			//on ne prend pas en compte le retour succès/echec (A FAIRE)
			this.supprKanjiList(new File(Environment.getExternalStorageDirectory(), "ListRev.txt"));
			this.btAddSuppr.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
		}
	}

	/**
	 * Supprime le kanji courant de la liste de revision
	 * @param file : le fichier
	 * @return true si succès, false sinon 
	 * 
	 */
	protected boolean supprKanjiList(File file) 
	{
		try 
		{ 
			StringBuffer sb = new StringBuffer(); 

			//parcours de la liste des kanji
			for(int numKanji = 0; numKanji < MainQuizzesActivity.listRev.size(); numKanji++)
			{
				//si le kanji courant n'est pas celui à supprimer, on le met dans le buffer pour l'écrire plus tard
				if(MainQuizzesActivity.listRev.get(numKanji) != MainQuizzesActivity.random)
				{
					sb.append(((Integer)MainQuizzesActivity.listRev.get(numKanji)).toString()+"\n");
				}
				//sinon, on le supprime de la liste et on met à jour l'indice
				else
				{
					MainQuizzesActivity.listRev.remove(numKanji);
					numKanji--;
				}
			} 
			//ecrire le contenu du buffer dans le fichier
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(sb.toString());
			out.close();
		} 
		catch (Exception e) 
		{
			return false;
		}
		return true;
	}
}
