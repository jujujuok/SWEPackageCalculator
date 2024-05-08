package gui;

import control.Calculator;
import data.Utils;
import data.Packet;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

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
    public CalculatorArea() {
        this.setPadding(new Insets(10, 10, 10, 10));

        // input fields
        TextField lengthTextField = new TextField();
        TextField widthTextField = new TextField();
        TextField heightTextField = new TextField();
        TextField weightTextField = new TextField();

        // output label
        Label shippingCostLabel = new Label("?");

        // buttons
        Button calcButton = new Button("Calculate");

        // shipping Group
        ToggleGroup shippingGroup  = new ToggleGroup();
        RadioButton dhlButton = new RadioButton("DHL");
        dhlButton.setToggleGroup(shippingGroup);
        dhlButton.setSelected(true);
        RadioButton hermesButton = new RadioButton("HERMES");
        hermesButton.setToggleGroup(shippingGroup);

        // destination Group
        ToggleGroup destinationGroup = new ToggleGroup();
        RadioButton germanyButton = new RadioButton("Deutschland");
        germanyButton.setToggleGroup(destinationGroup);
        RadioButton europeButton = new RadioButton("Europa");
        europeButton.setToggleGroup(destinationGroup);
        RadioButton worldButton = new RadioButton("Welt");
        worldButton.setToggleGroup(destinationGroup);

        // expressversand
        CheckBox expressCheckBox = new CheckBox("Expressversand");

        // vat
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

        // add everything to the UI
        int row = 0;

        this.add(dhlButton, 1, row);
        this.add(hermesButton, 2, row);

        row = row+1;
        this.add(germanyButton, 1, row);
        this.add(europeButton, 2,  row);
        this.add(worldButton, 3,   row);

        row = row+1;
        this.add(expressCheckBox, 1, row);

        row = row+1;
        this.add(vatCheckBox, 1, row);
        this.add(vat7Button, 2,  row);
        this.add(vat19Button, 3, row);

        // ROW 4 - packet size
        row = row+1;
        this.add(new Label("Length: "), 0, row+1);
        this.add(new Label("Width:  "), 0, row+2);
        this.add(new Label("Height: "), 0, row+3);
        this.add(new Label("Weight: "), 0, row+4);

        // add input fields
        this.add(lengthTextField, 1, row+1);
        this.add(widthTextField,  1, row+2);
        this.add(heightTextField, 1, row+3);
        this.add(weightTextField, 1, row+4);

        // add labels for units
        this.add(new Label("mm"), 2, row+1);
        this.add(new Label("mm"), 2, row+2);
        this.add(new Label("mm"), 2, row+3);
        this.add(new Label("g"), 2,  row+4);

        row = row+6;

        // add shipping cost calculation line
        this.add(new Label("Shipping Costs: "), 1, row);
        this.add(shippingCostLabel, 2, row);
        this.add(calcButton, 3, row);

        calcButton.setOnAction(ae -> {
            try {
                int length = Integer.parseInt(lengthTextField.getText());
                int width = Integer.parseInt(widthTextField.getText());
                int height = Integer.parseInt(heightTextField.getText());
                int weight = Integer.parseInt(weightTextField.getText());

                Calculator calculator = new Calculator();

                Packet packet = new Packet(length, width, height, weight);

                // vat
                if (vatCheckBox.isSelected()) {
                    if (vat7Button.isSelected()) {
                        calculator.setVat(7);
                    } else if (vat19Button.isSelected()) {
                        calculator.setVat(19);
                    } else {
                        calculator.setVat(0);
                    }
                }

                // express
                calculator.setExpress(expressCheckBox.isSelected());

                // shipping group
                if (dhlButton.isSelected()) {
                    calculator.setShippingChoice(Utils.Company.DHL);
                }else if (hermesButton.isSelected()) {
                    calculator.setShippingChoice(Utils.Company.HERMES);
                }

                // destination
                if (worldButton.isSelected()) {
                    calculator.setDestination(Utils.Destination.WORLD);
                }else if (europeButton.isSelected()) {
                    calculator.setDestination(Utils.Destination.EU);
                } else {
                    calculator.setDestination(Utils.Destination.GERMANY);
                }

                // calculate
                double costs = calculator.calcShippingCosts(packet);

                shippingCostLabel.setText(Double.toString(costs));
            } catch (NumberFormatException e) {
                // Handle non-integer input
                shippingCostLabel.setText("Invalid input");
            }
        });

    }
}
