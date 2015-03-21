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
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import static gameLogic.FakeMain2.gameState;
import mygame.Main;
import static mygame.Main.posX;

import gameLogic.FakeMain2;
import static gameLogic.FakeMain2.battle;
import static gameLogic.FakeMain2.gameState;
import static gameLogic.FakeMain2.creatureInCommand;

/**
 *
 * @author This PC
 */
public class GameState extends AbstractAppState implements ScreenController {

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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void hero1Skill1() {
        gameState = "skill";
        FakeMain2.commandType = 1;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 1);
    }

    public void hero1Skill2() {
        gameState = "skill";
        FakeMain2.commandType = 2;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 2);
    }

    public void hero1Skill3() {
        gameState = "skill";
        FakeMain2.commandType = 3;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 3);
    }

    public void hero1Skill4() {
        gameState = "skill";
        FakeMain2.commandType = 4;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 4);
    }

    public void hero1move() {
        gameState = "move";
        FakeMain2.battle.drawWithOverlayForCreatureMoves(FakeMain2.creatureInCommand);
        FakeMain2.animateMove();
    }

    public void hero1endTurn() {
        quitGame();
    }

    public void hero2Skill1() {
        gameState = "skill";
        FakeMain2.commandType = 5;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 5);
    }

    public void hero2Skill2() {
        gameState = "skill";
        FakeMain2.commandType = 6;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 6);
    }

    public void hero2Skill3() {
        gameState = "skill";
        FakeMain2.commandType = 7;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 7);
    }

    public void hero2Skill4() {
        gameState = "skill";
        FakeMain2.commandType = 8;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 8);
    }

    public void hero2move() {
        gameState = "move";
        FakeMain2.battle.drawWithOverlayForCreatureMoves(FakeMain2.creatureInCommand);
    }

    public void hero2endTurn() {
        quitGame();
    }

    public void hero3Skill1() {
        gameState = "skill";
        FakeMain2.commandType = 9;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 9);
    }

    public void hero3Skill2() {
        gameState = "skill";
        FakeMain2.commandType = 10;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 10);
    }

    public void hero3Skill3() {
        gameState = "skill";
        FakeMain2.commandType = 11;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 11);
    }

    public void hero3Skill4() {
        gameState = "skill";
        FakeMain2.commandType = 12;
        FakeMain2.battle.drawWithOverlayForCreatureSkill(creatureInCommand, 12);
    }

    public void hero3move() {
        gameState = "move";
        FakeMain2.battle.drawWithOverlayForCreatureMoves(FakeMain2.creatureInCommand);
    }

    public void hero3endTurn() {
        quitGame();
    }

    public void startGame(String nextScreen) {
        Main.nifty.gotoScreen("battle");  // switch to another screen
        // start the game and do some more stuff...
    }

    public void quitGame() {
        FakeMain2.app.stop();
    }
}
