package gui;

import control.Calculator;
import data.PackageCostOptimizer;
import data.Utils;
import data.Packet;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

/**
 * The CalculatorArea class represents a UI component for calculating shipping costs based on user input.
 * It contains input fields for length, width, height, and weight,
 * a label to display the shipping cost, and a button to trigger the calculation.
 * The calculation is done by a separate Calculator object.
 *
 * @see Calculator Used for performing the shipping cost calculation.
 * @see Packet Used to create a packet object with user-provided dimensions and weight.
 */

public class CalculatorArea extends GridPane {

    private Calculator calculator;

    public CalculatorArea() {
        this.setPadding(new Insets(10, 10, 10, 10));


        calculator = new Calculator();

        // UI „Guidance“
        Label companyLabel = new Label("Unternehmen");
        Label destinationLabel = new Label("Ziel");
        Label optionsLabel = new Label("Optionen");

        for (Label label : new Label[]{companyLabel, destinationLabel, optionsLabel}) {
            label.setStyle("-fx-font-weight: bold;"
            + "-fx-font-style: italic;"
            + "-fx-font-size: large;"
            );
        }

        // input fields
        TextField lengthTextField = new TextField();
        TextField widthTextField = new TextField();
        TextField heightTextField = new TextField();
        TextField weightTextField = new TextField();

        for (TextField textField : new TextField[]{lengthTextField, widthTextField, heightTextField, weightTextField}) {
            textField.setStyle("-fx-padding: 12 20 12 20;" +
                    "-fx-background-color: #ffffff;" +
                    "-fx-background-radius: 30;" +
                    "-fx-border-radius: 30;" +
                    "-fx-border-color: #cccccc;");

            textField.setMaxWidth(Double.MAX_VALUE);
        }
        // output label
        Label shippingCostLabel = new Label("?");
        shippingCostLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // buttons
        Button calcButton = new Button("Calculate");
        calcButton.setStyle("-fx-padding: 10 20 10 20;" +
                        "    -fx-background-radius: 30;" +
                        "    -fx-border-radius: 30;" +
                        "    -fx-text-fill: #ffffff;" +
                        "    -fx-background-color: #0078d7;" +
                        "    -fx-cursor: hand;");

        // shipping Group
        ToggleGroup shippingGroup  = new ToggleGroup();
        RadioButton dhlButton = new RadioButton("DHL");
        dhlButton.setToggleGroup(shippingGroup);
        dhlButton.setSelected(true);
        RadioButton hermesButton = new RadioButton("HERMES");
        hermesButton.setToggleGroup(shippingGroup);

        // DESTINATION
        ToggleGroup destinationGroup = new ToggleGroup();
        RadioButton germanyButton = new RadioButton("Deutschland");
        germanyButton.setToggleGroup(destinationGroup);
        RadioButton europeButton = new RadioButton("Europa");
        europeButton.setToggleGroup(destinationGroup);
        RadioButton worldButton = new RadioButton("Welt");
        worldButton.setToggleGroup(destinationGroup);

        // OPTIONS
        CheckBox expressCheckBox = new CheckBox("Expressversand");

        CheckBox vatCheckBox = new CheckBox("MwSt abziehen?");
        vatCheckBox.setSelected(false);

        ToggleGroup vatGroup = new ToggleGroup();
        RadioButton vat7Button = new RadioButton("7%");
        vat7Button.setVisible(false);
        vat7Button.setSelected(true);
        vat7Button.setToggleGroup(vatGroup);

        RadioButton vat19Button = new RadioButton("19%");
        vat19Button.setToggleGroup(vatGroup);
        vat19Button.setVisible(false);

        vatCheckBox.setOnAction(ae -> {
            vat7Button.setVisible(vatCheckBox.isSelected());
            vat19Button.setVisible(vatCheckBox.isSelected());
        });

        CheckBox optimize = new CheckBox("Optimieren?");


        // add everything to the UI
        int row = 0;

        Image image = new Image("file:gui/paket.png");
        ImageView iconView = new ImageView(image);
        iconView.setFitHeight(20);
        iconView.setFitWidth(20);

        this.add(iconView, 0,row++);

        this.add(companyLabel, 0, row++);

        row++;
        
        this.add(dhlButton, 1, row);
        this.add(hermesButton, 2, row);

        row++;
        this.add(new Label(" "), 0, row++);

        this.add(destinationLabel, 0, row);

        this.add(germanyButton, 1, row++);
        this.add(europeButton, 1,  row++);
        this.add(worldButton, 1,   row++);


        this.add(new Label(" "), 0, row++);


        this.add(optionsLabel, 0, row);

        row++;

        this.add(expressCheckBox, 1, row);

        row++;

        this.add(vatCheckBox, 1, row);
        this.add(vat7Button, 2,  row);
        this.add(vat19Button, 3, row);

        row++;

        this.add(optimize, 1, row);

        row++;

        this.add(new Label("Length: "), 0, row+1);
        this.add(new Label("Width:  "), 0, row+2);
        this.add(new Label("Height: "), 0, row+3);
        this.add(new Label("Weight: "), 0, row+4);

        this.add(lengthTextField, 1, row+1);
        this.add(widthTextField,  1, row+2);
        this.add(heightTextField, 1, row+3);
        this.add(weightTextField, 1, row+4);

        // add labels for units
        this.add(new Label("mm"), 2, row+1);
        this.add(new Label("mm"), 2, row+2);
        this.add(new Label("mm"), 2, row+3);
        this.add(new Label("g"), 2,  row+4);

        row +=6;

        // add shipping cost calculation line
        this.add(new Label("Shipping Costs: "), 1, row);
        this.add(shippingCostLabel, 2, row);

        row++;
        this.add(calcButton, 1, row++);

        this.getChildren().forEach(node -> {
            GridPane.setMargin(node, new Insets(2.5, 2.5, 2.5, 2.5));
        });

        calcButton.setOnAction(ae -> {
            try {
                int length = Integer.parseInt(lengthTextField.getText());
                int width = Integer.parseInt(widthTextField.getText());
                int height = Integer.parseInt(heightTextField.getText());
                int weight = Integer.parseInt(weightTextField.getText());

                if (length<0 || width<0 || height<0 || weight<0) {
                    shippingCostLabel.setText("Please enter a valid number");
                    return;
                }

                Packet packet = new Packet(length, width, height, weight);

                double vat = 0;
                if (vatCheckBox.isSelected()) {
                    if (vat7Button.isSelected()) {
                        vat = 0.07;
                    } else if (vat19Button.isSelected()) {
                        vat = 0.19;
                    } else {
                        vat = 0.0;
                    }
                }

                boolean express = expressCheckBox.isSelected();

                // shipping group
                if (dhlButton.isSelected()) {
                    this.calculator.setShippingChoice(Utils.Company.DHL);
                }else if (hermesButton.isSelected()) {
                    this.calculator.setShippingChoice(Utils.Company.HERMES);
                }

                double costs = 0;
                if (optimize.isSelected()) {
                    PackageCostOptimizer pco = new PackageCostOptimizer(packet);

                    costs = pco.optimizeCosts();
                } else {
                    costs = this.calculator.calcShippingCosts(packet, express, vat);
                }

                costs = Math.round(costs* 100) / 100.0;

                shippingCostLabel.setText("Preis: " + costs);

            } catch (NumberFormatException e) {
                // Handle non-integer input
                shippingCostLabel.setText("Falsche Eingabe.");
            }
        });

    }
}
