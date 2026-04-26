package vn.com.routex.hub.booking.service.infrastructure.persistence.constant;

public class ErrorConstant {


    public static final String SYSTEM_ERROR = "6800";
    public static final String SYSTEM_ERROR_MESSAGE = "System Error";
    public static final String TIMEOUT_ERROR = "0600";
    public static final String AUTHORIZATION_ERROR = "3200";
    public static final String AUTHORITIES_ERROR = "You are not authorized for this action";
    public static final String TIMEOUT_ERROR_MESSAGE = "Timeout";
    public static final String RECORD_NOT_FOUND = "1407";
    public static final String FLARE_CONFIG_NOT_FOUND = "Flare Config not found";
    public static final String SEAT_TEMPLATE_NOT_FOUND = "Seat template not found with template Id: %s";
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String AUTHORITIES_NOT_FOUND = "Authorities not found";
    public static final String ROUTE_NOT_FOUND = "Route with Id %s not found";
    public static final String TICKET_NOT_FOUND = "Ticket with Id %s not found";
    public static final String SEAT_NOT_FOUND = "Seats not found";
    public static final String VEHICLE_NOT_FOUND = "Vehicle not found";
    public static final String VEHICLE_TEMPLATE_NOT_FOUND = "Vehicle template not found";
    public static final String ROUTE_SEAT_NOT_FOUND = "Rout Seat with Route Id %s is not exists";
    public static final String RECORD_NOT_FOUND_MESSAGE = "Record not found";
    public static final String SUCCESS_CODE = "0000";
    public static final String SUCCESS_MESSAGE = "Success";
    public static final String DRIVER_NOT_FOUND_MESSAGE = "Driver Profile not found";
    public static final String USER_NOT_FOUND_MESSAGE = "User Profile not found";

    public static final String INVALID_HTTP_REQUEST_RESOURCE_ERROR = "4000";

    public static final String INVALID_HTTP_REQUEST_RESOURCE_ERROR_MESSAGE = "Resource %s path is not exists";

    public static final String INVALID_DATA_ERROR = "0410";
    public static final String INVALID_DATA_ERROR_MESSAGE = "Invalid data error";
    public static final String INVALID_EVENT_MESSAGE = "Invalid %s event";
    public static final String INVALID_INPUT_ERROR = "0310";
    public static final String SEAT_NOT_AVAILABLE = "Seat %s is not available";
    public static final String INVALID_INPUT_MESSAGE = "Invalid Input";
    public static final String INVALID_REQUEST_TIMESTAMP = "5186";
    public static final String DUPLICATE_ERROR = "9400";
    public static final String DUPLICATE_USER_ROLE_MESSAGE = "User already has this role";
    public static final String ROLE_EXISTS_ERROR = "Role with %s already exists";
    public static final String PERMISSION_EXISTS_ERROR = "Authorities with %s already exists";
    public static final String INVALID_PAGE_SIZE = "pageSize must be in [1..100]";
    public static final String INVALID_PAGE_NUMBER = "pageNumber must be >= 1";
    public static final String INVALID_SEAT_NO = "seatNos must not be empty";
    public static final String DUPLICATE_VEHICLE = "Vehicle is already exists by %s";
    public static final String ROUTE_SEAT_EXIST = "Route Seat with routeId %s already created";
    public static final String DUPLICATE_ROUTE_ASSIGNMENT = "Route Assignment with routeId %s already exists";
    public static final String VEHICLE_NOT_ASSIGNED_TO_ROUTE = "Vehicle is not assigned to route %s";
    public static final String RECORD_EXISTS = "Record is already existed";
    public static final String INVALID_START_TIME = "Planned Start Time must be before Planned End Time";
    public static final String INVALID_STOP_ORDER = "stopOrder must be positive & unique";
    public static final String INVALID_PLANNED_TIME = "Invalid planned arrival or departure time";
    public static final String INVALID_SEARCH_TIME = "From Time must be before To Time";
}
