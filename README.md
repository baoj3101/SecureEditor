# APCSA Final Project: A Java SWING-based Multi-Editor 

## Introduction

This is the 2018-2019 school year **APCSA Final Project**. The final project, Java SWING based Multi-Editor, is intended to demonstrate using Java SWING to implement a few commonly used editors, namely, Text Editor, Rich Editor, and HTML Editor. Also, a Enhanced Text Editor with content encryption and decryption will be implemented in this project.

The project uses the Java interface, abstract class, and inheritance to practice Object-Oriented Programming concepts, and utilizes JPanel and JFrame to provide a user interface for file loading, saving, editing, print, font customization, html rendering, as well as content encryption and decryption.

## Classes

**Encrypt**: This is an interface with two methods: encrypt and decrypt.

**BaseEditor**: This is the base class which contains the common frame, text pane, and file menu, editor switch menu, help menu, as well as menu event handlers. There are three abstract methods: SetTitle, OpenFile, and SaveFile.

**TextEditor**: This is extended from BaseEditor with all abstract methods implemented. It's a functional text editor.

**RichEditor**: This is extended from BaseEditor and added toolbar for text font customization: font size, font family, font style (Bold/Italic/Underline), and font color.

**HTMLEditor**: This is extended from BaseEditor with tool menu for HTML rendering. It's intended for raw HTML editing and rendering.

**SecureEditor**: This is extended from TextEditor and implements Encrypt interface. It supports 128-bit AES encryptino and decryption. When a file is saved, the content is encrypted and then encoded using base64. When a file is loaded, the content is decoded using base64 and then decrypted using AES.

## Resources

Resources directory contains app splash PNG, icon image for each type of editor, font awesome ttf file, and help message in HTML.

## Splash

A PNG image is loaded as splash during app startup. The splash contains project name, author and a basic description of the project.

## Icons

Each editor has its own icon using JFrame setIconImage.

## File Open/Save

File menu open and save event handlers are implemented in the BaseEditor class. However, for each type of editor, the file read and write are editor specific and implemented in each inherited class in OpenFile and SaveFile methods (defined as abstract in the BaseEditor).

## Rich Editor Open/Save

Rich Editor open/save uses the RTFEditorKit (import javax.swing.text.rtf.RTFEditorKit) to handle rich text style.

## Secure Editor Open/Save

Secure Editor uses the Cipher (import javax.crypto.Cipher) for editor content encryption/decryption. A 128-but AES encryption followed by BASE64 encoding is applied for file saving. When load from a file, BASE64 decoding followed by 128-bit AES decryption is performed before loading into the editor. AES stands for Advanced Encryption Standard and [this page](https://www.comparitech.com/blog/information-security/what-is-aes-encryption/) provides a good explaination of AES algorithm.

