package com.yuzhou.rmq.rc;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-02
 * Time: 上午10:47
 */
public class ConsumerInstance implements Serializable {

    private static final long serialVersionUID = 7347910076883583054L;

    private String name;

    private long lastUpdated;

    private boolean active;

    private String lastDeliverdId;

    public String getName() {
        return name;
    }

    public String getLastDeliverdId() {
        return lastDeliverdId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setLastDeliverdId(String lastDeliverdId) {
        this.lastDeliverdId = lastDeliverdId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumerInstance that = (ConsumerInstance) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
