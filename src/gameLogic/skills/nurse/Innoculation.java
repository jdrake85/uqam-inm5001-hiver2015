package gameLogic.skills.nurse;

import gameLogic.Creature;
import gameLogic.skills.RangedSkill;

public class Innoculation extends RangedSkill{
    
    public Innoculation(int energyCost, int power) {
        super("Innoculation", energyCost, power, 4);
        setAnimationType("Skill1");
        String description =
                "Attack the undead at a distance with this mixture of antibiotics.";
        setDescription(description);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        return damageDealt;
    }

}