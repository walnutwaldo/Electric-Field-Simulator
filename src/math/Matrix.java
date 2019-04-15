package math;

public class Matrix {

    private double[][] mat;

    public Matrix(Matrix m) {
        mat = new double[m.mat.length][m.mat[0].length];
        for (int i = 0; i < mat.length; i++)
            for (int j = 0; j < mat[0].length; j++)
                mat[i][j] = m.mat[i][j];
    }

    public Matrix(int r, int c) {
        mat = new double[r][c];
    }

    public static Matrix identity(int n) {
        Matrix res = new Matrix(n, n);
        for (int i = 0; i < n; i++) res.set(i, i, 1);
        return res;
    }

    public void multBy(double d) {
        for (int i = 0; i < numRows(); i++)
            for (int j = 0; j < numCols(); j++)
                mat[i][j] *= d;
    }

    public Matrix(double[][] _mat) {
        mat = new double[_mat.length][_mat[0].length];
        for (int i = 0; i < mat.length; i++)
            for (int j = 0; j < mat[0].length; j++)
                mat[i][j] = _mat[i][j];
    }

    public Matrix clone() {
        Matrix res = new Matrix(mat);
        return res;
    }

    public static Matrix mult(Matrix a, Matrix b) {
        if (a.numCols() != b.numRows())
            throw new IllegalArgumentException("Matrix dimensions do not work");
        Matrix res = new Matrix(a.numRows(), b.numCols());
        for (int i = 0; i < res.numRows(); i++)
            for (int j = 0; j < res.numCols(); j++) {
                double val = 0;
                for (int k = 0; k < a.numCols(); k++)
                    val += a.get(i, k) * b.get(k, j);
                res.set(i, j, val);
            }
        return res;
    }

    public static Matrix add(Matrix a, Matrix b) {
        if (a.numRows() != b.numRows() || a.numCols() != b.numCols())
            throw new IllegalArgumentException("Matrix dimensions do not work");
        Matrix res = new Matrix(a.numRows(), a.numCols());
        for (int i = 0; i < a.numRows(); i++)
            for (int j = 0; j < a.numCols(); j++)
                res.set(i, j, a.get(i, j) + b.get(i, j));
        return res;
    }

    public static Matrix subtract(Matrix a, Matrix b) {
        if (a.numRows() != b.numRows() || a.numCols() != b.numCols())
            throw new IllegalArgumentException("Matrix dimensions do not work");
        Matrix res = new Matrix(a.numRows(), a.numCols());
        for (int i = 0; i < a.numRows(); i++)
            for (int j = 0; j < a.numCols(); j++)
                res.set(i, j, a.get(i, j) - b.get(i, j));
        return res;
    }

    public static Matrix scale(Matrix a, double scalar) {
        Matrix res = a.clone();
        res.multBy(scalar);
        return res;
    }

    public static Matrix normalize(Matrix a) {
        if (a.numRows() != 1)
            throw new IllegalArgumentException("Can only normalize vectors (1 row matrices)");
        double length = 0;
        for (int i = 0; i < a.numCols(); i++) length += Math.pow(a.get(0, i), 2);
        length = Math.sqrt(length);

        Matrix res = new Matrix(1, a.numCols());

        for (int i = 0; i < a.numCols(); i++) res.set(0, i, a.get(0, i) / length);
        return res;
    }

    public double length() {
        double res = 0;
        for (int i = 0; i < numCols(); i++) res += Math.pow(get(0, i), 2);
        return Math.sqrt(res);
    }

    public void set(int r, int c, double val) {
        mat[r][c] = val;
    }

    public double get(int r, int c) {
        return mat[r][c];
    }

    public int numRows() {
        return mat.length;
    }

    public int numCols() {
        return mat[0].length;
    }

    public String toString() {
        String res = "[";
        for (int i = 0; i < numRows(); i++) {
            res += "[";
            for (int j = 0; j < numCols(); j++) {
                res += get(i, j);
                if (j < numCols() - 1) res += ", ";
                else res += "]";
            }
            if (i < numRows() - 1) res += '\n';
        }
        res += "]";
        return res;
    }

    public Matrix t() {
        Matrix res = new Matrix(numCols(), numRows());
        for (int i = 0; i < numRows(); i++)
            for (int j = 0; j < numCols(); j++) res.set(j, i, get(i, j));
        return res;
    }

    public Matrix inv() {
        if (numCols() != 3 || numRows() != 3) throw new IllegalArgumentException("Matrix is not 3 by 3");
        Matrix res = new Matrix(3, 3);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                double a1 = get((i + 1) % 3, (j + 1) % 3) * get((i + 2) % 3, (j + 2) % 3);
                double a2 = get((i + 1) % 3, (j + 2) % 3) * get((i + 2) % 3, (j + 1) % 3);
                res.set(i, j, a1 - a2);
            }
        double det = 0;
        for (int i = 0; i < 3; i++) det += res.get(0, i) * get(0, i);
        res = res.t();
        res.multBy(1 / det);
        return res;
    }
}