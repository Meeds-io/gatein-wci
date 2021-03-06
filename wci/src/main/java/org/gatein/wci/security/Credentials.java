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

package org.gatein.wci.security;

import java.io.Serializable;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class Credentials implements Serializable
{
   /** . */
   private final String username;

   /** . */
   private final String password;

   /** . */
   public static final String CREDENTIALS = "credentials";

   /**
    * Construct a new instance.
    *
    * @param username the username value
    * @param password the password value
    * @throws NullPointerException if any argument is null
    */
   public Credentials(String username, String password) throws NullPointerException
   {
      if (username == null)
      {
         throw new IllegalArgumentException("Username is null");
      }
      if (password == null)
      {
         throw new IllegalArgumentException("Password is null");
      }
      this.username = username;
      this.password = password;
   }

   /**
    * Returns the username.
    *
    * @return the username
    */
   public String getUsername()
   {
      return username;
   }

   /**
    * Returns the password.
    *
    * @return the password
    */
   public String getPassword()
   {
      return password;
   }
}
