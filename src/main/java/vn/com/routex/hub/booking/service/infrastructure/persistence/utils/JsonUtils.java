package vn.com.routex.hub.booking.service.infrastructure.persistence.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import vn.com.routex.hub.booking.service.infrastructure.persistence.config.ApplicationConfig;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.TIMEOUT_ERROR;
import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.TIMEOUT_ERROR_MESSAGE;

@UtilityClass

public class JsonUtils {

    private final ObjectMapper objectMapper = ApplicationConfig.getObjectMapper();


    public String parseToJsonStr(Object message) throws JsonProcessingException {
        try {
            return objectMapper.writeValueAsString(message);

        } catch (Exception e) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(TIMEOUT_ERROR, TIMEOUT_ERROR_MESSAGE));
        }
    }

    public Object parseToObject(String message, Class<Object> clazz) throws JsonProcessingException {
        try {
            return objectMapper.readValue(message, clazz);
        } catch (Exception e) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(TIMEOUT_ERROR, TIMEOUT_ERROR_MESSAGE));
        }
    }

    public <T> T parseToKafkaObject(String message, TypeReference<T> type) {
        try {
            return objectMapper.readValue(message, type);
        } catch (Exception e) {
            throw new BusinessException(ExceptionUtils.buildResultResponse(TIMEOUT_ERROR, TIMEOUT_ERROR_MESSAGE));
        }
    }
}
