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
package org.gatein.wci;

import java.io.IOException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Encapsulate dispatch functionnality into a single class so it is easy to
 * pass it as an argment to a framework that needs a dispatcher to just a
 * servlet context and does not care about the underlying spi or request/response.
 */
public class ServletContextDispatcher
{

   /** . */
   private final HttpServletRequest request;

   /** . */
   private final HttpServletResponse response;

   /** . */
   private final ServletContainer servletContainer;

   public ServletContextDispatcher(HttpServletRequest request, HttpServletResponse response, ServletContainer servletContainer)
   {
      if (request == null)
      {
         throw new IllegalArgumentException("No null request allowed");
      }
      if (response == null)
      {
         throw new IllegalArgumentException("No null response allowed");
      }
      if (servletContainer == null)
      {
         throw new IllegalArgumentException("No null servlet container allowed");
      }

      //
      this.request = request;
      this.response = response;
      this.servletContainer = servletContainer;
   }

   public HttpServletRequest getRequest()
   {
      return request;
   }

   public HttpServletResponse getResponse()
   {
      return response;
   }

   public Object include(
      ServletContext targetServletContext,
      RequestDispatchCallback callback,
      Object handback) throws ServletException, IOException
   {
      return servletContainer.include(targetServletContext, request, response, callback, handback);
   }
}
