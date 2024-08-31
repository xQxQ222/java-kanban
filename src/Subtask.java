public class Subtask extends Task{
    private final int epicId;
    public Subtask(String name, int uniqueTaskId, String description, TaskStatus taskStatus,int epicId) {
        super(name, uniqueTaskId, description, taskStatus);
        this.epicId=epicId;
    }
}
