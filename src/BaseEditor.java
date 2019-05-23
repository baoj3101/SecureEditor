
/**
 * BaseEditor Class: a base abstract class
 *   Very basic text editor based on SWING which supports
 *   copy (Ctrl+C), cut (Ctrl+X) and paste (Ctrl+V)
 *   and a file menu with Open/Save/Exit
 *   The splash is implemented in this class
 *
 * @author baoj3101
 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public abstract class BaseEditor {

    // editor frame, text pane, and scroll pane
    protected JFrame frame;
    protected JTextPane textPane;
    protected JScrollPane scrollPane;

    // file menu 
    JMenuItem fileOpen, fileSave, fileExit;

    // file
    protected File file;

    // constructor
    public BaseEditor() {
        // initialize frame
        frame = new JFrame("BaseEditor");
        frame.setSize(800, 800);          // window size
        frame.setLocation(100, 100);      // window location
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // initialize text pane
        textPane = new JTextPane();
        textPane.setDocument(new DefaultStyledDocument());

        // initialize scroll pane
        scrollPane = new JScrollPane(textPane);
        frame.add(scrollPane, BorderLayout.CENTER);

        // file menu: Open/Save/Exit
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileOpen = new JMenuItem("Open File");     // "Open File"
        fileOpen.addActionListener(new FileOpenListener());
        fileMenu.add(fileOpen);
        JMenuItem fileSave = new JMenuItem("Save File");     // "Save File"
        fileSave.addActionListener(new FileSaveListener());
        fileMenu.add(fileSave);
        JMenuItem fileExit = new JMenuItem("Exit");          // "Exit"
        fileExit.addActionListener(new FileExitListener());
        fileMenu.add(fileExit);

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);

        // add menubar to frame
        frame.setJMenuBar(menuBar);
    }

    // show with splash
    public void show() {
        // app splash
        JWindow window = new JWindow();
        ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/splash.png")).getImage().getScaledInstance(670, 340, Image.SCALE_DEFAULT));
        JLabel splash = new JLabel();
        splash.setIcon(icon);
        window.getContentPane().add(splash);        
        window.setBounds(400, 200, 670, 340);
        window.setVisible(true);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.setVisible(false);
        window.dispose();

        // show editor
        frame.setVisible(true);
        textPane.requestFocusInWindow();
    }

    //==========================================================================
    // Abstract Methods
    //==========================================================================
    // Open file and load into text pane
    public abstract void OpenFile();

    // Save content from text pane to file
    public abstract void SaveFile();

    // Set editor title
    public abstract void setTitle(String str);

    //==========================================================================
    // Event Handlers
    //==========================================================================
    // file menu even handler: Open File
    protected class FileOpenListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // open file dialog and choose a file to open
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
            } else {
                return;
            }
            OpenFile();   // read file
        }
    }

    // file menu even handler: Save File    
    protected class FileSaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // open file dialog and choose a file to save
            if (file == null) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                }
            }
            if (file == null) {
                return;
            }
            SaveFile();   // Save File
        }
    }

    // file menu even handler: Exit
    protected class FileExitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
