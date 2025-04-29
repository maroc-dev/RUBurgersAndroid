package model;

/**
 * Enum for the Flavors.
 *
 * @author Marco Ferreira
 * @author Sathish Kosalram
 */
public enum Flavor {
    COLA,
    LEMONADE,
    ROOT_BEER,
    TEA,
    SPRITE,
    CHERRY,
    JUICE,
    BLUE_RASPBERRY,
    MANGO,
    STRAWBERRY,
    PEACH,
    WATERMELON,
    LIME,
    VANILLA,
    PINEAPPLE;

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", " ").replaceFirst(".", name().charAt(0) + "");
    }
}
