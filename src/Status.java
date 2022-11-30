public enum Status {

    NEW ("NEW"),
    DONE ("DONE"),
    IN_PROGRESS ("IN_PROGRESS");
    private String title;

    Status(String title) {
        this.title=title;
    }
}
