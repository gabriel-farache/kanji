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
public class TrainingQuizzActivity extends MainTrainingActivity
{	
	protected void init()
	{
		super.init();
		
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
		//on met le listener
		this.btRep.setOnClickListener(this);
		
		TrainingQuizzActivity.repOk = false;			//
		TrainingQuizzActivity.isLastKanji = false;		//
	}

	
	protected void continuer()
	{
		//La passerelle vers la vue/activitée
		Intent intent;
		
		intent = new Intent(TrainingQuizzActivity.this, TrainingQuizzActivity.class);
		MainQuizzesActivity.nextActivate = false;
		//On démarre l'activité
		TrainingQuizzActivity.repOk = false;
		startActivity(intent);
		finish();
	}
	
	protected void display()
	{
		super.display();
		String rep = ChoiceChapInterActivity.getBundle().getString(MainQuizzesActivity.random.toString());

		this.btRep.setTextColor(android.graphics.Color.WHITE);
		this.btRep.setText(rep);
		this.btRep.setBackgroundColor(android.graphics.Color.BLACK);
	}
}//fin classe