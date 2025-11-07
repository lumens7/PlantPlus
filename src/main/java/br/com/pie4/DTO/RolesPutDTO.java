package br.com.pie4.DTO;


import java.util.List;
import java.util.Set;

public class RolesPutDTO {
    private Long id;
    private Set<Long> idsRoles;

    public RolesPutDTO() {
    }
    public RolesPutDTO(Long id, Set<Long> idsRoles) {
        this.id = id;
        this.idsRoles = idsRoles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Long> getIdsRoles() {
        return idsRoles;
    }

    public void setIdsRoles(Set<Long> idsRoles) {
        this.idsRoles = idsRoles;
    }
}
