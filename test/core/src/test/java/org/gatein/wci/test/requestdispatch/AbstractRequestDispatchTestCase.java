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
package org.gatein.wci.test.requestdispatch;

import java.net.HttpURLConnection;
import java.net.URL;

import org.gatein.wci.test.AbstractWCITestCase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import junit.framework.Assert;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public abstract class AbstractRequestDispatchTestCase extends AbstractWCITestCase
{

   @Deployment(name = "rdapp", order = 2)
   public static WebArchive deployment()
   {
      WebArchive war = ShrinkWrap.create(WebArchive.class, "rdapp.war");
      return war;
   }

   @Test
   @RunAsClient
   public void testFoo(@ArquillianResource @OperateOnDeployment("rdwci") URL requestDispatchURL) throws Exception
   {
      HttpURLConnection conn = (HttpURLConnection)requestDispatchURL.openConnection();
      conn.connect();
      Assert.assertEquals(200, conn.getResponseCode());
   }
}
