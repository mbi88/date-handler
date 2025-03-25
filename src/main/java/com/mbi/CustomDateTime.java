package com.mbi;

/**
 * POJO representing a custom date-time duration.
 * <p>
 * Fields:
 * <ul>
 *     <li>y  - years</li>
 *     <li>mo - months</li>
 *     <li>d  - days</li>
 *     <li>h  - hours</li>
 *     <li>m  - minutes</li>
 *     <li>s  - seconds</li>
 * </ul>
 */
final class CustomDateTime {

    private int y;   // years
    private int mo;  // months
    private int d;   // days
    private int h;   // hours
    private int m;   // minutes
    private int s;   // seconds

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public int getMo() {
        return mo;
    }

    public void setMo(final int mo) {
        this.mo = mo;
    }

    public int getD() {
        return d;
    }

    public void setD(final int d) {
        this.d = d;
    }

    public int getH() {
        return h;
    }

    public void setH(final int h) {
        this.h = h;
    }

    public int getM() {
        return m;
    }

    public void setM(final int m) {
        this.m = m;
    }

    public int getS() {
        return s;
    }

    public void setS(final int s) {
        this.s = s;
    }
}
