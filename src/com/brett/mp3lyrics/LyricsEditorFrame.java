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

import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class LyricsEditorFrame extends JFrame {

    private static final long serialVersionUID = 7640736840601774104L;
    
    private JPanel contentPane;
    private JTextField txtFilename;
    private JTextField txtArtist;
    private JTextField txtTitle;
  
    private JTextArea lyricsTextArea;
    
    
    private LyricsFetcher lyricsFetcher = new LyricsFetcher();
    private AudioFileUtils audioFileUtils = new AudioFileUtils();
    


    /**
     * Create the frame.
     */
    public LyricsEditorFrame(final String fileName) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 460, 520);
        
        createMenu();
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        createButtonPanel();
        
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
        populateScreenData(fileName);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        
        JButton lyricsButton = new JButton("Get Lyrics");
        lyricsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
               String lyrics = lyricsFetcher.fetchLyrics(txtArtist.getText(), txtTitle.getText());
               lyricsTextArea.setText(lyrics);
               
            }
        });
        lyricsButton.setHorizontalAlignment(SwingConstants.LEFT);
        buttonPanel.add(lyricsButton);
        
        JButton saveButton = new JButton("Save File");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveFile();
            }
        });
        saveButton.setHorizontalAlignment(SwingConstants.RIGHT);
        buttonPanel.add(saveButton);
    }

    private void createMenu() {
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
                    populateScreenData(selectedFile.getAbsolutePath());
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
    }
    
    private String extractFileName(String mp3FileName) {
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

    private void populateScreenData(String filePath) {
        txtFilename.setText(filePath);
        Tag tag = audioFileUtils.getTag(filePath);
        
        String artist = "";
        String title =  "";
        String existingLyrics = "";
        
        if (tag != null) {
            artist = tag.getFirst(FieldKey.ARTIST);
            title =  tag.getFirst(FieldKey.TITLE);
            existingLyrics = tag.getFirst(FieldKey.LYRICS);
        }
        
        String fileName = extractFileName(filePath);
        
        if (StringUtils.isBlank(artist)) {
            txtArtist.setText(fileName);
        }
        else {
            txtArtist.setText(artist);
        }
        
        if (StringUtils.isBlank(title)) {
            txtTitle.setText(fileName);
        }
        else {
            txtTitle.setText(title);
        }
        
        if (StringUtils.isBlank(existingLyrics)) {
            lyricsTextArea.setText("");
        }
        else {
            lyricsTextArea.setText(existingLyrics);
        }
    }
    
       
    
    private void saveFile() {
        audioFileUtils.updateFile(txtFilename.getText(), txtArtist.getText(), txtTitle.getText(), lyricsTextArea.getText());
    }

}
