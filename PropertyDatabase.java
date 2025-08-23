import java.util.*;

public class PropertyDatabase implements Searchable<Property> {
    private List<Property> properties;
    private Map<String, List<Property>> byType = new HashMap<>();
    private Map<String, List<Property>> byLocation = new HashMap<>();

    public PropertyDatabase() {
        this.properties = new ArrayList<>();
    }

    public void addProperty(Property property) {
        this.properties.add(property);
        index(property);
    }

    private void index(Property property) {
        // index by type
        String t = property.getType().toLowerCase();
        byType.computeIfAbsent(t, k -> new ArrayList<>()).add(property);
        // index by location
        byLocation.computeIfAbsent(property.getLocation(), k -> new ArrayList<>()).add(property);
    }

    public List<Property> getProperties() { return properties; }

    // Get sorted unique properties by type
    public List<String> getTypes() {
        return new ArrayList<>(new TreeSet<>(byType.keySet()));
    }

    // Get properties by type
    public List<Property> getByType(String type) {
        return byType.getOrDefault(type.toLowerCase(), Collections.emptyList());
    }

    @Override
    // Searches for the first property that matches the keyword in its location
    public Property search(String keyword) {
        String lowerKeyword = keyword.toLowerCase();

        for (Property property : properties) {
            if (property.matchesByLocation(lowerKeyword)) return property;
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
