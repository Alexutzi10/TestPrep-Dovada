package com.example.testprep_dovada.network;

public interface Callback<R> {
    void runResultOnUIThread(R result);
}
