package ua.ivanka.oop.lab3.dtw;

import java.util.List;

public final class Dtw {
    private Dtw() {}

    /**
     * DTW distance for 3D vectors.
     *
     * @param a first sequence of float[3]
     * @param b second sequence of float[3]
     * @param window Sakoe–Chiba band radius (use <=0 for no band)
     * @return normalized DTW distance
     */
    public static double distance3d(List<float[]> a, List<float[]> b, int window) {
        if (a == null || b == null || a.isEmpty() || b.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }

        int n = a.size();
        int m = b.size();
        int w = window <= 0 ? Math.max(n, m) : Math.max(window, Math.abs(n - m));

        double INF = Double.POSITIVE_INFINITY;
        double[][] dp = new double[n + 1][m + 1];
        int[][] steps = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = INF;
                steps[i][j] = Integer.MAX_VALUE;
            }
        }
        dp[0][0] = 0.0;
        steps[0][0] = 0;

        for (int i = 1; i <= n; i++) {
            int jStart = Math.max(1, i - w);
            int jEnd = Math.min(m, i + w);
            for (int j = jStart; j <= jEnd; j++) {
                double cost = euclid3(a.get(i - 1), b.get(j - 1));

                double bestPrev = dp[i - 1][j];
                int bestSteps = steps[i - 1][j];

                if (dp[i][j - 1] < bestPrev) {
                    bestPrev = dp[i][j - 1];
                    bestSteps = steps[i][j - 1];
                }
                if (dp[i - 1][j - 1] < bestPrev) {
                    bestPrev = dp[i - 1][j - 1];
                    bestSteps = steps[i - 1][j - 1];
                }

                if (bestPrev != INF) {
                    dp[i][j] = cost + bestPrev;
                    steps[i][j] = bestSteps + 1;
                }
            }
        }

        double total = dp[n][m];
        int pathLen = steps[n][m];
        if (total == INF || pathLen <= 0) {
            return INF;
        }
        return total / pathLen;
    }

    private static double euclid3(float[] x, float[] y) {
        double dx = x[0] - y[0];
        double dy = x[1] - y[1];
        double dz = x[2] - y[2];
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
