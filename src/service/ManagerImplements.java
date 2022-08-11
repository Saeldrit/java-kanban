package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import service.manager_interface.ManagerEpic;
import service.manager_interface.ManagerSubtask;
import service.manager_interface.ManagerTask;

import java.util.*;

public class ManagerImplements implements ManagerTask, ManagerEpic, ManagerSubtask {

    private final Map<Integer, Task> taskMap;
    private final Map<Integer, Epic> epicMap;
    private final Map<Integer, Subtask> subtaskMap;

    private int identifier;

    public ManagerImplements() {
        this.taskMap = new LinkedHashMap<>();
        this.epicMap = new LinkedHashMap<>();
        this.subtaskMap = new LinkedHashMap<>();
    }

    @Override
    public void addNewTask(Task task) {
        task.setId(++identifier);
        taskMap.put(task.getId(), task);
    }

    @Override
    public void addNewEpic(Epic epic) {
        epic.setId(++identifier);
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        int subtaskId = ++identifier;
        Epic epic = epicMap.get(epicId);

        if (epic == null) {
            throw new IllegalArgumentException("Add Epic for subtask");
        }

        subtask.setId(subtaskId);
        subtaskMap.put(subtaskId, subtask);

        epic.addSubtask(subtask);
        updateEpicStatus(epic);
    }

    @Override
    public void updateTask(Task task) {
        if (task != null) {
            if (taskMap.containsKey(task.getId())) {
                taskMap.put(task.getId(), task);
            }
        } else {
            throw new NullPointerException("Your Task is Null");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null) {
            updateEpicStatus(epic);
            if (epicMap.containsKey(epic.getId())) {
                epicMap.put(epic.getId(), epic);
            }
        } else {
            throw new IllegalArgumentException("Your Epic is Null");
        }
    }

    /**
     * Я не стал дополнять логику работы метода с изменением id epic'a в subtask.
     * Объясняю: в таком приложении необходимо будет свайпнуть подзадачу в нужный epic,
     * а это выглядит нелогично и сложно для пользователя (сужу как пользователь),
     * если только это не веб приложение в браузере. Мол, у нас выходит конструктор задач, а не ежедневник.
     * Я не видел такого в других приложениях. Гораздо проще, если
     * ты создал подзадачу не в нужном эпике, удалил её и создал новую в нужно эпике.
     * Логика работы простая и наверняка, а это все выглядит сложно, в особенности с телефона,
     * с точки зрения юзера.
     * Не знаю, мне так кажется, а там конечно сделаю, если это так необходимо, пока вроде по заданию
     * такого не требуется, если не ошибаюсь...я ведь так внимательно читаю тз)))
     */
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null) {
            Epic epic = epicMap.get(subtask.getEpicId());

            updateEpic(epic);
            subtaskMap.put(subtask.getId(), subtask);
        } else {
            throw new IllegalArgumentException("Your Subtask is Null");
        }
    }

    @Override
    public List<Task> getTasks() {
        Collection<Task> tasks = taskMap.values();
        return new ArrayList<>(tasks);
    }

    @Override
    public List<Subtask> getSubtasks() {
        Collection<Subtask> subtasks = subtaskMap.values();
        return new ArrayList<>(subtasks);
    }

    @Override
    public List<Epic> getEpics() {
        Collection<Epic> epics = epicMap.values();
        return new ArrayList<>(epics);
    }

    @Override
    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return epicMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return subtaskMap.get(id);
    }

    @Override
    public void removeTasks() {
        taskMap.clear();
    }

    @Override
    public void removeEpics() {
        epicMap.clear();
        subtaskMap.clear();
    }

    @Override
    public void removeSubtasks() {
        subtaskMap.clear();
        for (var key : epicMap.keySet()) {
            epicMap.get(key).setSubtaskList(null);
        }
    }

    @Override
    public void removeTaskById(int id) {
        taskMap.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            List<Subtask> subtasks = epic.getSubtask();
            subtasks.forEach(sub -> subtaskMap.remove(sub.getId()));
            epicMap.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtaskMap.containsKey(id)) {
            Subtask subtask = subtaskMap.get(id);
            Epic epic = epicMap.get(subtask.getEpicId());
            List<Subtask> subtasks = epic.getSubtask();

            subtasks.remove(subtask);

            if (subtasks.size() == 0) {
                subtaskMap.remove(id);
                epicMap.get(epic.getId()).setSubtaskList(null);
            } else {
                epic.setSubtaskList(subtasks);
                updateEpic(epic);
                subtaskMap.remove(id);
            }
        }
    }

    /**
     * Я решил оставить список подзадач, чтобы вернуть список из объекта Epic
     * путём вызова одного метода. Не знаю, конечно, насколько это верно,
     * но разве так не проще, когда нам порой не нужно делать лишние итерации в поисках
     * и копировании списков, поиска объектов, а сразу получить готовый и если нужно, то пройтись по нему.
     * Смотрел вебинар наставника, он тоже делал с id, я решил остановиться на списках
     * объектов, на том, что в моём понимании легче. Я думаю на данном этапе имею возможность
     * ошибаться и использовать разные варианты, чтобы прийти в понимании к этому.
     *
     */
    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        return epic.getSubtask();
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtasks = epic.getSubtask();
        boolean isCheck = false;
        int counter = 0;

        for (var subtask : subtasks) {
            if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (subtask.getStatus().equals(Status.DONE)) {
                ++counter;
            } else {
                isCheck = subtask.getStatus().equals(Status.NEW);
            }
        }

        if (counter == subtasks.size()) {
            epic.setStatus(Status.DONE);
        } else if (isCheck && counter < subtasks.size() && counter > 0) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }
}
