package com.servlet;

import com.FaultDetector;
import com.HeartBeatHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author leandro
 */
public class FE_FaultDetector extends HttpServlet {
    
    private ServerSocket server;
    
    private FaultDetector fd;
       
    private boolean run;

    public FE_FaultDetector() {
        run=false;
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
        String msg="";
        String start=request.getParameter("start");
        String stop=request.getParameter("stop");
        String getStatus=request.getParameter("getStatus");
        
        RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
        
        if(start!=null){
            if(!run)
                startFaultDetector();
            run=true;
            response.sendRedirect( "index.jsp?status=RUN" );
        }
        else if(stop!=null){
            if(run)
                stopFaultDetector();
            run=false;
            response.sendRedirect( "index.jsp?status=STOP" );
        }
        else if(getStatus!=null){
            msg="<h3> "+fd.getProcessStatusMap()+"</h3>";
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet FE_FaultDetector</title>");            
                out.println("</head>");
                out.println("<body>");
                out.println(msg);
                out.println("<a href='index.jsp?status=RUN'>Back</a>");
                out.println("</body>");
                out.println("</html>");
            }
        }
        

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

    private boolean startFaultDetector() {   
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Questi dati andrebbero caricati da file di configurazione...
                    int port=9999;
                    //------------------------------------------------------------
                    System.out.println("Fault Detector running...");
                    fd=new FaultDetector();
                    server=new ServerSocket(port);

                    while(true){                
                        Socket client=server.accept();
                        (new Thread(new HeartBeatHandler(client,fd))).start();
                    }
                    
                } 
                catch (IOException ex) {
                    Logger.getLogger(FE_FaultDetector.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }).start();
        return true;
    }
    
    private boolean stopFaultDetector(){
        try{
            server.close();
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

}
