<%-- 
    Document   : logged
    Created on : 6-gen-2016, 15.27.54
    Author     : leandro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Car sharing</title>
    </head>
    
    <%--Add jQuery library (remote)--%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

    
    
    <%--JQUERY readDB async call --%>
    <script>
    $(document).ready(function() {
                    $.ajax({
                            url : 'FE_Servlet_Logged',
                            type: 'POST',
                            data : {
                                table1 : $('#table1').val()
                            },
                            success : function(responseText) {
                                $('#table1').append(responseText);
                            }                            
                    });
    });
    </script>
    
    
    
    
    <%--JQUERY add offer asynch call --%>
    <script>
    $(document).ready(function() {
            $('#button1').click(function() {
                    $.ajax({
                            url : 'FE_Servlet_Logged',
                            type: 'POST',
                            data : {
                                CarID_OfferForm : $('#CarID_OfferForm').val()
                            },
                            success : function(responseText) {
                                $('#status').empty();
                                $('#status').append('<label>'+responseText+'</label>');
                            }                            
                    });
            });
    });
    </script>
 
        <%--JQUERY add offer asynch call --%>
    <script>
    $(document).ready(function() {
            $('#button2').click(function() {
                    $.ajax({
                            url : 'FE_Servlet_Logged',
                            type: 'POST',
                            data : {
                                offerID : $('#offerID').val(),
                                passengerID : $('#passengerID').val()
                            },
                            success : function(responseText) {
                                $('#status').empty();
                                $('#status').append('<label>'+responseText+'</label>');
                            }                            
                    });
            });
    });
    </script>
    
    
    
    <% 
        if(session==null){
            request.getRequestDispatcher("index.html").forward(request,response);
        }
    %>
    
    <center> <h1>SMART CAR SHARING</h1> </center>
    
    <h2>Welcome : <%= session.getAttribute("uname") %></h2>
    
    <body>        
        <center>
            <table border="1" width="1000" id="table1">
                <tr>
                    <th>Offer id    </th>
                    <th>Car id      </th>
                    <th>Passenger 1 </th>
                    <th>Passenger 2 </th>
                    <th>Passenger 3 </th>
                    <th>Passenger 4 </th>
                </tr>
            </table>
        </center>    

        <br>
        <br>
        
	<form id="offerForm">
            <h2>Add Offer:</h2>
            Insert your car id: <input type="text" id="CarID_OfferForm"/>
            <input type="button" value="Add Offer" id="button1"/>
	</form>
        
        <form id="reservationForm">
            <h2>Reservation seat: </h2>
            
            <table>
                <tbody>
                    <tr>
                        <td>Insert Offer ID: </td>
                        <td><input type="text" id="offerID"/></td>
                    </tr>
                    <tr>
                        <td>Insert Passenger ID: </td>
                        <td><input type="text" id="passengerID"/></td>
                    </tr>
                </tbody>
            </table>
            <input type="button" value="Add Reservation" id="button2"/>          
	</form>
      
        <br>
       
        <div>
            <h2>Status bar</h2>
            <p id="status"></p>
        </div>
    </body>
</html>
