package net.pkhapps.mvvm4vaadin.demo.data;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

import static java.util.Objects.requireNonNull;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Ticket extends AbstractPersistable<Long> {

    public static final int SUMMARY_MAX_LENGTH = 300;
    public static final int DESCRIPTION_MAX_LENGTH = 1000;

    @Column(length = SUMMARY_MAX_LENGTH, nullable = false)
    private String summary;

    @Column(length = DESCRIPTION_MAX_LENGTH, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TicketType type;

    @Enumerated(EnumType.STRING)
    private TicketState state;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdOn;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant lastModifiedOn;

    protected Ticket() {
    }

    public Ticket(TicketType type, String summary, String description) {
        this.type = requireNonNull(type);
        this.summary = StringUtils.truncate(requireNonNull(summary), SUMMARY_MAX_LENGTH);
        this.description = StringUtils.truncate(requireNonNull(description), DESCRIPTION_MAX_LENGTH);
        this.state = TicketState.PENDING;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public TicketState getState() {
        return state;
    }

    public void setState(TicketState state) {
        this.state = state;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Instant getLastModifiedOn() {
        return lastModifiedOn;
    }
}
