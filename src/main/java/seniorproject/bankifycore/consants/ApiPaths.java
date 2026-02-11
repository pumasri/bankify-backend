package seniorproject.bankifycore.consants;

public final class ApiPaths {
    //to prevent instantiation
    private ApiPaths() {}

    // global version
    public static final String API_V1 = "/api/v1";


    // modules
    public static final String ADMIN = API_V1 + "/admin";
    public static final String ACCOUNTS = API_V1 + "/admin/accounts";
    public static final String CUSTOMERS = API_V1 + "/admin/customers";
    public static final String TRANSACTIONS = API_V1 + "/admin/transactions";

    public static final String ATM = API_V1 + "/atm";
    public static final String PARTNER = API_V1 + "/partner";
    public static final String DEBUG = API_V1 + "/debug";
    public static final String HEALTH = "/health";

}
