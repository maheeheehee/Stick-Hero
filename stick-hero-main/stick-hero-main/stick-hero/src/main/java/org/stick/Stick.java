package org.stick;

public class Stick {
    private static Stick instance;

    private double length;
    private double angle;
    private double x;
    private double y;

    private Stick() {
    }

    public static Stick getInstance() {
        if (instance == null) {
            instance = new Stick();
        }
        return instance;
    }
    // Used singleton pattern

    public void setLength(double length) {
        this.length = length;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
