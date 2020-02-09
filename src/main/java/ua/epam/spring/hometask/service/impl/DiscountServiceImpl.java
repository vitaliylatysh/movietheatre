package ua.epam.spring.hometask.service.impl;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.UserService;
import ua.epam.spring.hometask.strategy.BirthdayStrategy;
import ua.epam.spring.hometask.strategy.DiscountStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DiscountServiceImpl implements DiscountService {

    private UserService userService;
    private EventService eventService;
    private List<DiscountStrategy> discountStrategy;

    public DiscountServiceImpl(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    public DiscountServiceImpl() {
    }

    @Override
    public byte getDiscount(@Nullable User user,
                            @Nonnull Event event,
                            @Nonnull LocalDateTime airDateTime,
                            long numberOfTickets) {
        byte birthDateDiscount = 0;
        byte everyNTicketDiscount = 0;

        if (user == null) {
            return 0;
        }

        LocalDate userBirthDate = user.getBirthDate();

        everyNTicketDiscount = checkEveryNTicketDiscount(user, event, airDateTime, numberOfTickets, everyNTicketDiscount, userBirthDate);

        birthDateDiscount = checkBirthdayDiscount(user, event, airDateTime, numberOfTickets, birthDateDiscount, userBirthDate);

        if (birthDateDiscount > everyNTicketDiscount) {
            return birthDateDiscount;
        }
        return everyNTicketDiscount;
    }

    private byte checkEveryNTicketDiscount(User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime, long numberOfTickets, byte everyNTicketDiscount, LocalDate userBirthDate) {
        if (airDateTime.toLocalDate().equals(userBirthDate)) {
            Optional<DiscountStrategy> first = discountStrategy.stream()
                    .filter(strategy -> strategy instanceof BirthdayStrategy).findFirst();

            if (first.isPresent()) {
                everyNTicketDiscount = first.get().count(user, event, airDateTime, numberOfTickets);
            }
        }
        return everyNTicketDiscount;
    }

    private byte checkBirthdayDiscount(User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime, long numberOfTickets, byte birthDateDiscount, LocalDate userBirthDate) {
        if (diffInDays(userBirthDate, airDateTime) >= 0 && diffInDays(userBirthDate, airDateTime) <= 5) {
            Optional<DiscountStrategy> first = discountStrategy.stream()
                    .filter(strategy -> strategy instanceof BirthdayStrategy).findFirst();

            if (first.isPresent()) {
                birthDateDiscount = first.get().count(user, event, airDateTime, numberOfTickets);
            }
        }
        return birthDateDiscount;
    }

    private int diffInDays(LocalDate userBirthDate, @Nonnull LocalDateTime airDateTime) {
        return airDateTime.toLocalDate().getDayOfYear() - userBirthDate.getDayOfYear();
    }

    public List<DiscountStrategy> getDiscountStrategy() {
        return discountStrategy;
    }

    public void setDiscountStrategy(List<DiscountStrategy> discountStrategy) {
        this.discountStrategy = discountStrategy;
    }
}
