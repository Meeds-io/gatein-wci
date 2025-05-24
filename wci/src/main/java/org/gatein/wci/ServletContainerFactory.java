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

import org.gatein.wci.spi.ServletContainerContext;

/**
 * It's rather a provider rather than a real factory. But I prefer the factory name than the provider name.
 */
public final class ServletContainerFactory
{

   /** . */
   public static final ServletContainerFactory instance = new ServletContainerFactory();

   /** . */
   private final ServletContainer container = new ServletContainer();

   /**
    * Returns the singleton instance.
    *
    * @return the singleton instance
    */
   public static ServletContainer getServletContainer()
   {
      return instance.container;
   }

   /**
    * Registers a servlet container context. The registration is considered as successful if no existing context is
    * already registered.
    *
    * @param context the servlet container context to register
    * @throws IllegalArgumentException if the context is null
    */
   public static void registerContext(ServletContainerContext context) throws IllegalArgumentException
   {
      instance.container.register(context);
   }
}
