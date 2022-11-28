import javax.swing.*;
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

        MenuBar.add(MyShapeMenu.GetMenu());
        MenuBar.add(MyPenMenu.GetMenu());
        MenuBar.add(MySelectMenu.GetMenu());
        MenuBar.add(MyFileMenu.GetMenu());
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
        Shape.add(ChooseDrawingRect);
        Shape.add(ChooseDrawingCircle);
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
        PenAttribute.add(ChoosePenWidth);
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
    private final JMenu Select;

    public SelectMenu(Board DrawingBoard) {
        Select = new JMenu("Select");
        JMenuItem SelectShape = new JMenuItem("SelectShape");
        SelectShape.addActionListener(new SelectHandler(DrawingBoard));
        JMenuItem ChangeColor = new JMenuItem("ChangeColor");

        ChangeColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(ChangeColor, "Select new color", Color.lightGray);
            if (color == null) color = Color.BLACK;
            DrawingBoard.ChangeSelectColor(color);
        });

        Select.add(SelectShape);
        Select.add(ChangeColor);
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

            if (e.getActionCommand().equals("Save to new file")) {
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
                NowOpenFileName = FileName;
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

    private final Board Target;
    private String NowOpenFileName;

    public FileMenu(Board NowDrawingBoard) {
        File = new JMenu("File");
        this.Target = NowDrawingBoard;
        NowOpenFileName = "";

        JMenuItem Save = new JMenuItem("Save");
        Save.addActionListener(e -> {
            if (!NowOpenFileName.isEmpty()) {
                File TargetFile = new File(NowOpenFileName);

                try {
                    ObjectOutputStream WriteToFile = new ObjectOutputStream(new FileOutputStream(TargetFile));
                    WriteToFile.writeObject(Target);
                    WriteToFile.flush();
                    WriteToFile.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        JMenuItem SaveToNewFile = new JMenuItem("Save to new file");
        SaveToNewFile.addActionListener(new MyFileHandler(NowDrawingBoard));
        JMenuItem Open = new JMenuItem("Open");
        Open.addActionListener(new MyFileHandler(NowDrawingBoard));

        File.add(Save);
        File.add(SaveToNewFile);
        File.add(Open);
    }

    public JMenu GetMenu() {
        return File;
    }
}