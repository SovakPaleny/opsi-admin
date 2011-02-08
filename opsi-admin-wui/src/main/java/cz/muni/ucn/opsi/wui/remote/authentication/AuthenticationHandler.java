/**
 *
 */
package cz.muni.ucn.opsi.wui.remote.authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.InetOrgPerson;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author Jan Dosoudil
 *
 */
public class AuthenticationHandler implements AuthenticationFailureHandler,
		AuthenticationSuccessHandler, LogoutSuccessHandler, InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(AuthenticationHandler.class);

	private JsonFactory jsonFactory;
	private ObjectMapper objectMapper;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		jsonFactory = new JsonFactory();
		jsonFactory.setCodec(objectMapper);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		AuthenticationStatus as = new AuthenticationStatus();
		as.setStatus(AuthenticationStatus.STATUS_LOGGED_IN);
		as.setMessage(null);

		Object principal = authentication.getPrincipal();
		if (principal instanceof InetOrgPerson) {
			InetOrgPerson inetOrgPerson = (InetOrgPerson) principal;
			as.setDisplayName(inetOrgPerson.getDisplayName());
		} else {
			as.setDisplayName(principal.toString());
		}
		as.setUsername(authentication.getName());

		Collection<GrantedAuthority> authorities = authentication.getAuthorities();
		List<String> auths = new ArrayList<String>();
		for (GrantedAuthority grantedAuthority : authorities) {
			auths.add(grantedAuthority.getAuthority());
		}
		as.setRoles(auths.toArray(new String[auths.size()]));

		writeResponse(response, as);

	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {

		if (logger.isDebugEnabled()) {
			logger.debug("authentication failure", exception);
		}

		AuthenticationStatus as = new AuthenticationStatus();
		as.setStatus(AuthenticationStatus.STATUS_LOGIN_FAILED);
		as.setMessage(exception.getMessage());

		writeResponse(response, as);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.logout.LogoutSuccessHandler#onLogoutSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		AuthenticationStatus as = new AuthenticationStatus();
		as.setStatus(AuthenticationStatus.STATUS_NOT_LOGGED_IN);

		writeResponse(response, as);

	}

	/**
	 * @param response
	 * @param authenticationStatus
	 * @param status
	 * @param message
	 * @throws IOException
	 */
	private void writeResponse(HttpServletResponse response,
			AuthenticationStatus authenticationStatus) throws IOException {

		PrintWriter writer = response.getWriter();

		JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
		generator.writeObject(authenticationStatus);

		writer.flush();

	}

	/**
	 * @param objectMapper the objectMapper to set
	 */
	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
}
