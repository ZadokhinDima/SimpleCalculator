import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Calculator {

    private enum Operation {
        ADDITION("+", (x, y) -> x + y), SUBTRACTION("-", (x, y) -> x - y),
        MULTIPLICATION("*", (x, y) -> x * y), DIVISION("/", (x, y) -> x / y);

        private String symbol;
        private BinaryOperator<Integer> operator;

        private static Map<String, Operation> map = new HashMap<>();

        static {
            Stream.of(Operation.values()).forEach(operation -> map.put(operation.symbol, operation));
        }

        Operation(final String symbol, final BinaryOperator<Integer> operator) {
            this.symbol = symbol;
            this.operator = operator;
        }

        public int apply(int x, int y) {
            return operator.apply(x, y);
        }

        public static Operation of(String symbol) {
            return map.get(symbol);
        }

    }


    public static int calculate(String expression) throws IOException {
        List<Operation> operations = parseOperations(expression);
        List<Integer> operands = parseOperands(expression);

        return calculate(operations, operands);
    }

    private static int calculate(List<Operation> operations, List<Integer> operands) {
        int index = 1;
        while (index < operands.size()) {
            if (EnumSet.of(Operation.MULTIPLICATION, Operation.DIVISION).contains(operations.get(index))) {
                performOperation(index, operations, operands);
            } else {
                index++;
            }
        }
        index = 1;
        while (index < operands.size()) {
            performOperation(index, operations, operands);
        }
        return operands.get(0);
    }

    private static List<Operation> parseOperations(String input) {
        return Stream.of(input.split("[0-9]+")).map(Operation::of).collect(Collectors.toList());
    }

    private static List<Integer> parseOperands(String input) {
        return Stream.of(input.split("[" + Pattern.quote("+-*/") + "]"))
                .map(Integer::valueOf).collect(Collectors.toList());
    }

    private static void performOperation(int index, List<Operation> operations, List<Integer> operands) {
        final int operationResult = operations.get(index).apply(operands.get(index - 1), operands.get(index));
        operands.set(index - 1, operationResult);
        operands.remove(index);
        operations.remove(index);
    }
}
