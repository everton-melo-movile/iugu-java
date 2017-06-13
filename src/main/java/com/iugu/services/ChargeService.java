package com.iugu.services;

import com.iugu.IuguConfiguration;
import com.iugu.exceptions.IuguException;
import com.iugu.model.Charge;
import com.iugu.responses.ChargeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ChargeService {

	private IuguConfiguration iugu;
	private final String CREATE_URL = IuguConfiguration.url("/charge");

	private final Logger LOG = LoggerFactory.getLogger(getClass());

  public ChargeService(IuguConfiguration iuguConfiguration) {
		this.iugu = iuguConfiguration;
	}

	public ChargeResponse create(Charge charge) throws IuguException {
		LOG.info("IUGU-JAVA: Create Payment Token {}", charge);
		Response response = this.iugu.getNewClient().target(CREATE_URL).request().post(Entity.entity(charge, MediaType.APPLICATION_JSON));

		int ResponseStatus = response.getStatus();
		String ResponseText = null;

		if (ResponseStatus == 200)
			return response.readEntity(ChargeResponse.class);

		// Error Happened
		if (response.hasEntity())
			ResponseText = response.readEntity(String.class);

		response.close();

		throw new IuguException("Error creating charge!", ResponseStatus, ResponseText);
	}
}
