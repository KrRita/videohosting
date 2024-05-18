package com.example.videohosting.dto.viewedVideoDto;

import com.example.videohosting.dto.videoDto.PreviewVideoResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.sql.Timestamp;

public class ViewedVideoResponse {
    @NotNull
    private PreviewVideoResponse previewVideoResponse;
    @NotNull
    @Past
    private Timestamp dateOfViewing;

    public ViewedVideoResponse(PreviewVideoResponse previewVideoResponse, Timestamp dateOfViewing) {
        this.previewVideoResponse = previewVideoResponse;
        this.dateOfViewing = dateOfViewing;
    }

    public ViewedVideoResponse() {
    }

    public PreviewVideoResponse getPreviewVideoResponse() {
        return previewVideoResponse;
    }

    public void setPreviewVideoResponse(PreviewVideoResponse previewVideoResponse) {
        this.previewVideoResponse = previewVideoResponse;
    }

    public Timestamp getDateOfViewing() {
        return dateOfViewing;
    }

    public void setDateOfViewing(Timestamp dateOfViewing) {
        this.dateOfViewing = dateOfViewing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewedVideoResponse that = (ViewedVideoResponse) o;

        if (!previewVideoResponse.equals(that.previewVideoResponse)) return false;
        return dateOfViewing.equals(that.dateOfViewing);
    }

    @Override
    public int hashCode() {
        int result = previewVideoResponse.hashCode();
        result = 31 * result + dateOfViewing.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ViewedVideoResponse{" +
               "previewVideoResponse=" + previewVideoResponse +
               ", dateOfViewing=" + dateOfViewing +
               '}';
    }
}
