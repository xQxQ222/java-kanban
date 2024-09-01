public class Main {

    public static void main(String[] args) {
        TaskManager manager=new TaskManager();
        Epic epic=new Epic("Сделать уборку","Нужно сделать полную уборку");
        manager.addAnyTask(epic);
        System.out.println(manager.getAllTasks());
        Subtask subtask1=new Subtask("Пропылесосить пол","Взять пылесос и пропылесосить пол",TaskStatus.DONE,1);
        manager.addAnyTask(subtask1);
        System.out.println(manager.getAllTasks());
        Subtask subtask2=new Subtask("Помыть посуду","Помыть с губкой и мылом",TaskStatus.IN_PROGRESS,1);
        manager.addAnyTask(subtask2);
        System.out.println(manager.getAllTasks());
        subtask1.setTaskStatus(TaskStatus.NEW);
        subtask2.setTaskStatus(TaskStatus.NEW);
        manager.updateTask(subtask1);
        manager.updateTask(subtask2);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getSubtasksByEpicId(1));
        System.out.println(manager.getTaskById(245));
        System.out.println(manager.getTaskById(2));
        manager.deleteTaskById(3);
        System.out.println(manager.getAllTasks());
        manager.deleteTaskById(1);
        System.out.println(manager.getAllTasks());
        manager.addAnyTask(epic);
        manager.deleteAllTasks();
        System.out.println(manager.getAllTasks());
    }
}
