import java.awt.*;

public abstract class Wall {
    public static final int width = Property.getInt("game.wall.size");
    public static final int length = Property.getInt("game.wall.size");
    int x, y;

    public abstract void draw(Graphics g);

    public Rectangle getRect() { // ����ָ�������ĳ�����ʵ��
        return new Rectangle(x, y, width, length);
    }
}
