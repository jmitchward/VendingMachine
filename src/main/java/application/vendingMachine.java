package application;

import machineController.machineController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class vendingMachine {
    public static void main(String [] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        machineController controller = context.getBean("controller", machineController.class);
        controller.programStart();

    }
}
