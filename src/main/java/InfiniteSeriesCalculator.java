public class InfiniteSeriesCalculator {

    private final long maxIterations;

    public InfiniteSeriesCalculator(long maxIterations) {
        this.maxIterations = maxIterations;
    }

    public double calculateSeriesValue(long i) {
        return 1.0 / i * Math.sin(i);
    }

    public long getMaxIterations() {
        return maxIterations;
    }
}
