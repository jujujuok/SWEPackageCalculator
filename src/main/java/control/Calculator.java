package control;

import data.Utils;
import data.Packet;
import data.Importer;

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
    private List<Double> shippingCosts;
    private float vat;
    private boolean express;
    private Utils.Destination destination;

    //enumerate representing different sizes of parcels
    private enum PacketSize {
        VerySmall,
        Small,
        Medium,
        Large,
        VeryLarge
    }

    public Calculator(){
        this.shippingCosts = importer.getPriceDHL();
        this.vat = 0;
        this.express = false;
        this.destination = Utils.Destination.GERMANY;
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


    public void setVat(final int vat){
        this.vat = (float) vat /100;
    }

    public void setExpress(final boolean express){
        this.express = express;
    }

    public void setDestination(final Utils.Destination destination){
        this.destination = destination;
    }

    /**
     * Calculates the shipping costs for a given parcel.
     *
     * @param packet The parcel object with attributes (length, width, height, weight).
     * @return The calculated shipping costs for the parcel.
     */
    public double calcShippingCosts(Packet packet) {

        System.out.println("Calculating shipping costs");

        System.out.println(this.shippingCosts);

        double cost;

        if (packet.length <= 300 && packet.width <= 300 && packet.height <= 150 && packet.weight <= 1000) {
            cost = shippingCosts.get(0);
        } else if (packet.length <= 600 && packet.width <= 300 && packet.height <= 150 && packet.weight <= 2000) {
            cost = shippingCosts.get(1);
        } else if (packet.length <= 1200 && packet.width <= 600 && packet.height <= 600) {
            if(packet.combinedDimensions <= 3000 && packet.weight <= 10000) {
                if (packet.weight <= 5000) {
                    cost = shippingCosts.get(2);
                } else {
                    cost = shippingCosts.get(3);
                }
            } else if (packet.weight <= 31500) {
                cost = shippingCosts.get(4);
            } else {
                throw new IllegalArgumentException("Package weight exceeds the maximum limit");
            }
        } else {
            throw new IllegalArgumentException("Package dimensions exceed the maximum limits or invalid");
        }

        // todo destination

        if (this.express){
            cost = cost *1.2;
        }
        cost = cost * 1-vat;


        System.out.println("Shipping cost: " + cost);
        return cost;
    }


    /**
     * Checks if the original packet size is smaller than or equal to the checking packet size.
     *
     * @param originalPacket The original packet to be checked.
     * @param checkingPacket The packet used for comparison.
     * @return {@code true} if the original packet size is smaller than or equal
     * to the checking packet size, otherwise {@code false}.
     */
    private boolean checkPacketSize (Packet originalPacket, Packet checkingPacket){
        return (originalPacket.height <= checkingPacket.height)
                && (originalPacket.width <= checkingPacket.width)
                && (originalPacket.length <= checkingPacket.length)
                && (originalPacket.weight <= checkingPacket.weight);
    }
}
