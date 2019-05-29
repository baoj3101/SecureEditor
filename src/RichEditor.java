
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
    protected JComboBox<String> fontSizeSel;                  // font size
    protected JComboBox<String> fontFamilySel;                // font family
    protected JComboBox<String> textAlignSel;                 // text align
    protected JButton btnBold, btnItalic, btnUnderline;       // font style
    protected JButton btnColor;                               // font color

    // constructor
    public RichEditor() {
        super();

        // button listener
        EditBtnListener editBtnListener = new EditBtnListener();

        // Bold Button
        btnBold = new JButton(new BoldAction());
        btnBold.setHideActionText(true);
        btnBold.setText("<html><b>B</b></html>");
        btnBold.addActionListener(editBtnListener);

        // Italic Button
        btnItalic = new JButton(new ItalicAction());
        btnItalic.setHideActionText(true);
        btnItalic.setText("<html><i>I</i></html>");
        btnItalic.addActionListener(editBtnListener);

        // Underline Button
        btnUnderline = new JButton(new UnderlineAction());
        btnUnderline.setHideActionText(true);
        btnUnderline.setText("<html><u>U</u></html>");
        btnUnderline.addActionListener(editBtnListener);

        // Color Button
        btnColor = new JButton("<html><font color=\"red\">Color</font></html>");
        btnColor.addActionListener(new ColorBtnListener());

        // Font Family Selector
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fontFamilySel = new JComboBox(g.getAvailableFontFamilyNames());
        fontFamilySel.setEditable(false);
        fontFamilySel.addItemListener(new FontFamilyListener());

        // Font Size Selector
        String[] SIZES = {"10", "11", "12", "14", "16", "18", "20", "24", "28", "30", "36", "40", "48"};
        fontSizeSel = new JComboBox<String>(SIZES);
        fontSizeSel.setEditable(false);
        fontSizeSel.addItemListener(new FontSizeListener());

//        String[] ALIGNS = {"Left", "Center", "Right", "Justified"};
        String[] ALIGNS = {"\uf036", "\uf037", "\uf038", "\uf039"};   // use font awesome icons: align-left/center/right/justified
        textAlignSel = new JComboBox<String>(ALIGNS);
        textAlignSel.setFont(fontAwesome);                            // load font awesome icons
        textAlignSel.setEditable(false);
        textAlignSel.addItemListener(new TextAlignListener());

        // panel
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Font Family"));
        panel.add(fontFamilySel);
        panel.add(new JLabel("Font Size"));
        panel.add(fontSizeSel);
        panel.add(new JLabel("Text Align"));
        panel.add(textAlignSel);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(btnBold);
        panel.add(btnItalic);
        panel.add(btnUnderline);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(btnColor);

        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.PAGE_AXIS));
        toolbarPanel.add(panel);

        // add menubar and toolbar to frame
        frame.add(toolbarPanel, BorderLayout.NORTH);
        setTitle("New File");
        setIcon("rtf.png");                        // change default window icon
    }

    // get doc from text pane
    public StyledDocument getDocument() {
        return (DefaultStyledDocument) textPane.getDocument();
    }

    // Open file and load into text pane
    public void OpenFile() {
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
        setTitle(file.getName());
    }

    // Save content from text pane to file
    public void SaveFile() {
        DefaultStyledDocument doc = (DefaultStyledDocument) getDocument();
        try (OutputStream outStream = new FileOutputStream(file); ObjectOutputStream objOutStream = new ObjectOutputStream(outStream)) {
            objOutStream.writeObject(doc);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        setTitle(file.getName());

    }

    // abstract methods
    public void setTitle(String str) {
        frame.setTitle("Rich Editor: " + str);
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
            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }

            // get selected font size
            String sel = (String) e.getItem();
            int fontSize = Integer.parseInt((String) e.getItem());;
            fontSizeSel.setAction(new FontSizeAction(sel, fontSize));
            textPane.requestFocusInWindow();
        }
    }

    // font family selector event handler
    protected class FontFamilyListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }

            String sel = (String) e.getItem();
            fontFamilySel.setAction(new FontFamilyAction(sel, sel));
            textPane.requestFocusInWindow();
        }
    }
    
    // text alignment selector event handler
    protected class TextAlignListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }

            // ALIGN_LEFT 0, 
            // ALIGN_CENTER 1
            // ALIGN_RIGHT 2
            // ALIGN_JUSTIFIED 3
            String sel = (String) e.getItem();
            textAlignSel.setAction(new AlignmentAction(sel, textAlignSel.getSelectedIndex()));
            textPane.requestFocusInWindow();
        }
    }
}
