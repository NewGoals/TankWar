import java.awt.*;

public abstract class Wall {
    public static final int BRICK_SIZE = Property.getInt("game.brickWall.size");
    public static final int METAL_SIZE = Property.getInt("game.metalWall.size");
    int x, y;

    public abstract void draw(Graphics g);

    public Rectangle getRect() { // 构造指定参数的长方形实例
        if (getType().equals("brick")){
            return new Rectangle(x, y, BRICK_SIZE, BRICK_SIZE);
        }
        if (getType().equals("metal")) {
            return new Rectangle(x, y, METAL_SIZE, METAL_SIZE);
        }
        throw new IllegalArgumentException("[" + getType() + "] is unknown!");
    }

    public abstract String getType();
}
