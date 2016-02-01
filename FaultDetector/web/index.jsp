<%-- 
    Document   : index
    Created on : 1-feb-2016, 14.04.31
    Author     : leandro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<!DOCTYPE html>

<html>
    <head>
        <title>Fault Detector interface</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div><h1>FAULT DETECTOR INTERFACE</h1></div>
        
        <% String status=request.getParameter("status"); %>
        
        <%= (status==null || status.equalsIgnoreCase("STOP")) ?
        "<form action='FE_FaultDetector' method='post'>"+
            "<input type='submit' value='START' name='start'>"+
        "</form>":"" 
        %>
        
        <%= (status!=null && status.equalsIgnoreCase("RUN")) ?
        "<form action='FE_FaultDetector' method='post'>"+
            "<input type='submit' value='STOP' name='stop'>"+
        "</form>":"" 
        %>
                    
        <%= (status!=null && status.equalsIgnoreCase("RUN")) ?
        "<form action='FE_FaultDetector' method='post'>"+
            "<input type='submit' value='GET STATUS PROCESS' name='getStatus'>"+
        "</form>":"" 
        %>
        
    </body>
</html>
</html>
