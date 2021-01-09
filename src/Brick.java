import java.awt.*;

public class Brick extends Wall {
    GameFrame tc;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] wallImags = null;

    static {
        wallImags = new Image[]{ // ����commonWall��ͼƬ
                tk.getImage(BrickWall.class.getResource("Images/commonWall.gif")),};
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
}
