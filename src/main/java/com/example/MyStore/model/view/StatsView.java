package com.example.MyStore.model.view;

public class StatsView {

    private int anonymousRequests;
    private int authRequests;

    public StatsView(int anonymousRequests, int authRequests) {
        this.anonymousRequests = anonymousRequests;
        this.authRequests = authRequests;
    }

    public int getAnonymousRequests() {
        return anonymousRequests;
    }

    public StatsView setAnonymousRequests(int anonymousRequests) {
        this.anonymousRequests = anonymousRequests;
        return this;
    }

    public int getAuthRequests() {
        return authRequests;
    }

    public StatsView setAuthRequests(int authRequests) {
        this.authRequests = authRequests;
        return this;
    }
}
