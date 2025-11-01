import java.util.Random;

public class HeroFactory {
    private static Random random = new Random();

    public static Hero createHero(String type, String name) {
        switch (type.toLowerCase()) {
            case "warrior":
                return new Warrior(name);
            case "mage":
                return new Mage(name);
            case "archer":
                return new Archer(name);
            default:
                throw new IllegalArgumentException("Unknown hero type: " + type);
        }
    }

    public static Hero createRandomHero(String name) {
        int choice = random.nextInt(3); // 0..2
        switch (choice) {
            case 0:
                return new Warrior(name);
            case 1:
                return new Mage(name);
            default:
                return new Archer(name);
        }
    }
}
