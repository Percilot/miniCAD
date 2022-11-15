import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorMasterButtons extends JPanel {
    public ColorMasterButtons(Board DrawingBoard) {
        JButton PenRed = new JButton("Red");
        PenRed.addActionListener(new ColorButtonHandler(DrawingBoard));

        JButton PenYellow = new JButton("Yellow");
        PenYellow.addActionListener(new ColorButtonHandler(DrawingBoard));

        JButton PenGreen = new JButton("Green");
        PenGreen.addActionListener(new ColorButtonHandler(DrawingBoard));

        JButton PenBlue = new JButton("Blue");
        PenBlue.addActionListener(new ColorButtonHandler(DrawingBoard));

        JButton PenGray = new JButton("Gray");
        PenGray.addActionListener(new ColorButtonHandler(DrawingBoard));

        JButton PenOrange = new JButton("Orange");
        PenOrange.addActionListener(new ColorButtonHandler(DrawingBoard));

        JButton PenCyan = new JButton("Cyan");
        PenCyan.addActionListener(new ColorButtonHandler(DrawingBoard));

        this.setLayout(new GridLayout(8, 1, 10, 10));
        this.add(PenRed);
        this.add(PenYellow);
        this.add(PenGreen);
        this.add(PenBlue);
        this.add(PenGray);
        this.add(PenOrange);
        this.add(PenCyan);
    }
}

class ColorButtonHandler implements ActionListener {
    private final Board Target;
    ColorButtonHandler(Board Target) {
        this.Target = Target;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String Info = e.getActionCommand();
        Target.IsWritingFinished = true;
        Target.IsDrawing = false;
        Target.repaint();
        switch (Info) {
            case "Red":
                Target.SetColor(Color.RED);
                break;
            case "Yellow":
                Target.SetColor(Color.YELLOW);
                break;
            case "Green":
                Target.SetColor(Color.GREEN);
                break;
            case "Blue":
                Target.SetColor(Color.BLUE);
                break;
            case "Orange":
                Target.SetColor(Color.ORANGE);
                break;
            case "Cyan":
                Target.SetColor(Color.CYAN);
                break;
            case "Gray":
                Target.SetColor(Color.GRAY);
                break;
        }
    }
}