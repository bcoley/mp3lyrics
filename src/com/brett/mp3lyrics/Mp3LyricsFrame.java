package com.brett.mp3lyrics;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class Mp3LyricsFrame {

    private JFrame frame;
    private JTable table;
    private JMenuBar menuBar;
    private JMenu mnNewMenu;
    private JMenuItem openMenuItem;
    private JMenuItem exitMenuItem;

    private Mp3LyricsTableModel sampleTableModel = new Mp3LyricsTableModel();
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Mp3LyricsFrame window = new Mp3LyricsFrame();
                    window.frame.setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Mp3LyricsFrame() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        mnNewMenu = new JMenu("File");
        menuBar.add(mnNewMenu);

        openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                openFileList();
            }
        });

        mnNewMenu.add(openMenuItem);

        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        mnNewMenu.add(exitMenuItem);

        JScrollPane scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        table = new JTable() {
            private static final long serialVersionUID = -1933064680622869913L;

            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                AudioFileUtils audioFileUtils = new AudioFileUtils();
                int realColumnIndex = convertColumnIndexToModel(colIndex);

                if (realColumnIndex == 0) { //Sport column
                    if ("y".equals(getValueAt(rowIndex, colIndex))) {
                        tip = audioFileUtils.getLyrics(sampleTableModel.getFiles()[rowIndex].getAbsolutePath());
                    }
                }
                if (realColumnIndex == 1) { //Sport column
                    tip = sampleTableModel.getFiles()[rowIndex].getAbsolutePath();
                } 
                return tip;
            }
        };

        table.setModel(sampleTableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumnModel columnModel = table.getColumnModel();
        TableColumn firstColumn = columnModel.getColumn(0);
        firstColumn.sizeWidthToFit();
        firstColumn.setMaxWidth(50);
        TableColumn secondColumn = columnModel.getColumn(1);
        secondColumn.setPreferredWidth(250);

        table.setRowSelectionAllowed(true);
        ListSelectionModel selectionModel = table.getSelectionModel();

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (! e.getValueIsAdjusting()) {
                    int[] selectedRow = table.getSelectedRows();

                    for (int i = 0; i < selectedRow.length; i++) {
                        try {
                            String canonicalPath = sampleTableModel.getFiles()[selectedRow[i]].getCanonicalPath();
                            LyricsEditorFrame lyricsEditorFrame = new LyricsEditorFrame(canonicalPath);
                            lyricsEditorFrame.setVisible(true);
                        }
                        catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }

        });
        scrollPane.setViewportView(table);
        openFileList();
    }

    private void openFileList() {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("mp3 files", "mp3");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = fc.getSelectedFiles();
            
            if (files != null && files.length > 0) {
                sampleTableModel.setFiles(files);
                sampleTableModel.fireTableDataChanged();
            }
        }
    }

}
