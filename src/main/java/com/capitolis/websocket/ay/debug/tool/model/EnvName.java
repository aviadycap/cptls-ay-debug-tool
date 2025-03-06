package com.capitolis.websocket.ay.debug.tool.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(allowableValues = {"Hearts", "Diamonds", "Clubs", "Spades"})
public enum EnvName {
     DEV    ("localhost")
    ,EDGE2  ("edge2")
    ,QA_EUW1("qa-euw1")
    ,QA3    ("qa3")
    ,EQ_UAT("eq-uat")
    ;

    private final String name;

    public static String[] getEnvDtoListAsString() {
        EnvName[] values = EnvName.values();
        String[] envDtoList = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            envDtoList[i] = values[i].name();
        }

       return envDtoList;
    }

    EnvName(String value) {
        this.name = value;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static EnvName fromName(String value) {
        for(EnvName b : values()) {
            if (b.name.equals(value)) {
                return b;
            }
        }

        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public EnvName getEnv() {
        return this;
    }

}
