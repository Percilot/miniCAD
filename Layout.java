import javax.swing.*;
import java.awt.*;

public class Layout extends JPanel {
    protected Board DrawingBoard;
    protected Buttons ShapeMaster;

    public Layout() {
        DrawingBoard = new Board();
        ShapeMaster = new Buttons(DrawingBoard);
        this.setLayout(new BorderLayout());
        this.add(ShapeMaster, BorderLayout.PAGE_START);
        this.add(DrawingBoard, BorderLayout.CENTER);
    }
}
