package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 4/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvizorCategoryWrapper {
    private AvizorCategory avizorCategory;

    public AvizorCategoryWrapper() {
    }

    public AvizorCategoryWrapper(AvizorCategory avizorCategory) {
        this.avizorCategory = avizorCategory;
    }

    public AvizorCategory getAvizorCategory() {
        return avizorCategory;
    }

    @JsonProperty("category")
    public void setAvizorCategory(AvizorCategory avizorCategory) {
        this.avizorCategory = avizorCategory;
    }

    @Override
    public String toString() {
        return "AvizorCategoryWrapper{" +
                "avizorCategory=" + avizorCategory +
                '}';
    }
}
