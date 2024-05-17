package site.adithk.authenticationservice.enums;

public enum AppErrorCode  {
    ERROR_USER_NOT_FOUND("USER-MANAGEMENT-1000");


    private final String name;

    AppErrorCode(String s) {
        name = s;
    }


    public String toString() {
        return this.name;
    }
}
