package com.middleshelf.handsfreeeartrainer;

/**
 * Created by walta on 7/5/15.
 */
public class HandsFreeEarTrainerUtilities {
    //Mod by 12 to use any range of notes
    public String getNoteName(int note){
        String noteName;
        note = note%12;

        switch(note){
            case(0):
                noteName = "C";
                break;
            case(1):
                noteName = "C#";
                break;
            case(2):
                noteName = "D";
                break;
            case(3):
                noteName = "Eb";
                break;
            case(4):
                noteName = "E";
                break;
            case(5):
                noteName = "F";
                break;
            case(6):
                noteName = "F#";
                break;
            case(7):
                noteName = "G";
                break;
            case(8):
                noteName = "G#";
                break;
            case(9):
                noteName = "A";
                break;
            case(10):
                noteName = "Bb";
                break;
            case(11):
                noteName = "B";
                break;
            default:
                noteName = "C";
        }

        return noteName;
    }
}
