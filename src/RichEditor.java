
/**
 * Rich Editor: a more advanced edit with style and undo
 *
 * @author baoj3101
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.text.DefaultEditorKit.*;
import javax.swing.text.StyledEditorKit.*;

public class RichEditor extends BaseEditor {

    // Font Buttons
    protected JButton btnBold, btnItalic, btnUnderline, btnColor;

    // constructor
    public RichEditor() {
        super();

        // button listener
        EditBtnListener editBtnListener = new EditBtnListener();

        // Bold
        btnBold = new JButton(new BoldAction());
        btnBold.setHideActionText(true);
        btnBold.setText("B");
        btnBold.addActionListener(editBtnListener);

        // Italic
        btnItalic = new JButton(new ItalicAction());
        btnItalic.setHideActionText(true);
        btnItalic.setText("I");
        btnItalic.addActionListener(editBtnListener);

        // Underline
        btnUnderline = new JButton(new UnderlineAction());
        btnUnderline.setHideActionText(true);
        btnUnderline.setText("U");
        btnUnderline.addActionListener(editBtnListener);

        // Color
        btnColor = new JButton("Color");
        btnColor.addActionListener(new ColorBtnListener());

        // panel
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(btnBold);
        panel.add(btnItalic);
        panel.add(btnUnderline);
        panel.add(new JSeparator(SwingConstants.VERTICAL));        
        panel.add(btnColor);
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.PAGE_AXIS));
        toolbarPanel.add(panel);
        
        // add toolbar to frame
        frame.add(toolbarPanel, BorderLayout.NORTH);
        frame.setTitle("RichEditor");
    }

    // main for testing
    public static void main(String [] args) {
        RichEditor e = new RichEditor();
        e.show();
    }
    
    // font button listeners
    protected class EditBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            textPane.requestFocusInWindow();
        }
    }

    // color button listener
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
}