public class MagicAttack implements AttackStrategy {
    private int manaCost;

    public MagicAttack() { this.manaCost = 12; }

    @Override
    public int attack(Hero attacker, Hero target) {
        if (!attacker.useMana(manaCost)) {
            System.out.println(attacker.getName() + " tries to cast a spell, but doesn't have enough mana!");
            return 0;
        }
        int base = 18 + attacker.getIntelligence() / 2;
        boolean crit = Math.random() < 0.11;
        int dmg = crit ? base + 12 : base;
        System.out.println(attacker.getName() + " casts a spell" + (crit ? " (enhanced)!" : "."));
        return dmg;
    }
}
