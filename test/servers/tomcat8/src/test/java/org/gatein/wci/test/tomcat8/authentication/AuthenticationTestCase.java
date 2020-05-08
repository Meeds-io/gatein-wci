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
package org.gatein.wci.test.tomcat8.authentication;

import org.gatein.wci.test.authentication.AbstractAuthenticationTestCase;
import org.gatein.wci.test.authentication.AuthenticationServlet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class AuthenticationTestCase extends AbstractAuthenticationTestCase
{

   @Deployment
   public static WebArchive wciDeployment()
   {
      WebArchive war = ShrinkWrap.create(WebArchive.class);
      war.setWebXML("org/gatein/wci/test/tomcat8/authentication/web.xml");
      war.addAsManifestResource("org/gatein/wci/test/tomcat8/authentication/context.xml", "context.xml");
      war.addClass(AuthenticationServlet.class);
      war.addClass(AbstractAuthenticationTestCase.class);

      //
      return war;
   }
}
