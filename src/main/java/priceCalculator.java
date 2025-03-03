public class priceCalculator {
    private static final double minPrice = 400;

    public double calcPrice(Double distance, Size size, Boolean isFragile, WorkloadList workload) {
        if (distance > 30 && isFragile) {
            throw new IllegalArgumentException("We can't deliver fragile cargos for more than 30km");
        }
        double deliveryPrice = (distanceCalcPrice(distance) + sizeCalPrice(size) + Fragility(isFragile)) * Workload(workload);
        if (deliveryPrice < 400) {
            return minPrice;
        }
        return deliveryPrice;
    }

    public Integer distanceCalcPrice(double distance) {
        if (distance > 30) {
            return 300;
        } else if (distance <= 30 && distance > 10) {
            return 200;
        } else if (distance <= 10 && distance > 2) {
            return 100;
        } else if (distance > 0 && distance <= 2) {
            return 50;
        } else throw new IllegalArgumentException("Unknown distance value");
    }

    public enum Size {
        BIG,
        SMALL
    }

    public Integer sizeCalPrice(Size size) {
        return switch (size) {
            case BIG -> 200;
            case SMALL -> 100;
        };
    }

    public Integer Fragility(Boolean fragile) {
        if (fragile) {
            return 300;
        } else return 0;
    }

    public enum WorkloadList {
        HIGHEST_LOAD,
        VERY_HIGH_LOAD,
        HIGH_LOAD,
        NORMAL
    }

    public Double Workload(WorkloadList workload) {
        return switch (workload) {
            case HIGHEST_LOAD -> 1.6;
            case VERY_HIGH_LOAD -> 1.4;
            case HIGH_LOAD -> 1.2;
            case NORMAL -> 1.0;
        };
    }
}
