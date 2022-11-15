import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorMasterButtons extends JPanel {
    public ColorMasterButtons(Board DrawingBoard) {
        JButton PenRed = new JButton("Red");
        PenRed.addActionListener(new ColorButtonHandler(DrawingBoard));
        this.setLayout(new FlowLayout());
        this.add(PenRed);
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
        if (Info.equals("Red"))
            Target.SetColor(Color.RED);
    }
}