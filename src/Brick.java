import java.awt.*;

/**
 * שǽ�ࣨ�ӵ��ɴ򴩣�
 */
public class Brick extends Wall {
    public static final String type = "brick";
    GameFrame tc;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] wallImags = null;

    static {
        wallImags = new Image[]{ // ����commonWall��ͼƬ
                tk.getImage(Brick.class.getResource("Images/commonWall.gif")),};
    }

    public Brick(int x, int y, GameFrame tc) { // ���캯��
        this.x = x;
        this.y = y;
        this.tc = tc; // ��ý������
    }

    @Override
    public void draw(Graphics g) {// ��commonWall
        g.drawImage(wallImags[0], x, y, null);
    }

    @Override
    public String getType() {
        return type;
    }
}
