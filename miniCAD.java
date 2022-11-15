public class miniCAD {
    public static void main(String[] args) {
        MyFrame TotalFrame = new MyFrame();
        Layout TotalLayout = new Layout();
        TotalFrame.getContentPane().add(TotalLayout);
        TotalFrame.pack();
    }
}