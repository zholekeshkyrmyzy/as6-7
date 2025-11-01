public class ShieldDecorator extends HeroDecorator {
    public ShieldDecorator(Hero hero) {
        super(hero);
    }

    @Override
    public void defend() {
        super.defend();
        decoratedHero.addDefenseBuff(0.3, 2);
        decoratedHero.notifyObservers(decoratedHero.getName() + " gains a magical shield (30% extra damage reduction).");
    }
}
