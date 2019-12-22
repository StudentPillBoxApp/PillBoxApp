package com.example.pillboxesapp;

import java.util.ArrayList;

public interface ICallbackResult {
    void onCallback(ArrayList result);

    void onCallback(String[] result);

    void onCallback(Boolean result);
}
