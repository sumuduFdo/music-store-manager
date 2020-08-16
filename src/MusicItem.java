import java.math.BigDecimal;
import java.util.Objects;

public abstract class MusicItem implements Comparable<MusicItem> {

    private String itemID;
    private String title;
    private String genre;
    private String releaseDate;
    private String artist;
    private BigDecimal price;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MusicItem)) return false;
        MusicItem musicItem = (MusicItem) o;
        return getItemID().equals(musicItem.getItemID()) &&
                getTitle().equals(musicItem.getTitle()) &&
                getGenre().equals(musicItem.getGenre()) &&
                getReleaseDate().equals(musicItem.getReleaseDate()) &&
                getArtist().equals(musicItem.getArtist()) &&
                getPrice().equals(musicItem.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemID(), getTitle(), getGenre(), getReleaseDate(), getArtist(), getPrice());
    }

    public MusicItem(String itemID, String title, String genre, String releaseDate, String artist, BigDecimal price) {
        this.itemID = itemID;
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.artist = artist;
        this.price = price;
    }

    @Override
    public String toString() {
        return "MusicItem{" +
                "itemID='" + itemID + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", artist='" + artist + '\'' +
                ", price=" + price +
                '}';
    }

    public String getItemID() {
        return itemID;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getArtist() {
        return artist;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public int compareTo(MusicItem obj){
        return this.title.compareTo(obj.getTitle());
    }


}
