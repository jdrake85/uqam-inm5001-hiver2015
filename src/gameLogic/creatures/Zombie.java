/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic.creatures;

import com.jme3.animation.AnimEventListener;
import com.jme3.asset.AssetManager;
import gameLogic.Creature;
import gameLogic.skills.Skill;
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
        
        Skill zombieStrike = new Strike(15, 1);
        zombieStrike.setTargetsZombies(false);
        setSkillAsNumber(zombieStrike, 1);
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
