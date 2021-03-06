package nl.sikken.bertrik.hab.ttn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of meta-data part of MQTT message.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TtnMessageMetaData {

    @JsonProperty("time")
    private String time;
    
    @JsonProperty("gateways")
    private List<TtnMessageGateway> gateways;
    
    public String getTime() {
        return time;
    }
    
    public List<TtnMessageGateway> getMqttGateways() {
        return gateways;
    }

}
