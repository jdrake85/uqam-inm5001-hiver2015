package gameLogic;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import level.board.*;

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
import mygame.Main;
import static mygame.Main.nifty;
import mygame.Scene;

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
    public static AnimControl ac;
    public static AnimChannel channel;
    public MotionEvent currentMotionEvent = null;

    @Override
    public void simpleInitApp() {
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);
        initScene();
        initKeys();

        battle = new GameBattle();

        // Distinctive hero colours
        heroMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        heroMat.setColor("Color", new ColorRGBA(0.75f, 4f, 3f, 0f));

        //load temp hero mexh + animation
        assetManager.registerLocator("assets/Models/Hero/", FileLocator.class);
        heroScene = (Node) assetManager.loadModel("HeroScene.scene");
        heroScene.setLocalScale(.015f);
        ac = findAnimControl(heroScene);
        channel = ac.createChannel();
        animateIdle();

        creatureInCommand = FakeMain.initializeHero(battle);
        FakeMain.initializeScenario(battle, creatureInCommand);
        battle.start();
        creatureInCommand = battle.getCreaturePlayingTurn();
    }

    private void initKeys() {

        for (int i = 1; i <= 12; i++) {
            String skillName = "Skill" + i;
            inputManager.addMapping(skillName, new KeyTrigger(KeyInput.KEY_1 + i - 1));
            inputManager.addListener(actionListener, skillName);
        }

        inputManager.addMapping("MoveKey", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("EnergyKey", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("SelectTile",
                new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        inputManager.addMapping("EndTurnKey", new KeyTrigger(KeyInput.KEY_Q));

        inputManager.addListener(actionListener, "MoveKey");
        inputManager.addListener(actionListener, "EnergyKey");
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
                animateMove();
            }

            // Ending turn
            if (name.equals("EndTurnKey") && !keyPressed && gameState.equals("idle")) {
                battle.endTurn();
                creatureInCommand = battle.getCreaturePlayingTurn();
                // Enemy turn(s), if next

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

                    MotionEvent nextMotionEvent = FakeMain.performTurn(commandType, commandX, commandY, creatureInCommand, battle);
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
                }

                battle.refreshCreatureList();
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


                    MotionEvent nextMotionEvent = FakeMain.performTurn(0, commandX, commandY, creatureInCommand, battle);
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
        creatureInCommand.initializeTurnEnergy();
        Coordinates attackPosition = battle.getCoordinatesForBestClosestTarget((Zombie) creatureInCommand);
        MotionEvent nextMotionEvent = null;
        if (attackPosition != null) {
            nextMotionEvent = battle.moveCreatureTo(creatureInCommand, attackPosition);
            setAndPlayNextMotionEvent(nextMotionEvent);
            battle.draw();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    g[i][j].setMaterial(greyMat);
                    g[i][j].setQueueBucket(Bucket.Translucent);
                }
            }
        } 
        
        
        

        Creature zombieTarget = battle.getTargetAdjacentToZombie(creatureInCommand);
        // TODO: remove after debugging
        if (attackPosition == null && zombieTarget == null) {
            System.out.println("*** ERROR: " + creatureInCommand + " could not calculate how to approach a hero");
        }
        
        if (zombieTarget != null) {
            while (creatureInCommand.canPayEnergyCostForSkillNumber(1) && zombieTarget.isAlive()) {
                battle.haveZombieAttackAnyAdjacentGoodCreatures(creatureInCommand); // TODO: target already calculated...
            }
            if (!zombieTarget.isAlive()) {
                gameState = "gameOver";
                battle.activateGameOver();
                System.out.println("Game over!");
            }
        }

        battle.endTurn();
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
            channel.setAnim("Hop");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void animateIdle() {
        try {
            // create a channel and start the wobble animation
            channel.setAnim("Idle");
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
        if (!battle.isGameOver()) {
            if (currentMotionEvent == null || currentMotionEvent.getPlayState().equals(PlayState.Stopped)) {
                if (battle.isZombieTurn() && !gameState.equals("enemyBusy")) {
                    gameState = "enemyBusy";
                    playZombieTurn();
                    creatureInCommand = battle.getCreaturePlayingTurn();
                    gameState = "enemyIdle";
                } else if (!(battle.isZombieTurn() || gameState.equals("move") || gameState.equals("skill"))) {
                    gameState = "idle";
                }
            }
        }
    }
}
