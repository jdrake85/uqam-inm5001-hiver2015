/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import static gameLogic.FakeMain2.creatureInCommand;
import static gameLogic.FakeMain2.findAnimControl;
import static gameLogic.FakeMain2.mainTransform;
import gameLogic.pathfinding.CoordPath;
import gameLogic.pathfinding.Coordinates;
import gameLogic.skills.Skill;

/**
 *
 * @author User
 */
public class Creature {

    private static final int COST_OF_STEP = 2;
    private String name = "noName";
    private int maxHealth = 16;
    private int health = maxHealth;
    private int maxEnergy = 30;
    private int energy = maxEnergy;
    private int speed = 10;
    private int cumulativeTurnSpeed = speed;
    private int power = 8;
    private double defenseRating = 1;
    private Skill[] skills;
    private boolean isGood = true;
    private boolean isImpaired = false;
    public Spatial geometry3D;
    private int turnsAssigned = 0;
    public AnimControl creatureControl;
    public AnimChannel creatureChannel;
    private String picturePath = null;

    public Creature(String name, AnimEventListener listener) {
        this.name = name;
        skills = new Skill[12]; // TODO: eventually set to 4
        Box box = new Box(0.2f, 1.5f, 0.2f);
        geometry3D = new Geometry(name, box);
        //geometry3D = (Node) assetManager.loadModel("Hero.scene");
        geometry3D.setMaterial(FakeMain2.redZombie);
        FakeMain2.charNode.attachChild(geometry3D);
        this.addAnimationListener(listener);
    }

    public Creature(String name, AssetManager assetManager, AnimEventListener listener) {
        this.name = name;
        skills = new Skill[12]; // TODO: eventually set to 4
        /*Box box = new Box(0.2f, 1.5f, 0.2f);
         geometry3D = new Geometry(name, box);*/
        geometry3D = (Node) assetManager.loadModel("Zombie.scene");
        geometry3D.setLocalScale(.025f);
        geometry3D.setMaterial(FakeMain2.redZombie);

        creatureControl = findAnimControl(geometry3D);
        creatureChannel = creatureControl.createChannel();
        creatureChannel.setAnim("Idle");

        //skillChannel = creatureControl.createChannel();
        //skillChannel.setLoopMode(LoopMode.DontLoop);

        FakeMain2.charNode.attachChild(geometry3D);
        this.addAnimationListener(listener);
    }

    public Creature(String name, Material material, AnimEventListener listener) {
        //this(name);
        this.name = name;
        skills = new Skill[12]; // TODO: eventually set to 4
        geometry3D = FakeMain2.heroScene; // WIP; node is assigned to Spatial..
        geometry3D.setMaterial(material);
        FakeMain2.charNode.attachChild(geometry3D);
        this.addAnimationListener(listener);
    }

    public Creature(String name, Material material, AssetManager assetManager, AnimEventListener listener) {
        this.name = name;
        skills = new Skill[12]; // TODO: eventually set to 4
        geometry3D = (Node) assetManager.loadModel(name + ".scene");
        geometry3D.setLocalScale(.025f);
        geometry3D.setMaterial(material);

        creatureControl = findAnimControl(geometry3D);
        creatureChannel = creatureControl.createChannel();
        creatureChannel.setAnim("Idle");

        //skillChannel = creatureControl.createChannel();
        //skillChannel.setLoopMode(LoopMode.DontLoop);

        FakeMain2.charNode.attachChild(geometry3D);
        this.addAnimationListener(listener);
    }

    public Spatial getSpatial() {
        return geometry3D;
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

    public MotionEvent generateMotionEventForMovingCreatureOn3DBoard(MotionPath path, int stepCount) {
        MotionEvent motionControl = new MotionEvent(geometry3D, path);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));//???
        motionControl.setSpeed(20f / stepCount);
        return motionControl;
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

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = energy = maxEnergy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setPower(int power) {
        this.power = power;
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

    public boolean isGood() {
        return isGood;
    }

    public void initializeTurnEnergy() {
        if (isImpaired) {
            energy = maxEnergy / 2;
        } else {
            energy = maxEnergy;
        }
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = health = maxHealth;
    }

    public void animateMove() {
        try {
            // create a channel and start the wobble animation
            creatureChannel.setAnim("Walk");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void animateSkill(String animationType) {
        try {
            //System.out.println(animationType);
            // create a channel and start the wobble animation
            creatureChannel.setAnim(animationType);
            creatureChannel.setLoopMode(LoopMode.DontLoop);
            FakeMain2.movingCreature = true;

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    void animateIdle() {
        try {
            // create a channel and start the wobble animation
            creatureChannel.setAnim("Idle");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void addAnimationListener(AnimEventListener main) {
        creatureControl.addListener(main);
    }

    void rotateModelTowardsCoordinates(Coordinates originatingCoords, Coordinates targetCoords) {
        Coordinates closestAdjacentCoords = originatingCoords.getAdjacentCoordinatesNearestTo(targetCoords);
        String facingDirection = originatingCoords.getCardinalDirectionTowards(closestAdjacentCoords);
        turnToFaceDirection(facingDirection);
    }

    private void turnToFaceDirection(String direction) {
        Quaternion facingRotation = new Quaternion();
        if (direction.equals("+x")) {
            geometry3D.setLocalRotation(facingRotation.fromAngleAxis(0f, Vector3f.UNIT_Y));  
        } else if (direction.equals("-x")) {
            geometry3D.setLocalRotation(facingRotation.fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y));  
        } else if (direction.equals("+y")) {
            geometry3D.setLocalRotation(facingRotation.fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));  
        } else if (direction.equals("-y")) {
            geometry3D.setLocalRotation(facingRotation.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y));  
        }
    }

    public Skill[] getSkills() {
        return skills;
    }
}
