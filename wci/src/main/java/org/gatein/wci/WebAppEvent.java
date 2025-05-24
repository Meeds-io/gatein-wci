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

/**
 * Base class for web application events.
 */
public abstract class WebAppEvent
{

   /** The web application. */
   private final WebApp webApp;

   /**
    * Construct a new web application event.
    *
    * @param webApp the web application
    * @throws IllegalArgumentException if the provided web application is null
    */
   public WebAppEvent(WebApp webApp) throws IllegalArgumentException
   {
      if (webApp == null)
      {
         throw new IllegalArgumentException("No null web application accepted");
      }
      this.webApp = webApp;
   }

   /**
    * Returns the web application.
    *
    * @return the web application
    */
   public WebApp getWebApp()
   {
      return webApp;
   }
}
