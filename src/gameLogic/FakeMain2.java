package gameLogic;
 
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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class FakeMain2 extends SimpleApplication {
 
    public static void main(String[] args){
        FakeMain2 app = new FakeMain2();
        app.start();
    }

    protected Node pivot = new Node("pivot");
    protected Node mainNode = new Node("mainNode");
    protected Node charNode = new Node ("charNode");
    protected String gameState = "idle"; // idle, move, skill  
    
    public static Material greenMat; //TODO remove
    public static Material greyMat; //TODO remove
        
    public static Geometry[][] g;
    
    protected GameBattle battle;
    protected Creature hero;
 
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
        inputManager.addMapping("MoveKey", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("SelectTile",
            new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
    
        inputManager.addListener(actionListener, "SelectTile");
        inputManager.addListener(actionListener, "MoveKey");
  }
  

private ActionListener actionListener = new ActionListener() {
 
    public void onAction(String name, boolean keyPressed, float tpf) {
        
      if (name.equals("MoveKey") && !keyPressed && gameState.equals("idle")){
          gameState = "move";
          battle.drawWithOverlayForCreatureMoves(hero);
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
        
        // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
          // The closest collision point is what was truly hit:
          Geometry closest = results.getClosestCollision().getGeometry();
          // Let's interact - we mark the hit with a red dot.
          Material mx = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
            mx.setColor("Color", new ColorRGBA(1f,1f,0f,1));//R,B,G,Alphas
          closest.setMaterial(greenMat);
        }
      }
    }
  };
  
    
    public void initScene() {
        
        mainNode.setLocalTranslation(new Vector3f(-4,4,-4));
        /***/
        greenMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        greenMat.setColor("Color", new ColorRGBA(.1f,.75f,.1f,0.5f));//R,B,G,Alphas
        greenMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        
        greyMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        greyMat.setColor("Color", new ColorRGBA(.1f,.1f,.1f,1f));//R,B,G,Alphas
        greyMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        
        /**/
        Box plancher = new Box(4,0,4);
        Geometry gp = new Geometry("Box", plancher);

        gp.setMaterial(floorMat());
        gp.setLocalTranslation(new Vector3f(3.5f,-2.01f,3.5f));
        mainNode.attachChild(gp);
        /**/
        Box b1 = new Box(0.45f,0,0.45f);
        g = new Geometry[8][8];
        
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                g[i][j] = new Geometry("box", b1);
                g[i][j].setLocalTranslation(new Vector3f(i,-2,j));
                g[i][j].setMaterial(greyMat);
                g[i][j].setQueueBucket(Bucket.Translucent);
                pivot.attachChild(g[i][j]);
            }
        }
        /**/
        Box ninja = new Box(0.2f,1.5f,0.2f);
        Geometry nin = new Geometry("Box", ninja);

        Material mNin = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mNin.setColor("Color", new ColorRGBA(0.25f,1f,0.5f,0f));//R,B,G,Alphas
        nin.setMaterial(mNin);
        nin.setLocalTranslation(new Vector3f(0,-1,0));
        charNode.attachChild(nin);
        /***/
        Box zombie1 = new Box(0.2f,1.5f,0.2f);
        Geometry zomb1 = new Geometry("Box", zombie1);

        Material mZomb = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mZomb.setColor("Color", new ColorRGBA(0.75f,0f,0f,0f));//R,B,G,Alphas
        zomb1.setMaterial(mZomb);
        zomb1.setLocalTranslation(new Vector3f(1,-1,1));
        charNode.attachChild(zomb1);
        /***/
        Box zombie2 = new Box(0.2f,1.5f,0.2f);
        Geometry zomb2 = new Geometry("Box", zombie2);

        zomb2.setMaterial(mZomb);
        zomb2.setLocalTranslation(new Vector3f(1,-1,2));
        charNode.attachChild(zomb2);
        /***/

        // ust add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);      

        mainNode.attachChild(pivot);
        mainNode.attachChild(charNode);
        mainNode.rotate(0.7f,0f,0f);
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
    
    public Material floorMat(){ //TODO placeHolder
        Material flMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        flMat.setColor("Color", new ColorRGBA(0.25f,0,0.75f,11f));//R,B,G,Alphas
        return flMat;
    }
}

