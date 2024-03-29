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
package org.gatein.wci.test.authentication;

import java.io.IOException;
import java.util.LinkedList;

import org.gatein.wci.ServletContainer;
import org.gatein.wci.ServletContainerFactory;
import org.gatein.wci.authentication.AuthenticationEvent;
import org.gatein.wci.authentication.AuthenticationException;
import org.gatein.wci.authentication.AuthenticationListener;
import org.gatein.wci.security.Credentials;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class AuthenticationServlet extends HttpServlet
{

   /** . */
   static int status = 0;

   /** . */
   static String remoteUser;

   /** . */
   ServletContainer sc;

   /** . */
   static LinkedList<AuthenticationEvent> authEvents = new LinkedList<AuthenticationEvent>();

   @Override
   public void init() throws ServletException
   {
      sc = ServletContainerFactory.getServletContainer();
      sc.addAuthenticationListener(new AuthenticationListener()
      {
         @Override
         public void onEvent(AuthenticationEvent event)
         {
            authEvents.addLast(event);
         }
      });
   }

   @Override
   public void destroy()
   {
      sc = null;
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      switch (status)
      {
         case 0:
            remoteUser = req.getRemoteUser();
            resp.setStatus(200);
            break;
         case 1:
            try
            {
               sc.login(req, resp, new Credentials("foo", "foo"));
               throw new ServletException("Was expecting an authentication exception");
            }
            catch (AuthenticationException ignore)
            {
            }
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            break;
         case 2 :
            sc.login(req, resp, new Credentials("foo", "bar"));
            try
            {
               sc.login(req, resp, new Credentials("foo", "bar"));
               throw new ServletException("Was expecting authenticated login to throw IllegalStateException");
            }
            catch (IllegalStateException ignore)
            {
            }
            remoteUser = req.getRemoteUser();
            resp.setStatus(200);
            resp.setContentType("text/plain");
            resp.getWriter().write(getURL(req, resp));
            break;
         case 3:
            req.getSession();
            remoteUser = req.getRemoteUser();
            resp.setStatus(200);
            resp.setContentType("text/plain");
            resp.getWriter().write(getURL(req, resp));
            break;
         case 4:
            sc.logout(req, resp);
            try
            {
               sc.logout(req, resp);
               throw new ServletException("Was expecting unauthenticated logout to throw IllegalStateException");
            }
            catch (IllegalStateException ignore)
            {
            }
            remoteUser = req.getRemoteUser();
            resp.setStatus(200);
            break;
         default:
            throw new ServletException("Unexpected status");
      }
   }

   private String getURL(HttpServletRequest req, HttpServletResponse resp)
   {
      return resp.encodeURL("http://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath());
   }
}
