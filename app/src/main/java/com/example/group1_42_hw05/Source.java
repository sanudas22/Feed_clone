package com.example.group1_42_hw05;

import java.io.Serializable;

public class Source implements Serializable {

    String id, name;

    @Override
    public String toString() {
        return name;
    }
}
