package br.com.vrsoftware.dto.jira;

import br.com.vrsoftware.config.ObjectMapperConfig;

import java.util.Objects;

public class ActorDTO {

    String displayName;
    String emailAddress;

    public ActorDTO() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ActorDTO actorDTO = (ActorDTO) o;
        return Objects.equals(displayName, actorDTO.displayName) && Objects.equals(emailAddress, actorDTO.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, emailAddress);
    }

    @Override
    public String toString() {
        return ObjectMapperConfig.toJson(this);
    }
}
