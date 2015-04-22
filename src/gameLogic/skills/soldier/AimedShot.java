package gameLogic.skills.soldier;

import gameLogic.Creature;
import gameLogic.skills.RangedSkill;
import java.util.Random;

public class AimedShot extends RangedSkill{
    
    public AimedShot(int energyCost, int power) {
        super("Aimed Shot", energyCost, power, 6);
        setAnimationType("Skill1");
        String description =
                "Fire a bullet at an enemy; chances to miss increase along with the distance of the target.";
        setDescription(description);
    }

    @Override
    public int performOn(Creature creature) {
        int damageDealt = 0;
        Random generator = new Random();
        int stepCount = originatingCoords.sumOfXYComponentDistancesTo(targetCoords);
        int nextNumber = generator.nextInt(range) + 2;
        if (nextNumber >= stepCount) {
            damageDealt = power;
            creature.receiveDamage(damageDealt);
        }
        return damageDealt;
    }
}
