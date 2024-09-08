package com.assignment.paymentmanagementservice.constants;

public final class Constants {
    public static final String NO_CUSTOMER_FOUND = "NO customer found with given id";
    public static final String NAME_VALIDATION = "Name must contain only alphabets and spaces";
    public static final String PHONE_NUMBER_VALIDATION = "Enter 10 digit phone number";
    public static final String EMAIL_VALIDATION = "Invalid email address";
    public static final String AMOUNT_VALIDATION = "Amount Cannot be negative";
    public static final String BAD_REQUEST_CODE = "400";
    public static final String GENERAL = "Something went wrong. ";
    public static final String NO_TRANSACTIONS_FOUND = "No transactions with given orderId";
    public static final String ORDER_ALREADY_CONFIRMED = "Order is already confirmed";
    public static final String NO_PAYMENT_WITH_TRANSACTIONID = "No PAYMENTS found with given TransactionId";
    public static final String FORBIDDEN_CODE = "403";
    public static final String PAYMENT_STATUS_FORBIDDEN = "Cannot override Payment status";
    public static final String CANNOT_PASS_STATUS = "Cannot Pass status";
    public static final String SELECT_PAYMENT_MODE = "Select payment mode";
    public static final String NO_ORDERS_WITH_PHONE_NUMBER = "No orders with given phone number";
    public static final String NO_ORDERS_WITH_EMAIL = "No orders with given email id";
    public static final String NO_ORDER_FOUND = "No order found with given orderId";
    public static final String AMOUNT_CANNOT_BE_CHANGED = "Amount can not be changed";
    public static final String ENTER_CUSTOMER_DETAILS = "Enter customer details";
    public static final String INTERNAL_SERVER_ERROR_CODE = "500";
    public static final String PHONE_NUMBER_NOT_EXISTS_ERROR = "No customer Exist with this phoneNumber. To create new customer enter full details.";
    public static final String FILE_PATH_NOT_CREATED = "File path not created";
    public static final String FILE_NOT_CREATED = "File not created";
    public static final String FILE_NOT_READ = "File not read";
    public static final String FILE_NOT_LOADED = "File not loaded";
    public static final String UPLOAD_FILE_ERROR = "Please upload file";
    public static final String PROVIDE_CSV_FILE_ERROR = "Please provide csv file";
    public static final String AMOUNT_IS_NULL = "Amount is not provided";
    public static final String NO_NAME_ERROR = "Please provide customer name";
    public static final String PHONE_NUMBER_BLANK = "Phone Number must not be blank";
    public static final String UPLOAD_STARTED = "Upload Started";
    public static final String PROCESSING = "Processing";
    public static final String TEMP_STORAGE = "/home/Abhinav.Santhosh/Desktop/uploadedFiles/";
    public static final String UPDATE_STARTED = "Update Started";
    public static final String UPLOAD_FINISHED = "Upload Finished";
    public static final String UPDATE_SUCCESSFUL = "Update Successful";
    public static final String FILE_NOT_FOUND = "File Not found";
    public static final String GET_TRANSACTIONS_FILE = "getTransactions.csv";
    public static final String PAYMENTS_FILE = "payments.csv";
    public static final String SUCCESS = "Success";
    public static final String STARTED_AT = "startedAT";
    public static final String STATUS_UPDATED = "Updated Status";
    public static final String TRUE = "True";


    private Constants() {
    }

}
