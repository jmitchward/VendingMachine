package machineController;

import machineDAO.InventoryNotFoundException;
import machineDTO.machineItem;
import machineService.machineService;
import machineView.machineView;
import machineService.NoItemInventoryException;
import machineService.InsufficientFundsException;
import machineService.RepeatIdException;
import machineService.BlankValueException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class machineController {
    // The controller interacts with two aspects of the program, the view and the service layer
    machineService service;
    machineView view;
    // They are declared here.

    public machineController(machineService service, machineView view) {
        this.service = service;
        this.view = view;
    }

    public void programStart() throws NoItemInventoryException, InsufficientFundsException, IOException, InventoryNotFoundException, BlankValueException, RepeatIdException, SQLException {
        view.displayMoney(service.checkTotal());
        // Program starts with the menu
        String menuSelect = view.getMenu(service.displayItems());
        // The user is selecting an item from a menu. Any item other than 0 will indicate an attempt to make a purchase
        // Therefore if it is not 0, it must be an item id
        if (menuSelect.equals("0")) {
            enterMoney();
        }
        else if (menuSelect.equals("-1")) {
            exitChain();
        }
        else if (menuSelect.equals("-2")) {
            insertItems();
        }
        else if (service.getItem(menuSelect) instanceof machineItem) {
            checkMoney();
            purchaseChain(menuSelect);
        }
        else {
            view.displayError("Invalid selection.");
            programStart();
        }
        programStart();
    }

    public void purchaseChain(String itemId) throws NoItemInventoryException, InsufficientFundsException, BlankValueException, RepeatIdException, InventoryNotFoundException, IOException, SQLException {
        view.vendItem(service.getItem(itemId).getName());
        // The requested item will need to be acquired.
        try {
            machineItem purchased = service.purchaseItem(itemId);
            view.vendSuccess(purchased, service.returnChange());
        } catch (InsufficientFundsException | NoItemInventoryException e) {
            view.displayError(e.getMessage());
            programStart();
        }
    }

    public void enterMoney() {
        BigDecimal userTotal = view.getMoney();
        service.setTotal(userTotal);
    }

    public void exitChain() throws IOException, InventoryNotFoundException, SQLException {
        view.displayExit();
        service.exitCascade();
        System.exit(0);
    }

    public void insertItems() {
        String newItemId = service.requestId();
        machineItem newItem = view.getItems(newItemId);
        try {
            service.addItem(newItem);
        } catch (RepeatIdException | BlankValueException e) {
            view.displayError(e.getMessage());
            view.getItems(newItemId);
        } catch (InventoryNotFoundException e) {
            view.displayError(e.getMessage());
        } catch (IOException e) {
            view.displayError(e.getMessage());
        }
    }

    public void checkMoney() throws InsufficientFundsException {
        service.checkTotal();
    }




}
