package md;

import java.util.Arrays;


public class MD {
    protected double[] x, y, vx, vy, ax, ay;
    protected double kE, pE, eE, te;
    private int nAtoms;
    private double boxWidth, pressure;
    private int wallStiffness;


    public MD(double[] xStart, double[] yStart, double[] vxStart, double[] vyStart, int boxWidth) {
        this.nAtoms = xStart.length;
        this.boxWidth = boxWidth;
        this.ax = new double[nAtoms];
        this.ay = new double[nAtoms];
        this.x = Arrays.copyOf(xStart, xStart.length);
        this.y = Arrays.copyOf(yStart, yStart.length);
        this.vx = Arrays.copyOf(vxStart, vxStart.length);
        this.vy = Arrays.copyOf(vyStart, vyStart.length);
        wallStiffness = 50;
        calculateAcceleration();
        totalEnergy();
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }


    public double getPressure() {
        return pressure;
    }

    private void calculateAcceleration() {
        for (int i = 0; i < nAtoms; i++) {
            ax[i] = 0;
            ay[i] = 0;
        }

        pE = 0;


        for (int i = 0; i < nAtoms - 1; i++)
            for (int j = i + 1; j < nAtoms; j++) {
                double dx = x[i] - x[j];
                double dy = y[i] - y[j];
                double rij2 = dx * dx + dy * dy;

                double fr2 = 1. / rij2;

                double fr6 = fr2 * fr2 * fr2;
                countPE(fr6);
                double fr = 48. * fr6 * (fr6 - 0.5) / rij2;


                double frx = fr * dx;
                double fry = fr * dy;

                ax[i] += frx;
                ay[i] += fry;

                ax[j] -= frx;
                ay[j] -= fry;

            }

        eE = 0;

        pressure=0;
        for (int i = 0; i < nAtoms; i++) {
            double d,da;
            if (x[i] < 0.5) {
                d = 0.5 - x[i];
                da= wallStiffness * d;
                pressure+=Math.abs(da);
                ax[i] += da;
                eE += 0.5 * wallStiffness * d * d;
            } else if (x[i] > boxWidth - 0.5) {
                d = boxWidth - 0.5 - x[i];
                ax[i] += wallStiffness * d;
                eE += 0.5 * wallStiffness * d * d;
            }

            if (y[i] < 0.5) {
                d = 0.5 - y[i];
                da= wallStiffness * d;
                pressure+=Math.abs(da);
                ay[i] += da;
                eE += 0.5 * wallStiffness * d * d;
            } else if (y[i] > boxWidth - 0.5) {
                d = boxWidth - 0.5 - y[i];
                ay[i] += wallStiffness * d;
                eE += 0.5 * wallStiffness * d * d;
            }
            }
    }


    private void countPE(double fr6) {
        pE += 4 * fr6 * (fr6 - 1);
    }

    public void verletStep(double dt) {

        kE = 0;
        double vyMid[] = new double[nAtoms];
        double vxMid[] = new double[nAtoms];
        for (int i = 0; i < nAtoms; i++) {
            vyMid[i] = vy[i] + dt * ay[i] / 2;
            y[i] = y[i] + dt * vyMid[i];

            vxMid[i] = vx[i] + dt * ax[i] / 2;
            x[i] = x[i] + dt * vxMid[i];

        }
        calculateAcceleration();
        for (int i = 0; i < nAtoms; i++) {
            vx[i] = vxMid[i] + ax[i] / 2 * dt;
            vy[i] = vyMid[i] + ay[i] / 2 * dt;

            countKE(vx[i], vy[i]);
        }
        totalEnergy();
    }

    public void countKE(double vx, double vy) {
        kE += (vx * vx + vy * vy) * 0.5;

    }




    public void totalEnergy() {
        te = kE + pE + eE;
    }

}

