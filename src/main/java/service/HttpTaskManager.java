package service;

import com.google.gson.Gson;
import factory.Managers;
import http.client.KVTaskClient;
import model.Epic;
import model.Subtask;
import model.Task;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private final Gson gson;

    /**
     * Как избавиться от конструктора super?
     * Пустой конструктор не получится, так как FileManager требует создания File с аргументом
     * пути к файлу. Не лучше наследоваться от InMemory? Или вообще от abstract TaskManager,
     * на крайний случай вынести туда метод save и тут переопределить, но тогда
     * я получу чистую площадку для деятельности, а не займусь сексом с файлами))
     */
    public HttpTaskManager(String urlToServer) {
        super("src/main/resources/tasks.csv"); //залипуха...
        this.kvTaskClient = new KVTaskClient(urlToServer);
        gson = Managers.getGson();
    }

    /**
     *
     * Если убрать вызов super.addNewTask(); то мне проще наследоваться от ManagerApp
     * и реализовать отдельную логику генерации id.
     * И, правильно добавлять 1 ключ - 1 значение? Получается, что один клиент API_TOKEN будет
     * хранить много ключей и у каждого по значению, или мне нужно на один ключ запихнуть вееееесь
     * список задач?
     */
    @Override
    public int addNewTask(Task task) {
        String json = gson.toJson(task);
        int id = super.addNewTask(task);
        kvTaskClient.put(String.valueOf(id), json);
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        String json = gson.toJson(epic);
        int id = super.addNewEpic(epic);
        kvTaskClient.put(String.valueOf(id), json);
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        String json = gson.toJson(subtask);
        int id = super.addNewSubtask(subtask);
        kvTaskClient.put(String.valueOf(id), json);
        return id;
    }

    @Override
    public Task getTaskById(int id) {
        String json = kvTaskClient.load(String.valueOf(id));
        return gson.fromJson(json, Task.class);
    }

    @Override
    public Epic getEpicById(int id) {
        String json = kvTaskClient.load(String.valueOf(id));
        return gson.fromJson(json, Epic.class);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        String json = kvTaskClient.load(String.valueOf(id));
        return gson.fromJson(json, Subtask.class);
    }

    @Override
    public int updateTask(Task task) {
        String json = gson.toJson(task);
        int id = super.updateTask(task);
        kvTaskClient.put(String.valueOf(id), json);
        return id;
    }

    @Override
    public int updateEpic(Epic epic) {
        String json = gson.toJson(epic);
        int id = super.updateEpic(epic);
        kvTaskClient.put(String.valueOf(id), json);
        return id;
    }

    @Override
    public int updateSubtask(Subtask subtask) {
        String json = gson.toJson(subtask);
        int id = super.updateSubtask(subtask);
        kvTaskClient.put(String.valueOf(id), json);
        return id;
    }

    /**
     * Что вообще делать с удалением? Метод save должен постоянно дергать все данные и перезаписывать?
     * То есть я буду хранить список задач в менеджере, в списке, удалять их из этого списка, например, или мапы,
     * а метод save просто парсить после каждого действия мапу на сервер?
     * А при наследовании FileBackedTaskManager они будут храниться в файле...какой-то фарш))
     * И я так понимаю, что я не могу дописать метод удаления на сервер, там должны быть только три метода:
     * register, load, save.
     */
    @Override
    public void removeTasks() {
        super.removeTasks();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
    }
}
