public class MeleeAttack implements AttackStrategy {

    @Override
    public int attack(Hero attacker, Hero target) {
        int base = 12 + attacker.getStrength();

        boolean crit = Math.random() < 0.12;

        int dmg = crit ? (int)(base * 1.8) : base;

        if (crit) System.out.println(attacker.getName() + " made a CRITICAL sword hit!");
        else System.out.println(attacker.getName() + " attacks in melee range.");

        return dmg;
    }
}
