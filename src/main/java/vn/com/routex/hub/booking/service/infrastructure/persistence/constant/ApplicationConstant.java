package vn.com.routex.hub.booking.service.infrastructure.persistence.constant;

import java.math.BigDecimal;
import java.time.ZoneId;

public class  ApplicationConstant {

    public static final String REQUEST_ID_REGREX = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String DATETIME_REGREX = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}[-+]\\d{2}:\\d{2}";
    public static final String CHANNEL_REGREX = "^(ONL|OFF)$";
    public static final String ONLY_NUMBER_REGEX = "^\\d+$";
    public static final String ONLY_CHARACTER_REGEX = "^[\\p{L} ]+$";
    public static final String ONLY_NUMBER_AND_CHARACTER_DIGITS = "^[A-Za-z0-9]+$";
    public static final String UUID_REGEX = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String DATE_MONTH_YEAR_REGEX = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
    public static final String OFFSET_DATE_TIME_REGEX = "^(?:\\d{4})-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\\d|3[01])T(?:[01]\\d|2[0-3]):(?:[0-5]\\d):(?:[0-5]\\d)(?:\\.\\d{1,9})?(?:Z|[+\\-](?:0\\d|1\\d|2[0-3]):[0-5]\\d)$";
    public static final String VEHICLE_PLATE_REGEX = "^(0[1-9]|[1-9]\\d)(?:[A-Z]|LD|NG)-\\d{3}\\.\\d{2}$";
    public static final String HOUR_MINUTES_REGEX = "^([01]\\d|2[0-3]):([0-5]\\d)$";
    public static final String YEAR_MONTH_DATE_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 1;
    public static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    public static final BigDecimal DEFAULT_MERCHANT_COMMISSION_RATE = new BigDecimal("15.00");

}
