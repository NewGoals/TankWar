import java.awt.*;

public class Metal extends Wall {
    public static final String type = "metal";
    GameFrame tc;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] wallImags = null;

    static {
        // ����commonWall��ͼƬ
        wallImags = new Image[]{tk.getImage(Metal.class.getResource("Images/metalWall.gif")),};
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

    @Override
    public String getType() {
        return type;
    }
}
