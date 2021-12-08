package net.pkhapps.mvvm4vaadin.demo.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
