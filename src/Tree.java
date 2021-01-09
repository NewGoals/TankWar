import java.awt.*;

/**
 * 树（丛林）类
 */

public class Tree {
    public static final int width = Property.getInt("game.tree.size");
    public static final int length = Property.getInt("game.tree.size");
    int x, y;
    GameFrame tc;
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] treeImags = null;

    static {
        treeImags = new Image[]{tk.getImage(Tree.class.getResource("Images/tree.gif")),};
    }

    public Tree(int x, int y, GameFrame tc) { // Tree的构造方法，传递x，y和tc对象
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(Graphics g) { // 画出树
        g.drawImage(treeImags[0], x, y, null);
    }

}
