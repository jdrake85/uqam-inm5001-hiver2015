package gameLogic;

import gameLogic.creatures.Zombie;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.cinematic.PlayState;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import de.lessvoid.nifty.Nifty;
import gameGUI.GameState;
import gameLogic.pathfinding.Coordinates;
import gameLogic.skills.Skill;
import gameLogic.skills.hero.*;
import gameLogic.skills.nurse.*;
import gameLogic.skills.soldier.*;
import java.util.LinkedList;

public class FakeMain2 extends SimpleApplication implements AnimEventListener {

    public static void main(String[] args) {
        app = new FakeMain2();
        app.setDisplayStatView(false);
        app.start();
    }
    protected Node pivot = new Node("pivot");
    protected Node mainNode = new Node("mainNode");
    public static Node charNode = new Node("charNode");
    public static String gameState = "outOfLevel"; // idle, move, skill, outOfLevel, enemyBusy, enemyIdle
    public static Material greenMat; //TODO remove
    public static Material redMat; //TODO remove
    public static Material greyMat; //TODO remove
    public static Material blueMat;
    public static Material redZombie;
    public static Material heroMat;
    public static Material nurseMat;
    public static Material soldierMat;
    public static Material numberMat[] = new Material[10];
    public static int commandType = -1;
    public static Geometry[][] g;
    public static GameBattle battle;
    public static Creature creatureInCommand;
    public static Creature hero = null;
    public static Creature nurse = null;
    public static Creature soldier = null;
    //public static Spatial ninja;
    public static Nifty nifty;
    //public static int posX = 0;
    public static FakeMain2 app;
    public static int turnCounter = 0;
    //protected int posZ = 0;
    //protected static Scene scene1;
    //New variables
    public static Node heroScene;
    public static Node nurseScene;
    public static Node soldierScene;
    public static AnimControl acHero;
    public static AnimControl acNurse;
    public static AnimControl acSoldier;
    public static AnimChannel channelHero;
    public static AnimChannel channelNurse;
    public static AnimChannel channelSoldier;
    public MotionEvent currentMotionEvent = null;
    public Creature currentMovingZombie = null;
    public static boolean playedPreBattleCinematic = false;
    public static boolean battleInProgress = false;
    public static boolean playedPostBattleCinematic = false;
    public int level = 1;
    public static boolean movingCreature = false;
    public static Transform mainTransform;
    public static Node lastDamageNode = null;
    private LinkedList<MotionEvent> activeDamageMotions = new LinkedList<MotionEvent>();
    private LinkedList<Node> activeDamageNodes = new LinkedList<Node>();
    public static final boolean FREQUENT = true;
    //private FilterPostProcessor fpp;
    //private FadeFilter fade;
    public static Skill[] allSkills = new Skill[12];
    public static String levelName;

    @Override
    public void simpleInitApp() {
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);
        initScene();
        initKeys();
        enableScreenshots(); // allows PRINTSCREEN to save screenshots

        // HERO GRAPHICS
        heroMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        heroMat.setColor("Color", new ColorRGBA(0f, 0f, 1f, 0f));
        assetManager.registerLocator("assets/Models/Hero/", FileLocator.class);

        // NURSE GRAPHICS
        nurseMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        nurseMat.setColor("Color", new ColorRGBA(1f, 0.5f, 0.1f, 0f));

        // SOLDIER GRAPHICS
        soldierMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        soldierMat.setColor("Color", new ColorRGBA(0f, 1f, 0f, 0f));
    }

    private void initKeys() {

        for (int i = 1; i <= 12; i++) {
            String skillName = "Skill" + i;
            inputManager.addMapping(skillName, new KeyTrigger(KeyInput.KEY_1 + i - 1));
            inputManager.addListener(actionListener, skillName);
        }

        inputManager.addMapping("BannerRefresh", new KeyTrigger(KeyInput.KEY_B));
        inputManager.addMapping("MoveKey", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("EnergyKey", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("RestoreHealthKey", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("SelectTile",
                new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        inputManager.addMapping("EndTurnKey", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("VictoryKey", new KeyTrigger(KeyInput.KEY_V));

        inputManager.addListener(actionListener, "BannerRefresh");
        inputManager.addListener(actionListener, "MoveKey");
        inputManager.addListener(actionListener, "EnergyKey");
        inputManager.addListener(actionListener, "RestoreHealthKey");
        inputManager.addListener(actionListener, "SelectTile");
        inputManager.addListener(actionListener, "EndTurnKey");
        inputManager.addListener(actionListener, "VictoryKey");
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("MoveKey") && !keyPressed) {
                requestMovesOverlay();
            }

            if (name.equals("EndTurnKey") && !keyPressed) {
                requestEndTurn();
            }

            if (name.substring(0, 5).equals("Skill") && !keyPressed) {
                int command;
                try {
                    command = Integer.parseInt(name.substring(5)) % 4;
                    if (command == 0) {
                        command = 4; // Simplest way to get numbers between 1 and 4
                    }
                } catch (Exception e) {
                    command = 1;
                }
                requestSkill(command);
            }
            
            // For debugging (TODO: remove)
            if (name.equals("VictoryKey") && !keyPressed && gameState.equals("idle") 
                    && noMotionEventPlaying() && !movingCreature) {
                level++;
                gameState = "outOfLevel";
            }

            // For debugging (TODO: remove)
            if (name.equals("EnergyKey") && !keyPressed && gameState.equals("idle")) {
                requestIncreaseEnergy();
            }

            // For debugging (TODO: remove)
            if (name.equals("RestoreHealthKey") && !keyPressed && gameState.equals("idle")) {
                requestRestoreHealth();
            }
            
            // For debugging (TODO: remove)
            if (name.equals("BannerRefresh") && !keyPressed && gameState.equals("idle")) {
                requestRefreshAndDisplayBanner();
            }

            if (name.equals("SelectTile") && !keyPressed && gameState.equals("skill")) {
                requestUseSkillOnSelectedTile();
            }

            if (name.equals("SelectTile") && !keyPressed && gameState.equals("move")) {
                requestMoveToSelectedTile();
            }
        }
    };

    public void requestMovesOverlay() {
        if ((gameState.equals("idle") || gameState.equals("skill")) 
                && noMotionEventPlaying() && !movingCreature) {
            battle.clearOverlay();
            gameState = "move";
            commandType = 0;
            battle.drawWithOverlayForCreatureMoves(creatureInCommand);
            creatureInCommand.animateMove();
        }
    }
    
    public void requestSkill(Creature creature, int command) { 
        if (creature.equals(creatureInCommand)) {
            requestSkill(command);
        }
    }
    
    private void requestSkill(int command) {
        if (!(gameState.equals("enemyAttack") || gameState.equals("outOfLevel")) 
                && noMotionEventPlaying() && !movingCreature && creatureInCommand.hasSkillNumber(command)) {
            creatureInCommand.animateIdle();
            battle.clearOverlay();
            commandType = command;
            gameState = "skill";
            battle.drawWithOverlayForCreatureSkill(creatureInCommand, commandType);
        }
    }

    public void requestEndTurn() {
        if (!(gameState.equals("enemyAttack") || gameState.equals("outOfLevel")) 
                && noMotionEventPlaying() && !movingCreature) {
            creatureInCommand.animateIdle();
            battle.clearOverlay();
            gameState = "idle";
            battle.endTurn();
            creatureInCommand = battle.getCreaturePlayingTurn();
            ((GameState) (nifty.getCurrentScreen().getScreenController())).update(!FREQUENT);
        }
    }

    // For debugging (TODO: remove)
    public void requestRefreshAndDisplayBanner() {
        if (noMotionEventPlaying() && !movingCreature) {
            Creature[] priorityBanner = battle.getCreatureTurnOrder();
            System.out.print("PRIORITY BANNER: ");
            for (Creature creature : priorityBanner) {
                System.out.print(creature + ", ");
            }
            System.out.println();
        }
    }

    // For debugging (TODO: remove)
    public void requestIncreaseEnergy() {
        if (noMotionEventPlaying() && !movingCreature) {
            creatureInCommand.setEnergy(creatureInCommand.getEnergy() + 20);
            System.out.println(creatureInCommand.getEnergy());
        }
    }

    // For debugging (TODO: remove)
    public void requestRestoreHealth() {
        if (noMotionEventPlaying() && !movingCreature) {
            creatureInCommand.receiveDamage(-16);
            System.out.println(creatureInCommand + " is now at " + creatureInCommand.getHealth() + " health!");
        }
    }

    public void requestUseSkillOnSelectedTile() {
        if (noMotionEventPlaying() && !movingCreature) {
            CollisionResults results = new CollisionResults();
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            Ray ray = new Ray(click3d, dir);
            pivot.collideWith(ray, results);

            // Use the results (we mark the hit object)
            if (results.size() > 0) {
                // The closest collision point is what was truly hit:
                Geometry closest = results.getClosestCollision().getGeometry();

                Vector3f targetTile;
                targetTile = closest.getLocalTranslation();
                int commandX = (int) targetTile.getX();
                int commandY = (int) targetTile.getZ();

                if (0 <= Math.min(commandX, commandY) && Math.max(commandX, commandY) <= 7 && creatureInCommand.hasSkillNumber(commandType)) {          
                    System.out.println("Using skill " + creatureInCommand.prepareSkill(commandType) + " at (" + commandX + ", " + commandY + ")...");
                    battle.useCreatureSkillAt(creatureInCommand, commandType, new Coordinates(commandX, commandY), activeDamageNodes, activeDamageMotions);

                } else {
                    System.out.println("Error: invalid command or coordinates");
                }

                if (battle.isWon()) {
                    System.out.println("<PLACEHOLDER FUNCTION>: battle is won");
                    gameState = "loadPostBattleCinematic";
                    battleInProgress = false;
                } else {
                    battle.refreshCreatureList();
                    ((GameState) (nifty.getCurrentScreen().getScreenController())).update(!FREQUENT);
                }

            }
            gameState = "idle";
            battle.clearOverlay();
            // creatureInCommand.animateIdle(); // TODO: specify creature
        }
    }

    public void requestMoveToSelectedTile() {
        if (noMotionEventPlaying() && !movingCreature) {
            CollisionResults results = new CollisionResults();
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            Ray ray = new Ray(click3d, dir);
            pivot.collideWith(ray, results);

            // Use the results (we mark the hit object)
            if (results.size() > 0) {
                // The closest collision point is what was truly hit:
                Geometry closest = results.getClosestCollision().getGeometry();

                Vector3f targetTile;
                targetTile = closest.getLocalTranslation();
                int commandX = (int) targetTile.getX();
                int commandY = (int) targetTile.getZ();

                if (0 <= Math.min(commandX, commandY) && Math.max(commandX, commandY) <= 7) {
                    System.out.println("Moving to (" + commandX + ", " + commandY + ")...");
                    MotionEvent nextMotionEvent = battle.moveCreatureTo(creatureInCommand, new Coordinates(commandX, commandY));
                    setAndPlayNextMotionEvent(nextMotionEvent);
                } else {
                    System.out.println("Error: coordinates " + new Coordinates(commandX, commandY) + " are outside the gameboard!");
                }

                gameState = "idle";
                battle.clearOverlay();
            }
        }
    }

    private Zombie performMovementForZombieTurn() {
        Zombie zombie = (Zombie) creatureInCommand;
        Coordinates attackPosition = battle.getCoordinatesForBestClosestTarget(zombie);
        MotionEvent nextMotionEvent;
        if (attackPosition != null) {
            creatureInCommand.animateMove();
            nextMotionEvent = battle.moveCreatureTo(zombie, attackPosition);
            setAndPlayNextMotionEvent(nextMotionEvent);
            battle.clearOverlay();
        }
        return zombie;
    }

    private boolean performAttackOnPlayerForZombieTurn() {
        //Debugging
        Zombie zombie = (Zombie) creatureInCommand;
        Creature zombieTarget = zombie.getCurrentTarget();
        System.out.println(zombie + " is currently pursuing " + zombieTarget + "...");
        boolean gameOver = false;

        if (zombieTarget != null) {
            battle.haveZombieAttackAdjacentTarget(zombie, activeDamageNodes, activeDamageMotions);
            gameOver = !zombieTarget.isAlive();
            if (gameOver) {
                System.out.println("GAMEOVER - " + zombieTarget + " defeated!");
            }
        }

        return gameOver;
    }

    public void initScene() {

        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);

        /**
         * Create a new NiftyGUI object
         */
        nifty = niftyDisplay.getNifty();
        /**
         * Read your XML and initialize your custom ScreenController
         */
        nifty.fromXml("./Interface/screen.xml", "battle");
        // nifty.fromXml("Interface/helloworld.xml", "start", new MySettingsScreen(data));
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
        // disable the fly cam
        flyCam.setDragToRotate(true);
        //nifty.fromXml("Interface/screen.xml", "start", new GUIOverlay());

        greenMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        greenMat.setColor("Color", new ColorRGBA(.1f, .75f, .1f, 0.5f));//R,B,G,Alphas
        greenMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        greyMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        greyMat.setColor("Color", new ColorRGBA(.1f, .1f, .1f, .1f));//R,B,G,Alphas
        greyMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        redMat.setColor("Color", new ColorRGBA(0.75f, 0f, 0f, 0.5f));//R,B,G,Alphas
        redMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        blueMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blueMat.setColor("Color", new ColorRGBA(.1f, .1f, .75f, 0.5f));//R,B,G,Alphas
        blueMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        redZombie = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        redZombie.setColor("Color", new ColorRGBA(0.75f, 0f, 0f, 0f));//R,B,G,Alphas

        for (int i = 0; i < 10; i++) {
            numberMat[i] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            Texture numberTex = assetManager.loadTexture("DamageNumbers/" + i + ".png");
            numberMat[i].setTexture("ColorMap", numberTex);
        }

        /**/
        Box plancher = new Box(4, 0, 4);
        Geometry gp = new Geometry("Box", plancher);

        gp.setMaterial(floorMat());
        gp.setLocalTranslation(new Vector3f(3.5f, -2.01f, 3.5f));
        mainNode.attachChild(gp);
        /**/
        Box b1 = new Box(0.45f, 0, 0.45f);
        g = new Geometry[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g[i][j] = new Geometry("box", b1);
                g[i][j].setLocalTranslation(new Vector3f(i, -2, j));
                g[i][j].setMaterial(greyMat);
                g[i][j].setQueueBucket(Bucket.Translucent);
                pivot.attachChild(g[i][j]);
            }
        }

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);

        mainTransform = new Transform();
        mainTransform.setTranslation(new Vector3f(-4, 4, -4));
        mainTransform.setRotation(new Quaternion().fromAngles(0.7f, 0f, 0f));
        //mainNode.setLocalTranslation(new Vector3f(-4, 4, -4));
        //mainNode.rotate(0.7f, 0f, 0f);
        mainNode.setLocalTransform(mainTransform);

        mainNode.attachChild(pivot);
        mainNode.attachChild(charNode);
        rootNode.attachChild(mainNode); // put this node in the scene
    }

    public Material floorMat() { //TODO placeHolder
        Material flMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //flMat.setColor("Color", new ColorRGBA(0.25f, 0, 0.75f, 11f));//R,B,G,Alphas
        Texture flTex = assetManager.loadTexture("Models/greenTileFloor2.jpg");
        flMat.setTexture("ColorMap", flTex);
        return flMat;
    }

    public static AnimControl findAnimControl(final Spatial parent) {
        final AnimControl animControl = parent.getControl(AnimControl.class);
        if (animControl != null) {
            return animControl;
        }

        if (parent instanceof Node) {
            for (final Spatial s : ((Node) parent).getChildren()) {
                final AnimControl animControl2 = findAnimControl(s);
                if (animControl2 != null) {
                    return animControl2;
                }
            }
        }
        return null;
    }

    public void setAndPlayNextMotionEvent(MotionEvent nextMotionEvent) {
        currentMotionEvent = nextMotionEvent;
        if (creatureInCommand instanceof Zombie) {
            currentMovingZombie = creatureInCommand;
        } else {
            currentMovingZombie = null;
        }
        if (currentMotionEvent != null) {
            System.out.println("PLAYING MOTION EVENT: " + currentMotionEvent + '\n');
            currentMotionEvent.play();
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        //displayGameInfo();
        if (!gameState.equals("outOfLevel")) {
            if (battleInProgress) {
                ((GameState) (nifty.getCurrentScreen().getScreenController())).update(FREQUENT);
                if (!battle.isWon()) {
                    clearFinishedDamageNodes();
                    if (noMotionEventPlaying() && !movingCreature) {
                        if (gameState.equals("idle") && !creatureInCommand.creatureChannel.getAnimationName().equals("Idle")) {
                            creatureInCommand.animateIdle();
                        } else if (currentMovingZombie != null) {
                            currentMovingZombie.animateIdle();
                            currentMovingZombie = null;
                        }
                        if (battle.isZombieTurn()) {
                            if (gameState.equals("idle")) {
                                performMovementForZombieTurn();
                                gameState = "enemyAttack";
                            } else if (gameState.equals("enemyAttack")) {
                                Zombie zombie = (Zombie) creatureInCommand;
                                if (zombie.hasEnoughEnergyToAttack() && battle.zombieIsAdjacentToTarget(zombie)) {
                                    if (performAttackOnPlayerForZombieTurn()) {
                                        System.out.println("<PLACEHOLDER FUNCTION>: battle is lost, return to main menu");
                                        gameState = "outOfLevel";
                                    }
                                } else {
                                    System.out.println("Zombie turn finished");
                                    gameState = "idle";
                                    battle.endTurn();
                                    creatureInCommand = battle.getCreaturePlayingTurn();
                                }
                                ((GameState) (nifty.getCurrentScreen().getScreenController())).update(!FREQUENT);
                            }
                        } else if (!(gameState.equals("move") || gameState.equals("skill"))) {
                            gameState = "idle";
                        }
                    }
                }
            } else if (!playedPreBattleCinematic) {
                System.out.println("<PLACEHOLDER FUNCTION / SIMPLE UPDATE>: play PRE battle cinematic now");
                playedPreBattleCinematic = true;
                battle.start();
                creatureInCommand = battle.getCreaturePlayingTurn();
                ((GameState) (nifty.getCurrentScreen().getScreenController())).update(!FREQUENT);
                battleInProgress = true;
            } else if (!playedPostBattleCinematic) {
                battleInProgress = false;
                System.out.println("<PLACEHOLDER FUNCTION / SIMPLE UPDATE>: play POST battle cinematic now");
                playedPostBattleCinematic = true;
                gameState = "outOfLevel";
                level++;
            }
        } else {
            if (level < 9) {
                System.out.println();
                System.out.println("---------------------------------");
                System.out.println("## LOADING LEVEL " + level + " ##");
                System.out.println("---------------------------------");
                System.out.println();
                initializeBattleForLevel(level);
                gameState = "idle";
            } else if (level == 9) {
                System.out.println("Congratulations!");
                app.stop();
            }
        }
    }

    private boolean noMotionEventPlaying() {
        return currentMotionEvent == null || currentMotionEvent.getPlayState().equals(PlayState.Stopped);
    }

    private void clearFinishedDamageNodes() {
        assert (activeDamageNodes.isEmpty() == activeDamageMotions.isEmpty());
        if (!activeDamageMotions.isEmpty() && activeDamageMotions.peek().getPlayState().equals(PlayState.Stopped)) {
            Node finishedNode = activeDamageNodes.removeFirst();
            finishedNode.removeFromParent();
            activeDamageMotions.removeFirst();
        }
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("Skill1") || animName.equals("Skill2") || animName.equals("Skill13")) {
            movingCreature = false;
        }
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        // Do nothing
    }

    public void initializeBattleForLevel(int level) {
        if (battle != null) {
            playedPreBattleCinematic = battleInProgress = playedPostBattleCinematic = false;
            currentMovingZombie = null;
            movingCreature = false;
            battle.clearCombattants();
            playedPreBattleCinematic = battleInProgress = playedPostBattleCinematic = false;
            currentMovingZombie = null;
            movingCreature = false;
        }
        battle = new GameBattle();
        switch (level) {
            case 1:
                initializeLevel1();
                break;
            case 2:
                initializeLevel2();
                break;
            case 3:
                initializeLevel3();
                break;
            case 4:
                initializeLevel4();
                break;
            case 5:
                initializeLevel5();
                break;
            case 6:
                initializeLevel6();
                break;
            case 7:
                initializeLevel7();
                break;
            case 8:
                initializeLevel8();
                break;
            default:
                System.out.println("Error loading level; loading first level.");
                initializeLevel1();
                break;
        }
    }

    private void initializeLevel1() {
        levelName = "Level 1: Fight for Freedom";

        soldier = null;
        nurse = null;
        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        battle.insertCreatureAt(hero, 1, 4);
        hero.faceEast();

        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        battle.insertCreatureAt(zombie1, 6, 4);
        zombie1.faceWest();
    }

    private void initializeLevel2() {
        levelName = "Level 2: Damsel in Distress";

        soldier = null;

        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        battle.insertCreatureAt(hero, 1, 4);
        hero.faceEast();

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 1);
        nurse.setSkillAsNumber(new Push(8, 4), 4);
        battle.insertCreatureAt(nurse, 7, 0);
        nurse.faceSouth();

        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        battle.insertCreatureAt(zombie1, 6, 1);
        zombie1.faceEast();
    }

    private void initializeLevel3() {
        levelName = "Level 3: Getting Through";

        soldier = null;

        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        battle.insertCreatureAt(hero, 3, 6);
        hero.faceNorth();

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 1);
        nurse.setSkillAsNumber(new Push(8, 4), 4);
        battle.insertCreatureAt(nurse, 3, 4);
        nurse.faceNorth();

        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        Creature zombie2 = new Zombie("Zombie2", assetManager, this);
        Creature zombie3 = new Zombie("Zombie3", assetManager, this);
        battle.insertCreatureAt(zombie1, 1, 2);
        battle.insertCreatureAt(zombie2, 5, 2);
        battle.insertCreatureAt(zombie3, 3, 1);
        zombie1.faceSouth();
        zombie2.faceSouth();
        zombie3.faceSouth();
    }

    private void initializeLevel4() {
        levelName = "Level 4: Pincer Attack";

        soldier = null;

        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        battle.insertCreatureAt(hero, 3, 3);
        hero.faceNorth();

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 1);
        nurse.setSkillAsNumber(new Innoculation(6, 4), 2);
        nurse.setSkillAsNumber(new Push(8, 4), 4);
        battle.insertCreatureAt(nurse, 3, 4);
        nurse.faceSouth();

        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        Creature zombie2 = new Zombie("Zombie2", assetManager, this);
        Creature zombie3 = new Zombie("Zombie3", assetManager, this);
        Creature zombie4 = new Zombie("Zombie4", assetManager, this);
        battle.insertCreatureAt(zombie1, 1, 1);
        battle.insertCreatureAt(zombie2, 1, 6);
        zombie1.faceEast();
        zombie2.faceEast();
        battle.insertCreatureAt(zombie3, 6, 6);
        battle.insertCreatureAt(zombie4, 6, 1);
        zombie3.faceWest();
        zombie4.faceWest();
    }

    private void initializeLevel5() {
        levelName = "Level 5: Making Friends";

        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        battle.insertCreatureAt(hero, 1, 6);
        hero.faceEast();

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 1);
        nurse.setSkillAsNumber(new Innoculation(6, 4), 2);
        nurse.setSkillAsNumber(new Push(8, 4), 4);
        battle.insertCreatureAt(nurse, 2, 7);
        nurse.faceNorth();

        soldier = new Creature("Soldier", FakeMain2.soldierMat, assetManager, this);
        soldier.setPicturePath("Interface/Images/Soldier.png");
        soldier.setSkillAsNumber(new AimedShot(9, 4), 1);
        soldier.setSkillAsNumber(new Stab(11, 4), 3);
        battle.insertCreatureAt(soldier, 7, 0);
        soldier.faceWest();

        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        Creature zombie2 = new Zombie("Zombie2", assetManager, this);
        Creature zombie3 = new Zombie("Zombie3", assetManager, this);
        Creature zombie4 = new Zombie("Zombie4", assetManager, this);
        battle.insertCreatureAt(zombie1, 2, 4);
        battle.insertCreatureAt(zombie2, 2, 5);
        zombie1.faceSouth();
        zombie2.faceSouth();
        battle.insertCreatureAt(zombie3, 6, 0);
        zombie3.faceEast();
        battle.insertCreatureAt(zombie4, 7, 1);
        zombie4.faceNorth();
    }

    private void initializeLevel6() {
        levelName = "Level 6: Showdown";

        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        hero.setSkillAsNumber(new SpinningPipe(3, 4), 3);
        battle.insertCreatureAt(hero, 5, 4);
        hero.faceWest();

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 1);
        nurse.setSkillAsNumber(new Innoculation(6, 4), 2);
        nurse.setSkillAsNumber(new Push(8, 4), 4);
        battle.insertCreatureAt(nurse, 6, 3);
        nurse.faceWest();

        soldier = new Creature("Soldier", FakeMain2.soldierMat, assetManager, this);
        soldier.setPicturePath("Interface/Images/Soldier.png");
        soldier.setSkillAsNumber(new AimedShot(9, 4), 1);
        soldier.setSkillAsNumber(new Stab(11, 4), 3);
        battle.insertCreatureAt(soldier, 6, 5);
        soldier.faceWest();

        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        Creature zombie2 = new Zombie("Zombie2", assetManager, this);
        Creature zombie3 = new Zombie("Zombie3", assetManager, this);
        Creature zombie4 = new Zombie("Zombie4", assetManager, this);
        Creature zombie5 = new Zombie("Zombie5", assetManager, this);
        Creature zombie6 = new Zombie("Zombie6", assetManager, this);
        battle.insertCreatureAt(zombie1, 1, 1);
        battle.insertCreatureAt(zombie2, 1, 2);
        battle.insertCreatureAt(zombie3, 1, 3);
        battle.insertCreatureAt(zombie4, 1, 4);
        battle.insertCreatureAt(zombie5, 1, 5);
        battle.insertCreatureAt(zombie6, 1, 6);
        zombie1.faceEast();
        zombie2.faceEast();
        zombie3.faceEast();
        zombie4.faceEast();
        zombie5.faceEast();
        zombie6.faceEast();
    }

    private void initializeLevel7() {
        levelName = "Level 7: Surrounded";

        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        hero.setSkillAsNumber(new SpinningPipe(3, 4), 3);
        battle.insertCreatureAt(hero, 5, 1);
        hero.faceWest();

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 1);
        nurse.setSkillAsNumber(new Innoculation(6, 4), 2);
        nurse.setSkillAsNumber(new MustardGas(7, 4), 3);
        nurse.setSkillAsNumber(new Push(8, 4), 4);
        battle.insertCreatureAt(nurse, 6, 0);
        nurse.faceSouth();

        soldier = new Creature("Soldier", FakeMain2.soldierMat, assetManager, this);
        soldier.setPicturePath("Interface/Images/Soldier.png");
        soldier.setSkillAsNumber(new AimedShot(9, 4), 1);
        soldier.setSkillAsNumber(new ShootEmAll(10, 4), 2);
        soldier.setSkillAsNumber(new Stab(11, 4), 3);
        battle.insertCreatureAt(soldier, 6, 1);
        soldier.faceSouth();

        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        Creature zombie2 = new Zombie("Zombie2", assetManager, this);
        Creature zombie3 = new Zombie("Zombie3", assetManager, this);
        Creature zombie4 = new Zombie("Zombie4", assetManager, this);
        Creature zombie5 = new Zombie("Zombie5", assetManager, this);
        Creature zombie6 = new Zombie("Zombie6", assetManager, this);
        Creature zombie7 = new Zombie("Zombie7", assetManager, this);
        Creature zombie8 = new Zombie("Zombie8", assetManager, this);
        Creature zombie9 = new Zombie("Zombie9", assetManager, this);
        Creature zombie10 = new Zombie("Zombie10", assetManager, this);
        Creature zombie11 = new Zombie("Zombie11", assetManager, this);
        Creature zombie12 = new Zombie("Zombie12", assetManager, this);
        battle.insertCreatureAt(zombie1, 3, 0);
        battle.insertCreatureAt(zombie2, 3, 2);
        zombie1.faceEast();
        zombie2.faceEast();
        battle.insertCreatureAt(zombie3, 3, 4);
        battle.insertCreatureAt(zombie4, 3, 5);
        battle.insertCreatureAt(zombie5, 3, 6);
        battle.insertCreatureAt(zombie6, 3, 7);
        battle.insertCreatureAt(zombie7, 4, 4);
        battle.insertCreatureAt(zombie8, 4, 5);
        battle.insertCreatureAt(zombie9, 4, 6);
        battle.insertCreatureAt(zombie10, 5, 4);
        battle.insertCreatureAt(zombie11, 5, 5);
        battle.insertCreatureAt(zombie12, 6, 4);
        zombie3.faceNorth();
        zombie4.faceNorth();
        zombie5.faceNorth();
        zombie6.faceNorth();
        zombie7.faceNorth();
        zombie8.faceNorth();
        zombie9.faceNorth();
        zombie10.faceNorth();
        zombie11.faceNorth();
        zombie12.faceNorth();
    }

    private void initializeLevel8() {
        levelName = "Level 8: Big Bad Boss";

        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        hero.setSkillAsNumber(new SpinningPipe(3, 4), 3);
        hero.setSkillAsNumber(new Knockback(4, 4), 4);
        battle.insertCreatureAt(hero, 3, 4);
        hero.faceNorth();

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 1);
        nurse.setSkillAsNumber(new Innoculation(6, 2), 2);
        nurse.setSkillAsNumber(new MustardGas(7, 4), 3);
        nurse.setSkillAsNumber(new Push(8, 4), 4);
        battle.insertCreatureAt(nurse, 2, 5);
        nurse.faceNorth();

        soldier = new Creature("Soldier", FakeMain2.soldierMat, assetManager, this);
        soldier.setPicturePath("Interface/Images/Soldier.png");
        soldier.setSkillAsNumber(new AimedShot(9, 4), 1);
        soldier.setSkillAsNumber(new ShootEmAll(10, 4), 2);
        soldier.setSkillAsNumber(new Stab(11, 4), 3);
        soldier.setSkillAsNumber(new CutThroat(12, 1), 4);
        battle.insertCreatureAt(soldier, 4, 5);
        soldier.faceNorth();

        Creature boss = new Zombie("Boss", assetManager, this);
        boss.setMaxEnergy(boss.getMaxEnergy() * 4);
        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        Creature zombie2 = new Zombie("Zombie2", assetManager, this);
        Creature zombie3 = new Zombie("Zombie3", assetManager, this);
        Creature zombie4 = new Zombie("Zombie4", assetManager, this);
        battle.insertCreatureAt(boss, 3, 2);
        battle.insertCreatureAt(zombie1, 2, 1);
        battle.insertCreatureAt(zombie2, 1, 0);
        battle.insertCreatureAt(zombie3, 4, 1);
        battle.insertCreatureAt(zombie4, 5, 0);
        boss.faceSouth();
        zombie1.faceSouth();
        zombie2.faceSouth();
        zombie3.faceSouth();
        zombie4.faceSouth();
    }
    
    // Debugging function (TODO: remove)
    private void displayGameInfo() {
        System.out.println();
        System.out.println("*******************");
        System.out.println("GameState: " + gameState);
        System.out.println("Motion event playing: " + !noMotionEventPlaying());
        System.out.println("Creature using a skill: " + movingCreature);
        System.out.println();
    }
    
    // Debugging function (TODO: remove)
    private void enableScreenshots() {
        ScreenshotAppState screenShotState = new ScreenshotAppState();
        this.stateManager.attach(screenShotState);
    }
}
