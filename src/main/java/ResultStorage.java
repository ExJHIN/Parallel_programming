import java.util.concurrent.atomic.DoubleAdder;

public class ResultStorage {

    // DoubleAdder - потокобезопасный класс для сложения чисел с плавающей точкой.
    // Используется для хранения общей суммы.
    private final DoubleAdder totalSum = new DoubleAdder();

    // Максимальное количество итераций (задач), которое будет выполнено.
    private final long maxIterations;

    // Количество итераций, после которого будет выводиться прогресс.
    private long progressCheck;

    // Текущее количество выполненных итераций.
    private long currentProgress;

    // Конструктор класса, принимающий максимальное количество итераций.
    public ResultStorage(long maxIterations) {
        this.maxIterations = maxIterations;

        // Вычисляем, как часто нужно выводить прогресс. В данном случае - каждые 10% от maxIterations.
        this.progressCheck = maxIterations / 10;
        this.currentProgress = 0;
    }

    // Метод для добавления результата (значения) к общей сумме.
    public void addResult(double value) {
        totalSum.add(value);
    }

    // Метод для получения текущей общей суммы.
    public double getTotalResult() {
        return totalSum.doubleValue();
    }

    // Метод для обновления прогресса выполнения задач.
    // Если выполнено определенное количество итераций (кратное progressCheck),
    // то выводится текущий прогресс выполнения.
    // Метод синхронизирован, чтобы предотвратить гонки данных в многопоточной среде.
    public synchronized void updateProgress() {
        currentProgress++;
        if (currentProgress % progressCheck == 0) {
            printProgress(currentProgress);
        }
    }

    // Приватный метод для вывода прогресса в консоль.
    private void printProgress(long completedIterations) {
        double progressPercentage = (double) completedIterations / maxIterations * 100.0;
        System.out.format("Progress: %.3f%%%n", progressPercentage);
    }
}
