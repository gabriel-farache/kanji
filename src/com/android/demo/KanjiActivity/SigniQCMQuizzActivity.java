package com.android.demo.KanjiActivity;

import java.util.ArrayList;
import java.util.Random;

import com.android.demo.KanjiActivity.R;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Classe/vue correspondant au quizz : affichage du kanji et saisie de la réponse par l'utlisateur
 * @author Gabriel
 *
 */
public class SigniQCMQuizzActivity extends MainQCMActivity
{
	protected void initButtons()
	{
		super.initButtons();
		//recuperer l'image-button du kanji
		this.btRep = (Button) findViewById(R.id.signi_rep);
		//mettre la bonne signification
		this.rep = ChoiceChapInterActivity.getBundle().getString(SigniQCMQuizzActivity.random.toString());
		this.btRep.setText(this.rep);

		//idem avec le bouton de validation
		this.btNext = (ImageButton) findViewById(R.id.next);
		//on met le listener
		this.btNext.setOnClickListener(this);

		//idem avec le bouton de retour au menu
		this.btQuit = (Button) findViewById(R.id.menu);
		//on met le listener
		this.btQuit.setOnClickListener(this);

		this.tabButtons = new ArrayList<Button>(4);

		this.tabButtons.add(0, (Button) findViewById(R.id.signi_k1));
		//on met le listener
		this.tabButtons.get(0).setOnClickListener(this);

		this.tabButtons.add(1, (Button) findViewById(R.id.signi_k2));
		//on met le listener
		this.tabButtons.get(1).setOnClickListener(this);

		this.tabButtons.add(2, (Button) findViewById(R.id.signi_k3));
		//on met le listener
		this.tabButtons.get(2).setOnClickListener(this);

		this.tabButtons.add(3, (Button) findViewById(R.id.signi_k4));
		//on met le listener
		this.tabButtons.get(3).setOnClickListener(this);

		this.btOk = (ImageButton) findViewById(R.id.ok);
		this.btOk.setOnClickListener(this);
	}


	protected void initQCM()
	{
		Drawable repK2, repK3, repK4;
		Integer rand2, rand3, rand4;

		rand2 = 1 + (new Random()).nextInt(2064 - 1);
		rand3 = 1 + (new Random()).nextInt(2064 - 1);
		rand4 = 1 + (new Random()).nextInt(2064 - 1);

		repK2 = getResources().getDrawable((Integer) SigniQCMQuizzActivity.kanjisBouton.get(rand2));	
		repK3 = getResources().getDrawable((Integer) SigniQCMQuizzActivity.kanjisBouton.get(rand3));	
		repK4 = getResources().getDrawable((Integer) SigniQCMQuizzActivity.kanjisBouton.get(rand4));		

		this.placement = new Random().nextInt(4 - 0);

		this.tabButtons.get(placement).setBackgroundResource(MainQuizzesActivity.kanjisBouton.get(MainQuizzesActivity.random));
		this.tabButtons.get(placement).setText(this.rep);
		this.tabButtons.get(placement).setTextColor(android.graphics.Color.TRANSPARENT);
		this.tabButtons.get((placement+1)%4).setBackgroundDrawable(repK2);
		this.tabButtons.get((placement+2)%4).setBackgroundDrawable(repK3);
		this.tabButtons.get((placement+3)%4).setBackgroundDrawable(repK4);
		
		if(SigniQCMQuizzActivity.dejaRep)
		{
			this.displayRep();
		}

		MainQuizzesActivity.nextActivate = true;
	}

	protected void handleNext()
	{
		Intent intent = new Intent(SigniQCMQuizzActivity.this, SigniQCMQuizzActivity.class);
		SigniQCMQuizzActivity.nextActivate = false;
		SigniQCMQuizzActivity.dejaRep = false;
		//On démarre l'activité
		startActivity(intent);
		finish();
	}
}//fin classe