package dtos.ArtPiece;

import entities.ArtPiece;

import java.util.List;
import java.util.Objects;

public class ArtPiecesDTO {
    private List<ArtPieceDTO> artCollection;

    public ArtPiecesDTO(List<ArtPiece> artCollection) {
        this.artCollection = ArtPieceDTO.getFromList(artCollection);
    }

    public List<ArtPieceDTO> getArtCollection() {
        return artCollection;
    }

    public void setArtCollection(List<ArtPieceDTO> artCollection) {
        this.artCollection = artCollection;
    }

    public int getSize() {
        int counter = 0;
        for (ArtPieceDTO a : artCollection) {
            counter++;
        }
        return counter;
    }

    @Override
    public String toString() {
        return "ArtPiecesDTO{" +
                "artCollection=" + artCollection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtPiecesDTO that = (ArtPiecesDTO) o;
        return Objects.equals(artCollection, that.artCollection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artCollection);
    }
}
