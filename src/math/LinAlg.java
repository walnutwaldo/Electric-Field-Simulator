package math;

import objects.Camera;
import objects.Positionable;
import shapes.Line3D;
import shapes.Triangle3D;

import java.awt.*;

public class LinAlg {

    public static final double EPSILON = 0.000001;

    public static boolean approx(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    public static boolean approx(Matrix a, Matrix b) {
        for (int i = 0; i < a.numCols(); i++) if (!approx(a.get(0, i), b.get(0, i))) return false;
        return true;
    }

    public static double dotProduct(Matrix m1, Matrix m2) {
        double res = 0;
        for (int i = 0; i < 3; i++) res += m1.get(0, i) * m2.get(0, i);
        return res;
    }

    public static Matrix crossProduct(Matrix m1, Matrix m2) {
        Matrix res = new Matrix(1, 3);
        res.set(0, 0, m1.get(0, 1) * m2.get(0, 2) - m1.get(0, 2) * m2.get(0, 1));
        res.set(0, 1, m1.get(0, 2) * m2.get(0, 0) - m1.get(0, 0) * m2.get(0, 2));
        res.set(0, 2, m1.get(0, 0) * m2.get(0, 1) - m1.get(0, 1) * m2.get(0, 0));
        return res;
    }

    public static double getDis(Matrix p, Line l) {
        return crossProduct(Matrix.subtract(p, l.pnt), Matrix.normalize(l.step)).length();
    }

    public static double getDis(Matrix p, LineSeg l) {
        if (dotProduct(Matrix.subtract(p, l.pnt1), Matrix.subtract(l.pnt2, l.pnt1)) < 0)
            return Math.sqrt(squareDis(l.pnt1, p));
        if (dotProduct(Matrix.subtract(p, l.pnt2), Matrix.subtract(l.pnt1, l.pnt2)) < 0)
            return Math.sqrt(squareDis(l.pnt2, p));
        return crossProduct(Matrix.subtract(p, l.pnt1), Matrix.normalize(Matrix.subtract(l.pnt2, l.pnt1))).length();
    }

    public static double getDis(Matrix p1, Matrix p2) {
        return Math.sqrt(squareDis(p1, p2));
    }

    public static Matrix intersection(Line l, Plane p) {
        double coeff = dotProduct(l.step, p.normal);
        double constant = dotProduct(Matrix.subtract(p.pnt, l.pnt), p.normal);
        if (approx(coeff, 0)) return null;
        Matrix res = Matrix.add(l.pnt, Matrix.scale(l.step, constant / coeff));
        return res;
    }

    public static boolean between(double a, double b, double c) {
        return a > Math.min(b, c) - EPSILON && a < Math.max(b, c) + EPSILON;
    }

    public static boolean between(Matrix p1, Matrix p2, Matrix p3) {
        if (p1 == null) return false;
        for (int i = 0; i < 3; i++) if (!between(p1.get(0, i), p2.get(0, i), p3.get(0, i))) return false;
        return true;
    }

    public static Matrix intersection(LineSeg ls, Plane p) {
        Matrix res = intersection(ls.getLine(), p);
        if (between(res, ls.pnt1, ls.pnt2)) return res;
        return null;
    }

    public static double squareDis(Matrix p1, Matrix p2) {
        double res = 0;
        for (int i = 0; i < p1.numCols(); i++) res += Math.pow(p1.get(0, i) - p2.get(0, i), 2);
        return res;
    }

    public static class Line {
        public Matrix pnt, step;

        public Line(Matrix _p1, Matrix _p2) {
            pnt = _p1;
            step = Matrix.subtract(_p2, _p1);
        }
    }

    public static class LineSeg implements Positionable {
        public Matrix pnt1, pnt2;
        public Color c;

        public LineSeg(Matrix _p1, Matrix _p2, Color _c) {
            pnt1 = _p1;
            pnt2 = _p2;
            c = _c;
        }

        public Line getLine() {
            return new Line(pnt1, pnt2);
        }

        public double getDisTo(Matrix m) {
            return Math.max(Matrix.subtract(m, pnt1).length(), Matrix.subtract(m, pnt2).length());
        }

        @Override
        public void draw(Graphics2D g) {
            Line3D.draw(g, LineSeg.mult(this, Camera.getTransformationMatrix()), c);
        }

        public static LineSeg mult(LineSeg ls, Matrix m) {
            return new LineSeg(Matrix.mult(ls.pnt1, m), Matrix.mult(ls.pnt2, m), ls.c);
        }

    }

    public static class Triangle implements Positionable {

        public Matrix p1, p2, p3;
        public Color c;

        public Triangle(Matrix _p1, Matrix _p2, Matrix _p3, Color _c) {
            p1 = _p1;
            p2 = _p2;
            p3 = _p3;
            c = _c;
        }

        public double getDisTo(Matrix m) {
            return getDis(m, centroid());
        }

        @Override
        public void draw(Graphics2D g) {
            Triangle3D.draw(g, Triangle.mult(this, Camera.getTransformationMatrix()), c);
        }

        public static Triangle mult(Triangle t, Matrix m) {
            return new Triangle(Matrix.mult(t.p1, m), Matrix.mult(t.p2, m), Matrix.mult(t.p3, m), t.c);
        }

        public Matrix centroid() {
            return Matrix.scale(Matrix.add(Matrix.add(p1, p2), p3), 1.0 / 3);
        }
    }

    public static class Plane {
        public Matrix pnt, normal;

        public Plane(Matrix _p, Matrix _n) {
            pnt = _p;
            normal = _n;
        }
    }

    public static Matrix solveLinSystem(Matrix linSystem) {
        if (linSystem.numCols() != linSystem.numRows() + 1)
            throw new IllegalArgumentException("The number of columns in a linear system matrix must be one more than the number of rows");
        Matrix m = linSystem.clone();
        for (int i = 0; i < m.numRows(); i++) {
            int firstCol;
            for (firstCol = 0; firstCol < m.numRows() && approx(m.get(i, firstCol), 0); firstCol++)
                m.set(i, firstCol, 0);
            if (firstCol == m.numRows() && !approx(m.get(i, m.numRows()), 0))
                return null;
            if (firstCol < m.numRows()) for (int j = 0; j < m.numRows(); j++)
                if (i != j) {
                    double mult = m.get(j, firstCol) / m.get(i, firstCol);
                    for (int k = 0; k < m.numCols(); k++) m.set(j, k, m.get(j, k) - m.get(i, k) * mult);
                    m.set(j, firstCol, 0);
                }
        }
        Matrix res = new Matrix(1, m.numRows());
        for (int i = 0; i < m.numRows(); i++) {
            int firstCol;
            for (firstCol = 0; firstCol < m.numRows() && approx(m.get(i, firstCol), 0); firstCol++) ;
            if (firstCol < m.numRows())
                res.set(0, firstCol, m.get(i, m.numRows()) / m.get(i, firstCol));
        }
        return res;
    }

}