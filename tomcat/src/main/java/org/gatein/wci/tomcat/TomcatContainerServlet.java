/**
 * This file is part of the Meeds project (https://meeds.io/).
 *
 * Copyright (C) 2020 - 2025 Meeds Association contact@meeds.io
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.gatein.wci.tomcat;

import org.apache.catalina.Container;
import org.apache.catalina.ContainerServlet;
import org.apache.catalina.Engine;
import org.apache.catalina.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public class TomcatContainerServlet extends HttpServlet implements ContainerServlet
{
  private static final Logger           log                      = LoggerFactory.getLogger(TomcatContainerServlet.class);

   /** Servlet context init parameter name that can be used to turn off cross-context logout */
   private static final String CROSS_CONTEXT_LOGOUT_KEY = "org.gatein.wci.cross_context_logout";

   /** . */
   private Wrapper wrapper;

   /** . */
   private TomcatServletContainerContext containerContext;

   /** . */
   private boolean started;

   public Wrapper getWrapper()
   {
      return wrapper;
   }

   public void setWrapper(Wrapper wrapper)
   {
      this.wrapper = wrapper;

      //
      if (wrapper != null)
      {
         attemptStart();
      }
      else
      {
         attemptStop();
      }
   }

   public void init() throws ServletException
   {
      started = true;

      //
      attemptStart();
   }

   public void destroy()
   {
      started = false;

      //
      attemptStop();
   }

   private void attemptStart()
   {
      if (started && wrapper != null)
      {
         start();
      }
   }

   private void attemptStop()
   {
      if (!started || wrapper == null)
      {
         stop();
      }
   }

   private void start()
   {
      Container container = wrapper;
      while (container.getParent() != null)
      {
         container = container.getParent();
         if (container instanceof Engine)
         {
            Engine engine = (Engine)container;
            containerContext = new TomcatServletContainerContext(engine);
            containerContext.setCrossContextLogout(getCrossContextLogoutConfig());
            containerContext.start();
            break;
         }
      }
   }

   private void stop()
   {
      if (containerContext != null)
      {
         containerContext.stop();

         //
         containerContext = null;
      }
   }

   private boolean getCrossContextLogoutConfig() {
      String val = getServletContext().getInitParameter(CROSS_CONTEXT_LOGOUT_KEY);
      if (val == null || Boolean.valueOf(val))
         return true;

      if (!"false".equalsIgnoreCase(val))
         log.warn("Context init param " + CROSS_CONTEXT_LOGOUT_KEY + " value is invalid: " + val + " - falling back to: false");

      log.info("Cross-context session invalidation on logout disabled");
      return false;
   }
}
