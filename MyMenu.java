import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class MyMenu {

    private final JMenuBar MenuBar;

    public MyMenu(Board TotalBoard) {
        MenuBar = new JMenuBar();

        ShapeMenu MyShapeMenu = new ShapeMenu(TotalBoard);
        PenMenu MyPenMenu = new PenMenu(TotalBoard);

        MenuBar.add(MyShapeMenu.GetMenu());
        MenuBar.add(MyPenMenu.GetMenu());
        MenuBar.setBounds(0, 0, 800, 30);
    }

    public JMenuBar GetMenuBar() {
        return MenuBar;
    }
}

class ShapeMenu {
    private final JMenu Shape;

    public ShapeMenu(Board DrawingBoard) {
        Shape = new JMenu("Shape");
        JMenuItem ChooseDrawingLine = new JMenuItem("Line");
        ChooseDrawingLine.addActionListener(new ShapeHandler(DrawingBoard));
        JMenuItem ChooseDrawingRect = new JMenuItem("Rectangle");
        ChooseDrawingRect.addActionListener(new ShapeHandler(DrawingBoard));
        JMenuItem ChooseDrawingCircle = new JMenuItem("Circle");
        ChooseDrawingCircle.addActionListener(new ShapeHandler(DrawingBoard));
        JMenuItem ChooseWritingText = new JMenuItem("Text");
        ChooseWritingText.addActionListener(new ShapeHandler(DrawingBoard));

        Shape.add(ChooseDrawingLine);
        Shape.add(ChooseDrawingRect);
        Shape.add(ChooseDrawingCircle);
        Shape.add(ChooseWritingText);
    }

    public JMenu GetMenu() {
        return Shape;
    }
}

class ShapeHandler implements ActionListener {
    private final Board Target;

    ShapeHandler(Board Target) {
        this.Target = Target;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String Info = e.getActionCommand();
        Target.SetShape(Info);
    }
}

class PenMenu {
    private final JMenu PenAttribute;

    private final Board Target;

    public PenMenu(Board DrawingBoard) {
        PenAttribute = new JMenu("PenAttribute");
        Target = DrawingBoard;
        JMenuItem ChoosePenColor = new JMenuItem("Color");
        JMenuItem ChoosePenWidth = new JMenuItem("PenSize");
        ChoosePenColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(ChoosePenColor, "Select pen color", Color.lightGray);
            if (color == null)
                color = Color.BLACK;
            Target.SetColor(color);
        });
        ChoosePenWidth.addActionListener(new PenSizeHandler(DrawingBoard));
        PenAttribute.add(ChoosePenColor);
        PenAttribute.add(ChoosePenWidth);
    }
    public JMenu GetMenu() {
        return PenAttribute;
    }
}

class PenSizeHandler implements ActionListener, AdjustmentListener {
    private final Board Target;
    private JScrollBar ScrollBar;
    private JLabel Reminder;

    private int Width;

    public PenSizeHandler(Board Target) {
        this.Target = Target;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Width = 10;
        JFrame setPenWidth = new JFrame("Set pen width");
        Container container = setPenWidth.getContentPane();

        ScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 10, 1, 10, 100);
        ScrollBar.setUnitIncrement(1);
        ScrollBar.setBlockIncrement(5);
        ScrollBar.addAdjustmentListener(this);

        Reminder = new JLabel("当前字号： 1", SwingConstants.CENTER);

        setPenWidth.setSize(600, 200);
        setPenWidth.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setPenWidth.setResizable(false);
        setPenWidth.setVisible(true);

        JButton Confirm = new JButton("Confirm");
        Confirm.addActionListener(e1 -> {
            String Info = e1.getActionCommand();
            if (Info.equals("Confirm"))
                Target.SetWidth((float)(Width / 10));
        });

        container.add(Reminder, BorderLayout.NORTH);
        container.add(ScrollBar, BorderLayout.CENTER);
        container.add(Confirm, BorderLayout.SOUTH);
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (e.getSource() == ScrollBar) {
            Reminder.setText("当前字号： " + e.getValue());
            Width = e.getValue();
        }
    }
}