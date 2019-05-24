# A Java SWING-based Multi-Editor 

## Introduction

This is the 2018-2019 school year **APCSA Final Project**. The final project, Java SWING based Multi-Editor, is intended to demonstrate using Java SWING to implement a few common used editor, such as Text Editor, Rich Editor, and HTML Editor. Also, a Enhanced Text Editor with content encryption and decryption will be implemented in this project.

The project uses the Java interface, abstract class, and inheritance to practice Object-Oriented Programming concepts, and utilizes JPanel and JFrame to provide a user interface for file loading, saving, editor, font customization, html rendering, as well as content encryption and decryption.

# Classes

**Encrypt**: This is an interface with two methods: encrypt and decrypt.

**BaseEditor**: This is the base class which contains the common frame, text pane, and file menu, as well as file menu event handlers. There are three abstract methods: SetTitle, OpenFile, and SaveFile.

**TextEditor**: This is extended from BaseEditor with all abstract methods implemented. It's a functional text editor.

**RichEditor**: This is extended from BaseEditor and added toolbar for text font customization: font size, font family, font style (Bold/Italic/Underline), and font color.

**HTMLEditor**: This is extended from BaseEditor with tool menu for HTML rendering. It's intended for raw HTML editing and rendering.

**SecureEditor**: This is extended from TextEditor and implements Encrypt interface. It supports AES encryptino and decryption. When a file is saved, the content is encrypted and then encoded using base64. When a file is loaded, the content is decoded using base64 and then decrypted using AES.

