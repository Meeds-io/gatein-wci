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
package org.gatein.wci.test.jetty8;

import org.eclipse.jetty.security.DefaultIdentityService;
import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.UserIdentity;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="hoang281283@gmail.com">Minh Hoang TO</a>
 * @since 9/28/12
 */
public class BasicLoginService implements LoginService
{

   private final ConcurrentMap<String, UserIdentity> identities = new ConcurrentHashMap<String, UserIdentity>();

   private static final Map<String, String> validUsers = new HashMap<String, String>();

   static
   {
      validUsers.put("foo", "bar");
   }

   private final IdentityService identityService = new DefaultIdentityService();

   @Override
   public String getName()
   {
      return "basic";
   }

   @Override
   public UserIdentity login(final String username, Object credentials)
   {
      UserIdentity identity = identities.get(username);
      if (identity == null)
      {
         if (validUsers.containsKey(username) && validUsers.get(username).equals(credentials))
         {
            Principal userPrincipal = new Principal()
            {
               @Override
               public String getName()
               {
                  return username;
               }
            };
            identity = new BasicUserIdentity(userPrincipal, new String[]{"myrole"});
            identities.putIfAbsent(username, identity);
         }
      }
      return identity;
   }

   @Override
   public boolean validate(UserIdentity user)
   {
      if (identities.containsKey(user.getUserPrincipal().getName()))
      {
         return true;
      }
      return false;
   }

   @Override
   public IdentityService getIdentityService()
   {
      return identityService;
   }

   @Override
   public void setIdentityService(IdentityService service)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public void logout(UserIdentity user)
   {
      identities.remove(user.getUserPrincipal().getName());

   }
}
