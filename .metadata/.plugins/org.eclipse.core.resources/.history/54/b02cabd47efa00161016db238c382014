package org.fmaes.simulinktotimedautomata.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

public class ConsoleContextMenu extends JPopupMenu {
  JMenuItem saveOutput;
  JTextArea console;

  public ConsoleContextMenu(JTextArea _console) {
    console = _console;
    console.append("text menu was created");
    saveOutput = new JMenuItem("Save output");
    saveOutput.addActionListener(new SaveAction(_console));
    this.add(saveOutput);
  }
}
