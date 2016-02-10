package com.servlet;

import com.Offer;
import com.Tag;
import com.bean.stateless.COOLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class FE_Servlet_Logged extends HttpServlet {

    @EJB(mappedName = "COOBean")
    private COOLocal COO;
    
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
        response.setContentType("text/html;charset=UTF-8");
        
         HttpSession session=request.getSession(false);
        
        if(session!=null){
            
            String typeRequest=request.getParameter("typeRequest");
                        
            Tag tag=null;

            int clientID=COO.createSession(session.getId());
            
            if(typeRequest!=null && typeRequest.equals("GET_DB")){
                
                tag=COO.getDB(clientID);

            }
            if(typeRequest!=null && typeRequest.equals("ADD_OFFER")){
                
                String OfferCarID=request.getParameter("CarID_OfferForm");
                
                tag=COO.addOffer(clientID,OfferCarID);
            }
            if(typeRequest!=null && typeRequest.equals("ADD_RESERVATION")){
                
                String offerID=request.getParameter("offerID");
                
                String passengerID=request.getParameter("passengerID");
                
                tag=COO.addReservation(clientID,offerID,passengerID);
            }
            
            if(tag!=null)
                checkAvailableData(tag,response);
        }
        else{
            RequestDispatcher rd=request.getRequestDispatcher("index.jsp?login=lostSession");
            rd.forward(request,response);
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

    
    private void checkAvailableData(Tag tag,HttpServletResponse response) throws IOException{
        
        String risp=COO.checkStatusData(tag);
        
        while(risp.equals("PENDING")){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(FE_Servlet_Logged.class.getName()).log(Level.SEVERE, null, ex);
            }
            risp=COO.checkStatusData(tag);
        }
        Object dataResponse=COO.getData(tag);
        try (PrintWriter out = response.getWriter()) {
            if(risp.equals("ABORT")){
                out.println("<p id='abortCase'>"+ dataResponse +"</p> ");
            }
            else{
                Object o=dataResponse;
                if(o instanceof String){
                    out.println("<p>"+ dataResponse +"</p> ");
                }
                else{
                    LinkedList<Offer> list=(LinkedList<Offer>)dataResponse;
                    for (Offer offer : list) {
                        out.println("<tr>");
                        out.println("<td>"+offer.getOfferID()+"</td>");
                        out.println("<td>"+offer.getCarID()+"</td>");
                        out.println("<td>"+offer.getPassenger1()+"</td>");
                        out.println("<td>"+offer.getPassenger2()+"</td>");
                        out.println("<td>"+offer.getPassenger3()+"</td>");
                        out.println("<td>"+offer.getPassenger4()+"</td>");
                        out.println("</tr>");
                    }
                }
            }
        }
    }
}
