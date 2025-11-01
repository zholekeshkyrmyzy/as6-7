public class FireAuraDecorator extends HeroDecorator {
    public FireAuraDecorator(Hero hero) {
        super(hero);
    }

    @Override
    public void attack(Hero target) {
        super.attack(target);
        int extra = 8;
        target.receiveDamage(extra, decoratedHero);
        decoratedHero.notifyObservers(decoratedHero.getName() + " burns the enemy with fire aura (" + extra + " extra dmg).");
    }
}
