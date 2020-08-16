import java.math.BigDecimal;

public class Vinyl extends MusicItem {

    private double speed;
    private double diameter;

    public Vinyl(String itemID, String title, String genre, String releaseDate, String artist, BigDecimal price,
                 double speed, double diameter) {
        super(itemID, title, genre, releaseDate, artist, price);
        this.speed = speed;
        this.diameter = diameter;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDiameter() {
        return diameter;
    }



}
