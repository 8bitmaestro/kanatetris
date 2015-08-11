package com.eightbitmaestro.kanatetris.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.eightbitmaestro.kanatetris.MyGdxGame;
import com.eightbitmaestro.kanatetris.constants.Constants;
import com.eightbitmaestro.kanatetris.control.InputHandler;
import com.eightbitmaestro.kanatetris.model.Block;
import com.eightbitmaestro.kanatetris.model.CyanPolygon;
import com.eightbitmaestro.kanatetris.model.GreenPolygon;
import com.eightbitmaestro.kanatetris.model.IndigoPolygon;
import com.eightbitmaestro.kanatetris.model.KanaMenuElement;
import com.eightbitmaestro.kanatetris.model.KanaSelector;
import com.eightbitmaestro.kanatetris.model.MenuText;
import com.eightbitmaestro.kanatetris.model.Polygon;
import com.eightbitmaestro.kanatetris.model.PurplePolygon;
import com.eightbitmaestro.kanatetris.model.RedPolygon;
import com.eightbitmaestro.kanatetris.model.OrangePolygon;
import com.eightbitmaestro.kanatetris.model.Selector;
import com.eightbitmaestro.kanatetris.model.YellowPolygon;

public class World {
	Array<Block> blocks;
	Block b; Iterator<Block> blockIterator;
	Array<Block> currentTetromino; //falling, "unlocked" tetromino
	Polygon currentPolygon;
	boolean currentTetrominoLocked, collisionsChecked, kanaEntered, sIsDown, sCanBeEntered, mainMenuShowing, gameStarted;
	boolean gameOptionsSelected, hiraganaOptionsSelected, katakanaOptionsSelected, tutorMode, endlessMode = true, classicMode;
	boolean retroFlavor, downtempoFlavor, coolFlavor, musicStarted, kanaSelectorInitialized;
	boolean hiraganaDiacriticsOn = true, katakanaDiacriticsOn = true, hiraganaCombinationsOn = true, katakanaCombinationsOn = true;
	boolean hiraganaEnabled = true, katakanaEnabled = true; //these are controlled by the "turn all <kana> on/off" in the menu
	boolean hiraganaDiacriticsEnabled = true, hiraganaCombinationsEnabled = true, katakanaDiacriticsEnabled = true, katakanaCombinationsEnabled = true;
	boolean soundPlayed = false;
	MyGdxGame game;
	KanaSelector kanaSelector, tutorKanaSelector;
	//float tetrisUnit = 64; old
	float tetrisUnit = Gdx.graphics.getWidth()/10;
	float standardWidth = Gdx.graphics.getWidth()*0.1f;
	float halfWidth = Gdx.graphics.getWidth()*0.05f;
	int fieldX = 2; int fieldY = 1; int fieldWidth = 10; int fieldHeight = 19;
	int scoreX = 17, scoreY = 5, scoreWidth = 10, scoreHeight = 15;
	int rowsCleared, correctAnswers, incorrectAnswers, tutorModeKanaCount = 10;
	Rectangle fieldBottom, fieldLeft, fieldRight, scoreBoxLeft, scoreBoxRight, scoreBoxTop, scoreBoxBottom,
				mouse, mouseMoved;
	Array<Rectangle> collisionBoxes; //collision boxes on the field only, not on blocks
	Array<Rectangle> menuButtons;
	Array<Rectangle> coloredLines; //these are for the positions of the colored lines on the Main Menu
	Rectangle hiraganaToggle, katakanaToggle; //mouse collision boxes for toggling hiragana and katakana
	Rectangle hiraganaDiacriticsToggle, hiraganaCombinationsToggle, katakanaDiacriticsToggle, katakanaCombinationsToggle;
	Rectangle r; Iterator<Rectangle> collisionBoxesIterator;
	Array<KanaMenuElement> standardHiraganaElements, standardKatakanaElements, dakutenHiraganaElements, dakutenKatakanaElements, 
							combinationHiraganaElements, combinationKatakanaElements, combinationDakutenHiraganaElements, 
							combinationDakutenKatakanaElements;
	Array<Float> yCleared; //use to store position of cleared rows, so that above rows can move down
	Float f; Iterator<Float> floatIterator;
	InputHandler inputHandler;
	Random random = new Random();
	float fallTimer = 1f, currentTime;
	int[] rowBlockCount;
	String currentKana, enteredKana = "", currentTutorKana;
	Music bgm;
	Sound startSound, hiraganaSound, katakanaSound, modeSound;
	MenuText startMenuText, hiraganaMenuText, katakanaMenuText, modeMenuText; //menuText class, controls movement
	
	
	public World(MyGdxGame game){
		this.game = game;
		inputHandler = new InputHandler(this);
		Gdx.input.setInputProcessor(inputHandler);
		startSound = Gdx.audio.newSound(Gdx.files.internal("kana tetris start.wav"));
		hiraganaSound = Gdx.audio.newSound(Gdx.files.internal("kana tetris fx hiragana.wav"));
		katakanaSound = Gdx.audio.newSound(Gdx.files.internal("kana tetris fx katakana.wav"));
		modeSound = Gdx.audio.newSound(Gdx.files.internal("kana tetris fx mode.wav"));
		selectors = new Array<Selector>();
		
		fieldBottom = new Rectangle(fieldX*tetrisUnit, fieldY*tetrisUnit, fieldWidth*tetrisUnit, tetrisUnit);
		fieldLeft = new Rectangle((fieldX*tetrisUnit)-tetrisUnit, fieldY*tetrisUnit, tetrisUnit, fieldHeight*tetrisUnit);
		fieldRight = new Rectangle((fieldX*tetrisUnit)+(fieldWidth*tetrisUnit), fieldY*tetrisUnit, tetrisUnit, fieldHeight*tetrisUnit);
		
		scoreBoxBottom = new Rectangle(scoreX*tetrisUnit, scoreY*tetrisUnit, scoreWidth*tetrisUnit, tetrisUnit);
		scoreBoxLeft = new Rectangle((scoreX*tetrisUnit)-tetrisUnit, scoreY*tetrisUnit, tetrisUnit, scoreHeight*tetrisUnit);
		scoreBoxRight = new Rectangle((scoreX*tetrisUnit)+(scoreWidth*tetrisUnit), scoreY*tetrisUnit, tetrisUnit, scoreHeight*tetrisUnit);
		scoreBoxTop = new Rectangle(scoreX*tetrisUnit, (scoreY*tetrisUnit)+((scoreHeight-1)*tetrisUnit), scoreWidth*tetrisUnit, tetrisUnit);
		
		mouse = new Rectangle(-50, -50, 2,2);
		mouseMoved = new Rectangle(-50, -50, 2,2);
		collisionBoxes = new Array<Rectangle>();
		for(int i =0; i < 10; i++){
			collisionBoxes.add(new Rectangle(fieldX*tetrisUnit, (fieldY*tetrisUnit)+(i*tetrisUnit)+(tetrisUnit/2), fieldWidth*tetrisUnit, tetrisUnit/2));
		}
		menuButtons = new Array<Rectangle>();
		for(int i =1; i <4; i++){
			//hiragana
			if (i==2){
				menuButtons.add(new Rectangle(Gdx.graphics.getWidth()*.5f,Gdx.graphics.getHeight()-(Gdx.graphics.getHeight()*0.66f)+tetrisUnit,
					standardWidth*2,standardWidth));
				continue;
			}
			//katakana
			if (i==3){
				menuButtons.add(new Rectangle(Gdx.graphics.getWidth()*.5f,Gdx.graphics.getHeight()-(Gdx.graphics.getHeight()*0.9f)+tetrisUnit,
						standardWidth*2,standardWidth));
					continue;
			}
			//menuButtons.add(new Rectangle(Gdx.graphics.getWidth()*.25f*i-Gdx.graphics.getWidth()*0.1f,Gdx.graphics.getHeight()-(Gdx.graphics.getHeight()*0.24f),
			//		standardWidth*2,standardWidth));
			//mode
			menuButtons.add(new Rectangle(Gdx.graphics.getWidth()*.5f,-Gdx.graphics.getHeight()*0.15f+tetrisUnit,
					standardWidth*2,standardWidth));
			
		}
		//menuButtons.add(new Rectangle(Gdx.graphics.getWidth()/2-standardWidth, 0, standardWidth*2,standardWidth));
		menuButtons.add(new Rectangle(Gdx.graphics.getWidth()*.5f,Gdx.graphics.getHeight()-(Gdx.graphics.getHeight()*0.45f)+tetrisUnit,
				standardWidth*2,standardWidth));
		for(int i =1; i <4; i++){
			menuButtons.add(new Rectangle(Gdx.graphics.getWidth()*.25f*i-Gdx.graphics.getWidth()*0.1f,Gdx.graphics.getHeight()/2,standardWidth*2,standardWidth));
		}
		for(int i =1; i <4; i++){
				
			menuButtons.add(new Rectangle(Gdx.graphics.getWidth()*.25f*i-Gdx.graphics.getWidth()*0.1f,Gdx.graphics.getHeight()/4,standardWidth*2,standardWidth));
			
		}
		coloredLines = new Array<Rectangle>();
		Rectangle redLineLeft = new Rectangle(menuButtons.get(3).x-Gdx.graphics.getWidth(),
				menuButtons.get(3).y+menuButtons.get(3).height, Gdx.graphics.getWidth(), tetrisUnit/10);
		Rectangle yellowLineLeft =new Rectangle(menuButtons.get(1).x-Gdx.graphics.getWidth(),
				menuButtons.get(1).y+menuButtons.get(1).height, Gdx.graphics.getWidth(), tetrisUnit/10);
		Rectangle greenLineLeft = new Rectangle(menuButtons.get(2).x-Gdx.graphics.getWidth(),
				menuButtons.get(2).y+menuButtons.get(2).height, Gdx.graphics.getWidth(), tetrisUnit/10);
		Rectangle blueLineLeft = new Rectangle(menuButtons.get(0).x-Gdx.graphics.getWidth(),
				menuButtons.get(0).y+menuButtons.get(0).height, Gdx.graphics.getWidth(), tetrisUnit/10);
		coloredLines.add(redLineLeft);
		coloredLines.add(yellowLineLeft);
		coloredLines.add(blueLineLeft);
		coloredLines.add(greenLineLeft);
		tutorModeBox = menuButtons.get(4);
		endlessModeBox = menuButtons.get(5);
		classicModeBox = menuButtons.get(6);
		tutorSelector = new Selector(new Vector2(tutorModeBox.x+tutorModeBox.width/2, tutorModeBox.y), tutorMode);
		endlessSelector = new Selector(new Vector2(endlessModeBox.x+endlessModeBox.width/2, endlessModeBox.y), endlessMode);
		classicSelector = new Selector(new Vector2(classicModeBox.x+classicModeBox.width/2, classicModeBox.y), classicMode);
		selectors.add(tutorSelector);
		selectors.add(endlessSelector);
		selectors.add(classicSelector);
		endlessModeBox = menuButtons.get(5);
		classicModeBox = menuButtons.get(6);
		retroFlavorBox = menuButtons.get(7);
		downtempoFlavorBox = menuButtons.get(8);
		coolFlavorBox = menuButtons.get(9);
		
		modeMenuText = new MenuText(new Vector2(menuButtons.get(0).x, menuButtons.get(0).y), new Vector2(Gdx.graphics.getWidth()/4, menuButtons.get(0).y), "mode", this);
		hiraganaMenuText = new MenuText(new Vector2(menuButtons.get(1).x, menuButtons.get(1).y), new Vector2(Gdx.graphics.getWidth()/4, menuButtons.get(1).y), "hiragana", this);
		katakanaMenuText = new MenuText(new Vector2(menuButtons.get(2).x, menuButtons.get(2).y), new Vector2(Gdx.graphics.getWidth()/4, menuButtons.get(2).y), "katakana", this);
		startMenuText = new MenuText(new Vector2(menuButtons.get(3).x, menuButtons.get(3).y), new Vector2(Gdx.graphics.getWidth()/4, menuButtons.get(3).y), "start", this);
		
		
		hiraganaToggle = new Rectangle(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-standardWidth, standardWidth, standardWidth);
		katakanaToggle = new Rectangle(Gdx.graphics.getWidth()/1.5f, Gdx.graphics.getHeight()-standardWidth, standardWidth, standardWidth);
		hiraganaDiacriticsToggle = new Rectangle(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/5, standardWidth, standardWidth);
		hiraganaCombinationsToggle = new Rectangle(Gdx.graphics.getWidth()/1.5f, Gdx.graphics.getHeight()/5, standardWidth, standardWidth);
		katakanaDiacriticsToggle = new Rectangle(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/5, standardWidth, standardWidth);
		katakanaCombinationsToggle = new Rectangle(Gdx.graphics.getWidth()/1.5f, Gdx.graphics.getHeight()/5, standardWidth, standardWidth);
		

		
		//kanaSelector = new KanaSelector(Constants.standardKana);
		blocks = new Array<Block>();
		currentPolygon = new CyanPolygon(new Vector2(128,128*2), standardWidth, standardWidth);
		currentTetromino = currentPolygon.getBlocks();
		mainMenuShowing = true;
		gameOptionsSelected = true;
		//create kana menu elements
		standardHiraganaElements = KanaMenuElement.createHiraganaMenuElements();
		standardKatakanaElements = KanaMenuElement.createKatakanaMenuElements();
		
		aColumn = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x, 
				standardHiraganaElements.get(0).getBasePosition().y+(Gdx.graphics.getWidth()*0.05f),
				halfWidth,
				halfWidth);
		kColumn = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x+halfWidth, 
				standardHiraganaElements.get(0).getBasePosition().y+halfWidth,halfWidth,halfWidth);
		sColumn = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x+halfWidth*2, 
				standardHiraganaElements.get(0).getBasePosition().y+halfWidth,halfWidth,halfWidth);
		tColumn = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x+halfWidth*3, 
				standardHiraganaElements.get(0).getBasePosition().y+halfWidth,halfWidth,halfWidth);
		nColumn = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x+halfWidth*4, 
				standardHiraganaElements.get(0).getBasePosition().y+halfWidth,halfWidth,halfWidth);
		hColumn = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x+halfWidth*5, 
				standardHiraganaElements.get(0).getBasePosition().y+halfWidth,halfWidth,halfWidth);
		mColumn = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x+halfWidth*6, 
				standardHiraganaElements.get(0).getBasePosition().y+halfWidth,halfWidth,halfWidth);
		yColumn = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x+halfWidth*7, 
				standardHiraganaElements.get(0).getBasePosition().y+halfWidth,halfWidth,halfWidth);
		rColumn = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x+halfWidth*8, 
				standardHiraganaElements.get(0).getBasePosition().y+halfWidth,halfWidth,halfWidth);
		wColumn = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x+halfWidth*9, 
				standardHiraganaElements.get(0).getBasePosition().y+halfWidth,halfWidth,halfWidth);
		lastColumn = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x+halfWidth*10, 
				standardHiraganaElements.get(0).getBasePosition().y+halfWidth,halfWidth,halfWidth);
		
		aRow = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x-halfWidth, 
				standardHiraganaElements.get(0).getBasePosition().y,halfWidth,halfWidth);
		iRow = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x-halfWidth, 
				standardHiraganaElements.get(0).getBasePosition().y-halfWidth,halfWidth,halfWidth);
		uRow = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x-halfWidth, 
				standardHiraganaElements.get(0).getBasePosition().y-halfWidth*2,halfWidth,halfWidth);
		eRow = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x-halfWidth, 
				standardHiraganaElements.get(0).getBasePosition().y-halfWidth*3,halfWidth,halfWidth);
		oRow = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x-halfWidth, 
				standardHiraganaElements.get(0).getBasePosition().y-halfWidth*4,halfWidth,halfWidth);
		
		allRowsAndColumns = new Rectangle(standardHiraganaElements.get(0).getBasePosition().x-halfWidth, 
				standardHiraganaElements.get(0).getBasePosition().y+halfWidth,halfWidth,halfWidth);
	}
	
	Selector retroSelector, downtempoSelector, coolSelector, tutorSelector, endlessSelector, classicSelector;
	Array<Selector> selectors;
	Rectangle retroFlavorBox, downtempoFlavorBox, coolFlavorBox, tutorModeBox, endlessModeBox, classicModeBox; 
	Rectangle aColumn, kColumn, sColumn, tColumn, nColumn, hColumn, mColumn, yColumn, rColumn, wColumn, lastColumn;
	Rectangle aRow, iRow, uRow, eRow, oRow;
	Rectangle allRowsAndColumns;
	private boolean tutorKanaIntroduced;
	public void update(){
		//check for menu interaction
		for (Selector s : selectors) s.update();
		for (KanaMenuElement k : standardHiraganaElements) k.getSelector().update();
		for (KanaMenuElement k : standardKatakanaElements) k.getSelector().update();
		if (mainMenuShowing && !hiraganaOptionsSelected && !katakanaOptionsSelected){
			if (mouse.overlaps(menuButtons.get(0))){ //mode
				gameOptionsSelected = true;
				hiraganaOptionsSelected = false;
				katakanaOptionsSelected = false;
				resetMouse();
			}
			if (mouse.overlaps(menuButtons.get(1))){ //hiragana
				gameOptionsSelected = false;
				hiraganaOptionsSelected = true;
				katakanaOptionsSelected = false;
				resetMouse();
			}
			if (mouse.overlaps(menuButtons.get(2))){ //katakana
				gameOptionsSelected = false;
				hiraganaOptionsSelected = false;
				katakanaOptionsSelected = true;
				resetMouse();
			}
			if (mouse.overlaps(menuButtons.get(3))){ //start
				gameOptionsSelected = false;
				hiraganaOptionsSelected = false;
				katakanaOptionsSelected = false;
				mainMenuShowing = false;
				resetMouse();
			}
			
			if (mouse.overlaps(tutorModeBox)){
				tutorMode = true;
				tutorSelector.select();
				endlessMode = false;
				endlessSelector.unselect();
				classicMode = false;
				classicSelector.unselect();
				resetMouse();
			}
			if (mouse.overlaps(endlessModeBox)){
				tutorMode = false;
				tutorSelector.unselect();
				endlessMode = true;
				endlessSelector.select();
				classicMode = false;
				classicSelector.unselect();
				resetMouse();
			}
			if (mouse.overlaps(classicModeBox)){
				tutorMode = false;
				tutorSelector.unselect();
				endlessMode = false;
				endlessSelector.unselect();
				classicMode = true;
				classicSelector.select();
				resetMouse();
			}
			if (mouse.overlaps(retroFlavorBox)){
				retroFlavor = true;
				downtempoFlavor = false;
				coolFlavor = false;
				resetMouse();
			}
			if (mouse.overlaps(downtempoFlavorBox)){
				retroFlavor = false;
				downtempoFlavor = true;
				coolFlavor = false;
				resetMouse();
			}
			if (mouse.overlaps(coolFlavorBox)){
				retroFlavor = false;
				downtempoFlavor = false;
				coolFlavor = true;
				resetMouse();
			}
			if (mouse.overlaps(hiraganaToggle)){
				hiraganaEnabled = !hiraganaEnabled;
				System.out.println("hiragana toggled, hiraganaEnabled is now " + hiraganaEnabled);
				resetMouse();
			}
			if (mouse.overlaps(katakanaToggle)){
				katakanaEnabled = !katakanaEnabled;
				System.out.println("katakana toggled, katakana enabled is now " + katakanaEnabled);
				resetMouse();
			}
			//mouse moved, not clicked:
			if (mouseMoved.overlaps(menuButtons.get(0))){ //mode
				modeMenuText.startMovingLeft();
				hiraganaMenuText.startMovingRight();
				katakanaMenuText.startMovingRight();
				startMenuText.startMovingRight();
			}
			if (mouseMoved.overlaps(menuButtons.get(1))){ //hiragana
				modeMenuText.startMovingRight();
				hiraganaMenuText.startMovingLeft();
				katakanaMenuText.startMovingRight();
				startMenuText.startMovingRight();
			}
			if (mouseMoved.overlaps(menuButtons.get(2))){ //katakana
				modeMenuText.startMovingRight();
				hiraganaMenuText.startMovingRight();
				katakanaMenuText.startMovingLeft();
				startMenuText.startMovingRight();
			}
			if (mouseMoved.overlaps(menuButtons.get(3))){ //start
				modeMenuText.startMovingRight();
				hiraganaMenuText.startMovingRight();
				katakanaMenuText.startMovingRight();
				startMenuText.startMovingLeft();
				
			}
			coloredLines.get(0).x = startMenuText.getPosition().x-Gdx.graphics.getWidth();
			coloredLines.get(1).x = hiraganaMenuText.getPosition().x-Gdx.graphics.getWidth();
			coloredLines.get(2).x = modeMenuText.getPosition().x-Gdx.graphics.getWidth();
			coloredLines.get(3).x = katakanaMenuText.getPosition().x-Gdx.graphics.getWidth();
			
			modeMenuText.update();
			hiraganaMenuText.update();
			katakanaMenuText.update();
			startMenuText.update();
		}
		if (gameOptionsSelected){
			
		}
		if (hiraganaOptionsSelected){
			for (KanaMenuElement k : standardHiraganaElements){
				if (k.getBounds().overlaps(mouse)){
					resetMouse();
					k.flip();
				}
			}
			if (mouse.overlaps(aRow)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements){
					if (k.getKana().contains("a") || k.getKana().equals("h.n")) k.flip();
				}
			}
			if (mouse.overlaps(iRow)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements){
					if (k.getKana().contains("i")) k.flip();
				}
			}
			if (mouse.overlaps(uRow)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements){
					if (k.getKana().contains("u")) k.flip();
				}
			}
			if (mouse.overlaps(eRow)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements){
					if (k.getKana().contains("e")) k.flip();
				}
			}
			if (mouse.overlaps(oRow)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements){
					if (k.getKana().contains("o")) k.flip();
				}
			}
			if (mouse.overlaps(aColumn)){
				resetMouse();
				for (int i = 0; i < 5; i++){
					standardHiraganaElements.get(i).flip();
				}
			}
			if (mouse.overlaps(kColumn)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements)
					if (k.getKana().contains("h.k")) k.flip();
			}
			if (mouse.overlaps(sColumn)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements)
					if (k.getKana().contains("h.s")) k.flip();
			}
			if (mouse.overlaps(tColumn)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements)
					if (k.getKana().contains("h.t") || k.getKana().contains("h.chi")) k.flip();
			}
			if (mouse.overlaps(hColumn)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements)
					if (k.getKana().contains("h.h")||k.getKana().contains("h.fu")) k.flip();
			}
			if (mouse.overlaps(mColumn)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements)
					if (k.getKana().contains("h.m")) k.flip();
			}
			if (mouse.overlaps(rColumn)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements)
					if (k.getKana().contains("h.r")) k.flip();
			}
			if (mouse.overlaps(yColumn)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements)
					if (k.getKana().contains("h.y")) k.flip();
			}
			if (mouse.overlaps(wColumn)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements)
					if (k.getKana().contains("h.w")) k.flip();
			}
			if (mouse.overlaps(nColumn)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements)
					if (k.getKana().contains("h.n")&&!k.getKana().equals("h.n")) k.flip();
			}
			if (mouse.overlaps(lastColumn)){
				resetMouse();
				for(KanaMenuElement k : standardHiraganaElements)
					if (k.getKana().equals("h.n")) k.flip();
			}
			if (mouse.overlaps(allRowsAndColumns)){
				resetMouse();
				for (KanaMenuElement k : standardHiraganaElements)
					k.flip();
			}
			if (mouse.overlaps(hiraganaDiacriticsToggle)){
				hiraganaDiacriticsEnabled = !hiraganaDiacriticsEnabled;
				System.out.println("h dia now " + hiraganaDiacriticsEnabled);
				resetMouse();
			}
			if (mouse.overlaps(hiraganaCombinationsToggle)){
				hiraganaCombinationsEnabled = !hiraganaCombinationsEnabled;
				System.out.println("h combo now " + hiraganaCombinationsEnabled);
				resetMouse();
			}
		}
		if (katakanaOptionsSelected){
			for (KanaMenuElement k : standardKatakanaElements){
				if (k.getBounds().overlaps(mouse)){
					resetMouse();
					k.flip();
				}
			}
			if (mouse.overlaps(aRow)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements){
					if (k.getKana().contains("a") || k.getKana().equals("h.n")) k.flip();
				}
			}
			if (mouse.overlaps(iRow)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements){
					if (k.getKana().contains("i")) k.flip();
				}
			}
			if (mouse.overlaps(uRow)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements){
					if (k.getKana().contains("u")) k.flip();
				}
			}
			if (mouse.overlaps(eRow)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements){
					if (k.getKana().contains("e")) k.flip();
				}
			}
			if (mouse.overlaps(oRow)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements){
					if (k.getKana().contains("o")) k.flip();
				}
			}
			if (mouse.overlaps(aColumn)){
				resetMouse();
				for (int i = 0; i < 5; i++){
					standardKatakanaElements.get(i).flip();
				}
			}
			if (mouse.overlaps(kColumn)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements)
					if (k.getKana().contains("k.k")) k.flip();
			}
			if (mouse.overlaps(sColumn)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements)
					if (k.getKana().contains("k.s")) k.flip();
			}
			if (mouse.overlaps(tColumn)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements)
					if (k.getKana().contains("k.t") || k.getKana().contains("k.chi")) k.flip();
			}
			if (mouse.overlaps(hColumn)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements)
					if (k.getKana().contains("k.h")||k.getKana().contains("k.fu")) k.flip();
			}
			if (mouse.overlaps(mColumn)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements)
					if (k.getKana().contains("k.m")) k.flip();
			}
			if (mouse.overlaps(rColumn)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements)
					if (k.getKana().contains("k.r")) k.flip();
			}
			if (mouse.overlaps(yColumn)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements)
					if (k.getKana().contains("k.y")) k.flip();
			}
			if (mouse.overlaps(wColumn)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements)
					if (k.getKana().contains("k.w")) k.flip();
			}
			if (mouse.overlaps(nColumn)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements)
					if (k.getKana().contains("k.n")&&!k.getKana().equals("k.n")) k.flip();
			}
			if (mouse.overlaps(lastColumn)){
				resetMouse();
				for(KanaMenuElement k : standardKatakanaElements)
					if (k.getKana().equals("k.n")) k.flip();
			}
			if (mouse.overlaps(allRowsAndColumns)){
				resetMouse();
				for (KanaMenuElement k : standardKatakanaElements)
					k.flip();
			}
			if (mouse.overlaps(katakanaDiacriticsToggle)){
				katakanaDiacriticsEnabled = !katakanaDiacriticsEnabled;
				System.out.println("k dia now " + katakanaDiacriticsEnabled);
				resetMouse();
			}
			if (mouse.overlaps(katakanaCombinationsToggle)){
				katakanaCombinationsEnabled = !katakanaCombinationsEnabled;
				System.out.println("k combo now " + katakanaCombinationsEnabled);
				resetMouse();
			}
		}
		//update game
		if (!mainMenuShowing){
			if (!kanaSelectorInitialized){
				ArrayList<String> selectedKana = new ArrayList<String>();
				if (hiraganaEnabled) for (KanaMenuElement k : standardHiraganaElements){
					if (k.getSelector().isSelected()){
						selectedKana.add(k.getKana());
						if (hiraganaDiacriticsEnabled && hasDiacritic(k.getKana())){
							System.out.println("Adding diacritic kana!!!!!");
							selectedKana.add(getDiacritic(k.getKana())); 
						}
						if (hiraganaCombinationsEnabled && hasCombination(k.getKana())){
							System.out.println("Adding combination kana!");
							addCombinations(selectedKana, k.getKana(), hiraganaDiacriticsEnabled, true);
						}
					}
				}
				if (katakanaEnabled) for (KanaMenuElement k : standardKatakanaElements){
					if (k.getSelector().isSelected()){
						selectedKana.add(k.getKana());
						
						if (katakanaDiacriticsEnabled && hasDiacritic(k.getKana())){
							System.out.println("Adding diacritic kana!!!!!");
							selectedKana.add(getDiacritic(k.getKana()));
						}
						if (katakanaCombinationsEnabled && hasCombination(k.getKana())){
							addCombinations(selectedKana, k.getKana(), katakanaDiacriticsOn, false);
						}
						
					}
				}
				kanaSelector = new KanaSelector(selectedKana);
				tutorKanaSelector = new KanaSelector(new ArrayList<String>());
				kanaSelectorInitialized=true;
			}
			if (!musicStarted){
				//FileHandle musicHandle = Gdx.files.internal("mazetheme.wav");
				//for browser version: 
				FileHandle musicHandle = Gdx.files.internal("mazetheme.mp3");
				//debug:
				bgm = Gdx.audio.newMusic(musicHandle);
				bgm.setLooping(true);
				bgm.play();
				musicStarted = true;
			}
			for(Block b : blocks){
		
			b.update();
		}
			//TODO blocks cant reach edge of field in html version
		boolean leftOfField=false,rightOfField=false;
		for(Block b : currentTetromino){
			if(b.getPosition().x<(fieldX*tetrisUnit)-tetrisUnit/2)
				leftOfField = true;
			else if(b.getPosition().x>(fieldX*tetrisUnit)+(fieldWidth*tetrisUnit)-tetrisUnit+tetrisUnit/2)
				rightOfField=true;
		}
		if(leftOfField){
			for(Block b : currentTetromino)
				b.getPosition().x+=tetrisUnit;
			leftOfField = false;
		}
		else if(rightOfField){
			for(Block b : currentTetromino)
				b.getPosition().x-=tetrisUnit;
			rightOfField = false;
		}
		checkIfRowFilled();
		currentTime+=Gdx.graphics.getDeltaTime();
		if (currentTime>=fallTimer){
			fall(); 
			currentTime = 0;
		}
		}
		
	}
	
	boolean blocksDetected = false;
	Array<Integer> yClearedInt = new Array<Integer>();
	public void checkIfRowFilled(){
		rowBlockCount = new int[20];
		yCleared = new Array<Float>();
		yClearedInt = new Array<Integer>();
//		for(Block b : blocks){
//			rowBlockCount[(int) ((b.getPosition().y/tetrisUnit))]++;
//		} OLD this does not work on HTML version
		blocksDetected = false;
		for (Block b :blocks){
			for (int i = 0; i < 20; i++){
				//if ((b.getPosition().y/tetrisUnit > i -0.49)&&(b.getPosition().y/tetrisUnit < i + 0.49))
				// didnt work in html^
				//if (b.getPosition().y/tetrisUnit==i)
				//didnt work in html^
				//	rowBlockCount[i]++;
				if (b.getSimplifiedPosition()==i){
					rowBlockCount[i]++;
					blocksDetected = true;
				}
			}
		}
		if (!classicMode && rowBlockCount[19]>0){
			blocks.clear();
		}
		int numBlocksMarked = 0;
		for (int i = 0; i <20; i++){
			if (rowBlockCount[i]==10){
//				System.out.println("Blocks size is " + blocks.size);
				blockIterator = blocks.iterator();
//				System.out.println(blockIterator.toString());
				int count = 0;

				for (Block b : blocks){
					count++;
					//if ((b.getPosition().y/tetrisUnit)==i){ didnt work in html
					if ((b.getSimplifiedPosition()==i)){
						b.markForRemoval();
						numBlocksMarked++;
						floatIterator = yCleared.iterator();
						if (yCleared.size==0) yCleared.add(b.getPosition().y);
						else{
							if(!yCleared.contains(new Float(b.getPosition().y), false)){
								yCleared.add(b.getPosition().y);
//								System.out.println("Added " + b.getPosition().y + " to yCleared. New yCleared size: " + yCleared.size);
							}
						}
						if (yClearedInt.size == 0) yClearedInt.add(b.getSimplifiedPosition());
						else {
							if (!yClearedInt.contains(b.getSimplifiedPosition(), false)){
								yClearedInt.add(b.getSimplifiedPosition());
							}
						}
					}
				}
//				if(yCleared.size!=0)
//					System.out.println("Ycleared size is " + yCleared.size);
				}
			}
//			if (numBlocksMarked!=0) System.out.println("marked: " + numBlocksMarked);
			blockIterator=blocks.iterator();
			int numBlocksRemoved = 0;
			int numberOfBlocksChecked = 0;
			boolean printedWhileMessage = false;
			while(blockIterator.hasNext()){
				b = blockIterator.next();
				numberOfBlocksChecked++;
				if (b.shouldBeRemoved()){
					if(!printedWhileMessage){
//						System.out.println("blocks.size is " + blocks.size);
						printedWhileMessage = true;
					}
					blockIterator.remove();
					numBlocksRemoved++;
				}
			}
			if (printedWhileMessage){
//				System.out.println("Checked " + numberOfBlocksChecked);
			}
			if (numBlocksRemoved > 0 && numBlocksRemoved % 10 != 0){
//				System.out.println("didnt remove ten blocks... removed " + numBlocksRemoved);
				ArrayList<String> l;
				l = null;
				l.size();
			}
			else if (numBlocksRemoved>0){
//				System.out.println("Removed " + numBlocksRemoved);
				rowsCleared++;
			}
			int count = 0;
			//The following was removed because it did not work in html version
//			for (Float f : yCleared){
//				count++;
//				System.out.println("entering for(floatf), ycleared value is " + f);
//				for (Block b:  blocks){
//					if (b.getPosition().y> f){
//						System.out.println("Moving block at " + b.getPosition().x + " , " + b.getPosition().y);
//						//b.getPosition().y-=tetrisUnit;
//						b.setNecessaryDisplacement(-tetrisUnit);
//					}
//				}
//				System.out.println("Entered yCleared " + count + " times this frame");
//			}
//			yCleared.clear();
			for(Integer j : yClearedInt){
				count++;
				for (Block b: blocks){
					if (b.getSimplifiedPosition()>j){
						b.setNecessaryDisplacement(-tetrisUnit);
					}
				}
			}
			for (Block b : blocks){
				b.displace();
			}
	}
	
	public void fall(){
//		System.out.println(currentTetromino.size);
		for (Block b : currentTetromino){
			b.fall();
			b.update();
			for (Block b2: blocks){
				if (b.getBounds().overlaps(b2.getBounds())){
					currentTetrominoLocked = true;
				}
			}
			if(b.getPosition().y < (fieldY*tetrisUnit)+tetrisUnit-tetrisUnit/2){
				currentTetrominoLocked = true;
			}
		}
		if (currentTetrominoLocked){
			blockIterator = currentTetromino.iterator();
			while (blockIterator.hasNext()){
				b = blockIterator.next();
				b.unfall();
				blocks.add(new Block(new Vector2(b.getPosition().x, b.getPosition().y), b.getWidth(), b.getHeight(), b.getColor()));
				currentTetrominoLocked = false;
			}
			getNextTetromino();
		}
	}
	
	public void getNextTetromino(){
		currentTetromino = new Array<Block>();
		int randomInt = random.nextInt(7);
		if (randomInt==0) currentPolygon = new CyanPolygon(
				new Vector2(fieldX*tetrisUnit, fieldY*tetrisUnit+fieldHeight*tetrisUnit), standardWidth,standardWidth);
		else if (randomInt==1) currentPolygon = new GreenPolygon(
				new Vector2(fieldX*tetrisUnit, fieldY*tetrisUnit+fieldHeight*tetrisUnit), standardWidth,standardWidth);
		else if (randomInt==2) currentPolygon = new IndigoPolygon(
				new Vector2(fieldX*tetrisUnit, fieldY*tetrisUnit+fieldHeight*tetrisUnit), standardWidth,standardWidth);
		else if (randomInt==3) currentPolygon = new OrangePolygon(
				new Vector2(fieldX*tetrisUnit, fieldY*tetrisUnit+fieldHeight*tetrisUnit), standardWidth,standardWidth);
		else if (randomInt==4) currentPolygon = new PurplePolygon(
				new Vector2(fieldX*tetrisUnit, fieldY*tetrisUnit+fieldHeight*tetrisUnit), standardWidth,standardWidth);
		else if (randomInt==5) currentPolygon = new RedPolygon(
				new Vector2(fieldX*tetrisUnit, fieldY*tetrisUnit+fieldHeight*tetrisUnit), standardWidth,standardWidth);
		else if (randomInt==6) currentPolygon = new YellowPolygon(
				new Vector2(fieldX*tetrisUnit, fieldY*tetrisUnit+fieldHeight*tetrisUnit), standardWidth,standardWidth);
		currentTetromino = currentPolygon.getBlocks();
		currentTetrominoLocked = false;
		for (Block b : currentTetromino){
//			System.out.println("new block: " + b.toString());
		}
		//if tutor mode and need new tutor kana, add from kana selector
		if (tutorMode && tutorModeKanaCount>4){
			currentTutorKana = kanaSelector.getRandomKana();
			tutorKanaSelector.addNewKana(currentTutorKana);
			tutorModeKanaCount = 0;
			tutorKanaIntroduced = false;
		}
		//if not tutor mode, get kana from kana selector
		if (!tutorMode){
			currentKana = kanaSelector.getRandomKana();
		}
		//else if tutor mode, get kana from tutorKanaSelector
		else {
				if (!tutorKanaIntroduced){
					currentKana = currentTutorKana;
					tutorKanaIntroduced = true;
					tutorKanaSelector.moveKanaToUsed(currentKana);
				}
				else
					currentKana = tutorKanaSelector.getRandomKana();
			}
		kanaEntered = false;
		if (sIsDown){
			sCanBeEntered = false;
		}
		moveAtRegularSpeed();
	}
	
	public void moveLeft(){
		for (Block b : currentTetromino){
			b.moveLeft();
			b.update();
		}
		boolean colliding = false;
		for(Block b: currentTetromino){
			for (Block bb : blocks){
				if(b.getBounds().overlaps(bb.getBounds())){
					colliding = true;
				}
			}
		}
		if (colliding){
			for (Block b: currentTetromino){
				b.moveRight();
			}
		}
	}
	
	public void moveRight(){
		for (Block b: currentTetromino){
			b.moveRight();
			b.update();
		}
		boolean colliding = false;
		for (Block b : currentTetromino){
			for (Block bb : blocks){
				if (b.getBounds().overlaps(bb.getBounds())){
					colliding = true;
				}
			}
		}
		if (colliding){
			for (Block b :currentTetromino){
				b.moveLeft();
			}
		}
	}
	
	public void rotateClockwise(){
		currentPolygon.rotateClockwise();
	}
	
	public void rotateCounterClockwise(){
		currentPolygon.rotateCounterClockwise();
	}
	
	public void enterKey(String key){
		enteredKana += key;
		System.out.println("Entered kana is now "+ enteredKana);
	}
	
	public void backspace(){
		if (enteredKana.length()>0) enteredKana = enteredKana.substring(0, enteredKana.length()-1);
		System.out.println("Entered kana is now "+ enteredKana);
		hiraganaOptionsSelected = false;
		katakanaOptionsSelected = false;
		gameOptionsSelected = false;
	}
	
	public void checkKana(){
		String currentKanaConverted = currentKana;
		if (currentKana.contains("\"")){
			currentKanaConverted = currentKana.replace(".k", ".g").replace("s", "z").replace("t", "d").replace(".h", ".b").replace("\"", "");
			if (currentKana.contains("shi")) currentKanaConverted = currentKana.replace("shi", "ji").replace("\"", "");
			if (currentKana.contains("chi")) currentKanaConverted = currentKana.replace("chi", "ji").replace("\"","");
			if (currentKana.contains("tsu")) currentKanaConverted = currentKana.replace("tsu", "zu").replace("\"","");
			//TODO p kana
		}
		System.out.println(currentKanaConverted.contains("chi"));
		if (currentKana.contains("-")){
			if (currentKanaConverted.contains("shi") || currentKanaConverted.contains("chi") || currentKana.contains("ji")){
				System.out.println("removing y from " + currentKanaConverted);
				currentKanaConverted = currentKanaConverted.replace("y", "");
				System.out.println("CurrentKanaconverted is now " + currentKanaConverted);
			}
			currentKanaConverted = currentKanaConverted.replace("i-","");
			System.out.println("currentKanaConverted is: " + currentKanaConverted);
			System.out.println("check kana algorithm is looking at " + currentKanaConverted.substring(2, currentKanaConverted.length()));
		}
		if (enteredKana.equals(currentKanaConverted.substring(2, currentKanaConverted.length()))){
			kanaEntered = true;
			enteredKana = "";
			correctAnswers++;
			//System.out.println("Checking if current kana (" + currentKanaConverted + ") equals currentTutorKana (" + currentTutorKana + ")");
			if (currentKanaConverted.equals(currentTutorKana)){
					tutorModeKanaCount++;
					//System.out.println("Current kana equals current tutor kana");
			}
		}
		else{
			incorrectAnswers++;
		}
		System.out.println("Entered kana is" + enteredKana);
		System.out.println("Current kana is " + currentKanaConverted);
	}
	
	public void moveAtRegularSpeed(){
		fallTimer = 1f;
		System.out.println("Moving at regular speed");
	}
	
	public void moveAtIncreasedSpeed(){
		fallTimer = 0.05f;
	}
	
	public void setSDown(){
		sIsDown = true;
	}
	
	public boolean isSDown(){
		return sIsDown;
	}
	public boolean canSBeEntered(){
		return sCanBeEntered;
	}
	
	public void setSCanBeEntered(){
		sCanBeEntered = true;
	}
	
	public Array<Rectangle> getFieldRectangles(){
		Array<Rectangle> toReturn = new Array<Rectangle>();
		toReturn.add(fieldBottom);
		toReturn.add(fieldLeft);
		toReturn.add(fieldRight);
		toReturn.add(scoreBoxBottom);
		toReturn.add(scoreBoxLeft);
		toReturn.add(scoreBoxRight);
		toReturn.add(scoreBoxTop);
		return toReturn;
	}
	
	public Array<Rectangle> getCollisionBoxes(){
		Array<Rectangle> allBoxes = new Array<Rectangle>();
		for(Rectangle r: collisionBoxes){
			allBoxes.add(r);
		}
		for(Block b : blocks){
			allBoxes.add(b.getBounds());
		}
		//return collisionBoxes;
		return allBoxes;
	}
	
	public Array<Rectangle> getMenuButtons(){
		return menuButtons;
	}
	
	public Array<Block> getBlocks(){
		return blocks;
	}
	
	public Array<Block> getCurrentTetromino(){
		return currentTetromino;
	}
	
	public int[] getRowBlockCount(){
		return rowBlockCount;
	}
	
	public boolean isKanaEntered(){
		return kanaEntered;
	}
	
	public String getCurrentKana(){
		return currentKana;
	}
	
	public String getCurrentTutorKana(){
		return currentTutorKana;
	}
	
	public boolean isMainMenuShowing(){
		return mainMenuShowing;
	}
	
	//for clicks
	public void setMousePosition(int x, int y){
		mouse.x = x;
		mouse.y = y;
	}
	
	public void resetMouse(){
		mouse.x = -128;
		mouse.y = -128;
	}
	
	//for mouse movement
	public void moveMouse(int x, int y){
		mouseMoved.x = x;
		mouseMoved.y = y;
	}
	
	public String getEnteredKana(){
		return enteredKana;
	}
	
	public Rectangle getScoreBox(){
		return new Rectangle(scoreX, scoreY, scoreWidth, scoreHeight);
	}
	
	public Rectangle getMouse(){
		return mouse;
	}
	
	public int getNumCorrectAnswers(){
		return correctAnswers;
	}
	
	public int getNumIncorrectAnswers(){
		return incorrectAnswers;
	}
	
	public int getNumRowsCleared(){
		return rowsCleared;
	}
	
	public boolean isHiraganaMenuShowing(){
		return hiraganaOptionsSelected;
	}
	
	public boolean isKatakanaMenuShowing(){
		return katakanaOptionsSelected;
	}
	
	public Array<Selector> getSelectors(){
		return selectors;
	}
	
	public Array<KanaMenuElement> getStandardHiraganaElements(){
		return standardHiraganaElements;
	}
	
	public Array<KanaMenuElement> getStandardKatakanaElements(){
		return standardKatakanaElements;
	}
	
	public Array<Vector2> getRightArrows(){
		Array<Vector2> array = new Array<Vector2>();
		array.add(new Vector2(aRow.x, aRow.y));
		array.add(new Vector2(iRow.x, iRow.y));
		array.add(new Vector2(uRow.x, uRow.y));
		array.add(new Vector2(eRow.x, eRow.y));
		array.add(new Vector2(oRow.x, oRow.y));
	
		return array;
	}
	
	public Array<Vector2> getDownArrows(){
		Array<Vector2> array = new Array<Vector2>();
		array.add(new Vector2(aColumn.x, aColumn.y));
		array.add(new Vector2(kColumn.x, kColumn.y));
		array.add(new Vector2(sColumn.x, sColumn.y));
		array.add(new Vector2(tColumn.x, tColumn.y));
		array.add(new Vector2(hColumn.x, hColumn.y));
		array.add(new Vector2(mColumn.x, mColumn.y));
		array.add(new Vector2(nColumn.x, nColumn.y));
		array.add(new Vector2(rColumn.x, rColumn.y));
		array.add(new Vector2(yColumn.x, yColumn.y));
		array.add(new Vector2(wColumn.x, wColumn.y));
		array.add(new Vector2(lastColumn.x, lastColumn.y));
	
		return array;
	}
	
	public Vector2 getAllRowsAndColumnsBox(){
		return new Vector2(allRowsAndColumns.x, allRowsAndColumns.y);
	}
	
	public void clearAll(){
			blocks.clear();
	}
	
	public boolean areBlocksDetected(){
		return blocksDetected;
	}
	
	//this method finds the diacritic version of the given kana
	public String getDiacritic(String kana){
		String toReturn ="";
		toReturn = kana+"\"";
		return toReturn;
	}
	
	//this does the same as the above method, but replaces the h with a p
	public String getHandakuten(String kana){
		return kana.replace(".h",".p");
	}
	
	public void addCombinations(ArrayList<String> selectedKana, String kana, boolean diacriticsOn, boolean hiragana){
		String prefix = "";
		if (hiragana) prefix = "h.";
		else prefix = "k.";
		if (kana.contains("ki")){
			selectedKana.add(prefix+"ki-yo");
			selectedKana.add(prefix+"ki-yu");
			selectedKana.add(prefix+"ki-ya");
			if (diacriticsOn){
				selectedKana.add(prefix+"ki\"-yo");
				selectedKana.add(prefix+"ki\"-yu");
				selectedKana.add(prefix+"ki\"-ya");
			}
		}
		if (kana.contains("shi")){
			selectedKana.add(prefix+"shi-yo");
			selectedKana.add(prefix+"shi-ya");
			selectedKana.add(prefix+"shi-yu");
		}
		if (kana.contains("chi")){
			selectedKana.add(prefix+"chi-yo");
			selectedKana.add(prefix+"chi-ya");
			selectedKana.add(prefix+"chi-yu");
		}
		if (kana.contains("ni")){
			selectedKana.add(prefix+"ni-yo");
			selectedKana.add(prefix+"ni-ya");
			selectedKana.add(prefix+"ni-yu");
		}
		if (kana.contains("ni")){
			selectedKana.add(prefix+"ni-yo");
			selectedKana.add(prefix+"ni-ya");
			selectedKana.add(prefix+"ni-yu");
		}
		if ((!kana.contains("chi") && kana.contains("hi"))){
			selectedKana.add(prefix+"hi-yo");
			selectedKana.add(prefix+"hi-ya");
			selectedKana.add(prefix+"hi-yu");
			if (diacriticsOn){
				selectedKana.add(prefix+"hi\"-yo");
				selectedKana.add(prefix+"hi\"-yu");
				selectedKana.add(prefix+"hi\"-ya");
			}
		}
		if (kana.contains("ni")){
			selectedKana.add(prefix+"ni-yo");
			selectedKana.add(prefix+"ni-ya");
			selectedKana.add(prefix+"ni-yu");
		}
		if (kana.contains("ri")){
			selectedKana.add(prefix+"ri-yo");
			selectedKana.add(prefix+"ri-ya");
			selectedKana.add(prefix+"ri-yu");
		}
	}
	
	
	//returns true if kana has a diacritic counterpart
	public boolean hasDiacritic(String kana){
//		System.out.println("checking if has diacritic");
//		System.out.println("kana.charAt(2)=="+kana.charAt(2));
		return (kana.charAt(2)=='k' || kana.charAt(2)=='s' || kana.contains("chi") || kana.contains("shi") 
				|| kana.contains("tsu") || kana.charAt(2)=='t' || kana.charAt(2) == 'h');
	}
	
	//returns true if kana has a combination
	public boolean hasCombination(String kana){
		return (kana.charAt(2)=='k' || kana.charAt(2)=='h' || kana.contains("chi") || kana.contains("shi") 
				|| kana.charAt(2)=='r' || kana.charAt(2)=='n' || kana.charAt(2) == 'h');
	}
	
	public Rectangle getHiraganaDiaToggle(){
		return hiraganaDiacriticsToggle;
	}
	public Rectangle getHiraganaComboToggle(){
		return hiraganaCombinationsToggle;
	}
	
	public Rectangle getKatakanaDiaToggle(){
		return katakanaDiacriticsToggle;
	}
	public Rectangle getKatakanaComboToggle(){
		return katakanaCombinationsToggle;
	}

	MenuText[] menuTextArray = new MenuText[4];
	public MenuText[] getMenuTextObjects(){
		menuTextArray[0]=modeMenuText;
		menuTextArray[1]=hiraganaMenuText;
		menuTextArray[2]=katakanaMenuText;
		menuTextArray[3]=startMenuText;
		return menuTextArray;
	}
	
	public void setSoundPlayed(boolean soundPlayed){
		this.soundPlayed = soundPlayed;
	}
	
	public void playSound(String sound){
		if (sound.equals("start")){
			startSound.play(1);
		}
		if (sound.equals("hiragana")){
			hiraganaSound.play(1);
		}
		if (sound.equals("katakana")){
			katakanaSound.play(1);
		}
		if (sound.equals("mode")){
			modeSound.play(1);
		}
	}
	
	public Array<Rectangle> getColoredLines(){
		return coloredLines;
	}

	
}
