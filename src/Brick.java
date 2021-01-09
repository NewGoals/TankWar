import java.awt.*;

public class Brick extends Wall {
    GameFrame tc;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] wallImags = null;

    static {
        wallImags = new Image[]{ // 储存commonWall的图片
                tk.getImage(BrickWall.class.getResource("Images/commonWall.gif")),};
    }

    public Brick(int x, int y, GameFrame tc) { // 构造函数
        this.x = x;
        this.y = y;
        this.tc = tc; // 获得界面控制
    }

    @Override
    public void draw(Graphics g) {// 画commonWall
        g.drawImage(wallImags[0], x, y, null);
    }
}
