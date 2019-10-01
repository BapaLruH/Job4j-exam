package ru.job4j.workersstore;

import java.util.List;

public interface Store {
    Profession createProfession(Profession profession);

    Worker createWorker(Worker worker);

    Profession updateProfession(int id, Profession profession);

    Worker updateWorker(int id, Worker worker);

    boolean deleteProfession(int id);

    boolean deleteWorker(int id);

    List<Profession> findAllProfessions();

    List<Worker> findAllWorkers();

    Profession findProfessionById(int id);

    Worker findWorkerById(int id);

    List<Profession> findProfessionsByName(String name);

    List<Worker> findWorkersByName(String name);

    List<Worker> findWorkersByProfession(Profession profession);
}
