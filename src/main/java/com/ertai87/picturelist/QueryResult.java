package com.ertai87.picturelist;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class QueryResult implements Serializable{
    private boolean isFull;
    private int maxId;
    private List<String> urls;
}
