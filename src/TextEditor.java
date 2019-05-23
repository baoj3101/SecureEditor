
/**
 * Text Editor:
 *   A basic text editor extended from BaseEditor that can Open/Edit/Save text file
 *
 * @author baoj3101
 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class TextEditor extends BaseEditor {

    public TextEditor() {
        super();
        setTitle("New File");
    }

    public String getDocument() {
        return textPane.getText();
    }

    // Open file and load into text pane
    public void OpenFile() {
        StringBuilder lines = new StringBuilder();
        try ( BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.append(line).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        textPane.setText(lines.toString());
        setTitle(file.getName());
    }
    
    // Save content from text pane to file
    public void SaveFile() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(getDocument());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // abstract methods
    public void setTitle(String str) {
        frame.setTitle("Text Editor: " + str);
    }

    // main method for testing
    public static void main(String[] args) {
        BaseEditor e = new TextEditor();
        e.show();
    }
}
