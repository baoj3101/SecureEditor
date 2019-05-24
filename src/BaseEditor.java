
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

    // file menu: Open File, Save File, and Exit 
    JMenuBar menuBar;
    JMenuItem fileOpen, fileSave, fileExit, helpHowTo;

    // file
    protected File file;

    // constructor
    public BaseEditor() {
        // initialize frame
        frame = new JFrame("BaseEditor");
        frame.setSize(800, 800);          // window size 800x800
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
        fileOpen = new JMenuItem("Open File");     // "Open File"
        fileOpen.addActionListener(new FileOpenListener());
        fileMenu.add(fileOpen);
        fileSave = new JMenuItem("Save File");     // "Save File"
        fileSave.addActionListener(new FileSaveListener());
        fileMenu.add(fileSave);
        fileExit = new JMenuItem("Exit");          // "Exit"
        fileExit.addActionListener(new FileExitListener());
        fileMenu.add(fileExit);

        // help menu: How To
        JMenu helpMenu = new JMenu("Help");
        helpHowTo = new JMenuItem("How to ...");   // "How to"
        helpHowTo.addActionListener(new HelpHowToListener());
        helpMenu.add(helpHowTo);

        // menu bar
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        // add menubar to frame
        frame.setJMenuBar(menuBar);
    }

    // show with splash
    public void show() {
        // app splash window with PNG
        JWindow window = new JWindow();
        ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/splash.png")).getImage().getScaledInstance(703, 391, Image.SCALE_DEFAULT));
        JLabel splash = new JLabel();
        splash.setIcon(icon);
        window.getContentPane().add(splash);
        window.setBounds(400, 200, 703, 391);
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
    // Abstract Methods: To be implemented by each type of editor
    //==========================================================================
    // Open file and load into text pane
    public abstract void OpenFile();

    // Save content from text pane to file
    public abstract void SaveFile();

    // Set editor title
    public abstract void setTitle(String str);

    //==========================================================================
    // Event Handlers for file menu items
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

    // help menu event handler: How to
    protected class HelpHowToListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // initialize popup window for help
            JFrame popup = new JFrame();
            popup.setSize(800, 600);
            popup.setLocation(200, 200);
            popup.setTitle("Help");
            JTextPane textArea = new JTextPane();
            textArea.setDocument(new DefaultStyledDocument());
            JScrollPane scroll = new JScrollPane(textArea);
            popup.add(scroll, BorderLayout.CENTER);

            // load how to from file
            StyledDocument doc = null;
            try (InputStream inStream = getClass().getResourceAsStream("resources/help.txt"); ObjectInputStream objInStream = new ObjectInputStream(inStream)) {
                doc = (DefaultStyledDocument) objInStream.readObject();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "File not found: " + file.getName());
                return;
            } catch (ClassNotFoundException | IOException ex) {
                throw new RuntimeException(ex);
            }
            // show help doc in popup window
            textArea.setDocument(doc);
            popup.setVisible(true);
            textArea.requestFocusInWindow();
        }
    }
}
