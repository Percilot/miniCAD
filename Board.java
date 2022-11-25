import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Board extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener , KeyListener, Serializable {
    transient int DrawingStartPointX, DrawingStartPointY;
    transient int DrawingEndPointX, DrawingEndPointY;

    transient int WritingStartPointX, WritingStartPointY;

    transient int SelectShapeID;

    transient final int ILLEGAL = -1;

    transient Color NowDrawingColor;
    transient float NowDrawingWidth;
    transient boolean IsDrawing;
    transient boolean IsWritingFinished;
    transient boolean IsSelectingShape;

    transient boolean IsSelectingFinished;
    transient String NowDrawingShape;

    ArrayList<Shapes> Memory;

    transient StringBuffer Input;

    public Board() {
        Memory = new ArrayList<>();
        Input = new StringBuffer();
        NowDrawingShape = "Line";
        NowDrawingColor = Color.BLACK;
        IsDrawing = false;
        IsSelectingShape = false;
        IsSelectingFinished = false;
        IsWritingFinished = false;
        NowDrawingWidth = 1;
        SelectShapeID = ILLEGAL;
        DrawingStartPointX = DrawingEndPointX = DrawingStartPointY = DrawingEndPointY = WritingStartPointX = WritingStartPointY = ILLEGAL;
        this.setBorder(BorderFactory.createLineBorder(Color.red, 5));
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addMouseWheelListener(this);
        setPreferredSize(new Dimension(1000, 800));
    }

    public void SetShape(String Shape) {
        NowDrawingShape = Shape;
        DrawingStartPointX = DrawingEndPointX = DrawingStartPointY = DrawingEndPointY = WritingStartPointX = WritingStartPointY = ILLEGAL;
        IsDrawing = false;
        IsWritingFinished = false;
        IsSelectingShape = false;
        IsSelectingFinished = false;
        SelectShapeID = ILLEGAL;
        Input.delete(0, Input.length());
        if (Shape.equals("Text"))
            requestFocusInWindow();
    }

    public void SetColor(Color Color) {
        IsSelectingShape = false;
        IsSelectingFinished = false;
        SelectShapeID = ILLEGAL;
        NowDrawingColor = Color;
    }

    public void SetWidth(float Width) {
        IsSelectingShape = false;
        IsSelectingFinished = false;
        SelectShapeID = ILLEGAL;
        NowDrawingWidth = Width;
    }

    public void SetModel(String Model) {
        IsSelectingShape = !Model.equals("Draw");
        IsSelectingFinished = false;
        SelectShapeID = ILLEGAL;

        if (IsSelectingShape)
            requestFocusInWindow();
    }

    public ArrayList<Shapes> GetMemory() {
        return this.Memory;
    }

    public void SetMemory(ArrayList<Shapes> Memory) {
        this.Memory = Memory;
        repaint();
    }

    public void ChangeSelectColor(Color color) {
        if (SelectShapeID != ILLEGAL) {
            Shapes SelectShape = Memory.get(SelectShapeID);
            SelectShape.ChangeColor(color);
            Memory.set(SelectShapeID, SelectShape);
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D temp = (Graphics2D) g;
        for (Shapes a : Memory)
            MyDrawShape(temp, a, true);

        temp.setColor(NowDrawingColor);
        Shapes Info;
        if (NowDrawingShape.equals("Text") && WritingStartPointX != ILLEGAL && WritingStartPointY != ILLEGAL) {
            Info = new Shapes(NowDrawingShape, WritingStartPointX, WritingStartPointY, ILLEGAL, ILLEGAL, -1, NowDrawingColor, Input.toString());
            MyDrawShape(temp, Info, true);
            if (IsWritingFinished) {
                Memory.add(Info);
                Input.delete(0, Input.length());
                IsWritingFinished = false;
                WritingStartPointX = WritingStartPointY = ILLEGAL;
            }
        } else {
            if (DrawingStartPointX != ILLEGAL && DrawingStartPointY != ILLEGAL && DrawingEndPointX != ILLEGAL && DrawingEndPointY != ILLEGAL) {
                Info = new Shapes(NowDrawingShape, DrawingStartPointX, DrawingStartPointY, DrawingEndPointX, DrawingEndPointY, NowDrawingWidth, NowDrawingColor, null);
                MyDrawShape(temp, Info, !IsDrawing);
                if (!IsDrawing) {
                    DrawingStartPointX = DrawingStartPointY = DrawingEndPointX = DrawingEndPointY = ILLEGAL;
                    Memory.add(Info);
                }
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
        if (!IsSelectingShape) {
            if (NowDrawingShape.equals("Text") && !IsWritingFinished) {
                WritingStartPointX = e.getX();
                WritingStartPointY = e.getY();
            } else {
                if (DrawingStartPointX == ILLEGAL || DrawingStartPointY == ILLEGAL) {
                    DrawingStartPointX = e.getX();
                    DrawingStartPointY = e.getY();
                    IsDrawing = true;

                } else {
                    DrawingEndPointX = e.getX();
                    DrawingEndPointY = e.getY();
                    IsDrawing = false;
                    repaint();
                }
            }
        } else {
            int ClickPointX = e.getX();
            int ClickPointY = e.getY();
            double Shortest = -1;
            double Distance;
            if (!Memory.isEmpty()) {
                for (int i = 0; i < Memory.size(); ++i) {
                    if (Memory.get(i).Kind.equals("Text"))
                        Distance = Math.pow(ClickPointX - Memory.get(i).x1, 2) + Math.pow(ClickPointY - Memory.get(i).y1, 2);
                    else
                        Distance = Math.pow(ClickPointX - ((Memory.get(i).x1 + Memory.get(i).x2) / 2.0), 2) + Math.pow(ClickPointY - ((Memory.get(i).y1 + Memory.get(i).y2) / 2.0), 2);

                    if (Shortest < 0 || Distance < Shortest) {
                        Shortest = Distance;
                        SelectShapeID = i;
                    }
                }
            }
            else
                SelectShapeID = ILLEGAL;
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
        if (!IsSelectingShape) {
            if (e.getKeyCode() != KeyEvent.VK_ENTER) {
                Input.append(e.getKeyChar());
                repaint();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && NowDrawingShape.equals("Text")) {
            IsWritingFinished = true;
            repaint();
        }

        if (IsSelectingShape && SelectShapeID != ILLEGAL)
        {
            Shapes SelectShape = Memory.get(SelectShapeID);
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    SelectShape.MoveUpOrDown(-10);
                    break;
                case KeyEvent.VK_DOWN:
                    SelectShape.MoveUpOrDown(10);
                    break;
                case KeyEvent.VK_LEFT:
                    SelectShape.MoveLeftOrRight(-10);
                    break;
                case KeyEvent.VK_RIGHT:
                    SelectShape.MoveLeftOrRight(10);
                    break;
                case KeyEvent.VK_DELETE:
                    Memory.remove(SelectShapeID);
                    SelectShapeID = ILLEGAL;
                    break;
            }
            if (SelectShapeID != ILLEGAL)
                Memory.set(SelectShapeID, SelectShape);
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (IsSelectingShape && SelectShapeID != ILLEGAL) {
            Shapes SelectShape = Memory.get(SelectShapeID);
            SelectShape.Zoom(-1 * e.getWheelRotation());
            Memory.set(SelectShapeID, SelectShape);
            repaint();
        }
    }

}

class Shapes implements Serializable{
    String Kind;
    int x1, y1;
    int x2, y2;
    float width;
    Color color;

    String Info;

    public Shapes(String Kind, int x1, int y1, int x2, int y2, float width, Color color, String Info) {
        this.Kind = Kind;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.width = width;
        this.color = color;
        this.Info = Info;
    }

    public void MoveUpOrDown(int delta) {
        y1 += delta;
        y2 += delta;
    }

    public void MoveLeftOrRight(int delta) {
        x1 += delta;
        x2 += delta;
    }

    public void Zoom(int delta) {
        x2 += delta * ((x2 - x1) / 10);
        y2 += delta * ((y2 - y1) / 10);
    }

    public void ChangeColor(Color NewColor) {
        color = NewColor;
    }
}