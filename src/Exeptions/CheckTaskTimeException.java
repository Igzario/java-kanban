package Exeptions;

public class CheckTaskTimeException extends RuntimeException {

    public CheckTaskTimeException() {
        super("Ошибка: пересечение времени исполнения задачи");
    }

}
