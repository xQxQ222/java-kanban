import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task{
    private final ArrayList<Subtask> subtasks=new ArrayList<>();

    public Epic(String name,String description) {
        super(name, description, TaskStatus.NEW);

    }


    public void addSubtask(Subtask subtask){
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void calculateEpicStatus(){
        if(subtasks.isEmpty()){
            this.setTaskStatus(TaskStatus.NEW);
            return;
        }
        var statuses=getSubtasksStatuses();
        if(!statuses.contains(TaskStatus.DONE) && !statuses.contains(TaskStatus.IN_PROGRESS))
            this.setTaskStatus(TaskStatus.NEW);
        else if(statuses.contains(TaskStatus.IN_PROGRESS) || statuses.contains(TaskStatus.NEW))
            this.setTaskStatus(TaskStatus.IN_PROGRESS);
        else
            this.setTaskStatus(TaskStatus.DONE);
    }

    protected ArrayList<TaskStatus> getSubtasksStatuses(){
        ArrayList<TaskStatus> statuses=new ArrayList<>();
        for(Subtask subtask : subtasks)
            statuses.add(subtask.getTaskStatus());
        return statuses;
    }
}
