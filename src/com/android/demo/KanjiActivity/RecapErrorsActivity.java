package com.android.demo.KanjiActivity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.demo.KanjiActivity.R;


public class RecapErrorsActivity extends Activity implements View.OnClickListener
{

	private ImageButton endChoice;
	private Button btQuit;
	private ImageButton next;
	private ArrayList<Integer> errAnswers;
	private ArrayList<ImageButton> boutons;
	private int pageCour;
	private static int typeQCM;
	private int indiceDeb; //indice de départ pour lire errAnswers
	private int indiceFin; //nombre d'itérations à faire pour arriver à la fin => utile que pour un affichage où il y a moins de 9 éléments
	private static HashMap<Integer, Integer> kanjisBouton = AccueilActivity.getKanjisBouton();

	/**
	 * Méthode appelée é la création de la vue
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//On affiche la vue comme défini dans recap_display.xml
		setContentView(R.layout.recap_errors);


		//Les erreurs
		//La zone de texte pour tout indiquer é l'utilisateur
		TextView recap = (TextView) findViewById(R.id.recapTxtErr);

		this.initVars();

		//initialise le tableau avec les boutons
		this.initButtons();

		//verifier si on va re-appeller la classe
		this.verifEnd();

		//Lui afficher le recapitulatif
		if(!this.errAnswers.isEmpty())
		{
			recap.setText("Vos erreurs (kanji "+(this.indiceDeb+1)+" - "+(9*this.pageCour-(9-(this.indiceFin+1)))+") :");
		}
		else
		{
			recap.setText("Félicitation ! Vous n'avez fait aucune erreur !!");
		}

		this.afficherButtons();

	}//fin méthode

	private void initVars()
	{
		//L'intent pour récupérer les infos
		Intent thisIntent = getIntent();

		//Récupérer le bouton de fin et lui mettre un listener
		this.endChoice = (ImageButton) findViewById(R.id.endChoiceErr);
		this.endChoice.setOnClickListener(this);
		this.btQuit = (Button) findViewById(R.id.menu);
		this.btQuit.setOnClickListener(this);
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
		
		//Récupérer le bouton de suite et lui mettre un listener
		this.next = (ImageButton) findViewById(R.id.nextErr);
		this.next.setOnClickListener(this);

		//Récupérer les données en entrée
		this.errAnswers = thisIntent.getExtras().getIntegerArrayList("errAnswers");
		this.pageCour = thisIntent.getExtras().getInt("pageCour");

		this.indiceDeb = this.pageCour*9-9; //-9 car on commence à 0
		this.indiceFin = (this.errAnswers.size()-indiceDeb-1 > 8) ? 8:this.errAnswers.size()-indiceDeb-1 ;
		
		RecapErrorsActivity.typeQCM = thisIntent.getExtras().getInt("typeQCM");	//1 = QCM, 2 = SigniQCM

	}

	/**
	 * Listener du bouton back est pressé.
	 * Ici, il est désactivé et on en informe l'utilisateur.
	 */
	public void onBackPressed()
	{
		//si je suis au délà de la 1ere pages, permettre à l'utilisateur de revenir en arrière
		if (this.pageCour > 1)
		{
			//La passerelle vers la vue/activitée
			Intent intent;
			
			intent = new Intent(RecapErrorsActivity.this, RecapErrorsActivity.class);
			intent.putExtra("pageCour", this.pageCour-1);
			intent.putExtra("typeQCM", RecapErrorsActivity.typeQCM);
			intent.putIntegerArrayListExtra("errAnswers", this.errAnswers);
			startActivity(intent);
			finish();
		}
		else
		{
			Toast.makeText(getBaseContext(),
					"Le bouton back est desactivé sur l'application.",
					Toast.LENGTH_SHORT).show();
		}
		return;
	}//fin méthode

	/**
	 * Listener des boutons.
	 * Determine l'action é effectuer (ici appel d'une autre vue) lorsqu'un bouton est pressé.
	 */
	public void onClick(View v) 
	{
		if(v != this.endChoice && v != this.next && v != this.btQuit)
		{

			this.displayKanjiInfo(v);
		}
		else
		{
			if (v == this.next)
			{
				this.handleNext();
			}
			else
			{
				this.handleQuit();
			}
		}
	}//fin méthode

	
	private void displayKanjiInfo(View v)
	{
		//verifier bouton presser
		for(int i =0; i < 9 && i <= this.indiceFin; i++)
		{
			if(v == this.boutons.get(i))
			{
				this.affichePopUp(i+this.indiceDeb);
			}
		}
	}
	
	private void handleNext()
	{
		//La passerelle vers la vue/activitée
		Intent intent;
		
		intent = new Intent(RecapErrorsActivity.this, RecapErrorsActivity.class);
		intent.putExtra("pageCour", this.pageCour+1);
		intent.putExtra("typeQCM", RecapErrorsActivity.typeQCM);
		intent.putIntegerArrayListExtra("errAnswers", this.errAnswers);
		startActivity(intent);
		finish();
	}
	
	private void handleQuit()
	{
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage("Êtes-vous sûr ?");
		builder.setCancelable(false);
		builder.setTitle("Confirmation");

		builder.setPositiveButton("OUI",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent;
				
				if(RecapErrorsActivity.typeQCM == 1)
				{
					if ((QCMQuizzActivity.getType()).compareToIgnoreCase("chap") == 0)
					{
						intent = new Intent(RecapErrorsActivity.this, ChapitreActivity.class);
					}
					else
					{
						intent = new Intent(RecapErrorsActivity.this, IntervalleActivity.class);
					}
				}
				else
				{
					if ((SigniQCMQuizzActivity.getType()).compareToIgnoreCase("chap") == 0)
					{
						intent = new Intent(RecapErrorsActivity.this, ChapitreActivity.class);
					}
					else
					{
						intent = new Intent(RecapErrorsActivity.this, IntervalleActivity.class);
					}
				}
				
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

	private void affichePopUp(int i)
	{
		String mess;
		if(i != -1)
		{
			mess = ChoiceChapInterActivity.getBundle().getString(this.errAnswers.get(i).toString());
		}
		else
		{
			mess = "Erreur !";
		}
		//Afficher le message dans un pop-up
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Kanji n°"+this.errAnswers.get(i));
		builder.setMessage("Ce kanji signifie :\n"+mess+".")
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

	private void initButtons()
	{
		this.boutons = new ArrayList<ImageButton>(9);
		this.boutons.add(0,(ImageButton) findViewById(R.id.kErr1));
		this.boutons.add(1,(ImageButton) findViewById(R.id.kErr2));
		this.boutons.add(2,(ImageButton) findViewById(R.id.kErr3));
		this.boutons.add(3,(ImageButton) findViewById(R.id.kErr4));
		this.boutons.add(4,(ImageButton) findViewById(R.id.kErr5));
		this.boutons.add(5,(ImageButton) findViewById(R.id.kErr6));
		this.boutons.add(6,(ImageButton) findViewById(R.id.kErr7));
		this.boutons.add(7,(ImageButton) findViewById(R.id.kErr8));
		this.boutons.add(8,(ImageButton) findViewById(R.id.kErr9));

		for (int i = 0; i < 9; i++)
		{
			this.boutons.get(i).setOnClickListener(this);
		}
	}

	private void afficherButtons()
	{		
		for(int i = 0; i < 9 && i <= this.indiceFin; i++)
		{
			this.boutons.get(i).setBackgroundDrawable(getResources().getDrawable((Integer) RecapErrorsActivity.kanjisBouton.get(this.errAnswers.get(i+this.indiceDeb))));
			this.boutons.get(i).setVisibility(android.view.View.VISIBLE);
		}
	}

	private void verifEnd()
	{
		if(this.errAnswers.size()-indiceDeb-1 > 8) //car indiceFin commence à 0
		{
			this.next.setVisibility(android.view.View.VISIBLE);
		}
		else
		{
			this.endChoice.setVisibility(android.view.View.VISIBLE);
		}

	}

}
