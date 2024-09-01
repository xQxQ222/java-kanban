import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task{
    private ArrayList<Subtask> subtasks=new ArrayList<>();

    public Epic(String name,String description) {
        super(name, description, TaskStatus.NEW);
    }

    public Epic(String name,String description, ArrayList<Subtask> newSubtasks){
        super(name,description,TaskStatus.NEW);
        this.setTaskStatus(calculateEpicStatus());
        subtasks=newSubtasks;
    }


    public void addSubtask(Subtask subtask){
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public TaskStatus calculateEpicStatus(){
        if(subtasks.isEmpty()){
            return TaskStatus.NEW;
        }
        var statuses=getSubtasksStatuses();
        if(!statuses.contains(TaskStatus.DONE) && !statuses.contains(TaskStatus.IN_PROGRESS))
            return TaskStatus.NEW;
        else if(statuses.contains(TaskStatus.IN_PROGRESS) || statuses.contains(TaskStatus.NEW))
            return TaskStatus.IN_PROGRESS;
        else
            return TaskStatus.DONE;
    }

    protected ArrayList<TaskStatus> getSubtasksStatuses(){
        ArrayList<TaskStatus> statuses=new ArrayList<>();
        for(Subtask subtask : subtasks)
            statuses.add(subtask.getTaskStatus());
        return statuses;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uniqueTaskId=" + uniqueTaskId +
                ", taskStatus=" + taskStatus +
                ", subtasks=" + subtasks+
                "}";
    }
}
