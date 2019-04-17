package objects;

import math.Matrix;

import java.awt.*;

import static math.LinAlg.EPSILON;

public class Camera implements Positionable {

    public static final double FOV = 114.0 / 180 * Math.PI;

    private static final double MIN_DIS = 10, MAX_DIS = 100;
    private static final double START_DIS = 40;
    private static final double START_THETA = 0; // looking down the y -axis
    private static final double START_TILT = Math.PI / 4;

    private static double dis = START_DIS, theta = START_THETA, tilt = START_TILT;

    public static double getDis() {
        return dis;
    }

    public static double getTheta() {
        return theta;
    }

    public static double getTilt() {
        return tilt;
    }

    public static void setDis(double dis) {
        Camera.dis = Math.max(MIN_DIS, Math.min(MAX_DIS, dis));
    }

    public static void increaseDis(double inc) {
        setDis(getDis() + inc);
    }

    public static void setTheta(double theta) {
        Camera.theta = theta;
    }

    public static void increaseTheta(double inc) {
        Camera.theta += inc;
    }

    public static void setTilt(double tilt) {
        Camera.tilt = Math.max(-Math.PI / 2, Math.min(Math.PI / 2, tilt));
    }

    public static void increaseTilt(double inc) {
        setTilt(getTilt() + inc);
    }

    public static Matrix getPos() {
        return new Matrix(new double[][]{{-Math.sin(theta) * dis * Math.cos(tilt), -Math.cos(theta) * dis * Math.cos(tilt), Math.sin(tilt) * dis}});
    }

    public double getDisTo(Matrix m) {
        return Matrix.subtract(getPos(), m).length();
    }

    @Override
    public void draw(Graphics2D g) {
    }

    public static Matrix getTransformationMatrix() {
        Matrix thetaMat = new Matrix(3, 3);
        thetaMat.set(0, 0, Math.cos(theta));
        thetaMat.set(0, 1, Math.sin(theta));
        thetaMat.set(1, 0, -Math.sin(theta));
        thetaMat.set(1, 1, Math.cos(theta));
        thetaMat.set(2, 2, 1);

        Matrix tiltMat = new Matrix(3, 3);
        tiltMat.set(0, 0, 1);
        tiltMat.set(1, 1, Math.cos(tilt));
        tiltMat.set(1, 2, Math.sin(tilt));
        tiltMat.set(2, 1, -Math.sin(tilt));
        tiltMat.set(2, 2, Math.cos(tilt));

        Matrix res = Matrix.mult(thetaMat, tiltMat);
        return res;
    }

    /**
     * @param pos tranformed 3-d position
     * @return [-1, 1] x [-1, 1]
     */
    public static Matrix getProjection(Matrix pos) {
        Matrix res = new Matrix(1, 2);
        res.set(0, 0, pos.get(0, 0) / ((pos.get(0, 1) + dis) * Math.tan(FOV / 2)));
        res.set(0, 1, pos.get(0, 2) / ((pos.get(0, 1) + dis) * Math.tan(FOV / 2)));
        return res;
    }

    public static boolean visible(Matrix point) {
        if (point == null) return false;
        return point.get(0, 1) > -getDis() - EPSILON &&
                Math.abs(point.get(0, 2)) < Math.tan(FOV / 2) * (getDis() + point.get(0, 1)) + EPSILON &&
                Math.abs(point.get(0, 0)) < Math.tan(FOV / 2) * (getDis() + point.get(0, 1)) + EPSILON;
    }

    public static Matrix getTransformedPos() {
        return new Matrix(new double[][]{{0, -getDis(), 0}});
    }

}