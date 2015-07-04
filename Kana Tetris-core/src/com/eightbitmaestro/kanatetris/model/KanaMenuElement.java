package com.eightbitmaestro.kanatetris.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class KanaMenuElement {
	
	public Vector2 position;
	Rectangle bounds;
	String kana;
	Selector selector;
	
	public KanaMenuElement(Vector2 position, String kana, boolean kanaSelected){
		this.position = position;
		this.kana = kana;
		bounds = new Rectangle(this.position.x, this.position.y, 32,32);
		selector = new Selector(this.position, kanaSelected);
	}
	
	/**
	 * Changes selection from true to false
	 */
	public void flip(){
		selector.selected = !selector.selected;
	}
	
	public void turnOff(){
		selector.selected = false;
	}
	
	public void turnOn(){
		
	}
	
	public void update(){
		selector.update();
	}
	
	public Vector2 getBasePosition(){
		return position;
	}
	
	public Rectangle getBounds(){
		return bounds;
	}
	
	public String getKana(){
		return kana;
	}
	
	public Selector getSelector(){
		return selector;
	}
	
	//static float standardX = 50, standardY=400, dakutenX =50, dakutenY =50, comboX =50, comboY=50, dakutenComboX=50, dakutenComboY=50, width=32, height=32;
	static float standardX = (float) (0.078125*Gdx.graphics.getWidth()), standardY=(float) (0.7*Gdx.graphics.getHeight()), 
			dakutenX =50, dakutenY =50, 
			comboX =50, comboY=50, 
			dakutenComboX=50, dakutenComboY=50, 
			width=(float) (Gdx.graphics.getWidth()*0.05), 
			height=width;
	
	public static Array<KanaMenuElement> createHiraganaMenuElements(){
		Array<KanaMenuElement> array = new Array<KanaMenuElement>();
		array.add(new KanaMenuElement(new Vector2(standardX, standardY), "h.a", true));
		array.add(new KanaMenuElement(new Vector2(standardX, standardY-height), "h.i", true));
		array.add(new KanaMenuElement(new Vector2(standardX, standardY-height*2), "h.u", true));
		array.add(new KanaMenuElement(new Vector2(standardX, standardY-height*3), "h.e", true));
		array.add(new KanaMenuElement(new Vector2(standardX, standardY-height*4), "h.o", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width, standardY), "h.ka", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width, standardY-height), "h.ki", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width, standardY-height*2), "h.ku", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width, standardY-height*3), "h.ke", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width, standardY-height*4), "h.ko", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*2, standardY), "h.sa", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*2, standardY-height), "h.shi", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*2, standardY-height*2), "h.su", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*2, standardY-height*3), "h.se", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*2, standardY-height*4), "h.so", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*3, standardY), "h.ta", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*3, standardY-height), "h.chi", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*3, standardY-height*2), "h.tsu", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*3, standardY-height*3), "h.te", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*3, standardY-height*4), "h.to", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*4, standardY), "h.na", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*4, standardY-height), "h.ni", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*4, standardY-height*2), "h.nu", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*4, standardY-height*3), "h.ne", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*4, standardY-height*4), "h.no", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*5, standardY), "h.ha", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*5, standardY-height), "h.hi", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*5, standardY-height*2), "h.fu", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*5, standardY-height*3), "h.he", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*5, standardY-height*4), "h.ho", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*6, standardY), "h.ma", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*6, standardY-height), "h.mi", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*6, standardY-height*2), "h.mu", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*6, standardY-height*3), "h.me", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*6, standardY-height*4), "h.mo", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*7, standardY), "h.ya", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*7, standardY-height*2), "h.yu", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*7, standardY-height*4), "h.yo", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*8, standardY), "h.ra", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*8, standardY-height), "h.ri", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*8, standardY-height*2), "h.ru", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*8, standardY-height*3), "h.re", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*8, standardY-height*4), "h.ro", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*9, standardY), "h.wa", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*9, standardY-height*4), "h.wo", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*10, standardY), "h.n", true));
		
		return array;
	}
	
	public static Array<KanaMenuElement> createKatakanaMenuElements(){
		Array<KanaMenuElement> array = new Array<KanaMenuElement>();
		array.add(new KanaMenuElement(new Vector2(standardX, standardY), "k.a", true));
		array.add(new KanaMenuElement(new Vector2(standardX, standardY-height), "k.i", true));
		array.add(new KanaMenuElement(new Vector2(standardX, standardY-height*2), "k.u", true));
		array.add(new KanaMenuElement(new Vector2(standardX, standardY-height*3), "k.e", true));
		array.add(new KanaMenuElement(new Vector2(standardX, standardY-height*4), "k.o", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width, standardY), "k.ka", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width, standardY-height), "k.ki", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width, standardY-height*2), "k.ku", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width, standardY-height*3), "k.ke", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width, standardY-height*4), "k.ko", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*2, standardY), "k.sa", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*2, standardY-height), "k.shi", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*2, standardY-height*2), "k.su", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*2, standardY-height*3), "k.se", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*2, standardY-height*4), "k.so", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*3, standardY), "k.ta", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*3, standardY-height), "k.chi", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*3, standardY-height*2), "k.tsu", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*3, standardY-height*3), "k.te", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*3, standardY-height*4), "k.to", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*4, standardY), "k.na", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*4, standardY-height), "k.ni", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*4, standardY-height*2), "k.nu", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*4, standardY-height*3), "k.ne", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*4, standardY-height*4), "k.no", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*5, standardY), "k.ha", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*5, standardY-height), "k.hi", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*5, standardY-height*2), "k.fu", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*5, standardY-height*3), "k.he", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*5, standardY-height*4), "k.ho", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*6, standardY), "k.ma", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*6, standardY-height), "k.mi", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*6, standardY-height*2), "k.mu", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*6, standardY-height*3), "k.me", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*6, standardY-height*4), "k.mo", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*7, standardY), "k.ya", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*7, standardY-height*2), "k.yu", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*7, standardY-height*4), "k.yo", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*8, standardY), "k.ra", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*8, standardY-height), "k.ri", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*8, standardY-height*2), "k.ru", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*8, standardY-height*3), "k.re", true));
		array.add(new KanaMenuElement(new Vector2(standardX+width*8, standardY-height*4), "k.ro", true));
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*9, standardY), "k.wa", true));
		//array.add(new KanaMenuElement(new Vector2(standardX+width*9, standardY-height*4), "k.wo", true)); no wo in katakana?
		
		array.add(new KanaMenuElement(new Vector2(standardX+width*10, standardY), "k.n", true));
		
		return array;
	}

}
