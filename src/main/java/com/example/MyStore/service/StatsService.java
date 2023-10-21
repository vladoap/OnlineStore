package com.example.MyStore.service;

import com.example.MyStore.model.view.StatsView;

public interface StatsService {

    void onRequest();

    StatsView getStats();
}
