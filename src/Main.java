// Семён, приветствую! Я во многих местах сильно не понял задание, в чатах тоже понимания не нашел,
// по этому сделал как понял, скорее всего получилась фигня...
// Где не так сделал, пиши, переделаю. Спасибо!

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали проверять!!!");


        Manager manager = new Manager();
        manager.newTask("Решить задачу", "Решить задачу на практикуме");
        manager.newTask("Заправить авто", "95го до полного");

        manager.newEpic("Переезд", "Организовать переезд");
        manager.newSubTask("Собрать вещи", "Упаковать в коробки", 1);
        manager.newSubTask("Вызвать такси", "Нужно грузовое", 1);

        manager.newEpic("Переобуть авто", "Переобуть на лето");
        manager.newSubTask("Забрать резину", "Из гаража", 2);

        System.out.println(manager.toString());
        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");


        Epic epic = new Epic(2, "Переобуть авто обратно", "Переобуть на зиму");
        manager.refreshEpic(epic);
        System.out.println(manager.toString());
        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");


        Subtask subtask = new Subtask(3, "Купить резину", "Купить зимнюю резину", 2);
        subtask.status = "DONE";
        manager.refreshSubTask(subtask);
        System.out.println(manager.toString());
        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");


        Subtask subtask2 = new Subtask(1, "Собрать вещи", "Упаковать в коробки", 1);
        subtask2.status = "IN_PROGRESS";
        manager.refreshSubTask(subtask2);
        System.out.println(manager.toString());
        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");

        manager.deleteSubTaskForId(2);
        System.out.println(manager.toString());
        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");

        manager.deleteEpicForId(2);
        System.out.println(manager.toString());
        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");

        manager.deleteTaskForId(1);
        System.out.println(manager.toString());
        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");

    }
}
