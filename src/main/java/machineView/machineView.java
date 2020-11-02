package machineView;

import inputFormat.userIO;
import machineDTO.machineItem;

import java.math.BigDecimal;
import java.util.List;


public class machineView {
    private final userIO user;

    public machineView(userIO user) {
        this.user = user;
    }

    public String getMenu(List<machineItem> inStock) {
        user.print("Main Menu:");
        for (machineItem items : inStock) {
            user.print(items.getItemId() + " | " + items.getName() + " | Price: $" + items.getCost());
        }
        user.print("0. Enter Money");
        user.print("-1. Exit");
        return user.readString("Please make a selection: ");
    }

    public machineItem getItems(String nextItemId) {
        user.print("Admin Add: ");
        String itemId = nextItemId;
        String itemName = user.readString("Item Name: ");
        BigDecimal itemCost = user.readBig("Item Cost: $");
        int itemStock = user.readInt("Item Stock: ");
        return new machineItem(itemId, itemName, itemCost, itemStock);
    }

    public BigDecimal getMoney() {
        return user.readBig("How much would you like to put in?");
    }

    public void vendItem(String name) {
        user.print("Vending " + name + "...");
    }

    public void vendSuccess(machineItem purchased, List<BigDecimal> change) {
        user.print("Please enjoy your " + purchased.getName());
        user.print("Your change: ");
        user.print("Quarters: " + change.get(0).toString());
        user.print("Dimes: " + change.get(1).toString());
        user.print("Nickels: " + change.get(2).toString());
        user.print("Pennies: " + change.get(3).toString());
    }

    public void displayError(String message) {
        user.print("An error has occurred.");
        user.print(message);
    }

    public void displayExit() {
        user.print("Thank you for vending with us.");
    }

    public void displayMoney(BigDecimal money) {
        user.print("Current Amount: $" + money.toString());
    }


}
