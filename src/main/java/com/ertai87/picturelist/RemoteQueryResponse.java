package com.ertai87.picturelist;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RemoteQueryResponse implements Serializable {
    private String id;
    private int createdAt;
    private String name;
    private String imageUrl;
}
