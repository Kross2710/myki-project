public class Property {
    private String name;
    private String location;
    private String description;
    private String type;
    private String hostName;
    private int maxGuests;
    private double rating;
    private double pricePerNight;
    private double serviceFeePerNight;
    private double cleaningFee;
    private int weeklyDiscount;

    public Property(String name, String location, String description, String type, String hostName,
                            int maxGuests, double rating, double pricePerNight, double serviceFeePerNight,
                            double cleaningFee, int weeklyDiscount) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.type = type;
        this.hostName = hostName;
        this.maxGuests = maxGuests;
        this.rating = rating;
        this.pricePerNight = pricePerNight;
        this.serviceFeePerNight = serviceFeePerNight;
        this.cleaningFee = cleaningFee;
        this.weeklyDiscount = weeklyDiscount;
    }

    public String getName() {
        return name;
    }

    public String getHostName() {
        return hostName;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getWeeklyDiscount() {
        return weeklyDiscount;
    }

    public double getServiceFeePerNight() {
        return serviceFeePerNight;
    }

    public double getCleaningFee() {
        return cleaningFee;
    }

    public double calculateBookingFee(int totalNights) {
        return (pricePerNight * ((100.0 - weeklyDiscount) / 100.0) + serviceFeePerNight) * totalNights + cleaningFee;
    }

    public boolean matchesByLocation(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return name.toLowerCase().contains(lowerKeyword) ||
               location.toLowerCase().contains(lowerKeyword);
    }
}