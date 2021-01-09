import java.awt.*;

public abstract class Wall {
    public static final int width = 20; // ����ǽ�Ĺ̶�����
    public static final int length = 20;
    int x, y;

    public abstract void draw(Graphics g);

    public Rectangle getRect() { // ����ָ�������ĳ�����ʵ��
        return new Rectangle(x, y, width, length);
    }
}
