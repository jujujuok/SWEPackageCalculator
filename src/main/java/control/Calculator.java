package control;

import data.Utils;
import data.Packet;
import data.Importer;

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

    final Importer importer = new Importer(".\\data\\shippingCosts.csv");
    private List<Double> shippingCost;
    private int vat;
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
        this.shippingCost = importer.getPriceDHL();
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
            case HERMES -> this.shippingCost = importer.getPriceHermes();
            case DHL -> this.shippingCost = importer.getPriceDHL(); //todo duplicate
            default -> this.shippingCost = importer.getPriceDHL();
        }
    }


    public void setVat(final int vat){
        this.vat = vat;
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
     * @param pack The parcel object with attributes (length, width, height, weight).
     * @return The calculated shipping costs for the parcel.
     */
    public double calcShippingCosts(Packet pack) {
        // Generate standard packets for comparison
        Packet smallPacket = new Packet(300, 300, 150, 1000);
        Packet mediumPacket = new Packet(600, 300, 150, 2000);
        Packet largePacket = new Packet(1200, 600, 600, 5000);
        Packet verylargePacket = new Packet(1200, 600, 600, 10000);
        Packet defaultPacket = new Packet(1200, 600, 600, 31500);

        if (checkPacketSize(pack, smallPacket)) {
            return shippingCost.get(PacketSize.VerySmall.ordinal());
        }
        if (checkPacketSize(pack, mediumPacket)) {
            return shippingCost.get(PacketSize.Small.ordinal());
        }
        if (checkPacketSize(pack, largePacket) && pack.combinedDimensions <= 3000) {
            return shippingCost.get(PacketSize.Medium.ordinal());
        }
        if (checkPacketSize(pack, verylargePacket) && pack.combinedDimensions <= 3000) {
            return shippingCost.get(PacketSize.Large.ordinal());
        }
        if (checkPacketSize(pack, defaultPacket)) {
            return shippingCost.get(PacketSize.VeryLarge.ordinal());
        }
        return 0.0;
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
