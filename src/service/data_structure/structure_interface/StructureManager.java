package service.data_structure.structure_interface;

import java.util.List;

public interface StructureManager {
    void push(int j);

    int pop();

    int peek();

    List<Integer> peek(int totalQuantity);

    boolean isEmpty();

    int size();
}
