package AccessFromBothSides;

public enum EnumCategories {
    FILM (11, "Film"),
    GEOGRAPHY (22, "Geography"),
    POLITICS (24, "Politics"),
    SPORTS (21, "Sports"),
    MYTHOLOGY (20, "Mythology"),
    VEHICLES (28, "Vehicles");

    private final int value;
    private final String text;

    EnumCategories(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return Integer.toString(value);
    }
}
