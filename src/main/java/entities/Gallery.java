package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String galleryName;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.PERSIST)
    private List<ArtPiece> artCollection;

    public Gallery() {
    }

    public Gallery(String galleryName) {
        this.galleryName = galleryName;
        this.artCollection = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGalleryName() {
        return galleryName;
    }

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    public List<ArtPiece> getArtCollection() {
        return artCollection;
    }

    public void setArtCollection(List<ArtPiece> artCollection) {
        this.artCollection = artCollection;
    }

    public void addArtPiece(ArtPiece artPiece) {
        this.artCollection.add(artPiece);
        if (artPiece != null) {
            artPiece.setGallery(this);
        }
    }
}
