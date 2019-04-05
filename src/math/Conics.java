package math;

public class Conics {

    public static Matrix getEllipseData(Matrix ellipse) {
        double A = ellipse.get(0, 0);
        double B = ellipse.get(0, 1);
        double C = ellipse.get(0, 2);
        double D = ellipse.get(0, 3);
        double E = ellipse.get(0, 4);
        double theta = 0.5 * Math.atan2(B, A - C);
        double c = Math.cos(theta), s = Math.sin(theta);
        double A2 = A * c * c + B * s * c + C * s * s;
        double C2 = A * s * s - B * s * c + C * c * c;
        double D2 = D * c + E * s;
        double E2 = -D * s + E * c;

        double m = D2 * D2 / (4 * A2) + E2 * E2 / (4 * C2) - 1;

        double a = Math.sqrt(m / A2);
        double b = Math.sqrt(m / C2);
        double h = -D2 / (2 * A2);
        double k = -E2 / (2 * C2);

        return new Matrix(new double[][]{{theta, a, b, h, k}});
    }

}
