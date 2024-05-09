package data;

import gui.PackageCalculator;

public class Packet {

	// length of package in millimeters
	public final int length;

	// width of package in millimeters
	public final int width;
	
	// height of package in millimeters
	public final int height;
	
	// weight of package in grams
	public final int weight;

	public final int combinedDimensions;

	// constructor
	public Packet(int length, int width, int height, int weight) {
		this.length = length;
		this.width = width;
		this.height = height;
		this.weight = weight;

		// calculate the size of the packet
		if (width <0 || height <0 || weight < 0|| length < 0){
			String error = "Dimensions must be greater than 0.";
			PackageCalculator.getInstance().messagesArea.setMessage(error);
			throw new AssertionError(error);
		}

		if (weight > 31500){
			String error = "Weight must be less than 31500.";
			PackageCalculator.getInstance().messagesArea.setMessage(error);
			throw new AssertionError(error);
		}

		if (length > 1200 || width > 600 || height > 600){
			String error = "Wrong dimensions";
			PackageCalculator.getInstance().messagesArea.setMessage(error);
			throw new AssertionError(error);
		}

		this.combinedDimensions = length + 2*width + 2*height;
	}
	
}
