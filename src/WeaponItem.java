public class WeaponItem implements Item {
    private String weaponName;

    public WeaponItem(String weaponName) {
        this.weaponName = weaponName;
    }

    @Override
    public String getName() {
        return "Weapon: " + weaponName;
    }

    @Override
    public String use(Hero hero) {
        hero.setStrategy(new RangedAttack(weaponName));
        return hero.getName() + " equipped a " + weaponName + " (now using ranged attack: " + weaponName + ")";
    }
}
