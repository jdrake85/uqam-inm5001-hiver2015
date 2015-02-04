package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import java.util.ArrayList;

/**
 * Tech Demo 1 A Ninja moves on 3x3 grid WASD are used to move the ninja The
 * camera is locked
 */
public class Main extends SimpleApplication {

    public static Spatial ninja;
    public static int posX = 0;
    protected int posZ = 0;
    protected static Scene scene1;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        initScene();
        initKeys(); // load my custom keybinding

    }

    private void initKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Front", new KeyTrigger(KeyInput.KEY_W));

        inputManager.addListener(actionListener, "Left", "Right", "Back", "Front");
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Right") && (posX < 2) && keyPressed) {
                Vector3f v = ninja.getLocalTranslation();
                ninja.setLocalTranslation(v.x + 2, v.y, v.z);
                posX += 2;
            }
            if (name.equals("Left") && (posX > -2) && keyPressed) {
                Vector3f v = ninja.getLocalTranslation();
                ninja.setLocalTranslation(v.x - 2, v.y, v.z);
                posX -= 2;
            }
            if (name.equals("Front") && (posZ < 2) && keyPressed) {
                Vector3f v = ninja.getLocalTranslation();
                ninja.setLocalTranslation(v.x, v.y, v.z + 2);
                posZ += 2;
            }
            if (name.equals("Back") && (posZ > -2) && keyPressed) {
                Vector3f v = ninja.getLocalTranslation();
                ninja.setLocalTranslation(v.x, v.y, v.z - 2);
                posZ -= 2;
            }
        }
    };

    public void initScene() {
        scene1 = new Scene(assetManager);
        scene1.populate();
        ArrayList<Spatial> spatials = scene1.getSpatials();

        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);

        Node pivot = new Node("pivot");
        rootNode.attachChild(pivot); // put this node in the scene
        for (Spatial s : spatials) {
            pivot.attachChild(s);
        }
        // Load a model from test_data (OgreXML + material + texture)
        ninja = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        ninja.scale(0.02f, 0.02f, 0.02f);
        ninja.rotate(0.0f, -3.0f, 0.0f);
        ninja.setLocalTranslation(0.0f, -1.0f, 0.0f);

        pivot.attachChild(ninja);
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
    assetManager, inputManager, audioRenderer, guiViewPort);
/** Create a new NiftyGUI object */
Nifty nifty = niftyDisplay.getNifty();
/** Read your XML and initialize your custom ScreenController */
nifty.fromXml("Interface/screen.xml", "start");
// nifty.fromXml("Interface/helloworld.xml", "start", new MySettingsScreen(data));
// attach the Nifty display to the gui view port as a processor
guiViewPort.addProcessor(niftyDisplay);
// disable the fly cam
flyCam.setDragToRotate(true);
//nifty.fromXml("Interface/screen.xml", "start", new GUIOverlay());
    }
}