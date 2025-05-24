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
 * Web application life cycle event.
 */
public class WebAppLifeCycleEvent extends WebAppEvent
{

   /** Application is removed. */
   public final static int REMOVED = 0;

   /** Application is added. */
   public final static int ADDED = 1;

   /** The type of the life cycle which can be <code>ADDED</code> or <code>REMOVED</code> . */
   private final int type;

   /**
    * Creates a new web application life cycle event.
    *
    * @param webApp the web application
    * @param type the life cycle type
    * @throws IllegalArgumentException if the web application is null or the type value is not valid
    */
   public WebAppLifeCycleEvent(WebApp webApp, int type)
      throws IllegalArgumentException
   {
      super(webApp);

      //
      if (type < REMOVED || type > ADDED)
      {
         throw new IllegalArgumentException("Type " + type + " not accepted");
      }

      //
      this.type = type;
   }

   /**
    * Returns the life cycle type.
    *
    * @return the life cycle type
    */
   public int getType()
   {
      return type;
   }
}
