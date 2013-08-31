package com.android.demo.KanjiActivity;

import com.android.demo.KanjiActivity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Classe/vue pour le choix Intervalle
 * @author Gabriel
 *
 */
public class IntervalleActivity  extends Activity implements View.OnClickListener
{
	private int intervalleFin;
	private int intervalleDeb;
	private ImageButton okInter;
	private EditText interDeb;
	private EditText interFin;
	private Button btQuit;
	
	/**
	 * Méthode appelée é la création de la vue
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
		 super.onCreate(savedInstanceState);
		 
		 //Affichage des éléments comme définit dans intervalle_display.xml
		 setContentView(R.layout.intervalle_display);
		 
		//Récupération des zone d'EditText pour les intervalles de l'utilisateur
		 this.interDeb = (EditText) findViewById(R.id.intervalleDeb);
		 this.interFin = (EditText) findViewById(R.id.intervalleFin);
		 
		 //on affiche un bouton terminer sur le clavier pour le faire disparaître
		 this.interDeb.setImeOptions (EditorInfo.IME_ACTION_DONE);
		 this.interFin.setImeOptions (EditorInfo.IME_ACTION_DONE);
		 
		 //Récupération du bouton de validation "okInter" puis ajout d'un listener
		 this.okInter = (ImageButton) findViewById(R.id.okInter);
		 this.okInter.setOnClickListener(this);
		 
		//idem avec le bouton de retour au menu
		this.btQuit = (Button) findViewById(R.id.menu);
		//on met le listener
		this.btQuit.setOnClickListener(this);

	}//fin méthode
	
	 /**
     * Listener des boutons.
     * Determine l'action é effectuer (ici appel d'une autre vue) lorsqu'un bouton est pressé.
     */
	public void onClick(View v) 
	{
		if (v != this.btQuit)
		{		
			this.handleOk();
		}//fin if(retour menu)
		else
		{
			this.handleBackMenu();
		}
		
	}//fin méthode
	
	private void handleOk()
	{
		//La passerelle vers la vue/activitée
		Intent intent;
		
		/*Vérifier que l'utilisateur est bien rentré un nombre*/
		this.recupBornes();
		
		//Vérifier que les nombres rentrés soit bien dans l'intervalle
		if (this.verifBornes())
		{
			intent = this.startUtilChoice();
			
			/*On stock les informations dont aura besoin QuizzActivity pour fonctionner :
			*  le type du choix de l'utilisateur (intervalle ou chapitre)
			*  la borne inf et la borne sup
			*/
			intent.putExtra("type", "inter");
			intent.putExtra("intervalleDeb", this.intervalleDeb);
			intent.putExtra("intervalleFin", this.intervalleFin);
			//On démarre l'activité
			startActivity(intent);
		}
	}
	
	private void recupBornes()
	{
		if (this.interDeb.getText().toString().compareToIgnoreCase("") == 0 || this.interFin.getText().toString().compareToIgnoreCase("") == 0)
		{
			 Toast.makeText(getBaseContext(),
			 "Il faut rentrer un nombre.",
			 Toast.LENGTH_SHORT).show();
			 return;
		}
		
		try 
		{ 
			this.intervalleDeb =  Integer.parseInt(this.interDeb.getText().toString());
			this.intervalleFin =  Integer.parseInt(this.interFin.getText().toString());
		} 
		catch (Exception e) 
		{ 
			Toast.makeText(getBaseContext(),
					 "Il faut rentrer un nombre.",
					 Toast.LENGTH_SHORT).show();
					 return; 
		}
	}
	
	
	private boolean verifBornes()
	{
		if (this.intervalleDeb < 1 || this.intervalleFin > 2065)
		{
			 Toast.makeText(getBaseContext(),
			 "Intervalle doit etre entre 1 a 2065 exclut.",
			 Toast.LENGTH_SHORT).show();
			 return false;
		}
		
		//Vérifier la cohérences des bornes
		if (this.intervalleFin <= this.intervalleDeb)
		{
			 Toast.makeText(getBaseContext(),
			 "L'intervalle de debut doit etre superieur a celui de fin.",
			 Toast.LENGTH_SHORT).show();
			 return false;
		}	
		return true;
	}
	
	private Intent startUtilChoice()
	{
		Intent intent;
		
		if (AccueilActivity.getChoixAcc().compareToIgnoreCase("kanjiTxtIn") == 0)
		{
			intent = new Intent(IntervalleActivity.this, QuizzActivity.class);
		}
		else if (AccueilActivity.getChoixAcc().compareToIgnoreCase("kanjiBtn") == 0)
		{
			intent = new Intent(IntervalleActivity.this, TrainingQuizzActivity.class);
		}
		else if (AccueilActivity.getChoixAcc().compareToIgnoreCase("SigniBtn") == 0)
		{
			intent = new Intent(IntervalleActivity.this, SigniTrainingQuizzActivity.class);
		}
		else if (AccueilActivity.getChoixAcc().compareToIgnoreCase("SigniQCM") == 0)
		{
			intent = new Intent(IntervalleActivity.this, SigniQCMQuizzActivity.class);
		}
		else
		{
			intent = new Intent(IntervalleActivity.this, QCMQuizzActivity.class);
		}
		
		return intent;
	}
	
	private void handleBackMenu()
	{
		//QCMQuizzActivity.setEnd();
		Intent intent = new Intent(IntervalleActivity.this, AccueilActivity.class);
		//On démarre l'activité
		startActivity(intent);
	}
	
}//fin classe