package me.carly.calculator;

import java.util.*;

public class Main {

    private static final Map<String, String> roms = new HashMap<>();

    public static String calc(String input) throws Exception {

        String[] args = getArgs(input);

        checkLength(args.length);

        String firstNumber = String.valueOf(getNumb(args[0]));
        char operator = args[1].charAt(0);
        String secondNumber = String.valueOf(getNumb(args[2]));

        Integer calculate = calculate(Integer.parseInt(firstNumber), operator, Integer.parseInt(secondNumber));

        if (!roms.get(firstNumber).equalsIgnoreCase(roms.get(secondNumber))) {
            throw new Exception("т.к. используются одновременно разные системы счисления");
        } else {
            if (roms.get(firstNumber).equalsIgnoreCase("roman") && calculate < 0) {
                throw new Exception("т.к. в римской системе нет отрицательных чисел");
            } else {
                if (roms.get(firstNumber).equalsIgnoreCase("roman")) {
                    return arabicNumberToRomanNumber(calculate);
                } else {
                    return String.valueOf(calculate);
                }
            }
        }
    }

    private static Integer getNumb(String number) {
        try {
            number = String.valueOf(romanNumberToArabicNumber(number));
            roms.put(number, "roman");
            return Integer.parseInt(number);
        } catch (Exception e) {
            number = String.valueOf(number);
            roms.put(number, "noroman");
            return Integer.parseInt(number);
        }
    }

    private static String[] getArgs(String text) {
        return text.split(" ");
    }

    private static Integer calculate(int firstNumber, char operator, int secondNumber) {

        int result = 0;

        switch (operator) {
            case '*' -> result = Math.multiplyExact(firstNumber, secondNumber);
            case '/' -> result = Math.floorDiv(firstNumber, secondNumber);
            case '+' -> result = firstNumber + secondNumber;
            case '-' -> result = firstNumber - secondNumber;
            default -> System.exit(1);
        }
        return result;
    }

    private static void checkLength(int length) throws Exception {
        if (length == 1)
            throw new Exception("т.к. строка не является математической операцией");
        else if (length > 3)
            throw new Exception("т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
    }

    private static int romanNumberToArabicNumber(String number) {
        int result = 0;
        int i = 0;

        List<Roman> romanList = getReverseSortedValues();

        while (!number.isEmpty() && i < romanList.size()) {
            Roman roman = romanList.get(i);

            if (number.startsWith(roman.name())) {
                result += roman.getValue();
                number = number.substring(roman.name().length());
            } else {
                i++;
            }
        }

        if (number.length() > 0) {
            throw new IllegalArgumentException(number + " не может быть преобразован в римскую цифру");
        } else {
            return result;
        }
    }

    private static String arabicNumberToRomanNumber(int number) {
        List<Roman> reverseSortedValues = getReverseSortedValues();

        int i = 0;

        StringBuilder stringBuilder = new StringBuilder();

        while (number > 0 && i < reverseSortedValues.size()) {
            Roman roman = reverseSortedValues.get(i);

            if (roman.getValue() <= number) {
                stringBuilder.append(roman.name());
                number -= roman.getValue();
            } else {
                i++;
            }
        }
        return stringBuilder.toString();
    }

    private static List<Roman> getReverseSortedValues() {
        return Arrays.stream(Roman.values()).sorted(Comparator.comparing(Roman::getValue).reversed()).toList();
    }
}

enum Roman {
    I(1),
    IV(4),
    V(5),
    IX(9),
    X(10),
    XL(40),
    L(50),
    XC(90),
    C(100),
    CD(400),
    D(500),
    CM(900),
    M(1000);

    private final int value;

    Roman(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
