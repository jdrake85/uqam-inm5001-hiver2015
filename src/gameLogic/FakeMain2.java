package gameLogic;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.app.SimpleApplication;
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
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;
import gameLogic.pathfinding.Coordinates;
import gameLogic.skills.hero.*;
import gameLogic.skills.nurse.*;
import gameLogic.skills.soldier.*;

public class FakeMain2 extends SimpleApplication {

    public static void main(String[] args) {
        app = new FakeMain2();
        app.start();
    }
    protected Node pivot = new Node("pivot");
    protected Node mainNode = new Node("mainNode");
    protected static Node charNode = new Node("charNode");
    public static String gameState = "idle"; // idle, move, skill  
    public static Material greenMat; //TODO remove
    public static Material redMat; //TODO remove
    public static Material greyMat; //TODO remove
    public static Material redZombie;
    public static Material heroMat;
    public static Material nurseMat;
    public static Material soldierMat;
    public static int commandType = -1;
    public static Geometry[][] g;
    public static GameBattle battle;
    public static Creature creatureInCommand;
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
    public static boolean playedPreBattleCinematic = false;
    public static boolean battleInProgress = false;
    public static boolean playedPostBattleCinematic = false;

    @Override
    public void simpleInitApp() {
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);
        initScene();
        initKeys();

        battle = new GameBattle();

        // HERO GRAPHICS
        heroMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        heroMat.setColor("Color", new ColorRGBA(0.75f, 3f, 3f, 0f));
        //load temp hero mexh + animation
        assetManager.registerLocator("assets/Models/Hero/", FileLocator.class);
        heroScene = (Node) assetManager.loadModel("HeroScene.scene");
        heroScene.setLocalScale(.020f);
        acHero = findAnimControl(heroScene);
        channelHero = acHero.createChannel();
        animateIdle();

        // NURSE GRAPHICS
        nurseMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        nurseMat.setColor("Color", new ColorRGBA(0.75f, 6f, 3f, 0f));
        nurseScene = (Node) assetManager.loadModel("HeroScene.scene");
        nurseScene.setLocalScale(.015f);
        acNurse = findAnimControl(nurseScene);
        channelNurse = acNurse.createChannel();
        //animateIdle();

        // SOLDIER GRAPHICS
        soldierMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        soldierMat.setColor("Color", new ColorRGBA(0.75f, 9f, 3f, 0f));
        soldierScene = (Node) assetManager.loadModel("HeroScene.scene");
        soldierScene.setLocalScale(.025f);
        acSoldier = findAnimControl(soldierScene);
        channelSoldier = acSoldier.createChannel();
        //animateIdle();

        Creature hero = initializeHero(FakeMain2.battle);
        Creature nurse = initializeNurse(FakeMain2.battle);
        Creature soldier = initializeSoldier(battle);

        initializeScenario(FakeMain2.battle);
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
                gameState = "move";
                commandType = 0;
                battle.drawWithOverlayForCreatureMoves(creatureInCommand);
                animateMove(creatureInCommand);
            }

            // Ending turn
            if (name.equals("EndTurnKey") && !keyPressed && gameState.equals("idle")) {
                battle.endTurn();
                creatureInCommand = battle.getCreaturePlayingTurn();
                // Enemy turn(s), if next

            }

            // Ending turn
            if (name.equals("BannerRefresh") && !keyPressed && gameState.equals("idle")) {
                Creature[] priorityBanner = battle.getCreatureTurnOrder();
                System.out.print("PRIORITY BANNER: ");
                for (Creature creature : priorityBanner) {
                    System.out.print(creature + ", ");
                }
                System.out.println();
            }

            if (name.substring(0, 5).equals("Skill") && !keyPressed && gameState.equals("idle")) {
                try {
                    commandType = Integer.parseInt(name.substring(5));
                } catch (Exception e) {
                    commandType = 1;
                }
                gameState = "skill";
                battle.drawWithOverlayForCreatureSkill(creatureInCommand, commandType);
            }

            if (name.equals("EnergyKey") && !keyPressed && gameState.equals("idle")) {
                creatureInCommand.setEnergy(creatureInCommand.getEnergy() + 20);
                System.out.println(creatureInCommand.getEnergy());
            }

            if (name.equals("RestoreHealthKey") && !keyPressed && gameState.equals("idle")) {
                creatureInCommand.receiveDamage(-16);
                System.out.println(creatureInCommand + " is now at " + creatureInCommand.getHealth() + " health!");
            }

            if (name.equals("SelectTile") && !keyPressed && gameState.equals("skill")) {
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

                    gameState = "idle";
                    animateIdle();

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
                        battleInProgress = false;
                    } else {
                        battle.refreshCreatureList();
                    }
                }
            }


            if (name.equals("SelectTile") && !keyPressed && gameState.equals("move")) {
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
                    FakeMain2.animateIdle();
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
    };

    private void playZombieTurn() {
        Zombie zombie = (Zombie) creatureInCommand;
        zombie.initializeTurnEnergy();
        Coordinates attackPosition = battle.getCoordinatesForBestClosestTarget(zombie);
        MotionEvent nextMotionEvent = null;
        if (attackPosition != null) {
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

        //Debugging
        Creature zombieTarget = zombie.getCurrentTarget();
        System.out.println(zombie + " is currently pursuing " + zombieTarget + "...");
        boolean gameOver = false;

        if (zombieTarget != null && battle.zombieIsAdjacentToTarget(zombie)) {
            while (zombie.canPayEnergyCostForSkillNumber(1) && !gameOver) {
                battle.haveZombieAttackAdjacentTarget(zombie); // TODO: target already calculated...
                gameOver = !zombieTarget.isAlive();
            }
        }

        if (gameOver) {
            System.out.println("<PLACEHOLDER FUNCTION>: battle is lost, return to main menu");
            gameState = "finished";
        } else {
            battle.endTurn();
        }
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

        mainNode.setLocalTranslation(new Vector3f(-4, 4, -4));
        /**
         *
         */
        greenMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        greenMat.setColor("Color", new ColorRGBA(.1f, .75f, .1f, 0.5f));//R,B,G,Alphas
        greenMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        greyMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        greyMat.setColor("Color", new ColorRGBA(.1f, .1f, .1f, 1f));//R,B,G,Alphas
        greyMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        redMat.setColor("Color", new ColorRGBA(0.75f, 0f, 0f, 0.5f));//R,B,G,Alphas
        redMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        redZombie = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        redZombie.setColor("Color", new ColorRGBA(0.75f, 0f, 0f, 0f));//R,B,G,Alphas

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
        /**
         * Box ninja = new Box(0.2f,1.5f,0.2f); Geometry nin = new
         * Geometry("Box", ninja);
         *
         * Material mNin = new Material(assetManager,
         * "Common/MatDefs/Misc/Unshaded.j3md"); mNin.setColor("Color", new
         * ColorRGBA(0.25f,1f,0.5f,0f));//R,B,G,Alphas nin.setMaterial(mNin);
         * nin.setLocalTranslation(new Vector3f(0,-1,0));
         * charNode.attachChild(nin);
         *
         * Box zombie1 = new Box(0.2f,1.5f,0.2f); Geometry zomb1 = new
         * Geometry("Box", zombie1);
         *
         * Material mZomb = new Material(assetManager,
         * "Common/MatDefs/Misc/Unshaded.j3md"); mZomb.setColor("Color", new
         * ColorRGBA(0.75f,0f,0f,0f));//R,B,G,Alphas zomb1.setMaterial(mZomb);
         * zomb1.setLocalTranslation(new Vector3f(1,-1,1));
         * charNode.attachChild(zomb1);
         *
         * Box zombie2 = new Box(0.2f,1.5f,0.2f); Geometry zomb2 = new
         * Geometry("Box", zombie2);
         *
         * zomb2.setMaterial(mZomb); zomb2.setLocalTranslation(new
         * Vector3f(1,-1,2)); charNode.attachChild(zomb2); *
         */
        // ust add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);

        mainNode.attachChild(pivot);
        mainNode.attachChild(charNode);
        mainNode.rotate(0.7f, 0f, 0f);
        rootNode.attachChild(mainNode); // put this node in the scene
    }
    /*TODO
     public Material setGreenTileMat(){
     Material greenMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
     greenMat.setColor("Color", new ColorRGBA(.1f,.75f,.1f,0.5f));//R,B,G,Alphas
     greenMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
     return greenMat;
     }*/

    /*TODO
     public Material greyTileMat(){
     Material greyMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
     greyMat.setColor("Color", new ColorRGBA(.1f,.1f,.1f,1f));//R,B,G,Alphas
     greyMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
     return greyMat;
     }*/
    public Material floorMat() { //TODO placeHolder
        Material flMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        flMat.setColor("Color", new ColorRGBA(0.25f, 0, 0.75f, 11f));//R,B,G,Alphas
        return flMat;
    }

    public static void animateMove() {
        try {
            // create a channel and start the wobble animation
            channelHero.setAnim("Hop");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void animateMove(Creature creature) {
        try {
            // create a channel and start the wobble animation
            findAnimControl(creature).createChannel().setAnim("Hop");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void animateIdle() {
        try {
            // create a channel and start the wobble animation
            channelHero.setAnim("Idle");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void animateIdle(Creature creature) {
        try {
            // create a channel and start the wobble animation
            findAnimControl(creature).createChannel().setAnim("Idle");
        } catch (final Exception e) {
            e.printStackTrace();
        }
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
        if (currentMotionEvent != null) {
            System.out.println("PLAYING MOTION EVENT: " + currentMotionEvent + '\n');
            currentMotionEvent.play();
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (!gameState.equals("finished")) {
            if (battleInProgress) {
                if (battle.isWon()) {
                    System.out.println("<PLACEHOLDER FUNCTION / SIMPLE UPDATE>: battle is won");
                    gameState = "finished";
                } else if (battle.isLost()) {
                    System.out.println("<PLACEHOLDER FUNCTION / SIMPLE UPDATE>: battle is lost, return to main menu");
                    gameState = "finished";
                } else {
                    if (currentMotionEvent == null || currentMotionEvent.getPlayState().equals(PlayState.Stopped)) {
                        if (battle.isZombieTurn() && !gameState.equals("enemyBusy")) {
                            gameState = "enemyBusy";
                            playZombieTurn();
                            if (!gameState.equals("finished")) {
                                creatureInCommand = battle.getCreaturePlayingTurn();
                                gameState = "enemyIdle";
                            }
                        } else if (!(battle.isZombieTurn() || gameState.equals("move") || gameState.equals("skill"))) {
                            gameState = "idle";
                        }
                    }
                }
            } else if (!playedPreBattleCinematic) {
                System.out.println("<PLACEHOLDER FUNCTION / SIMPLE UPDATE>: play PRE battle cinematic now");
                playedPreBattleCinematic = true;
                battle.start();
                creatureInCommand = battle.getCreaturePlayingTurn();
                battleInProgress = true;
            } else if (!playedPostBattleCinematic) {
                System.out.println("<PLACEHOLDER FUNCTION / SIMPLE UPDATE>: play POST battle cinematic now");
                playedPostBattleCinematic = true;
            } else {
                playedPreBattleCinematic = battleInProgress = playedPostBattleCinematic = false;
                gameState = "finished";
                System.out.println("<PLACEHOLDER FUNCTION / SIMPLE UPDATE>: load next level now");
            }
        } else {
            //app.stop();
        }
    }

    protected static MotionEvent performTurn(int commandType, int commandX, int commandY, Creature creature, GameBattle battle) {
        boolean keepPlaying = true;
        MotionEvent motionEvent = null;
        if (commandX == 8 || commandY == 8) {
            System.out.println("Energy boost +40!");
            creature.setEnergy(creature.getEnergy() + 40);
        } else if (commandX == -1 || commandY == -1) {
            keepPlaying = false;
        } else if (commandType == 0) {
            System.out.println("Moving to (" + commandX + ", " + commandY + ")...");
            motionEvent = battle.moveCreatureTo(creature, new Coordinates(commandX, commandY));
        } else if (commandType >= 1 && commandType <= 12) {
            System.out.println("Using skill " + creature.prepareSkill(commandType) + " at (" + commandX + ", " + commandY + ")...");
            motionEvent = battle.useCreatureSkillAt(creature, commandType, new Coordinates(commandX, commandY));
        } else {
            System.out.println("** Unrecognized commands; ending turn.");
        }
        battle.refreshCreatureList();
        return motionEvent;
    }

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

    protected static Creature initializeHero(GameBattle battle) {
        Creature hero = new Creature("Hero", FakeMain2.heroMat, heroScene);
        hero.setAlignment("good");
        battle.insertCreatureAt(hero, 7, 1);
        assignAllSkillsTo(hero);
        /* TODO
         hero.setSkillAsNumber(new Strike(1, 4), 1);
         hero.setSkillAsNumber(new HomeRun(2, 4), 2);
         hero.setSkillAsNumber(new SpinningPipe(3, 4), 3);
         hero.setSkillAsNumber(new Knockback(4, 4), 4);
         * */
        hero.setSpeed(10);
        return hero;
    }

    protected static Creature initializeNurse(GameBattle battle) {
        Creature nurse = new Creature("Nurse", FakeMain2.nurseMat, nurseScene);
        nurse.setAlignment("good");
        battle.insertCreatureAt(nurse, 7, 3);
        assignAllSkillsTo(nurse);
        /* TODO
         nurse.setSkillAsNumber(new Heal(5, 4), 1);
         nurse.setSkillAsNumber(new Innoculation(6, 4), 2);
         nurse.setSkillAsNumber(new MustardGas(7, 4), 3);
         nurse.setSkillAsNumber(new Push(8, 4), 4);
         * */
        return nurse;
    }

    protected static Creature initializeSoldier(GameBattle battle) {
        Creature soldier = new Creature("Soldier", FakeMain2.soldierMat, soldierScene);
        soldier.setAlignment("good");
        battle.insertCreatureAt(soldier, 7, 5);
        assignAllSkillsTo(soldier);
        /* TODO
         soldier.setSkillAsNumber(new AimedShot(9, 4), 1);
         soldier.setSkillAsNumber(new ShootEmAll(10, 4), 2);
         soldier.setSkillAsNumber(new Stab(11, 4), 3);
         soldier.setSkillAsNumber(new CutThroat(12, 1), 4);
         */
        return soldier;
    }

    protected static void initializeScenario(GameBattle battle) {
        Creature zombie1 = new Zombie("ZombieA1(FE)");
        zombie1.setSpeed(8);
        Creature zombie2 = new Zombie("ZombieA2(FL)");
        zombie2.setSpeed(9);
        zombie2.setMaxEnergy(zombie1.getMaxEnergy() / 2);
        Creature zombie3 = new Zombie("ZombieA3(--)");
        zombie3.setSpeed(10);
        Creature zombie4 = new Zombie("ZombieA4(SL)");
        zombie4.setSpeed(11);
        zombie4.setMaxEnergy(zombie1.getMaxEnergy() / 2);
        Creature zombie5 = new Zombie("ZombieA5(SE)");
        zombie5.setSpeed(12);
        battle.insertCreatureAt(zombie1, 1, 1);
        battle.insertCreatureAt(zombie2, 1, 2);
        battle.insertCreatureAt(zombie3, 3, 1);
        battle.insertCreatureAt(zombie4, 3, 2);
        battle.insertCreatureAt(zombie5, 3, 3);
    }
}
