package gameLogic.skills.nurse;

import gameLogic.Creature;
import gameLogic.skills.MeleeSkill;

public class Heal extends MeleeSkill{
    
    public Heal(int energyCost, int power) {
        super("Heal", energyCost, power * -1);
        setAnimationType("Skill1");
        setTargetsZombies(false);
        String description =
                "Restore health to yourself or to an adjacent ally.";
        setDescription(description);
    }

    @Override
    public int performOn(Creature creature) {
        int healingDealt = power;
        creature.receiveDamage(healingDealt);
        return healingDealt;
    }
}
