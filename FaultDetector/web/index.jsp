<%-- 
    Document   : index
    Created on : 1-feb-2016, 14.04.31
    Author     : leandro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Fault Detector interface</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
       
        <!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

	<!-- Optional theme -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">
	
        <!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
        
        <%--Add jQuery library (remote)--%>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        
    </head>
    
    <body>
        
        <%String status=request.getParameter("status");%>
             
            <%--JQUERY INIT--%>
            <script>
                var stat="<%=status%>";
                if(stat==null || stat==""){
                    $(document).ready(function() {
                                $.ajax({
                                        url : 'FE_FaultDetector',
                                        type: 'POST',
                                        data : {
                                            status : 'INIT'
                                        },
                                        success : function(responseText) {
                                           window.location = 'index.jsp?status='+responseText;
                                           //document.getElementById("temp").value=responseText;
                                        }                            
                                });
                    });
                }
            </script>
            
            <%--JQUERY GET STATUS--%>
            <script>
                $(document).ready(function() {
                    $('#getStatus').click(function() {
                            $.ajax({
                                    url : 'FE_FaultDetector',
                                    type: 'POST',
                                    data : {
                                        status : 'GETSTATUS'
                                    },
                                    success : function(responseText) {
                                        $('#statusDiv').empty();
                                        $('#statusDiv').append(responseText);
                                    }                            
                            });
                    });
                });
            </script>
        <%String uname=(String)session.getAttribute("uname");%>
                
	<nav class="navbar navbar-inverse">
	  <div class="container-fluid">
              <center><h2><font color="white">Fault Detector</font></h2></center>
	  </div>
	</nav>	
	
        <div class="container">
        
        <%= (status==null || status.equalsIgnoreCase("STOP")) ?
        "<form action='FE_FaultDetector' method='post'>"+
            "<input type='submit' class='btn btn-success' value='START' name='start'/>"+
        "</form>":"" 
        %>
        
        <%= (status!=null && status.equalsIgnoreCase("RUN")) ?
        "<form action='FE_FaultDetector' method='post'>"+
            "<input type='submit' class='btn btn-danger' value='STOP' name='stop'/>"+
        "</form>":"" 
        %>
                    
        <%= (status!=null && status.equalsIgnoreCase("RUN")) ?
        "<input type='button' class='btn btn-info' value='GET STATUS' id='getStatus'/>":"" 
        %>
        </div>
            
        <br>
        
        <div id="statusDiv">
            
        </div>
        
    </body>
</html>
