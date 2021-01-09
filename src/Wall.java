import java.awt.*;

public abstract class Wall {
    public static final int width = Property.getInt("game.wall.size");
    public static final int length = Property.getInt("game.wall.size");
    int x, y;

    public abstract void draw(Graphics g);

    public Rectangle getRect() { // 构造指定参数的长方形实例
        return new Rectangle(x, y, width, length);
    }
}
