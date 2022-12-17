package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tickets")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketsRootSeedDtoXML {

    @XmlElement(name = "ticket")
    private List<TicketDto> ticket;

    public TicketsRootSeedDtoXML() {
    }

    public List<TicketDto> getTicket() {
        return ticket;
    }

    public void setTicket(List<TicketDto> ticket) {
        this.ticket = ticket;
    }
}
