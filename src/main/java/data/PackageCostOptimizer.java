package data;

import control.Calculator;
import gui.PackageCalculator;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.List;

/**
 * This function calculates if possible a rotation, so that a package fits into another package
 * */
public class PackageCostOptimizer {

    private final Vector3D xAxis = new Vector3D(1, 0, 0);
    private final Vector3D yAxis = new Vector3D(0, 1, 0);
    private final Vector3D zAxis = new Vector3D(0, 0, 1);
    private final Calculator calculator = new Calculator();

    private final ArrayList<Vector3D> startMeasurements = new ArrayList<>(3);
    private int weight;

    public PackageCostOptimizer(Packet packet) {
        startMeasurements.add(new Vector3D(packet.length, 0, 0));
        startMeasurements.add(new Vector3D(0, packet.width, 0));
        startMeasurements.add(new Vector3D(0, 0, packet.height));
        weight = packet.weight;
    }

    public double optimizeCosts() {
        final double degreesToRotate = 2 * Math.PI;
        final double steps = 20;
        double minCosts = Double.MAX_VALUE;
        Vector3D bestRotation = new Vector3D(0, 0, 0);

        for (double x = 0; x < degreesToRotate; x += degreesToRotate / steps) {
            ArrayList<Vector3D> xRotatedMeasurements = rotateAroundAxis(xAxis, x, startMeasurements);
            for (double y = 0; y < degreesToRotate; y += degreesToRotate / steps) {
                ArrayList<Vector3D> yRotatedMeasurements = rotateAroundAxis(yAxis, y, xRotatedMeasurements);
                for (double z = 0; z < degreesToRotate; z += degreesToRotate / steps) {
                    ArrayList<Vector3D> zRotatedMeasurements = rotateAroundAxis(zAxis, z, yRotatedMeasurements);

                    ArrayList<Double> dimensions = calculatePackageDimensions(zRotatedMeasurements);

                    double costs = calculator.calcShippingCosts(new Packet(
                            (int) (Math.ceil(dimensions.get(0))),
                            (int) (Math.ceil(dimensions.get(1))),
                            (int) (Math.ceil(dimensions.get(2))), weight),
                            false,
                            0
                    );
                    if (costs < minCosts) {
                        minCosts = costs;
                        bestRotation = new Vector3D(x, y, z);
                    }
                }
            }
        }
        PackageCalculator.getInstance().messagesArea.setMessage("Best rotation: " + bestRotation);
        return minCosts;
    }

    private ArrayList<Vector3D> rotateAroundAxis(Vector3D axis, double degrees, ArrayList<Vector3D> measurements) {
        Rotation rotation = new Rotation(axis, degrees, RotationConvention.VECTOR_OPERATOR);
        ArrayList<Vector3D> rotatedMeasurements = new ArrayList<>(3);
        for (Vector3D measurement : measurements) {
            rotatedMeasurements.add(rotation.applyTo(measurement));
        }

        return rotatedMeasurements;
    }

    private ArrayList<Double> calculatePackageDimensions(ArrayList<Vector3D> measurements) {
        measurements = getAllVectors(measurements);

        measurements = moveIntoPositive(measurements);

        return getMaxPoints(measurements);
    }

    private ArrayList<Vector3D> getAllVectors(ArrayList<Vector3D> measurements) {
        ArrayList<Vector3D> points = new ArrayList<>(8);
        points.add(new Vector3D(0, 0, 0));
        for (int i = 0; i < 3; i++) {
            points.add(measurements.get(i));
        }
        points.add(measurements.get(0).add(measurements.get(1)));
        points.add(measurements.get(0).add(measurements.get(2)));
        points.add(measurements.get(1).add(measurements.get(2)));
        points.add(measurements.get(0).add(measurements.get(1).add(measurements.get(2))));

        return points;
    }

    private ArrayList<Vector3D> moveIntoPositive(ArrayList<Vector3D> measurements) {
        for (Vector3D measurement : measurements) {
            if (measurement.getX() < 0) {
                for (int i = 0; i < measurements.size(); i++) {
                    measurements.set(i, measurements.get(i).add(new Vector3D(Math.abs(measurement.getX()), 0, 0)));
                }
            }

            if (measurement.getY() < 0) {
                for (int i = 0; i < measurements.size(); i++) {
                    measurements.set(i, measurements.get(i).add( new Vector3D(0, Math.abs(measurement.getY()), 0)));
                }
            }

            if (measurement.getZ() < 0) {
                for (int i = 0; i < measurements.size(); i++) {
                    measurements.set(i, measurements.get(i).add(new Vector3D(0,0, Math.abs(measurement.getZ()))));
                }
            }
        }
        return measurements;
    }

    private ArrayList<Double> getMaxPoints(ArrayList<Vector3D> points) {
        double maxLength = 0;
        double maxWidth = 0;
        double maxHeight = 0;
        for (Vector3D point : points) {
            if (point.getX() > maxLength) {
                maxLength = point.getX();
            }
            if (point.getY() > maxWidth) {
                maxWidth = point.getY();
            }
            if (point.getZ() > maxHeight) {
                maxHeight = point.getZ();
            }
        }
        return new ArrayList<>(List.of(maxLength, maxWidth, maxHeight));
    }

}