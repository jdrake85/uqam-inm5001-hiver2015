package gameLogic;

import level.board.*;

import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.app.SimpleApplication;
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
    private static int commandType = -1;
    public static Geometry[][] g;
    public static GameBattle battle;
    public static Creature hero;
    //public static Spatial ninja;
    public static Nifty nifty;
    //public static int posX = 0;
    public static FakeMain2 app;
    //protected int posZ = 0;
    //protected static Scene scene1;

    @Override
    public void simpleInitApp() {
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);
        initScene();
        initKeys();

        battle = new GameBattle();

        hero = FakeMain.initializeHero(battle);
        FakeMain.initializeScenario(battle, hero);
    }

    private void initKeys() {
        inputManager.addMapping("Skill1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Skill2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Skill3", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("Skill4", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("Skill5", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("Skill6", new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping("Skill7", new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("Skill8", new KeyTrigger(KeyInput.KEY_8));
        inputManager.addMapping("Skill9", new KeyTrigger(KeyInput.KEY_9));
        inputManager.addMapping("Skill10", new KeyTrigger(KeyInput.KEY_0));
        inputManager.addMapping("Skill11", new KeyTrigger(KeyInput.KEY_MINUS));
        inputManager.addMapping("Skill12", new KeyTrigger(KeyInput.KEY_EQUALS));





        inputManager.addMapping("MoveKey", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("EnergyKey", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("SelectTile",
                new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click

        inputManager.addMapping("SelectTile", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        inputManager.addListener(actionListener, "Skill1");
        inputManager.addListener(actionListener, "Skill2");
        inputManager.addListener(actionListener, "Skill3");
        inputManager.addListener(actionListener, "Skill4");
        inputManager.addListener(actionListener, "Skill5");
        inputManager.addListener(actionListener, "Skill6");
        inputManager.addListener(actionListener, "Skill7");
        inputManager.addListener(actionListener, "Skill8");
        inputManager.addListener(actionListener, "Skill9");
        inputManager.addListener(actionListener, "Skill10");
        inputManager.addListener(actionListener, "Skill11");
        inputManager.addListener(actionListener, "Skill12");


        inputManager.addListener(actionListener, "SelectTile");
        inputManager.addListener(actionListener, "EnergyKey");
        inputManager.addListener(actionListener, "MoveKey");
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {

            //Equivalent du premier bouton
            // TODO; effacer
            if (name.equals("MoveKey") && !keyPressed && gameState.equals("idle")) {
                gameState = "move";
                battle.drawWithOverlayForCreatureMoves(hero);
            }

            if (name.equals("Skill1") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 1;

                battle.drawWithOverlayForCreatureSkill(hero, 1);
            }

            if (name.equals("Skill2") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 2;
                battle.drawWithOverlayForCreatureSkill(hero, 2);
            }

            if (name.equals("Skill3") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 3;
                battle.drawWithOverlayForCreatureSkill(hero, 3);
            }

            if (name.equals("Skill4") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 4;

                battle.drawWithOverlayForCreatureSkill(hero, 4);
            }
            
            if (name.equals("Skill5") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 5;

                battle.drawWithOverlayForCreatureSkill(hero, 5);
            }
            
            if (name.equals("Skill6") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 6;

                battle.drawWithOverlayForCreatureSkill(hero, 6);
            }
            
            if (name.equals("Skill7") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 7;

                battle.drawWithOverlayForCreatureSkill(hero, 7);
            }
            
            if (name.equals("Skill8") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 8;

                battle.drawWithOverlayForCreatureSkill(hero, 8);
            }
            
            if (name.equals("Skill9") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 9;

                battle.drawWithOverlayForCreatureSkill(hero, 9);
            }
            
            if (name.equals("Skill10") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 10;

                battle.drawWithOverlayForCreatureSkill(hero, 10);
            }
            
            if (name.equals("Skill11") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 11;

                battle.drawWithOverlayForCreatureSkill(hero, 11);
            }
            
            if (name.equals("Skill12") && !keyPressed && gameState.equals("idle")) {
                gameState = "skill";
                commandType = 12;

                battle.drawWithOverlayForCreatureSkill(hero, 12);
            }

            if (name.equals("EnergyKey") && !keyPressed && gameState.equals("idle")) {
                hero.setEnergy(hero.getEnergy() + 20);
                System.out.println(hero.getEnergy());
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

                    FakeMain.performTurn(commandType, commandX, commandY, hero, battle);
                    gameState = "idle";
                    commandType = 0;
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

                    FakeMain.performTurn(0, commandX, commandY, hero, battle);
                    gameState = "idle";
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
}
