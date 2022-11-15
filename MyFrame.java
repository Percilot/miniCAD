import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    public MyFrame() {
        super("miniCAD");
        this.setSize(1000, 800);
        this.setResizable(false);
        this.setBackground(Color.WHITE);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
