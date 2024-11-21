package com.ionshield.abithelperahp.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.*;

public class Matrix {
    
    private final List<List<Double>> data;
    private final int sizeX;
    private final int sizeY;
    
    
    private Matrix(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Negative matrix size");
        }
        
        sizeX = x;
        sizeY = y;
        data = new ArrayList<>();
        
        for (int i = 0; i < sizeX; ++i) {
            data.add(new ArrayList<>());
            for (int j = 0; j < sizeY; ++j) {
                data.get(i).add(0.0);
            }
        }
        
    }
    
    private Matrix(Matrix that) {
        
        sizeX = that.sizeX;
        sizeY = that.sizeY;
        data = new ArrayList<>();
        
        for (int i = 0; i < sizeX; ++i) {
            data.add(i, new ArrayList<>());
            for (int j = 0; j < sizeY; ++j) {
                data.get(i).add(j, 0.0);
            }
        }
        
        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                set(i, j, that.get(i, j));
            }
        }
    }
    
    public static Matrix makeEmptyMatrix(int sizeX, int sizeY) {
        return new Matrix(sizeX, sizeY);
    }
    
    public static Matrix makeEmptyMatrix(int size) {
        return makeEmptyMatrix(size, size);
    }
    
    public static Matrix makeValueMatrix(int sizeX, int sizeY, double value) {
        return new Matrix(sizeX, sizeY).fill((i, j) -> value);
    }
    
    public static Matrix makeValueMatrix(int size, double value) {
        return makeValueMatrix(size, size, value);
    }
    
    public static Matrix makeIdentityMatrix(int size) {
        Matrix m = new Matrix(size, size);
        
        for (int i = 0; i < size; ++i) {
            m.set(i, i, 1.0);
        }
        
        return m;
    }
    
    public Matrix fill(double[][] values) {
        Matrix m = copy();
    
        if (values.length > sizeX) {
            throw new IllegalArgumentException("Invalid dimensions of input array");
        }
        for (int i = 0; i < values.length; ++i) {
            if (values[i].length > sizeY) {
                throw new IllegalArgumentException("Invalid dimensions of input array");
            }
            
            for (int j = 0; j < values[i].length; ++j) {
                m.set(i, j, values[i][j]);
            }
        }
        
        return m;
    }
    
    public Matrix fill(List<List<Double>> values) {
        Matrix m = copy();
        
        if (values.size() > sizeX) {
            throw new IllegalArgumentException("Invalid dimensions of input list");
        }
        for (int i = 0; i < values.size(); ++i) {
            if (values.get(i).size() > sizeY) {
                throw new IllegalArgumentException("Invalid dimensions of input list");
            }
        
            for (int j = 0; j < values.get(i).size(); ++j) {
                m.set(i, j, values.get(i).get(j));
            }
        }
        
        return m;
    }
    
    public Matrix fill(int rowOffset, int columnOffset, Matrix matrix) {
        Matrix m = copy();
        
        if (columnOffset + matrix.sizeX > sizeX || rowOffset + matrix.sizeY > sizeY) {
            throw new IllegalArgumentException("Invalid dimensions of input matrix");
        }
        
        matrix.forEach((i, j) -> m.set(columnOffset + i, rowOffset + j, matrix.get(i, j)));
        
        return m;
    }
    
    public Matrix fill(Matrix matrix) {
        return fill(0, 0, matrix);
    }
    
    public Matrix fill(BiFunction<Integer, Integer, Double> filler) {
        Matrix m = copy();
        
        for (int i = 0; i < sizeX; ++i) {
            
            for (int j = 0; j < sizeY; ++j) {
                m.set(i, j, filler.apply(i, j));
            }
        }
        
        return m;
    }
    
    public void forEach(Consumer<Double> action) {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                action.accept(get(i, j));
            }
        }
    }
    
    public void forEach(BiConsumer<Integer, Integer> action) {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                action.accept(i, j);
            }
        }
    }
    
    public Matrix forEach(Matrix matrix, BinaryOperator<Double> operation) {
        if (matrix.sizeX != sizeX || matrix.sizeY != sizeY) {
            throw new IllegalArgumentException("Invalid dimensions of input matrix");
        }
        return fill((i, j) -> operation.apply(get(i, j), matrix.get(i, j)));
    }
    
    public void forEachInRow(int row, Consumer<Double> action) {
        checkIndexY(row);
        for (int i = 0; i < sizeX; i++) {
            action.accept(get(i, row));
        }
    }
    
    public void forEachInColumn(int column, Consumer<Double> action) {
        checkIndexX(column);
        for (int i = 0; i < sizeY; i++) {
            action.accept(get(column, i));
        }
    }
    
    public Matrix map(UnaryOperator<Double> mapper) {
        return fill((i, j) -> mapper.apply(get(i, j)));
    }
    
    public double reduce(Double identity, BinaryOperator<Double> accumulator) {
        Double[] value = {identity};
        forEach(d -> value[0] = accumulator.apply(value[0], d));
        return value[0];
    }
    
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, Double> accumulator) {
        R result = supplier.get();
        forEach(d -> accumulator.accept(result, d));
        return result;
    }
    
    public Matrix swapRows(int a, int b) {
        if (a < 0 || b < 0 || a >= sizeY || b>= sizeY) {
            throw new IllegalArgumentException("Row index is out of bounds");
        }
        
        Matrix m = copy();
        
        for (int i = 0; i < sizeX; ++i) {
            double temp = m.data.get(i).get(b);
            m.data.get(i).set(b, m.data.get(i).get(a));
            m.data.get(i).set(a, temp);
        }
        
        return m;
    }
    
    public Matrix swapColums(int a, int b) {
        if (a < 0 || b < 0 || a >= sizeX || b>= sizeX) {
            throw new IllegalArgumentException("Column index is out of bounds");
        }
    
        Matrix m = copy();
        
        List<Double> temp = m.data.get(b);
        m.data.set(b, data.get(a));
        m.data.set(a, temp);
    
        return m;
    }
    
    public Matrix transpose() {
        Matrix m = makeEmptyMatrix(sizeY(), sizeX());
        return m.fill((i, j) -> get(j, i));
    }
    
    public double get(int x, int y) {
        checkIndex(x, y);
        return data.get(x).get(y);
    }
    
    private void set(int x, int y, double value) {
        checkIndex(x, y);
        data.get(x).set(y, value);
    }
    
    public Double determinant(boolean cutToSquare) {
        if (!cutToSquare) {
            return determinant();
        }
        int min = Math.min(sizeX, sizeY);
        return submatrix(0, min, 0, min).determinant();
    }
    
    public Double determinant() {
        if (!isSquare()) {
            return null;
        }
        
        if (sizeX == 1 && sizeY == 1) {
            return get(0, 0);
        }
        
        double result = 0.0;
        
        for (int i = 0; i < sizeX; ++i) {
            Double det = minor(i, 0).determinant();
            if (det == null) {
                return null;
            }
            if (i % 2 == 0) {
                result += get(i, 0) * det;
            }
            else {
                result -= get(i, 0) * det;
            }
        }
        
        return result;
    }
    
    public int sizeX() {
        return sizeX;
    }
    
    public int sizeY() {
        return sizeY;
    }
    
    public boolean isSquare() {
        return sizeX == sizeY;
    }
    
    public Matrix copy() {
        return new Matrix(this);
    }
    
    public Matrix submatrix(int startX, int endX, int startY, int endY) {
        
        if (startX > endX) {
            throw new IllegalArgumentException("End X index: " + endX + " is lower than start X index: " + startX);
        }
        if (endX > sizeX) {
            throw new IllegalArgumentException("Index X is out of bounds: " + endX);
        }
        if (startX < 0) {
            throw new IllegalArgumentException("Index X is out of bounds: " + startX);
        }
        if (startY > endY) {
            throw new IllegalArgumentException("End Y index: " + endY + " is lower than start Y index: " + startY);
        }
        if (endY > sizeY) {
            throw new IllegalArgumentException("Index Y is out of bounds: " + endY);
        }
        if (startY < 0) {
            throw new IllegalArgumentException("Index X is out of bounds: " + startY);
        }
        
        int sizeX = endX - startX;
        int sizeY = endY - startY;
        Matrix m = new Matrix(sizeX, sizeY);
        
        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                m.set(i, j, get(startX + i, startY + j));
            }
        }
        
        return m;
    }
    
    public Matrix concatRows(Matrix matrix) {
        if (matrix.sizeX != sizeX) {
            throw new IllegalArgumentException("Matrix row size is not equal");
        }
        
        Matrix m = new Matrix(sizeX, sizeY + matrix.sizeY);
        
        m = m.fill(this);
        
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < matrix.sizeY; j++) {
                m.set(i, sizeY + j, matrix.get(i, j));
            }
        }
        
        return m;
    }
    
    public Matrix concatColumns(Matrix matrix) {
        if (matrix.sizeY != sizeY) {
            throw new IllegalArgumentException("Matrix column size is not equal");
        }
    
        Matrix m = new Matrix(sizeX + matrix.sizeX, sizeY);
    
        m = m.fill(this);
    
        for (int i = 0; i < matrix.sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                m.set(sizeX + i, j, matrix.get(i, j));
            }
        }
    
        return m;
    }
    
    public Matrix minor(int x, int y) {
        if (sizeX < 2 || sizeY < 2) {
            throw new IllegalArgumentException("The matrix size is less or equal to 1");
        }
        
        Matrix lt;
        Matrix rt;
        Matrix lb;
        Matrix rb;
        
        if (x > 0 && y > 0) {
            lt = submatrix(0, x, 0, y);
        }
        else {
            lt = null;
        }
    
        if (x < sizeX - 1 && y > 0) {
            rt = submatrix(x + 1, sizeX, 0, y);
        }
        else {
            rt = null;
        }
    
        if (x > 0 && y < sizeY - 1) {
            lb = submatrix(0, x, y + 1, sizeY);
        }
        else {
            lb = null;
        }
    
        if (x < sizeX - 1 && y < sizeY - 1) {
            rb = submatrix(x + 1, sizeX, y + 1, sizeY);
        }
        else {
            rb = null;
        }
        
        Matrix t;
        Matrix b;
        
        if (lt != null && rt != null) {
            t = lt.concatColumns(rt);
        }
        else if (lt == null) {
            t = rt;
        }
        else {
            t = lt;
        }
    
        if (lb != null && rb != null) {
            b = lb.concatColumns(rb);
        }
        else if (lb == null) {
            b = rb;
        }
        else {
            b = lb;
        }

        Matrix result;

        if (t != null && b!= null) {
            result = t.concatRows(b);
        }
        else if (t == null) {
            result = b;
        }
        else {
            result = t;
        }
        
        return result;
    }
    
    public Matrix add(int rowOffset, int columnOffset, Matrix matrix) {
        Matrix m = copy();
    
        if (columnOffset + matrix.sizeX > sizeX || rowOffset + matrix.sizeY > sizeY) {
            throw new IllegalArgumentException("Invalid dimensions of input matrix");
        }
        
        matrix.forEach((i, j) -> m.set(columnOffset + i, rowOffset + j, m.get(columnOffset + i, rowOffset + j) + matrix.get(i, j)));
        
        return m;
    }
    
    public Matrix add(Matrix matrix) {
        return add(0, 0, matrix);
    }
    
    public Matrix add(double value) {
        return  fill((i, j) -> get(i, j) + value);
    }

    public Matrix negative() {
        return fill((i, j) -> -get(i, j));
    }

    public Matrix multiply(Matrix matrix) {
        if (matrix.sizeY != sizeX) {
            throw new IllegalArgumentException("Impossible to perform multiplication: invalid matrix sizes: " + sizeX + " != " + matrix.sizeY);
        }
    
        Matrix m = new Matrix(matrix.sizeX, sizeY);
        m = m.fill((i, j) -> {
            double res = 0;
            for (int k = 0; k < sizeX; k++) {
                res += get(k, j) * matrix.get(i, k);
            }
            return res;
        });
        
        return m;
    }

    public Matrix multiplyEach(int rowOffset, int columnOffset, Matrix matrix) {
        Matrix m = copy();
        
        if (columnOffset + matrix.sizeX > sizeX || rowOffset + matrix.sizeY > sizeY) {
            throw new IllegalArgumentException("Invalid dimensions of input matrix");
        }
        
        matrix.forEach((i, j) -> m.set(columnOffset + i, rowOffset + j, m.get(columnOffset + i, rowOffset + j) * matrix.get(i, j)));
    
        return m;
    }
    
    public Matrix multiplyEach(Matrix matrix) {
        return multiplyEach(0, 0, matrix);
    }
    
    public Matrix multiplyEach(double value) {
        return fill((i, j) -> get(i, j) * value);
    }

    public double sum() {
        final double[] sum = {0};
        
        forEach(val -> sum[0] += val);
        
        return sum[0];
    }
    
    public double sum(Function<Double, Double> operation) {
        final double[] sum = {0};
        
        forEach(val -> sum[0] += operation.apply(val));
        
        return sum[0];
    }

    public double sumRow(int row) {
        final double[] sum = {0};
    
        forEachInRow(row, val -> sum[0] += val);
    
        return sum[0];
    }
    
    public double sumRow(int row, Function<Double, Double> operation) {
        final double[] sum = {0};
        
        forEachInRow(row, val -> sum[0] += operation.apply(val));
        
        return sum[0];
    }

    public double sumColumn(int column) {
        final double[] sum = {0};
    
        forEachInColumn(column, val -> sum[0] += val);
    
        return sum[0];
    }
    
    public double sumColumn(int column, Function<Double, Double> operation) {
        final double[] sum = {0};
        
        forEachInColumn(column, val -> sum[0] += operation.apply(val));
        
        return sum[0];
    }
    
    public Matrix getRow(int row){
        checkIndexY(row);
        
        Matrix m = new Matrix(sizeX, 1);
        
        for (int i = 0; i < sizeX; i++) {
            m.set(i, 0, get(i, row));
        }
        
        return m;
    }
    
    public Matrix getColumn(int column){
        checkIndexX(column);
    
        Matrix m = new Matrix(1, sizeY);
    
        for (int i = 0; i < sizeX; i++) {
            m.set(0, i, get(column, i));
        }
    
        return m;
    }
    
    public double rowNorm() {
        List<Double> values = new ArrayList<>();
    
        for (int i = 0; i < sizeY(); i++) {
            values.add(sumRow(i, Math::abs));
        }
        if (values.isEmpty()) {
            return Double.NaN;
        }
        return values.stream().reduce(values.get(0), Math::max);
    }
    
    public double columnNorm() {
        List<Double> values = new ArrayList<>();
    
        for (int i = 0; i < sizeX(); i++) {
            values.add(sumColumn(i, Math::abs));
        }
        if (values.isEmpty()) {
            return Double.NaN;
        }
        return values.stream().reduce(values.get(0), Math::max);
    }
    
    public double abs() {
        return Math.sqrt(sum(d -> d * d));
    }
    
    protected void checkIndex(int x, int y) {
        checkIndexX(x);
        checkIndexY(y);
    }
    
    protected void checkIndexX(int x) {
        if (x < 0 || x >= sizeX) {
            throw new IllegalArgumentException("Index x is out of bounds [0, " + (sizeX - 1) + "]: " + x);
        }
    }
    
    protected void checkIndexY(int y) {
        if (y < 0 || y >= sizeY) {
            throw new IllegalArgumentException("Index y is out of bounds [0, " + (sizeY - 1) + "]: " + y);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;
        
        if (matrix.sizeX != sizeX || matrix.sizeY != sizeY) {
            return false;
        }
    
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (get(i, j) != matrix.get(i, j)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(data, sizeX, sizeY);
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
    
        for (int j = 0; j < sizeY; j++) {
            for (int i = 0; i < sizeX; i++) {
                
                s.append(get(i, j));
                
                if (i == sizeX - 1) {
                    if (j != sizeY - 1) {
                        s.append("\n");
                    }
                }
                else {
                    s.append(" ");
                }
                
            }
        }
        
        return s.toString();
    }
}
