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
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

	<!-- Optional theme -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>


    
        <%--Add jQuery library (remote)--%>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

    
    
        <%--JQUERY readDB async call --%>
        <script>
            $(document).ready(function() {
                            $.ajax({
                                    url : 'FE_Servlet_Logged',
                                    type: 'POST',
                                    data : {
                                        typeRequest:'GET_DB'
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
                                        typeRequest:'ADD_OFFER',
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
 
        <%--JQUERY add Reservetion asynch call --%>
        <script>
            $(document).ready(function() {
                    $('#button2').click(function() {
                            $.ajax({
                                    url : 'FE_Servlet_Logged',
                                    type: 'POST',
                                    data : {
                                        typeRequest:'ADD_RESERVATION',
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
            
    </head>
	
	
    <body>
        
        <%HttpSession mySession=request.getSession(); %>
        
        <% String uname=(String)mySession.getAttribute("uname"); %>
        
        <% 
            if(mySession==null || uname==null){
                request.getRequestDispatcher("index.jsp?login=lostSession").forward(request,response);
            }
        %>
		
        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <ul class="nav navbar-nav navbar-left">
                    <li><a href="#"><span class="glyphicon glyphicon-user"></span> <%=uname%></a></li>
                </ul>
		<ul class="nav navbar-nav">
		  <li><a href="logged.jsp">Home</a></li>      
		  <li class="active"><a href="../FaultDetector/">Fault Detector</a></li>
		</ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="../FE-war/index.jsp?logout=<%=uname%>"><span class="glyphicon glyphicon-log-out"></span>  Logout</a></li>
                </ul>
	  </div>
        </nav>
	  
	<div class="container">
	
            <form id="offerForm">
                <div class="form-group">
                    <label for="CarID_OfferForm">Add Offer:</label>
                    <input type="text" id="CarID_OfferForm" class="form-control"  placeholder="Insert your car id:">
                </div>
                <input type="button" class="btn btn-success" value="Add Offer" id="button1"/>
            </form>
        
            <br>
	
            <form id="reservationForm">
                <h2>Reservation seat: </h2>

                <div class="form-group">
                    <label for="offerID">Insert Offer ID</label>
                    <input type="text" id="offerID" class="form-control"  placeholder="Insert Offer ID">
                </div>
                <div class="form-group">
                    <label for="passengerID">Insert Passenger ID:</label>
                    <input type="text" id="passengerID" class="form-control"  placeholder="Insert Passenger ID:">
                </div>
                <input type="button" class="btn btn-success" value="Add Reservation" id="button2"/>          
            </form>
      
            <br>	
	     
            <center>
                <table class="table table-striped table-hover table-bordered" id="table1">
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
            <br>
       
            <div>
                <h2>Status bar</h2>
                <p id="status"></p>
            </div>
	</div>	
    </body>
</html>
