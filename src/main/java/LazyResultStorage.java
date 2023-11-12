import java.util.concurrent.locks.ReentrantLock;

public class LazyResultStorage {

    private ReentrantLock LOCK = new ReentrantLock();
    private double totalResultInMicros = 0;
    private int progress = 0;
    private int progressUpdateInterval;

    // Приватный конструктор гарантирует, что экземпляр класса создается только один раз
    private LazyResultStorage() {
        System.out.println("LazyResultStorage instance created.");
    }

    // Единственный экземпляр класса для реализации шаблона Singleton
    private static final LazyResultStorage instance = new LazyResultStorage();

    // Метод для получения единственного экземпляра класса
    public static LazyResultStorage getInstance() {
        return instance;
    }

    // Инициализация параметра прогресса
    public void initialize(long maxIterations) {
        int progressInterval = (int) (maxIterations * 0.10);
        this.progressUpdateInterval = progressInterval > 0 ? progressInterval : 1;
    }

    // Добавление результата к сумме. Перевод значения в микроединицы для избежания потери точности
    public void addResult(double value) {
        long valueInMicros = (long) (value * 1_000_000);
        LOCK.lock();
        try {
            totalResultInMicros += valueInMicros;
        } finally {
            LOCK.unlock();
        }
    }

    // Получение общего результата. Конвертация микроединиц обратно в дробное значение
    public double getTotalResult() {
        return totalResultInMicros / 1_000_000.0;
    }

    // Обновление и печать прогресса вычислений
    public void updateProgress() {
        LOCK.lock();
        try {
            progress++;
            if (progress % progressUpdateInterval == 0) {
                Main.printProgress(progress);
            }
        } finally {
            LOCK.unlock();
        }
    }
}
