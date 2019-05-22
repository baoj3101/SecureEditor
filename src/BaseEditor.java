/**
 * BaseEditor Class
 *   Very basic text editor based on SWING which supports 
 *   copy (Ctrl+C), cut (Ctrl+X) and paste (Ctrl+V)
 *   
 * @author baoj3101
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class BaseEditor {
    // editor frame, text pane, and scroll pane
    protected JFrame      frame;
    protected JTextPane   textPane;
    protected JScrollPane scrollPane;
    
    // constructor
    public BaseEditor () {
        // initialize frame
        frame = new JFrame("Base Editor");
        frame.setSize(800, 800);          // window size
        frame.setLocation(100, 100);      // window location
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // initialize text pane
        textPane = new JTextPane();
        textPane.setDocument(new DefaultStyledDocument());
        
        // initialize scroll pane
        scrollPane = new JScrollPane(textPane);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    // show
    public void show() {
        frame.setVisible(true);
        textPane.requestFocusInWindow();        
    }
    
    // get doc from text pane
    public StyledDocument getDocument () {
	return (DefaultStyledDocument) textPane.getDocument();
    }
    
    // main method for testing
    public static void main(String [] args) {
        BaseEditor e = new BaseEditor();
        e.show();
    }
}
