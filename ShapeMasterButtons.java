import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShapeMasterButtons extends JPanel {

    public ShapeMasterButtons(Board DrawingBoard) {
        JButton buttonLine = new JButton("Line");
        buttonLine.setPreferredSize(new Dimension(100, 50));
        buttonLine.setContentAreaFilled(false);
        buttonLine.setFocusable(false);
        buttonLine.addActionListener(new ShapeButtonHandler(DrawingBoard));


        JButton buttonRect = new JButton("Rectangle");
        buttonRect.setPreferredSize(new Dimension(100, 50));
        buttonRect.setContentAreaFilled(false);
        buttonRect.setFocusable(false);
        buttonRect.addActionListener(new ShapeButtonHandler(DrawingBoard));


        JButton buttonCir = new JButton("Circle");
        buttonCir.setContentAreaFilled(false);
        buttonCir.setPreferredSize(new Dimension(100, 50));
        buttonCir.setFocusable(false);
        buttonCir.addActionListener(new ShapeButtonHandler(DrawingBoard));

        JButton buttonText = new JButton("Text");
        buttonText.setContentAreaFilled(false);
        buttonText.setPreferredSize(new Dimension(100, 50));
        buttonText.setFocusable(false);
        buttonText.addActionListener(new ShapeButtonHandler(DrawingBoard));

        this.setLayout(new GridLayout(4, 1, 10, 10));
        this.add(buttonLine);
        this.add(buttonRect);
        this.add(buttonCir);
        this.add(buttonText);
    }
}

class ShapeButtonHandler implements ActionListener {
    private final Board Target;
    ShapeButtonHandler(Board Target) {
        this.Target = Target;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String Info = e.getActionCommand();

        if (Info.equals("Text")) {
            Target.SetShape(Info);
            Target.requestFocusInWindow();
        }
        else if (Info.equals("Line") || Info.equals("Rectangle") || Info.equals("Circle")) {
            Target.SetShape(Info);
        }
    }
}
