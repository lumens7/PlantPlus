package br.com.pie4.DTO;

import br.com.pie4.Domain.Roles;

import java.util.Set;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private Long id;
    private Set<Roles> roles;

    public JwtResponse(String accessToken, String username, Set<Roles> id, Long roles) {
        this.token = accessToken;
        this.username = username;
        this.roles = id;
        this.id = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }
}