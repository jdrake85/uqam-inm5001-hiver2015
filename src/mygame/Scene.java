/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 *
 * @author This PC
 */
public class Scene {

    private AssetManager assetMgr;
    private ArrayList<Spatial> spatials;

    public Scene(AssetManager assetMgr1) {
        assetMgr = assetMgr1;
        spatials = new ArrayList<Spatial>();
    }

    private Geometry makeBox(float x, float y, float z, ColorRGBA col) {
        Box box1 = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", box1);
        geom.setLocalTranslation(new Vector3f(x, y, z));
        Material m1 = new Material(assetMgr,
                "Common/MatDefs/Misc/Unshaded.j3md");
        m1.setColor("Color", col);
        geom.setMaterial(m1);
        return geom;
    }

    public void populate() {

        spatials.add((Spatial) makeBox(-2, -2, -2, ColorRGBA.Blue));
        spatials.add((Spatial) makeBox(0, -2, -2, ColorRGBA.Red));
        spatials.add((Spatial) makeBox(2, -2, -2, ColorRGBA.Blue));
        spatials.add((Spatial) makeBox(-2, -2, 0, ColorRGBA.Red));
        spatials.add((Spatial) makeBox(0, -2, 0, ColorRGBA.Blue));
        spatials.add((Spatial) makeBox(2, -2, 0, ColorRGBA.Red));
        spatials.add((Spatial) makeBox(-2, -2, 2, ColorRGBA.Blue));
        spatials.add((Spatial) makeBox(0, -2, 2, ColorRGBA.Red));
        spatials.add((Spatial) makeBox(2, -2, 2, ColorRGBA.Blue));

    }

    /**
     * @return the node1
     */
    public ArrayList<Spatial> getSpatials() {
        return spatials;
    }
}
