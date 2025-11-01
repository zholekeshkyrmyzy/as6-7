public class DefenseBuffItem implements Item {
    private double reducePercent;
    private int turns;

    public DefenseBuffItem(double reducePercent, int turns) {
        this.reducePercent = reducePercent;
        this.turns = turns;
    }

    @Override
    public String getName() {
        return "Defense Buff (-" + (int)(reducePercent * 100) + "% damage for " + turns + " turns)";
    }

    @Override
    public String use(Hero hero) {
        hero.addDefenseBuff(reducePercent, turns);
        return hero.getName() + " gained defense buff: -" + (int)(reducePercent * 100)
                + "% damage for " + turns + " turns";
    }
}
