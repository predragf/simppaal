package org.fmaes.simulinktotimedautomata.gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SaveAction extends AbstractAction {

  private JTextArea console;
  private String lastUsedBrowseLocation;

  public SaveAction(JTextArea _console) {
    console = _console;
    lastUsedBrowseLocation = ".";
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    JFileChooser fc = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
    fc.setFileFilter(filter);
    fc.setCurrentDirectory(new java.io.File(lastUsedBrowseLocation));
    int returnVal = fc.showSaveDialog(console);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File selectedFolder = fc.getSelectedFile();
      lastUsedBrowseLocation = fc.getSelectedFile().getParent();
      String fileLocation = selectedFolder.getPath();
      SaveConsole(fileLocation);
    }
  }

  private void SaveConsole(String location) {
    if (!location.endsWith(".txt") && !location.endsWith(".log")) {
      location = location += ".txt";
    }
    try (PrintStream out = new PrintStream(new FileOutputStream(location))) {
      out.print(console.getText());
      console.append("The log successfully saved.");
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      console.append(e.getMessage());
    }
  }

}
