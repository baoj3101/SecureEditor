
/**
 * RichEditor Class:
 *   A more advanced editor with toolbar.
 *   Toolbar includes the following:
 *     - font style: bold, italic, underline
 *     - font size
 *     - font family
 *     - font color
 *   Extended from BaseEditor
 *
 * @author baoj3101
 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.text.DefaultEditorKit.*;
import javax.swing.text.StyledEditorKit.*;

public class RichEditor extends BaseEditor {

    // Toolbar items
    protected JButton btnBold, btnItalic, btnUnderline;       // font style
    protected JButton btnColor;                               // font color
    protected JComboBox<String> fontSizeSel;                  // font size
    protected JComboBox<String> fontFamilySel;                // font family

    // File Load/Save
    protected File file;

    // constructor
    public RichEditor() {
        super();

        // button listener
        EditBtnListener editBtnListener = new EditBtnListener();

        // Bold Button
        btnBold = new JButton(new BoldAction());
        btnBold.setHideActionText(true);
        btnBold.setText("B");
        btnBold.addActionListener(editBtnListener);

        // Italic Button
        btnItalic = new JButton(new ItalicAction());
        btnItalic.setHideActionText(true);
        btnItalic.setText("I");
        btnItalic.addActionListener(editBtnListener);

        // Underline Button
        btnUnderline = new JButton(new UnderlineAction());
        btnUnderline.setHideActionText(true);
        btnUnderline.setText("U");
        btnUnderline.addActionListener(editBtnListener);

        // Color Button
        btnColor = new JButton("Color");
        btnColor.addActionListener(new ColorBtnListener());

        // Font Size Selector
        String[] SIZES = {"Font Size", "10", "11", "12", "14", "16", "18", "20", "24", "28", "30", "36", "40", "48"};
        fontSizeSel = new JComboBox<String>(SIZES);
        fontSizeSel.setEditable(false);
        fontSizeSel.addItemListener(new FontSizeListener());

        // Font Family Selector
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fontFamilySel = new JComboBox(g.getAvailableFontFamilyNames());
        fontFamilySel.setEditable(false);
        fontFamilySel.addItemListener(new FontFamilyListener());

        // panel
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(btnBold);
        panel.add(btnItalic);
        panel.add(btnUnderline);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(fontSizeSel);
        panel.add(fontFamilySel);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(btnColor);

        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.PAGE_AXIS));
        toolbarPanel.add(panel);

        // file menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem fileNew = new JMenuItem("New File");       // "New File"
        fileNew.addActionListener(new FileNewListener());
        fileMenu.add(fileNew);
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

        // add menubar and toolbar to frame
        frame.setJMenuBar(menuBar);
        frame.add(toolbarPanel, BorderLayout.NORTH);
        frame.setTitle("RichEditor: New File");
    }

    // main for testing
    public static void main(String[] args) {
        RichEditor e = new RichEditor();
        e.show();
    }

    //==========================================================================
    // Event Handlers
    //==========================================================================
    // font button event handler
    protected class EditBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            textPane.requestFocusInWindow();
        }
    }

    // color button event handler
    protected class ColorBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Color userColor = JColorChooser.showDialog(frame, "Pick a color", Color.BLACK);
            if (userColor != null) {
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setForeground(attr, userColor);
                textPane.setCharacterAttributes(attr, false);
            }
            textPane.requestFocusInWindow();
        }
    }

    // font size selector event handler
    protected class FontSizeListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() != ItemEvent.SELECTED || fontSizeSel.getSelectedIndex() == 0) {
                return;
            }

            // get selected font size
            String sel = (String) e.getItem();
            int fontSize = Integer.parseInt((String) e.getItem());;
            fontSizeSel.setAction(new FontSizeAction(sel, fontSize));
            fontSizeSel.setSelectedIndex(0);
            textPane.requestFocusInWindow();
        }
    }

    // font family selector event handler
    protected class FontFamilyListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() != ItemEvent.SELECTED || fontFamilySel.getSelectedIndex() == 0) {
                return;
            }

            String sel = (String) e.getItem();
            fontFamilySel.setAction(new FontFamilyAction(sel, sel));
            fontFamilySel.setSelectedIndex(0);
            textPane.requestFocusInWindow();
        }
    }

    // file menu even handler: New File
    protected class FileNewListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            textPane.setDocument(new DefaultStyledDocument());
            file = null;
            frame.setTitle("RichEditor: New file");
        }
    }

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

            // read file
            StyledDocument doc = null;
            try (InputStream inStream = new FileInputStream(file); ObjectInputStream objInStream = new ObjectInputStream(inStream)) {
                doc = (DefaultStyledDocument) objInStream.readObject();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "File not found: " + file.getName());
                return;
            } catch (ClassNotFoundException | IOException ex) {
                throw new RuntimeException(ex);
            }
            textPane.setDocument(doc);
            frame.setTitle("RichEditor: " + file.getName());
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

            DefaultStyledDocument doc = (DefaultStyledDocument) getDocument();
            try (OutputStream outStream = new FileOutputStream(file); ObjectOutputStream objOutStream = new ObjectOutputStream(outStream)) {
                objOutStream.writeObject(doc);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.setTitle("RichEditor: " + file.getName());
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
