import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ̹�˴�ս������
 */

public class GameFrame extends Frame implements ActionListener {

    public static final int FRAME_WIDTH = Property.getInt("window.width"); // ��̬ȫ�ִ��ڴ�С
    public static final int FRAME_LENGTH = Property.getInt("window.height");
    public static boolean printable = true; // ��¼��ͣ״̬����ʱ�̲߳�ˢ�½���
    private long startTime = System.currentTimeMillis();
    private long currentTime = startTime;
    MenuBar menuBar = null;
    Menu menu1 = null, menu2 = null, menu3 = null, menu4 = null;
    MenuItem menuItem1 = null, menuItem2 = null, menuItem3 = null, menuItem4 = null,
            menuItem5 = null, menuItem6 = null, menuItem7 = null, menuItem8 = null, menuItem9 = null;
    Image screenImage = null;

    Tank homeTank = new Tank(300, 560, true, Direction.STOP, this);// ʵ����̹��
    Blood blood = new Blood(); // ʵ����Ѫ��
    Home home = new Home(373, 545, this);// ʵ����home

    // ���¼��ϱ����ڹ��췽���н����˳�ʼ��
    List<River> theRiver = new ArrayList<River>();
    List<Tank> tanks = new ArrayList<Tank>();
    List<BombTank> bombTanks = new ArrayList<BombTank>();
    List<Bullets> bullets = new ArrayList<Bullets>();
    List<Tree> trees = new ArrayList<Tree>();
    List<Wall> homeWall = new ArrayList<>(); // ʵ������������
    List<Wall> otherWall = new ArrayList<>();
    List<Wall> metalWall = new ArrayList<>();

    // ����һ����д�ķ���,����repaint()�����Զ�����
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
        g.setColor(Color.green); // ����������ʾ����

        Font f1 = g.getFont();
        g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g.drawString("�����ڻ��ез�̹��: "+ tanks.size(), 50, 85);
        g.drawString("ʣ������ֵ: " + homeTank.getLife(), 320, 85);
        g.drawString("��Ϸ��ʱ: " + ((currentTime - startTime) / 1000), 550, 85);
        g.setFont(f1);

        // ������Ӯ�ˣ������ǵз�̹��ȫ�𡢴�Ӫ���ڡ����̹������Ѫ����
        if (tanks.size() == 0 && home.isLive() && homeTank.isLive()) {
            Font f = g.getFont();
            g.setFont(new Font("TimesRoman", Font.BOLD, 60));
            this.otherWall.clear();
            g.drawString("��Ӯ�ˣ� ", 310, 300);
            g.setFont(f);
        }

        if (!homeTank.isLive()) {
            home.gameOver(g);
        } else {
            currentTime = System.currentTimeMillis();
        }
        g.setColor(c);

        for (int i = 0; i < theRiver.size(); i++) { // ��������
            River r = theRiver.get(i);
            r.draw(g);
        }

        for (int i = 0; i < theRiver.size(); i++) {    // ײ���������ָ�ԭλ
            River river = theRiver.get(i);
            homeTank.collideRiver(river);
            river.draw(g);
        }

        home.draw(g); // ����home
        homeTank.draw(g); // �����Լ��ҵ�̹��
        homeTank.eat(blood);// ��Ѫ--����ֵ

        for (int i = 0; i < bullets.size(); i++) { // ��ÿһ���ӵ�
            Bullets bullets = this.bullets.get(i);
            bullets.hitTanks(tanks); // ÿһ���ӵ���̹����
            bullets.hitTank(homeTank); // ÿһ���ӵ����Լ��ҵ�̹����ʱ
            bullets.hitHome(); // ÿһ���ӵ��򵽼���ʱ

            for (int j = 0; j < metalWall.size(); j++) { // ÿһ���ӵ��򵽽���ǽ��
                bullets.hitWall(metalWall.get(j));
            }

            for (int j = 0; j < otherWall.size(); j++) {// ÿһ���ӵ�������ǽ��
                bullets.hitWall(otherWall.get(j));
            }

            for (int j = 0; j < homeWall.size(); j++) {// ÿһ���ӵ��򵽼ҵ�ǽ��
                bullets.hitWall(homeWall.get(j));
            }
            bullets.draw(g); // ����Ч��ͼ
        }

        // ����ÿһ���з�̹��
        for (int i = 0; i < tanks.size(); i++) {
            Tank tank = tanks.get(i); // ��ü�ֵ�Եļ�

            for (int j = 0; j < homeWall.size(); j++) {
                Wall wall = homeWall.get(j);
                tank.collideWithWall(wall); // ÿһ��̹��ײ�������ǽʱ
                wall.draw(g);
            }
            for (int j = 0; j < otherWall.size(); j++) { // ÿһ��̹��ײ���������ǽ
                Wall wall = otherWall.get(j);
                tank.collideWithWall(wall);
                wall.draw(g);
            }
            for (int j = 0; j < metalWall.size(); j++) { // ÿһ��̹��ײ������ǽ
                Wall wall = metalWall.get(j);
                tank.collideWithWall(wall);
                wall.draw(g);
            }
            for (int j = 0; j < theRiver.size(); j++) {
                River river = theRiver.get(j); // ÿһ��̹��ײ������ʱ
                tank.collideRiver(river);
                river.draw(g);
                // t.draw(g);
            }

            tank.collideWithTanks(tanks); // ײ���Լ�����
            tank.collideHome(home);

            tank.draw(g);
        }

        blood.draw(g);// ������Ѫ��

        for (int i = 0; i < trees.size(); i++) { // ����trees
            Tree tree = trees.get(i);
            tree.draw(g);
        }

        for (int i = 0; i < bombTanks.size(); i++) { // ������ըЧ��
            BombTank bombTank = bombTanks.get(i);
            bombTank.draw(g);
        }

        for (int i = 0; i < otherWall.size(); i++) { // ����otherWall
            Wall wall = otherWall.get(i);
            wall.draw(g);
        }

        for (int i = 0; i < metalWall.size(); i++) { // ����metalWall
            Wall wall = metalWall.get(i);
            wall.draw(g);
        }

        homeTank.collideWithTanks(tanks);
        homeTank.collideHome(home);

        for (int i = 0; i < metalWall.size(); i++) {// ײ������ǽ
            Wall wall = metalWall.get(i);
            homeTank.collideWithWall(wall);
            wall.draw(g);
        }

        for (int i = 0; i < otherWall.size(); i++) {    // ����שǽ
            Wall wall = otherWall.get(i);
            homeTank.collideWithWall(wall);
            wall.draw(g);
        }

        for (int i = 0; i < homeWall.size(); i++) { // �����̹��ײ���Լ���
            Wall wall = homeWall.get(i);
            homeTank.collideWithWall(wall);
            wall.draw(g);
        }

    }

    public GameFrame() {
        // printable = false;
        // �����˵����˵�ѡ��
        menuBar = new MenuBar();
        menu1 = new Menu("��Ϸ");
        menu2 = new Menu("��ͣ/����");
        menu3 = new Menu("����");
        menu4 = new Menu("��Ϸ����");
        menu1.setFont(new Font("TimesRoman", Font.BOLD, 15));// ���ò˵���ʾ������
        menu2.setFont(new Font("TimesRoman", Font.BOLD, 15));// ���ò˵���ʾ������
        menu3.setFont(new Font("TimesRoman", Font.BOLD, 15));// ���ò˵���ʾ������
        menu4.setFont(new Font("TimesRoman", Font.BOLD, 15));// ���ò˵���ʾ������

        menuItem1 = new MenuItem("��ʼ����Ϸ");
        menuItem2 = new MenuItem("�˳�");
        menuItem3 = new MenuItem("��ͣ");
        menuItem4 = new MenuItem("����");
        menuItem5 = new MenuItem("��Ϸ˵��");
        menuItem6 = new MenuItem("����1");
        menuItem7 = new MenuItem("����2");
        menuItem8 = new MenuItem("����3");
        menuItem9 = new MenuItem("����4");
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

        this.setMenuBar(menuBar);// �˵�Bar�ŵ�JFrame��

        for (int i = 0; i < 10; i++) { // �ҵĸ��
            if (i < 4) {
                homeWall.add(WallFactory.getWall("brick",350, 580 - 21 * i, this));
            } else if (i < 7) {
                homeWall.add(WallFactory.getWall("brick",372 + 22 * (i - 4), 517, this));
            } else {
                homeWall.add(WallFactory.getWall("brick",416, 538 + (i - 7) * 21, this));
            }
        }

        for (int i = 0; i < 32; i++) { // שǽ
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

        for (int i = 0; i < 20; i++) { // ����ǽ����
            if (i < 10) {
                metalWall.add(WallFactory.getWall("metal",140 + 30 * i, 150, this));
                metalWall.add(WallFactory.getWall("metal",600, 400 + 20 * (i),this));
            } else if (i < 20) {
                metalWall.add(WallFactory.getWall("metal",140 + 30 * (i - 10), 180, this));
            }
            else {
                metalWall.add(WallFactory.getWall("metal",500 + 30 * (i - 10), 160, this));
            }
        }

        for (int i = 0; i < 4; i++) { // ���Ĳ���
            if (i < 4) {
                trees.add(new Tree(0 + 30 * i, 360, this));
                trees.add(new Tree(220 + 30 * i, 360, this));
                trees.add(new Tree(440 + 30 * i, 360, this));
                trees.add(new Tree(660 + 30 * i, 360, this));
            }
        }

        theRiver.add(new River(85, 100, this));

        for (int i = 0; i < Property.getInt("game.tank.initCount"); i++) { // ��ʼ��20��̹��, ����̹�˳��ֵ�λ��
            if (i < 9) {
                tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this));
            } else if (i < 15) {
                tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D, this));
            } else {
                tanks.add(new Tank(10, 50 * (i - 12), false, Direction.D, this));
            }
        }

        this.setSize(FRAME_WIDTH, FRAME_LENGTH); // ���ý����С
        setLocationRelativeTo(null);// �ô������
        this.setTitle("̹�˴�ս����(���¿�ʼ��R��  ����F��)                 ");

        this.addWindowListener(new WindowAdapter() { // ���ڹرռ���
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        this.setResizable(false);
        this.setBackground(Color.GREEN);
        this.setVisible(true);

        this.addKeyListener(new KeyMonitor(this));// ���̼���
        new Thread(new PaintThread()).start(); // �߳�����
    }

    public static void main(String[] args) {
        new GameFrame(); // ʵ����
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

        public void keyReleased(KeyEvent e) { // ���������ͷ�
            homeTank.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) { // �������̰���
            homeTank.keyPressed(e, frame);
        }
    }

    // ����¼�
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("NewGame")) {
            printable = false;
            Object[] options = {"ȷ��", "ȡ��"};
            int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ��ʼ����Ϸ��", "", JOptionPane.YES_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (response == 0) {
                printable = true;
                this.dispose();
                new GameFrame();
            } else {
                printable = true;
                new Thread(new PaintThread()).start(); // �߳�����
            }
        } else if (e.getActionCommand().endsWith("Stop")) {// ��ͣ
            printable = false;
        } else if (e.getActionCommand().equals("Continue")) {// ����
            if (!printable) {
                printable = true;
                new Thread(new PaintThread()).start(); // �߳�����
            }
        } else if (e.getActionCommand().equals("Exit")) {// �˳�
            printable = false;
            Object[] options = {"ȷ��", "ȡ��"};
            int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ�˳���", "", JOptionPane.YES_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (response == 0) {
                System.out.println("�˳�");
                System.exit(0);
            } else {
                printable = true;
                new Thread(new PaintThread()).start(); // �߳�����
            }
        } else if (e.getActionCommand().equals("help")) {
            printable = false;
            JOptionPane.showMessageDialog(null, "�á� �� �� �����Ʒ���F���̷��䣬R���¿�ʼ��", "��ʾ��", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(true);
            printable = true;
            new Thread(new PaintThread()).start(); // �߳�����
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
