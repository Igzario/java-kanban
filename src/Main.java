// Почему теперь protected? - изменил на private и применил LinkedList, из реализаций List, только он подходит

//модификаторы в InMemoryTaskManager поменял

//Очень странно конечно, что такой баг возникает. - если в одном блоке case x: вызывать один сканер несколько раз
//происходит баг, создал 4 сканера, и вызываю их по очереди, все ок...

//________________________

//я поменял логика работы истории и Managers, посмотри пожалуйста, так вроде более правильно

//------------------------

import Managers.InMemoryTaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.checkClass();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int userInput = printMenuAndRead(scanner);
            Scanner scanner1 = new Scanner(System.in);
            Scanner scanner2 = new Scanner(System.in);
            Scanner scanner3 = new Scanner(System.in);
            Scanner scanner4 = new Scanner(System.in);
            if (userInput == 0) {
                break;
            }
            switch (userInput) {
                case 1:
                    System.out.println("Введите название задачи:");
                    String nameTask = scanner1.nextLine();
                    System.out.println("Введите описание задачи");
                    String opisanieTask = scanner2.nextLine();
                    manager.newTask(nameTask, opisanieTask);
                    System.out.println("Готово");
                    continue;
                case 2:
                    System.out.println("Введите название Эпика:");
                    String nameEpic = scanner1.nextLine();
                    System.out.println("Введите описание Эпика");
                    String opisanieEpic = scanner2.nextLine();
                    manager.newEpic(nameEpic, opisanieEpic);
                    System.out.println("Готово");
                    continue;
                case 3:
                    System.out.println("Введите название подзадачи:");
                    String nameSubTask = scanner1.nextLine();
                    System.out.println("Введите описание подзадачи");
                    String opisanieSubTask = scanner2.nextLine();
                    System.out.println(manager.getEpicHashMap());
                    System.out.println("Введите ID Эпика");
                    int idEpic = scanner3.nextInt();
                    manager.newSubTask(nameSubTask, opisanieSubTask, idEpic);
                    System.out.println("Готово");
                    continue;
                case 4:
                    System.out.println("Введите ID целевой задачи");

                    int idTask = scanner1.nextInt();
                    if (manager.getTaskHashMap().containsKey(idTask)) {
                        System.out.println("Введите новое название задачи:");
                        String newNameTask = scanner2.nextLine();
                        System.out.println("Введите новое описание задачи");
                        String newOpisanieTask = scanner3.nextLine();
                        System.out.println("Выберите новый статус задачи: 1 - Новая задча, 2 - В процессе, 3 - Выполнена");
                        int status = scanner4.nextInt();
                        Task task = new Task(idTask, newNameTask, newOpisanieTask);
                        if (status == 1) {
                            task.status = Status.NEW;
                            System.out.println("Готово");
                        }
                        if (status == 2) {
                            task.status = Status.IN_PROGRESS;
                            System.out.println("Готово");
                        }
                        if (status == 3) {
                            task.status = Status.DONE;
                            System.out.println("Готово");
                        }
                        manager.refreshTask(task);
                        continue;
                    } else {
                        System.out.println("Такой задачи нет");
                        continue;
                    }
                case 5:
                    System.out.println("Введите ID целевого Эпика");
                    int idNewEpic = scanner1.nextInt();
                    if (manager.getEpicHashMap().containsKey(idNewEpic)) {
                        System.out.println("Введите новое название Эпика");
                        String newNameEpic = scanner2.nextLine();
                        System.out.println("Введите новое описание Эпика");
                        String newOpisanieEpic = scanner3.nextLine();
                        System.out.println("Выберите новый статус Эпика: 1 - Новая задча, 2 - В процессе, 3 - Выполнена");
                        int statusNew = scanner4.nextInt();
                        Epic epic = new Epic(idNewEpic, newNameEpic, newOpisanieEpic);
                        if (statusNew == 1) {
                            epic.status = Status.NEW;
                            System.out.println("Готово");
                        }
                        if (statusNew == 2) {
                            epic.status = Status.IN_PROGRESS;
                            System.out.println("Готово");
                        }
                        if (statusNew == 3) {
                            epic.status = Status.DONE;
                            System.out.println("Готово");
                        }
                        manager.refreshEpic(epic);
                        continue;
                    } else {
                        System.out.println("Такой задачи нет");
                    }
                case 6:
                    System.out.println("Введите ID целевой подзадачи`");
                    int idNewSubTask = scanner1.nextInt();
                    if (manager.getSubtaskHashMap().containsKey(idNewSubTask)) {
                        System.out.println("Введите новое название подзадачи");
                        String newNameSubTask = scanner2.nextLine();
                        System.out.println("Введите новое описание подзадачи");
                        String newOpisanieSubTask = scanner3.nextLine();
                        System.out.println("Выберите новый статус подзадачи: 1 - Новая задча, 2 - В процессе, 3 - Выполнена");
                        int statusNewSubTask = scanner4.nextInt();
                        int idEpicSub = manager.getSubtaskHashMap().get(idNewSubTask).idEpic;
                        Subtask subtask = new Subtask(idNewSubTask, newNameSubTask, newOpisanieSubTask, idEpicSub);
                        if (statusNewSubTask == 1) {
                            subtask.status = Status.NEW;
                            System.out.println("Готово");
                        }
                        if (statusNewSubTask == 2) {
                            subtask.status = Status.IN_PROGRESS;
                            System.out.println("Готово");
                        }
                        if (statusNewSubTask == 3) {
                            subtask.status = Status.DONE;
                            System.out.println("Готово");
                        }
                        manager.refreshSubTask(subtask);
                        continue;
                    } else {
                        System.out.println("Такой задачи нет");
                        continue;
                    }
                case 7:
                    System.out.println("Введите ID задачи");
                    int idSearchTask = scanner1.nextInt();
                    if (manager.getTaskHashMap().containsKey(idSearchTask)) {
                        System.out.println(manager.searchTaskForId(idSearchTask));
                        System.out.println("Готово");
                        continue;
                    } else {
                        System.out.println("Такой задачи нет");
                        continue;
                    }
                case 8:
                    System.out.println("Введите ID Эпика");
                    int idSearchEpic = scanner1.nextInt();
                    if (manager.getEpicHashMap().containsKey(idSearchEpic)) {
                        System.out.println(manager.searchEpicForId(idSearchEpic));
                        System.out.println("Готово");
                        continue;
                    } else {
                        System.out.println("Такого эпика нет");
                        continue;
                    }
                case 9:
                    System.out.println("Введите ID подзадачи");
                    int idSearchSubTask = scanner1.nextInt();
                    if (manager.getSubtaskHashMap().containsKey(idSearchSubTask)) {
                        System.out.println(manager.searchSubtaskForId(idSearchSubTask));
                        System.out.println("Готово");
                        continue;
                    }
                case 10:
                    manager.clearTask();
                    continue;
                case 11:
                    manager.clearEpic();
                    continue;
                case 12:
                    manager.clearSubtask();
                    continue;
                case 13:
                    System.out.println("Введите ID задачи для удаления");
                    int idDelTask = scanner1.nextInt();
                    manager.deleteTaskForId(idDelTask);
                    continue;
                case 14:
                    System.out.println("Введите ID Эпика для удаления");
                    int idDelEpic = scanner1.nextInt();
                    manager.deleteEpicForId(idDelEpic);
                    continue;
                case 15:
                    System.out.println("Введите ID подзадачи для удаления");
                    int idDelSubtask = scanner1.nextInt();
                    manager.deleteSubTaskForId(idDelSubtask);
                    continue;
                case 16:
                    System.out.println(manager);
                    continue;
                case 17:
                    System.out.println(manager.getHistory());


            }
        }
    }


    static int printMenuAndRead(Scanner scanner) {
        String userInputString;
        while (true) {
            System.out.println("Что вы хотите сделать? ");
            System.out.println("1 - Создать задачу");
            System.out.println("2 - Создать Эпик");
            System.out.println("3 - Создать подзадачу");
            System.out.println("4 - Обновить задачу");
            System.out.println("5 - Обновить Эпик");
            System.out.println("6 - Обновить подзадачу");
            System.out.println("7 - Найти задачу по ID");
            System.out.println("8 - Найти Эпик по ID");
            System.out.println("9 - Найти подзадачу по ID");
            System.out.println("10 - Очистить список задач");
            System.out.println("11 - Очистить список Эпиков");
            System.out.println("12 - Очистить список подзадач");
            System.out.println("13 - Удалить задачу по ID");
            System.out.println("14 - Удалить Эпик по ID");
            System.out.println("15 - Удалить подзадачу по ID");
            System.out.println("16 - Распечатать полный список задач, эпиков и подзадач");
            System.out.println("17 - Историю запросов");


            System.out.println("exit - Выход");

            try {
                userInputString = scanner.nextLine().trim();
                if (userInputString.equalsIgnoreCase("exit")) {
                    return 0;
                } else {

                    int userInputInt = Integer.parseInt(userInputString);

                    if (userInputInt >= 1 && userInputInt <= 17) {
                        return userInputInt;
                    } else {
                        System.out.println("Не верная команда");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Не верная команда");
            }
        }
    }
}