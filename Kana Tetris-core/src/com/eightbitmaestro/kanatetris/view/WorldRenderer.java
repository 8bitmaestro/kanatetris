package com.eightbitmaestro.kanatetris.view;

import java.util.Arrays;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.eightbitmaestro.kanatetris.constants.Constants;
import com.eightbitmaestro.kanatetris.model.Block;
import com.eightbitmaestro.kanatetris.model.KanaMenuElement;
import com.eightbitmaestro.kanatetris.model.MenuText;
import com.eightbitmaestro.kanatetris.model.Selector;

public class WorldRenderer {
	
	World world;
	Array<Block> blocks;
	Block b; Iterator<Block> blockIterator;
	Array<Rectangle> fieldRectangles, collisionBoxes;
	Rectangle r; Iterator<Rectangle> rectangleIterator;
	SpriteBatch levelBatch, uiBatch;
	BitmapFont font;
	ShapeRenderer shapeRenderer, uiShapeRenderer;
	OrthographicCamera camera;
	float colorR, colorG, colorB;
	boolean debug = false;
	Array<Selector> selectors;
	float tetrisUnit = Gdx.graphics.getWidth()/10;
	float standardWidth = Gdx.graphics.getWidth()*0.1f;
	float halfWidth = Gdx.graphics.getWidth()*0.05f;
	
	public WorldRenderer(World world){
		Gdx.graphics.setTitle("Kana Tetris (Alpha version 0.001)");
		this.world = world;
		blocks = new Array<Block>();
		levelBatch = new SpriteBatch();
		uiBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		uiShapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth()*3, Gdx.graphics.getHeight()*3);
		redIsTarget = true;
		differenceSet = false;
		font = new BitmapFont();
		if (Gdx.graphics.getWidth() < 800 )
			font.scale(2);
		else
			font.scale(Gdx.graphics.getWidth()/600);
		loadTextures();
	}
	
	
	boolean hiragana; //used to decide whether to draw katakana or hiragana
	public void render(){
		Gdx.gl20.glClearColor(0, 0, 0, 0);
		Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);
		
		tetrisUnit = Gdx.graphics.getWidth()/10;
		standardWidth = Gdx.graphics.getWidth()*0.1f;
		halfWidth = Gdx.graphics.getWidth()*0.05f;
		
		//uiBatch.begin(); uiBatch.draw(standardKatakanaRegions[44], 10, 20); uiBatch.end(); 
		//DRAWING MENU
		selectors = world.getSelectors();
		if (world.isMainMenuShowing()){
			uiBatch.begin();
			//uiBatch.draw(menu, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			String[] mbs = {"Mode", "Hiragana", "Katakana", "Start", "Tutor Mode", "Endless Mode", "Classic Mode", "Retro Flavor", "Downtempo Flavor", "Cool Flavor"}; //menu button strings
			int count = 0;
			if (world.isHiraganaMenuShowing()){
				font.draw(uiBatch, "Press backspace to return", 64, 64);
				for(KanaMenuElement k : world.getStandardHiraganaElements()){
					uiBatch.draw(
					standardHiraganaRegions[Arrays.asList(Constants.standardKana).indexOf(k.getKana().substring(2, k.getKana().length()))],
					k.getBasePosition().x, k.getBasePosition().y, 32, 32);
					uiBatch.setColor(1,1,1,k.getSelector().getCircleAlpha());
					uiBatch.draw(circle, k.getSelector().getCirclePosition().x, k.getSelector().getCirclePosition().y, 32,32);
					uiBatch.setColor(1,1,1,k.getSelector().getCrossAlpha());
					uiBatch.draw(cross, k.getSelector().getCrossPosition().x, k.getSelector().getCrossPosition().y, 32,32);
					uiBatch.setColor(1,1,1,1);
				}
				for (Vector2 v : world.getRightArrows()){
					uiBatch.draw(rightArrow, v.x, v.y);
				}
				for (Vector2 v : world.getDownArrows()){
					uiBatch.draw(downArrow, v.x, v.y);
				}
				uiBatch.draw(rightArrow, world.getAllRowsAndColumnsBox().x, world.getAllRowsAndColumnsBox().y);
				uiBatch.draw(downArrow, world.getAllRowsAndColumnsBox().x, world.getAllRowsAndColumnsBox().y);
				uiBatch.draw(diacriticsText, world.getHiraganaDiaToggle().x-world.getHiraganaDiaToggle().width, world.getHiraganaDiaToggle().y, world.getHiraganaDiaToggle().width, world.getHiraganaDiaToggle().height);
				if (world.hiraganaDiacriticsEnabled) uiBatch.draw(toggleOnText, world.getHiraganaDiaToggle().x, world.getHiraganaDiaToggle().y, world.getHiraganaDiaToggle().width, world.getHiraganaDiaToggle().height);
				else uiBatch.draw(toggleOffText, world.getHiraganaDiaToggle().x, world.getHiraganaDiaToggle().y, world.getHiraganaDiaToggle().width, world.getHiraganaDiaToggle().height);
				uiBatch.draw(combinationText, world.getHiraganaComboToggle().x-world.getHiraganaComboToggle().width, world.getHiraganaComboToggle().y, world.getHiraganaComboToggle().width, world.getHiraganaComboToggle().height);
				if (world.hiraganaCombinationsEnabled) uiBatch.draw(toggleOnText, world.getHiraganaComboToggle().x, world.getHiraganaComboToggle().y, world.getHiraganaComboToggle().width, world.getHiraganaComboToggle().height);
				else uiBatch.draw(toggleOffText, world.getHiraganaComboToggle().x, world.getHiraganaComboToggle().y, world.getHiraganaComboToggle().width, world.getHiraganaComboToggle().height);
				uiBatch.end();
				return;
			}
			else if (world.isKatakanaMenuShowing()){
				font.draw(uiBatch, "Press backspace to return", 64, 64);
				for(KanaMenuElement k : world.getStandardKatakanaElements()){
					uiBatch.draw( 
					standardKatakanaRegions[Arrays.asList(Constants.standardKana).indexOf(k.getKana().substring(2, k.getKana().length()))],
					k.getBasePosition().x, k.getBasePosition().y, 32, 32);
					uiBatch.setColor(1,1,1,k.getSelector().getCircleAlpha());
					uiBatch.draw(circle, k.getSelector().getCirclePosition().x, k.getSelector().getCirclePosition().y, 32,32);
					uiBatch.setColor(1,1,1,k.getSelector().getCrossAlpha());
					uiBatch.draw(cross, k.getSelector().getCrossPosition().x, k.getSelector().getCrossPosition().y, 32,32);
					uiBatch.setColor(1,1,1,1);
				}
				for (Vector2 v : world.getRightArrows()){
					uiBatch.draw(rightArrow, v.x, v.y);
				}
				for (Vector2 v : world.getDownArrows()){
					uiBatch.draw(downArrow, v.x, v.y);
				}
				uiBatch.draw(rightArrow, world.getAllRowsAndColumnsBox().x, world.getAllRowsAndColumnsBox().y);
				uiBatch.draw(downArrow, world.getAllRowsAndColumnsBox().x, world.getAllRowsAndColumnsBox().y);
				uiBatch.draw(diacriticsText, world.getKatakanaDiaToggle().x-world.getKatakanaDiaToggle().width, world.getKatakanaDiaToggle().y, world.getKatakanaDiaToggle().width, world.getKatakanaDiaToggle().height);
				if (world.katakanaDiacriticsEnabled) uiBatch.draw(toggleOnText, world.getKatakanaDiaToggle().x, world.getKatakanaDiaToggle().y, world.getKatakanaDiaToggle().width, world.getKatakanaDiaToggle().height);
				else uiBatch.draw(toggleOffText, world.getKatakanaDiaToggle().x, world.getKatakanaDiaToggle().y, world.getKatakanaDiaToggle().width, world.getKatakanaDiaToggle().height);
				uiBatch.draw(combinationText, world.getKatakanaComboToggle().x-world.getKatakanaComboToggle().width, world.getKatakanaComboToggle().y, world.getKatakanaComboToggle().width, world.getKatakanaComboToggle().height);
				if (world.katakanaCombinationsEnabled) uiBatch.draw(toggleOnText, world.getKatakanaComboToggle().x, world.getKatakanaComboToggle().y, world.getKatakanaComboToggle().width, world.getKatakanaComboToggle().height);
				else uiBatch.draw(toggleOffText, world.getKatakanaComboToggle().x, world.getKatakanaComboToggle().y, world.getKatakanaComboToggle().width, world.getKatakanaComboToggle().height);
				uiBatch.end();
				return;
			}
			
			count = 0;
			MenuText[] menuTextArray = world.getMenuTextObjects();
			float verticalOffset = tetrisUnit;
			//float verticalOffset = 0;//for testing
			for(Rectangle r : world.getMenuButtons()){
				//uiBatch.draw(menuButton, r.getX(), r.getY()-r.getHeight()/2, r.getWidth(), r.getWidth());
				//if (count ==0||count==1 || count==2 || count==3)font.draw(uiBatch, mbs[count], r.getX(), r.getY()+r.getHeight());
				if (count==0) uiBatch.draw(modeText, menuTextArray[count].getPosition().x, r.getY()-verticalOffset, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
				if (count==1) uiBatch.draw(hiraganaText, menuTextArray[count].getPosition().x, r.getY()-verticalOffset, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
				if (count==2) uiBatch.draw(katakanaText, menuTextArray[count].getPosition().x, r.getY()-verticalOffset, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
				if (count==3) uiBatch.draw(startText, menuTextArray[count].getPosition().x, r.getY()-verticalOffset, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
				count++;
			}
			//draw colored lines
			//uiBatch.draw(redLineTexture, world.getColoredLines().get(0).x, world.getColoredLines().get(0).y, world.getColoredLines().get(0).width, world.getColoredLines().get(0).height);
			uiBatch.draw(redLineTexture, world.getColoredLines().get(0).x, world.getColoredLines().get(0).y, world.getColoredLines().get(0).width, world.getColoredLines().get(0).height);
			uiBatch.draw(yellowLineTexture, world.getColoredLines().get(1).x, world.getColoredLines().get(1).y, world.getColoredLines().get(1).width, world.getColoredLines().get(1).height);
			uiBatch.draw(greenLineTexture, world.getColoredLines().get(2).x, world.getColoredLines().get(2).y, world.getColoredLines().get(2).width, world.getColoredLines().get(2).height);
			uiBatch.draw(blueLineTexture, world.getColoredLines().get(3).x, world.getColoredLines().get(3).y, world.getColoredLines().get(3).width, world.getColoredLines().get(3).height);
			
			for(Selector s : selectors){
//				uiBatch.setColor(1,1,1,s.getCircleAlpha());
//				uiBatch.draw(circle, s.getCirclePosition().x, s.getCirclePosition().y, standardWidth, standardWidth);
//				uiBatch.setColor(1,1,1,s.getCrossAlpha()); TODO make me pretty, commented out 6/12
//				uiBatch.draw(cross, s.getCrossPosition().x, s.getCrossPosition().y, standardWidth, standardWidth);
//				uiBatch.setColor(1,1,1,1);
			}
				
			//font.draw(uiBatch, "Mode:", );
//			uiBatch.draw(menu, world.getHiraganaDiaToggle().x, world.getHiraganaDiaToggle().y, world.getHiraganaDiaToggle().width, world.getHiraganaDiaToggle().height); TODO make me pretty 6/12
//			uiBatch.draw(menu, world.getHiraganaComboToggle().x, world.getHiraganaComboToggle().y, world.getHiraganaComboToggle().width, world.getHiraganaComboToggle().height);
			uiBatch.end();
			if(debug)drawMenuDebug();
			return;
		}
		blocks = world.getBlocks();
		blockIterator = blocks.iterator();
		fieldRectangles = world.getFieldRectangles();
		rectangleIterator = fieldRectangles.iterator();
		levelBatch.begin();
		levelBatch.setProjectionMatrix(camera.combined);
		int clearedBlocks = 0;
		while(blockIterator.hasNext()){
			b = blockIterator.next();
			if(b.shouldBeRemoved()) clearedBlocks++;
			if (b.getColor().equals("cyan")){
				levelBatch.draw(cyan, b.getPosition().x, 
						b.getSimplifiedPosition()*tetrisUnit, standardWidth, standardWidth);
			}
			else if (b.getColor().equals("green")){
				levelBatch.draw(green, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
			}
			else if (b.getColor().equals("indigo")){
				levelBatch.draw(indigo, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
			}
			else if (b.getColor().equals("purple")){
				levelBatch.draw(purple, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
			}
			else if (b.getColor().equals("red")){
				levelBatch.draw(red, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
			}
			else if (b.getColor().equals("yellow")){
				levelBatch.draw(yellow, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
			}
			else if (b.getColor().equals("orange")){
				levelBatch.draw(orange, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
			}
		}
		//font.draw(levelBatch, "Debug mode, press space to clear all blocks", Gdx.graphics.getWidth()*1.5f, Gdx.graphics.getHeight()*.2f);
		int tutorIndexToDraw = 0;
		boolean tutorHiragana = true;
		String currentTutorKana = world.getCurrentTutorKana();
		String currentKana = world.getCurrentKana();
		float averageX;
		float averageY;
		if(world.isKanaEntered()){
			blockIterator = world.getCurrentTetromino().iterator();
			while(blockIterator.hasNext()){
				b = blockIterator.next();
				if (b.getColor().equals("cyan")){
					levelBatch.draw(cyan, b.getPosition().x, b.getPosition().y, standardWidth, standardWidth);
				}
				else if (b.getColor().equals("green")){
					levelBatch.draw(green, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
				}
				else if (b.getColor().equals("indigo")){
					levelBatch.draw(indigo, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
				}
				else if (b.getColor().equals("purple")){
					levelBatch.draw(purple, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
				}
				else if (b.getColor().equals("red")){
					levelBatch.draw(red, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
				}
				else if (b.getColor().equals("yellow")){
					levelBatch.draw(yellow, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
				}
				else if (b.getColor().equals("orange")){
					levelBatch.draw(orange, b.getPosition().x, b.getPosition().y, standardWidth,standardWidth);
				}
				int indexToDraw = 0;
				currentKana = world.getCurrentKana();
				hiragana = true;
				if (!world.tutorMode && currentKana.charAt(0)=='h')
					for (int i =0; i < standardHiraganaRegions.length-1; i++){
						if (Constants.standardKana[i].equals(currentKana.substring(2, currentKana.length())))
							indexToDraw = i;
					}
				else if (!world.tutorMode && currentKana.charAt(0)=='k'){
					//System.out.println("currentKana.charAt(0) ["+currentKana.charAt(0)+"] == k");
					for (int i =0; i < standardKatakanaRegions.length-1; i++){
						if (Constants.standardKana[i].equals(currentKana.substring(2, currentKana.length())))
							indexToDraw = i;
						hiragana = false;
					}
				}
				if (world.tutorMode && currentTutorKana.charAt(0)=='h')
					for (int i =0; i < standardHiraganaRegions.length-1; i++){
						if (Constants.standardKana[i].equals(currentTutorKana.substring(2, currentTutorKana.length())))
							tutorIndexToDraw = i;
					}
				else if (world.tutorMode && currentTutorKana.charAt(0)=='k')
					for (int i =0; i < standardKatakanaRegions.length-1; i++){
						if (Constants.standardKana[i].equals(currentTutorKana.substring(2, currentTutorKana.length())))
							tutorIndexToDraw = i;
						tutorHiragana = false;
					}
				//System.out.println(hiragana);
				
				font.draw(levelBatch, "Current kana: ", world.getScoreBox().x*standardWidth+8, world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.75f+standardWidth);
				if (hiragana && currentKana!=null){ 
					levelBatch.draw(standardHiraganaRegions[indexToDraw], world.getScoreBox().x*standardWidth+8+(font.getBounds("CurrentKana").width)+18,
						world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.723125f+standardWidth, 50,50);
					
				}
				else if (currentKana!=null) 
					levelBatch.draw(standardKatakanaRegions[indexToDraw], world.getScoreBox().x*standardWidth+8+(font.getBounds("CurrentKana").width)+18,
							world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.723125f+standardWidth, 50,50);
				font.draw(levelBatch, "Congratulation!", 
						world.getScoreBox().x*standardWidth+8, world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.75f);
				font.draw(levelBatch, "Number of rows cleared: " + Integer.toString(world.getNumRowsCleared()), 
						world.getScoreBox().x*standardWidth+8, world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.75f-standardWidth);
				font.draw(levelBatch, "Number of correct answers: " + Integer.toString(world.getNumCorrectAnswers()),
						world.getScoreBox().x*standardWidth+8, world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.75f-standardWidth*2);
				font.draw(levelBatch, "Number of incorrect answers: " + Integer.toString(world.getNumIncorrectAnswers()),
						world.getScoreBox().x*standardWidth+8, world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.75f-standardWidth*3);
			}
		}
		//	if kana ISNT entered...
		
			else{ 
				//find average vector2 of current tetromino; falling kana will be drawn there
				averageX = 0;
				averageY = 0;
				for (Block b : world.getCurrentTetromino()){
					averageX+=b.getPosition().x;
					averageY+=b.getPosition().y;
				}
				averageX/=4;
				averageY/=4;
				boolean dakuten = false; //if this is true, will draw dakuten
				boolean combination = false; //if this is true, will draw combination
				hiragana = true; 
				//check if dakuten:
				if (currentKana!=null &&(currentKana.contains("\"")||currentKana.contains("\'"))){
					dakuten = true;
				}
				//check if combination:
				if (currentKana!=null&&currentKana.contains("-")){
					combination = true;
				}
				//check if katakana:
				else if (!world.tutorMode && currentKana!=null && currentKana.charAt(0)=='k'){
						hiragana = false;
				}
				//find kana index
				//find index of current hiragana
				int indexToDraw = 0;
				int subIndexToDraw = 0; //this is used for the small kana in a combo
				//System.out.println(combination);
				if (/*hiragana &&*/ !dakuten && !combination){
					for (int i =0; i < standardHiraganaRegions.length-1; i++){
						if (currentKana!=null && Constants.standardKana[i].equals(currentKana.substring(2, currentKana.length())))
							indexToDraw = i;
					}
				}
				//if dakuten, find index of current dakuten hiragana
				else if (/*hiragana &&*/ dakuten && !combination){
					for (int i =0; i < standardHiraganaRegions.length-1; i++){
						if (currentKana!=null && Constants.standardKana[i].equals(currentKana.substring(2, currentKana.length()-1)))
							indexToDraw = i;
					}
				}
				//if combination, find indices of current combination
				else if (/*hiragana && */!dakuten && combination){
					for (int i =0; i < standardHiraganaRegions.length-1; i++){
						//System.out.println("(1)Checking to see which kana matches " + currentKana.substring(0, currentKana.indexOf("-")));
						if (currentKana!=null && Constants.standardKana[i].equals(currentKana.substring(2, currentKana.indexOf("-"))))
							indexToDraw = i;
					}
					for (int i =0; i < standardHiraganaRegions.length-1; i++){
						//System.out.println("(1)[sub]Checking to see which kana matches " + currentKana.substring(currentKana.indexOf("-")+1, currentKana.length()));
						if (currentKana!=null && Constants.standardKana[i].equals(currentKana.substring(currentKana.indexOf("-")+1, currentKana.length())))
							subIndexToDraw = i;
					}
				}
				//if dakuten AND combination, find indices
				else if (/*hiragana &&*/ dakuten && combination){
					for (int i =0; i < standardHiraganaRegions.length-1; i++){
						//System.out.println("(2)Checking to see which kana matches " + currentKana.substring(2, currentKana.indexOf("-")-1));
						if (currentKana!=null && Constants.standardKana[i].equals(currentKana.substring(2, currentKana.indexOf("-")-1)))
							indexToDraw = i;
					}
					for (int i =0; i < standardHiraganaRegions.length-1; i++){
						//System.out.println("(2)[sub]Checking to see which kana matches " + currentKana.substring(currentKana.indexOf("-")+1, currentKana.length()));
						if (currentKana!=null && Constants.standardKana[i].equals(currentKana.substring(currentKana.indexOf("-")+1, currentKana.length())))
							subIndexToDraw = i;
					}
				}
				if (world.tutorMode && currentTutorKana!=null && currentTutorKana.charAt(0)=='h')
					for (int i =0; i < standardHiraganaRegions.length-1; i++){
						if (Constants.standardKana[i].equals(currentTutorKana.substring(2, currentTutorKana.length())))
							tutorIndexToDraw = i;
					}
				else if (world.tutorMode && currentTutorKana!=null && currentTutorKana.charAt(0)=='k')
					for (int i =0; i < standardKatakanaRegions.length-1; i++){
						if (Constants.standardKana[i].equals(currentTutorKana.substring(2, currentTutorKana.length())))
							tutorIndexToDraw = i;
						tutorHiragana = false;
					}
				if (currentKana!=null && currentKana.contains("k.")){hiragana = false;}
				//draw kana on stage (endless & classic mode)
					//hiragana
				if (!world.tutorMode && currentKana!=null && hiragana){
					levelBatch.draw(standardHiraganaRegions[indexToDraw], averageX, averageY, standardWidth, standardWidth);
					if (dakuten)
						font.draw(levelBatch, "\"", averageX+standardWidth, averageY+standardWidth);
					if (combination){ 
						levelBatch.draw(standardHiraganaRegions[subIndexToDraw], averageX+standardWidth, averageY+standardWidth, standardWidth/2, standardWidth/2);
					}
				}
					//katakana
				else if (!world.tutorMode && currentKana!=null) {
					levelBatch.draw(standardKatakanaRegions[indexToDraw], averageX, averageY, standardWidth, standardWidth);
					if (dakuten)
						font.draw(levelBatch, "\"", averageX+standardWidth, averageY+standardWidth);
					if (combination){
						levelBatch.draw(standardKatakanaRegions[subIndexToDraw], averageX+standardWidth, averageY+standardWidth, standardWidth/2, standardWidth/2);
					}
				}
				//draw kana on stage (tutor mode)
					//hiragana
				if ( world.tutorMode && currentTutorKana!=null && currentTutorKana.charAt(0)=='h'){
					levelBatch.draw(standardHiraganaRegions[indexToDraw], averageX, averageY, standardWidth, standardWidth);
					if (dakuten)
						font.draw(levelBatch, "\"", averageX+standardWidth, averageY+standardWidth);
					if (combination){
						levelBatch.draw(standardHiraganaRegions[indexToDraw], averageX+standardWidth, averageY+standardWidth, standardWidth/2, standardWidth/2);
					}
				}
					//katakana
				else if (world.tutorMode && currentTutorKana!=null) {
					levelBatch.draw(standardKatakanaRegions[indexToDraw], averageX, averageY, standardWidth, standardWidth);
					if (dakuten)
						font.draw(levelBatch, "\"", averageX+standardWidth, averageY+standardWidth);
					if (combination){
						levelBatch.draw(standardKatakanaRegions[indexToDraw], averageX+standardWidth, averageY+standardWidth, standardWidth/2, standardWidth/2);
					}
				}
				//draw entered and current kana text
				font.draw(levelBatch, "You have entered: " + world.getEnteredKana(), 
						world.getScoreBox().x*standardWidth+8, world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.75f);
				font.draw(levelBatch, "Current kana: ", world.getScoreBox().x*standardWidth+8, world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.75f+standardWidth);
				font.draw(levelBatch, world.getEnteredKana(), averageX+standardWidth*1.3f, averageY+standardWidth*0.66f);
				//draw current kana texture in score box
					//tutor mode, hiragana
				if (world.tutorMode && currentTutorKana!=null && currentTutorKana.charAt(0)=='h')
					levelBatch.draw(standardHiraganaRegions[indexToDraw], world.getScoreBox().x*standardWidth+8+(font.getBounds("CurrentKana").width)+18,
						world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.723125f+standardWidth, standardWidth,standardWidth);
					//tutor mode, katakana
				else if (world.tutorMode && standardKatakanaRegions[indexToDraw]!=null)//TODO fix this, it should never be null
					levelBatch.draw(standardKatakanaRegions[indexToDraw], world.getScoreBox().x*standardWidth+8+(font.getBounds("CurrentKana").width)+18,
							world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.723125f+standardWidth, standardWidth,standardWidth);
					//endless/classic, hiragana
				else if (!world.tutorMode && hiragana)
					levelBatch.draw(standardHiraganaRegions[indexToDraw], world.getScoreBox().x*standardWidth+8+(font.getBounds("CurrentKana").width)+18,
							world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.723125f+standardWidth, standardWidth,standardWidth);
					//TODO if dakuten, draw " thingy
					//endless/classic, katakana
				else if (!world.tutorMode && !hiragana){
					levelBatch.draw(standardKatakanaRegions[indexToDraw], world.getScoreBox().x*standardWidth+8+(font.getBounds("CurrentKana").width)+18,
							world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.723125f+standardWidth, standardWidth,standardWidth);
					//TODO if dakuten, draw " thingy
				}
				if (currentTutorKana!=null && currentKana.equals(currentTutorKana) && world.tutorModeKanaCount<3){
					font.draw(levelBatch, "("+currentTutorKana.substring(2)+")", world.getScoreBox().x*standardWidth+8+(font.getBounds("CurrentKana").width)+70,
							world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.723125f+110);
				}
				font.draw(levelBatch, "Number of rows cleared: " + Integer.toString(world.getNumRowsCleared()), 
						world.getScoreBox().x*standardWidth+8, world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.75f-standardWidth);
				font.draw(levelBatch, "Number of correct answers: " + Integer.toString(world.getNumCorrectAnswers()),
						world.getScoreBox().x*standardWidth+8, world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.75f-standardWidth*2);
				font.draw(levelBatch, "Number of incorrect answers: " + Integer.toString(world.getNumIncorrectAnswers()),
						world.getScoreBox().x*standardWidth+8, world.getScoreBox().y*standardWidth+world.getScoreBox().height*standardWidth*0.75f-standardWidth*3);
		}
			
		levelBatch.end();
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(colorR/100, colorG/100, colorB/100, 0);
		while(rectangleIterator.hasNext()){
			r = rectangleIterator.next();
			shapeRenderer.rect(r.x, r.y, r.width, r.height);
		}
		shapeRenderer.end();
		if (debug) drawDebug();
		updateRGB();
		
	}
	
	
	void drawDebug(){
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(255,0,0,0);
		collisionBoxes = world.getCollisionBoxes();
		rectangleIterator = collisionBoxes.iterator();
		while(rectangleIterator.hasNext()){
			r = rectangleIterator.next();
			shapeRenderer.rect(r.x, r.y, r.width, r.height);
		}
		shapeRenderer.end();
		levelBatch.begin();
		int count = 0;
		//draw row block counts
		for (int i : world.getRowBlockCount()){
			font.draw(levelBatch, ("Row " + count + ": " + i), 1024, 128*count);
			count++;
		}
		count = 0;
		//draw kana chart
		for(int column = 10; column >= 0; column--){
			for (int row= 0; row < 5; row++){
				if(column==3){
					if(row>2) row = 4;
					else if(row>0) row=2;
				}
				else if(column==1){
					if(row>0) row = 4;
				}
				levelBatch.draw(standardHiraganaRegions[count], 1000+column*80, 1000-row*80,80,80);
				count++;
				if (count==45){
					column = 0; row = 0;
					levelBatch.draw(standardHiraganaRegions[count], 1000+column*80, 1000-row*80,80,80);
					row = 5;
					column =-1;
				}
			}
		}
		levelBatch.end();
	}
	
	void drawMenuDebug(){
		uiShapeRenderer.begin(ShapeType.Line);
		uiShapeRenderer.setColor(Color.RED);
		rectangleIterator = world.getMenuButtons().iterator();
		while (rectangleIterator.hasNext()){
			r = rectangleIterator.next();
			uiShapeRenderer.rect(r.x, r.y, r.width, r.height);
		}
		uiShapeRenderer.rect(world.getMouse().getX(), world.getMouse().getY(), world.getMouse().getWidth(), world.getMouse().getHeight());
		uiShapeRenderer.end();
		
	}
	
	boolean redIsTarget, orangeIsTarget, yellowIsTarget, greenIsTarget, blueIsTarget, purpleIsTarget;
	boolean differenceSet;
	float rDiff = 0, gDiff = 0, bDiff = 0; //difference between target value and current value; used to scale color change
	void updateRGB(){
		
		if (colorR>255) colorR = 255;
		if (colorG>255) colorG = 255;
		if (colorB>255) colorB = 255;
		if (colorR<0) colorR = 0;
		if (colorG<0) colorG = 0;
		if (colorB<0) colorB = 0;
		if (colorR == 255 && colorG == 0 && colorB == 0 && redIsTarget){
			redIsTarget = false;
			orangeIsTarget = true;
			differenceSet = false;
		}
		if (colorR == 255 && colorG > 128 && colorB == 0 && orangeIsTarget){
			orangeIsTarget = false;
			yellowIsTarget = true;
			differenceSet = false;
		}
		if (colorR == 255 && colorG == 255 && colorB == 0 && yellowIsTarget){
			yellowIsTarget=false;
			greenIsTarget = true;
			differenceSet = false;
		}
		if (colorR==0 && colorG==255&&colorB==0 && greenIsTarget){
			greenIsTarget=false;
			blueIsTarget=true;
			differenceSet=false;
		}
		if (colorR==0 && colorG == 0 && colorB==255 && blueIsTarget){
			blueIsTarget=false;
			purpleIsTarget = true;
			differenceSet =false;
		}
		
		if (colorR>=102 && colorG==0 && colorB<=204 && purpleIsTarget){
			purpleIsTarget=false;
			redIsTarget=true;
			differenceSet=false;
		}
			
		if (redIsTarget && !differenceSet){
			rDiff = 255-colorR;
			gDiff = 0-colorG;
			bDiff = 0-colorB;
			differenceSet=true;
		}
		
		if (orangeIsTarget && !differenceSet){
			rDiff = 255-colorR;
			gDiff = 128-colorG;
			bDiff = 0-colorB;
			differenceSet = true;
		}
		
		if(yellowIsTarget && !differenceSet){
			rDiff = 255-colorR;
			gDiff = 255-colorG;
			bDiff = 0-colorB;
			differenceSet=true;
		}
		
		if (greenIsTarget&&!differenceSet){
			rDiff=0-colorR;
			gDiff = 255-colorG;
			bDiff = 0-colorB;
			differenceSet=true;
		}
		
		if (blueIsTarget && !differenceSet){
			rDiff=0-colorR;
			gDiff=0-colorG;
			bDiff=255-colorB;
			differenceSet = true;
		}
		
		if (purpleIsTarget && !differenceSet){
			rDiff=102-colorR;
			gDiff=0-colorG;
			bDiff=204-colorB;
			differenceSet = true;
		}
		
		if(rDiff!=0) colorR+= rDiff>0 ? Gdx.graphics.getDeltaTime()*50 : -Gdx.graphics.getDeltaTime()*50;
		if(gDiff!=0) colorG+= gDiff>0 ? Gdx.graphics.getDeltaTime()*50 : -Gdx.graphics.getDeltaTime()*50;
		if(bDiff!=0) colorB+= bDiff>0 ? Gdx.graphics.getDeltaTime()*50 : -Gdx.graphics.getDeltaTime()*50;
		
		
	}
	
	Texture cyan, green, indigo, orange, purple, red, yellow, field, standardHiragana, menu, menuButton,
				circle, cross, rightArrow, downArrow, hiraganaText, katakanaText, startText, modeText, 
				katakanaToggleText, hiraganaToggleText, toggleOnText, toggleOffText, diacriticsText, 
				combinationText, redLineTexture, greenLineTexture, blueLineTexture, yellowLineTexture;
	TextureRegion[] standardHiraganaRegions = new TextureRegion[46];
	Texture[] standardKatakana = new Texture[46];
	TextureRegion[] standardKatakanaRegions = new TextureRegion[46]; //debug, testing
	TextureRegion aKana;
	
	public void loadTextures(){
		cyan = new Texture("cyanblock.png");
		cyan.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		green = new Texture("greenblock.png");
		green.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		indigo = new Texture("indigoblock.png");
		indigo.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		purple = new Texture("purpleblock.png");
		purple.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		red = new Texture("redblock.png");
		red.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		yellow = new Texture("yellowblock.png");
		yellow.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		orange = new Texture("orangeblock.png");
		orange.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		menu = new Texture("kanatetrismenubackground.png");
		menu.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		menuButton = new Texture("kanatetrismenubutton.png");
		menuButton.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		circle = new Texture("circle.png");
		circle.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		cross = new Texture("cross.png");
		cross.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		rightArrow = new Texture("rightArrow.png");
		rightArrow.setFilter(TextureFilter.Nearest,  TextureFilter.Nearest);
		downArrow = new Texture("downArrow.png");
		downArrow.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		hiraganaText = new Texture("hiragana.png");
		hiraganaText.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		katakanaText = new Texture("katakana.png");
		katakanaText.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		startText = new Texture("start.png");
		startText.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		modeText = new Texture("mode.png");
		modeText.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		katakanaToggleText = new Texture("katakanaText.png");
		katakanaToggleText.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		hiraganaToggleText = new Texture("hiraganaText.png");
		hiraganaToggleText.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		toggleOnText = new Texture("onText.png");
		toggleOnText.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		toggleOffText = new Texture("offText.png");
		toggleOffText.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		diacriticsText = new Texture("diacriticstext.png");
		diacriticsText.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		combinationText = new Texture("combinationtext.png");
		combinationText.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		redLineTexture = new Texture("redline.png");
		redLineTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		blueLineTexture = new Texture("blueline.png");
		blueLineTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		greenLineTexture = new Texture("greenline.png");
		greenLineTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		yellowLineTexture = new Texture("yellowline.png");
		yellowLineTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		//standard hiragana textures
		standardHiragana = new Texture("standardhiragana.png");
		standardHiragana.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		//width 80, height 80, x = multiples of 75, y = (multiples of 75) + 30
		aKana = new TextureRegion(standardHiragana, 10*75, 0*75+30, 80,80);
		int count = 0;
		for(int column = 10; column >= 0; column--){
			for (int row= 0; row < 5; row++){
				if(column==3){
					if(row>2) row = 4;
					else if(row>0) row=2;
				}
				else if(column==1){
					if(row>0) row = 4;
				}
				standardHiraganaRegions[count] = new TextureRegion(standardHiragana, column*75, (row*75)+30, 80,80);
				count++;
				if(count==45){
					column=0; row = 0;
					standardHiraganaRegions[count] = new TextureRegion(standardHiragana, column*75, (row*75)+30, 80,80);
					row =5;
					column = -1; //if all kana have been added to texture region
				}
			}
		}
		//standard katakana textures
		for (int i = 0; i < Constants.standardKana.length; i++){
			if (!Constants.standardKana[i].equals("wo")){
				standardKatakana[i] = new Texture("katakanawhite/"+Constants.standardKana[i]+".png");
				standardKatakana[i].setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			}
		}
		
		for (int i = 0; i < 46; i++){
			if (i!=44)
			standardKatakanaRegions[i] = new TextureRegion(standardKatakana[i]);
		}
		
		//TODO field texture
	}
}
