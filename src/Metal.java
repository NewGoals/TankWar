import java.awt.*;

public class Metal extends Wall {
    GameFrame tc;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] wallImags = null;

    static {
        // 储存commonWall的图片
        wallImags = new Image[]{tk.getImage(BrickWall.class.getResource("Images/metalWall.gif")),};
    }

    public Metal(int x, int y, GameFrame tc) { // 构造函数
        this.x = x;
        this.y = y;
        this.tc = tc; // 获得界面控制
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(wallImags[0], x, y, null);
    }
}
