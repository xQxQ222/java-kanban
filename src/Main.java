public class Main {

    public static void main(String[] args) {
        TaskManager manager=new TaskManager();
        /*Task task=new Task("Помыть посуду","ЛЛЛЛ",TaskStatus.NEW);
        Task task2=new Task("АААА","ААА",TaskStatus.IN_PROGRESS);
        Epic epic1=new Epic("JJJJ","ffff");
        manager.addAnyTask(TaskType.TASK,task);
        manager.addAnyTask(TaskType.TASK,task2);
        manager.addAnyTask(TaskType.EPIC,epic1);
        System.out.println(manager.getAllTasks());
        Subtask subtask=new Subtask("nafjki","afijafij",TaskStatus.NEW,3);
        manager.addAnyTask(TaskType.SUBTASK,subtask);
        task2.setTaskStatus(TaskStatus.DONE);
        manager.updateTask(task2);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getSubtasksByEpicId(23));
        manager.deleteAllTasks();
        System.out.println(manager.getAllTasks());*/
        Epic epic=new Epic("Сделать уборку","Нужно сделать полную уборку");
        manager.addAnyTask(TaskType.EPIC,epic);
        System.out.println(manager.getAllTasks());
        Subtask subtask1=new Subtask("Пропылесосить пол","Взять пылесос и пропылесосить пол",TaskStatus.DONE,1);
        manager.addAnyTask(TaskType.SUBTASK,subtask1);
        System.out.println(manager.getAllTasks());
        Subtask subtask2=new Subtask("Помыть посуду","Помыть с губкой и мылом",TaskStatus.IN_PROGRESS,1);
        manager.addAnyTask(TaskType.SUBTASK,subtask2);
        System.out.println(manager.getAllTasks());
        subtask1.setTaskStatus(TaskStatus.NEW);
        subtask2.setTaskStatus(TaskStatus.NEW);
        manager.updateTask(subtask1);
        manager.updateTask(subtask2);
        System.out.println(manager.getAllTasks());

    }
}
