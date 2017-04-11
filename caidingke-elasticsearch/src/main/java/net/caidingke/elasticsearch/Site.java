package net.caidingke.elasticsearch;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String coordinates;

    public Site(String name, String coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }
}
