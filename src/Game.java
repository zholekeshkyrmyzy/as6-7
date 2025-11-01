import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Scanner sc = new Scanner(System.in);
    private final Announcer announcer = new Announcer();
    private final Logger logger = new Logger();
    private final Random random = new Random();

    public void start() {
        System.out.println("=== WELCOME TO HERO BATTLE GAME ===");
        System.out.println("1. Start Game");
        System.out.println("2. Exit");
        System.out.print("Choose an option: ");

        int choice = readIntWithDefault(1);
        if (choice != 1) {
            System.out.println("Exiting game. Goodbye!");
            return;
        }

        System.out.println("Enter your hero type (warrior / archer / mage): ");
        String type = sc.nextLine().trim();
        System.out.println("Enter your hero name: ");
        String name = sc.nextLine().trim();

        Hero player = HeroFactory.createHero(type, name);
        Hero bot = HeroFactory.createRandomHero("Bot");

        player.registerObserver(announcer);
        bot.registerObserver(announcer);
        player.registerObserver(logger);
        bot.registerObserver(logger);

        // каждому свой стартовый предмет
        player.addToInventory("Health Potion", 1);
        bot.addToInventory("Health Potion", 1);

        System.out.println("\nBattle starts: " + player.getName() + " vs " + bot.getName());

        int round = 1;
        while (player.isAlive() && bot.isAlive()) {
            System.out.println("\n=== Round " + round + " ===");
            player.showStatusShort();
            bot.showStatusShort();
            showInventory(player);

            System.out.println("\nChoose action: 1-Attack  2-Defend  3-Use Item  4-Shop  5-Change Strategy  6-Pass");
            int action = readIntWithDefault(1);

            switch (action) {
                case 1 -> player.attack(bot);
                case 2 -> player.defend();
                case 3 -> handleUseItem(player);
                case 4 -> handleShop(player);
                case 5 -> handleChangeStrategy(player);
                case 6 -> System.out.println(player.getName() + " skips the turn.");
                default -> System.out.println("Invalid choice.");
            }

            if (!bot.isAlive() || !player.isAlive()) break;

            // === Ход бота ===
            System.out.println("\n--- Bot’s turn ---");
            botTurn(bot, player);

            // восстановление маны
            player.restoreMana(5);
            bot.restoreMana(5);

            round++;
        }

        System.out.println("\n=== Battle Ended ===");
        if (player.isAlive()) System.out.println(player.getName() + " wins!");
        else if (bot.isAlive()) System.out.println(bot.getName() + " wins!");
        else System.out.println("It's a draw!");
    }

    private void botTurn(Hero bot, Hero player) {
        if (!bot.isAlive()) return;

        int botAction = random.nextInt(4) + 1; // 1-4
        switch (botAction) {
            case 1 -> bot.attack(player);
            case 2 -> bot.defend();
            case 3 -> {
                // бот использует предмет, если есть
                if (!bot.getInventory().isEmpty()) {
                    String firstItem = bot.getInventory().keySet().iterator().next();
                    Item item = itemFromName(firstItem);
                    if (item != null) {
                        bot.consumeFromInventory(firstItem);
                        String event = item.use(bot);
                        bot.notifyObservers(event);
                    } else bot.attack(player);
                } else bot.attack(player);
            }
            case 4 -> System.out.println("Bot skips the turn.");
        }
    }

    private void showInventory(Hero h) {
        System.out.println(h.getName() + " inventory: " + h.getInventory());
    }

    private int readIntWithDefault(int def) {
        try {
            String s = sc.nextLine().trim();
            if (s.isEmpty()) return def;
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    private void handleUseItem(Hero actor) {
        Map<String, Integer> inv = actor.getInventory();
        if (inv.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("Choose an item to use or type 'pass' to skip: " + inv);
        String key = sc.nextLine().trim();
        if (key.equalsIgnoreCase("pass")) {
            System.out.println(actor.getName() + " skipped using an item.");
            return;
        }
        if (!inv.containsKey(key) || inv.get(key) <= 0) {
            System.out.println("Invalid item name.");
            return;
        }
        Item item = itemFromName(key);
        if (item == null) {
            System.out.println("Cannot use this item right now.");
            return;
        }
        actor.consumeFromInventory(key);
        String event = item.use(actor);
        if (event != null) actor.notifyObservers(event);
    }

    private Item itemFromName(String key) {
        String lower = key.toLowerCase();
        if (lower.contains("health")) return new HealthPotion(30);
        if (lower.contains("damage")) return new DamageBuffItem(8, 3);
        if (lower.contains("defense")) return new DefenseBuffItem(0.25, 3);
        if (lower.contains("pistol")) return new WeaponItem("Pistol");
        if (lower.contains("bow")) return new WeaponItem("Bow");
        if (lower.contains("crossbow")) return new WeaponItem("Crossbow");
        return null;
    }

    private void handleShop(Hero buyer) {
        System.out.println("Shop:");
        System.out.println("1. Health Potion (+30 HP)");
        System.out.println("2. Damage Buff (+8 dmg for 3 turns)");
        System.out.println("3. Defense Buff (-25% dmg for 3 turns)");
        System.out.println("4. Pistol (weapon)");
        System.out.println("5. Bow (weapon)");
        System.out.println("6. Crossbow (weapon)");
        System.out.print("Enter item number: ");

        int choice = readIntWithDefault(-1);
        String itemKey = switch (choice) {
            case 1 -> "Health Potion";
            case 2 -> "Damage Buff";
            case 3 -> "Defense Buff";
            case 4 -> "Pistol";
            case 5 -> "Bow";
            case 6 -> "Crossbow";
            default -> null;
        };

        if (itemKey == null) {
            System.out.println("Leaving the shop.");
            return;
        }

        buyer.addToInventory(itemKey, 1);
        buyer.notifyObservers(buyer.getName() + " bought " + itemKey + ".");
    }

    private void handleChangeStrategy(Hero actor) {
        System.out.println("Choose new attack type: 1-Melee  2-Ranged  3-Magic");
        int s = readIntWithDefault(2);
        switch (s) {
            case 1 -> actor.setStrategy(new MeleeAttack());
            case 2 -> {
                System.out.println("Choose ranged weapon: 1-Pistol  2-Bow  3-Crossbow");
                int w = readIntWithDefault(2);
                String weapon = (w == 1) ? "Pistol" : (w == 3) ? "Crossbow" : "Bow";
                actor.setStrategy(new RangedAttack(weapon));
            }
            case 3 -> actor.setStrategy(new MagicAttack());
            default -> System.out.println("Invalid input. Strategy not changed.");
        }
    }
}
