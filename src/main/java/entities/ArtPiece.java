package entities;

import javax.persistence.*;

@Entity
public class ArtPiece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private int year;
    private String name;
    private String artist;
    private String imageUrl;

    @ManyToOne
    private Gallery gallery;

    public ArtPiece() {
    }

    public ArtPiece(int year, String name, String artist, String imageUrl) {
        this.year = year;
        this.name = name;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }

    public ArtPiece(int year, String artist, String imageUrl, Gallery gallery) {
        this.year = year;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.gallery = gallery;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}
