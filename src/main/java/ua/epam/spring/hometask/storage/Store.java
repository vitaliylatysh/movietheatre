package ua.epam.spring.hometask.storage;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

import java.util.HashMap;
import java.util.Map;

public class Store {
    private Map<String, User> userMap = new HashMap<>();
    private Map<String, Ticket> ticketMap = new HashMap<>();
    private Map<String, Event> eventMap = new HashMap<>();
    private Map<String, Auditorium> auditoriumMap = new HashMap<>();

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public Map<String, Ticket> getTicketMap() {
        return ticketMap;
    }

    public Map<String, Auditorium> getAuditoriumMap() {
        return auditoriumMap;
    }

    public Map<String, Event> getEventMap() {
        return eventMap;
    }
}
