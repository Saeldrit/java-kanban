package service;

import model.Task;
import service.data_structure.StructureImplementation;
import service.data_structure.structure_interface.StructureManager;
import service.manager_interface.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private StructureManager taskIdSet;
    private final InMemoryTaskManager manager;
    private List<Task> tasks;
    private int limitSize;

    public InMemoryHistoryManager(InMemoryTaskManager manager) {
        limitSize = 10;
        this.taskIdSet = new StructureImplementation(limitSize);
        this.manager = manager;
    }

    private void initialization() {
        tasks = new ArrayList<>();
        tasks.addAll(manager.getEpics());
        tasks.addAll(manager.getTasks());
        tasks.addAll(manager.getSubtasks());
    }

    public int getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(int limitSize) {
        taskIdSet = new StructureImplementation(limitSize);
        this.limitSize = limitSize;
    }

    @Override
    public void add(int taskId) {
        taskIdSet.push(taskId);
    }

    @Override
    public List<Task> getHistory() {
        List<Integer> listId = taskIdSet.peek(limitSize);
        initialization();

        return lookForTask(tasks, listId);
    }

    private List<Task> lookForTask(List<? extends Task> tasks, List<Integer> listId) {
        List<Task> newTasks = new ArrayList<>();

        for (var id : listId) {
            for (var task : tasks) {
                if (task.getId().equals(id)) {
                    newTasks.add(task);
                    break;
                }
            }
        }

        return newTasks;
    }
}
