package gameLogic.skills.hero;

import gameLogic.Creature;
import gameLogic.skills.MeleeSkill;

public class Strike extends MeleeSkill{
    
    public Strike(int energyCost, int power) {
        super("Strike", energyCost, power);
        setAnimationType("Skill1");
        String description =
                "A blunt and basic strike to the enemy.";
        setDescription(description);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        return damageDealt;
    }

}
