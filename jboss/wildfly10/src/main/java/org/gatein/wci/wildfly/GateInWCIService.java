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

import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.extension.undertow.ServletContainerService;

public class GateInWCIService implements Service<GateInWCIService> {
  public static final ServiceName                      NAME             = ServiceName.of("org", "gatein", "wci");

  private Wildfly10Integration                         integration;

  private final InjectedValue<ServletContainerService> servletContainer = new InjectedValue<ServletContainerService>();

  public GateInWCIService() {}

  @Override
  public void start(StartContext context) throws StartException {
    integration = new Wildfly10Integration(servletContainer.getValue());
    integration.start();
  }

  @Override
  public void stop(StopContext context) {
    integration.stop();
  }

  @Override
  public GateInWCIService getValue() throws IllegalStateException, IllegalArgumentException {
    return this;
  }

  public InjectedValue<ServletContainerService> getServletContainer() {
    return servletContainer;
  }
}
