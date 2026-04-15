package com.wmsai.oopj;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * NotificationQueue — ArrayList holding mixed types.
 * Covers T042, OOPJ-25 — ArrayList with Alert, LocalDate, String, WarehouseZone objects.
 */
public class NotificationQueue extends ArrayList<Object> {

    /** Add a notification item of any type. */
    public void addNotification(Object item) {
        this.add(item);
    }

    /** Print all items with their type. */
    public void printAll() {
        for (int i = 0; i < size(); i++) {
            Object item = get(i);
            System.out.println("[" + i + "] " + item.getClass().getSimpleName() + ": " + item);
        }
    }

    /** Get summary of item types in the queue. */
    public String getSummary() {
        long strings = stream().filter(o -> o instanceof String).count();
        long dates = stream().filter(o -> o instanceof LocalDate).count();
        long zones = stream().filter(o -> o instanceof WarehouseZone).count();
        long others = size() - strings - dates - zones;
        return "NotificationQueue[total=" + size() + ", strings=" + strings +
                ", dates=" + dates + ", zones=" + zones + ", others=" + others + "]";
    }
}
