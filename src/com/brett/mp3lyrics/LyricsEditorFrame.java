package com.brett.mp3lyrics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class LyricsEditorFrame extends JFrame {

    private static final long serialVersionUID = 7640736840601774104L;
    
    private JPanel contentPane;
    private JTextField txtFilename;
    private JTextField txtArtist;
    private JTextField txtTitle;
    
    private LyricsFetcher lyricsFetcher = new LyricsFetcher();
    private JTextArea lyricsTextArea;


    /**
     * Create the frame.
     */
    public LyricsEditorFrame(final String fileName) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);
        
        JMenuItem mntmOpen = new JMenuItem("Open");
        mntmOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    processFile(selectedFile.getAbsolutePath());
                }
            }
        
        });
        mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        mnFile.add(mntmOpen);
        
        JMenuItem mntmSave = new JMenuItem("Save");
        mntmSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveFile();
            }
        });
        mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        mnFile.add(mntmSave);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        
        JButton btnNewButton_1 = new JButton("Get Lyrics");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
               String lyrics = lyricsFetcher.fetchLyrics(txtArtist.getText(), txtTitle.getText());
               lyricsTextArea.setText(lyrics);
               
            }
        });
        btnNewButton_1.setHorizontalAlignment(SwingConstants.LEFT);
        buttonPanel.add(btnNewButton_1);
        
        JButton btnNewButton = new JButton("Save File");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveFile();
            }
        });
        btnNewButton.setHorizontalAlignment(SwingConstants.RIGHT);
        buttonPanel.add(btnNewButton);
        
        JPanel dataPanel = new JPanel();
        dataPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPane.add(dataPanel, BorderLayout.NORTH);
        dataPanel.setLayout(new GridLayout(3, 2, 2, 2));
        
        JLabel lblFile = new JLabel("File");
        dataPanel.add(lblFile);
        
        txtFilename = new JTextField();
        txtFilename.setText("");
        dataPanel.add(txtFilename);
        
        JLabel lblTitle = new JLabel("Title");
        dataPanel.add(lblTitle);
        
        txtTitle = new JTextField();
        txtTitle.setText("");
        dataPanel.add(txtTitle);
        
        
        JLabel lblArtist = new JLabel("Artist");
        dataPanel.add(lblArtist);
        
        txtArtist = new JTextField();
        txtArtist.setText("");
        dataPanel.add(txtArtist);
        
        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        lyricsTextArea = new JTextArea();
        scrollPane.setViewportView(lyricsTextArea);
        processFile(fileName);
    }
    
    private String processFileName(String mp3FileName) {
        String result = null;
        String fileName = mp3FileName;
        // split off path info...
        if (mp3FileName.contains(File.separator)) {
            fileName = mp3FileName.substring(1 + mp3FileName.lastIndexOf(File.separator));
            result = fileName;
        }
        // split off suffix...
        if (fileName.contains(".mp3")) {
        String [] parts = fileName.split(".mp3");
            result = parts[0];
        }
        return result;
    }

    private void processFile(String filePath) {
        txtFilename.setText(filePath);
        MP3File mp3File = readAudioFile(filePath);
        Tag tag = mp3File.getTag();
        String artist = tag.getFirst(FieldKey.ARTIST);
        String title =  tag.getFirst(FieldKey.TITLE);
        String existingLyrics = tag.getFirst(FieldKey.LYRICS);
        
        String fileName = processFileName(filePath);
        
        if (isEmpty(artist)) {
            txtArtist.setText(fileName);
        }
        else {
            txtArtist.setText(artist);
        }
        
        if (isEmpty(title)) {
            txtTitle.setText(fileName);
        }
        else {
            txtTitle.setText(title);
        }
        
        if (isEmpty(existingLyrics)) {
            lyricsTextArea.setText("");
        }
        else {
            lyricsTextArea.setText(existingLyrics);
        }
    }
    
    
    private boolean isEmpty(String str) {
        if (null == str) {
            return true;
        }
        if (str.length() == 0) {
            return true;
        }
        return str.isEmpty();
    }
    
    private MP3File readAudioFile(String fileName) {
        File testFile = new File(fileName);

        MP3File mp3File = null;
        try {
            mp3File = new MP3File(testFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return mp3File;
    }
    
    private void saveFile() {
        lyricsFetcher.updateFile(txtFilename.getText(), txtArtist.getText(), txtTitle.getText(), lyricsTextArea.getText());
    }

}
