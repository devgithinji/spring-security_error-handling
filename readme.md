# Spring Security Error Handling with RestControllerAdvice - Simple README

## Overview

This README guides you through the process of setting up error handling in a Spring Security-enabled REST API using three key components: `AccessDeniedHandler`, `AuthenticationEntryPoint`, and `HandlerExceptionResolver`. The goal is to centralize error management and send all errors to a `@RestControllerAdvice` class for consistent and customized responses.

## Components

### 1. AccessDeniedHandler

The `CustomAccessDeniedHandler` component handles access denied errors. When an authenticated user attempts to access a resource without the necessary permissions, this handler takes over. It collaborates with `HandlerExceptionResolver` to ensure a unified approach to responding to access denied scenarios.

### 2. AuthenticationEntryPoint

The `CustomAuthEntryPoint` component manages authentication errors. If an unauthenticated user tries to access a secured resource, this entry point is triggered. It works in tandem with `HandlerExceptionResolver` to provide a standardized response to authentication-related issues.

### 3. HandlerExceptionResolver

The `GlobalExceptionHandler` is a `@RestControllerAdvice` class that handles general exceptions in a RESTful manner. It serves as a centralized hub for error handling, allowing you to provide tailored responses based on the type of exception. This enhances the overall consistency of error messages throughout your application.

## Usage

1. **CustomAccessDeniedHandler:**

   ```java
   @Component
   public class CustomAccessDeniedHandler implements AccessDeniedHandler {
       // Detailed implementation provided in the code
   }
   ```

2. **CustomAuthEntryPoint:**

   ```java
   @Component
   public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
       // Detailed implementation provided in the code
   }
   ```

3. **GlobalExceptionHandler:**

   ```java
   @RestControllerAdvice
   public class GlobalExceptionHandler {
       // Detailed implementation provided in the code
   }
   ```

4. **Security Configuration:**

   ```java
   @Configuration
   public class SecurityConfig {
       // Detailed implementation provided in the code
   }
   ```

Customize the provided code snippets based on your specific application requirements. This setup ensures that your Spring Security-enabled REST API has a centralized and consistent error-handling mechanism, offering a user-friendly experience across various error scenarios.