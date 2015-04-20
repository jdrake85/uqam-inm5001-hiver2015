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
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import gameLogic.Creature;

import gameLogic.Main;
import static gameLogic.Main.creatureInCommand;
import gameLogic.skills.Skill;
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
        if (Main.nifty.getCurrentScreen().getScreenId().equals("battle")) {
            updateCombatWindow(frequent);
        }
    }

    public void updateCombatWindow(boolean frequent) {
        if (!frequent) {
            
            resetHpAndEnergyPics();
            resetFacesPics();
            resetSkillsPics();
            resetMoveAndEndPics();
            disableAllButtons();
            disableAllButtons();
            smartEnableButtons();
            smartEnableImages();
            smartDisableButtons();
            smartDisableImages();
            updateTurnBanner();
            Element title = Main.nifty.getCurrentScreen().findElementByName("levelTitle");
            title.getRenderer(TextRenderer.class).setText(Main.app.levelName);
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
        Main.app.requestEndTurn();
        //disableAllButtons();
        //smartEnableButtons();
        //smartEnableImages();
    }

    public void heroSkillCall(Creature player, int index) {
        if (player.canPayEnergyCostForSkillNumber(index)){
            heroSkillInfo(player, index - 1);
            Main.app.requestSkill(player, index);
        } else {
            String infoString = "Not enough Energy to use skill!";
            Element myElem = Main.nifty.getScreen("battle").findElementByName("infoText");
            myElem.getRenderer(TextRenderer.class).setText(infoString);
        }
    }

    public void hero1Skill1() {        
        heroSkillCall(Main.hero, 1);
    }

    public void hero1Skill2() {
        heroSkillCall(Main.hero, 2);
    }

    public void hero1Skill3() {
        heroSkillCall(Main.hero, 3);
    }

    public void hero1Skill4() {
        heroSkillCall(Main.hero, 4);
    }

    public void hero1move() {
        Main.app.requestMovesOverlay();
    }

    public void hero1endTurn() {
        endTurn();
    }

    public void hero2Skill1() {
        heroSkillCall(Main.nurse, 1);
    }

    public void hero2Skill2() {
        heroSkillCall(Main.nurse, 2);
    }

    public void hero2Skill3() {
        heroSkillCall(Main.nurse, 3);
    }

    public void hero2Skill4() {
        heroSkillCall(Main.nurse, 4);
    }

    public void hero2move() {
        Main.app.requestMovesOverlay();
    }

    public void hero2endTurn() {
        endTurn();
    }

    public void hero3Skill1() {
        heroSkillCall(Main.soldier, 1);
    }

    public void hero3Skill2() {
        heroSkillCall(Main.soldier, 2);
    }

    public void hero3Skill3() {
        heroSkillCall(Main.soldier, 3);
    }

    public void hero3Skill4() {
        heroSkillCall(Main.soldier, 4);
    }

    public void hero3move() {
        Main.app.requestMovesOverlay();
    }

    public void hero3endTurn() {
        endTurn();
    }

    public void hero1Skill1Info() {
        heroSkillInfo(Main.hero, 0);
    }

    public void hero1Skill2Info() {
        heroSkillInfo(Main.hero, 1);
    }

    public void hero1Skill3Info() {
        heroSkillInfo(Main.hero, 2);
    }

    public void hero1Skill4Info() {
        heroSkillInfo(Main.hero, 3);
    }

    public void hero2Skill1Info() {
        heroSkillInfo(Main.nurse, 0);
    }

    public void hero2Skill2Info() {
        heroSkillInfo(Main.nurse, 1);
    }

    public void hero2Skill3Info() {
        heroSkillInfo(Main.nurse, 2);
    }

    public void hero2Skill4Info() {
        heroSkillInfo(Main.nurse, 3);
    }

    public void hero3Skill1Info() {
        heroSkillInfo(Main.soldier, 0);
    }

    public void hero3Skill2Info() {
        heroSkillInfo(Main.soldier, 1);
    }

    public void hero3Skill3Info() {
        heroSkillInfo(Main.soldier, 2);
    }

    public void hero3Skill4Info() {
        heroSkillInfo(Main.soldier, 3);
    }

    public void heroSkillInfo(Creature player, int index) {
        if (player != null) {
            Skill currentSkill = player.getSkills()[index];
            if (currentSkill != null) {
                String info = "Skill : " + currentSkill.getName();
                info += "\nDescription : " + currentSkill.getDescription();
                Element myElem = Main.nifty.getScreen("battle").findElementByName("infoText");
                myElem.getRenderer(TextRenderer.class).setText(info);
            }
        }
    }

    public void startGame(String nextScreen) {
        Main.nifty.gotoScreen("battle");

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
            myElem = Main.nifty.getScreen("battle").findElementByName(s);
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
        NiftyImage newImage = Main.nifty.getRenderEngine().createImage(Main.nifty.getCurrentScreen(), filePath, false);
        for (String s : holdersList) {
            Element image = Main.nifty.getCurrentScreen().findElementByName(s);
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
        Main.nifty.gotoScreen("mainMenu");
    }

    public void toggleInfo() {
        Element myElem = Main.nifty.getScreen("battle").findElementByName("infoText");
        if (myElem.isVisible()) {
            myElem.hide();
        } else {
            myElem.show();
        }
    }

    public void swapImages(String scr1, String img1, String scr2, String img2) {
        NiftyImage newImage = Main.nifty.getRenderEngine().createImage(Main.nifty.getScreen(scr1), img1, false);
        Element image = Main.nifty.getScreen(scr2).findElementByName(img2);
        image.getRenderer(ImageRenderer.class).setImage(newImage);
    }

    public void swapImages(String img1, String img2) {
        NiftyImage newImage = Main.nifty.getRenderEngine().createImage(Main.nifty.getCurrentScreen(), img1, false);
        Element image = Main.nifty.getCurrentScreen().findElementByName(img2);
        image.getRenderer(ImageRenderer.class).setImage(newImage);
    }

    public String creatureInfo(int turn) {
        Creature toStats = Main.battle.getCreatureTurnOrder()[turn];
        String infoString = toStats.statsOutput();
        return infoString;
    }

    public void showTurnStats(int turn) {
        Element myElem = Main.nifty.getScreen("battle").findElementByName("infoText");
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
        Main.gameState = "outOfLevel";
        Main.app.level = 1;
        Main.app.initializeBattleForLevel(Main.app.level);
        Main.nifty.gotoScreen("battle");
    }

    public void continueGame() {
        Main.nifty.gotoScreen("battle");
    }

    public void loadGame() {
        String savePath = "assets/Configurations/save.json";
        Main.gameState = "outOfLevel";
        try {
            Reader myReader = new FileReader(savePath);
            JSONTokener myJsonReader = new JSONTokener(myReader);
            Main.app.level = (new JSONObject(myJsonReader)).getInt("level");
        } catch (IOException ioe) {
            System.err.println("Reading file error : " + ioe.getMessage());
            Main.app.level = 1;
        } catch (JSONException jse) {
            System.err.println("JSON error : " + jse.getMessage());
            Main.app.level = 1;
        } finally {
            Main.app.initializeBattleForLevel(Main.app.level);

            Main.nifty.gotoScreen("battle");
        }
    }

    public void saveGame() {
        int level = Main.app.level;
        String savePath = "assets/Configurations/save.json";
        try {
            Writer myWriter = new FileWriter(savePath, false);
            JSONWriter myJsonWriter = new JSONWriter(myWriter);
            myJsonWriter.object();
            myJsonWriter.key("level");
            myJsonWriter.value(level);
            myJsonWriter.endObject();
            myWriter.close();
        } catch (IOException ioe) {
            System.err.println("Savin file error : " + ioe.getMessage());
        } catch (JSONException jse) {
            System.err.println("JSON error : " + jse.getMessage());
        } finally {
            Main.nifty.gotoScreen("battle");
        }
    }

    public void quitGame() {
        Main.app.stop();
    }

    public void showHeroStats(Creature toStats) {
        String infoString = toStats.statsOutput();
        Element myElem = Main.nifty.getScreen("battle").findElementByName("infoText");
        myElem.getRenderer(TextRenderer.class).setText(infoString);
    }

    public void showHero1Stats() {
        Creature toStats = Main.hero;
        if (toStats != null) {
            showHeroStats(toStats);
        }
    }

    public void showHero2Stats() {
        Creature toStats = Main.nurse;
        if (toStats != null) {
            showHeroStats(toStats);
        }
    }

    public void showHero3Stats() {
        Creature toStats = Main.soldier;
        if (toStats != null) {
            showHeroStats(toStats);
        }
    }

    private void smartEnableChar(Creature character, int index) {
        Element myElem;
        int scaled2X = 2 * index;
        int scaled4X = 4 * index;
        if (character != null) {
            myElem = Main.nifty.getScreen("battle").findElementByName(buttons.get(scaled2X + 12));
            myElem.enable();
            myElem = Main.nifty.getScreen("battle").findElementByName(buttons.get(scaled2X + 13));
            myElem.enable();
            //JASON: TODO check changement (OK1)
            for (int i = 0; i < 4; i++) {
                if (character.getSkills()[i] != null) {
                    myElem = Main.nifty.getScreen("battle").findElementByName(buttons.get(i + scaled4X));
                    myElem.enable();
                }
            }
        }
    }

    private void smartEnableImages() {
        smartEnablePlayerImages(Main.hero, 0);
        smartEnablePlayerImages(Main.nurse, 1);
        smartEnablePlayerImages(Main.soldier, 2);
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
            for (int i = 0; i < 4; i++) { // JASON: TODO check changement (OK1)
                if (player.getSkills()[i] != null) {
                    swapImages(skillsList.get(i + scaled4X), skillsHolders.get(i + scaled4X));
                }
            }
        }
    }

    private void smartEnableHero() {
        smartEnableChar(Main.hero, 0);
    }

    private void smartEnableNurse() {
        smartEnableChar(Main.nurse, 1);
    }

    private void smartEnableSoldier() {
        smartEnableChar(Main.soldier, 2);
    }

    private void smartDisableHero() {
        smartDisableChar(Main.hero, 0);
    }

    private void smartDisableNurse() {
        smartDisableChar(Main.nurse, 1);
    }

    private void smartDisableSoldier() {
        smartDisableChar(Main.soldier, 2);
    }

    private void smartDisableChar(Creature character, int index) {
        Element myElem;
        int scaled2X = 2 * index;
        int scaled4X = 4 * index;
        if (character != null && !character.equals(creatureInCommand)) {
            myElem = Main.nifty.getScreen("battle").findElementByName(buttons.get(scaled2X + 12));
            myElem.disable();
            myElem = Main.nifty.getScreen("battle").findElementByName(buttons.get(scaled2X + 13));
            myElem.disable();
            // JASON: CHANGEMENT ICI (TODO check)
            for (int i = scaled4X; i < scaled4X + 4; i++) {
                if (Main.allSkills[i] != null) {
                    myElem = Main.nifty.getScreen("battle").findElementByName(buttons.get(i));
                    myElem.disable();
                }
            }
        }
    }

    private void smartDisableImages() {
        smartDisablePlayerImages(Main.hero, 0);
        smartDisablePlayerImages(Main.nurse, 1);
        smartDisablePlayerImages(Main.soldier, 2);
    }

    private void smartDisablePlayerImages(Creature player, int index) {
        int scaled2X = 2 * index;
        int scaled4X = 4 * index;
        if (player != null && !player.equals(creatureInCommand)) {
            swapImages(greyedAsMoveAndEnd, moveAndEnd.get(scaled2X + 0));
            swapImages(greyedAsMoveAndEnd, moveAndEnd.get(scaled2X + 1));
            //JASON: TODO check changement
            for (int i = 0 + scaled4X; i < scaled4X + 4; i++) {
                if (Main.allSkills[i] != null) {
                    swapImages(lockedAsSkill, skillsHolders.get(i));
                }
            }
        }
    }

    private void updateTurnBanner() {
        resetTurnsPics();
        int i = 0;
        for (Creature c : Main.battle.getCreatureTurnOrder()) {
            swapImages(c.getPicturePath(), turnsHolders.get(i));
            i++;
        }
    }

    private void updateHealthAndEnergyBars() {
        if (Main.hero != null) {
            updatePlayerBars(Main.hero, 0);
        }
        if (Main.nurse != null) {
            updatePlayerBars(Main.nurse, 1);
        }
        if (Main.soldier != null) {
            updatePlayerBars(Main.soldier, 2);
        }
    }

    private void updatePlayerBars(Creature player, int index) {
        int scaled2X = 2 * index;
        int maxBoxHeight = Main.nifty.getCurrentScreen().findElementByName("hero1hp").getHeight();

        float hpRatio = computeHpRatio(player);
        float enRatio = computeEnRatio(player);

        int newHpHeight = (int) Math.ceil(hpRatio * maxBoxHeight);
        int newEnHeight = (int) Math.ceil(enRatio * maxBoxHeight);

        Element myElem = Main.nifty.getCurrentScreen().findElementByName(hpAndEnergy.get(scaled2X));
        Element myElem2 = Main.nifty.getCurrentScreen().findElementByName(hpAndEnergy.get(scaled2X + 1));

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
