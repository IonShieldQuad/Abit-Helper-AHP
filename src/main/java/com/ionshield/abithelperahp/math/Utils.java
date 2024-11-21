package com.ionshield.abithelperahp.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Utils {

    public static Matrix makeMatrixFromPairs(List<PreferencePair> pairs) {
        int maxIndex = Math.max(pairs.stream().mapToInt(p -> p.a).max().orElse(0), pairs.stream().mapToInt(p -> p.b).max().orElse(0));

        List<List<Double>> data = new ArrayList<>();
        for (int i = 0; i <= maxIndex; i++) {
            List<Double> l = new ArrayList<>();
            for (int j = 0; j <= maxIndex; j++) {
                l.add(i == j ? 1.0 : 0.0);
            }
            data.add(l);
        }

        pairs.forEach(pair -> {
            if (pair.a == pair.b) return;
            data.get(pair.a).set(pair.b, pair.value);
            data.get(pair.b).set(pair.a, 1 / pair.value);
        });
        return Matrix.makeEmptyMatrix(maxIndex + 1).fill(data).transpose();
    }

    public static List<Double> calculateWeights(Matrix m) {
        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < m.sizeY(); i++) {
            double mul = 1;
            for (int j = 0; j < m.sizeX(); j++) {
                mul *= m.get(j, i);
            }
            mul = Math.pow(mul, 1.0 / m.sizeX());
            weights.add(mul);
        }
        double sum = weights.stream().mapToDouble(x -> x).sum();
        for (int i = 0; i < weights.size(); i++) {
            weights.set(i, weights.get(i) / sum);
        }
        return weights;
    }

    public static double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

}
