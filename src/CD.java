import java.math.BigDecimal;

public class CD extends MusicItem{

    private int duration;


    public CD(String itemID, String title, String genre, String releaseDate, String artist, BigDecimal price, int duration) {
        super(itemID, title, genre, releaseDate, artist, price);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
