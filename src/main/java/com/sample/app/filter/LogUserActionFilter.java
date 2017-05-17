package com.sample.app.filter;

import com.google.inject.Inject;
import org.glassfish.jersey.message.internal.ReaderWriter;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Provider
public class LogUserActionFilter implements ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LogUserActionFilter.class);

    @Context
    private HttpServletRequest request;

    @Resource
    private SessionFactory sessionFactory;

    @Inject
    public LogUserActionFilter(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String encodedCredentials = requestContext.getHeaderString("Authorization");
        if (encodedCredentials != null && encodedCredentials.startsWith("Basic")) {
            String encodedUsernamePassword = encodedCredentials.replace("Basic ", "").trim();
            byte[] decodedString = Base64.getDecoder().decode(encodedUsernamePassword);
            String decodedCredentials = new String(decodedString, StandardCharsets.UTF_8);

            if (decodedCredentials.contains(":")) {
                decodedCredentials = decodedCredentials.split(":")[0];
            }

            String entityString = this.readEntityStream(requestContext);

            String message = "Accessed by {} with data {}";

            logger.info(message, decodedCredentials, entityString);
        } else {
            logger.warn("No auth header found");
        }
    }

    /**
     * Read request context
     *
     * @param requestContext
     * @return
     */
    private String readEntityStream(ContainerRequestContext requestContext) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final InputStream inputStream = requestContext.getEntityStream();
        final StringBuilder builder = new StringBuilder();
        try {
            ReaderWriter.writeTo(inputStream, outStream);
            byte[] requestEntity = outStream.toByteArray();
            if (requestEntity.length == 0) {
                builder.append("");
            } else {
                builder.append(new String(requestEntity, "UTF-8"));
            }
            requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
        } catch (IOException ex) {
            logger.debug("----Exception occurred while reading entity stream :{}", ex.getMessage());
        }
        return builder.toString();
    }
}
