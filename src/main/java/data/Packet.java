package data;

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
		this.combinedDimensions = length + 2*width + 2*height;
	}
	
}
