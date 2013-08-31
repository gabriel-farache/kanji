package com.android.demo.KanjiActivity;

import java.util.ArrayList;
import java.util.Random;

import com.android.demo.KanjiActivity.R;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Classe/vue correspondant au quizz : affichage du kanji et saisie de la réponse par l'utlisateur
 * @author Gabriel
 *
 */
public class QCMQuizzActivity extends MainQCMActivity
{
	protected void initButtons()
	{
		super.initButtons();
		//recuperer l'image-button du kanji
		this.btRep = (Button) findViewById(R.id.k1);
		//mettre la bonne image et le rendre visible	
		//this.btRep.setBackgroundResource(MainQuizzesActivity.kanjisBouton.get(MainQuizzesActivity.random));
		this.btRep.setBackgroundResource(MainQuizzesActivity.kanjisBouton.get(MainQuizzesActivity.random));
		this.btRep.setVisibility(android.view.View.VISIBLE);

		//idem avec le bouton de validation
		this.btNext = (ImageButton) findViewById(R.id.next);
		//on met le listener
		this.btNext.setOnClickListener(this);

		//idem avec le bouton de reponse
		this.tabButtons = new ArrayList<Button>(4);

		this.tabButtons.add(0, (Button) findViewById(R.id.rep1));
		//on met le listener
		this.tabButtons.get(0).setOnClickListener(this);

		//idem avec le bouton de reponse
		this.tabButtons.add(1, (Button) findViewById(R.id.rep2));
		//on met le listener
		this.tabButtons.get(1).setOnClickListener(this);

		//idem avec le bouton de reponse
		this.tabButtons.add(2, (Button) findViewById(R.id.rep3));
		//on met le listener
		this.tabButtons.get(2).setOnClickListener(this);

		//idem avec le bouton de reponse
		this.tabButtons.add(3, (Button) findViewById(R.id.rep4));
		//on met le listener
		this.tabButtons.get(3).setOnClickListener(this);	
	}


	protected void initQCM()
	{
		String rep2, rep3, rep4;
		Integer rand2, rand3, rand4;

		this.rep = ChoiceChapInterActivity.getBundle().getString(MainQuizzesActivity.random.toString());

		rand2 = 1 + (new Random()).nextInt(2064 - 1);
		rand3 = 1 + (new Random()).nextInt(2064 - 1);
		rand4 = 1 + (new Random()).nextInt(2064 - 1);

		rep2 = ChoiceChapInterActivity.getBundle().getString(rand2.toString());
		rep3 = ChoiceChapInterActivity.getBundle().getString(rand3.toString());
		rep4 = ChoiceChapInterActivity.getBundle().getString(rand4.toString());

		this.placement = new Random().nextInt(4 - 0);

		this.tabButtons.get(placement).setText(this.rep);
		this.tabButtons.get((placement+1)%4).setText(rep2);
		this.tabButtons.get((placement+2)%4).setText(rep3);
		this.tabButtons.get((placement+3)%4).setText(rep4);

		if(QCMQuizzActivity.dejaRep)
		{
			this.displayRep();
		}

		MainQuizzesActivity.nextActivate = true;
	}

	

	protected void handleNext()
	{
		Intent intent = new Intent(QCMQuizzActivity.this, QCMQuizzActivity.class);
		MainQuizzesActivity.nextActivate = false;
		QCMQuizzActivity.dejaRep = false;
		//On démarre l'activité
		startActivity(intent);
		finish();
	}
	
}//fin classe