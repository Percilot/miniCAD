import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.*;

public class MyMenu {

    private final JMenuBar MenuBar;

    public MyMenu(Board TotalBoard) {
        MenuBar = new JMenuBar();

        ShapeMenu MyShapeMenu = new ShapeMenu(TotalBoard);
        PenMenu MyPenMenu = new PenMenu(TotalBoard);
        SelectMenu MySelectMenu = new SelectMenu(TotalBoard);
        FileMenu MyFileMenu = new FileMenu(TotalBoard);

        MenuBar.add(MyFileMenu.GetMenu());
        MenuBar.add(MyShapeMenu.GetMenu());
        MenuBar.add(MyPenMenu.GetMenu());
        MenuBar.add(MySelectMenu.GetMenu());
        MenuBar.setBounds(0, 0, 800, 30);
    }

    public JMenuBar GetMenuBar() {
        return MenuBar;
    }
}

class ShapeMenu {
    static class ShapeHandler implements ActionListener {
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
        Shape.addSeparator();
        Shape.add(ChooseDrawingRect);
        Shape.addSeparator();
        Shape.add(ChooseDrawingCircle);
        Shape.addSeparator();
        Shape.add(ChooseWritingText);
    }

    public JMenu GetMenu() {
        return Shape;
    }
}

class PenMenu {

    static class PenSizeHandler implements ActionListener, AdjustmentListener {
        private final Board Target;
        private JScrollBar ScrollBar;
        private JLabel Reminder;

        private int Width;

        public PenSizeHandler(Board Target) {
            this.Target = Target;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Width = (int) ((Target.NowDrawingWidth) * 10);
            JFrame setPenWidth = new JFrame("Set pen width");
            Container container = setPenWidth.getContentPane();

            ScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, Width, 1, 10, 100);
            ScrollBar.setUnitIncrement(1);
            ScrollBar.setBlockIncrement(5);
            ScrollBar.addAdjustmentListener(this);

            Reminder = new JLabel("当前画笔粗细： " + Width, SwingConstants.CENTER);

            setPenWidth.setSize(600, 200);
            setPenWidth.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setPenWidth.setResizable(false);
            setPenWidth.setVisible(true);

            JButton Confirm = new JButton("Confirm");
            Confirm.addActionListener(e1 -> {
                String Info = e1.getActionCommand();
                if (Info.equals("Confirm")) Target.SetWidth((float) (Width / 10));
                setPenWidth.dispose();
            });

            container.add(Reminder, BorderLayout.NORTH);
            container.add(ScrollBar, BorderLayout.CENTER);
            container.add(Confirm, BorderLayout.SOUTH);
        }

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
            if (e.getSource() == ScrollBar) {
                Reminder.setText("当前画笔粗细： " + e.getValue());
                Width = e.getValue();
            }
        }
    }

    static class CharSizeHandler implements ActionListener, AdjustmentListener {
        private final Board Target;
        private JScrollBar ScrollBar;
        private JLabel Reminder;

        private int CharSize;

        public CharSizeHandler(Board Target) {
            this.Target = Target;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            CharSize = Target.NowWritingSize;
            JFrame setCharSize = new JFrame("Set char size");
            Container container = setCharSize.getContentPane();

            ScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, CharSize, 1, 10, 100);
            ScrollBar.setUnitIncrement(1);
            ScrollBar.setBlockIncrement(5);
            ScrollBar.addAdjustmentListener(this);

            Reminder = new JLabel("当前字号： " + CharSize, SwingConstants.CENTER);

            setCharSize.setSize(600, 200);
            setCharSize.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setCharSize.setResizable(false);
            setCharSize.setVisible(true);

            JButton Confirm = new JButton("Confirm");
            Confirm.addActionListener(e1 -> {
                String Info = e1.getActionCommand();
                if (Info.equals("Confirm")) Target.SetCharSize(CharSize);
                setCharSize.dispose();
            });

            container.add(Reminder, BorderLayout.NORTH);
            container.add(ScrollBar, BorderLayout.CENTER);
            container.add(Confirm, BorderLayout.SOUTH);
        }

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
            if (e.getSource() == ScrollBar) {
                Reminder.setText("当前字号： " + e.getValue());
                CharSize = e.getValue();
            }
        }
    }

    private final JMenu PenAttribute;

    private final Board Target;

    public PenMenu(Board DrawingBoard) {
        PenAttribute = new JMenu("PenAttribute");
        Target = DrawingBoard;

        JMenuItem ChoosePenColor = new JMenuItem("Color");
        JMenuItem ChoosePenWidth = new JMenuItem("PenSize");
        JMenuItem ChooseCharSize = new JMenuItem("CharSize");

        ChoosePenColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(ChoosePenColor, "Select pen color", Color.lightGray);
            if (color == null) color = Color.BLACK;
            Target.SetColor(color);
        });
        ChoosePenWidth.addActionListener(new PenSizeHandler(DrawingBoard));
        ChooseCharSize.addActionListener(new CharSizeHandler(DrawingBoard));

        PenAttribute.add(ChoosePenColor);
        PenAttribute.addSeparator();
        PenAttribute.add(ChoosePenWidth);
        PenAttribute.addSeparator();
        PenAttribute.add(ChooseCharSize);
    }

    public JMenu GetMenu() {
        return PenAttribute;
    }
}

class SelectMenu {
    static class SelectHandler implements ActionListener {
        private final Board Target;

        public SelectHandler(Board Target) {
            this.Target = Target;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String Info = e.getActionCommand();
            if (Info.equals("SelectShape")) {
                Target.SetModel("Select");
            }
        }
    }

    class ButtonStateMonitor implements MenuListener {

        @Override
        public void menuSelected(MenuEvent e) {
            ChangeColor.setEnabled(Target.IsShapeSelected());
            DeleteShape.setEnabled(Target.IsShapeSelected());
            ChangePosition.setEnabled(Target.IsShapeSelected());
            Zoom.setEnabled(Target.IsShapeSelected());
            Deselect.setEnabled(Target.IsShapeSelected());
        }

        @Override
        public void menuDeselected(MenuEvent e) {

        }

        @Override
        public void menuCanceled(MenuEvent e) {

        }
    }
    private final JMenu Select;
    private final JMenuItem ChangeColor;
    private final JMenuItem DeleteShape;

    private final JMenuItem ChangePosition;

    private final JMenuItem Zoom;

    private final JMenuItem Deselect;

    private final Board Target;

    public SelectMenu(Board DrawingBoard) {
        Target = DrawingBoard;
        Select = new JMenu("Select & Edit");
        Select.addMenuListener(new ButtonStateMonitor());
        JMenuItem SelectShape = new JMenuItem("SelectShape");
        SelectShape.addActionListener(new SelectHandler(DrawingBoard));
        ChangeColor = new JMenuItem("ChangeColor");

        ChangeColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(ChangeColor, "Select new color", Color.lightGray);
            if (color == null) color = Color.BLACK;
            DrawingBoard.ChangeToSelectColor(color);
        });

        DeleteShape = new JMenuItem("Delete");
        DeleteShape.addActionListener(e -> Target.RemoveSelectedShape());

        ChangePosition = new JMenuItem("ChangePosition");
        ChangePosition.addActionListener(e -> Target.StartMoveSelectedShape());

        Zoom = new JMenuItem("Zoom");
        Zoom.addActionListener(e -> Target.StartZoomSelectedShape());

        Deselect = new JMenuItem("Deselect");
        Deselect.addActionListener(e -> Target.Deselect());


        Select.add(SelectShape);
        Select.addSeparator();
        Select.add(Deselect);
        Select.addSeparator();
        Select.add(ChangeColor);
        Select.addSeparator();
        Select.add(ChangePosition);
        Select.addSeparator();
        Select.add(Zoom);
        Select.addSeparator();
        Select.add(DeleteShape);
    }

    public JMenu GetMenu() {
        return Select;
    }
}
class FileMenu {
    class MyFileHandler implements ActionListener {
        private final Board Target;

        public MyFileHandler(Board Target) {
            this.Target = Target;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            if (e.getActionCommand().equals("Save") && !NowOpenFileName.toString().isEmpty()) {
                File TargetFile = new File(NowOpenFileName.toString());
                try {
                    ObjectOutputStream WriteToFile = new ObjectOutputStream(new FileOutputStream(TargetFile));
                    WriteToFile.writeObject(Target);
                    WriteToFile.flush();
                    WriteToFile.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else if (e.getActionCommand().equals("Save to new file") || (e.getActionCommand().equals("Save") && NowOpenFileName.toString().isEmpty())) {
                chooser.setFileFilter(new FileNameExtensionFilter("miniCAD文件(.miniCAD)", "miniCAD"));
                int option = chooser.showSaveDialog(null);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File TargetFile = chooser.getSelectedFile();
                    String FileName = TargetFile.getName();

                    if (!FileName.endsWith(".miniCAD"))
                        FileName = FileName + ".miniCAD";
                    TargetFile = new File(chooser.getCurrentDirectory(), FileName);

                    try {
                        ObjectOutputStream WriteToFile = new ObjectOutputStream(new FileOutputStream(TargetFile));
                        WriteToFile.writeObject(Target);
                        WriteToFile.flush();
                        WriteToFile.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else if (e.getActionCommand().equals("Open")) {
                String FileName = "";
                chooser.setFileFilter(new FileNameExtensionFilter("miniCAD文件(.miniCAD)", "miniCAD"));
                int option = chooser.showOpenDialog(null);
                if (option == JFileChooser.APPROVE_OPTION)
                    FileName = chooser.getSelectedFile().getPath();
                if (FileName.isEmpty())
                    return;
                NowOpenFileName = new StringBuilder(FileName);
                File TargetFile = new File(FileName);

                try {
                    ObjectInputStream ReadFromFile = new ObjectInputStream(new FileInputStream(TargetFile));
                    Board Temp = (Board) ReadFromFile.readObject();
                    Target.SetMemory(Temp.GetMemory());
                    ReadFromFile.close();
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
    private final JMenu File;

    private StringBuilder NowOpenFileName;

    public FileMenu(Board NowDrawingBoard) {
        File = new JMenu("File");
        NowOpenFileName = new StringBuilder();

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new MyFileHandler(NowDrawingBoard));
        JMenuItem saveToNewFile = new JMenuItem("Save to new file");
        saveToNewFile.addActionListener(new MyFileHandler(NowDrawingBoard));
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(new MyFileHandler(NowDrawingBoard));

        File.add(save);
        File.addSeparator();
        File.add(saveToNewFile);
        File.addSeparator();
        File.add(open);
    }

    public JMenu GetMenu() {
        return File;
    }
}