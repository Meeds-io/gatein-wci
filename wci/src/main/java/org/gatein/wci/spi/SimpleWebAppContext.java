/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.gatein.wci.spi;

import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class SimpleWebAppContext implements WebAppContext
{

   /** . */
   private final ServletContext servletContext;

   public SimpleWebAppContext(ServletContext servletContext)
   {
      this.servletContext = servletContext;
   }

   public void start() throws Exception
   {
   }

   public void stop()
   {
   }

   public ServletContext getServletContext()
   {
      return servletContext;
   }

   public ClassLoader getClassLoader()
   {
      return servletContext.getClassLoader();
   }

   public String getContextPath()
   {
      return servletContext.getContextPath();
   }

   public boolean importFile(String parentDirRelativePath, String name, InputStream source, boolean overwrite) throws IOException
   {
      return false;
   }

   public HttpSession getHttpSession(String sessId)
   {
      return null;
   }

   @Override
   public void fireRequestInitialized(ServletRequest servletRequest)
   {
   }

   @Override
   public void fireRequestDestroyed(ServletRequest servletRequest)
   {
   }
}