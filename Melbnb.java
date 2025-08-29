import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Melbnb {

    // Constants for color codes
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";

    // Method to print a page break
    public static void pageBreak() {
        System.out.println(RESET + "--------------------------------------------------------------------------------");
    }

    // Initiate Scanner
    public static final Scanner scnr = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        String csvFilePath = "Melbnb.csv";
        String csvSplitBy = ",";

        PropertyDatabase propertyDatabase = new PropertyDatabase();

        // Use BufferedReader to read all chunks at once
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            int lineCount = 0;

            while ((line = br.readLine()) != null) {
                lineCount++;

                if (lineCount <= 1) {
                    continue; // Skip the header line
                }

                String[] propertyData = line.split(csvSplitBy);

                if (propertyData.length == 11) {
                    String name = propertyData[0].trim();
                    String location = propertyData[1].trim();
                    String description = propertyData[2].trim();
                    String type = propertyData[3].trim();
                    String hostName = propertyData[4].trim();
                    int maxGuests = Integer.parseInt(propertyData[5].trim());
                    double rating = Double.parseDouble(propertyData[6].trim());
                    double pricePerNight = Double.parseDouble(propertyData[7].trim());
                    double serviceFeePerNight = Double.parseDouble(propertyData[8].trim());
                    double cleaningFee = Double.parseDouble(propertyData[9].trim());
                    int weeklyDiscount = Integer.parseInt(propertyData[10].trim());

                    Property property = new Property(name, location, description, type, hostName,
                            maxGuests, rating, pricePerNight, serviceFeePerNight, cleaningFee, weeklyDiscount);

                    propertyDatabase.addProperty(property);

                } else {
                    System.out.println("Invalid data format in line: " + line);
                }
            }
        } catch (IOException e) {
            // ...
        }

        // Debugging line to check if properties are loaded correctly
        // System.out.println("DEBUG: Loaded " + propertyDatabase.getProperties().size()
        // + " properties from CSV file.");

        // Options for users to choose to search for properties
        String[] options = {
                "Search by location",
                "Browse by type of place",
                "Filter by rating",
                "Exit"
        };

        boolean running = true; // Loop value for main app
        
        // Main loop for the Melbnb application
        while (running) {
            System.out.println(RESET + "Welcome to Melbnb!");
            pageBreak();
            System.out.println("> Select from main menu");
            pageBreak();
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ") " + options[i]);
            }

            String choice = "";  // try catch
            int validChoice;
            while (true) {
                System.out.print(RESET + "Please select: " + GREEN);
                choice = scnr.nextLine();
                if (isNumber(choice) && isValidChoice(Integer.parseInt(choice), options.length)) {
                    validChoice = Integer.parseInt(choice);
                    break; // Valid choice, exit the loop
                } else {
                    System.out.println(RESET + "Invalid selection. Please try again.");
                }
            }

            switch (validChoice) {
                case 1:
                    while (true) {
                        System.out.print(RESET + "Please provide a location: " + GREEN);
                        String location = scnr.nextLine(); // try catch
                        System.out.print(RESET);
                        pageBreak();
                        System.out.println("> Select from matching list");
                        pageBreak();

                        List<Property> matchedProperties = propertyDatabase.searchAll(location);
                        int count = 1;

                        for (Property property : matchedProperties) {
                            System.out.println(count + ") " + property.getName());
                            count++;
                        }
                        if (matchedProperties.isEmpty()) {
                            System.out.println(RESET + "No properties found in " + location + ".");
                        }

                        System.out.println(count + ") Back to main menu");
                        System.out.print(RESET + "Please select: " + GREEN);
                        String subChoice = scnr.nextLine(); // try catch
                        int validSubChoice;
                        if (isNumber(subChoice) && isValidChoice(Integer.parseInt(subChoice), count)) {
                            validSubChoice = Integer.parseInt(subChoice);
                        } else {
                            System.out.println(RESET + "Invalid selection. Please try again.");
                            continue; // Restart the loop for location search
                        }

                        if (validSubChoice == count) {
                            break; // Go back to the main menu
                        } else if (validSubChoice > 0 && validSubChoice < count) {
                            Property selectedProperty = matchedProperties.get(validSubChoice - 1);
                            BookingManager.handleBooking(selectedProperty);
                            if (BookingManager.isCancelled()) {
                                break;
                            }
                            running = false;
                            break;
                        } else {
                            System.out.println("Invalid input. Please try again.");
                        }
                    }
                    break;

                case 2:
                    while (true) {
                        pageBreak();
                        System.out.println("> Browse by type of place");
                        pageBreak();

                        List<String> types = propertyDatabase.getTypes(); // check best type to store data yet?
                        int typeCount = types.size() + 1; // +1 for "Back to main menu"
                        for (int i = 0; i < types.size(); i++) {
                            String capitalizedType = types.get(i).substring(0, 1).toUpperCase()
                                    + types.get(i).substring(1); // Capitalize type names
                            System.out.println(RESET + (i + 1) + ") " + capitalizedType);
                        }
                        System.out.println(RESET + (types.size() + 1) + ") Back to main menu");

                        System.out.print(RESET + "Please select: " + GREEN);
                        String subChoice = scnr.nextLine(); // try catch
                        int validSubChoice;
                        if (isNumber(subChoice) && isValidChoice(Integer.parseInt(subChoice), typeCount)) {
                            validSubChoice = Integer.parseInt(subChoice);
                        } else {
                            System.out.println(RESET + "Invalid selection. Please try again.");
                            continue; // Restart the loop for location search
                        }

                        if (validSubChoice == typeCount) {
                            break; // Go back to the main menu
                        } else if (validSubChoice > 0 && validSubChoice < typeCount) {
                            while (true) {
                                String selectedType = types.get(validSubChoice - 1);
                                // Return matched properties for the selected type
                                List<Property> matchedType = propertyDatabase.getByType(selectedType);

                                for (int i = 0; i < matchedType.size(); i++) {
                                    System.out.println(RESET + (i + 1) + ") " + matchedType.get(i).getName());
                                }
                                System.out.println(RESET + (matchedType.size() + 1) + ") Back to main menu");
                                System.out.print(RESET + "Please select: " + GREEN);

                                String typeSubChoice = scnr.nextLine(); // try catch
                                int validTypeSubChoice;
                                if (isNumber(typeSubChoice)
                                        && isValidChoice(Integer.parseInt(typeSubChoice), matchedType.size() + 1)) {
                                    validTypeSubChoice = Integer.parseInt(typeSubChoice);
                                } else {
                                    System.out.println(RESET + "Invalid selection. Please try again.");
                                    continue; // Restart the loop for type selection
                                }

                                // Handle booking or going back
                                if (validTypeSubChoice == matchedType.size() + 1) {
                                    break; // Go back to the main menu
                                } else if (validTypeSubChoice > 0 && validTypeSubChoice <= matchedType.size()) {
                                    Property selectedProperty = matchedType.get(validTypeSubChoice - 1);
                                    BookingManager.handleBooking(selectedProperty);
                                    running = false; // Exit after booking
                                    break;
                                }
                            }
                            break; // Exit the type browsing loop
                        }
                    }
                    break;

                case 3:
                    while (true) {
                        pageBreak();
                        System.out.println("> Filter by rating");
                        pageBreak();
                        System.out.print(RESET + "Please provide a minimum rating (0 - 5): " + GREEN);
                        String minRatingStr = scnr.nextLine().trim(); // try catch
                        System.out.print(RESET);

                        if (!isDouble(minRatingStr)) {
                            System.out.println(RESET + "Invalid rating. Please try again.");
                            continue; // Restart the loop for valid rating input
                        }
                        double minRating = Double.parseDouble(minRatingStr);
                        if (minRating < 0 || minRating > 5) {
                            System.out.println(RESET + "Rating must be between 0 and 5. Please try again.");
                            continue; // Restart the loop for valid rating input
                        }

                        List<Property> filteredProperties = new ArrayList<>();
                        for (Property property : propertyDatabase.getProperties()) {
                            if (property.getRating() >= minRating) {
                                filteredProperties.add(property);
                            }
                        }

                        // Display filtered properties
                        if (filteredProperties.isEmpty()) {
                            System.out.println(
                                    RESET + "No properties found with a rating of " + minRating + " or higher.");
                        } else {
                            System.out.println(RESET + "Properties found:");
                            for (int i = 0; i < filteredProperties.size(); i++) {
                                System.out.println(RESET + (i + 1) + ") " + filteredProperties.get(i).getName() +
                                        " - Rating: " + filteredProperties.get(i).getRating());
                            }
                            System.out.println(RESET + (filteredProperties.size() + 1) + ") Back to main menu");
                            System.out.print(RESET + "Please select: " + GREEN);
                            String ratingChoiceStr = scnr.nextLine().trim(); // try catch
                            System.out.print(RESET);

                            if (!isNumber(ratingChoiceStr)) {
                                System.out.println(RESET + "Invalid selection. Please try again.");
                                continue; // Restart the loop for valid input
                            }
                            int ratingChoice = Integer.parseInt(ratingChoiceStr);

                            if (ratingChoice == filteredProperties.size() + 1) {
                                break; // Go back to the main menu
                            } else if (ratingChoice > 0 && ratingChoice <= filteredProperties.size()) {
                                Property selectedProperty = filteredProperties.get(ratingChoice - 1);
                                BookingManager.handleBooking(selectedProperty);
                                running = false; // Exit after booking
                                break;
                            } else {
                                System.out.println(RESET + "Invalid selection. Please try again.");
                            }
                        }
                    }
                    break;

                case 4:
                    running = false; // Exit the application
                    break;

            }
        }

        scnr.close();
    }

    public static boolean isValidChoice(int choice, int max) {
        return choice > 0 && choice <= max;
    }

    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
