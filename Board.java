import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Board extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    int DrawingStartPointX, DrawingStartPointY;
    int DrawingEndPointX, DrawingEndPointY;

    int WritingStartPointX, WritingStartPointY;

    final int ILLEGAL = -1;

    Color NowDrawingColor;
    float NowDrawingWidth;
    boolean IsDrawing;

    boolean IsWritingFinished;
    String NowDrawingShape;

    ArrayList<Shapes> Memory;

    StringBuffer Input;

    public Board() {
        Memory = new ArrayList<>();
        Input = new StringBuffer();
        NowDrawingShape = "Line";
        NowDrawingColor = Color.BLACK;
        IsDrawing = false;
        IsWritingFinished = false;
        NowDrawingWidth = 1;
        DrawingStartPointX = DrawingEndPointX = DrawingStartPointY = DrawingEndPointY = WritingStartPointX = WritingStartPointY = ILLEGAL;
        this.setBorder(BorderFactory.createLineBorder(Color.red, 5));
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setPreferredSize(new Dimension(1000, 800));
    }

    public void SetShape(String Shape) {
        NowDrawingShape = Shape;
        DrawingStartPointX = DrawingEndPointX = DrawingStartPointY = DrawingEndPointY = WritingStartPointX = WritingStartPointY = ILLEGAL;
        IsDrawing = false;
        IsWritingFinished = false;
        Input.delete(0, Input.length());
        if (Shape.equals("Text"))
            requestFocusInWindow();
    }

    public void SetColor(Color Color) {
        NowDrawingColor = Color;
    }

    public void SetWidth(float Width) {
        NowDrawingWidth = Width;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D temp = (Graphics2D) g;
        temp.setColor(NowDrawingColor);

        for (Shapes a : Memory)
            MyDrawShape(temp, a, true);

        Shapes Info;
        if (NowDrawingShape.equals("Text")) {
            Info = new Shapes(NowDrawingShape, WritingStartPointX, WritingStartPointY, ILLEGAL, ILLEGAL, -1, NowDrawingColor, Input.toString());
            MyDrawShape(temp, Info, true);
            if (IsWritingFinished) {
                Memory.add(Info);
                Input.delete(0, Input.length());
                IsWritingFinished = false;
                WritingStartPointX = WritingStartPointY = ILLEGAL;
            }
        } else {
            Info = new Shapes(NowDrawingShape, DrawingStartPointX, DrawingStartPointY, DrawingEndPointX, DrawingEndPointY, NowDrawingWidth, NowDrawingColor, null);
            MyDrawShape(temp, Info, !IsDrawing);
            if (!IsDrawing)
            {
                DrawingStartPointX = DrawingStartPointY = DrawingEndPointX = DrawingEndPointY = ILLEGAL;
                Memory.add(Info);
            }
        }
        temp.dispose();
    }

    private void MyDrawShape(Graphics2D temp, Shapes a, boolean IsRealDraw) {
        if (a.Kind.equals("Text")) {
            temp.setColor(a.color);
            Font NowFont = new Font("楷体", Font.BOLD, 40);
            temp.setFont(NowFont);
        } else if (IsRealDraw) {
            temp.setColor(a.color);
            temp.setStroke(new BasicStroke(a.width));
        } else {
            temp.setColor(a.color);
            temp.setStroke(new BasicStroke(a.width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[]{10, 10}, 0));
        }

        switch (a.Kind) {
            case "Line":
                temp.drawLine(a.x1, a.y1, a.x2, a.y2);
                break;
            case "Rectangle":
                temp.drawRect(Math.min(a.x1, a.x2), Math.min(a.y1, a.y2), Math.abs(a.x1 - a.x2), Math.abs(a.y1 - a.y2));
                break;
            case "Circle":
                temp.drawOval(Math.min(a.x1, a.x2), Math.min(a.y1, a.y2), Math.abs(a.x1 - a.x2), Math.abs(a.y1 - a.y2));
                break;
            case "Text":
                temp.drawString(a.Info, a.x1, a.y1);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (NowDrawingShape.equals("Text") && !IsWritingFinished) {
            WritingStartPointX = e.getX();
            WritingStartPointY = e.getY();
        }
        else
        {
            if (DrawingStartPointX == ILLEGAL || DrawingStartPointY == ILLEGAL) {
                DrawingStartPointX = e.getX();
                DrawingStartPointY = e.getY();
                IsDrawing = true;

            }
            else {
                DrawingEndPointX = e.getX();
                DrawingEndPointY = e.getY();
                IsDrawing = false;
                repaint();
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (IsDrawing && !NowDrawingShape.equals("Text")) {
            DrawingEndPointX = e.getX();
            DrawingEndPointY = e.getY();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_ENTER) {
            Input.append(e.getKeyChar());
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            IsWritingFinished = true;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

class Shapes {
    String Kind;
    int x1, y1;
    int x2, y2;
    float width;
    Color color;

    String Info;

    Shapes(String Kind, int x1, int y1, int x2, int y2, float width, Color color, String Info) {
        this.Kind = Kind;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.width = width;
        this.color = color;
        this.Info = Info;
    }
}