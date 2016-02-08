package com.servlet;

import com.FaultDetector;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author leandro
 */
public class FE_FaultDetector extends HttpServlet {
        
    private FaultDetector fd;

    private int port;
    
    private boolean run;

    public FE_FaultDetector() {
        port=9999;
        run=false;
        fd=new FaultDetector();
    }
    
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        

        String status=request.getParameter("status");
        String start=request.getParameter("start");
        String stop=request.getParameter("stop");
        
        System.out.println("STATUS:  "+status);
       
        String msg="";
        
        
        if(start!=null){
            if(!run)
                fd.start(port);
            run=true;
            response.sendRedirect( "index.jsp?status=RUN" );
        }
        else if(stop!=null){
            if(run)
                fd.stop();
            run=false;
            response.sendRedirect( "index.jsp?status=STOP" );
        }
        else if(status!=null && status.equals("GETSTATUS")){
           msg="<table class='table table-striped table-hover table-bordered'><thead><tr><th>ISTANZA</th><th>STATO</th></tr></thead><tbody> "+ parseFD(fd.getProcessStatusMap().toString())+"</tbody></table>";
            try (PrintWriter out = response.getWriter()) {
                out.println(msg);
            }
        }
        else if(status!=null && status.equals("INIT")){
          
            try (PrintWriter out = response.getWriter()) {
                if(run) 
                    out.println("RUN");
                else 
                    out.println("STOP");
            }
        } 
        
    }
	
	private String parseFD(String rawData)
	{
		String result = "";
		int i;
		for (String row: rawData.split(",")) {
			i = 0;
			row = row.replace("{", "");
			row = row.replace("}", "");
			result +="</tr>";
			for (String cell: row.split("=")) {
				if(i==0)
				{
					result +="<td>" + cell + "</td>";
				}
				else
				{
					result +="<td>" + (cell.contains("ALIVE") ? "<img src='image/green.png' >" : "<img src='image/red.png' >") + "</td>";
                                        
				}
				i++;
			}
			result +="<tr>";
		}
		return result;
	}

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

