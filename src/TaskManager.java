import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public final class TaskManager {
    private final HashMap<Integer,Object> tasks=new HashMap<Integer, Object>();
    private static int taskId=0;

    public static int generateTaskId(){
        taskId++;
        return taskId;
    }

    public void deleteAllTasks(){
        tasks.clear();
    }

    public Object getTaskById(int taskId){
        return tasks.get(taskId);
    }

    public void deleteTaskById(int taskId){
        if(tasks.containsKey(taskId))
            tasks.remove(taskId);
    }

    public void updateTask(){

    }

    public void addAnyTask(TaskType type,Object anyTask){
        switch (type) {
            case TASK:
                if (anyTask.getClass() == Task.class)
                    addTask((Task) anyTask);
                break;
            case SUBTASK:
                if (anyTask.getClass() == Subtask.class)
                    addSubtask((Subtask) anyTask);
                break;
            case EPIC:
                if (anyTask.getClass() == Epic.class)
                    addEpic((Epic) anyTask);
                break;
        }
    }


    private void addTask(Task task){
        tasks.put(task.getUniqueTaskId(),task);
    }

    private void addSubtask(Subtask subtask){
        Object epicObj=tasks.get(subtask.getEpicId());
        Epic epic=null;
        if(epicObj.getClass()==Epic.class)
            epic=(Epic)epicObj;
        if(epic==null)
            return;
        epic.addSubtask(subtask);
        epic.calculateEpicStatus();
    }

    private void addEpic(Epic epic){
        tasks.put(epic.getUniqueTaskId(),epic);
        epic.calculateEpicStatus();
    }

    public Object getAllTasks(){
        return tasks.values();
    }
}
