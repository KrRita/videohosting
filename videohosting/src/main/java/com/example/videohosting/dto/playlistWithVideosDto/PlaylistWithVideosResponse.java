package com.example.videohosting.dto.playlistWithVideosDto;

import com.example.videohosting.dto.videoDto.PreviewVideoResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.sql.Timestamp;
import java.util.Objects;

public class PlaylistWithVideosResponse {
    @NotNull
    private PreviewVideoResponse previewVideoResponse;
    @NotNull
    @Past
    private Timestamp dateOfAddition;

    public PlaylistWithVideosResponse(PreviewVideoResponse previewVideoResponse, Timestamp dateOfAddition) {
        this.previewVideoResponse = previewVideoResponse;
        this.dateOfAddition = dateOfAddition;
    }

    public PlaylistWithVideosResponse() {
    }

    public PreviewVideoResponse getPreviewVideoResponse() {
        return previewVideoResponse;
    }

    public void setPreviewVideoResponse(PreviewVideoResponse previewVideoResponse) {
        this.previewVideoResponse = previewVideoResponse;
    }

    public Timestamp getDateOfAddition() {
        return dateOfAddition;
    }

    public void setDateOfAddition(Timestamp dateOfAddition) {
        this.dateOfAddition = dateOfAddition;
    }


    @Override
    public String toString() {
        return "PlaylistWithVideosResponse{" +
               "previewVideoResponse=" + previewVideoResponse +
               ", dateOfAddition=" + dateOfAddition +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistWithVideosResponse that = (PlaylistWithVideosResponse) o;

        if (!Objects.equals(previewVideoResponse, that.previewVideoResponse))
            return false;
        return Objects.equals(dateOfAddition, that.dateOfAddition);
    }

    @Override
    public int hashCode() {
        int result = previewVideoResponse != null ? previewVideoResponse.hashCode() : 0;
        result = 31 * result + (dateOfAddition != null ? dateOfAddition.hashCode() : 0);
        return result;
    }
}
