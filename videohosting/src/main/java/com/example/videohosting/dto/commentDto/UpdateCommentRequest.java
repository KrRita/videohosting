package com.example.videohosting.dto.commentDto;

import jakarta.validation.constraints.NotBlank;

public class UpdateCommentRequest {
    @NotBlank
    private String text;

    public UpdateCommentRequest(String text) {
        this.text = text;
    }

    public UpdateCommentRequest() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateCommentRequest that = (UpdateCommentRequest) o;

        return text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    @Override
    public String toString() {
        return "UpdateCommentRequest{" +
               "text='" + text + '\'' +
               '}';
    }
}
