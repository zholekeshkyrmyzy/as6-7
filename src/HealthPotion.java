public class HealthPotion implements Item {
    private int healAmount;

    public HealthPotion(int healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public String getName() {
        return "Health Potion (+" + healAmount + " HP)";
    }

    @Override
    public String use(Hero hero) {
        hero.heal(healAmount);
        return hero.getName() + " used " + getName();
    }
}
