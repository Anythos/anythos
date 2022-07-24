package net.anythos.authentication;

import java.io.Serializable;

public record LoginResponse(String jwtToken) implements Serializable {
}
