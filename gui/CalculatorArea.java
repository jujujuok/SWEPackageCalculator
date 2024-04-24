package gui;

import control.Calculator;
import data.Packet;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CalculatorArea extends GridPane {

	// input fields
	TextField lengthTextField = new TextField();
	TextField widthTextField  = new TextField();
	TextField heightTextField = new TextField();
	TextField weightTextField = new TextField();
	
	// output label
	Label shippingCostLabel = new Label("?");
	
	// buttons
	Button calcButton = new Button("Calculate");
	
	private double calcShippingCosts() {
		// init
		Calculator calc = new Calculator();
		
		// get user input values
		int length = Integer.parseInt(lengthTextField.getText());
		int width = Integer.parseInt(widthTextField.getText());
		int height = Integer.parseInt(heightTextField.getText());
		int weight = Integer.parseInt(weightTextField.getText());
		
		// peform calculation
		Packet packet = new Packet(length, width, height, weight);
		Double costs = calc.calcShippingCosts(packet);
		
		// show result
		shippingCostLabel.setText(costs.toString());
		
		return costs;
	}
	
	// constructor
	public CalculatorArea() {
					
		// set standard distance between elements
		this.setPadding(new Insets(10, 10, 10, 10));
		
		// add decription labels
		this.add(new Label("Length: "), 1, 1);
		this.add(new Label("Width:  "), 1, 2);
		this.add(new Label("Height: "), 1, 3);
		this.add(new Label("Weight: "), 1, 4);

		// add input fields
		this.add(lengthTextField, 2, 1);
		this.add(widthTextField,  2, 2);
		this.add(heightTextField, 2, 3);
		this.add(weightTextField, 2, 4);

		// add labels for units
		this.add(new Label("mm"), 3, 1);
		this.add(new Label("mm"), 3, 2);
		this.add(new Label("mm"), 3, 3);
		this.add(new Label("g"), 3, 4);
				
		// add shipping cost calculation line
		this.add(new Label("Shipping Costs: "), 1, 5);
		this.add(shippingCostLabel, 2, 5);
		this.add(calcButton, 3, 5);
		
		// set action listeners
		calcButton.setOnAction(ae-> {calcShippingCosts();});
	}
}
