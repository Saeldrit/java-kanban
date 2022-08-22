package service.data_structure;

import service.data_structure.structure_interface.SetAndStackManager;

import java.util.ArrayList;
import java.util.List;

public class SetAndStackImplementation implements SetAndStackManager {
    private int[] stackArray;
    private int top;
    private int count;

    public SetAndStackImplementation(int limitSize) {
        stackArray = new int[limitSize];
        top = -1;
        count = 0;
    }

    @Override
    public void push(int value) {
        if (lookFor(value)) {
            movingToForward();
        }
        add(value);
    }

    @Override
    public int pop() {
        if (isEmpty()) {
            count--;
            return stackArray[top--];
        }
        return 0;
    }

    @Override
    public int peek() {
        if (isEmpty()) {
            return stackArray[top];
        } else {
            return top;
        }
    }

    @Override
    public List<Integer> peek(int totalQuantity) {
        List<Integer> list = new ArrayList<>();

        convertToList(list, Math.min(totalQuantity, stackArray.length));
        return list;
    }

    @Override
    public boolean isEmpty() {
        return (top != -1);
    }

    @Override
    public int size() {
        return count;
    }

    private void convertToList(List<Integer> list, int maxLength) {
        for (int i = maxLength - 1; i >= 0; i--) {
            if (stackArray[i] != 0) {
                list.add(stackArray[i]);
            }
        }
    }

    private boolean lookFor(int value) {
        boolean isDuplicate = false;

        if (count > 0) {
            for (int i = 0; i < stackArray.length; i++) {
                if (stackArray[i] == value) {
                    stackArray[i] = 0;
                    --count;
                    isDuplicate = true;
                    break;
                }
            }
        }

        return isDuplicate;
    }

    private void add(int value) {
        int length = stackArray.length;

        if (count == length) {
            System.arraycopy(stackArray, 1, stackArray, 0, count - 1);
            stackArray[length - 1] = value;
        } else {
            stackArray[++top] = value;
        }

        ++count;
    }

    private void movingToForward() {
        int[] copyArray = new int[stackArray.length];
        int j = 0;
        --top;

        for (var value : stackArray) {
            if (value != 0) {
                copyArray[j] = value;
                j++;
            }
        }

        stackArray = copyArray;
    }
}
