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
package org.gatein.wci.command;

import org.gatein.wci.RequestDispatchCallback;
import org.gatein.wci.ServletContainerFactory;
import org.gatein.wci.ServletContainerVisitor;
import org.gatein.wci.WebApp;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="http://community.jboss.org/people/kenfinni">Ken Finnigan</a>
 */
public class TomcatCommandDispatcher extends CommandDispatcher
{

   public TomcatCommandDispatcher(String servletPath)
   {
      super(servletPath);
   }

   @Override
   public Object include(
         ServletContext targetServletContext,
         HttpServletRequest req,
         HttpServletResponse resp,
         RequestDispatchCallback callback,
         Object handback) throws ServletException, IOException
   {

      final ServletContext target = targetServletContext;

      CallbackCommand cmd = new CallbackCommand(targetServletContext, callback, handback)
      {
         @Override
         public Object execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
         {
            final HttpServletRequest request = req;

            ServletContainerFactory.getServletContainer().visit(new ServletContainerVisitor()
            {
               @Override
               public void accept(WebApp webApp)
               {
                  if (webApp.getServletContext().equals(target))
                  {
                     webApp.fireRequestInitialized(request);
                  }
               }
            });

            ServletException servletException = null;
            IOException ioException = null;
            Object result = null;

            try
            {
               result = super.execute(req, resp);
            }
            catch (IOException e)
            {
               ioException = e;
            }
            catch (ServletException e)
            {
               servletException = e;
            }

            ServletContainerFactory.getServletContainer().visit(new ServletContainerVisitor()
            {
               @Override
               public void accept(WebApp webApp)
               {
                  if (webApp.getServletContext().equals(target))
                  {
                     webApp.fireRequestDestroyed(request);
                  }
               }
            });

            if (null != ioException)
            {
               throw ioException;
            }
            if (null != servletException)
            {
               throw servletException;
            }

            return result;
         }
      };

      return CommandServlet.include(servletPath, req, resp, cmd, targetServletContext);
   }
}
