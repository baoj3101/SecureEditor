
/**
 * HTMLEditor Class:
 *   This is extended from TextEditor with added function to render HTML
 *
 * @author baoj3101
 */
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class HTMLEditor extends TextEditor {

    // tool menu items
    JMenuItem showHTML, showPlain;

    // buffer to store text
    private String contents;

    public HTMLEditor() {
        super();
        setTitle("New File");

        // add tool menu: to render HTML
        JMenu toolMenu = new JMenu("View");
        showHTML = new JMenuItem("Show HTML");            // "Show HTML"
        showHTML.addActionListener(new ShowHTMLListener());
        toolMenu.add(showHTML);
        showPlain = new JMenuItem("Show Plain Text");     // "Show Plain Text"
        showPlain.setEnabled(false);
        showPlain.addActionListener(new ShowPlainListener());
        toolMenu.add(showPlain);
        menuBar.add(toolMenu, 1);
    }

    // set title
    public void setTitle(String str) {
        frame.setTitle("HTML Editor: " + str);
    }

    // main method for testing
    public static void main(String[] args) {
        BaseEditor e = new HTMLEditor();
        e.show();
    }

    //==========================================================================
    // Event Handlers for tool menu items
    //==========================================================================
    // tool menu even handler: Show HTML
    protected class ShowHTMLListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // store text pane sting to contents
            contents = textPane.getText();
            textPane.setContentType("text/html");
            textPane.setText(contents);
            showHTML.setEnabled(false);
            showPlain.setEnabled(true);
        }
    }

    // tool menu even handler: Show Plain Text
    protected class ShowPlainListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            textPane.setContentType("text/plain");
            textPane.setText(contents);
            contents = null;
            showHTML.setEnabled(true);
            showPlain.setEnabled(false);
        }
    }

}
