package dtos.Gallery;

import dtos.ArtPiece.ArtPieceDTO;
import entities.ArtPiece;
import entities.Gallery;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GalleryDTO {
    private int id;
    private String galleryName;
    private List<ArtPieceDTO> artCollection;

    public static List<GalleryDTO> getFromList(List<Gallery> galleries) {
        return galleries.stream()
                .map(gallery -> new GalleryDTO(gallery))
                .collect(Collectors.toList());
    }

    public GalleryDTO(Gallery gallery) {
        this.id = gallery.getId();
        this.galleryName = gallery.getGalleryName();
        this.artCollection = ArtPieceDTO.getFromList(gallery.getArtCollection());
    }

    public GalleryDTO(String galleryName) {
        this.id = -1;
        this.galleryName = galleryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGalleryName() {
        return galleryName;
    }

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    public List<ArtPieceDTO> getArtCollection() {
        return artCollection;
    }

    public void setArtCollection(List<ArtPieceDTO> artCollection) {
        this.artCollection = artCollection;
    }

    @Override
    public String toString() {
        return "GalleryDTO{" +
                "id=" + id +
                ", galleryName='" + galleryName + '\'' +
                ", artCollection=" + artCollection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GalleryDTO that = (GalleryDTO) o;
        return id == that.id && Objects.equals(galleryName, that.galleryName) && Objects.equals(artCollection, that.artCollection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, galleryName, artCollection);
    }
}
