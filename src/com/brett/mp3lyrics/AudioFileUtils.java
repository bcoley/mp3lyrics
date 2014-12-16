package com.brett.mp3lyrics;

import java.io.File;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class AudioFileUtils {

    public Tag getTag(String fileName) {
        Tag tag = null;
        MP3File mp3File = readAudioFile(fileName);
        if (mp3File != null) {
            tag = mp3File.getTag();
        }
        return tag;
    }

    public String getLyrics(String fileName) {
        String lyrics = null;
        MP3File mp3File = readAudioFile(fileName);
        if (mp3File != null) {
            Tag tag = mp3File.getTag();
            if (tag != null) {
                lyrics = tag.getFirst(FieldKey.LYRICS);
            }
        }
        return lyrics;
    }

    public MP3File readAudioFile(String fileName) {
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

    public void updateFile(String fileName, String lyrics) {
        File file = new File(fileName);
        AudioFile f = null;
        try {
            f = AudioFileIO.read(file);
            Tag tag = f.getTag();
            tag.setField(FieldKey.LYRICS, lyrics);
            AudioFileIO.write(f);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateFile(String fileName, String artist, String title, String lyrics) {
        File file = new File(fileName);
        AudioFile f = null;
        try {
            f = AudioFileIO.read(file);
            Tag tag = f.getTag();
            tag.setField(FieldKey.LYRICS, lyrics);
            tag.setField(FieldKey.ARTIST, artist);
            tag.setField(FieldKey.TITLE, title);
            AudioFileIO.write(f);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
