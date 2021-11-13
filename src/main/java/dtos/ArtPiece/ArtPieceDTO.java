package dtos.ArtPiece;

import entities.ArtPiece;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArtPieceDTO {
    private int id;
    private int year;
    private String name;
    private String artist;
    private String imageUrl;

    public static List<ArtPieceDTO> getFromList(List<ArtPiece> artCollection) {
        return artCollection.stream()
                .map(artPiece -> new ArtPieceDTO(artPiece))
                .collect(Collectors.toList());
    }

    public ArtPieceDTO(ArtPiece artPiece) {
        this.id = artPiece.getId();
        this.year = artPiece.getYear();
        this.name = artPiece.getName();
        this.artist = artPiece.getArtist();
        this.imageUrl = artPiece.getImageUrl();
    }

    public ArtPieceDTO(int year, String name, String artist, String imageUrl) {
        this.id = -1;
        this.year = year;
        this.name = name;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "ArtPieceDTO{" +
                "id=" + id +
                ", year=" + year +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtPieceDTO that = (ArtPieceDTO) o;
        return id == that.id && year == that.year && Objects.equals(name, that.name) && Objects.equals(artist, that.artist) && Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, name, artist, imageUrl);
    }
}
