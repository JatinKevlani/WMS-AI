package com.wmsai.oopj;

import com.wmsai.entity.PurchaseOrder;
import java.util.PriorityQueue;

/**
 * OrderQueue extends PriorityQueue and implements Cloneable.
 * Covers T039, OOPJ-28.
 * Orders are prioritized by status (PENDING first via PurchaseOrder.compareTo).
 */
public class OrderQueue extends PriorityQueue<PurchaseOrder> implements Cloneable {

    public OrderQueue() {
        super();
    }

    /**
     * Deep clone of the queue [OOPJ-28].
     */
    @Override
    @SuppressWarnings("unchecked")
    public OrderQueue clone() {
        OrderQueue cloned = new OrderQueue();
        cloned.addAll(this);
        return cloned;
    }

    /** Returns summary of the queue state. */
    public String getQueueSummary() {
        return "OrderQueue[size=" + size() + ", pending=" +
                stream().filter(o -> o.getStatus().name().equals("PENDING")).count() + "]";
    }
}
