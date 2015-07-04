package com.eightbitmaestro.kanatetris.model;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.utils.Array;

public class KanaSelector {
	
	ArrayList<String> unusedKana = new ArrayList<String>();
	ArrayList<String> usedKana = new ArrayList<String>();
	Random random = new Random();
	
	public KanaSelector(ArrayList<String> unusedKana){
//		for (String s : list)
//			unusedKana.add(s);
		this.unusedKana = unusedKana;
	}
	
	public String getRandomKana(){
		String selectedString = unusedKana.get(random.nextInt(unusedKana.size()));
		unusedKana.remove(selectedString);
		usedKana.add(selectedString);
		if (unusedKana.size() == 0)
			resetArrayLists();
		return selectedString;
	}
	
	public void moveKanaToUsed(String kana){
		if (unusedKana.contains(kana)){
			unusedKana.remove(kana);
			usedKana.add(kana);
		}
		if (unusedKana.size() == 0)
			resetArrayLists();
	}
	
	void resetArrayLists(){
		for(String s : usedKana){
			unusedKana.add(s);
		}
		usedKana = new ArrayList<String>();
	}
	
	public void addNewKana(String kana){
		unusedKana.add(kana);
	}
	
}
