package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
//import com.jme3.renderer.RendererManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
 
/** Tech Demo 1
 * A Ninja moves on 3x3 grid
 * WASD are used to move the ninja
 * The camera is locked */
public class Main extends SimpleApplication {
 
    public static void main(String[] args){
        Main app = new Main();
        app.start();
    }
    
    protected Spatial ninja;
    protected int posX = 0;
    protected int posZ = 0;
 
    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        initScene();
        initKeys(); // load my custom keybinding
    }
    
    private void initKeys() {
    inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Back",   new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Front",  new KeyTrigger(KeyInput.KEY_W));
    
    inputManager.addListener(actionListener,"Left","Right","Back","Front");
  }

  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("Right") && (posX < 2 ) && keyPressed) {
          Vector3f v = ninja.getLocalTranslation();
          ninja.setLocalTranslation(v.x + 2, v.y, v.z);
          posX += 2;
        }
        if (name.equals("Left") && (posX > -2) && keyPressed) {
          Vector3f v = ninja.getLocalTranslation();
          ninja.setLocalTranslation(v.x - 2, v.y, v.z);
          posX -= 2;
        }
        if (name.equals("Front") && (posZ < 2 ) && keyPressed) {
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
        Box b1 = new Box(1,1,1);
        Geometry g1 = new Geometry("Box", b1);
        g1.setLocalTranslation(new Vector3f(-2,-2,-2));
        Material m1 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        m1.setColor("Color", ColorRGBA.Blue);
        g1.setMaterial(m1);
 
        Box b2 = new Box(1,1,1);      
        Geometry g2 = new Geometry("Box", b2);
        g2.setLocalTranslation(new Vector3f(0,-2,-2));
        Material m2 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        m2.setColor("Color", ColorRGBA.Red);
        g2.setMaterial(m2);
        
        Box b3 = new Box(1,1,1);
        Geometry g3 = new Geometry("Box", b3);
        g3.setLocalTranslation(new Vector3f(2,-2,-2));
        Material m3 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        m3.setColor("Color", ColorRGBA.Blue);
        g3.setMaterial(m3);
 
        Box b4 = new Box(1,1,1);      
        Geometry g4 = new Geometry("Box", b4);
        g4.setLocalTranslation(new Vector3f(-2,-2,0));
        Material m4 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        m4.setColor("Color", ColorRGBA.Red);
        g4.setMaterial(m4);
        
        Box b5 = new Box(1,1,1);
        Geometry g5 = new Geometry("Box", b5);
        g5.setLocalTranslation(new Vector3f(0,-2,0));
        Material m5 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        m5.setColor("Color", ColorRGBA.Blue);
        g5.setMaterial(m5);
 
        Box b6 = new Box(1,1,1);      
        Geometry g6 = new Geometry("Box", b6);
        g6.setLocalTranslation(new Vector3f(2,-2,0));
        Material m6 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        m6.setColor("Color", ColorRGBA.Red);
        g6.setMaterial(m6);
        
        Box b7 = new Box(1,1,1);
        Geometry g7 = new Geometry("Box", b7);
        g7.setLocalTranslation(new Vector3f(-2,-2,2));
        Material m7 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        m7.setColor("Color", ColorRGBA.Blue);
        g7.setMaterial(m7);
 
        Box b8 = new Box(1,1,1);      
        Geometry g8 = new Geometry("Box", b8);
        g8.setLocalTranslation(new Vector3f(0,-2,2));
        Material m8 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        m8.setColor("Color", ColorRGBA.Red);
        g8.setMaterial(m8);
        
        Box b9 = new Box(1,1,1);
        Geometry g9 = new Geometry("Box", b9);
        g9.setLocalTranslation(new Vector3f(2,-2,2));
        Material m9 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        m9.setColor("Color", ColorRGBA.Blue);
        g9.setMaterial(m9);
        
        // Load a model from test_data (OgreXML + material + texture)
        ninja = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        ninja.scale(0.02f, 0.02f, 0.02f);
        ninja.rotate(0.0f, -3.0f, 0.0f);
        ninja.setLocalTranslation(0.0f, -1.0f, 0.0f);

        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);      
 
        /** Create a pivot node at (0,0,0) and attach it to the root node */
        /* Je travaillais a partir d'un fichier de demonstration,
         * comme c'est juste une tech demo, j'ai laisse l'objet pivot
         * meme si celui-ci n'est pas uitilise
         */
        Node pivot = new Node("pivot");
        rootNode.attachChild(pivot); // put this node in the scene

        pivot.attachChild(g1);
        pivot.attachChild(g2);
        pivot.attachChild(g3);
        pivot.attachChild(g4);
        pivot.attachChild(g5);
        pivot.attachChild(g6);
        pivot.attachChild(g7);
        pivot.attachChild(g8);
        pivot.attachChild(g9);
        pivot.attachChild(ninja);       
    }
}