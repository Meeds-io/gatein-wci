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
package org.gatein.wci.test.tomcat;

import java.security.Principal;
import java.util.Arrays;

import org.apache.catalina.Container;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class AuthenticationRealm extends RealmBase
{

   @Override
   public void setContainer(Container container) {
      super.setContainer(container);

      // Need to set container log
      this.containerLog = container.getLogger();
   }

   @Override
   protected String getPassword(String username) {
      return "foo".equals(username) ? "bar" : "" + Math.random();
   }

   @Override
   protected Principal getPrincipal(String username) {
      return new GenericPrincipal(username, username, Arrays.asList("myrole"));
   }
}
