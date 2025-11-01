public class Logger implements HeroObserver {
    @Override
    public void onNotify(String message) {
        System.out.println("[LOG] " + message);
    }
}
