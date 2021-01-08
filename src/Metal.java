import java.awt.*;

public class Metal extends Wall{
    GameFrame tc;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] wallImags = null;
    static {
        wallImags = new Image[] { // ����commonWall��ͼƬ
                tk.getImage(BrickWall.class.getResource("Images/metalWall.gif")), };
    }

    public Metal(int x, int y, GameFrame tc) { // ���캯��
        this.x = x;
        this.y = y;
        this.tc = tc; // ��ý������
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(wallImags[0], x, y, null);
    }
}
