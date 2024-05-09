package control;

import data.Utils;
import data.Packet;
import data.Importer;
import gui.PackageCalculator;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Calculator} class calculates shipping costs for parcels based on their dimensions and weight.
 * Shipping costs can be set for different shipping providers such as DHL or Hermes.
 * <p>
 * The class provides methods to initialize shipping costs, set shipping costs based on the chosen provider,
 * and calculate shipping costs for a given parcel. It also includes an enumeration for representing different
 * sizes of parcels and a private method to check if a parcel's size is smaller than or equal to a reference packet.
 * </p>
 * <p>
 * The class uses an {@link Importer} to import shipping cost data from a CSV file, and the default shipping costs
 * are initialized with DHL. The shipping costs can be later set to Hermes using the {@code setShippingCost} method.
 * </p>
 * <p>
 * The class is designed to be used in a shipping cost calculation system, where it provides flexibility in choosing
 * the shipping provider and calculating costs based on parcel dimensions and weight.
 * </p>
 */


public class Calculator {

    final Importer importer = new Importer("data/shippingCosts.csv");

    /**
     * shippingCosts is an ArrayList with the prices:
     * 0: small
     * 1: medium
     * 2: large
     * 3: large from 5kg
     * 4: large up to 31.5kg
     * */
    private List<Double> shippingCosts;

    public Calculator(){
        this.shippingCosts = importer.getPriceDHL();
    }

    /**
     * Sets the shipping costs based on the choice of the shipping provider.
     *
     * @param company The choice of the shipping provider.
     */
    public void setShippingChoice(final Utils.Company company){
        switch (company){
            case HERMES -> this.shippingCosts = importer.getPriceHermes();
            case DHL -> this.shippingCosts = importer.getPriceDHL();
        }
    }

    /**
     * Calculates the shipping costs for a given parcel.
     *
     * @param packet The parcel object with attributes (length, width, height, weight).
     * @param vat todo
     * @param express boolean
     * @return The calculated shipping costs for the parcel.
     */
    public double calcShippingCosts(final Packet packet, final boolean express, final double vat) {

        double cost = 0;

        if (isSmall(packet)) {
            cost = shippingCosts.get(0);
        } else if (isMedium(packet)) {
            cost = shippingCosts.get(1);
        } else if (isLarge(packet)) {
            // check for combinedDimensions and weight for the price
            if(packet.combinedDimensions <= 3000 && packet.weight <= 10000) {
                cost = packet.weight <= 5000 ? shippingCosts.get(2) : shippingCosts.get(3);
            } else if (packet.weight <= 31500) {
                cost = shippingCosts.get(4);
            }
            else{
                String error = "Package dimensions exceed the maximum limits or invalid";
                PackageCalculator.getInstance().messagesArea.setMessage(error);
                throw new IllegalArgumentException(error);
            }
        } else {
            String error = "Package dimensions exceed the maximum limits or invalid";
            PackageCalculator.getInstance().messagesArea.setMessage(error);
            throw new IllegalArgumentException(error);
        }

        if (express){
            cost = cost *1.2;
        }
        cost = cost * 1-vat;

        return cost;
    }

    private boolean isSmall(Packet packet){
        return packet.length <= 300 && packet.width <= 300 && packet.height <= 150 && packet.weight <= 1000;
    }

    private boolean isMedium(Packet packet){
        return packet.length <= 600 && packet.width <= 300 && packet.height <= 150 && packet.weight <= 2000;
    }

    private boolean isLarge(Packet packet){
        return packet.length <= 1200 && packet.width <= 600 && packet.height <= 600;
    }
}
