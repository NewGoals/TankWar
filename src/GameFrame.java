import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 坦克大战的主类
 */

public class GameFrame extends Frame implements ActionListener {

    public static final int FRAME_WIDTH = Property.getInt("window.width"); // 静态全局窗口大小
    public static final int FRAME_LENGTH = Property.getInt("window.height");
    public static boolean printable = true; // 记录暂停状态，此时线程不刷新界面
    private long startTime = System.currentTimeMillis();
    private long currentTime = startTime;
    MenuBar menuBar = null;
    Menu menu1 = null, menu2 = null, menu3 = null, menu4 = null;
    MenuItem menuItem1 = null, menuItem2 = null, menuItem3 = null, menuItem4 = null,
            menuItem5 = null, menuItem6 = null, menuItem7 = null, menuItem8 = null, menuItem9 = null;
    Image screenImage = null;

    Tank homeTank = new Tank(300, 560, true, Direction.STOP, this);// 实例化坦克
    Blood blood = new Blood(); // 实例化血包
    Home home = new Home(373, 545, this);// 实例化home

    // 以下集合变量在构造方法中进行了初始化
    List<River> theRiver = new ArrayList<River>();
    List<Tank> tanks = new ArrayList<Tank>();
    List<BombTank> bombTanks = new ArrayList<BombTank>();
    List<Bullets> bullets = new ArrayList<Bullets>();
    List<Tree> trees = new ArrayList<Tree>();
    List<Wall> homeWall = new ArrayList<>(); // 实例化对象容器
    List<Wall> otherWall = new ArrayList<>();
    List<Wall> metalWall = new ArrayList<>();

    // 这是一个重写的方法,将由repaint()方法自动调用
    public void update(Graphics g) {
        screenImage = this.createImage(FRAME_WIDTH, FRAME_LENGTH);
        Graphics gps = screenImage.getGraphics();
        Color c = gps.getColor();
        gps.setColor(Color.GRAY);
        gps.fillRect(0, 0, FRAME_WIDTH, FRAME_LENGTH);
        gps.setColor(c);
        framePaint(gps);
        g.drawImage(screenImage, 0, 0, null);
    }

    public void framePaint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.green); // 设置字体显示属性

        Font f1 = g.getFont();
        g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g.drawString("区域内还有敌方坦克: "+ tanks.size(), 50, 85);
        g.drawString("剩余生命值: " + homeTank.getLife(), 320, 85);
        g.drawString("游戏用时: " + ((currentTime - startTime) / 1000), 550, 85);
        g.setFont(f1);

        // 如果玩家赢了（条件是敌方坦克全灭、大本营健在、玩家坦克仍有血量）
        if (tanks.size() == 0 && home.isLive() && homeTank.isLive()) {
            Font f = g.getFont();
            g.setFont(new Font("TimesRoman", Font.BOLD, 60));
            this.otherWall.clear();
            g.drawString("你赢了！ ", 310, 300);
            g.setFont(f);
        }

        if (!homeTank.isLive()) {
            home.gameOver(g);
        } else {
            currentTime = System.currentTimeMillis();
        }
        g.setColor(c);

        for (int i = 0; i < theRiver.size(); i++) { // 画出河流
            River r = theRiver.get(i);
            r.draw(g);
        }

        for (int i = 0; i < theRiver.size(); i++) {    // 撞到河流，恢复原位
            River river = theRiver.get(i);
            homeTank.collideRiver(river);
            river.draw(g);
        }

        home.draw(g); // 画出home
        homeTank.draw(g); // 画出自己家的坦克
        homeTank.eat(blood);// 加血--生命值

        for (int i = 0; i < bullets.size(); i++) { // 对每一个子弹
            Bullets bullets = this.bullets.get(i);
            bullets.hitTanks(tanks); // 每一个子弹打到坦克上
            bullets.hitTank(homeTank); // 每一个子弹打到自己家的坦克上时
            bullets.hitHome(); // 每一个子弹打到家里时

            for (int j = 0; j < metalWall.size(); j++) { // 每一个子弹打到金属墙上
                bullets.hitWall(metalWall.get(j));
            }

            for (int j = 0; j < otherWall.size(); j++) {// 每一个子弹打到其他墙上
                bullets.hitWall(otherWall.get(j));
            }

            for (int j = 0; j < homeWall.size(); j++) {// 每一个子弹打到家的墙上
                bullets.hitWall(homeWall.get(j));
            }
            bullets.draw(g); // 画出效果图
        }

        // 画出每一辆敌方坦克
        for (int i = 0; i < tanks.size(); i++) {
            Tank tank = tanks.get(i); // 获得键值对的键

            for (int j = 0; j < homeWall.size(); j++) {
                Wall wall = homeWall.get(j);
                tank.collideWithWall(wall); // 每一个坦克撞到家里的墙时
                wall.draw(g);
            }
            for (int j = 0; j < otherWall.size(); j++) { // 每一个坦克撞到家以外的墙
                Wall wall = otherWall.get(j);
                tank.collideWithWall(wall);
                wall.draw(g);
            }
            for (int j = 0; j < metalWall.size(); j++) { // 每一个坦克撞到金属墙
                Wall wall = metalWall.get(j);
                tank.collideWithWall(wall);
                wall.draw(g);
            }
            for (int j = 0; j < theRiver.size(); j++) {
                River river = theRiver.get(j); // 每一个坦克撞到河流时
                tank.collideRiver(river);
                river.draw(g);
                // t.draw(g);
            }

            tank.collideWithTanks(tanks); // 撞到自己的人
            tank.collideHome(home);

            tank.draw(g);
        }

        blood.draw(g);// 画出加血包

        for (int i = 0; i < trees.size(); i++) { // 画出trees
            Tree tree = trees.get(i);
            tree.draw(g);
        }

        for (int i = 0; i < bombTanks.size(); i++) { // 画出爆炸效果
            BombTank bombTank = bombTanks.get(i);
            bombTank.draw(g);
        }

        for (int i = 0; i < otherWall.size(); i++) { // 画出otherWall
            Wall wall = otherWall.get(i);
            wall.draw(g);
        }

        for (int i = 0; i < metalWall.size(); i++) { // 画出metalWall
            Wall wall = metalWall.get(i);
            wall.draw(g);
        }

        homeTank.collideWithTanks(tanks);
        homeTank.collideHome(home);

        for (int i = 0; i < metalWall.size(); i++) {// 撞到金属墙
            Wall wall = metalWall.get(i);
            homeTank.collideWithWall(wall);
            wall.draw(g);
        }

        for (int i = 0; i < otherWall.size(); i++) {    // 画出砖墙
            Wall wall = otherWall.get(i);
            homeTank.collideWithWall(wall);
            wall.draw(g);
        }

        for (int i = 0; i < homeWall.size(); i++) { // 家里的坦克撞到自己家
            Wall wall = homeWall.get(i);
            homeTank.collideWithWall(wall);
            wall.draw(g);
        }

    }

    public GameFrame() {
        // printable = false;
        // 创建菜单及菜单选项
        menuBar = new MenuBar();
        menu1 = new Menu("游戏");
        menu2 = new Menu("暂停/继续");
        menu3 = new Menu("帮助");
        menu4 = new Menu("游戏级别");
        menu1.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体
        menu2.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体
        menu3.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体
        menu4.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体

        menuItem1 = new MenuItem("开始新游戏");
        menuItem2 = new MenuItem("退出");
        menuItem3 = new MenuItem("暂停");
        menuItem4 = new MenuItem("继续");
        menuItem5 = new MenuItem("游戏说明");
        menuItem6 = new MenuItem("级别1");
        menuItem7 = new MenuItem("级别2");
        menuItem8 = new MenuItem("级别3");
        menuItem9 = new MenuItem("级别4");
        menuItem1.setFont(new Font("TimesRoman", Font.BOLD, 15));
        menuItem2.setFont(new Font("TimesRoman", Font.BOLD, 15));
        menuItem3.setFont(new Font("TimesRoman", Font.BOLD, 15));
        menuItem4.setFont(new Font("TimesRoman", Font.BOLD, 15));
        menuItem5.setFont(new Font("TimesRoman", Font.BOLD, 15));

        menu1.add(menuItem1);
        menu1.add(menuItem2);
        menu2.add(menuItem3);
        menu2.add(menuItem4);
        menu3.add(menuItem5);
        menu4.add(menuItem6);
        menu4.add(menuItem7);
        menu4.add(menuItem8);
        menu4.add(menuItem9);

        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu4);
        menuBar.add(menu3);

        menuItem1.addActionListener(this);
        menuItem1.setActionCommand("NewGame");
        menuItem2.addActionListener(this);
        menuItem2.setActionCommand("Exit");
        menuItem3.addActionListener(this);
        menuItem3.setActionCommand("Stop");
        menuItem4.addActionListener(this);
        menuItem4.setActionCommand("Continue");
        menuItem5.addActionListener(this);
        menuItem5.setActionCommand("help");
        menuItem6.addActionListener(this);
        menuItem6.setActionCommand("level1");
        menuItem7.addActionListener(this);
        menuItem7.setActionCommand("level2");
        menuItem8.addActionListener(this);
        menuItem8.setActionCommand("level3");
        menuItem9.addActionListener(this);
        menuItem9.setActionCommand("level4");

        this.setMenuBar(menuBar);// 菜单Bar放到JFrame上

        for (int i = 0; i < MapProperty.getInt("game.home.initCount"); i++) { // 家的格局
            if (i < 4) {
                homeWall.add(WallFactory.getWall("brick",350, 580 - 21 * i, this));
            } else if (i < 7) {
                homeWall.add(WallFactory.getWall("brick",372 + 22 * (i - 4), 517, this));
            } else {
                homeWall.add(WallFactory.getWall("brick",416, 538 + (i - 7) * 21, this));
            }
        }

        for (int i = 0; i < MapProperty.getInt("game.brick.initCount"); i++) { // 砖墙
            if (i < 16) {
                otherWall.add(WallFactory.getWall("brick",220 + 20 * i, 300, this));
                otherWall.add(WallFactory.getWall("brick",500 + 20 * i, 180,  this));
                otherWall.add(WallFactory.getWall("brick",200, 400 + 20 * i, this));
                otherWall.add(WallFactory.getWall("brick",500, 400 + 20 * i,  this));
            } else if (i < 32) {
                otherWall.add(WallFactory.getWall("brick",220 + 20 * (i - 16), 320, this));
                otherWall.add(WallFactory.getWall("brick",500 + 20 * (i - 16), 220, this));
                otherWall.add(WallFactory.getWall("brick",220, 400 + 20 * (i - 16),  this));
                otherWall.add(WallFactory.getWall("brick",520, 400 + 20 * (i - 16), this));
            }
        }

        for (int i = 0; i < MapProperty.getInt("game.metal.initCount"); i++) { // 金属墙布局
            if (i < 10) {
                metalWall.add(WallFactory.getWall("metal",140 + 30 * i, 150, this));
                metalWall.add(WallFactory.getWall("metal",600, 400 + 20 * (i),this));
            } else if (i < 20) {
                metalWall.add(WallFactory.getWall("metal",140 + 30 * (i - 10), 180, this));
            } else {
                metalWall.add(WallFactory.getWall("metal",500 + 30 * (i - 10), 160, this));
            }
        }

        for (int i = 0; i < MapProperty.getInt("game.tree.initCount"); i++) { // 树的布局
            if (i < 4) {
                trees.add(new Tree(0 + 30 * i, 360, this));
                trees.add(new Tree(220 + 30 * i, 360, this));
                trees.add(new Tree(440 + 30 * i, 360, this));
                trees.add(new Tree(660 + 30 * i, 360, this));
            }
        }

        theRiver.add(new River(85, 100, this));

        for (int i = 0; i < Property.getInt("game.tank.initCount"); i++) { // 初始化20辆坦克, 设置坦克出现的位置
            if (i < 9) {
                tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this));
            } else if (i < 15) {
                tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D, this));
            } else {
                tanks.add(new Tank(10, 50 * (i - 12), false, Direction.D, this));
            }
        }

        this.setSize(FRAME_WIDTH, FRAME_LENGTH); // 设置界面大小
        setLocationRelativeTo(null);// 让窗体居中
        this.setTitle("坦克大战——(重新开始：R键  开火：F键)                 ");

        this.addWindowListener(new WindowAdapter() { // 窗口关闭监听
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        this.setResizable(false);
        this.setBackground(Color.GREEN);
        this.setVisible(true);

        this.addKeyListener(new KeyMonitor(this));// 键盘监听
        new Thread(new PaintThread()).start(); // 线程启动
    }

    public static void main(String[] args) {
        new GameFrame(); // 实例化
    }

    private class PaintThread implements Runnable {
        public void run() {
            while (printable) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class KeyMonitor extends KeyAdapter {

        private Frame frame;

        public KeyMonitor(Frame frame) {
            this.frame = frame;
        }

        public void keyReleased(KeyEvent e) { // 监听键盘释放
            homeTank.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) { // 监听键盘按下
            homeTank.keyPressed(e, frame);
        }
    }

    // 点击事件
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("NewGame")) {
            printable = false;
            Object[] options = {"确定", "取消"};
            int response = JOptionPane.showOptionDialog(this, "您确认要开始新游戏！", "", JOptionPane.YES_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (response == 0) {
                printable = true;
                this.dispose();
                new GameFrame();
            } else {
                printable = true;
                new Thread(new PaintThread()).start(); // 线程启动
            }
        } else if (e.getActionCommand().endsWith("Stop")) {// 暂停
            printable = false;
        } else if (e.getActionCommand().equals("Continue")) {// 继续
            if (!printable) {
                printable = true;
                new Thread(new PaintThread()).start(); // 线程启动
            }
        } else if (e.getActionCommand().equals("Exit")) {// 退出
            printable = false;
            Object[] options = {"确定", "取消"};
            int response = JOptionPane.showOptionDialog(this, "您确认要退出吗", "", JOptionPane.YES_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (response == 0) {
                System.out.println("退出");
                System.exit(0);
            } else {
                printable = true;
                new Thread(new PaintThread()).start(); // 线程启动
            }
        } else if (e.getActionCommand().equals("help")) {
            printable = false;
            JOptionPane.showMessageDialog(null, "用→ ← ↑ ↓控制方向，F键盘发射，R重新开始！", "提示！", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(true);
            printable = true;
            new Thread(new PaintThread()).start(); // 线程启动
        } else if (e.getActionCommand().equals("level1")) {
            Tank.count = 12;
            Tank.speedX = 6;
            Tank.speedY = 6;
            Bullets.speedX = 10;
            Bullets.speedY = 10;
            this.dispose();
            new GameFrame();
        } else if (e.getActionCommand().equals("level2")) {
            Tank.count = 12;
            Tank.speedX = 10;
            Tank.speedY = 10;
            Bullets.speedX = 12;
            Bullets.speedY = 12;
            this.dispose();
            new GameFrame();
        } else if (e.getActionCommand().equals("level3")) {
            Tank.count = 20;
            Tank.speedX = 14;
            Tank.speedY = 14;
            Bullets.speedX = 16;
            Bullets.speedY = 16;
            this.dispose();
            new GameFrame();
        } else if (e.getActionCommand().equals("level4")) {
            Tank.count = 20;
            Tank.speedX = 16;
            Tank.speedY = 16;
            Bullets.speedX = 18;
            Bullets.speedY = 18;
            this.dispose();
            new GameFrame();
        }
    }
}
