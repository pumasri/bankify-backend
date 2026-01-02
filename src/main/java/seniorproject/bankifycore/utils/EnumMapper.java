package seniorproject.bankifycore.utils;

public final class EnumMapper {
    private EnumMapper() {}

    public static <E extends Enum<E>> E toEnum(Class<E> enumClass, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Value is required");
        }
        try {
            return Enum.valueOf(enumClass, value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid value: " + value + " for " + enumClass.getSimpleName()
            );
        }
    }
}
