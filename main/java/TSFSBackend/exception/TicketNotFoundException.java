package TSFSBackend.exception;

public class TicketNotFoundException extends RuntimeException{
    public TicketNotFoundException(int ticketid) {
        super("Ticket with ticket id " + String.valueOf(ticketid) + " does not exist");
    }
}
