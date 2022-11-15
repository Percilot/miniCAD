import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Buttons extends JPanel {

    public Buttons(Board DrawingBoard) {
        JButton buttonLine = new JButton("Line");
        buttonLine.addActionListener(new ButtonHandler(DrawingBoard));
        JButton buttonRect = new JButton("Rectangle");
        buttonRect.addActionListener(new ButtonHandler(DrawingBoard));
        JButton buttonCir = new JButton("Circle");
        buttonCir.addActionListener(new ButtonHandler(DrawingBoard));

        this.setLayout(new FlowLayout());
        this.add(buttonLine);
        this.add(buttonRect);
        this.add(buttonCir);
    }
}

class ButtonHandler implements ActionListener {
    private final Board Target;
    ButtonHandler(Board Target) {
        this.Target = Target;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String Info = e.getActionCommand();
        if (Info.equals("Line") || Info.equals("Rectangle") || Info.equals("Circle"))
            Target.SetShape(Info);
        if (Info.equals("Blue"))
            Target.SetColor(Color.decode(Info));
    }
}
