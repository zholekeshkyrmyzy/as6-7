public abstract class HeroDecorator extends Hero {
    protected Hero decoratedHero;

    public HeroDecorator(Hero hero) {
        super(hero.getName(), hero.getMaxHp(), hero.getMaxMana(),
                new MeleeAttack(), hero.getStrength(), hero.getDexterity(), hero.getIntelligence());
        this.decoratedHero = hero;
    }

    @Override
    public void attack(Hero target) {
        decoratedHero.attack(target);
    }

    @Override
    public void defend() {
        decoratedHero.defend();
    }

    @Override
    public void heal(int amount) {
        decoratedHero.heal(amount);
    }

    @Override
    public void notifyObservers(String msg) {
        decoratedHero.notifyObservers(msg);
    }

    @Override
    public boolean isAlive() {
        return decoratedHero.isAlive();
    }
}
