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
package org.gatein.wci.test.jetty8.requestdispatch;

import org.gatein.wci.test.requestdispatch.AbstractCallback;
import org.gatein.wci.test.requestdispatch.AbstractRequestDispatchTestCase;
import org.gatein.wci.test.requestdispatch.ExceptionCallback;
import org.gatein.wci.test.requestdispatch.NormalCallback;
import org.gatein.wci.test.requestdispatch.RequestDispatchServlet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author <a href="hoang281283@gmail.com">Minh Hoang TO</a>
 * @date 10/2/12
 */
public class RequestDispatchTestCase extends AbstractRequestDispatchTestCase
{
   @Deployment(name = "rdwci", order = 1)
   public static WebArchive wciDeployment()
   {
      WebArchive war = ShrinkWrap.create(WebArchive.class, "rdwci.war");
      war.setWebXML("org/gatein/wci/test/jetty8/requestdispatch/web.xml");
      war.addAsWebResource("org/gatein/wci/test/jetty8/requestdispatch/jetty-web.xml", "WEB-INF/jetty-web.xml");
      war.addClasses(RequestDispatchServlet.class, AbstractCallback.class, NormalCallback.class, ExceptionCallback.class);

      return war;
   }
}
