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
package org.gatein.wci.test.tomcat.deployment;

import junit.framework.Assert;

import org.gatein.wci.ServletContainer;
import org.gatein.wci.ServletContainerFactory;
import org.gatein.wci.test.AbstractWCITestCase;
import org.gatein.wci.test.WebAppRegistry;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

public class RedeployServiceTestCase extends AbstractWCITestCase
{  
   @ArquillianResource
   Deployer deployer;
   
   @Test
   @RunAsClient
   @InSequence(0)
   public void deployWCIServiceArchive()
   {
      deployer.deploy("deploymentwci");
   }
   
   @Test
   @InSequence(1)
   @OperateOnDeployment("deploymentwci")
   public void testFirstDeploy()
   {
      ServletContainer _container = ServletContainerFactory.getServletContainer();
      Assert.assertNotNull(_container);

      //
      WebAppRegistry _registry = new WebAppRegistry();
      _container.addWebAppListener(_registry);
      
      //check that the keys contain the /deploymentwci
      Assert.assertTrue(_registry.getKeys().contains("/deploymentwci"));
   }
   
   @Test
   @RunAsClient
   @InSequence(2)
   public void deployRedeployWCIServiceArchive()
   {
      deployer.undeploy("deploymentwci");
      deployer.deploy("deploymentwci");
   }
   
   @Test
   @InSequence(3)
   @OperateOnDeployment("deploymentwci")
   public void testReDeploy()
   {
      ServletContainer _container = ServletContainerFactory.getServletContainer();
      Assert.assertNotNull(_container);

      //
      WebAppRegistry _registry = new WebAppRegistry();
      _container.addWebAppListener(_registry);
      
      //check that the keys contain the /deploymentwci
      Assert.assertTrue(_registry.getKeys().contains("/deploymentwci"));
   }
   
   @Test
   @RunAsClient
   @InSequence(4)
   public void undeployArchive()
   {
      deployer.undeploy("deploymentwci");
   }
   
   @Deployment (name = "deploymentwci", managed = false, testable=true)
   public static WebArchive wciDeployment()
   {
      WebArchive war = ShrinkWrap.create(WebArchive.class, "deploymentwci.war");
      war.setWebXML("org/gatein/wci/test/tomcat/deployment/web.xml");
      war.addAsManifestResource("org/gatein/wci/test/tomcat/deployment/context.xml", "context.xml");
      return war;
   }
}

