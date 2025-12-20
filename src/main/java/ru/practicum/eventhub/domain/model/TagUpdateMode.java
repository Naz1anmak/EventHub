package ru.practicum.eventhub.domain.model;

import java.util.Set;

public enum TagUpdateMode {

    REPLACE {
        @Override
        public void apply(Event event, Set<Tag> tags) {
            event.setTags(tags);
        }
    },
    ADD {
        @Override
        public void apply(Event event, Set<Tag> tags) {
            tags.forEach(event::addTag);
        }
    };

    public abstract void apply(Event event, Set<Tag> tags);
}
