package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by luisrodriguez on 30/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Listado<T> {
    private Integer total;
    private Integer offset;
    private Integer limit;
    private List<T> content;

    public Listado() {
    }

    public Listado(Integer total, Integer offset, Integer limit, List<T> content) {
        this.total = total;
        this.offset = offset;
        this.limit = limit;
        this.content = content;
    }

    public Integer getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOffset() {
        return offset;
    }

    @JsonProperty("offset")
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    @JsonProperty("limit")
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<T> getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(List<T> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Listado{" +
                "total=" + total +
                ", offset=" + offset +
                ", limit=" + limit +
                ", content=" + content +
                '}';
    }
}
