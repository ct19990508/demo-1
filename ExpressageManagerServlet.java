package com.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bean.ExpressageBean;
import com.bean.MemberBean;
import com.bean.PermissionBean;
import com.bean.RoleBean;
import com.bean.UserToExpressageBean;
import com.util.Common;

public class ExpressageManagerServlet extends HttpServlet {


	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 String action = request.getParameter("action");
		 HttpSession session = request.getSession();
		 //Query out orders according to the user's ID query out orders
		 
		 
		 if(Common.isEmpty(action))
		 {
	         Integer  userid2=(Integer) session.getAttribute("userid");//sessionIt's not a strong type. It can only be stored as integer and needs to be unpacked.
             int userid=userid2;
		
         //Query out all logistic information of users according to their ID
         ExpressageBean ex=new ExpressageBean();
         ExpressageBean[] exlist= ex.getAllByUserId(userid);
         for(ExpressageBean extest:exlist)
         {
         	System.out.println(extest.toString());
         	
         }
         
        request.setAttribute("exlist", exlist);
        request.getRequestDispatcher("./member/prep/index.jsp").forward(request, response);		
		}
		else if(action.equals("update"))
		{
			//First jump to the update page to prepare for the update
			//Get current information based on ID
			int id=Integer.parseInt(request.getParameter("id"));
			ExpressageBean ex=new ExpressageBean();
		    ex=ex.getOneByUserId(id);
			
		    request.setAttribute("ex", ex);
			request.getRequestDispatcher("./member/prep/up.jsp?id="+id).forward(request, response);
					
		}
		else if(action.equals("Collect"))
		{

			//Query out the ID of the current user
		    //According to the user's ID to foreign key inquiry out all the express information
			//And return
			String member=(String)session.getAttribute("member"); 
        	if(member==null){

        		response.sendRedirect("./login.jsp");
        	
        	}

        	else
        	{
        MemberBean mb=new MemberBean();
        int userid=mb.getId(member);
        
		
        ExpressageBean ex=new ExpressageBean();
        ExpressageBean[] exlist= ex.getExpressageToUser(userid);
        request.setAttribute("exlist", exlist);
        
		request.getRequestDispatcher("./admin/hzp/type4.jsp").forward(request, response);

        	}
		    
		    
		}
		else if(action.equals("dq"))
		{
              //Get the ID of the current user
			  //Get the ID of the current express message
			  //Insert the two separately

			   //You have to put the user's ID in
			
		
               //Get the user's ID based on the user name
            	String member=(String)session.getAttribute("member"); 
            	if(member==null){

            		response.sendRedirect("./login.jsp");
            	
            	}
            	
            	else
            	{
            MemberBean mb=new MemberBean();
            int userid=mb.getId(member);
		    ExpressageBean ex=new ExpressageBean();

			int expressageid=Integer.parseInt(request.getParameter("id"));
			//insert
			
			
			
		    UserToExpressageBean ub=new UserToExpressageBean();
		    //Check whether the current constraint exists before insertion.
		    int flag=ub.isExist(userid,expressageid);
		    if(flag==1)
		    {
		    	    request.setAttribute("message","The operation failed and the substitution information already exists");
					request.getRequestDispatcher("./expressage").forward(request, response);
		    	    return ;
		    	
		    }
        //Check if someone has taken it       		    
		    flag=ex.check(expressageid);
		    if(flag==4)
		    {
		    	 request.setAttribute("message","Operations failed, replaced by someone");
					request.getRequestDispatcher("./expressage").forward(request, response);
		    	    return ;
		    	
		    }
		    
		    	
    	    ub.insert(userid, expressageid);
			//And set the current substitution
    	    
		    ex.deleteOne(expressageid);	
		    
		    request.setAttribute("message","This information was successfully retrieved. Please go to the User Center for more information.");
			request.getRequestDispatcher("./expressage").forward(request, response);
		    }
			
		    
		}
		else if(action.equals("update2"))
		{
			//First jump to the update page to prepare for the update
			//Get current information based on ID
			int id=Integer.parseInt(request.getParameter("id"));
			ExpressageBean ex=new ExpressageBean();
		    ex=ex.getOneByUserId(id);
			
		    request.setAttribute("ex", ex);
			request.getRequestDispatcher("./member/prep/up2.jsp?id="+id).forward(request, response);
					
		}
		else if(action.equals("all"))
		{
		
				//Find out all the information and manage it
				ExpressageBean ex=new ExpressageBean();
				ExpressageBean[] exlist=ex.getAllExpressage();
				
				
				
			    request.setAttribute("exlist", exlist);
				request.getRequestDispatcher("./admin/hzp/type2.jsp").forward(request, response);
			
		
			
					
		}
		else if(action.equals("all2"))
		{
			//Check authority
			String username2 = (String)session.getAttribute("user");
			RoleBean roleBean=new RoleBean();
			int flag2=roleBean.selectRoleByUsername(username2);
			//Check authority
			PermissionBean pb=new PermissionBean();
			int flag=pb.checkaffiche("expressage");
			if(flag==1||flag2==1)
			{
				//Find out all the information and manage it
				ExpressageBean ex=new ExpressageBean();
				ExpressageBean[] exlist=ex.getAllExpressage();
				
				
				
			    request.setAttribute("exlist", exlist);
				request.getRequestDispatcher("./admin/hzp/type3.jsp").forward(request, response);
			
			}
			else
			{
				
				request.setAttribute("message", "Only over-managed permissions can be used. Please confirm permissions.£¡");
				request.getRequestDispatcher("error2.jsp").forward(request, response);
				
			}
			
			
					
		}
		
		
		
		}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);

	}

}
