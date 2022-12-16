package ee.cyber.cdoc20.server;

/**
 * Test scenario identifiers
 */
public final class ScenarioIdentifiers {
    private ScenarioIdentifiers() {
    }

    public static final String POS_PUT_01 = "ECC-PUT_CAPSULE-POS-01-ONCE";
    public static final String POS_GET_02 = "ECC-GET_CAPSULE-POS-01-CORRECT_REQUEST";

    public static final String NEG_GET_02 = "GET_CAPSULE-NEG-02-RANDOM_UUID_TRANSACTION_ID";
    public static final String NEG_GET_03 = "GET_CAPSULE-NEG-03-TOO_SHORT_TRANSACTION_ID";
    public static final String NEG_GET_04 = "GET_CAPSULE-NEG-04-EMPTY_STRING_TRANSACTION_ID";
    public static final String NEG_GET_05 = "GET_CAPSULE-NEG-05-TOO_LONG_RANDOM_STRING_TRANSACTION_ID";
    public static final String NEG_GET_06 = "ECC-GET_CAPSULE-NEG-06-PUBLIC_KEY_NOT_MATCHING";
}
