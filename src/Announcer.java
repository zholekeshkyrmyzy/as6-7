public class Announcer implements HeroObserver {
    @Override
    public void onNotify(String message) {
        System.out.println("[ANNOUNCER] " + message);
    }
}
