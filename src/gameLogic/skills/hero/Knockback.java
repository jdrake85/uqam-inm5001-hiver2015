package gameLogic.skills.hero;

import gameLogic.Creature;
import gameLogic.skills.MeleeSkill;

public class Knockback extends MeleeSkill{
    
    public Knockback(int energyCost, int power) {
        super("Knockback", energyCost, power, true, true);
        setAnimationType("Skill1");
        String description =
                "Punishing blow which knocks back the enemy up to two tiles.";
        setDescription(description);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        creature.becomeImpaired();
        return damageDealt;
    }

}
