import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private final int uniqueTaskId;
    private TaskStatus taskStatus;


    public Task(String name, int uniqueTaskId, String description,TaskStatus taskStatus) {
        this.name = name;
        this.uniqueTaskId = uniqueTaskId;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getUniqueTaskId() {
        return uniqueTaskId;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return uniqueTaskId == task.uniqueTaskId;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(uniqueTaskId);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uniqueTaskId=" + uniqueTaskId +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
