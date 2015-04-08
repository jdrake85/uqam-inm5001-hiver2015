/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameGUI;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import gameLogic.Creature;

import gameLogic.FakeMain2;
import static gameLogic.FakeMain2.creatureInCommand;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;

/**
 *
 * @author This PC
 */
public class GameState extends AbstractAppState implements ScreenController {

    private ArrayList<String> buttons = GUIData.fillButtons();
    private ArrayList<String> facesHolders = GUIData.fillFacesHolders();
    private ArrayList<String> turnsHolders = GUIData.fillTurnsHolders();
    private ArrayList<String> skillsHolders = GUIData.fillSkillsHolders();
    private ArrayList<String> moveAndEnd = GUIData.fillMoveAndEnd();
    private ArrayList<String> hpAndEnergy = GUIData.fillHpAndEnergy();
    private ArrayList<String> skillsList = GUIData.fillSkills();
    private ArrayList<String> picturePaths = GUIData.fillPicsPaths();
    private static String noneAsChar = GUIData.noneAsChar;
    private static String lockedAsSkill = GUIData.lockedAsSkill;
    private static String greyedAsMoveAndEnd = GUIData.greyedAsMoveAndEnd;
    private static String heroMovePng = GUIData.heroMovePng;
    private static String heroEndTurnPng = GUIData.heroEndTurnPng;
    private static String heroHpPng = GUIData.heroHpPng;
    private static String heroEnergyPng = GUIData.heroEnergyPng;
    private static String greyedAsHpAndEnergy = GUIData.greyedAsHpAndEnergy;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }

    public void update(boolean frequent) {
        if (FakeMain2.nifty.getCurrentScreen().getScreenId().equals("battle")) {
            updateCombatWindow(frequent);
        }
    }

    public void updateCombatWindow(boolean frequent) {
        if (!frequent) {
            disableAllButtons();
            smartEnableButtons();
            smartEnableImages();
            smartDisableButtons();
            smartDisableImages();
            updateTurnBanner();
            Element title = FakeMain2.nifty.getCurrentScreen().findElementByName("levelTitle");
            title.getRenderer(TextRenderer.class).setText("Level " + FakeMain2.app.level);
        }
        updateHealthAndEnergyBars();
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
    }

    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void endTurn() {
        FakeMain2.app.requestEndTurn();
        //disableAllButtons();
        //smartEnableButtons();
        //smartEnableImages();
    }

    public void hero1Skill1() {
        FakeMain2.app.requestSkill(1);
    }

    public void hero1Skill2() {
        FakeMain2.app.requestSkill(2);
    }

    public void hero1Skill3() {
        FakeMain2.app.requestSkill(3);
    }

    public void hero1Skill4() {
        FakeMain2.app.requestSkill(4);
    }

    public void hero1move() {
        FakeMain2.app.requestMovesOverlay();
    }

    public void hero1endTurn() {
        endTurn();
    }

    public void hero2Skill1() {
        FakeMain2.app.requestSkill(5);
    }

    public void hero2Skill2() {
        FakeMain2.app.requestSkill(6);
    }

    public void hero2Skill3() {
        FakeMain2.app.requestSkill(7);
    }

    public void hero2Skill4() {
        FakeMain2.app.requestSkill(8);
    }

    public void hero2move() {
        FakeMain2.app.requestMovesOverlay();
    }

    public void hero2endTurn() {
        endTurn();
    }

    public void hero3Skill1() {
        FakeMain2.app.requestSkill(9);
    }

    public void hero3Skill2() {
        FakeMain2.app.requestSkill(10);
    }

    public void hero3Skill3() {
        FakeMain2.app.requestSkill(11);
    }

    public void hero3Skill4() {
        FakeMain2.app.requestSkill(12);
    }

    public void hero3move() {
        FakeMain2.app.requestMovesOverlay();
    }

    public void hero3endTurn() {
        endTurn();
    }

    public void startGame(String nextScreen) {
        FakeMain2.nifty.gotoScreen("battle");

        resetHpAndEnergyPics();
        resetFacesPics();
        resetSkillsPics();
        resetMoveAndEndPics();

        smartEnableImages();
        disableAllButtons();
        smartEnableButtons();
    }

    public void disableAllButtons() {
        Element myElem;
        for (String s : buttons) {
            myElem = FakeMain2.nifty.getScreen("battle").findElementByName(s);
            myElem.disable();
        }
    }

    public void smartEnableButtons() {
        smartEnableHero();
        smartEnableNurse();
        smartEnableSoldier();
    }

    public void smartDisableButtons() {
        smartDisableHero();
        smartDisableNurse();
        smartDisableSoldier();
    }

    public void resetImages(ArrayList<String> holdersList, String filePath) {
        NiftyImage newImage = FakeMain2.nifty.getRenderEngine().createImage(FakeMain2.nifty.getCurrentScreen(), filePath, false);
        for (String s : holdersList) {
            Element image = FakeMain2.nifty.getCurrentScreen().findElementByName(s);
            image.getRenderer(ImageRenderer.class).setImage(newImage);
        }
    }

    public void resetTurnsPics() {
        resetImages(turnsHolders, noneAsChar);
    }

    public void resetFacesPics() {
        resetImages(facesHolders, noneAsChar);
    }

    public void resetSkillsPics() {
        resetImages(skillsHolders, lockedAsSkill);
    }

    public void resetMoveAndEndPics() {
        resetImages(moveAndEnd, greyedAsMoveAndEnd);
    }

    public void resetHpAndEnergyPics() {
        resetImages(hpAndEnergy, greyedAsHpAndEnergy);
    }

    public void mainMenu() {
        FakeMain2.nifty.gotoScreen("mainMenu");
    }

    public void toggleInfo() {
        Element myElem = FakeMain2.nifty.getScreen("battle").findElementByName("infoText");
        if (myElem.isVisible()) {
            myElem.hide();
        } else {
            myElem.show();
        }
    }

    public void swapImages(String scr1, String img1, String scr2, String img2) {
        NiftyImage newImage = FakeMain2.nifty.getRenderEngine().createImage(FakeMain2.nifty.getScreen(scr1), img1, false);
        Element image = FakeMain2.nifty.getScreen(scr2).findElementByName(img2);
        image.getRenderer(ImageRenderer.class).setImage(newImage);
    }

    public void swapImages(String img1, String img2) {
        NiftyImage newImage = FakeMain2.nifty.getRenderEngine().createImage(FakeMain2.nifty.getCurrentScreen(), img1, false);
        Element image = FakeMain2.nifty.getCurrentScreen().findElementByName(img2);
        image.getRenderer(ImageRenderer.class).setImage(newImage);
    }

    public String creatureInfo(int turn) {
        Creature toStats = FakeMain2.battle.getCreatureTurnOrder()[turn];
        String infoString = toStats.statsOutput();
        return infoString;
    }

    public void showTurnStats(int turn) {
        Element myElem = FakeMain2.nifty.getScreen("battle").findElementByName("infoText");
        myElem.getRenderer(TextRenderer.class).setText(creatureInfo(turn));
    }

    public void showTurn1Stats() {
        showTurnStats(0);
    }

    public void showTurn2Stats() {

        showTurnStats(1);
    }

    public void showTurn3Stats() {

        showTurnStats(2);
    }

    public void showTurn4Stats() {

        showTurnStats(3);
    }

    public void showTurn5Stats() {

        showTurnStats(4);
    }

    public void newGame() {
        FakeMain2.gameState = "outOfLevel";
        FakeMain2.app.level = 1;
        FakeMain2.app.initializeBattleForLevel(FakeMain2.app.level);
        FakeMain2.nifty.gotoScreen("battle");
    }

    public void continueGame() {
        FakeMain2.nifty.gotoScreen("battle");
    }

    public void loadGame() {
        String savePath = "assets/Configurations/save.json";
        FakeMain2.gameState = "outOfLevel";
        try{
            Reader myReader = new FileReader(savePath);
            JSONTokener myJsonReader = new JSONTokener(myReader);
            FakeMain2.app.level = (new JSONObject(myJsonReader)).getInt("level");
        }
        catch(IOException ioe){
            System.err.println("Reading file error : " + ioe.getMessage());
            FakeMain2.app.level = 1;
        }
        catch(JSONException jse){
            System.err.println("JSON error : " + jse.getMessage());
            FakeMain2.app.level = 1;
        }
        finally{
            FakeMain2.app.initializeBattleForLevel(FakeMain2.app.level);
            FakeMain2.nifty.gotoScreen("battle");            
        }
    }

    public void saveGame() {
        int level = FakeMain2.app.level;
        String savePath = "assets/Configurations/save.json";
        try{
            Writer myWriter = new FileWriter(savePath, false);
            JSONWriter myJsonWriter = new JSONWriter(myWriter);
            myJsonWriter.object();
                myJsonWriter.key("level");
                myJsonWriter.value(level);
            myJsonWriter.endObject();
            myWriter.close();
        }
        catch(IOException ioe){
            System.err.println("Savin file error : " + ioe.getMessage());
        }
        catch(JSONException jse){
            System.err.println("JSON error : " + jse.getMessage());
        }
        finally{
            FakeMain2.nifty.gotoScreen("battle");            
        }
    }

    public void quitGame() {
        FakeMain2.app.stop();
    }

    public void showHeroStats(Creature toStats) {
        String infoString = toStats.statsOutput();
        Element myElem = FakeMain2.nifty.getScreen("battle").findElementByName("infoText");
        myElem.getRenderer(TextRenderer.class).setText(infoString);
    }

    public void showHero1Stats() {
        Creature toStats = FakeMain2.hero;
        if (toStats != null){
            showHeroStats(toStats);
        }
    }

    public void showHero2Stats() {
        Creature toStats = FakeMain2.nurse;
        if (toStats != null){
            showHeroStats(toStats);
        }
    }

    public void showHero3Stats() {
        Creature toStats = FakeMain2.soldier;
        if (toStats != null){
            showHeroStats(toStats);
        }
    }

    private void smartEnableChar(Creature character, int index) {
        Element myElem;
        int scaled2X = 2 * index;
        int scaled4X = 4 * index;
        if (character != null) {
            myElem = FakeMain2.nifty.getScreen("battle").findElementByName(buttons.get(scaled2X + 12));
            myElem.enable();
            myElem = FakeMain2.nifty.getScreen("battle").findElementByName(buttons.get(scaled2X + 13));
            myElem.enable();
            for (int i = scaled4X; i < scaled4X + 4; i++) {
                if (FakeMain2.hero.getSkills()[i] != null) {
                    myElem = FakeMain2.nifty.getScreen("battle").findElementByName(buttons.get(i));
                    myElem.enable();
                }
            }
        }
    }

    private void smartEnableImages() {
        smartEnablePlayerImages(FakeMain2.hero, 0);
        smartEnablePlayerImages(FakeMain2.nurse, 1);
        smartEnablePlayerImages(FakeMain2.soldier, 2);
    }

    private void smartEnablePlayerImages(Creature player, int index) {
        int scaled2X = 2 * index;
        int scaled4X = 4 * index;
        if (player != null) {
            swapImages(picturePaths.get(index), facesHolders.get(index));
            swapImages(heroHpPng, hpAndEnergy.get(scaled2X + 0));
            swapImages(heroEnergyPng, hpAndEnergy.get(scaled2X + 1));
            swapImages(heroMovePng, moveAndEnd.get(scaled2X + 0));
            swapImages(heroEndTurnPng, moveAndEnd.get(scaled2X + 1));
            for (int i = 0 + scaled4X; i < scaled4X + 4; i++) {
                if (player.getSkills()[i] != null) {
                    swapImages(skillsList.get(i), skillsHolders.get(i));
                }
            }
        }
    }

    private void smartEnableHero() {
        smartEnableChar(FakeMain2.hero, 0);
    }

    private void smartEnableNurse() {
        smartEnableChar(FakeMain2.nurse, 1);
    }

    private void smartEnableSoldier() {
        smartEnableChar(FakeMain2.soldier, 2);
    }

    private void smartDisableHero() {
        smartDisableChar(FakeMain2.hero, 0);
    }

    private void smartDisableNurse() {
        smartDisableChar(FakeMain2.nurse, 1);
    }

    private void smartDisableSoldier() {
        smartDisableChar(FakeMain2.soldier, 2);
    }

    private void smartDisableChar(Creature character, int index) {
        Element myElem;
        int scaled2X = 2 * index;
        int scaled4X = 4 * index;
        if (character != null && !character.equals(creatureInCommand)) {
            myElem = FakeMain2.nifty.getScreen("battle").findElementByName(buttons.get(scaled2X + 12));
            myElem.disable();
            myElem = FakeMain2.nifty.getScreen("battle").findElementByName(buttons.get(scaled2X + 13));
            myElem.disable();
            for (int i = scaled4X; i < scaled4X + 4; i++) {
                if (FakeMain2.hero.getSkills()[i] != null) {
                    myElem = FakeMain2.nifty.getScreen("battle").findElementByName(buttons.get(i));
                    myElem.disable();
                }
            }
        }
    }

    private void smartDisableImages() {
        smartDisablePlayerImages(FakeMain2.hero, 0);
        smartDisablePlayerImages(FakeMain2.nurse, 1);
        smartDisablePlayerImages(FakeMain2.soldier, 2);
    }

    private void smartDisablePlayerImages(Creature player, int index) {
        int scaled2X = 2 * index;
        int scaled4X = 4 * index;
        if (player != null && !player.equals(creatureInCommand)) {
            swapImages(greyedAsMoveAndEnd, moveAndEnd.get(scaled2X + 0));
            swapImages(greyedAsMoveAndEnd, moveAndEnd.get(scaled2X + 1));
            for (int i = 0 + scaled4X; i < scaled4X + 4; i++) {
                if (player.getSkills()[i] != null) {
                    swapImages(lockedAsSkill, skillsHolders.get(i));
                }
            }
        }
    }

    private void updateTurnBanner() {
        resetTurnsPics();
        int i = 0;
        for (Creature c : FakeMain2.battle.getCreatureTurnOrder()) {
            swapImages(c.getPicturePath(), turnsHolders.get(i));
            i++;
        }
    }

    private void updateHealthAndEnergyBars() {
        if (FakeMain2.hero != null) {
            updatePlayerBars(FakeMain2.hero, 0);
        }
        if (FakeMain2.nurse != null) {
            updatePlayerBars(FakeMain2.nurse, 1);
        }
        if (FakeMain2.soldier != null) {
            updatePlayerBars(FakeMain2.soldier, 2);
        }
    }

    private void updatePlayerBars(Creature player, int index) {
        int scaled2X = 2 * index;
        int maxBoxHeight = FakeMain2.nifty.getCurrentScreen().findElementByName("hero1hp").getHeight(); 

        float hpRatio = computeHpRatio(player);
        float enRatio = computeEnRatio(player);
        
        int newHpHeight = (int)Math.ceil(hpRatio * maxBoxHeight);
        int newEnHeight = (int)Math.ceil(enRatio * maxBoxHeight);
        
        Element myElem = FakeMain2.nifty.getCurrentScreen().findElementByName(hpAndEnergy.get(scaled2X));
        Element myElem2 = FakeMain2.nifty.getCurrentScreen().findElementByName(hpAndEnergy.get(scaled2X + 1));
        
        myElem.setHeight(maxBoxHeight - newHpHeight);
        myElem2.setHeight(maxBoxHeight - newEnHeight);
        
    }

    private float computeHpRatio(Creature player) {
        return (float) player.getHealth() / player.getMaxHealth();
    }

    private float computeEnRatio(Creature player) {
        return (float) player.getEnergy() / player.getMaxEnergy();
    }
}
