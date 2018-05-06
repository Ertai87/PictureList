package com.ertai87.picturelist;

import lombok.Data;

import java.io.Serializable;

@Data
public class RemoteQueryResponse implements Serializable {
    private String id;
    private int createdAt;
    private String name;
    private String imageUrl;
}
