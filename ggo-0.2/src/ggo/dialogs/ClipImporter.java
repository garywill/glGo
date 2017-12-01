/*
 * ClipImporter.java
 *
 *  gGo
 *  Copyright (C) 2002  Peter Strempel <pstrempel@t-online.de>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package ggo.dialogs;

import javax.swing.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import ggo.*;
import ggo.utils.SwingWorker;

/**
 *  Dialog to import text from the clipboard or a file
 *
 *@author     Peter Strempel
 *@version    $Revision: 1.3 $, $Date: 2002/10/08 14:47:35 $
 */
public class ClipImporter extends javax.swing.JFrame {
    private Clipboard clipboard;
    private MainFrame mainFrame;
    private ResourceBundle clipboard_dialog_resources;
    private boolean sgf;  // Currently unused

    /** ClipImporter() constructor */
    public ClipImporter(MainFrame mainFrame, boolean sgf) {
        this.mainFrame = mainFrame;
        this.sgf = sgf;

        clipboard_dialog_resources = gGo.getClipboardDialogResources();
        initComponents();
        clipboard = getToolkit().getSystemClipboard();
        setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        javax.swing.JButton fileButton;
        javax.swing.JScrollPane scrollPane;
        javax.swing.JButton loadButton;
        javax.swing.JPanel mainPanel;
        javax.swing.JButton pasteButton;
        javax.swing.JButton clearButton;
        javax.swing.JPanel buttonPanel;
        javax.swing.JButton closeButton;

        mainPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        buttonPanel = new javax.swing.JPanel();
        fileButton = new javax.swing.JButton();
        pasteButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setTitle(clipboard_dialog_resources.getString("Import_game"));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        mainPanel.setLayout(new java.awt.BorderLayout());

        scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));
        scrollPane.setViewportView(textArea);

        mainPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        buttonPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 5, 2, 5)));
        fileButton.setText(clipboard_dialog_resources.getString("file_button_text"));
        fileButton.setToolTipText(clipboard_dialog_resources.getString("file_button_tooltip"));
        fileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(fileButton);

        pasteButton.setText(clipboard_dialog_resources.getString("paste_button_text"));
        pasteButton.setToolTipText(clipboard_dialog_resources.getString("paste_button_tooltip"));
        pasteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(pasteButton);

        clearButton.setText(clipboard_dialog_resources.getString("clear_button_text"));
        clearButton.setToolTipText(clipboard_dialog_resources.getString("clear_button_tooltip"));
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(clearButton);

        loadButton.setText(clipboard_dialog_resources.getString("load_button_text"));
        loadButton.setToolTipText(clipboard_dialog_resources.getString("load_button_tooltip"));
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(loadButton);

        closeButton.setText(clipboard_dialog_resources.getString("close_button_text"));
        closeButton.setToolTipText(clipboard_dialog_resources.getString("close_button_tooltip"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(closeButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }//GEN-END:initComponents

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        textArea.setText("");
    }//GEN-LAST:event_clearButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        exitForm(null);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void displayError(String txt) {
        JOptionPane.showMessageDialog(
                this,
                txt,
                MessageFormat.format(
                gGo.getSGFResources().getString("file_read_error_title"),
                new Object[]{gGo.getSGFResources().getString("SGF")}),
                JOptionPane.ERROR_MESSAGE);
    }

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        final Board board = mainFrame.getBoard();
        final String s = textArea.getText();

        final SwingWorker worker =
        new SwingWorker() {
            public Object construct() {
                board.lock();
                if (board.openFromString(s)) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            }

            public void finished() {
                board.unlock();
                board.repaint();
            }
        };
        worker.start(); //required for SwingWorker 3
    }//GEN-LAST:event_loadButtonActionPerformed

    private void pasteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteButtonActionPerformed
        try {
            Transferable data = clipboard.getContents(textArea);

            // TODO: i18n

            // Empty?
            if (data == null) {
                displayError(clipboard_dialog_resources.getString("clipboard_empty_error"));
                return;
            }

            // No text data?
            if (!data.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                displayError(clipboard_dialog_resources.getString("invalid_clipboard_error"));
                return;
            }

            // All ok, display text in textArea
            textArea.setText((String)data.getTransferData(DataFlavor.stringFlavor));
        } catch (NullPointerException e) {
            System.err.println("Failed to access clipboard data: " + e);
            displayError(clipboard_dialog_resources.getString("clipboard_access_failed_error"));
        } catch (IllegalStateException e) {
            System.err.println("Failed to access clipboard: " + e);
            displayError(clipboard_dialog_resources.getString("clipboard_access_failed_error."));
        } catch (IOException e) {
            System.err.println("Failed to access clipboard data: " + e);
            displayError(clipboard_dialog_resources.getString("clipboard_data_access_failed_error"));
        } catch (UnsupportedFlavorException e) {
            System.err.println("Invalid clipboard data: " + e);
            displayError(clipboard_dialog_resources.getString("clipboard_data_access_failed_error"));
        }
    }//GEN-LAST:event_pasteButtonActionPerformed

    private void fileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileButtonActionPerformed
        final JFileChooser fc = new JFileChooser();  // Use default directory
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String s = parseFileContent(fc.getSelectedFile());
            if (s != null && s.length() > 0)
                textArea.setText(s);
            else
                displayError(gGo.getSGFResources().getString("file_empty_error"));
        }
    }//GEN-LAST:event_fileButtonActionPerformed

    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        setVisible(false);
    }//GEN-LAST:event_exitForm

    private String parseFileContent(File file) {
        StringBuffer s = new StringBuffer();
        try {
            if (!file.exists()) {
                displayError(
                MessageFormat.format(gGo.getSGFResources().getString("file_does_not_exist_error"), new Object[]{file.getName()}));
                return null;
            }
            if (!file.canRead()) {
                displayError(
                MessageFormat.format(gGo.getSGFResources().getString("cannot_read_file_error"), new Object[]{file.getName()}));
                return null;
            }

            BufferedReader f = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = f.readLine()) != null) {
                s.append(line + "\n");
            }
            f.close();
        } catch (IOException e) {
            System.err.println("Error reading file '" + file.getName() + "': " + e);
            displayError(
            MessageFormat.format(gGo.getSGFResources().getString("reading_file_error"), new Object[]{file.getName()}));
            return null;
        } catch (SecurityException e) {
            displayError(gGo.getSGFResources().getString("access_denied_error"));
            return null;
        }
        return s.toString();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
