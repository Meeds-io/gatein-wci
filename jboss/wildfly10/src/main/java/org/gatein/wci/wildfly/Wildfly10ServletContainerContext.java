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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.gatein.wci.RequestDispatchCallback;
import org.gatein.wci.ServletContainerFactory;
import org.gatein.wci.authentication.AuthenticationException;
import org.gatein.wci.command.CommandDispatcher;
import org.gatein.wci.command.WildflyCommandDispatcher;
import org.gatein.wci.security.Credentials;
import org.gatein.wci.session.SessionTask;
import org.gatein.wci.session.SessionTaskVisitor;
import org.gatein.wci.spi.ServletContainerContext;

import io.undertow.servlet.api.Deployment;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.servlet.spec.ServletContextImpl;

public class Wildfly10ServletContainerContext
    implements ServletContainerContext, ServletContainerInitializer, ServletContextListener {
  private static final Logger                       log                  =
                                                        LoggerFactory.getLogger(Wildfly10ServletContainerContext.class);

  private final CommandDispatcher                   dispatcher           =
                                                               new WildflyCommandDispatcher(Wildfly10WebAppContext.GATEIN_SERVLET_PATH);

  /**
   * Perform cross-context session invalidation on logout, or not
   */
  protected boolean                                 crossContextLogout   = true;

  protected Registration                            registration;

  protected ServletContainer                        servletContainer;

  protected Set<String>                             monitoredDeployments = new HashSet<String>();

  protected static Wildfly10ServletContainerContext instance;

  public Wildfly10ServletContainerContext() {
  }

  public Wildfly10ServletContainerContext(ServletContainer servletContainer) {
    this.servletContainer = servletContainer;
    Wildfly10ServletContainerContext.instance = this;
  }

  @Override
  public Object include(ServletContext targetServletContext,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        RequestDispatchCallback callback,
                        Object handback) throws ServletException, IOException {
    return dispatcher.include(targetServletContext, request, response, callback, handback);
  }

  @Override
  public void setCallback(Registration registration) {
    this.registration = registration;
  }

  @Override
  public void unsetCallback(Registration registration) {
    this.registration = null;
  }

  @Override
  public void login(HttpServletRequest request,
                    HttpServletResponse response,
                    Credentials credentials) throws AuthenticationException, ServletException, IOException {
    request.getSession();
    try {
      request.login(credentials.getUsername(), credentials.getPassword());
    } catch (ServletException se) {
      throw new AuthenticationException(se);
    }
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    HttpSession sess = request.getSession(false);
    if (sess == null) {
      return;
    }

    request.logout();

    if (!crossContextLogout) {
      return;
    }

    ServletContainerFactory.getServletContainer().visit(new SessionTaskVisitor(sess.getId(), new SessionTask() {
      @Override
      public boolean executeTask(HttpSession session) {
        ClassLoader portalContainerCL = Thread.currentThread().getContextClassLoader();
        ClassLoader webAppCL = session.getServletContext().getClassLoader();

        Thread.currentThread().setContextClassLoader(webAppCL);
        try {
          session.invalidate();
        } finally {
          Thread.currentThread().setContextClassLoader(portalContainerCL);
        }

        return true;
      }
    }));
  }

  @Override
  public String getContainerInfo() {
    return "Wildfly/10";
  }

  public void setCrossContextLogout(boolean val) {
    crossContextLogout = val;
  }

  public void start() {
    ServletContainerFactory.registerContext(this);
    if(instance.servletContainer == null) {
      log.warn("Can't deploy Portlets and Extensions from external EARs because the servlet container wasn't found");
    }
    Collection<String> listDeployments = instance.servletContainer.listDeployments();
    for (String deploymentName : listDeployments) {
      try {
        DeploymentManager deployment = instance.servletContainer.getDeployment(deploymentName);
        ServletContextImpl servletContext = deployment.getDeployment().getServletContext();
        contextInitialized(new ServletContextEvent(servletContext));
      } catch (Exception e) {
        log.error("Can't integrate deployment '" + deploymentName + "' to eXo Platform. It will not be considered as eXo extension or portlet", e);
      }
    }
  }

  public void stop() {
    if (servletContainer != null) {
      log.debug("Destroying WCI Servlet container context");
      instance = null;
      Collection<String> listDeployments = servletContainer.listDeployments();
      for (String deploymentName : listDeployments) {
        if (monitoredDeployments.remove(deploymentName) && instance != null && instance.registration != null) {
          instance.registration.unregisterWebApp(deploymentName);
          DeploymentManager deploymentManager = servletContainer.getDeployment(deploymentName);
          deploymentManager.undeploy();
        }
      }
      //
      registration.cancel();
      registration = null;
      servletContainer = null;
    }
  }

  @Override
  public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
    if (instance != null && instance.registration != null) {
      ServletContextImpl servletContextImpl = (io.undertow.servlet.spec.ServletContextImpl) ctx;
      Deployment deployment = servletContextImpl.getDeployment();
      String deploymentName = deployment.getDeploymentInfo().getDeploymentName();
      if (!monitoredDeployments.contains(deploymentName)) {
        // To be catch contextDestroyed event
        deployment.getServletContext().addListener(this);

        if (!org.gatein.wci.ServletContainer.isDisabledNativeRegistration(servletContextImpl)) {
          monitoredDeployments.add(deploymentName);
          log.info("Integrate webapp '" + deploymentName + "' to eXo Platform context on context startup");
          instance.registration.registerWebApp(new Wildfly10WebAppContext(deployment));
        }
      }
    }
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    if (instance != null && instance.registration != null) {
      ServletContextImpl servletContextImpl = (io.undertow.servlet.spec.ServletContextImpl) sce.getServletContext();

      if (!org.gatein.wci.ServletContainer.isDisabledNativeRegistration(servletContextImpl)) {
        Deployment deployment = servletContextImpl.getDeployment();
        String deploymentName = deployment.getDeploymentInfo().getDeploymentName();
        if (!monitoredDeployments.contains(deploymentName)) {
          try {
            log.info("Integrate webapp '" + deploymentName + "' to eXo Platform context");
            monitoredDeployments.add(deploymentName);
            instance.registration.registerWebApp(new Wildfly10WebAppContext(deployment));
          } catch (Exception e) {
            log.warn("Cannot register Webapp " + deploymentName + " to eXo Platfom context", e);
          }
        }
      }
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContextImpl servletContextImpl = (io.undertow.servlet.spec.ServletContextImpl) sce.getServletContext();
    String contextPath = servletContextImpl.getContextPath();

    Deployment deployment = servletContextImpl.getDeployment();
    String deploymentName = deployment.getDeploymentInfo().getDeploymentName();
    if (monitoredDeployments.remove(deploymentName) && instance != null && instance.registration != null) {
      instance.registration.unregisterWebApp(contextPath);
    }
  }
}
