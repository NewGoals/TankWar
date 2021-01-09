import java.awt.*;

/**
 * 砖墙类（子弹可打穿）
 */
public class BrickWall {
    public static final int width = Property.getInt("game.brick.size"); // 设置墙的固定参数
    public static final int length = Property.getInt("game.brick.size");
    int x, y;

    GameFrame tc;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] wallImags = null;

    static {
        // 储存commonWall的图片
        wallImags = new Image[]{tk.getImage(BrickWall.class.getResource("Images/commonWall.gif")),};
    }

    public BrickWall(int x, int y, GameFrame tc) { // 构造函数
        this.x = x;
        this.y = y;
        this.tc = tc; // 获得界面控制
    }

    public void draw(Graphics g) {// 画commonWall
        g.drawImage(wallImags[0], x, y, null);
    }

    public Rectangle getRect() { // 构造指定参数的长方形实例
        return new Rectangle(x, y, width, length);
    }
}
