/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import gameLogic.skills.Skill;

/**
 *
 * @author User
 */
public class Creature {

    private static final int COST_OF_STEP = 2;
    private int level = 8;
    private String name = "noName";
    private int health = 16;
    private int maxHealth = 16;
    private int energy = 18;
    private int maxEnergy = 30;
    private int speed = 10;
    private int cumulativeTurnSpeed = speed;
    private int power = 8;
    private double defenseRating = 1;
    private Skill[] skills;
    private boolean isGood = false;
    private boolean isImpaired = false;
    private Spatial geometry3D;
    private int turnsAssigned = 0;

    public Creature(String name) {
        this.name = name;
        skills = new Skill[12]; // TODO: eventually set to 4
        //TODO: clean up
        Box box = new Box(0.2f, 1.5f, 0.2f);
        geometry3D = new Geometry(name, box);
        geometry3D.setMaterial(FakeMain2.redZombie);
        FakeMain2.charNode.attachChild(geometry3D);

        //FakeMain2.charNode.attachChild(geo);

        /**
         * Box zombie1 = new Box(0.2f,1.5f,0.2f); Geometry zomb1 = new
         * Geometry("Box", zombie1);
         *
         * Material mZomb = new Material(assetManager,
         * "Common/MatDefs/Misc/Unshaded.j3md"); mZomb.setColor("Color", new
         * ColorRGBA(0.75f,0f,0f,0f));//R,B,G,Alphas zomb1.setMaterial(mZomb);
         * zomb1.setLocalTranslation(new Vector3f(1,-1,1));
         * charNode.attachChild(zomb1);
         *
         */
    }

    public Creature(String name, Material material) {
        //this(name);
        this.name = name;
        skills = new Skill[12]; // TODO: eventually set to 4
        geometry3D = FakeMain2.heroScene; // WIP; node is assigned to Spatial..
        geometry3D.setMaterial(material);
        FakeMain2.charNode.attachChild(geometry3D);
    }

    @Override
    public String toString() {
        return name;
    }

    public void displayCreatureOn3DBoard(int xCoord, int yCoord) {
        geometry3D.setLocalTranslation(new Vector3f(xCoord, -1, yCoord));
        //FakeMain2.charNode.attachChild(geometry3D);
    }

    public void hideCreatureOn3DBoard() {
        geometry3D.removeFromParent();
    }
    
   public MotionEvent generateMotionEventForMovingCreatureOn3DBoard(MotionPath path) {
       //geometry3D.setLocalTranslation(new Vector3f(xDest, -1, yDest));
       //System.out.println("** ANIMATION **");
       MotionEvent motionControl = new MotionEvent(geometry3D, path);
       motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
       motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));//???
       motionControl.setSpeed(5f);
       return motionControl;
       /*
       int maxFrame = 9999999;
       float translationX = (xDest - xInit);// / (float) maxFrame;
       float translationY = (yDest - yInit);// / (float) maxFrame;
       Vector3f v = geometry3D.getLocalTranslation();
      
       for (int i = 0; i < maxFrame; i++) {
           geometry3D.setLocalTranslation(v.x + translationX , v.y , v.z + translationY);
       }*/
   } 

    public void displayStats() {
        String output = name + ": HEALTH: " + health
                + " / ENERGY: " + energy;
        if (isGood) {
            output = "#####################################\n" + output;
            output += "\n#####################################";
        }
        System.out.println(output);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean canPayEnergyCostForSkillNumber(int skillNumber) {
        int energyCost = getEneryCostForSkillNumber(skillNumber);
        return canPayEnergyCostOf(energyCost);
    }

    public int getEneryCostForSkillNumber(int skillNumber) {
        Skill chosenSkill = skills[skillNumber - 1];
        return chosenSkill.getEnergyCost();
    }

    public void consumeEnergyForSkillNumber(int skillNumber) {
        consumeEnergy(getEneryCostForSkillNumber(skillNumber));
    }

    private void consumeEnergy(int energyConsumed) {
        if (canPayEnergyCostOf(energyConsumed)) {
            energy -= energyConsumed;
        } else {    // Debugging?
            System.out.println("Error: not enough energy to perform action");
        }
    }

    public boolean canPayEnergyCostForSteps(int numberOfSteps) {
        int energyCost = getEnergyCostForSteps(numberOfSteps);
        return canPayEnergyCostOf(energyCost);
    }

    public int getEnergyCostForSteps(int numberOfSteps) {
        return numberOfSteps * COST_OF_STEP;
    }

    public void consumeEnergyForSteps(int numberOfSteps) {
        consumeEnergy(getEnergyCostForSteps(numberOfSteps));
    }

    public boolean canPayEnergyCostOf(int energy) {
        return this.energy >= energy;
    }

    public int getEnergy() {
        return energy;
    }

    public int getPower() {
        return power;
    }

    public int maximumStepsAbleToWalk() {
        return (int) (energy / COST_OF_STEP);
    }

    public void receiveDamage(int damage) {
        health -= (int) (damage / defenseRating);
        if (health < 0) {
            health = 0;
        } else if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public void setIsImpaired(boolean isImpaired) {
        this.isImpaired = isImpaired;
    }

    public void isImpaired(boolean isImpaired) {
        this.isImpaired = isImpaired;
    }

    public void becomeImpaired() {
        energy = maxEnergy / 2;
    }

    public void setAlignment(String alignment) {
        if (alignment.equals("bad")) {
            isGood = false;
        } else {
            isGood = true;
        }
    }

    public boolean isAlignedTo(String alignment) {
        boolean matchingAlignment;
        if (alignment.equals("all")) {
            matchingAlignment = true;
        } else {
            matchingAlignment = alignment.equals("good") && isGood
                    || alignment.equals("bad") && !isGood;
        }
        return matchingAlignment;
    }

    public void setSkillAsNumber(Skill skill, int skillNumber) {
        if (1 <= skillNumber && skillNumber <= 12) {
            skills[skillNumber - 1] = skill;
        } else {
            System.err.println("Error: skill added with an out-of-bounds number");
        }
    }

    public Skill prepareSkill(int skillNumber) {
        return skills[skillNumber - 1];
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getCumulativeTurnSpeed() {
        return cumulativeTurnSpeed;
    }

    public void incrementTurnSpeedAfterEndOfTurn() {
        cumulativeTurnSpeed += speed;
    }


    public void setSpeed(int speed) {
        this.speed = speed;
        this.cumulativeTurnSpeed = speed;
    }
    
    public void setCumulativeSpeed(int cumulativeSpeed) {
       cumulativeTurnSpeed = cumulativeSpeed;
    }

    public void incrementTurnsAssigned() {
        turnsAssigned++;
    }

    public int getTurnsAssigned() {
        return turnsAssigned;
    }
    
    public int getOriginalSpeed() {
        return speed;
    }
    
    
}
