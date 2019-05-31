# APCSA Final Project: A Java SWING-based Multi-Editor 

## Project Concept Document

[Link](https://docs.google.com/document/d/1K4kCTjnqTND0TbxBPEzNEEcZodzrgDc_SKsVpyzpQjc/edit)

## Introduction

This is the 2018-2019 school year **APCSA Final Project**. The final project, Java SWING based Multi-Editor, is intended to demonstrate using Java SWING to implement a few commonly used editors, namely, Text Editor, Rich Editor, and HTML Editor. Also, a Enhanced Text Editor with content encryption and decryption will be implemented in this project.

The project uses the Java interface, abstract class, and inheritance to practice Object-Oriented Programming concepts, and utilizes JPanel and JFrame to provide a user interface for file loading, saving, editing, print, font customization, html rendering, as well as content encryption and decryption.

## Classes

Class/Interface | Description
------------ | -------------
Interface **Encrypt** | This is an interface with two methods: encrypt and decrypt.
Abstract Class **BaseEditor** | This is the base class which contains the common frame, text pane, and file menu, editor switch menu, help menu, as well as menu event handlers. There are three abstract methods: SetTitle, OpenFile, and SaveFile.
Class **TextEditor** extentds BaseEditor | This is extended from BaseEditor with all abstract methods implemented. It's a functional text editor.
Class **HTMLEditor** extentds TextEditor | This is extended from BaseEditor with tool menu for HTML rendering. It's intended for raw HTML editing and rendering.
Class **SecureEditor** extentds TextEditor | This is extended from TextEditor and implements Encrypt interface. It supports **128-bit AES** encryptino and decryption. When a file is saved, the content is encrypted and then encoded using base64. When a file is loaded, the content is decoded using base64 and then decrypted using AES.
Class **RichEditor** extentds BaseEditor | This is extended from BaseEditor and added toolbar for text font customization: font size, font family, font style (Bold/Italic/Underline), and font color.

## Resources

Resources directory contains the following resources for the app:

* App splash image
> splash.png
* Icon image for each type of editor
> html.png
> lock.png
> note.png
> rtf.png
* Font awesome ttf file
> fontawesome-webfont.ttf
* Help message in HTML
> help.html
## Splash

A PNG image is loaded as splash during app startup. The splash contains project name, author and a basic description of the project.

## Icons

Each editor has its own icon using JFrame setIconImage.
```
    // set icon image
    public void setIcon(String iconFile) {
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("resources/" + iconFile));
        frame.setIconImage(img.getImage());
    }
```

## File Open/Save

File menu open and save event handlers are implemented in the BaseEditor class. However, for each type of editor, the file read and write are editor specific and implemented in each inherited class in OpenFile and SaveFile methods (defined as abstract in the BaseEditor).

Four example files are saved under test/ directory as demo. When file dialog is openned, the default location is set to the test direcory.

## Rich Editor Open/Save

Rich Editor open/save uses the RTFEditorKit (import javax.swing.text.rtf.RTFEditorKit) to handle rich text style.
```
        DefaultStyledDocument doc = (DefaultStyledDocument) getDocument();
        RTFEditorKit rtfKit = new RTFEditorKit();
        try {
            rtfKit.write(new FileOutputStream(file), doc, 0, doc.getLength());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
```

## Secure Editor Open/Save

Secure Editor uses the Cipher (import javax.crypto.Cipher) for editor content encryption/decryption. A 128-but AES encryption followed by BASE64 encoding is applied for file saving. When load from a file, BASE64 decoding followed by 128-bit AES decryption is performed before loading into the editor. AES stands for Advanced Encryption Standard and [this page](https://www.comparitech.com/blog/information-security/what-is-aes-encryption/) provides a good explaination of AES algorithm.

## Testing

* Clone in NetBeans
* Build the project
* Run
* Test Editor will open
** Open File: Choose example.text.txt
** Edit: use Ctrl+C/Ctrl+V/Ctrl+X
** Save File
** Print (if printer is available)
** Help -> How To
** Editors: Open HTML Editor
* HTML Editor will open
** Open File: Choose example.html.txt
** Edit File: add a new paragraph
** View -> Show HTML
** View -> Show Plain
** Save File
** Editors: Open Rich Editor
* Rich Editor will open
** Open File: Choose example.rich.txt
** Try customize font style, size, family, and color
** Editors: Open Secure Editor
* Secure Editor will open

