package gameLogic.skills.soldier;

import gameLogic.Creature;
import gameLogic.skills.MeleeSkill;
import java.util.Random;

public class CutThroat extends MeleeSkill{
    
    public CutThroat(int energyCost, int power) {
        super("Cut Throat", energyCost, power, false, true);
        setAnimationType("Skill2");
        String description =
                "Powerful attack that has a chance of causing IMPAIR to the enemy.";
        setDescription(description);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = power;
        creature.receiveDamage(damageDealt);
        Random generator = new Random();
        int numberRange = 10;
        int numberLimit = (int) (numberRange * 0.4);
        if (generator.nextInt(numberRange) > numberLimit) {
            creature.becomeImpaired();
        }
        return damageDealt;
    }

}
