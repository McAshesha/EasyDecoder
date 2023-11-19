package ru.ashesha;

public class Decoder {

    private static Decoder INSTANCE = null;

    public static Decoder getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Decoder();
        return INSTANCE;
    }


    private int lfm = 0;

    private Decoder() {
    }

    public void readLfm(int lfm) {
        this.lfm = lfm;
    }

}
