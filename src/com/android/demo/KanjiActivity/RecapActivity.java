package com.android.demo.KanjiActivity;

import com.android.demo.KanjiActivity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Classe/vue affichant le récapitlatif final
 * @author Gabriel
 *
 */
public class RecapActivity extends Activity implements View.OnClickListener
{
	/**
	 * Bouton de validation du recapitulatif
	 */
	private ImageButton endChoice;

	
	
	/**
	 * Méthode appelée é la création de la vue
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//On affiche la vue comme défini dans recap_display.xml
		setContentView(R.layout.recap_display);
				
		//L'intent pour récupérer les infos
		Intent thisIntent = getIntent();
		//Le temps écoulé depuis le début du quizz (hors correction)
		long elapsedMillis = SystemClock.elapsedRealtime() - QuizzActivity.getChrono().getBase(); 
		//Le nombre de points final
		int pts = thisIntent.getExtras().getInt("pts");
		//La zone de texte pour tout indiquer é l'utilisateur
		TextView recap = (TextView) findViewById(R.id.recapTxt);
		
		//Lui afficher le recapitulatif
		recap.setText("Vous avez eu "+pts+" bonne(s) reponse(s) sur "+MainQuizzesActivity.getNbKanjiTotChap()+".\nVous avez mis "+elapsedMillis/1000+" secondes.");
		
		//Récupérer le bouton de fin et lui mettre un listener
		this.endChoice = (ImageButton) findViewById(R.id.endChoice);
		this.endChoice.setOnClickListener(this);
	}//fin méthode

	
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
     * Listener des boutons.
     * Determine l'action é effectuer (ici appel d'une autre vue) lorsqu'un bouton est pressé.
     */
	public void onClick(View v) 
	{
		//La passerelle vers la vue/activitée
		Intent intent;
		
		//On remet l'utlisateur sur le choix des sous-chapitres, dans le groupe qu'il a choisit
		if ((QuizzActivity.getType()).compareToIgnoreCase("chap") == 0)
		{
			intent = new Intent(RecapActivity.this, ChapitreActivity.class);
		}
		//Sinon, sur celle des intervalles.
		else
		{
			intent = new Intent(RecapActivity.this, IntervalleActivity.class);
		}
		//On démarre l'activité
		startActivity(intent);
		finish();
	}//fin méthode
	
}//fin classe