package data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ImportHandler {

    private final String path;
    private final List<Double> dhlPrices;
    private final List<Double> hermesPrices;

    public ImportHandler(String path){
        this.path = System.getProperty("user.dir") + "/src/main/java/"+path;
        this.dhlPrices = new ArrayList<>();
        this.hermesPrices = new ArrayList<>();
        this.importShippingCosts();
    }

    private void importShippingCosts() {
        System.out.println("Importing shipping costs...");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.path), StandardCharsets.UTF_8))) {
            String line = br.readLine();
            if (line != null) {
                String[] tokens = line.split(";");

                System.out.println(Arrays.toString(tokens));

                int i = 1;
                for (; !tokens[i].equals("Hermes"); i++) {
                    this.dhlPrices.add(Double.parseDouble(tokens[i]));
                }
                for (i++; i < tokens.length; i++) {
                    this.hermesPrices.add(Double.parseDouble(tokens[i]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Double> getPriceHermes() {
        return this.hermesPrices;
    }

    public List<Double> getPriceDHL() {
        return this.dhlPrices;
    }
}