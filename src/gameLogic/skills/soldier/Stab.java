package gameLogic.skills.soldier;

import gameLogic.Creature;
import gameLogic.skills.MeleeSkill;

public class Stab extends MeleeSkill{
    
    public Stab(int energyCost, int power) {
        super("Stab", energyCost, power);
        setAnimationType("Skill2");
        String description =
                "Stab an enemy with deadly force.";
        setDescription(description);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        return damageDealt;
    }

}
