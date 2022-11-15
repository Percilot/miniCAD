import javax.swing.*;
import java.awt.*;

public class Layout extends JPanel {
    protected Board DrawingBoard;
    protected ShapeMasterButtons ShapeMaster;

    protected ColorMasterButtons ColorMaster;

    public Layout() {
        DrawingBoard = new Board();
        ShapeMaster = new ShapeMasterButtons(DrawingBoard);
        ColorMaster = new ColorMasterButtons(DrawingBoard);
        this.setLayout(new BorderLayout());
        this.add(ShapeMaster, BorderLayout.EAST);
        this.add(DrawingBoard, BorderLayout.CENTER);
        this.add(ColorMaster, BorderLayout.WEST);

    }
}
