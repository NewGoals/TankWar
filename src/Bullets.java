import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 子弹类
 */

public class Bullets {
    public static int speedX = Property.getInt("game.bullet.speed");
    public static int speedY = Property.getInt("game.bullet.speed"); // 子弹的全局静态速度

    public static final int width = Property.getInt("game.bullet.size");
    public static final int length = Property.getInt("game.bullet.size");

    private int x, y;
    Direction diretion;

    private boolean good;
    private boolean live = true;

    private GameFrame tc;

    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] bulletImages = null;
    private static Map<String, Image> imgs = new HashMap<String, Image>(); // 定义Map键值对，是不同方向对应不同的弹头

    static {
        bulletImages = new Image[]{ // 不同方向的子弹
                tk.getImage(Bullets.class.getClassLoader().getResource("Images/bulletL.gif")),
                tk.getImage(Bullets.class.getClassLoader().getResource("Images/bulletU.gif")),
                tk.getImage(Bullets.class.getClassLoader().getResource("Images/bulletR.gif")),
                tk.getImage(Bullets.class.getClassLoader().getResource("Images/bulletD.gif")),
        };

        imgs.put("L", bulletImages[0]); // 加入Map容器

        imgs.put("U", bulletImages[1]);

        imgs.put("R", bulletImages[2]);

        imgs.put("D", bulletImages[3]);

    }

    public Bullets(int x, int y, Direction dir) { // 构造函数1，传递位置和方向
        this.x = x;
        this.y = y;
        this.diretion = dir;
    }

    // 构造函数2，接受另外两个参数
    public Bullets(int x, int y, boolean good, Direction dir, GameFrame tc) {
        this(x, y, dir);
        this.good = good;
        this.tc = tc;
    }

    private void move() {
        switch (diretion) {
            case L:
                x -= speedX; // 子弹不断向左进攻
                break;

            case U: // 子弹不断向上进攻
                y -= speedY;
                break;

            case R:
                x += speedX; // 子弹不断向右进攻
                break;

            case D: // 子弹不断向下进攻
                y += speedY;
                break;

            case STOP: // 游戏暂停
                break;
        }

        if (x < 0 || y < 0 || x > GameFrame.FRAME_WIDTH || y > GameFrame.FRAME_LENGTH) {
            live = false;
        }
    }

    public void draw(Graphics g) {
        if (!live) {
            tc.bullets.remove(this);
            return;
        }

        switch (diretion) { // 选择不同方向的子弹
            case L:
                g.drawImage(imgs.get("L"), x, y, null);
                break;

            case U:
                g.drawImage(imgs.get("U"), x, y, null);
                break;

            case R:
                g.drawImage(imgs.get("R"), x, y, null);
                break;

            case D:
                g.drawImage(imgs.get("D"), x, y, null);
                break;

        }
        move(); // 调用子弹move()函数
    }

    public boolean isLive() { // 是否还活着
        return live;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, length);
    }

    public boolean hitTanks(List<Tank> tanks) {// 当子弹打到坦克时
        for (int i = 0; i < tanks.size(); i++) {
            if (hitTank(tanks.get(i))) { // 对每一辆坦克，调用hitTank
                return true;
            }
        }
        return false;
    }

    public boolean hitTank(Tank t) { // 当子弹打到坦克上
        if (this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {

            BombTank e = new BombTank(t.getX(), t.getY(), tc);
            tc.bombTanks.add(e);
            if (t.isGood()) {
                t.setLife(t.getLife() - 50); // 中一颗子弹寿命减少50，中4枪就死，总生命值200
                if (t.getLife() <= 0)
                    t.setLive(false); // 当寿命为0时，设置寿命为死亡状态
            } else {
                t.setLive(false);
            }
            this.live = false;
            return true; // 射击成功，返回true
        }
        return false; // 否则返回false
    }

    public boolean hitWall(Wall w) { // 子弹打到金属墙上
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.live = false;
            if (w.getType().equals("brick")){
                this.tc.otherWall.remove(w); // 子弹打到CommonWall墙上时则移除此击中墙
                this.tc.homeWall.remove(w);
            }
            return true;
        }
        return false;
    }

    public boolean hitHome() { // 当子弹打到大本营时
        if (this.live && this.getRect().intersects(tc.home.getRect())) {
            this.live = false;
            this.tc.home.setLive(false); // 当大本营接受一枪时就毁灭
            return true;
        }
        return false;
    }

}
