package model;

public enum ArithmeticType {
    ADD("dodawanie"), SUB("odejmowanie"), MULTIPLI("mnożenie"), DIV("dzielenie");

    private final String name;

    ArithmeticType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ArithmeticType{" +
                "name='" + name + '\'' +
                '}';
    }
}
