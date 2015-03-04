/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package level.board;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author Tobie
 */
public class MyMaterial {
    
    private AssetManager assetManager;
    private Material myMaterial;
    
    
    public MyMaterial(AssetManager assetManager){
        this.assetManager = assetManager;       
    }
    
    public void setGreenTileMat(){
        myMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        myMaterial.setColor("Color", new ColorRGBA(.1f,.75f,.1f,0.5f));//R,B,G,Alphas
        myMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    }
    
    public void setGreyTileMat(){
        myMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        myMaterial.setColor("Color", new ColorRGBA(.1f,.1f,.1f,1f));//R,B,G,Alphas
        myMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    }
    
    public void setFloorMat(){ //TODO placeHolder
        myMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        myMaterial.setColor("Color", new ColorRGBA(0.25f,0,0.75f,11f));//R,B,G,Alphas
    }
    
    public Material getMat(){
        return myMaterial;
    }
    
}
