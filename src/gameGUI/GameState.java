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

    public void left() {
          gameState = "move";
          FakeMain2.battle.drawWithOverlayForCreatureMoves(FakeMain2.hero);
        
    }
    
    public void hero1Skill1() {
        left();
    }

    public void hero1Skill2() {
        
        NiftyImage newImage = Main.nifty.getRenderEngine().createImage(Main.nifty.getScreen("battle"), "Interface/Images/face1.png", false);
        Element image = Main.nifty.getCurrentScreen().findElementByName("imageTest");
        image.getRenderer(ImageRenderer.class).setImage(newImage);
    }

    public void hero1Skill3() {
        Element myElem = Main.nifty.getCurrentScreen().findElementByName("text");
        myElem.getRenderer(TextRenderer.class).setText("Hello");
    }

    public void hero1Skill4() {
        Element myElem = Main.nifty.getCurrentScreen().findElementByName("hero1Skill1");
        myElem.disable();
    }

    public void hero1move() {
        Main.nifty.gotoScreen("mainMenu");
    }

    public void hero1endTurn() {
        quitGame();
    }

    public void hero2Skill1() {

    }

    public void hero2Skill2() {

    }

    public void hero2Skill3() {

    }

    public void hero2Skill4() {

    }

    public void hero2move() {

    }

    public void hero2endTurn() {

    }

    public void hero3Skill1() {

    }

    public void hero3Skill2() {

    }

    public void hero3Skill3() {

    }

    public void hero3Skill4() {

    }

    public void hero3move() {

    }

    public void hero3endTurn() {

    }
    
      public void startGame(String nextScreen) {
    Main.nifty.gotoScreen("battle");  // switch to another screen
    // start the game and do some more stuff...
  }
 
  public void quitGame() {
    Main.app.stop(); 
  }
}
