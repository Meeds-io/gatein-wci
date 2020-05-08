/*
 * This file is part of the Meeds project (https://meeds.io/).
 * Copyright (C) 2020 Meeds Association
 * contact@meeds.io
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.gatein.wci.test.servletlistener;

import org.gatein.wci.RequestDispatchCallback;
import org.gatein.wci.ServletContainer;
import org.gatein.wci.ServletContainerFactory;
import org.gatein.wci.WebApp;
import org.gatein.wci.test.WebAppRegistry;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="http://community.jboss.org/people/kenfinni">Ken Finnigan</a>
 */
public class ListenerServlet extends HttpServlet
{
   private ServletContainer container;

   private WebAppRegistry registry;

   @Override
   public void init() throws ServletException
   {
      container = ServletContainerFactory.getServletContainer();
      container.addWebAppListener(registry = new WebAppRegistry());
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      WebApp app = registry.getWebApp("/servletlistenerapp");
      if (app == null)
      {
         throw new ServletException("Could not find the app to dispatch to");
      }

      //
      container.include(app.getServletContext(), req, resp, new RequestDispatchCallback()
      {
         @Override
         public Object doCallback(ServletContext dsc, HttpServletRequest req, HttpServletResponse resp, Object handback) throws ServletException, IOException
         {
            return null;
         }
      }, null);

      //
      resp.setStatus(200);
   }

   @Override
   public void destroy()
   {
      if (registry != null && container != null)
      {
         container.removeWebAppListener(registry);
      }
   }
}
