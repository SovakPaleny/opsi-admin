/**
 *
 */
package cz.muni.ucn.opsi.wui.remote;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import cz.muni.ucn.opsi.api.RequestError;

/**
 * @author Jan Dosoudil
 *
 */
public class JsonExceptionResolver extends AbstractHandlerExceptionResolver {

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver#doResolveException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {


		String message = ex.getMessage();
		RequestError error = new RequestError(message);

		if (ex instanceof AccessDeniedException) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else if (ex instanceof ValidationException) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		MappingJacksonJsonView view = new MappingJacksonJsonView() {
			/* (non-Javadoc)
			 * @see org.springframework.web.servlet.view.json.MappingJacksonJsonView#filterModel(java.util.Map)
			 */
			@Override
			protected Object filterModel(Map<String, Object> model) {
				return model.get("error");
			}
		};
		ModelAndView modelAndView = new ModelAndView(view, "error", error);
		return modelAndView;
	}

}
