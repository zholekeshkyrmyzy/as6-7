public class DamageBuffItem implements Item {
    private int extraDamage;
    private int turns;

    public DamageBuffItem(int extraDamage, int turns) {
        this.extraDamage = extraDamage;
        this.turns = turns;
    }

    @Override
    public String getName() {
        return "Damage Buff (+" + extraDamage + " for " + turns + " turns)";
    }

    @Override
    public String use(Hero hero) {
        hero.addTempDamage(extraDamage, turns);
        return hero.getName() + " gained +" + extraDamage + " damage for " + turns + " turns";
    }
}
