public class RangedAttack implements AttackStrategy {
    private String weapon;

    public RangedAttack(String weapon) {
        this.weapon = weapon;
    }

    public String getWeapon() { return weapon; }

    @Override
    public int attack(Hero attacker, Hero target) {
        int base;
        switch (weapon.toLowerCase()) {
            case "pistol": base = 20 + attacker.getDexterity()/2; break;
            case "crossbow": base = 18 + attacker.getDexterity()/2; break;
            case "bow": default: base = 15 + attacker.getDexterity()/2; break;
        }

        double critChance = weapon.equalsIgnoreCase("pistol") ? 0.10 : weapon.equalsIgnoreCase("crossbow") ? 0.08 : 0.07;
        boolean crit = Math.random() < critChance;
        int dmg = crit ? (int)(base * 1.6) : base;
        System.out.println(attacker.getName() + " shoots with " + weapon + (crit ? " (CRITICAL)!" : "."));
        return dmg;
    }
}
