package com.android.demo.KanjiActivity;

import com.android.demo.KanjiActivity.R;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Classe/vue correspondant au quizz : affichage du kanji et saisie de la réponse par l'utlisateur
 * @author Gabriel
 *
 */
public class SigniTrainingQuizzActivity extends MainTrainingActivity
{
	protected void init()
	{
		super.init();
		
		//recuperer l'image-button du kanji
		this.btKanji = (Button) findViewById(R.id.signi_k1);		

		this.btKanji.setOnClickListener(this);

		//idem avec le bouton de validation
		this.btNext = (ImageButton) findViewById(R.id.next);
		//on met le listener
		this.btNext.setOnClickListener(this);

		//idem avec le bouton de reponse
		this.btRep = (Button) findViewById(R.id.signi_rep);
		this.btRep.setText(ChoiceChapInterActivity.getBundle().getString(MainQuizzesActivity.random.toString()));
		//on met le listener
		this.btRep.setOnClickListener(this);
		
		SigniTrainingQuizzActivity.repOk = false;			//
		SigniTrainingQuizzActivity.isLastKanji = false;		//
	}


	protected void continuer()
	{
		//La passerelle vers la vue/activitée
		Intent intent;

		intent = new Intent(SigniTrainingQuizzActivity.this, SigniTrainingQuizzActivity.class);
		MainQuizzesActivity.nextActivate = false;
		//On démarre l'activité
		SigniTrainingQuizzActivity.repOk = false;
		startActivity(intent);
		finish();
	}
	
	protected void display()
	{
		super.display();
		//mettre la bonne image et le rendre visible
		this.btKanji.setBackgroundResource(MainQuizzesActivity.kanjisBouton.get(MainQuizzesActivity.random));
		this.btKanji.setVisibility(android.view.View.VISIBLE);
	}
	
}//fin classe