package hrd.com.hrms.common;

public class Constants {

    // Private constructor to prevent instantiation
    private Constants() {}

    // --- API Response Messages ---
    public static final String MSG_FETCH_SUCCESS   = "Data retrieved successfully.";
    public static final String MSG_CREATE_SUCCESS  = "Resource created successfully.";
    public static final String MSG_UPDATE_SUCCESS  = "Resource updated successfully.";
    public static final String MSG_DELETE_SUCCESS  = "Resource deleted successfully.";

    // --- Error & Exception Messages ---
    public static final String ERR_NOT_FOUND       = "Requested resource could not be found.";
    public static final String ERR_BAD_REQUEST     = "Invalid request data provided.";
    public static final String ERR_UNAUTHORIZED    = "Authentication is required to access this resource.";
    public static final String ERR_FORBIDDEN       = "You do not have permission to perform this action.";
    public static final String ERR_INTERNAL_SERVER = "An unexpected error occurred on the server.";

    // --- Pagination Defaults ---
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE   = "10";
    public static final String DEFAULT_SORT_BY     = "id";
    public static final String DEFAULT_SORT_DIR    = "asc";
}