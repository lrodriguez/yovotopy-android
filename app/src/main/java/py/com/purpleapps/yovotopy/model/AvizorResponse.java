package py.com.purpleapps.yovotopy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by luisrodriguez on 4/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvizorResponse {
    private AvizorEntity avizorEntity;
    private AvizorError avizorError;

    public AvizorResponse() {
    }

    public AvizorResponse(AvizorEntity avizorEntity, AvizorError avizorError) {
        this.avizorEntity = avizorEntity;
        this.avizorError = avizorError;
    }

    public AvizorEntity getAvizorEntity() {
        return avizorEntity;
    }

    @JsonProperty("payload")
    public void setAvizorEntity(AvizorEntity avizorEntity) {
        this.avizorEntity = avizorEntity;
    }

    public AvizorError getAvizorError() {
        return avizorError;
    }

    @JsonProperty("error")
    public void setAvizorError(AvizorError avizorError) {
        this.avizorError = avizorError;
    }

    @Override
    public String toString() {
        return "AvizorResponse{" +
                "avizorEntity=" + avizorEntity +
                ", avizorError=" + avizorError +
                '}';
    }
}
