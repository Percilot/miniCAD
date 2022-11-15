import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorMasterButtons extends JPanel {
    public ColorMasterButtons(Board DrawingBoard) {
        JButton SelectColorButton = new JButton("ChangeColor");
        SelectColorButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JColorChooser colorChooser = new JColorChooser();
                        Color color = colorChooser.showDialog(SelectColorButton, "Select color", Color.lightGray);
                        if (color == null)
                            color = Color.BLACK;
                        DrawingBoard.SetColor(color);
                    }
                });
        this.setLayout(new FlowLayout());
        this.add(SelectColorButton);
    }
}