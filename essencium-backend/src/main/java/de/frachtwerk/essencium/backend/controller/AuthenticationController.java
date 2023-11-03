/*
 * Copyright (C) 2023 Frachtwerk GmbH, Leopoldstraße 7C, 76133 Karlsruhe.
 *
 * This file is part of essencium-backend.
 *
 * essencium-backend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * essencium-backend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with essencium-backend. If not, see <http://www.gnu.org/licenses/>.
 */

package de.frachtwerk.essencium.backend.controller;

import de.frachtwerk.essencium.backend.configuration.properties.AppConfigProperties;
import de.frachtwerk.essencium.backend.configuration.properties.JwtConfigProperties;
import de.frachtwerk.essencium.backend.model.AbstractBaseUser;
import de.frachtwerk.essencium.backend.model.dto.LoginRequest;
import de.frachtwerk.essencium.backend.model.dto.TokenResponse;
import de.frachtwerk.essencium.backend.security.event.CustomAuthenticationSuccessEvent;
import de.frachtwerk.essencium.backend.service.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@ConditionalOnProperty(
    value = "essencium-backend.overrides.auth-controller",
    havingValue = "false",
    matchIfMissing = true)
@Tag(name = "AuthenticationController", description = "Set of endpoints used for authentication")
public class AuthenticationController {
  private final AppConfigProperties appConfigProperties;
  private final JwtConfigProperties jwtConfigProperties;
  private final JwtTokenService jwtTokenService;
  private final AuthenticationManager authenticationManager;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  public AuthenticationController(
      AppConfigProperties appConfigProperties,
      JwtConfigProperties jwtConfigProperties,
      JwtTokenService jwtTokenService,
      AuthenticationManager authenticationManager,
      ApplicationEventPublisher applicationEventPublisher) {
    this.appConfigProperties = appConfigProperties;
    this.jwtConfigProperties = jwtConfigProperties;
    this.jwtTokenService = jwtTokenService;
    this.authenticationManager = authenticationManager;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @PostMapping("/token")
  @Operation(description = "Log in to request a new JWT token")
  public TokenResponse postLogin(
      @RequestBody @Validated LoginRequest login,
      @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent,
      HttpServletResponse response) {
    try {
      // Authenticate using username and password
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(login.username(), login.password()));
      applicationEventPublisher.publishEvent(
          new CustomAuthenticationSuccessEvent(
              authentication,
              String.format("Login successful for user %s", authentication.getName())));

      // Get refresh token
      String refreshToken =
          jwtTokenService.login(
              (AbstractBaseUser<? extends Serializable>) authentication.getPrincipal(), userAgent);

      // Store refresh token as cookie limited to renew endpoint
      Cookie cookie = new Cookie("refreshToken", refreshToken);
      cookie.setHttpOnly(true);
      cookie.setPath("/auth/renew");
      cookie.setMaxAge(jwtConfigProperties.getRefreshTokenExpiration());
      cookie.setDomain(appConfigProperties.getUrl());
      cookie.setSecure(true);

      response.addCookie(cookie);

      // create first access token and return it.
      return new TokenResponse(jwtTokenService.renew(refreshToken, userAgent));

    } catch (AuthenticationException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
    }
  }

  @PostMapping("/renew")
  @CrossOrigin(origins = "${app.url}", allowCredentials = "true")
  @Operation(description = "Request a new JWT access token, given a valid refresh token")
  public TokenResponse postRenew(
      @RequestHeader(value = HttpHeaders.USER_AGENT) String userAgent,
      @CookieValue(value = "refreshToken") String refreshToken) {
    try {
      return new TokenResponse(jwtTokenService.renew(refreshToken, userAgent));
    } catch (AuthenticationException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
    }
  }

  @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
  public final ResponseEntity<?> collectionOptions() {
    return ResponseEntity.ok().allow(getAllowedMethods().toArray(new HttpMethod[0])).build();
  }

  protected Set<HttpMethod> getAllowedMethods() {
    return Set.of(HttpMethod.HEAD, HttpMethod.POST, HttpMethod.OPTIONS);
  }
}
