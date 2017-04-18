/*
 * Copyright (C) 2017 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.gatein.wci.wildfly;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.gatein.wci.command.CommandServlet;
import org.gatein.wci.spi.WebAppContext;

import io.undertow.server.session.Session;
import io.undertow.server.session.SessionManager;
import io.undertow.servlet.api.Deployment;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.DeploymentManager.State;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.servlet.api.ServletInfo;
import io.undertow.servlet.core.ApplicationListeners;
import io.undertow.servlet.handlers.ServletHandler;
import io.undertow.servlet.spec.HttpSessionImpl;
import io.undertow.servlet.spec.ServletContextImpl;
import io.undertow.servlet.spec.ServletRegistrationImpl;

public class Wildfly10WebAppContext implements WebAppContext {
  public static final String  GATEIN_SERVLET_NAME = "CommandServlet";

  public static final String  GATEIN_SERVLET_PATH = "/wildfly10gateinservlet";

  private static final Logger log                 = LoggerFactory.getLogger(Wildfly10WebAppContext.class);

  private Deployment          deployment;

  private SessionManager      undertowSessionManager;

  private ServletContextImpl  servletContext;

  private ClassLoader         classLoader;

  private String              contextPath;

  public Wildfly10WebAppContext(Deployment deployment) {
    if (deployment == null) {
      throw new IllegalArgumentException("'deployment' parameter is mandatory in WCI WebApp constructor");
    }
    this.deployment = deployment;
    this.undertowSessionManager = deployment.getSessionManager();
    if (undertowSessionManager == null) {
      throw new IllegalStateException("'undertowSessionManager' is mandatory in WCI WebApp constructor but couldn't be found in Deployment object");
    }
    this.servletContext = deployment.getServletContext();
    if (servletContext == null) {
      throw new IllegalStateException("'servletContext' is mandatory in WCI WebApp constructor but couldn't be found in Deployment object");
    }
    classLoader = servletContext.getClassLoader();
    if (classLoader == null) {
      throw new IllegalStateException("'classLoader' is mandatory in WCI WebApp constructor but couldn't be found in Deployment object");
    }
    contextPath = servletContext.getContextPath();
    if (contextPath == null) {
      throw new IllegalStateException("'contextPath' is mandatory in WCI WebApp constructor but couldn't be found in Deployment object");
    }
  }

  @Override
  public void start() throws Exception {
    DeploymentInfo deploymentInfo = deployment.getDeploymentInfo();
    ServletInfo servlet = new ServletInfo(GATEIN_SERVLET_NAME, CommandServlet.class, deploymentInfo.getClassIntrospecter().createInstanceFactory(CommandServlet.class));
    deploymentInfo.addServlet(servlet);
    ServletHandler handler = deployment.getServlets().addServlet(servlet);
    ServletRegistrationImpl servletRegistrationImpl = new ServletRegistrationImpl(servlet, handler.getManagedServlet(), deployment);
    servletRegistrationImpl.addMapping(GATEIN_SERVLET_PATH);
  }

  @Override
  public void stop() {
    if (deployment.getDeploymentState() != State.STARTED) {
      log.debug("Attempt to stop deployment '" + deployment.getDeploymentInfo().getDeploymentName() + "' already stopped.");
      return;
    }
    try {
      deployment.getServletPaths().getServletHandlerByName(GATEIN_SERVLET_NAME).getManagedServlet().getServlet().release();
    } catch (Exception e) {
      log.error("Error while removing Servlet " + GATEIN_SERVLET_NAME, e);
    }
    ServletContainer servletContainer = deployment.getServletContainer();
    DeploymentInfo deploymentInfo = deployment.getDeploymentInfo();

    String deploymentName = deploymentInfo.getDeploymentName();
    DeploymentManager deploymentManager = servletContainer.getDeployment(deploymentName);
    deploymentManager.undeploy();
  }

  @Override
  public ServletContext getServletContext() {
    return servletContext;
  }

  @Override
  public ClassLoader getClassLoader() {
    return classLoader;
  }

  @Override
  public String getContextPath() {
    return contextPath;
  }

  @Override
  public boolean importFile(String parentDirRelativePath, String name, InputStream source, boolean overwrite) throws IOException {
    return false;
  }

  @Override
  public HttpSession getHttpSession(String sessionId) {
    Session session = undertowSessionManager.getSession(sessionId);
    if (session == null) {
      log.debug("Couldn't find session with id " + sessionId);
      return null;
    }
    return HttpSessionImpl.forSession(session, getServletContext(), false);
  }

  @Override
  public void fireRequestInitialized(ServletRequest request) {
    ApplicationListeners applicationListeners = deployment.getApplicationListeners();
    if (null != applicationListeners) {
      applicationListeners.requestInitialized(request);
    }
  }

  @Override
  public void fireRequestDestroyed(ServletRequest request) {
    ApplicationListeners applicationListeners = deployment.getApplicationListeners();
    if (null != applicationListeners) {
      applicationListeners.requestDestroyed(request);
    }
  }

}
