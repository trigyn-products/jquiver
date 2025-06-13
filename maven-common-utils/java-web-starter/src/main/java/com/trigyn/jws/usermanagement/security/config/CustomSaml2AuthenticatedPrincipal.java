package com.trigyn.jws.usermanagement.security.config;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;

public class CustomSaml2AuthenticatedPrincipal implements Saml2AuthenticatedPrincipal {

    private String name;
    private String firstName;
    private String lastName;
    private String email;

    public CustomSaml2AuthenticatedPrincipal(String name,String firstName,String lastName,String email) {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public String getName() {
        return name;
    }

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    // Implement other methods of Saml2AuthenticatedPrincipal if needed
}
