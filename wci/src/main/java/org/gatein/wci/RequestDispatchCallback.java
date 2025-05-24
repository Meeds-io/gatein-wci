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
 * Callback contract.
 */
public interface RequestDispatchCallback
{
   /**
    * Performs the request dispatch logic.
    *
    * @param dispatchedServletContext the dispatched servlet context
    * @param dispatchedRequest the dispatched request
    * @param dispatchedResponse the dispatched response
    * @param handback the handback object provided to the dispatched @return any object
    * @return any object
    * @throws ServletException if an exception occurs that interferes with the normal operation
    * @throws IOException if an input or output exception occurs
    */
   Object doCallback(
      ServletContext dispatchedServletContext,
      HttpServletRequest dispatchedRequest,
      HttpServletResponse dispatchedResponse,
      Object handback) throws ServletException, IOException;
}
