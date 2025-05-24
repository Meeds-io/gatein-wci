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
package org.gatein.wci.command;

import java.io.IOException;

import org.gatein.wci.RequestDispatchCallback;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CommandDispatcher
{

   /** . */
   protected final String servletPath;

   public CommandDispatcher(String servletPath)
   {
     this.servletPath = servletPath;
   }

  public Object include(
      ServletContext targetServletContext,
      HttpServletRequest req,
      HttpServletResponse resp,
      RequestDispatchCallback callback,
      Object handback) throws ServletException, IOException
   {
      CallbackCommand cmd = new CallbackCommand(targetServletContext, callback, handback);

      //
      return CommandServlet.include(servletPath, req, resp, cmd, targetServletContext);
   }

   public static class CallbackCommand
   {

      /** . */
      private final ServletContext servletContext;

      /** . */
      private final RequestDispatchCallback invocation;

      /** . */
      private final Object handback;

      public CallbackCommand(ServletContext servletContext, RequestDispatchCallback invocation, Object handback)
      {
         this.servletContext = servletContext;
         this.invocation = invocation;
         this.handback = handback;
      }

      public Object execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
      {
         return invocation.doCallback(servletContext, req, resp, handback);
      }
   }
}
