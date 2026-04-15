package com.wmsai.oopj;

/**
 * WarehouseZone — demonstrates constructor overloading for volume calculations.
 * Covers T070, OOPJ-11 (constructor overloading), OOPJ-12 (copy constructor).
 *
 * Constructors:
 *   Zone(a)      — cubic zone
 *   Zone(l,b,h)  — cuboid zone
 *   Zone(r,h)    — cylindrical silo
 *   Zone(other)  — copy constructor
 */
public class WarehouseZone {
    private String type;
    private double volume;
    private String zoneName;

    /** Cubic zone constructor [OOPJ-11]. */
    public WarehouseZone(double side) {
        this.type = "Cube";
        this.volume = side * side * side;
        this.zoneName = "Zone-Cube-" + (int) side;
    }

    /** Cuboid zone constructor [OOPJ-11]. */
    public WarehouseZone(double length, double breadth, double height) {
        this.type = "Cuboid";
        this.volume = length * breadth * height;
        this.zoneName = "Zone-Cuboid-" + (int) length + "x" + (int) breadth + "x" + (int) height;
    }

    /** Cylindrical silo constructor (2 params, differentiated by a flag) [OOPJ-11]. */
    public WarehouseZone(double radius, double height, boolean isCylinder) {
        this.type = "Cylinder";
        this.volume = Math.PI * radius * radius * height;
        this.zoneName = "Zone-Silo-R" + (int) radius;
    }

    /** Copy constructor [OOPJ-12]. */
    public WarehouseZone(WarehouseZone other) {
        this.type = other.type;
        this.volume = other.volume;
        this.zoneName = other.zoneName + "-COPY";
    }

    public String getType() { return type; }
    public double getVolume() { return volume; }
    public String getZoneName() { return zoneName; }

    @Override
    public String toString() {
        return String.format("WarehouseZone[%s, name=%s, volume=%.2f m³]", type, zoneName, volume);
    }
}
