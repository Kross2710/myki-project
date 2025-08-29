import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class BookingManager {

    private static boolean isCancelled = false;

    public static void handleBooking(Property property) {
        Scanner scnr = Melbnb.scnr;
        isCancelled = false;
        String checkInDate = "";
        String checkOutDate = "";

        System.out.print(Melbnb.RESET);
        Melbnb.pageBreak();
        System.out.println("> Provide dates");
        Melbnb.pageBreak();
        while (true) {
            System.out.print("Please provide check-in date (DD/MM/YYYY): " + Melbnb.GREEN);
            checkInDate = scnr.nextLine().trim();
            System.out.print(Melbnb.RESET);
            if (!checkDateFormat(checkInDate)) {
                System.out.println("Invalid date format. Please try again.");
                continue; // Restart the loop for valid date input
            }

            System.out.print("Please provide check-out date (DD/MM/YYYY): " + Melbnb.GREEN);
            checkOutDate = scnr.nextLine().trim();
            System.out.print(Melbnb.RESET);
            if (!checkDateFormat(checkOutDate)) {
                System.out.println("Invalid date format. Please try again.");
                continue; // Restart the loop for valid date input
            }

            if (checkInDate.equals(checkOutDate)) {
                System.out.println("Check-in and check-out dates cannot be the same. Please try again.");
                continue; // Restart the loop for valid date input
            }
            break; // Exit the loop after valid dates are provided
        }
        System.out.print(Melbnb.RESET);
        int totalNights = calculateTotalNights(checkInDate, checkOutDate);
        Melbnb.pageBreak();
        System.out.println("> Show property details");
        Melbnb.pageBreak();
        System.out.printf("%-22s%-22s\n", "Property:", property.getName() + " hosted");
        System.out.printf("%-22s%-22s\n", "", "by " + property.getHostName());
        System.out.printf("%-22s%-22s\n", "Type of place:", property.getType());
        System.out.printf("%-22s%-22s\n", "Location:", property.getLocation());
        System.out.printf("%-22s%.2f\n", "Rating:", property.getRating());
        String desc = property.getDescription();
        String[] words = desc.split(" ");
        StringBuilder lineBuilder = new StringBuilder();
        boolean firstLine = true;

        for (String word : words) {
            if (lineBuilder.length() + word.length() + 1 > 55) { // 55 characters per line
                // Print the current line
                if (firstLine) {
                    System.out.printf("%-22s%s\n", "Description:", lineBuilder.toString().trim());
                    firstLine = false;
                } else {
                    System.out.printf("%-22s%s\n", "", lineBuilder.toString().trim());
                }
                lineBuilder.setLength(0); // reset
            }
            lineBuilder.append(word).append(" ");
        }

        // Print the last line if there is leftover content
        if (lineBuilder.length() > 0) {
            if (firstLine) {
                System.out.printf("%-22s%s\n", "Description:", lineBuilder.toString().trim());
            } else {
                System.out.printf("%-22s%s\n", "", lineBuilder.toString().trim());
            }
        }

        System.out.printf("%-22s%-22d\n", "Number of guests:", property.getMaxGuests());
        System.out.printf("%-22s$%.2f ($%.2f * %d nights)\n",
                "Price per night:", property.getPricePerNight() * totalNights, property.getPricePerNight(),
                totalNights);
        double discountedPerNight = property.getPricePerNight() * (1 - property.getWeeklyDiscount() / 100.0);
        System.out.printf("%-22s$%.2f ($%.2f * %d nights)\n",
                "Discounted price:", discountedPerNight * totalNights, discountedPerNight, totalNights);
        System.out.printf("%-22s$%.2f ($%.2f * %d nights)\n",
                "Service fee:", property.getServiceFeePerNight() * totalNights, property.getServiceFeePerNight(),
                totalNights);
        System.out.printf("%-22s$%.2f\n", "Cleaning fee:", property.getCleaningFee());
        System.out.printf("%-22s$%.2f\n", "Total:", property.calculateBookingFee(totalNights));
        while (true) {
            System.out.print("Would you like to reserve the property (Y/N)? " + Melbnb.GREEN);
            String reserveChoice = scnr.nextLine().toUpperCase();
            System.out.print(Melbnb.RESET);
            if (BookingManager.isYes(reserveChoice)) {
                Melbnb.pageBreak();
                System.out.println("> Provide personal information");
                Melbnb.pageBreak();
                System.out.print("Please provide your given name: " + Melbnb.GREEN);
                String givenName = scnr.nextLine().trim();
                System.out.print(Melbnb.RESET + "Please provide your surname: " + Melbnb.GREEN);
                String surname = scnr.nextLine().trim();
                System.out.print(Melbnb.RESET + "Please provide your email address: " + Melbnb.GREEN);
                String email = scnr.nextLine().trim();
                String numberOfGuestsStr = "";
                int numberOfGuests;
                while (true) {
                    System.out.print(Melbnb.RESET + "Please provide number of guests: " + Melbnb.GREEN);
                    numberOfGuestsStr = scnr.nextLine().trim();
                    if (!numberOfGuestsStr.matches("\\d+")) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        continue;
                    } else if (Integer.parseInt(numberOfGuestsStr) <= 0) {
                        System.out.println("Number of guests must be greater than 0. Please try again.");
                        continue; // Restart the loop for valid number of guests input
                    } else if (Integer.parseInt(numberOfGuestsStr) > property.getMaxGuests()) {
                        System.out.println("Number of guests exceeds the maximum allowed (" + property.getMaxGuests()
                                + "). Please try again.");
                        continue; // Restart the loop for valid number of guests input
                    }

                    numberOfGuests = Integer.parseInt(numberOfGuestsStr);
                    break; // Valid input, exit the loop
                }

                while (true) {
                    System.out.print(Melbnb.RESET + "Confirm and pay (Y/N): " + Melbnb.GREEN);
                    String confirmPayment = scnr.nextLine().toUpperCase();
                    System.out.print(Melbnb.RESET);
                    if (BookingManager.isYes(confirmPayment)) {
                        Client client = new Client(givenName, surname, email, numberOfGuests);
                        printReceipt(client, property, checkInDate, checkOutDate, totalNights);
                        break; // Exit the loop after payment confirmation
                    } else if (BookingManager.isNo(confirmPayment)) {
                        Melbnb.pageBreak();
                        System.out.println("> Payment cancelled.");
                        System.out.println();
                        isCancelled = true;
                        break; // Exit the loop without payment
                    } else {
                        System.out.println("Invalid choice. Please enter Y or N.");
                    }
                }
                break; // Exit the loop after reservation
            } else if (BookingManager.isNo(reserveChoice)) {
                Melbnb.pageBreak();
                System.out.println("> Booking cancelled.");
                System.out.println();
                isCancelled = true;
                break; // Exit the loop without reservation
            } else {
                System.out.println("Invalid choice. Please enter Y or N.");
            }
        }
    }

    public static boolean isYes(String str) {
        return str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("y");
    }

    public static boolean isNo(String str) {
        return str.equalsIgnoreCase("no") || str.equalsIgnoreCase("n");
    }

    public static boolean checkDateFormat(String date) {
        return date.matches("\\d{1,2}/\\d{1,2}/\\d{4}");
    }

    public static boolean isCancelled() {
        return isCancelled;
    }

    public static int calculateTotalNights(String checkInDate, String checkOutDate) {
        String[] checkInParts = checkInDate.split("/");
        String[] checkOutParts = checkOutDate.split("/");

        int checkInDay = Integer.parseInt(checkInParts[0]);
        int checkInMonth = Integer.parseInt(checkInParts[1]);
        int checkInYear = Integer.parseInt(checkInParts[2]);

        int checkOutDay = Integer.parseInt(checkOutParts[0]);
        int checkOutMonth = Integer.parseInt(checkOutParts[1]);
        int checkOutYear = Integer.parseInt(checkOutParts[2]);

        // Simple calculation assuming all months have 30 days for this example
        return (checkOutYear - checkInYear) * 365 + (checkOutMonth - checkInMonth) * 30 + (checkOutDay - checkInDay);
    }

    private static void printReceipt(Client client, Property property,
            String checkInDate, String checkOutDate, int totalNights) {
        Melbnb.pageBreak();
        System.out.println("> Congratulations! Your trip is booked. A receipt has been sent to your email.");
        System.out.println("  Details of your trip are shown below.");
        System.out.println("  Your host will contact you before your trip. Enjoy your stay!");
        Melbnb.pageBreak();
        System.out.printf("%-22s%-22s\n", "Name:", client.getFullName());
        System.out.printf("%-22s%-22s\n", "Email:", client.getEmail());
        System.out.printf("%-22s%-22s\n", "Your stay:", property.getName());
        System.out.printf("%-22s%-22s\n", "", "by " + property.getHostName());
        System.out.printf("%-22s%-22d\n", "Who's coming:", client.getNumberOfGuests());
        System.out.printf("%-22s%-22s\n", "Check-in date:", checkInDate);
        System.out.printf("%-22s%-22s\n", "Check-out date:", checkOutDate);
        System.out.printf("%-22s$%.2f\n", "Total payment:", property.calculateBookingFee(totalNights));

        // this will later be a function
        try (FileWriter writer = new FileWriter("receipt.txt", false)) {
            writer.write("Congratulations! Your trip is booked. A receipt has been sent to your email.\n");
            writer.write("Details of your trip are shown below.\n");
            writer.write("Your host will contact you before your trip. Enjoy your stay!\n\n");
            writer.write(String.format("%-22s%-22s\n", "Name:", client.getFullName()));
            writer.write(String.format("%-22s%-22s\n", "Email:", client.getEmail()));
            writer.write(String.format("%-22s%-22s\n", "Your stay:", property.getName()));
            writer.write(String.format("%-22s%-22s\n", "", "by " + property.getHostName()));
            writer.write(String.format("%-22s%-22d\n", "Who's coming:", client.getNumberOfGuests()));
            writer.write(String.format("%-22s%-22s\n", "Check-in date:", checkInDate));
            writer.write(String.format("%-22s%-22s\n", "Check-out date:", checkOutDate));
            writer.write(String.format("%-22s$%.2f\n", "Total payment:", property.calculateBookingFee(totalNights)));
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error writing receipt to file: " + e.getMessage());
        }
    }
}
