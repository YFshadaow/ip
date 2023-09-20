package cn.yfshadaow.cs2113.ip.command;

import cn.yfshadaow.cs2113.ip.*;
import cn.yfshadaow.cs2113.ip.task.Deadline;
import cn.yfshadaow.cs2113.ip.task.Event;
import cn.yfshadaow.cs2113.ip.task.Task;
import cn.yfshadaow.cs2113.ip.task.Todo;

import java.util.Arrays;
import java.util.List;

public class CommandHandler {

    private final XiaoAiBot bot;

    public CommandHandler(XiaoAiBot bot) {
        this.bot = bot;
    }

    public void handleCommand(String command) {
        if (command == null) {
            bot.sendMessage("Command is null!");
            return;
        }
        String[] splitCommand = command.split(" ");
        if (splitCommand.length < 1) {
            bot.sendMessage("No command detected!");
            return;
        }
        String[] args = Arrays.copyOfRange(splitCommand, 1, splitCommand.length);
        bot.sendSplit();
        switch (splitCommand[0]) {
            case "bye": {
                bot.setShouldQuit(true);
                break;
            }
            case "todo": {
                Todo todo;
                try {
                    todo = Todo.parseTodo(args);
                } catch (Exception e) {
                    bot.sendMessage(String.format("Error parsing task: %s", e.getMessage()));
                    break;
                }
                bot.getTasks().add(todo);
                bot.sendMessageWithoutSplit("Got it. I've added this task:");
                bot.sendMessageWithoutSplit(todo.toStringWithIsDone());
                bot.sendMessage("Now you have " + bot.getTasks().size() + " tasks in the list.");
                break;
            }
            case "deadline": {
                Deadline deadline;
                try {
                    deadline = Deadline.parseDeadline(args);
                } catch (Exception e) {
                    bot.sendMessage(String.format("Error parsing task: %s", e.getMessage()));
                    break;
                }
                bot.getTasks().add(deadline);
                bot.sendMessageWithoutSplit("Got it. I've added this task:");
                bot.sendMessageWithoutSplit(deadline.toStringWithIsDone());
                bot.sendMessage("Now you have " + bot.getTasks().size() + " tasks in the list.");
                break;
            }
            case "event": {
                Event event;
                try {
                    event = Event.parseEvent(args);
                } catch (Exception e) {
                    bot.sendMessage(String.format("Error parsing task: %s", e.getMessage()));
                    break;
                }
                bot.getTasks().add(event);
                bot.sendMessageWithoutSplit("Got it. I've added this task:");
                bot.sendMessageWithoutSplit(event.toStringWithIsDone());
                bot.sendMessage("Now you have " + bot.getTasks().size() + " tasks in the list.");
                break;
            }
            case "mark": {
                if (args.length != 1) {
                    bot.sendMessage("Incorrect arguments");
                    break;
                }
                int index;
                try {
                    index = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    bot.sendMessage(String.format("Error parsing int: %s", e.getMessage()));
                    break;
                }
                try {
                    bot.getTasks().get(index - 1).setDone(true);
                } catch (IndexOutOfBoundsException e) {
                    bot.sendMessage(String.format("Error getting task: %s", e.getMessage()));
                    break;
                }
                bot.sendMessageWithoutSplit("Nice! I've marked this task as done:");
                bot.sendMessage(bot.getTasks().get(index - 1).toStringWithIsDone());
                break;
            }
            case "unmark": {
                if (args.length != 1) {
                    bot.sendMessage("Incorrect arguments");
                    break;
                }
                int index;
                try {
                    index = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    bot.sendMessage(String.format("Error parsing int: %s", e.getMessage()));
                    break;
                }
                try {
                    bot.getTasks().get(index - 1).setDone(false);
                } catch (Exception e) {
                    bot.sendMessage(String.format("Error getting task: %s", e.getMessage()));
                    break;
                }
                bot.sendMessageWithoutSplit("OK, I've marked this task as not done yet:");
                bot.sendMessage(bot.getTasks().get(index - 1).toStringWithIsDone());
                break;
            }
            case "list": {
                List<Task> tasks = bot.getTasks();
                for (int i = 0; i < tasks.size(); i += 1) {
                    Task task = tasks.get(i);
                    bot.sendMessageWithoutSplit((i + 1) + "." + task.toStringWithIsDone());
                }
                bot.sendSplit();
                break;
            }
            default: {
                bot.sendMessage("Unknown command");
            }
        }
    }
}