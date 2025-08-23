import java.util.*;

public class PropertyDatabase implements Searchable<Property> {
    private List<Property> properties;
    

    public PropertyDatabase() {
        this.properties = new ArrayList<>();
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    public List<Property> getProperties() {
        return properties;
    }

    @Override
    // Searches for the first property that matches the keyword in its location
    public Property search(String keyword) {
        String lowerKeyword = keyword.toLowerCase();

        for (Property property : properties) {
            if (property.matchesByLocation(lowerKeyword)) {
                return property;
            }
        }
        return null; // No matching property found
    }

    // Searches for all properties that match the keyword in their location
    public List<Property> searchAll(String keyword) {
        List<Property> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Property property : properties) {
            if (property.matchesByLocation(lowerKeyword)) {
                results.add(property);
            }
        }
        return results;
    }

    
}
