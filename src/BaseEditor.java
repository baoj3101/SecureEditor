/**
 * BaseEditor Class: very basic text editor supporting copy/paste
 * @author baoj3101
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class BaseEditor {
    // editor frame and text pane
    protected JFrame    frame;
    protected JTextPane textPane;
    
    // constructor
    public BaseEditor () {
        // initialize frame
        frame = new JFrame("Base Editor");
        frame.setSize(800, 800);
        frame.setLocation(100, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // initialize text pane
        textPane = new JTextPane();
        textPane.setDocument(new DefaultStyledDocument());
    }

    // show
    public void show() {
        // add text pane to frame and show
        JScrollPane scrollPane = new JScrollPane(textPane);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
        textPane.requestFocusInWindow();        
    }
    
    // get doc from text pane
    public StyledDocument getDocument () {
	return (DefaultStyledDocument) textPane.getDocument();
    }
    
    // main for testing
    public static void main(String [] args) {
        BaseEditor e = new BaseEditor();
        e.show();
    }
}
