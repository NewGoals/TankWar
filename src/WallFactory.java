public class WallFactory {

    public static Wall getWall(String type, int x, int y, GameFrame gameFrame) {
        switch (type) {
            case "brick":
                return new Brick(x, y, gameFrame);
            case "metal":
                return new Metal(x, y, gameFrame);
            default:
                throw new IllegalArgumentException("[" + type + "] is a unknown type wall!");
        }
    }

}
