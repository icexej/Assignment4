
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class RequestQueue {

    private Queue<EmployeeRequest> requests = new LinkedList<>();

    private PriorityQueue<EmployeeRequest> priorityRequests =
            new PriorityQueue<>((r1, r2) ->
                    getPriorityValue(r1.getPriority())
                            - getPriorityValue(r2.getPriority()));

    private int getPriorityValue(String priority) {
        if (priority == null) {
            return 4;
        }

        switch (priority.toLowerCase()) {
            case "high":
                return 1;
            case "medium":
                return 2;
            case "low":
                return 3;
            default:
                return 4;
        }
    }

    // Сотрудник отправляет заявку
    public void submitRequest(EmployeeRequest request) {
        if (request == null) {
            System.out.println("Cannot submit an empty request.");
            return;
        }

        requests.offer(request);
        priorityRequests.offer(request);

        System.out.println("Request submitted by "
                + request.getRequester().getName());
    }

    // Исполнитель обрабатывает следующую заявку по FIFO
    public EmployeeRequest processNextRequest(Employee executor) {

        if (executor == null) {
            System.out.println("Executor is not specified.");
            return null;
        }

        EmployeeRequest request = requests.poll();

        if (request == null) {
            System.out.println("No requests available.");
            return null;
        }

        priorityRequests.remove(request);
        request.setExecutor(executor);

        System.out.println("Request processed by "
                + executor.getName());

        System.out.println(request);

        return request;
    }

    // Обработка заявки с наивысшим приоритетом
    public EmployeeRequest processHighestPriorityRequest(Employee executor) {

        if (executor == null) {
            System.out.println("Executor is not specified.");
            return null;
        }

        EmployeeRequest request = priorityRequests.poll();

        if (request == null) {
            System.out.println("No priority requests available.");
            return null;
        }

        requests.remove(request);
        request.setExecutor(executor);

        System.out.println("High-priority request processed by "
                + executor.getName());

        System.out.println(request);

        return request;
    }

    public int getRequestCount() {
        return requests.size();
    }

    public int getPriorityRequestCount() {
        return priorityRequests.size();
    }
}
