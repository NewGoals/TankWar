import java.awt.*;

public abstract class Wall {
    public static final int width = 20; // 设置墙的固定参数
    public static final int length = 20;
    int x, y;

    public abstract void draw(Graphics g);

    public Rectangle getRect() { // 构造指定参数的长方形实例
        return new Rectangle(x, y, width, length);
    }
}
