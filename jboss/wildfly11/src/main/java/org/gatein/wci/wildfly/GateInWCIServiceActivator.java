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

import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceRegistryException;
import org.wildfly.extension.undertow.ServletContainerService;
import org.wildfly.extension.undertow.UndertowService;

public class GateInWCIServiceActivator implements ServiceActivator {
  @Override
  public void activate(ServiceActivatorContext context) throws ServiceRegistryException {
    final GateInWCIService service = new GateInWCIService();

    String servletContainerName = System.getProperty("exo.gatein.wci.servlet.container.name", "default");
    final ServiceBuilder<GateInWCIService> serviceBuilder =
                                                          context.getServiceTarget()
                                                                 .addService(GateInWCIService.NAME, service)
                                                                 .addDependency(UndertowService.SERVLET_CONTAINER.append(servletContainerName),
                                                                                ServletContainerService.class,
                                                                                service.getValue().getServletContainer())
                                                                 .setInitialMode(ServiceController.Mode.ACTIVE);
    serviceBuilder.install();
  }
}
