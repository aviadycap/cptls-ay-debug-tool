package com.capitolis.websocket.ay.debug.tool.utils;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.List;

public class CustomBeanSerializerModifier extends BeanSerializerModifier {


    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        for (BeanPropertyWriter writer : beanProperties) {
            // Implement logic to filter out properties based on some condition
            if ("paymentPeriods".equals(writer.getName())) {
                writer.assignSerializer(null); // this won't include this property when null
                writer.removeInternalSetting("created");
            }
        }

        return beanProperties;
    }
}