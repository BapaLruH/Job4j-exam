package ru.job4j.mvc;

import java.util.Arrays;
import java.util.List;

public class QuestionsStorage {
    private static List<Question> questions;
    private static volatile QuestionsStorage instance;

    private QuestionsStorage() {
        questions = Arrays.asList(
                new Question(
                        1, "How many primitive variables does Java have?",
                        Arrays.asList(
                                new Option(1, "1.1"), new Option(2, "1.2"),
                                new Option(3, "1.3"), new Option(4, "1.4")
                        ), 4
                ),
                new Question(
                        2, "What is Java Virtual Machine?",
                        Arrays.asList(
                                new Option(1, "2.1"), new Option(2, "2.2"),
                                new Option(3, "2.3"), new Option(4, "2.4")
                        ), 4
                ),
                new Question(
                        3, "What is happen if we try unboxing null?",
                        Arrays.asList(
                                new Option(1, "3.1"), new Option(2, "3.2"),
                                new Option(3, "3.3"), new Option(4, "3.4")
                        ), 4
                )
        );
    }

    public static QuestionsStorage getInstance() {
        QuestionsStorage localInstance = instance;
        if (localInstance == null) {
            synchronized (QuestionsStorage.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new QuestionsStorage();
                }
            }
        }
        return localInstance;
    }

    public int size() {
        return questions.size();
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public List<Question> getAllQuestions() {
        return questions;
    }
}
