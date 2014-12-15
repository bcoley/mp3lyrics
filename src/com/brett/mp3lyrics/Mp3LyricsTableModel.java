package com.brett.mp3lyrics;

import java.io.File;

import javax.swing.table.AbstractTableModel;

public class Mp3LyricsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 620340418889524722L;

    public Mp3LyricsTableModel() {
    }

    private File[] files = null;

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        if (files != null) {
            return files.length;
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getValueAt(int row, int col) {
        
        if ((files != null) &&
                (row < files.length)) {
            if (col == 1) {
                return files[row].getName();
            }
            else if (col == 0) {
                //TODO: Y or N if file has existing lyrics
                return "?";
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public File[] getFiles() {
        return files;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }

    public String getColumnName(int col) {
        if (col == 0) {
            return "lyrics";
        }
        else if (col == 1) {
            return "file name";
        }
        else {
            return null;
        }
    }

}