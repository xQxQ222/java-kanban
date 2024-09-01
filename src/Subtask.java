public class Subtask extends Task{
    private final int epicId;
    public Subtask(String name, String description, TaskStatus taskStatus,int epicId) {
        super(name, description, taskStatus);
        this.epicId=epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uniqueTaskId=" + uniqueTaskId +
                ", taskStatus=" + taskStatus +
                "}";
    }
}
