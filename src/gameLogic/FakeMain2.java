package gameLogic;

import gameLogic.creatures.Zombie;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
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
import gameLogic.skills.hero.*;
import gameLogic.skills.nurse.*;
import gameLogic.skills.soldier.*;

public class FakeMain2 extends SimpleApplication implements AnimEventListener {

    public static void main(String[] args) {
        app = new FakeMain2();
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
    public static final boolean FREQUENT = true;

    @Override
    public void simpleInitApp() {
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);
        initScene();
        initKeys();

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

        // (DEBUGGING) Specifiy starting level here (first level is 1)
        level = 1;
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

        inputManager.addListener(actionListener, "BannerRefresh");
        inputManager.addListener(actionListener, "MoveKey");
        inputManager.addListener(actionListener, "EnergyKey");
        inputManager.addListener(actionListener, "RestoreHealthKey");
        inputManager.addListener(actionListener, "SelectTile");
        inputManager.addListener(actionListener, "EndTurnKey");
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            //Equivalent du premier bouton
            // TODO; effacer
            if (name.equals("MoveKey") && !keyPressed && gameState.equals("idle")) {
                requestMove();
            }

            // Ending turn
            if (name.equals("EndTurnKey") && !keyPressed && gameState.equals("idle")) {
                endTurn();

            }

            // Ending turn
            if (name.equals("BannerRefresh") && !keyPressed && gameState.equals("idle")) {
                refreshAndDisplayBanner();
            }

            if (name.substring(0, 5).equals("Skill") && !keyPressed && gameState.equals("idle")) {
                try {
                    commandType = Integer.parseInt(name.substring(5));
                } catch (Exception e) {
                    commandType = 1;
                }
                requestSkill(commandType);
            }

            if (name.equals("EnergyKey") && !keyPressed && gameState.equals("idle")) {
                increaseEnergy();
            }

            if (name.equals("RestoreHealthKey") && !keyPressed && gameState.equals("idle")) {
                restoreHealth();
            }

            if (name.equals("SelectTile") && !keyPressed && gameState.equals("skill")) {
                confirmSkill();
            }


            if (name.equals("SelectTile") && !keyPressed && gameState.equals("move")) {
                confirmMove();
            }
        }
    };

    public void requestMove() {
        if (noMotionEventPlaying() && !movingCreature) {
            gameState = "move";
            commandType = 0;
            battle.drawWithOverlayForCreatureMoves(creatureInCommand);
            creatureInCommand.animateMove();
        }
    }
    
    public void endTurn() {
        if (noMotionEventPlaying() && !movingCreature) {
            battle.endTurn();
            creatureInCommand = battle.getCreaturePlayingTurn();
            ((GameState)(nifty.getCurrentScreen().getScreenController())).update(!FREQUENT);
            // Enemy turn(s), if next
        }
    }

    public void refreshAndDisplayBanner() {
        if (noMotionEventPlaying() && !movingCreature) {
            Creature[] priorityBanner = battle.getCreatureTurnOrder();
            System.out.print("PRIORITY BANNER: ");
            for (Creature creature : priorityBanner) {
                System.out.print(creature + ", ");
            }
            System.out.println();
        }
    }

    public void requestSkill(int command) {
        if (noMotionEventPlaying() && !movingCreature) {
            commandType = command;
            gameState = "skill";
            battle.drawWithOverlayForCreatureSkill(creatureInCommand, commandType);
        }
    }

    public void increaseEnergy() {
        if (noMotionEventPlaying() && !movingCreature) {
            creatureInCommand.setEnergy(creatureInCommand.getEnergy() + 20);
            System.out.println(creatureInCommand.getEnergy());
        }
    }

    public void restoreHealth() {
        if (noMotionEventPlaying() && !movingCreature) {
            creatureInCommand.receiveDamage(-16);
            System.out.println(creatureInCommand + " is now at " + creatureInCommand.getHealth() + " health!");
        }
    }

    public void confirmSkill() {
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

                MotionEvent nextMotionEvent = FakeMain2.performTurn(FakeMain2.commandType, commandX, commandY, FakeMain2.creatureInCommand, FakeMain2.battle);
                setAndPlayNextMotionEvent(nextMotionEvent);
                // TODO HERE


                /*
                 MyMaterial greenTile = new MyMaterial(assetManager);
                 greenTile.setGreenTileMat();
                 closest.setMaterial(greenTile.getMat());*/
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        g[i][j].setMaterial(greyMat);
                        g[i][j].setQueueBucket(Bucket.Translucent);
                    }
                }
                if (battle.isWon()) {
                    System.out.println("<PLACEHOLDER FUNCTION>: battle is won");
                    gameState = "loadPostBattleCinematic";
                    battleInProgress = false;
                } else {
                    battle.refreshCreatureList();
                    ((GameState)(nifty.getCurrentScreen().getScreenController())).update(!FREQUENT);
                }

            }
            gameState = "idle";
            // creatureInCommand.animateIdle(); // TODO: specify creature
        }
    }

    public void confirmMove() {
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


                MotionEvent nextMotionEvent = FakeMain2.performTurn(0, commandX, commandY, FakeMain2.creatureInCommand, FakeMain2.battle);
                setAndPlayNextMotionEvent(nextMotionEvent);

                gameState = "idle";
                //creatureInCommand.animateIdle(); // TODO: specify creature
                /*
                 MyMaterial greenTile = new MyMaterial(assetManager);
                 greenTile.setGreenTileMat();
                 closest.setMaterial(greenTile.getMat());*/
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        g[i][j].setMaterial(greyMat);
                        g[i][j].setQueueBucket(Bucket.Translucent);
                    }
                }
            }
        }
    }

    private Zombie performMovementForZombieTurn() {
        Zombie zombie = (Zombie) creatureInCommand;
        zombie.initializeTurnEnergy();
        Coordinates attackPosition = battle.getCoordinatesForBestClosestTarget(zombie);
        MotionEvent nextMotionEvent = null;
        if (attackPosition != null) {
            creatureInCommand.animateMove();
            nextMotionEvent = battle.moveCreatureTo(zombie, attackPosition);
            setAndPlayNextMotionEvent(nextMotionEvent);
            battle.draw();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    g[i][j].setMaterial(greyMat);
                    g[i][j].setQueueBucket(Bucket.Translucent);
                }
            }
        }
        return zombie;
    }

    private boolean performAttackOnPlayerForZombieTurn() {

        //Debugging
        Zombie zombie = (Zombie) creatureInCommand;
        Creature zombieTarget = zombie.getCurrentTarget();
        System.out.println(zombie + " is currently pursuing " + zombieTarget + "...");
        boolean gameOver = false;

        if (zombieTarget != null && battle.zombieIsAdjacentToTarget(zombie)) {
            //rotateModelsForSkillUserAndAffectedCreatures(zombieTarget);
            battle.haveZombieAttackAdjacentTarget(zombie);
            gameOver = !zombieTarget.isAlive();
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
        if (!gameState.equals("outOfLevel")) {
            if (battleInProgress) {
        ((GameState)(nifty.getCurrentScreen().getScreenController())).update(FREQUENT);
                if (!battle.isWon()) {
                    if (noMotionEventPlaying() && !movingCreature) {
                        if (lastDamageNode != null) {
                            lastDamageNode.getParent().detachChild(lastDamageNode);
                            lastDamageNode = null;
                        }
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
                                if (zombie.hasEnoughEnergyToAttack()) {
                                    if (performAttackOnPlayerForZombieTurn()) {
                                        System.out.println("<PLACEHOLDER FUNCTION>: battle is lost, return to main menu");
                                        gameState = "outOfLevel";
                                        level--;
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
                ((GameState)(nifty.getCurrentScreen().getScreenController())).update(!FREQUENT);
                battleInProgress = true;
            } else if (!playedPostBattleCinematic) {
                battleInProgress = false;
                System.out.println("<PLACEHOLDER FUNCTION / SIMPLE UPDATE>: play POST battle cinematic now");
                playedPostBattleCinematic = true;
                gameState = "outOfLevel";
            }
        } else {
            if (level < 9) {
                System.out.println();
                System.out.println("---------------------------------");
                System.out.println("## LOADING LEVEL " + level + " ##");
                System.out.println("---------------------------------");
                System.out.println();
                playedPreBattleCinematic = battleInProgress = playedPostBattleCinematic = false;
                currentMovingZombie = null;
                movingCreature = false;
                initializeBattleForLevel(level++);
                gameState = "idle";
            } else if (level == 9) {
                System.out.println("Congratulations!");
                app.stop();
            }
        }
    }

    protected static MotionEvent performTurn(int commandType, int commandX, int commandY, Creature creature, GameBattle battle) {
        MotionEvent motionEvent = null;
        if (commandX == 8 || commandY == 8) {
            System.out.println("Energy boost +40!");
            creature.setEnergy(creature.getEnergy() + 40);
        } else if (commandType == 0) {
            System.out.println("Moving to (" + commandX + ", " + commandY + ")...");
            motionEvent = battle.moveCreatureTo(creature, new Coordinates(commandX, commandY));
            //} else if (commandType = )
        } else if (commandType >= 1 && commandType <= 12) {
            System.out.println("Using skill " + creature.prepareSkill(commandType) + " at (" + commandX + ", " + commandY + ")...");
            motionEvent = battle.useCreatureSkillAt(creature, commandType, new Coordinates(commandX, commandY));
        } else {
            System.out.println("** Unrecognized commands; ending turn.");
        }
        return motionEvent;
    }

    public static Vector3f coordinatesWithHeightToVector3f(int x, float height, int y) {
        Vector3f vector = null;
        if (Math.min(x, y) >= 0 && Math.max(x, y) < 8) {
            vector = g[x][y].getWorldTranslation();
            vector.setY(height);
        }
        return vector;
    }

    private boolean noMotionEventPlaying() {
        return currentMotionEvent == null || currentMotionEvent.getPlayState().equals(PlayState.Stopped);
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("Skill1") || animName.equals("Skill2") || animName.equals("Skill13")) {
            movingCreature = false;
        }
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        // Do nothing
    }

    private void initializeBattleForLevel(int level) {
        if (battle != null) {
            battle.clearCombattants();
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

    // Level 1: 'Freedom'
    private void initializeLevel1() {
        soldier = null;
        nurse = null;
        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        battle.insertCreatureAt(hero, 1, 4);


        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        battle.insertCreatureAt(zombie1, 6, 4);

        // TODO: remove later
        //assignAllSkillsTo(hero);
    }

    // Level 2: 'Damsel in Distress'
    private void initializeLevel2() {
        soldier = null;

        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        battle.insertCreatureAt(hero, 1, 4);

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 5);
        nurse.setSkillAsNumber(new Push(8, 4), 8);

        battle.insertCreatureAt(nurse, 7, 0);

        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        battle.insertCreatureAt(zombie1, 6, 1);

        // TODO: remove later
        assignAllSkillsTo(hero);
        assignAllSkillsTo(nurse);
    }

    // Level 3: 'Getting through'
    private void initializeLevel3() {
        soldier = null;

        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        battle.insertCreatureAt(hero, 3, 6);

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 5);
        nurse.setSkillAsNumber(new Push(8, 4), 8);
        battle.insertCreatureAt(nurse, 3, 4);

        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        Creature zombie2 = new Zombie("Zombie2", assetManager, this);
        Creature zombie3 = new Zombie("Zombie3", assetManager, this);
        battle.insertCreatureAt(zombie1, 1, 2);
        battle.insertCreatureAt(zombie2, 5, 2);
        battle.insertCreatureAt(zombie3, 3, 1);

        // TODO: remove later
        assignAllSkillsTo(hero);
        assignAllSkillsTo(nurse);
    }

    // Level 4: 'Pincer Attack'
    private void initializeLevel4() {
        soldier = null;

        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        battle.insertCreatureAt(hero, 3, 3);

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 5);
        nurse.setSkillAsNumber(new Innoculation(6, 4), 6);
        nurse.setSkillAsNumber(new Push(8, 4), 8);
        battle.insertCreatureAt(nurse, 3, 4);

        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        Creature zombie2 = new Zombie("Zombie2", assetManager, this);
        Creature zombie3 = new Zombie("Zombie3", assetManager, this);
        Creature zombie4 = new Zombie("Zombie4", assetManager, this);
        battle.insertCreatureAt(zombie1, 1, 1);
        battle.insertCreatureAt(zombie2, 1, 6);
        battle.insertCreatureAt(zombie3, 6, 6);
        battle.insertCreatureAt(zombie4, 6, 1);

        // TODO: remove later
        assignAllSkillsTo(hero);
        assignAllSkillsTo(nurse);
    }

    // Level 5: 'Making Friends'
    private void initializeLevel5() {
        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        battle.insertCreatureAt(hero, 1, 6);

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 5);
        nurse.setSkillAsNumber(new Innoculation(6, 4), 6);
        nurse.setSkillAsNumber(new Push(8, 4), 8);
        battle.insertCreatureAt(nurse, 2, 7);

        soldier = new Creature("Soldier", FakeMain2.soldierMat, assetManager, this);
        soldier.setPicturePath("Interface/Images/Soldier.png");
        soldier.setSkillAsNumber(new AimedShot(9, 4), 9);
        soldier.setSkillAsNumber(new Stab(11, 4), 11);
        battle.insertCreatureAt(soldier, 7, 0);

        Creature zombie1 = new Zombie("Zombie1", assetManager, this);
        Creature zombie2 = new Zombie("Zombie2", assetManager, this);
        Creature zombie3 = new Zombie("Zombie3", assetManager, this);
        Creature zombie4 = new Zombie("Zombie4", assetManager, this);
        battle.insertCreatureAt(zombie1, 2, 4);
        battle.insertCreatureAt(zombie2, 2, 5);
        battle.insertCreatureAt(zombie3, 6, 0);
        battle.insertCreatureAt(zombie4, 7, 1);

        // TODO: remove later
        assignAllSkillsTo(hero);
        assignAllSkillsTo(nurse);
        assignAllSkillsTo(soldier);
    }

    // Level 6: 'Showdown'
    private void initializeLevel6() {
        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        hero.setSkillAsNumber(new SpinningPipe(3, 4), 3);
        battle.insertCreatureAt(hero, 5, 4);

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 5);
        nurse.setSkillAsNumber(new Innoculation(6, 4), 6);
        nurse.setSkillAsNumber(new Push(8, 4), 8);
        battle.insertCreatureAt(nurse, 6, 3);

        soldier = new Creature("Soldier", FakeMain2.soldierMat, assetManager, this);
        soldier.setPicturePath("Interface/Images/Soldier.png");
        soldier.setSkillAsNumber(new AimedShot(9, 4), 9);
        soldier.setSkillAsNumber(new Stab(11, 4), 11);
        battle.insertCreatureAt(soldier, 6, 5);

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

        // TODO: remove later
        assignAllSkillsTo(hero);
        assignAllSkillsTo(nurse);
        assignAllSkillsTo(soldier);
    }

    // Level 7: 'Surrounded'
    private void initializeLevel7() {
        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        hero.setSkillAsNumber(new SpinningPipe(3, 4), 3);
        battle.insertCreatureAt(hero, 5, 1);

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 5);
        nurse.setSkillAsNumber(new Innoculation(6, 4), 6);
        nurse.setSkillAsNumber(new MustardGas(7, 4), 7);
        nurse.setSkillAsNumber(new Push(8, 4), 8);
        battle.insertCreatureAt(nurse, 6, 0);

        soldier = new Creature("Soldier", FakeMain2.soldierMat, assetManager, this);
        soldier.setPicturePath("Interface/Images/Soldier.png");
        soldier.setSkillAsNumber(new AimedShot(9, 4), 9);
        soldier.setSkillAsNumber(new ShootEmAll(10, 4), 10);
        soldier.setSkillAsNumber(new Stab(11, 4), 11);
        battle.insertCreatureAt(soldier, 6, 1);

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

        // TODO: remove later
        assignAllSkillsTo(hero);
        assignAllSkillsTo(nurse);
        assignAllSkillsTo(soldier);
    }

    // Level 8: 'Big Bad Boss'
    private void initializeLevel8() {
        hero = new Creature("Hero", FakeMain2.heroMat, assetManager, this);
        hero.setPicturePath("Interface/Images/Hero.png");
        hero.setSkillAsNumber(new Strike(1, 4), 1);
        hero.setSkillAsNumber(new HomeRun(2, 4), 2);
        hero.setSkillAsNumber(new SpinningPipe(3, 4), 3);
        hero.setSkillAsNumber(new Knockback(4, 4), 4);
        battle.insertCreatureAt(hero, 3, 4);

        nurse = new Creature("Nurse", FakeMain2.nurseMat, assetManager, this);
        nurse.setPicturePath("Interface/Images/Nurse.png");
        nurse.setSkillAsNumber(new Heal(5, 4), 5);
        nurse.setSkillAsNumber(new Innoculation(6, 4), 6);
        nurse.setSkillAsNumber(new MustardGas(7, 4), 7);
        nurse.setSkillAsNumber(new Push(8, 4), 8);
        battle.insertCreatureAt(nurse, 2, 5);

        soldier = new Creature("Soldier", FakeMain2.soldierMat, assetManager, this);
        soldier.setPicturePath("Interface/Images/Soldier.png");
        soldier.setSkillAsNumber(new AimedShot(9, 4), 9);
        soldier.setSkillAsNumber(new ShootEmAll(10, 4), 10);
        soldier.setSkillAsNumber(new Stab(11, 4), 11);
        soldier.setSkillAsNumber(new CutThroat(12, 1), 12);
        battle.insertCreatureAt(soldier, 4, 5);

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

        // TODO: remove later
        //assignAllSkillsTo(hero);
        //assignAllSkillsTo(nurse);
        //assignAllSkillsTo(soldier);
    }

    // TODO: remove later
    protected static void assignAllSkillsTo(Creature creature) {
        creature.setSkillAsNumber(new Strike(1, 4), 1);
        creature.setSkillAsNumber(new HomeRun(2, 4), 2);
        creature.setSkillAsNumber(new SpinningPipe(3, 4), 3);
        creature.setSkillAsNumber(new Knockback(4, 4), 4);
        creature.setSkillAsNumber(new Heal(5, 4), 5);
        creature.setSkillAsNumber(new Innoculation(6, 4), 6);
        creature.setSkillAsNumber(new MustardGas(7, 4), 7);
        creature.setSkillAsNumber(new Push(8, 4), 8);
        creature.setSkillAsNumber(new AimedShot(9, 4), 9);
        creature.setSkillAsNumber(new ShootEmAll(10, 4), 10);
        creature.setSkillAsNumber(new Stab(11, 4), 11);
        creature.setSkillAsNumber(new CutThroat(12, 1), 12);
    }
}
