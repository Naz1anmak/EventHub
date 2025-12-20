package ru.practicum.eventhub.domain.model;

import java.util.Set;

public enum ProjectUpdateMode {

    REPLACE {
        @Override
        public void apply(Category category, Set<Project> projects) {
            category.setProjects(projects);
        }
    },
    ADD {
        @Override
        public void apply(Category category, Set<Project> projects) {
            projects.forEach(category::addProject);
        }
    };

    public abstract void apply(Category category, Set<Project> projects);
}
