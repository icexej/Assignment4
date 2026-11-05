
public class EmployeeRequest {

    private int requestId;
    private String description;
    private String priority;
    private Employee requester;
    private Employee executor;

    public EmployeeRequest(int requestId, String description,
                           String priority, Employee requester) {
        this.requestId = requestId;
        this.description = description;
        this.priority = priority;
        this.requester = requester;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public Employee getRequester() {
        return requester;
    }

    public Employee getExecutor() {
        return executor;
    }

    public void setExecutor(Employee executor) {
        this.executor = executor;
    }

    @Override
    public String toString() {
        return "Request #" + requestId
                + " | Description: " + description
                + " | Priority: " + priority
                + " | Requester: " + requester.getName()
                + " | Executor: "
                + (executor == null ? "Not assigned" : executor.getName());
    }
}
