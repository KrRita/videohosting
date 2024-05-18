package com.example.videohosting.dto.userDto;

import jakarta.validation.constraints.NotNull;

public class UpdateSubscriptionsRequest {
    @NotNull
    private Long idSubscription;

    public UpdateSubscriptionsRequest(Long idSubscription) {
        this.idSubscription = idSubscription;
    }

    public UpdateSubscriptionsRequest() {
    }

    public Long getIdSubscription() {
        return idSubscription;
    }

    public void setIdSubscription(Long idSubscription) {
        this.idSubscription = idSubscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateSubscriptionsRequest that = (UpdateSubscriptionsRequest) o;

        return idSubscription.equals(that.idSubscription);
    }

    @Override
    public int hashCode() {
        return idSubscription.hashCode();
    }

    @Override
    public String toString() {
        return "UpdateSubscriptionsRequest{" +
               "idSubscription=" + idSubscription +
               '}';
    }
}
