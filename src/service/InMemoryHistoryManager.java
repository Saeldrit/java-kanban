package service;

import model.Task;
import service.manager_interface.HistoryManager;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Set<Task> tasks;
    private int limitSize;

    public InMemoryHistoryManager() {
        tasks = new LinkedHashSet<>();
        limitSize = 10;
    }

    public int getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }

    /**
     *
     * Сначала делаю удаление, чтобы старый просмотр удалился, а новый добавился на свое место.
     * Иначе выйдет так, что при вводе 1 - 2 - 3 - 1 - 3 история будет хранить = 1 - 2 - 3,
     * когда правильный результат 2 - 1 - 3. Я изначально хотел LinkedHashSet использовать,
     * но сначала не додумался об простом удалении и после добавлении, чтобы выстроить правильный порядок
     * добавления, поэтому решил сам написать смесь стека и множества. Херня вышла, а может не совсем,
     * но в итоге переделал вот таким решением.
     */
    @Override
    public void add(Task task) {
        tasks.remove(task);
        tasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        if (tasks.size() < limitSize && tasks.size() > 0) {
            return new ArrayList<>(tasks);
        } else if (tasks.size() > limitSize) {
            return getTasksByLimitSize();
        }
        return new ArrayList<>();
    }

    private List<Task> getTasksByLimitSize() {
        int startIndex = tasks.size() - limitSize;

        return new ArrayList<>(tasks).subList(startIndex, tasks.size());
    }
}
