
import java.util.Comparator;

public class EmployeeComparators {

    // Sort by salary (highest first)
    public static Comparator<Employee> bySalary =
            Comparator.comparingDouble(Employee::calculateSalary).reversed();

    // Sort by experience (most experienced first)
    public static Comparator<Employee> byExperience =
            Comparator.comparingInt(Employee::getExperienceYears).reversed();

    // Sort by followers (highest first)
    public static Comparator<SocialMediaManager> byFollowers =
            Comparator.comparingInt(
                    SocialMediaManager::getFollowersCount
            ).reversed();
}
