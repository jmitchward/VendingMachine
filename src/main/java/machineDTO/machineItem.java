package machineDTO;

import java.math.BigDecimal;
import java.util.Objects;

public class machineItem {
    String itemId;
    String name;
    // Name of the item.
    BigDecimal cost;
    // Cost of the item.
    int stock;
    // Number of items in stock.

    public machineItem(String itemId, String name, BigDecimal cost, int stock) {
        // Because the user is not interacting with this model, the constructor parameters are set for private use.
        this.itemId = itemId;
        this.name = name;
        this.cost = cost;
        this.stock = stock;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    // The name and cost of an individual item are set at the time they are created, therefore they will not need be changed
    // These values are set at the time of construction.

    public int getStock() {
        return stock;
    }

    public void setStock(int updatedStock) {
        this.stock = updatedStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        machineItem that = (machineItem) o;
        return stock == that.stock &&
                Objects.equals(itemId, that.itemId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, name, cost, stock);
    }

    @Override
    public String toString() {
        return "machineItem{" +
                "itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", stock=" + stock +
                '}';
    }
    // The stock of an item is the only mutable value within this class, therefore it must be updatable.

}
