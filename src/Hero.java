import java.util.*;

public abstract class Hero {
    private String name;
    private int maxHp;
    private int hp;
    private int maxMana;
    private int mana;

    private AttackStrategy strategy;
    private List<HeroObserver> observers = new ArrayList<>();
    private Map<String, Integer> inventory = new LinkedHashMap<>();

    private int strength;
    private int dexterity;
    private int intelligence;

    private int tempDamage = 0;
    private int tempDamageTurns = 0;
    private double tempDefensePercent = 0.0;
    private int tempDefenseTurns = 0;

    public Hero(String name, int maxHp, int maxMana, AttackStrategy defaultStrategy,
                int strength, int dexterity, int intelligence) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMana = maxMana;
        this.mana = maxMana;
        this.strategy = defaultStrategy;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public int getStrength() { return strength; }
    public int getDexterity() { return dexterity; }
    public int getIntelligence() { return intelligence; }


    public void registerObserver(HeroObserver o) {
        if (!observers.contains(o)) observers.add(o);
    }

    public void unregisterObserver(HeroObserver o) {
        observers.remove(o);
    }

    public void notifyObservers(String msg) {
        for (HeroObserver o : observers) o.onNotify(msg);
    }

    public Map<String, Integer> getInventory() {
        return Collections.unmodifiableMap(inventory);
    }

    public void addToInventory(String itemName, int count) {
        inventory.put(itemName, inventory.getOrDefault(itemName, 0) + count);
    }

    public boolean consumeFromInventory(String itemName) {
        Integer cnt = inventory.get(itemName);
        if (cnt == null || cnt <= 0) return false;
        inventory.put(itemName, cnt - 1);
        return true;
    }

    public void setStrategy(AttackStrategy s) {
        this.strategy = s;
        notifyObservers(name + " switched to " + s.getClass().getSimpleName()
                + (s instanceof RangedAttack ? " (" + ((RangedAttack) s).getWeapon() + ")" : "") + " attack mode.");
    }

    public void attack(Hero target) {
        if (!isAlive()) {
            notifyObservers(name + " cannot attack — already dead.");
            return;
        }
        int raw = strategy.attack(this, target);
        int total = raw + tempDamage;
        target.receiveDamage(total, this);
        notifyObservers(name + " dealt " + total + " damage to " + target.getName() + ".");
        decrementBuffsAfterAction();
    }

    public void receiveDamage(int dmg, Hero from) {
        int finalDmg = dmg;
        if (tempDefenseTurns > 0 && tempDefensePercent > 0) {
            finalDmg = (int) Math.ceil(dmg * (1.0 - tempDefensePercent));
            notifyObservers(name + " took reduced damage (" + finalDmg + ").");
        }
        hp -= finalDmg;
        if (hp < 0) hp = 0;
        notifyObservers(name + " took " + finalDmg + " damage. HP: " + hp + "/" + maxHp);
        if (hp == 0) notifyObservers(name + " was defeated by " + from.getName() + "!");
    }

    public void defend() {
        addDefenseBuff(0.5, 1);
        notifyObservers(name + " is defending (next damage reduced by 50%).");
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
        notifyObservers(name + " healed " + amount + " HP. Current HP: " + hp + "/" + maxHp);
    }

    public void restoreMana(int amount) {
        mana = Math.min(maxMana, mana + amount);
        notifyObservers(name + " restored " + amount + " mana. Current mana: " + mana + "/" + maxMana);
    }

    public boolean useMana(int amount) {
        if (mana >= amount) {
            mana -= amount;
            return true;
        }
        return false;
    }

    public boolean isAlive() { return hp > 0; }

    public void addTempDamage(int extra, int turns) {
        this.tempDamage += extra;
        this.tempDamageTurns = Math.max(this.tempDamageTurns, turns);
    }

    public void addDefenseBuff(double percent, int turns) {
        this.tempDefensePercent = Math.max(this.tempDefensePercent, percent);
        this.tempDefenseTurns = Math.max(this.tempDefenseTurns, turns);
    }

    private void decrementBuffsAfterAction() {
        if (tempDamageTurns > 0) {
            tempDamageTurns--;
            if (tempDamageTurns == 0) tempDamage = 0;
        }
        if (tempDefenseTurns > 0) {
            tempDefenseTurns--;
            if (tempDefenseTurns == 0) tempDefensePercent = 0.0;
        }
    }

    public void showStatusShort() {
        System.out.println(name + " | HP: " + hp + "/" + maxHp + " | Mana: " + mana + "/" + maxMana);
    }

    @Override
    public String toString() {
        return name + " (HP:" + hp + "/" + maxHp + ", Mana:" + mana + "/" + maxMana + ")";
    }
}
