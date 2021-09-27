package uk.ac.ed.inf;

public class Menus {
    String machineFirstName = "";
    String port = "";

    /*Constructor*/
    Menus(String machineFirstName, String port){
        this.machineFirstName = machineFirstName;
        this.port = port;
    }

    /*Returns the int cost in pence of having all of these items delivered by drone*/
     public int getDeliveryCost(String... items){
         return items.length * 50;
     }
}
