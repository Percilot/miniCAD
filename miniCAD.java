public class miniCAD {
    public static void main(String[] args) {
        MyFrame TotalFrame = new MyFrame();
        Board TotalBoard = new Board();
        MyMenu TotalMenu = new MyMenu(TotalBoard);

        TotalFrame.setJMenuBar(TotalMenu.GetMenuBar());

        TotalFrame.getContentPane().add(TotalBoard);
        TotalFrame.setVisible(true);
        TotalFrame.pack();
    }
}