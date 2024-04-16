import java.util.Arrays;

// Clase para representar el conjunto de datos
class Dataset {
    private double[] xValues;
    private double[] yValues;

    public Dataset(double[] xValues, double[] yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
    }

    public double[] getXValues() {
        return xValues;
    }

    public double[] getYValues() {
        return yValues;
    }
}

// Clase para realizar el análisis de regresión cuadrática
class QuadraticRegression {
    private double a, b, c;

    public QuadraticRegression(Dataset dataset) {
        double[] x = dataset.getXValues();
        double[] y = dataset.getYValues();

        int n = x.length;

        double sumX = Arrays.stream(x).sum();
        double sumX2 = Arrays.stream(x).map(v -> v * v).sum();
        double sumX3 = Arrays.stream(x).map(v -> v * v * v).sum();
        double sumX4 = Arrays.stream(x).map(v -> v * v * v * v).sum();
        double sumY = Arrays.stream(y).sum();
        double sumXY = 0;
        double sumX2Y = 0;

        for (int i = 0; i < n; i++) {
            sumXY += x[i] * y[i];
            sumX2Y += x[i] * x[i] * y[i];
        }

        double[][] coefficients = {
                {sumX4, sumX3, sumX2},
                {sumX3, sumX2, sumX},
                {sumX2, sumX, n}
        };

        double[] constants = {sumX2Y, sumXY, sumY};

        // Resolver sistema de ecuaciones
        GaussianElimination gaussianElimination = new GaussianElimination(coefficients, constants);
        double[] results = gaussianElimination.solve();

        this.a = results[0];
        this.b = results[1];
        this.c = results[2];
    }

    public double[] getCoefficients() {
        return new double[]{a, b, c};
    }

    public double predict(double x) {
        return a * x * x + b * x + c;
    }
}

// Clase para resolver sistemas de ecuaciones
class GaussianElimination {
    private final double[][] coefficients;
    private final double[] constants;
    private final int n;

    public GaussianElimination(double[][] coefficients, double[] constants) {
        this.coefficients = coefficients;
        this.constants = constants;
        this.n = constants.length;
    }

    public double[] solve() {
        // Eliminación hacia adelante
        for (int pivot = 0; pivot < n - 1; pivot++) {
            for (int row = pivot + 1; row < n; row++) {
                double factor = coefficients[row][pivot] / coefficients[pivot][pivot];
                for (int col = pivot; col < n; col++) {
                    coefficients[row][col] -= factor * coefficients[pivot][col];
                }
                constants[row] -= factor * constants[pivot];
            }
        }

        // Sustitución hacia atrás
        double[] solution = new double[n];
        for (int row = n - 1; row >= 0; row--) {
            double sum = 0;
            for (int col = row + 1; col < n; col++) {
                sum += coefficients[row][col] * solution[col];
            }
            solution[row] = (constants[row] - sum) / coefficients[row][row];
        }

        return solution;
    }
}

