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
package org.gatein.wci.test.tomcat.deployment;

import org.gatein.wci.test.deployment.AbstractDeploymentTestCase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class DeploymentTestCase extends AbstractDeploymentTestCase
{

   @Deployment
   public static WebArchive wciDeployment()
   {
      WebArchive war = ShrinkWrap.create(WebArchive.class, "deploymentwci.war");
      war.setWebXML("org/gatein/wci/test/tomcat/deployment/web.xml");
      war.addAsManifestResource("org/gatein/wci/test/tomcat/deployment/context.xml", "context.xml");

      //

      //
      return war;
   }

}
