import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Board extends JPanel implements Serializable {

    class BoardMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (!IsSelectingShape) {
                /* 绘图状态 */
                /* 写入文字，只需要取一个起始点即可 */
                if (NowDrawingShape.equals("Text") && !IsWritingFinished) {
                    WritingStartPointX = e.getX();
                    WritingStartPointY = e.getY();
                    repaint();
                } else {
                    /* 绘制图形，需要取两点 */
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
                /* 尝试选中图形 */
                int ClickPointX = e.getX();
                int ClickPointY = e.getY();
                double Shortest = -1;
                double Distance;
                /* 遍历Memory，寻找中心点与单击点最接近的地方 */
                /* 更新选中图形在Memory中的ID */
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
                } else
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
    }

    class BoardMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent e) {
            /* 绘制图形时，根据用户鼠标移动绘制虚线预览图 */
            if (IsDrawing && !NowDrawingShape.equals("Text")) {
                DrawingEndPointX = e.getX();
                DrawingEndPointY = e.getY();
                repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            /* 选中图形并移动时，根据用户鼠标拖动移动图形 */
            if (IsSelectingShape && IsChangingPosition) {
                Shapes SelectShape = Memory.get(SelectShapeID);
                SelectShape.ChangePosition(e.getX(), e.getY());
                repaint();
            }
        }
    }

    class BoardKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {

            /* 用户输入时，读取键盘输入 */
            if (!IsSelectingShape && NowDrawingShape.equals("Text")) {
                if (e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                    Input.append(e.getKeyChar());
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    Input.deleteCharAt(Input.length() - 1);
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    IsWritingFinished = true;
                    repaint();
                }
            }

            /* 用户敲下回车，结束移动选中图形 */
            if (e.getKeyCode() == KeyEvent.VK_ENTER && IsChangingPosition && IsSelectingShape) {
                IsChangingPosition = false;
                SelectShapeID = ILLEGAL;
            }

            /* 用户移动图形时，根据方向键移动图形 */
            if (IsSelectingShape && SelectShapeID != ILLEGAL) {
                Shapes SelectShape = Memory.get(SelectShapeID);
                switch (e.getKeyCode()) {
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
                }
                if (SelectShapeID != ILLEGAL)
                    Memory.set(SelectShapeID, SelectShape);
                repaint();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

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

    transient boolean IsZoomSelectedShape;
    transient boolean IsChangingPosition;
    transient String NowDrawingShape;

    transient int NowWritingSize;

    ArrayList<Shapes> Memory;

    transient StringBuffer Input;

    public Board() {
        Memory = new ArrayList<>();
        Input = new StringBuffer();
        NowDrawingShape = "Line";
        NowDrawingColor = Color.BLACK;
        IsDrawing = false;
        IsSelectingShape = false;
        IsWritingFinished = false;
        IsChangingPosition = false;
        IsZoomSelectedShape = false;
        NowDrawingWidth = 1;
        NowWritingSize = 40;
        SelectShapeID = ILLEGAL;
        DrawingStartPointX = DrawingEndPointX = DrawingStartPointY = DrawingEndPointY = WritingStartPointX = WritingStartPointY = ILLEGAL;
        this.setBorder(BorderFactory.createLineBorder(Color.red, 5));

        addMouseListener(new BoardMouseListener());
        addMouseMotionListener(new BoardMouseMotionListener());
        addKeyListener(new BoardKeyListener());
        addMouseWheelListener(e -> {
            if (IsSelectingShape && SelectShapeID != ILLEGAL && IsZoomSelectedShape) {
                Shapes SelectShape = Memory.get(SelectShapeID);
                SelectShape.Zoom(-1 * e.getWheelRotation());
                Memory.set(SelectShapeID, SelectShape);
                repaint();
            }
        });
        setPreferredSize(new Dimension(1000, 800));
    }

    public void SetShape(String Shape) {
        NowDrawingShape = Shape;
        DrawingStartPointX = DrawingEndPointX = DrawingStartPointY = DrawingEndPointY = WritingStartPointX = WritingStartPointY = ILLEGAL;
        IsDrawing = false;
        IsWritingFinished = false;
        IsSelectingShape = false;
        IsChangingPosition = false;
        IsZoomSelectedShape = false;
        SelectShapeID = ILLEGAL;
        Input.delete(0, Input.length());
        if (Shape.equals("Text"))
            requestFocusInWindow();
    }

    public void SetColor(Color Color) {
        IsSelectingShape = false;
        IsChangingPosition = false;
        IsZoomSelectedShape = false;
        SelectShapeID = ILLEGAL;
        NowDrawingColor = Color;
    }

    public void SetWidth(float Width) {
        IsSelectingShape = false;
        IsChangingPosition = false;
        IsZoomSelectedShape = false;
        SelectShapeID = ILLEGAL;
        NowDrawingWidth = Width;
    }

    public void SetModel(String Model) {
        IsSelectingShape = !Model.equals("Draw");
        IsChangingPosition = false;
        IsZoomSelectedShape = false;
        SelectShapeID = ILLEGAL;

        if (IsSelectingShape)
            requestFocusInWindow();
    }

    public void SetCharSize(int CharSize) {
        IsSelectingShape = false;
        IsChangingPosition = false;
        IsZoomSelectedShape = false;
        SelectShapeID = ILLEGAL;
        NowWritingSize = CharSize;
    }

    public ArrayList<Shapes> GetMemory() {
        return this.Memory;
    }

    public void SetMemory(ArrayList<Shapes> Memory) {
        this.Memory = Memory;
        repaint();
    }

    public void ChangeToSelectColor(Color color) {
        Shapes SelectShape = Memory.get(SelectShapeID);
        SelectShape.ChangeColor(color);
        Memory.set(SelectShapeID, SelectShape);
        repaint();
    }

    public void RemoveSelectedShape() {
        Memory.remove(SelectShapeID);
        SelectShapeID = ILLEGAL;
        IsChangingPosition = false;
        repaint();
    }

    public void StartMoveSelectedShape() {
        IsChangingPosition = true;
        IsZoomSelectedShape = false;
    }

    public void StartZoomSelectedShape() {
        IsChangingPosition = false;
        IsZoomSelectedShape = true;
    }

    public void Deselect() {
        IsSelectingShape = false;
        SelectShapeID = ILLEGAL;
        repaint();
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
            Info = new Shapes(NowDrawingShape, WritingStartPointX, WritingStartPointY, ILLEGAL, ILLEGAL, -1, NowWritingSize, NowDrawingColor, Input.toString());
            MyDrawShape(temp, Info, true);
            if (IsWritingFinished) {
                Memory.add(Info);
                Input.delete(0, Input.length());
                IsWritingFinished = false;
                WritingStartPointX = WritingStartPointY = ILLEGAL;
            }
        } else {
            if (DrawingStartPointX != ILLEGAL && DrawingStartPointY != ILLEGAL && DrawingEndPointX != ILLEGAL && DrawingEndPointY != ILLEGAL) {
                Info = new Shapes(NowDrawingShape, DrawingStartPointX, DrawingStartPointY, DrawingEndPointX, DrawingEndPointY, NowDrawingWidth, -1, NowDrawingColor, null);
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
            Font NowFont = new Font("楷体", Font.BOLD, a.size);
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

    public boolean IsShapeSelected() {
        return (SelectShapeID != -1);
    }

}

class Shapes implements Serializable {
    String Kind;

    int size;
    int x1, y1;
    int x2, y2;
    float width;
    Color color;

    String Info;

    public Shapes(String Kind, int x1, int y1, int x2, int y2, float width, int size, Color color, String Info) {
        this.Kind = Kind;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.width = width;
        this.color = color;
        this.Info = Info;
        this.size = size;
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
        if (!Kind.equals("Text")) {
            x2 += delta * ((x2 - x1) / 10);
            y2 += delta * ((y2 - y1) / 10);
        } else {
            size += (delta * size) / 10;
        }
    }

    public void ChangeColor(Color NewColor) {
        color = NewColor;
    }

    public void ChangePosition(int new_x, int new_y) {
        if (Kind.equals("Text")) {
            x1 = new_x;
            y1 = new_y;
        } else {
            int temp_x1 = x1;
            int temp_x2 = x2;
            int temp_y1 = y1;
            int temp_y2 = y2;

            x1 = new_x - ((temp_x1 + temp_x2) / 2) + temp_x1;
            x2 = new_x - ((temp_x1 + temp_x2) / 2) + temp_x2;
            y1 = new_y - ((temp_y1 + temp_y2) / 2) + temp_y1;
            y2 = new_y - ((temp_y1 + temp_y2) / 2) + temp_y2;
        }
    }
}