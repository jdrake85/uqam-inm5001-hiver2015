package gameLogic.skills.nurse;

import gameLogic.Creature;
import gameLogic.skills.MeleeSkill;

public class Push extends MeleeSkill{
    
    public Push(int energyCost, int power) {
        super("Push", energyCost, power, true, false);
        setAnimationType("Skill2");
        String description =
                "Push the enemy up to two tiles away to escape from danger.";
        setDescription(description);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        return damageDealt;
    }

}
