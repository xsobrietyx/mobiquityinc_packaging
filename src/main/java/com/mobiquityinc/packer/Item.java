package com.mobiquityinc.packer;

import java.util.Objects;

/**
 * Class that serves for data wrapping. Also holds some important methods for data processing.
 */
class Item implements Comparable<Item> {
    private Integer index;
    private Double weight;
    private Integer cost;

    Item(Integer index, Double weight, Integer cost) {
        this.index = index;
        this.weight = weight;
        this.cost = cost;
    }

    Integer getIndex() {
        return index;
    }

    Double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(index, item.index) &&
                Objects.equals(weight, item.weight) &&
                Objects.equals(cost, item.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, weight, cost);
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") Item o) {
        if (Objects.equals(this.cost, o.cost))
            return this.weight > o.weight ? 1 : -1;
        else
            return this.cost > o.cost ? -1 : 1;
    }

    @Override
    public String toString() {
        return index.toString();
    }
}
