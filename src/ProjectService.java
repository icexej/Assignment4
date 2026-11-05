
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectService {

    private Map<Integer, MarketingProject> projects = new HashMap<>();

    // CREATE: добавление проекта
    public boolean addProject(MarketingProject project) {
        if (project == null) {
            return false;
        }

        if (projects.containsKey(project.getProjectId())) {
            return false;
        }

        projects.put(project.getProjectId(), project);
        return true;
    }

    // READ: поиск проекта по ID
    public MarketingProject getProjectById(int id) {
        return projects.get(id);
    }

    // READ: получение всех проектов
    public List<MarketingProject> getAllProjects() {
        return new ArrayList<>(projects.values());
    }

    // Добавление сотрудника в проект
    public boolean assignEmployee(int projectId, Employee employee) {
        MarketingProject project = projects.get(projectId);

        if (project == null || employee == null) {
            return false;
        }

        project.addEmployee(employee);
        return true;
    }

    // Удаление сотрудника из проекта
    public boolean removeEmployeeFromProject(int projectId, Employee employee) {
        MarketingProject project = projects.get(projectId);

        if (project == null || employee == null) {
            return false;
        }

        project.removeEmployee(employee);
        return true;
    }

    // DELETE: удаление проекта
    public boolean deleteProject(int id) {
        MarketingProject project = projects.remove(id);

        if (project == null) {
            return false;
        }

        for (Employee employee : project.getEmployees()) {
            project.removeEmployee(employee);
        }

        return true;
    }

    public int getProjectCount() {
        return projects.size();
    }
}
