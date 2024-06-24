package com.thaivan.maxme.model;

public interface CardReaderListener {
    void onSuccess(Card card);
    void onFail(String errMsg);
}
