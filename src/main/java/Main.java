import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    // Константы для количества задач, потоков и максимальных итераций
    public static final int COUNT_TASKS = 4;
    public static final int COUNT_THREADS = 4; // количество потоков должно соответствовать количеству задач
    public static final long MAX_ITERATIONS = 100000000;

    // Создаем экземпляр калькулятора для вычислений
    private static InfiniteSeriesCalculator seriesCalculator = new InfiniteSeriesCalculator(MAX_ITERATIONS);

    public static void main(String[] args) {
        // Создаем пул потоков
        ExecutorService service = Executors.newFixedThreadPool(COUNT_THREADS);
        // Получаем экземпляр хранилища для результатов
        LazyResultStorage storage = LazyResultStorage.getInstance();
        // Инициализируем хранилище
        storage.initialize(MAX_ITERATIONS);

        // Засекаем время старта выполнения
        long startMillis = System.currentTimeMillis();

        for (int i = 0; i < COUNT_TASKS; i++) {
            service.execute(() -> {
                // Разделяем итерации между потоками
                long taskIterations = seriesCalculator.getMaxIterations() / COUNT_TASKS;
                long startIter = 1;
                long endIter = taskIterations;

                // Вычисляем и сохраняем результаты для каждой итерации
                for (long j = startIter; j <= endIter; j++) {
                    double result = seriesCalculator.calculateSeriesValue(j);
                    storage.addResult(result);
                    storage.updateProgress(); // Обновляем прогресс
                }
            });
        }

        // Завершаем все потоки после выполнения задач
        service.shutdown();
        try {
            // Ожидаем завершения всех потоков или прерываем их после 1 часа
            service.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        // Вычисляем итоговую сумму и время выполнения
        double totalSum = storage.getTotalResult();
        long endMillis = System.currentTimeMillis();

        // Выводим результаты
        System.out.println("Total sum: " + totalSum);
        System.out.println("Time taken: " + ((endMillis - startMillis) / 1000.0) + " seconds");
    }

    // Метод для вывода прогресса выполнения задачи
    public static void printProgress(long completedIterations) {
        double progressPercentage = (double) completedIterations / MAX_ITERATIONS * 100.0;
        System.out.format("Progress: %.3f%%%n", progressPercentage);
    }
}





//public class Main {
//
//    public static final int COUNT_TASKS = 4;
//    public static final int COUNT_THREADS = 4;
//    public static final long MAX_ITERATIONS = 100000000;
//    private static InfiniteSeriesCalculator seriesCalculator = new InfiniteSeriesCalculator(MAX_ITERATIONS);
//    private static ResultStorage storage = new ResultStorage(MAX_ITERATIONS);
//
//    public static void main(String[] args) {
//        ExecutorService service = Executors.newFixedThreadPool(COUNT_THREADS);
//
//        long startMillis = System.currentTimeMillis();
//
//        for (int i = 0; i < COUNT_TASKS; i++) {
//            service.execute(() -> {
//                long taskIterations = seriesCalculator.getMaxIterations() / COUNT_TASKS;
//                long startIter = 1;
//                long endIter = taskIterations;
//
//                for (long j = startIter; j <= endIter; j++) {
//                    double result = seriesCalculator.calculateSeriesValue(j);
//                    storage.addResult(result);
//                    storage.updateProgress();
//                }
//            });
//        }
//
//        service.shutdown();
//        try {
//            service.awaitTermination(1, TimeUnit.HOURS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        double totalSum = storage.getTotalResult();
//        long endMillis = System.currentTimeMillis();
//
//        System.out.println("Total sum: " + totalSum);
//        System.out.println("Time taken: " + ((endMillis - startMillis) / 1000.0) + " seconds");
//    }
//}


