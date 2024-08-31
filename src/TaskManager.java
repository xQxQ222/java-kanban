import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private final HashMap<Integer,Task> tasks=new HashMap<Integer, Task>();
    private int taskId=1;
    public void addAnyTask(TaskType type){
        switch (type) {
            case TASK:

        }
        taskId++;
    }

    private void addTask(Task task){
        tasks.put(task.getUniqueTaskId(),task);
    }
}
