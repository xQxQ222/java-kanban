public class Main {

    public static void main(String[] args) {
        TaskManager manager=new TaskManager();
        Task task=new Task("Помыть посуду","ЛЛЛЛ",TaskStatus.NEW);
        Task task2=new Task("АААА","ААА",TaskStatus.IN_PROGRESS);
        Epic epic1=new Epic("JJJJ","ffff");
        manager.addAnyTask(TaskType.TASK,task);
        manager.addAnyTask(TaskType.TASK,task2);
        manager.addAnyTask(TaskType.EPIC,epic1);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getTaskById(1));
    }
}
