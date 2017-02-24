package org.fmaes.simulinktotimedautomata.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.fmaes.j2uppaal.datastructures.uppaalstructures.UppaalDocument;
import org.fmaes.simulinktotimedautomata.configuration.ApplicationConfiguration;
import org.fmaes.simulinktotimedautomata.transformers.SimulinkModelTransformer;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class SIMPPAAL {

  private JFrame frame;
  private JTextField txtSimulinkModelFolder;
  private JButton btnBrowseTAFile;
  private JTextField txtTAFile;
  private JTextArea txtConsole;
  private JLabel lblSelectTimedautomataFile;
  private JLabel lblConsole;
  private JButton btnStartTransformation;

  private JLabel lblSortedOrderList;
  private JButton btnBrowseSList;
  private JTextField txtSlistPath;

  private ConsoleContextMenu consoleMenu;

  private String lastUsedBrowseLocation;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          SIMPPAAL window = new SIMPPAAL();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public SIMPPAAL() {
    initialize();
    checkInput();
    lastUsedBrowseLocation = ".";
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 691, 518);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);

    JLabel lblSelectSimulinkModel = new JLabel("Select Simulink Model Folder");
    lblSelectSimulinkModel.setBounds(35, 26, 191, 16);
    frame.getContentPane().add(lblSelectSimulinkModel);



    JButton btnBrowseModelFolder = new JButton("Browse");
    btnBrowseModelFolder.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.mdl", "mdl");
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(new java.io.File(lastUsedBrowseLocation));
        int returnVal = fc.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File selectedFolder = fc.getSelectedFile();
          lastUsedBrowseLocation = fc.getSelectedFile().getParent();
          txtSimulinkModelFolder.setText(selectedFolder.getPath());
        }
      }
    });
    btnBrowseModelFolder.setBounds(35, 54, 117, 29);
    frame.getContentPane().add(btnBrowseModelFolder);

    txtConsole = new JTextArea();
    consoleMenu = new ConsoleContextMenu(txtConsole);
    txtConsole.setComponentPopupMenu(consoleMenu);

    JScrollPane srlConsole = new JScrollPane(txtConsole);
    srlConsole.setBounds(35, 261, 563, 190);
    frame.getContentPane().add(srlConsole);



    txtSimulinkModelFolder = new JTextField();
    txtSimulinkModelFolder.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
        checkInput();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
        checkInput();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
      }
    });
    txtSimulinkModelFolder.setEditable(false);
    txtSimulinkModelFolder.setBounds(164, 57, 434, 26);
    frame.getContentPane().add(txtSimulinkModelFolder);
    txtSimulinkModelFolder.setColumns(45);

    btnBrowseTAFile = new JButton("Browse");
    btnBrowseTAFile.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setApproveButtonText("Select");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xml", "xml");
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(new java.io.File(lastUsedBrowseLocation)); // start at application
                                                                          // current directory
        // fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File selectedFolder = fc.getSelectedFile();
          lastUsedBrowseLocation = fc.getSelectedFile().getParent();
          txtTAFile.setText(selectedFolder.getPath());
        }
      }
    });
    btnBrowseTAFile.setBounds(35, 185, 117, 29);
    frame.getContentPane().add(btnBrowseTAFile);

    txtTAFile = new JTextField();

    txtTAFile.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
        checkInput();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
        checkInput();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
      }
    });
    txtTAFile.setEditable(false);
    txtTAFile.setBounds(164, 188, 434, 26);
    frame.getContentPane().add(txtTAFile);
    txtTAFile.setColumns(10);

    lblSelectTimedautomataFile = new JLabel("Select Timed Automata File");
    lblSelectTimedautomataFile.setBounds(35, 157, 191, 16);
    frame.getContentPane().add(lblSelectTimedautomataFile);

    lblConsole = new JLabel("Console");
    lblConsole.setBounds(35, 233, 61, 16);
    frame.getContentPane().add(lblConsole);

    btnStartTransformation = new JButton("Start");
    btnStartTransformation.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!validate()) {
          return;
        }
        txtConsole.setText("");
        JTextAreaOutputStream out = new JTextAreaOutputStream(txtConsole);
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);
        System.setErr(ps);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
          @Override
          public void run() {
            Util.executeTransformationProcedure(txtSimulinkModelFolder.getText(),
                txtSlistPath.getText(), txtTAFile.getText());
          }
        });
      }
    });
    btnStartTransformation.setBounds(35, 463, 117, 29);
    frame.getContentPane().add(btnStartTransformation);

    lblSortedOrderList = new JLabel("Sorted Order List");
    lblSortedOrderList.setBounds(35, 95, 191, 16);
    frame.getContentPane().add(lblSortedOrderList);

    btnBrowseSList = new JButton("Browse");
    btnBrowseSList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(new java.io.File(lastUsedBrowseLocation));
        int returnVal = fc.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File selectedFolder = fc.getSelectedFile();
          lastUsedBrowseLocation = fc.getSelectedFile().getParent();
          txtSlistPath.setText(selectedFolder.getPath());
        }
      }
    });
    btnBrowseSList.setBounds(35, 116, 117, 29);
    frame.getContentPane().add(btnBrowseSList);

    txtSlistPath = new JTextField();

    txtSlistPath.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
        checkInput();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
        checkInput();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub
      }
    });

    txtSlistPath.setEditable(false);
    txtSlistPath.setBounds(164, 116, 434, 26);
    frame.getContentPane().add(txtSlistPath);
    txtSlistPath.setColumns(10);

    JButton btnReset = new JButton("Reset");
    btnReset.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        reset();
      }
    });
    btnReset.setBounds(160, 463, 117, 29);
    frame.getContentPane().add(btnReset);
  }

  private void checkInput() {
    Boolean shouldButtonBeEnabled = !txtSimulinkModelFolder.getText().isEmpty()
        && !txtSlistPath.getText().isEmpty() && !txtTAFile.getText().isEmpty();
    btnStartTransformation.setEnabled(shouldButtonBeEnabled);
  }

  private void reset() {
    String emptyString = "";
    txtSimulinkModelFolder.setText(emptyString);
    txtSlistPath.setText(emptyString);
    txtTAFile.setText(emptyString);
    txtConsole.setText(emptyString);
    lastUsedBrowseLocation = ".";
    btnStartTransformation.setEnabled(false);
  }

  private Boolean validate() {
    Boolean isValid = true;
    if (txtSlistPath.getText() == null || !txtSlistPath.getText().endsWith(".txt")) {
      isValid = false;
      txtSlistPath.setText("");
    }
    if (txtSimulinkModelFolder.getText() == null
        || !txtSimulinkModelFolder.getText().endsWith(".mdl")) {
      isValid = false;
      txtSimulinkModelFolder.setText("");
    }
    if (txtTAFile.getText() == null || !txtTAFile.getText().endsWith(".xml")) {
      isValid = false;
      txtTAFile.setText("");
    }
    return isValid;
  }
}
