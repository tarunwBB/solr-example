package org.example;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.common.annotation.JsonProperty;

import java.util.List;

@Data
public class Sample {
    public Sample(String id, List<String> name) {
        this.id = id;
        this.name = name;
    }

    @Field("id")
    @JsonProperty("id")
    private String id;
    @JsonProperty("cat")
    private List<String> cat;
    @Field("name")
    @JsonProperty("name")
    private List<String> name;
    @JsonProperty("price")
    private List<Double> price;
    @JsonProperty("inStock")
    private List<Boolean> inStock;
    @JsonProperty("author")
    private List<String> author;
    @JsonProperty("series_t")
    private String seriesT;
    @JsonProperty("sequence_i")
    private Integer sequenceI;
    @JsonProperty("genre_s")
    private String genreS;
    @JsonProperty("_version_")
    private Long version;
}
