package vn.com.routex.hub.booking.service.infrastructure.persistence.utils;

import lombok.experimental.UtilityClass;
import vn.com.routex.hub.booking.service.infrastructure.persistence.exception.BusinessException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static vn.com.routex.hub.booking.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;


@UtilityClass
public class DateTimeUtils {

    public static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public int parseIntOrThrow(String v, String field, String requestId, String requestDateTime, String channel) {
        if (v == null || v.isBlank()) {
            throw new BusinessException(requestId, requestDateTime, channel,
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, field + " is required"));
        }
        if (!v.trim().matches("^\\d{1,3}$")) {
            throw new BusinessException(requestId, requestDateTime, channel,
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, field + " must be numeric"));
        }
        return Integer.parseInt(v.trim());
    }

    public LocalDate parseDateOrThrow(String v, String field, String requestId, String requestDateTime, String channel) {
        try {
            return LocalDate.parse(v.trim()); // yyyy-MM-dd
        } catch (Exception e) {
            throw new BusinessException(requestId, requestDateTime, channel,
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, field + " must be yyyy-MM-dd"));
        }
    }

    public LocalTime parseTimeNullable(String v, String field, String requestId, String requestDateTime, String channel) {
        if (v == null || v.isBlank()) return null;
        try {
            return LocalTime.parse(v.trim(), TIME_FMT); // HH:mm
        } catch (Exception e) {
            throw new BusinessException(requestId, requestDateTime, channel,
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, field + " must be HH:mm"));
        }
    }

    public int safeStopOrder(String v) {
        try { return Integer.parseInt(v); } catch (Exception e) { return Integer.MAX_VALUE; }
    }
}
