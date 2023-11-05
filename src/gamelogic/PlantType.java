package gamelogic;

public enum PlantType {
    NONE, APPLE, GRAPE, BANANA, PINEAPPLE;

    public static PlantType convertToPlantType(String name) {
        switch(name) {
            case "apple":
                return PlantType.APPLE;
            case "grape":
                return PlantType.GRAPE;
            case "banana":
                return PlantType.BANANA;
            case "pineapple":
                return PlantType.PINEAPPLE;
            case "none":
                return PlantType.NONE;
            default:
                return null;
        }
    } 

    public static String convertToString(PlantType type) {
        switch(type) {
            case APPLE:
                return "apple";
            case GRAPE:
                return "grape";
            case BANANA:
                return "banana";
            case PINEAPPLE:
                return "pineapple";
            case NONE:
                return "none";
            default:
                return null;
        }
    }
}
