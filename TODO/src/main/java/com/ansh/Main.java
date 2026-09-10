package com.ansh;

import com.ansh.entity.*;
import com.ansh.factory.TaskFactory;
import com.ansh.repository.*;
import com.ansh.service.TodoService;
import com.ansh.service.TodoServiceImpl;
import com.ansh.strategy.DeadlineSortStrategy;

import java.time.LocalDateTime;
import java.util.Set;



public class Main {

    public static void main(String[] args) {

        TaskRepository taskRepository = new InMemoryTaskRepository();
        TaskHistoryRepository historyRepository = new InMemoryTaskHistoryRepository();
        ActivityRepository activityRepository = new InMemoryActivityRepository();


        TodoService taskService = new TodoServiceImpl(taskRepository, historyRepository, activityRepository, new DeadlineSortStrategy());

        TaskFactory taskFactory = new TaskFactory();

        String user1 = "user-1";
        String user2 = "user-2";

        Task task1 = taskFactory.create(
                user1,
                "Complete LLD",
                "Prepare TODO application",
                LocalDateTime.now().plusHours(3),
                LocalDateTime.now(),
                Set.of("interview", "lld")
        );

        taskService.addTask(task1);

        Task task2 = taskFactory.create(
                user1,
                "Buy groceries",
                "Buy milk and bread",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now(),
                Set.of("personal")
        );

        taskService.addTask(task2);

        Task futureTask = taskFactory.create(
                user1,
                "Prepare presentation",
                "Prepare tomorrow's presentation",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1),
                Set.of("work")
        );

        taskService.addTask(futureTask);

        System.out.println("\nCurrent tasks:");

        taskService.listTasks(
                TaskFilter.builder()
                        .userId(user1)
                        .build()
        ).forEach(System.out::println);

        System.out.println("\nAll tasks including future tasks:");

        taskService.listTasks(
                TaskFilter.builder()
                        .userId(user1)
                        .includeFutureTasks(true)
                        .build()
        ).forEach(System.out::println);

        System.out.println("\nInterview tasks:");

        taskService.listTasks(
                TaskFilter.builder()
                        .userId(user1)
                        .tags(Set.of("interview"))
                        .build()
        ).forEach(System.out::println);


        Task modifiedTask = task1.toBuilder()
                .title("Complete Advanced LLD")
                .description("Prepare advanced Java LLD")
                .tags(Set.of("interview", "lld", "java"))
                .build();

        taskService.modifyTask(modifiedTask);

        System.out.println("\nModified task:");
        System.out.println(taskService.getTask(task1.getId()));

        System.out.println("\n List of Tasks with Tags");
        taskService.listTasks(
                TaskFilter.builder()
                        .tags(Set.of("interview", "lld", "java"))
                        .build()
        ).forEach(System.out::println);


        taskService.completeTask(task1.getId());

        System.out.println("\nTasks after completing task1:");

        taskService.listTasks(
                TaskFilter.builder()
                        .userId(user1)
                        .includeFutureTasks(true)
                        .build()
        ).forEach(System.out::println);



        taskService.removeTask(task2.getId());

        System.out.println("\nActivity log:");

        TimePeriod period = TimePeriod.builder()
                .from(LocalDateTime.now().minusMinutes(5))
                .to(LocalDateTime.now().plusMinutes(5))
                .build();

        for (Activity activity : taskService.getActivityLog(period)) {
            System.out.println(activity);
        }


        Statistics statistics = taskService.getStatistics(period);

        System.out.println("\nStatistics:");
        System.out.println(statistics);


        Task user2Task = taskFactory.create(
                user2,
                "Review PR",
                "Review backend PR",
                LocalDateTime.now().plusHours(5),
                LocalDateTime.now(),
                Set.of("work", "review")
        );

        taskService.addTask(user2Task);

        System.out.println("\nUser 2 tasks:");

        taskService.listTasks(
                TaskFilter.builder()
                        .userId(user2)
                        .build()
        ).forEach(System.out::println);
    }
}