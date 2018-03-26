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

import java.util.concurrent.atomic.AtomicBoolean;

import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.wildfly.extension.undertow.ServletContainerService;

import io.undertow.servlet.api.ServletContainer;

public class Wildfly11Integration {
  private static final Logger              log                      = LoggerFactory.getLogger(Wildfly11Integration.class);

  /**
   * System property name that can be used to turn off cross-context logout
   */
  private static final String              CROSS_CONTEXT_LOGOUT_KEY = "org.gatein.wci.cross_context_logout";

  protected final AtomicBoolean            start                    = new AtomicBoolean(false);

  private Wildfly11ServletContainerContext containerContext         = null;

  private ServletContainerService          servletContainerService;

  public Wildfly11Integration(ServletContainerService servletContainerService) {
    this.servletContainerService = servletContainerService;
  }

  public void start() {
    if (start.compareAndSet(false, true)) {
      ServletContainer servletContainer = servletContainerService.getServletContainer();
      containerContext = new Wildfly11ServletContainerContext(servletContainer);
      containerContext.setCrossContextLogout(getCrossContextLogoutConfig());
      containerContext.start();
    }
  }

  public void stop() {
    if (start.compareAndSet(true, false)) {
      containerContext.stop();
    }
  }

  private boolean getCrossContextLogoutConfig() {
    String val = System.getProperty(CROSS_CONTEXT_LOGOUT_KEY);
    if (val == null || Boolean.valueOf(val)) {
      return true;
    }

    if (!"false".equalsIgnoreCase(val)) {
      log.warn("System property " + CROSS_CONTEXT_LOGOUT_KEY + " value is invalid: [" + val + "] - falling back to: [false]");
    }

    log.debug("Cross-context session invalidation on logout disabled");
    return false;
  }
}
