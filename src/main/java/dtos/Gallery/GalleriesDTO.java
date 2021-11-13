package dtos.Gallery;

import entities.Gallery;

import java.util.List;
import java.util.Objects;

public class GalleriesDTO {
    private List<GalleryDTO> galleries;

    public GalleriesDTO(List<Gallery> galleries) {
        this.galleries = GalleryDTO.getFromList(galleries);
    }

    public List<GalleryDTO> getGalleries() {
        return galleries;
    }

    public void setGalleries(List<GalleryDTO> galleries) {
        this.galleries = galleries;
    }

    @Override
    public String toString() {
        return "GalleriesDTO{" +
                "galleries=" + galleries +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GalleriesDTO that = (GalleriesDTO) o;
        return Objects.equals(galleries, that.galleries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(galleries);
    }
}
