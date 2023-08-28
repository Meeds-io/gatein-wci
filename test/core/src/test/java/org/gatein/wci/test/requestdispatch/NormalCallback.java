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

import java.io.IOException;

import org.gatein.wci.ServletContextDispatcher;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NormalCallback extends AbstractCallback
{

   /** . */
   private final ServletContext expectedContext;

   /** . */
   private final ClassLoader expectedThreadContextClassLoader;

   /** . */
   private final Object expectedHandback;

   /** . */
   private final Object expectedReturnedValue;

   /** . */
   private Object handback;

   /** . */
   private ClassLoader threadContextClassLoader;

   /** . */
   private boolean invoked;

   public NormalCallback(ServletContext expectedContext, ClassLoader expectedThreadContextClassLoader)
   {
      this.expectedContext = expectedContext;
      this.expectedThreadContextClassLoader = expectedThreadContextClassLoader;
      this.expectedHandback = new Object();
      this.expectedReturnedValue = new Object();

      //
      this.invoked = false;
      this.handback = null;
   }

   public Object doCallback(ServletContext dispatchedServletContext, HttpServletRequest dispatchedRequest, HttpServletResponse dispatchedResponse, Object handback) throws ServletException, IOException
   {
      this.invoked = true;
      this.threadContextClassLoader = Thread.currentThread().getContextClassLoader();
      this.handback = handback;

      //
      return expectedReturnedValue;
   }

   @Override
   protected Throwable test(ServletContextDispatcher dispatcher)
   {
      try
      {
         Object returnedValue = dispatcher.include(expectedContext, this, expectedHandback);

         //
         if (!invoked)
         {
            return new Exception("The callback was not invoked");
         }
         if (expectedHandback != handback)
         {
            return new Exception("The provided handback is not the same than the expected handback");
         }
         if (expectedReturnedValue != returnedValue)
         {
            return new Exception("The returned value is not the same than the expected one");
         }
         if (expectedThreadContextClassLoader != threadContextClassLoader)
         {
            return new Exception("The thread context class loader is not the same than the expected one");
         }
      }
      catch (Exception e)
      {
         return e;
      }

      //
      return null;
   }
}
