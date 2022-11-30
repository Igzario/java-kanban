//По сути методы newTask(), newEpic(), newSubTask() можно сделать void. - можно но там не понятно, что за логика будет
// дальше, и т.к. в методы по обновлению объекты прилетают как параметры, я предположил, что и тут нужно объект возвращать.

// Многие метода класса не используются. - такое задание... а вызывать их все, и так очень много лишнего в мейне
//Статусы (DONE, IN_PROGRESS, NEW) можно выделить в отдельный класс enum (почитай про это). - почитал, сделал, вроде ок

//Метод toString() - это переопределенный метод от класса Object - исправил, спасибо

//Здесь и далее в коде приведи код-стайл в порядок (особенно лишние пустые строки). - исправил, оставил часть, что бы
//было удобнее читать код

//Список конечно умеет авто определять тип параметра, но на это полагаться не стоит - вот тут не понял, вроде прописано

//Лучше использовать StringBuilder вместо String - но я же не могу этого сделать в переопределяемом toString...или могу?

//

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
                manager.newTask("Решить задачу", "Решить задачу на практикуме");
                manager.newTask("Заправить авто", "95го до полного");
                manager.newEpic("Переезд", "Организовать переезд");
                manager.newSubTask("Собрать вещи", "Упаковать в коробки", 1);
                manager.newSubTask("Вызвать такси", "Нужно грузовое", 1);
                manager.newEpic("Переобуть авто", "Переобуть на лето");
                manager.newSubTask("Забрать резину", "Из гаража", 2);
                System.out.println(manager);

        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");

                Epic epic = new Epic(2, "Переобуть авто обратно", "Переобуть на зиму");
                manager.refreshEpic(epic);
                System.out.println(manager);

        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");

                Subtask subtask = new Subtask(3, "Купить резину", "Купить зимнюю резину", 2);
                subtask.status = Status.DONE;
                manager.refreshSubTask(subtask);
                System.out.println(manager);

        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");

                Subtask subtask2 = new Subtask(1, "Собрать вещи", "Упаковать в коробки", 1);
                subtask2.status = Status.IN_PROGRESS;
                manager.refreshSubTask(subtask2);
                System.out.println(manager);

        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");

                manager.deleteSubTaskForId(2);
                System.out.println(manager);

        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");

                manager.deleteEpicForId(2);
                System.out.println(manager);

        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");

                manager.deleteTaskForId(1);
                System.out.println(manager);

        System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");
    }
}
