<!DOCTYPE html>

<html>
    <head>
        <title>Car sharing login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

		<!-- Optional theme -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

		<!-- Latest compiled and minified JavaScript -->
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
    <style>
	
	body {
		  padding-top: 40px;
		  padding-bottom: 40px;
		  background-color: #eee;
                  
                  background-image: url("image/carsharing_opacity.png");
                  background-position: 10% 10%;
                  background-repeat: no-repeat;
                  /*background-attachment: fixed;*/
                  background-size: cover;
                  font-family:  'Oswald', sans-serif;
		}

		.form-signin {
		  max-width: 330px;
		  padding: 15px;
		  margin: 0 auto;
		}
		.form-signin .form-signin-heading,
		.form-signin .checkbox {
		  margin-bottom: 10px;
		}
		.form-signin .checkbox {
		  font-weight: normal;
		}
		.form-signin .form-control {
		  position: relative;
		  height: auto;
		  -webkit-box-sizing: border-box;
			 -moz-box-sizing: border-box;
				  box-sizing: border-box;
		  padding: 10px;
		  font-size: 16px;
		}
		.form-signin .form-control:focus {
		  z-index: 2;
		}
		.form-signin input[type="email"] {
		  margin-bottom: -1px;
		  border-bottom-right-radius: 0;
		  border-bottom-left-radius: 0;
		}
		.form-signin input[type="password"] {
		  margin-bottom: 10px;
		  border-top-left-radius: 0;
		  border-top-right-radius: 0;
		}
                .form-signin-heading {
                    text-align: center;
                }    
	</style>
    
    </head>
    
    <body>
        
        <% HttpSession mySession=request.getSession(); %>
        
        <% String logout=request.getParameter("logout");%>
        
        <% String login=request.getParameter("login");%>
        
        <% if(logout!=null){mySession.invalidate();}%>
        
        <%= (login!=null && login.equals("failed")) ?
            "<script>"+
                "alert('Username o password errati!');"+
                "window.location.href = 'index.jsp';"+
            "</script>"
            :
            ""
        %>
        
        <%= (login!=null && login.equals("lostSession")) ?
            "<script>"+
                "alert('La sessione è stata persa! Effettua nuovamente il login..');"+
                "window.location.href = 'index.jsp';"+
            "</script>"
            :
            ""
        %>
        
        <div class="container" >

          <form class="form-signin" action="FE_Servlet" method="POST">
            <h2 class="form-signin-heading">Car Sharing</h2>
            <label for="inputuname" class="sr-only">Username</label>
            <input type="text" name="uname" id="inputuname" class="form-control" placeholder="Username" required autofocus>
            <label for="inputPassword" class="sr-only">Password</label>
            <input type="password"  name="password" id="inputPassword" class="form-control" placeholder="Password" required>
            <div class="checkbox">
                <label>
                    <input type="checkbox" value="ricordami"> Ricordami
                </label>
            </div>
            <button class="btn btn-lg btn-primary btn-block" type="submit" >Login</button>
          </form>

        </div> <!-- /container -->
 

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script>
    </body>
</html>
