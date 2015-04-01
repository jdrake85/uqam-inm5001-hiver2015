/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameGUI;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import static gameLogic.FakeMain3.gameState;
import mygame.Main;
import static mygame.Main.posX;

import gameLogic.FakeMain2;
import static gameLogic.FakeMain2.battle;
import static gameLogic.FakeMain2.gameState;
import static gameLogic.FakeMain2.creatureInCommand;
import java.util.ArrayList;

/**
 *
 * @author This PC
 */
public class GameState extends AbstractAppState implements ScreenController {

    private ArrayList<String> buttons = new ArrayList<String>();
    private boolean alwaysVisible = true;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }

    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }

    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }

    public void bind(Nifty nifty, Screen screen) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onStartScreen() {
        populateButtons();
    }

    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void hero1Skill1() {
        gameState = "skill";
        FakeMain3.commandType = 1;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 1);
    }

    public void hero1Skill2() {
        gameState = "skill";
        FakeMain3.commandType = 2;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 2);
    }

    public void hero1Skill3() {
        gameState = "skill";
        FakeMain3.commandType = 3;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 3);
    }

    public void hero1Skill4() {
        gameState = "skill";
        FakeMain3.commandType = 4;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 4);
    }

    public void hero1move() {
        gameState = "move";
        FakeMain3.battle.drawWithOverlayForCreatureMoves(FakeMain3.creatureInCommand);
        FakeMain3.animateMove();
    }

    public void hero1endTurn() {
        quitGame();
    }

    public void hero2Skill1() {
        gameState = "skill";
        FakeMain3.commandType = 5;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 5);
    }

    public void hero2Skill2() {
        gameState = "skill";
        FakeMain3.commandType = 6;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 6);
    }

    public void hero2Skill3() {
        gameState = "skill";
        FakeMain3.commandType = 7;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 7);
    }

    public void hero2Skill4() {
        gameState = "skill";
        FakeMain3.commandType = 8;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 8);
    }

    public void hero2move() {
        gameState = "move";
        FakeMain3.battle.drawWithOverlayForCreatureMoves(FakeMain3.creatureInCommand);
    }

    public void hero2endTurn() {
        quitGame();
    }

    public void hero3Skill1() {
        gameState = "skill";
        FakeMain3.commandType = 9;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 9);
    }

    public void hero3Skill2() {
        gameState = "skill";
        FakeMain3.commandType = 10;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 10);
    }

    public void hero3Skill3() {
        gameState = "skill";
        FakeMain3.commandType = 11;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 11);
    }

    public void hero3Skill4() {
        gameState = "skill";
        FakeMain3.commandType = 12;
        FakeMain3.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 12);
    }

    public void hero3move() {
        gameState = "move";
        FakeMain3.battle.drawWithOverlayForCreatureMoves(FakeMain3.creatureInCommand);
    }

    public void hero3endTurn() {
        quitGame();
    }

    public void startGame(String nextScreen) {
        FakeMain2.nifty.gotoScreen("battle");  // switch to another screen
        // start the game and do some more stuff...
    }

    public void populateButtons() {
        buttons.add("hero1Skill1");
        buttons.add("hero1Skill2");
        buttons.add("hero1Skill3");
        buttons.add("hero1Skill4");
        buttons.add("hero2Skill1");
        buttons.add("hero2Skill2");
        buttons.add("hero2Skill3");
        buttons.add("hero2Skill4");
        buttons.add("hero3Skill1");
        buttons.add("hero3Skill2");
        buttons.add("hero3Skill3");
        buttons.add("hero3Skill4");
        buttons.add("hero3Skill4");
        buttons.add("hero1move");
        buttons.add("hero1endTurn");
        buttons.add("hero2move");
        buttons.add("hero2endTurn");
        buttons.add("hero3move");
        buttons.add("hero3endTurn");
    }

    public void disableAll() {
        Element myElem;
        for (String s : buttons) {
            myElem = FakeMain2.nifty.getScreen("battle").findElementByName(s);
            myElem.disable();
        }
    }

    public void mainMenu() {
        FakeMain2.nifty.gotoScreen("mainMenu");
    }

    public void lastInfo() {
        Element myElem = FakeMain2.nifty.getScreen("battle").findElementByName("infoText");
        if (myElem.isVisible()){
            myElem.hide();
        } else {
            myElem.show();
        }
    }

    public void swapImages(String scr1, String img1, String scr2, String img2) {
        NiftyImage newImage = FakeMain2.nifty.getRenderEngine().createImage(Main.nifty.getScreen(scr1), img1, false);
        Element image = FakeMain2.nifty.getScreen(scr2).findElementByName(img2);
        image.getRenderer(ImageRenderer.class).setImage(newImage);
    }

    public void swapImages(String img1, String img2) {
        NiftyImage newImage = FakeMain2.nifty.getRenderEngine().createImage(Main.nifty.getCurrentScreen(), img1, false);
        Element image = FakeMain2.nifty.getCurrentScreen().findElementByName(img2);
        image.getRenderer(ImageRenderer.class).setImage(newImage);
    }

    public void showTurnStats(int turn) {
        Element myElem = FakeMain2.nifty.getScreen("battle").findElementByName("infoText");
        myElem.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
        myElem.getRenderer(TextRenderer.class).setText("Hello");
        myElem.show(); 
    }

    public void mouseOut() {
        Element myElem = FakeMain2.nifty.getScreen("battle").findElementByName("infoArea");
        if (!alwaysVisible) {
            myElem.hide();            
        }
    }
    
    public void showTurn1Stats() {
        showTurnStats(1);
    }

    public void showTurn2Stats() {

        showTurnStats(2);
    }

    public void showTurn3Stats() {

        showTurnStats(3);
    }

    public void showTurn4Stats() {

        showTurnStats(4);
    }

    public void showTurn5Stats() {

        showTurnStats(5);
    }

    public void newGame() {
        FakeMain2.nifty.gotoScreen("battle");
    }

    public void continueGame() {
        FakeMain2.nifty.gotoScreen("battle");
    }

    public void loadGame() {
        FakeMain2.nifty.gotoScreen("battle");
    }

    public void saveGame() {
        FakeMain2.nifty.gotoScreen("battle");
    }

    public void quitGame() {
        FakeMain3.app.stop();
    }

    public void showHeroStats(int i) {
        Element myElem = FakeMain2.nifty.getScreen("battle").findElementByName("infoText");
        myElem.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
        myElem.getRenderer(TextRenderer.class).setText("HeroStats");
        myElem.show(); 
    }

    public void showHero1Stats() {
        showHeroStats(1);
    }

    public void showHero2Stats() {
        showHeroStats(2);
    }

    public void showHero3Stats() {
        showHeroStats(3);
    }
}
