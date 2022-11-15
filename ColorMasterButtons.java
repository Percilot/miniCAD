import javax.swing.*;
import java.awt.*;


public class ColorMasterButtons extends JPanel {
    public ColorMasterButtons(Board DrawingBoard) {
        JButton SelectColorButton = new JButton("ChangeColor");
        SelectColorButton.addActionListener(
                e -> {
                    Color color = JColorChooser.showDialog(SelectColorButton, "Select color", Color.lightGray);
                    if (color == null)
                        color = Color.BLACK;
                    DrawingBoard.SetColor(color);
                });
        this.setLayout(new FlowLayout());
        this.add(SelectColorButton);
    }
}