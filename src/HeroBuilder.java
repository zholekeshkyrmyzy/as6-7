public class HeroBuilder {
    private String name;
    private int hp = 100;
    private int mana = 50;
    private int strength = 5;
    private int dexterity = 5;
    private int intelligence = 5;
    private AttackStrategy strategy = new MeleeAttack();

    public HeroBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public HeroBuilder setHp(int hp) {
        this.hp = hp;
        return this;
    }

    public HeroBuilder setMana(int mana) {
        this.mana = mana;
        return this;
    }

    public HeroBuilder setStrength(int strength) {
        this.strength = strength;
        return this;
    }

    public HeroBuilder setDexterity(int dexterity) {
        this.dexterity = dexterity;
        return this;
    }

    public HeroBuilder setIntelligence(int intelligence) {
        this.intelligence = intelligence;
        return this;
    }

    public HeroBuilder setStrategy(AttackStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public Hero build() {
        return new CustomHero(name, hp, mana, strategy, strength, dexterity, intelligence);
    }
}
