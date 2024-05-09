package user;

import annotations.Ammar;
import java.util.ArrayList;
import java.util.Arrays;

@Ammar
public class Schedule
{
    private final int AM8 = 0;
    private final int AM9 = 1;
    private final int AM10 = 2;
    private final int AM11 = 3;
    private final int PM1 = 4;
    private final int PM2 = 5;
    
    ArrayList<ScheduleHour> scheduleHours = new ArrayList<>(Arrays.asList(
            new ScheduleHour("8am"), new ScheduleHour("9am"),
            new ScheduleHour("10am"), new ScheduleHour("11am"),
            new ScheduleHour("1pm"), new ScheduleHour("2pm")));
    
    public void addCourseToSchedule (Course course) throws Exception
    {
        String coursedays = course.getCourseDays();
        int coursehour = course.getCourseHours();
        String courseName = course.getCode();
        
        if (!canAdd(course))
            throw new Exception("Conflict in Schedule");
        
        switch(coursehour)
        {
            case 8: scheduleHours.get(AM8).addCourse(coursedays, courseName);
                break;
            case 9: scheduleHours.get(AM9).addCourse(coursedays, courseName);
                break;
            case 10: scheduleHours.get(AM10).addCourse(coursedays, courseName);
                break;
            case 11: scheduleHours.get(AM11).addCourse(coursedays, courseName);
                break;
            case 1: scheduleHours.get(PM1).addCourse(coursedays, courseName);
                break;
            case 2: scheduleHours.get(PM2).addCourse(coursedays, courseName);
                break;  
        }
    }
    
    public void removeCourseFromSchedule (Course course)
    {
        String coursedays = course.getCourseDays();
        int coursehour = course.getCourseHours();
        String courseName = course.getCode();
        
        switch(coursehour)
        {
            case 8: scheduleHours.get(AM8).removeCourse(coursedays, courseName);
                break;
            case 9: scheduleHours.get(AM9).removeCourse(coursedays, courseName);
                break;
            case 10: scheduleHours.get(AM10).removeCourse(coursedays, courseName);
                break;
            case 11: scheduleHours.get(AM11).removeCourse(coursedays, courseName);
                break;
            case 1: scheduleHours.get(PM1).removeCourse(coursedays, courseName);
                break;
            case 2: scheduleHours.get(PM2).removeCourse(coursedays, courseName);
                break;  
        }
    }
    
    public boolean canAdd(Course course) throws Exception
    {
        String coursedays = course.getCourseDays();
        int coursehour = course.getCourseHours();
        
        switch(coursehour)
        {
            case 8: return scheduleHours.get(AM8).canAdd(coursedays);

            case 9: return scheduleHours.get(AM9).canAdd(coursedays);

            case 10: return scheduleHours.get(AM10).canAdd(coursedays);

            case 11: return scheduleHours.get(AM11).canAdd(coursedays);

            case 1: return scheduleHours.get(PM1).canAdd(coursedays);

            case 2: return scheduleHours.get(PM2).canAdd(coursedays);
        }
        throw new Exception("Invalid course hour");
    }

    public ArrayList<ScheduleHour> getScheduleHours()
    {
        return scheduleHours;
    }
}
