import java.util.ArrayList;

public class Epic extends Task{
    private final ArrayList<Subtask> subtasks=new ArrayList<>();

    public Epic(String name, int uniqueTaskId, String description, TaskStatus taskStatus) {
        super(name, uniqueTaskId, description, taskStatus);
    }


    public void addSubtask(Subtask subtask){
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }
}
