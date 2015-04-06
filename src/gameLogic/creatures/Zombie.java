/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.creatures;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import gameLogic.Creature;
import static gameLogic.FakeMain2.findAnimControl;
import static gameLogic.FakeMain2.heroScene;
import gameLogic.skills.hero.Strike;



/**
 *
 * @author User
 */
public class Zombie extends Creature {
    Creature currentTarget = null;
    int stepsToCurrentTarget = Integer.MAX_VALUE;
    
    
    
    
    public Zombie(String name, AssetManager assetManager, AnimEventListener listener) {
        super(name, assetManager, listener);
        this.setAlignment("bad");
        setSkillAsNumber(new Strike(15, 1), 1);
        setPicturePath("Interface/Images/" + name + ".png");
    }
    
    /*public Zombie(String name, Material material, int speed, int power) {
        this(name); // Does nothing with material (TODO)
        setSpeed(speed);
        setPower(power);
        setSkillAsNumber(new Strike(15, power), 1);
    }*/
    
    public boolean isCurrentlyTargetting(Creature creature) {
        return currentTarget != null && currentTarget.equals(creature);
    }
    
    public boolean isCloserToTarget(int stepsToTarget) { 
        return stepsToTarget < stepsToCurrentTarget;
    }
    
    public void setCurrentTarget(Creature target) { 
        currentTarget = target;
    }
    
    public Creature getCurrentTarget() {
        return currentTarget;
    }
    
    public void setStepsToCurrentTarget(int stepsToTarget) {
        stepsToCurrentTarget = stepsToTarget;
    }
    
    public int getStepsToCurrentTarget() {
        return stepsToCurrentTarget;
    }

    public boolean hasEnoughEnergyToAttack() {
        return this.canPayEnergyCostForSkillNumber(1);
    }
}
